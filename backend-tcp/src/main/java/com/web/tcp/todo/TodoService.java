package com.web.tcp.todo;

import com.web.tcp.util.IdGenerator;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@AllArgsConstructor
public class TodoService {

    private IdGenerator idGenerator;
    TodoDao todoDao;

    @Transactional
    public List<Todo> addTodo(TodoDto todoDto) {

        try{

            String tid = idGenerator.generateId();
            while(todoDao.existsById(tid)){
                tid = idGenerator.generateId();
            }

            Todo todo = Todo.builder()
                    .id(tid)
                    .title(todoDto.getTitle())
                    .status(todoDto.getStatus())
                    .project_id(todoDto.getProject_id())
                    .team_id(todoDto.getTeam_id())
                    .member_id(todoDto.getMember_id())
                    .modify_date(todoDto.getModify_date())
                    .reg_date(todoDto.getReg_date())
                    .build();

            todoDao.save(todo);

        } catch(Exception e){
            e.printStackTrace();
        }

        return todoDao.findAll();
    }
}
