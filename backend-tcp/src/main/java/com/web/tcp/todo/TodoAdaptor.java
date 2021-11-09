package com.web.tcp.todo;

public class TodoAdaptor {

    public static TodoDto entityToDto(Todo todo){
        return TodoDto.builder()
                .id(todo.getId())
                .title(todo.getTitle())
                .status(todo.getStatus())
                .memberId(todo.getMemberId())
                .teamId(todo.getTeamId())
                .projectId(todo.getProjectId())
                .modifyDate(todo.getModifyDate())
                .regDate(todo.getRegDate())
                .build();
    }

    public static Todo todoToEntity(TodoDto todoDto){
        return Todo.builder()
                .title(todoDto.getTitle())
                .status(todoDto.getStatus())
                .projectId(todoDto.getProjectId())
                .teamId(todoDto.getTeamId())
                .memberId(todoDto.getMemberId())
                .modifyDate(todoDto.getModifyDate())
                .regDate(todoDto.getRegDate())
                .build();
    }
}
