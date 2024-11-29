package rkzk.demo.tms.model.persistent;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.util.Arrays;
import java.util.Map;
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
    public enum PriorityEnum {
        LOW(1L, "not important"),
        HIGH(2L, "important");

        private final Long priorityId;
        private final String description;

        private Priority priority;

        PriorityEnum(Long priorityId, String description) {
            this.priorityId = priorityId;
            this.description = description;
        }

        public Priority getPriority() {
            if (priority == null) {
                throw new IllegalStateException(
                        "PriorityEnum not initialized. Ensure PriorityEnum is initialized during application startup.");
            }
            return priority;
        }

        public static Priority getById(Long id) {
            return Arrays.stream(values())
                    .filter(p -> Objects.equals(p.priorityId, id))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("No priority with such ID"))
                    .getPriority();
        }

        public static Priority getByDescription(String description) {
            return Arrays.stream(values())
                    .filter(p -> Objects.equals(p.description, description))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("No priority with such description"))
                    .getPriority();
        }

        public static void initialize(Map<Long, Priority> priorities) {
            for (PriorityEnum priorityEnum : values()) {
                Priority priority = priorities.get(priorityEnum.priorityId);
                if (priority == null || !priority.getDescription().equals(priorityEnum.description)) {
                    throw new IllegalStateException(
                            String.format("PriorityEnum mismatch for %s: DB contains invalid or missing value.",
                                    priorityEnum.name()));
                }
                priorityEnum.priority = priority;
            }
        }
    }
}
