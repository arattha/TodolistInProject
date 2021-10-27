package com.web.tip.team;

import com.web.tip.project.Project;
import com.web.tip.project.ProjectDao;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class TeamService {

    TeamDao teamDao;
    ProjectDao projectDao;

    public Object getAllTeam(String projectName){

        Optional<Project> project = projectDao.findProjectByName(projectName);

        System.out.println(project.get().getId());
        if(project.isPresent()){
            return teamDao.findTeamByProjectId(project.get().getId());
        }

        return null;
    }


}
