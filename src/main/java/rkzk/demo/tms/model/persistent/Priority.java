package rkzk.demo.tms.model.persistent;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "priorities")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Priority {
    @Id
    @Column(name = "priority_id", nullable = false, unique = true)
    private Long priorityId;

    @Column(nullable = false, unique = true, length = 20)
    private String description;

    @Getter
    @RequiredArgsConstructor
    public enum PriorityEnum {
        LOW(new Priority(1L, "not important")),
        HIGH(new Priority(2L, "important"));

        private final Priority priority;
    }
}
