package com.web.tcp.todo;

import com.web.tcp.util.IdGenerator;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class TodoService {

    TodoDao todoDao;

    @Transactional
    public List<Todo> addTodo(TodoDto todoDto) {

        try{

            IdGenerator idGenerator = new IdGenerator();
            String tid = idGenerator.generateId();
            while(todoDao.existsById(tid)){
                tid = idGenerator.generateId();
            }

            Todo todo = Todo.builder()
                    .id(tid)
                    .title(todoDto.getTitle())
                    .status(todoDto.getStatus())
                    .projectId(todoDto.getProject_id())
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

    @Transactional
    public Object getTodo(String projectId) {

        List<TodoDto> todoDtoList = new ArrayList<>();
        try{
            TodoDto todoDto = new TodoDto();
            List<Todo> todoList = todoDao.findTodoByProjectId(projectId);
            for(Todo todo : todoList) {

                todoDto = todoDto.builder()
                        .id(todo.getId())
                        .title(todo.getTitle())
                        .status(todo.getStatus())
                        .project_id(todo.getProjectId())
                        .team_id(todo.getTeam_id())
                        .member_id(todo.getMember_id())
                        .modify_date(todo.getModify_date())
                        .reg_date(todo.getReg_date())
                        .build();

                todoDtoList.add(todoDto);
            }

        } catch (Exception e){
            e.printStackTrace();
        }

        return todoDtoList;
    }
}
