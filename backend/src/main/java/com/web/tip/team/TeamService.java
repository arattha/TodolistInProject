package com.web.tip.team;

import ch.qos.logback.core.net.SyslogOutputStream;
import com.web.tip.common.MemberHasTeam;
import com.web.tip.common.MemberHasTeamDao;
import com.web.tip.project.Project;
import com.web.tip.project.ProjectDao;
import com.web.tip.util.GeneratePK;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class TeamService {

    TeamDao teamDao;
    ProjectDao projectDao;
    MemberHasTeamDao memberHasTeamDao;

    @Transactional
    public Object getAllTeam(String projectName){

        Optional<Project> project = projectDao.findProjectByName(projectName);

        System.out.println(project.get().getId());
        if(project.isPresent()){
            return teamDao.findTeamByProjectId(project.get().getId());
        }

        return null;
    }

    @Transactional
    public boolean createProjectTeam(CreationTeamRequest newRequestTeam){//이미 팀체크가 끝난상태라 가정

        Optional<Project> p  = projectDao.findProjectByName(newRequestTeam.getProjectName());
        String projectId = p.get().getId();//프로젝트 id 가져오기
        GeneratePK rnd = new GeneratePK();
        String teamId = "";
        while (true){
            teamId = rnd.generateKey();
            if(!teamDao.existsById(teamId)) break;
        }

        Team newTeam = new Team(teamId,newRequestTeam.getTeamName(),projectId,true);
        try {
            teamDao.save(newTeam); //새팀 생성
        } catch (Exception e ){
            return false;
        }

        List<String> teamMembers = newRequestTeam.getMemberList(); //
        List<Team> teamList = teamDao.findTeamByProjectId(projectId);//팀 리스트

        //List<String> teamIdList = new ArrayList<>();
        //teamList.forEach(v -> teamIdList.add(v.getProjectId()));
        //List<MemberHasTeam> alreadyMember = memberHasTeamDao.findByTeamIdIn(teamIdList);

        for ( String m : teamMembers) {
            MemberHasTeam mt = new MemberHasTeam(m,teamId);
            for ( Team t : teamList){
                Optional<MemberHasTeam> temp = memberHasTeamDao.findMemberHasTeamByMemberIdAndTeamId(m,t.getId());
                if(temp.isPresent()){
                    try{
                        memberHasTeamDao.delete(temp.get()); //기존것 삭제
                    } catch (Exception e ){
                        return false;
                    }
                    break;
                }
            }
            try {
                memberHasTeamDao.save(mt);
            } catch (Exception e){
                return false;
            }
        }

        return true;
    }


}
