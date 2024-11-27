package rkzk.demo.tms.repository;

import org.springframework.data.jpa.domain.Specification;
import rkzk.demo.tms.model.Task;
import rkzk.demo.tms.model.persistent.Priority;
import rkzk.demo.tms.model.persistent.TaskStatus;

public class TaskSpecifications {

    public static Specification<Task> filterByOwner(Long owner) {
        return (root, query, criteriaBuilder) -> {
            if (owner == null) {
                return null;
            }
            return criteriaBuilder.equal(root.get("ownerId"), owner);
        };
    }

    public static Specification<Task> filterByExecutor(Long executor) {
        return (root, query, criteriaBuilder) -> {
            if (executor == null) {
                return null;
            }
            return criteriaBuilder.equal(root.get("executorId"), executor);
        };
    }

    public static Specification<Task> filterByPriority(Priority priority) {
        return (root, query, criteriaBuilder) -> {
            if (priority == null) {
                return null;
            }
            return criteriaBuilder.equal(root.get("priority"), priority);
        };
    }

    public static Specification<Task> filterByStatus(TaskStatus status) {
        return (root, query, criteriaBuilder) -> {
            if (status == null) {
                return null;
            }
            return criteriaBuilder.equal(root.get("status"), status);
        };
    }
}
