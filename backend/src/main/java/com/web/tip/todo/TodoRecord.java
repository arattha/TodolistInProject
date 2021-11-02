package com.web.tip.todo;

import com.vladmihalcea.hibernate.type.json.JsonStringType;
import lombok.*;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;

@Getter
@Builder
@ToString
@Entity
@NoArgsConstructor
@AllArgsConstructor
@TypeDef(name = "jsonb", typeClass = JsonStringType.class)
public class TodoRecord {
    @Id
    private String id;

    @Type(type = "jsonb")
    @Column(name = "diff", columnDefinition = "jsonb", nullable = false)
    private Map<String, String> diff = new HashMap<>();

    @JoinColumn(name = "todo_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Todo todo;
}
