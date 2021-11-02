package com.web.tcp.todoRecord;

import com.vladmihalcea.hibernate.type.json.JsonStringType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TypeDef(name = "jsonb", typeClass = JsonStringType.class)
public class TodoRecord {

    @Id
    private String id;

    private String todo_id;

    @Column(insertable = false)
    private LocalDateTime modifyDate;

    @Type(type = "jsonb")
    @Column(name = "diff", columnDefinition = "jsonb", nullable = false)
    private Map<String, String> diff = new HashMap<>();
}
