package com.web.tip.team;

import com.web.tip.common.MemberHasTeam;
import com.web.tip.common.MemberHasTeamDao;
import com.web.tip.common.MemberHasTeamDto;
import com.web.tip.error.CustomException;
import com.web.tip.error.ErrorCode;
import com.web.tip.error.JpaErrorCode;
import com.web.tip.error.JpaException;
import com.web.tip.project.Project;
import com.web.tip.project.ProjectDao;
import com.web.tip.util.IdGenerator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@AllArgsConstructor
public class TeamService {

    TeamDao teamDao;
    ProjectDao projectDao;
    MemberHasTeamDao memberHasTeamDao;
    private IdGenerator idGenerator;

    @Transactional(readOnly = true)
    public Object getAllTeam(String projectId){

        List<TeamDto> result = new ArrayList<>();
        teamDao.findTeamByProjectIdAndIsUse(projectId,true).forEach(v -> result.add(TeamAdaptor.entityToDto(v)));

        return result;
    }

    @Transactional(readOnly = true)
    public Object getAllMembers(String projectId){//이미 팀체크가 끝난상태라 가정
        try {
            List<Team> teamList = teamDao.findTeamByProjectIdAndIsUse(projectId,true);//팀 리스트

            ArrayList<String> teamIdList = new ArrayList<>();
            teamList.forEach(v -> teamIdList.add(v.getId()));
            List<MemberHasTeam> byTeamIdIn = memberHasTeamDao.findByTeamIdInAndIsUse(teamIdList, true);//프로젝트 내부의 사람들

            if(byTeamIdIn.isEmpty()){
                return Collections.emptyList();
            }

            List<MemberHasTeamDto> result = new ArrayList<>();
            byTeamIdIn.forEach(v -> result.add(MemberHasTeamDto.entityToDto(v)));

            return result;

        } catch (DataAccessException e) {
            e.printStackTrace();
            throw new JpaException(JpaErrorCode.SAVE_TEAM_ERROR);
        }
    }

    @Transactional(readOnly=true)
    public Optional<TeamDto> getMyTeam(String projectId, String memberId){
        projectDao.findProjectById(projectId)
                .orElseThrow(() -> new CustomException(ErrorCode.PROJECT_NOT_FOUND));

        List<Team> teamList = teamDao.findTeamByProjectIdAndIsUse(projectId, true);
        List<String> teams = new ArrayList<>();
        for(Team team: teamList){
            teams.add(team.getId());
        }

        Optional<MemberHasTeam> memberHasTeam = memberHasTeamDao.findMemberHasTeamByMemberIdAndTeamIdInAndIsUse(memberId, teams, true);
        if(!memberHasTeam.isPresent())
            return Optional.empty();

        return Optional.of(TeamAdaptor.entityToDto(teamDao.findTeamById(memberHasTeam.get().getTeamId())
                .orElseThrow(() -> new CustomException(ErrorCode.TEAM_NOT_FOUND))));
    }

    @Transactional
    public boolean teamNameCheck(String projectId, String teamName){//이미 팀체크가 끝난상태라 가정
        if(teamDao.findTeamByProjectIdAndName(projectId,teamName).isPresent()){
            return false;
        } else {
            return true;
        }
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
            List<Team> teamList = teamDao.findTeamByProjectIdAndIsUse(projectId,true);//팀 리스트

            ArrayList<String> teamIdList = new ArrayList<>();
            teamList.forEach(v -> teamIdList.add(v.getId()));
            List<MemberHasTeam> byTeamIdIn = memberHasTeamDao.findByTeamIdInAndIsUse(teamIdList, true);//프로젝트 내부의 사람들

            for ( String m : teamMembers) {
                MemberHasTeam mt = new MemberHasTeam(m,teamId,true);
                for ( MemberHasTeam t : byTeamIdIn){
                    if(t.getMemberId().equals(m)){
                        t.setUse(false);
                        memberHasTeamDao.save(t);
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
            Team team = teamDao.findTeamById(teamId).orElseThrow(() -> new CustomException(ErrorCode.TEAM_NOT_FOUND));

            List<String> teamMembers = modificationTeamRequest.getMemberList();
            List<Team> teamList = teamDao.findTeamByProjectIdAndIsUse(team.getProjectId(),true);//팀 리스트

            ArrayList<String> teamIdList = new ArrayList<>();
            teamList.forEach(v -> teamIdList.add(v.getId()));
            List<MemberHasTeam> byTeamIdIn = memberHasTeamDao.findByTeamIdInAndIsUse(teamIdList, true);//프로젝트 내부의 사람들

            for ( String m : teamMembers) {
                MemberHasTeam mt = new MemberHasTeam(m,teamId, true);
                for ( MemberHasTeam t : byTeamIdIn){
                    if(t.getMemberId().equals(m)){
                        t.setUse(false);
                        memberHasTeamDao.save(t); //기존것 삭제
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
            teamDao.save(team);
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
            teamDao.save(team);
        } catch (DataAccessException e) {
            e.printStackTrace();
            throw new JpaException(JpaErrorCode.SAVE_TEAM_ERROR);
        }

        return true;

    }

}
