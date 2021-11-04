package com.web.tip.project;

import com.web.tip.common.MemberHasTeam;
import com.web.tip.common.MemberHasTeamDao;
import com.web.tip.error.CustomException;
import com.web.tip.error.ErrorCode;
import com.web.tip.error.JpaErrorCode;
import com.web.tip.error.JpaException;
import com.web.tip.member.MemberDao;
import com.web.tip.project.response.ProjectResponse;
import com.web.tip.team.Team;
import com.web.tip.team.TeamDao;
import com.web.tip.todo.TodoDao;
import com.web.tip.util.IdGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
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

        // 사용자가 담당 중인 프로젝트 목록
        List<ProjectResponse> projectList = new ArrayList<>();

        for (MemberHasTeam mht : hasTeamList) {

            // 사용자가 속한 팀의 아이디를 읽어온다.
            String teamId = mht.getTeamId();

            // 팀의 아이디로 그 팀에 대한 정보를 불러온다.
            Team team = teamDao.findTeamById(teamId).orElseThrow(() -> new CustomException(ErrorCode.TEAM_NOT_FOUND));

            // 해당 팀이 존재하면 그 팀이 속해 있는 프로젝트의 아이디를 통해 프로젝트 정보를 가져온다.
            Project project = projectDao.findProjectById(team.getProjectId()).orElseThrow(() -> new CustomException(ErrorCode.PROJECT_NOT_FOUND));

            int[] countTodoStatus = countTodos(project);

            // 진행 중인 프로젝트 목록인지 완료된 프로젝트 목록인지 구분하여 projectList에 추가한다.
            if (project.isDone() == isDone) {
                projectList.add(ProjectResponse.entityToResponse(project, countTodoStatus));
            }

        }

        return projectList;
    }

    @Transactional
    public boolean addProject(ProjectDto projectDto) {

        try {
            // 새로운 프로젝트를 위한 id 생성
            String pid = idGenerator.generateId();
            while (projectDao.existsById(pid)) {
                pid = idGenerator.generateId();
            }
            // Dto로 받은 project를 Entity로 변경
            projectDto.setId(pid);

            Project project = ProjectAdaptor.dtoToEntity(projectDto);
            // 중복되지 않은 pid를 새로운 프로젝트 Entity인 project변수에 set

            // project table에 insert
            projectDao.save(project);

            return true;

        } catch (Exception e) {
            e.printStackTrace();

            return false;
        }

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
        int[] count = {0, 0, 0};
        List<String> status = todoDao.findStatusByProject(project);
        count[0] = status.size();

        int temp = 0;
        for (String sta : status) {
            if ("진행중".equals(sta))
                temp++;
        }
        count[1] = temp;
        count[2] = count[0] - count[1];

        return count;
    }
}
