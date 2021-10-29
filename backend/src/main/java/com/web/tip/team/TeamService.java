package com.web.tip.team;

import com.web.tip.common.MemberHasTeam;
import com.web.tip.common.MemberHasTeamDao;
import com.web.tip.error.CustomException;
import com.web.tip.error.ErrorCode;
import com.web.tip.error.JpaErrorCode;
import com.web.tip.error.JpaException;
import com.web.tip.project.ProjectDao;
import com.web.tip.util.IdGenerator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class TeamService {

    TeamDao teamDao;
    ProjectDao projectDao;
    MemberHasTeamDao memberHasTeamDao;
    private IdGenerator idGenerator;

    @Transactional
    public Object getAllTeam(String projectId){
        List<TeamDto> result = new ArrayList<>();
        teamDao.findTeamByProjectId(projectId).forEach(v -> result.add(TeamAdaptor.entityToDto(v)));
        return result;
    }

    @Transactional
    public boolean createProjectTeam(CreationTeamRequest newRequestTeam){//이미 팀체크가 끝난상태라 가정

        String projectId = newRequestTeam.getProjectId();
        String teamId = idGenerator.generateId();

        while (teamDao.existsById(teamId)){
            teamId = idGenerator.generateId();
        }

        try {
            teamDao.save(new Team(teamId,newRequestTeam.getTeamName(),projectId,true)); //새 팀 생성

            List<String> teamMembers = newRequestTeam.getMemberList();
            List<Team> teamList = teamDao.findTeamByProjectId(projectId);//팀 리스트

            ArrayList<String> teamIdList = new ArrayList<>();
            teamList.forEach(v -> teamIdList.add(v.getId()));
            List<MemberHasTeam> byTeamIdIn = memberHasTeamDao.findByTeamIdIn(teamIdList);//프로젝트 내부의 사람들

            for ( String m : teamMembers) {
                MemberHasTeam mt = new MemberHasTeam(m,teamId);
                for ( MemberHasTeam t : byTeamIdIn){
                    if(t.getMemberId().equals(m)){
                        memberHasTeamDao.delete(t);
                        break;
                    }
                }
                memberHasTeamDao.save(mt);
            }
        } catch (DataAccessException e) {
            e.printStackTrace();
            throw new JpaException(JpaErrorCode.SAVE_TEAM_ERROR);
        }

        return true;

    }

    @Transactional
    public boolean modifyProjectTeamMember(ModificationTeamRequest modificationTeamRequest){
        String teamId = modificationTeamRequest.getTeamId();

        try {
            Team team = teamDao.findById(teamId).orElseThrow(() -> new CustomException(ErrorCode.TEAM_NOT_FOUND));

            List<String> teamMembers = modificationTeamRequest.getMemberList();
            List<Team> teamList = teamDao.findTeamByProjectId(team.getProjectId());//팀 리스트

            ArrayList<String> teamIdList = new ArrayList<>();
            teamList.forEach(v -> teamIdList.add(v.getId()));
            List<MemberHasTeam> byTeamIdIn = memberHasTeamDao.findByTeamIdIn(teamIdList);//프로젝트 내부의 사람들

            for ( String m : teamMembers) {
                MemberHasTeam mt = new MemberHasTeam(m,teamId);
                for ( MemberHasTeam t : byTeamIdIn){
                    if(t.getMemberId().equals(m)){
                        memberHasTeamDao.delete(t); //기존것 삭제
                        break;
                    }
                }
                memberHasTeamDao.save(mt);
            }
        } catch (DataAccessException e) {
            e.printStackTrace();
            throw new JpaException(JpaErrorCode.SAVE_TEAM_ERROR);
        }

        return true;

    }

    @Transactional
    public boolean modifyProjectTeamName(TeamDto teamDto){

        String teamId = teamDto.getId();

        try {
            Team team = teamDao.findById(teamId).orElseThrow(() -> new CustomException(ErrorCode.TEAM_NOT_FOUND));
            team.changeName(teamDto.getName());
            teamDao.save(team); //삭제
        } catch (DataAccessException e) {
            e.printStackTrace();
            throw new JpaException(JpaErrorCode.SAVE_TEAM_ERROR);
        }

        return true;

    }

    @Transactional
    public boolean deleteProjectTeam(String teamId){
        try {
            Team team = teamDao.findById(teamId).orElseThrow(() -> new CustomException(ErrorCode.TEAM_NOT_FOUND));
            team.deleteTeam();
            teamDao.save(team); //삭제
        } catch (DataAccessException e) {
            e.printStackTrace();
            throw new JpaException(JpaErrorCode.SAVE_TEAM_ERROR);
        }

        return true;

    }

}
