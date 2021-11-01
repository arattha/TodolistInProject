package com.web.tip.todo.content.record;

import com.vladmihalcea.hibernate.type.json.JsonStringType;
import com.web.tip.todo.content.TodoContent;
import lombok.*;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Getter
@Builder
@ToString
@Entity
@NoArgsConstructor
@AllArgsConstructor
@TypeDef(name = "jsonb", typeClass = JsonStringType.class)
public class TodoContentRecord {
    @Id
    private String id;

    @Type(type = "jsonb")
    @Column(name = "diff", columnDefinition = "jsonb", nullable = false)
    private Map<String, String> diff = new HashMap<>();

    @JoinColumn(name = "todo_content_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private TodoContent todoContent;

    @Column(name = "modify_date")
    private LocalDateTime modifyDate;
}
