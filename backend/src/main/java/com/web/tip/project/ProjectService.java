package com.web.tip.project;

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

    public Optional<Project> getProjectList(String nickname) {

        Optional<Member> memberOpt = memberDao.findMemberByNickname(nickname);
        if(memberOpt.isPresent()) {

            Member member = memberOpt.get();

            String myTeam = member.getTeamId();

            Team team = teamDao.findTeamById(myTeam).get();

            return projectDao.findProjectById(team.getProjectId());
        } else{
            return null;
        }

    }
}
