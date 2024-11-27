package rkzk.demo.tms.model.persistent;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

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

        public static Priority getById(Long id) {
            for (PriorityEnum priorityEnum : values()) {
                if (Objects.equals(priorityEnum.priority.priorityId, id))
                    return priorityEnum.priority;
            }
            throw new IllegalArgumentException("No priority with such value");
        }

        public static Priority getByDescription(String description) {
            for (PriorityEnum priorityEnum : values()) {
                if (Objects.equals(priorityEnum.priority.description, description))
                    return priorityEnum.priority;
            }
            throw new IllegalArgumentException("No priority with such name");
        }
    }
}
