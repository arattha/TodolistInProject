package com.web.tip.project;

import com.web.tip.common.MemberHasTeam;
import com.web.tip.common.MemberHasTeamDao;
import com.web.tip.error.CustomException;
import com.web.tip.error.ErrorCode;
import com.web.tip.error.JpaErrorCode;
import com.web.tip.error.JpaException;
import com.web.tip.member.MemberDao;
import com.web.tip.project.request.ProjectRequest;
import com.web.tip.project.response.ProjectResponse;
import com.web.tip.team.Team;
import com.web.tip.team.TeamDao;
import com.web.tip.todo.Todo;
import com.web.tip.todo.TodoDao;
import com.web.tip.util.IdGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectDao projectDao;
    private final TeamDao teamDao;
    private final MemberDao memberDao;
    private final MemberHasTeamDao memberHasTeamDao;
    private final TodoDao todoDao;

    private final IdGenerator idGenerator;

    @Transactional
    public List<ProjectResponse> getProjectList(String memberId, boolean isDone) {

        // 읽어온 사용자의 Id로 사용자가 속해있는 팀의 목록을 불러온다.
        List<MemberHasTeam> hasTeamList = memberHasTeamDao.findMemberHasTeamByMemberId(memberId);
        List<String> teamIdList = new ArrayList<>();
        hasTeamList.forEach(v -> teamIdList.add(v.getTeamId()));//id 만 String으로 변경

        //해당 팀이 존재하는 프로젝트의 리스트를 불러온다.
        List<Team> teamList = teamDao.findTeamByIdIn(teamIdList);

        List<String> projectIdList = new ArrayList<>();
        teamList.forEach(v -> projectIdList.add(v.getProjectId()));

        //프로젝트의 아이디를 토대로 데이터를 불러온다.
        List<Project> projectList = projectDao.findProjectByIdInAndIsDone(projectIdList, isDone);

        List<ProjectResponse> result = new ArrayList<>();
        projectList.forEach(v -> result.add(ProjectResponse.entityToResponse(v, countTodos(v))));

        return result;
    }

    @Transactional
    public boolean addProject(ProjectDto projectDto, String memberId) {

        try {
            // 1. 프로젝트를 생성
            // 2. 생성한 사용자를 해당 프로젝트의 admin팀의 일원으로 저장

            // 1.
            // 새로운 프로젝트를 위한 id 생성
            String pid = idGenerator.generateId();
            while (projectDao.existsById(pid)) {
                pid = idGenerator.generateId();
            }
            // Dto로 받은 project를 Entity로 변경
            projectDto.setId(pid);
            projectDto.setRegDate(LocalDateTime.now());

            Project project = ProjectAdaptor.dtoToEntity(projectDto);
            // 중복되지 않은 pid를 새로운 프로젝트 Entity인 project변수에 set

            // project table에 insert
            projectDao.save(project);

            // 2.
            String tid = idGenerator.generateId();
            while (teamDao.existsById(tid)) {
                tid = idGenerator.generateId();
            }

            Team team = new Team(tid, "admin", pid, false);
            teamDao.save(team);

            MemberHasTeam memberHasTeam = new MemberHasTeam(memberId, tid, true);
            memberHasTeamDao.save(memberHasTeam);

            return true;

        } catch (Exception e) {
            e.printStackTrace();

            return false;
        }

    }

    @Transactional
    public boolean modifyProject(ProjectRequest projectRequest) {
        // 프로젝트 이름으로 완료시킬 프로젝트 Entity를 가져온다.
        Project project = projectDao.findById(projectRequest.getId()).orElseThrow(() -> new CustomException(ErrorCode.PROJECT_NOT_FOUND));
        project.changeInfo(projectRequest.getName(),projectRequest.getDesc(),projectRequest.getStartDate(),projectRequest.getEndDate());

        try {
            projectDao.save(project);
        } catch (DataAccessException e) {
            throw new JpaException(JpaErrorCode.SAVE_PROJECT_ERROR);
        }
        return true;
    }

    @Transactional
    public boolean finishProject(String projectId) {
        // 프로젝트 이름으로 완료시킬 프로젝트 Entity를 가져온다.
        Project project = projectDao.findById(projectId).orElseThrow(() -> new CustomException(ErrorCode.PROJECT_NOT_FOUND));
        project.changeProjectState();
        try {
            projectDao.save(project);
        } catch (DataAccessException e) {
            throw new JpaException(JpaErrorCode.SAVE_PROJECT_ERROR);
        }
        return true;
    }

    @Transactional
    public boolean existsProjectCheck(String projectName) {
        return projectDao.existsByName(projectName);
    }

    @Transactional
    public Project getProjectByProjectName(String projectName) {
        return projectDao.findProjectByName(projectName)
                .orElseThrow(() -> new CustomException(ErrorCode.PROJECT_NOT_FOUND));
    }

    private int[] countTodos(Project project) {
        List<Todo> todos = todoDao.findTodosByProjectId(project.getId());

        int total = 0;
        int done = 0;
        for (Todo todo : todos) {
            if ("진행하지않음".equals(todo.getStatus())) {
                continue;
            }
            total++;
            if ("완료".equals(todo.getStatus())) {
                done++;
            }
        }

        return new int[]{total, total - done, done};
    }
}
