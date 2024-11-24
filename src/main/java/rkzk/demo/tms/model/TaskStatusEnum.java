package rkzk.demo.tms.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TaskStatusEnum {
    CREATED("created", 1L),
    EXECUTING("executing", 2L),
    FINISHED("finished", 3L);

    private final String description;
    private final Long id;

    public static TaskStatusEnum fromDescription(String description) {
        for (TaskStatusEnum taskStatus : TaskStatusEnum.values()) {
            if (taskStatus.description.equalsIgnoreCase(description)) {
                return taskStatus;
            }
        }
        throw new IllegalArgumentException("Unknown role: " + description);
    }

    public static TaskStatusEnum fromTaskStatus(TaskStatus taskStatus) {
        return fromDescription(taskStatus.getDescription());
    }
}
