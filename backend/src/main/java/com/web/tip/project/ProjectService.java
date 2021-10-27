package com.web.tip.project;

import com.web.tip.common.MemberHasTeam;
import com.web.tip.common.MemberHasTeamDao;
import com.web.tip.member.Member;
import com.web.tip.member.MemberDao;
import com.web.tip.team.Team;
import com.web.tip.team.TeamDao;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ProjectService {

    ProjectDao projectDao;
    TeamDao teamDao;
    MemberDao memberDao;
    MemberHasTeamDao memberHasTeamDao;

    public List<ProjectDto> getProjectList(String nickname) {

        Optional<Member> memberOpt = memberDao.findMemberByNickname(nickname);
        if(memberOpt.isPresent()) {

            Member member = memberOpt.get();

            // 사용자의 Id를 읽어온다.
            String memberId = member.getId();

            // 읽어온 사용자의 Id로 사용자가 속해있는 팀의 목록을 불러온다.
            List<MemberHasTeam> MHTList = memberHasTeamDao.findMemberHasTeamByMemberId(memberId);

            // 사용자가 담당 중인 프로젝트 목록
            List<ProjectDto> projectList = new ArrayList<>();

            for(MemberHasTeam mht: MHTList) {

                // 사용자가 속한 팀의 아이디를 읽어온다.
                String teamId = mht.getTeamId();

                // 팀의 아이디로 그 팀에 대한 정보를 불러온다.
                Optional<Team> teamOpt = teamDao.findTeamById(teamId);


                if(teamOpt.isPresent()){
                    // 해당 팀이 존재하면 그 팀이 속해 있는 프로젝트의 아이디를 통해 프로젝트 정보를 projectList에 추가한다.
                    Project project = projectDao.findProjectById(teamOpt.get().getProjectId()).get();
                    projectList.add(ProjectAdaptor.entityToDto(project));

                } else {
                    return null;
                }

            }

            return projectList;
        } else{
            return null;
        }

    }
}
