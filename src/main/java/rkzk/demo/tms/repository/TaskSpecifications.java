package rkzk.demo.tms.repository;

import org.springframework.data.jpa.domain.Specification;
import rkzk.demo.tms.model.Task;
import rkzk.demo.tms.model.persistent.Priority;
import rkzk.demo.tms.model.persistent.TaskStatus;

public class TaskSpecifications {

    public static Specification<Task> filterByOwner(Long ownerId) {
        return (root, query, criteriaBuilder) -> {
            if (ownerId == null) {
                return null;
            }
            return criteriaBuilder.equal(root.get("ownerId"), ownerId);
        };
    }

    public static Specification<Task> filterByExecutor(Long executorId) {
        return (root, query, criteriaBuilder) -> {
            if (executorId == null) {
                return null;
            }
            return criteriaBuilder.equal(root.get("executorId"), executorId);
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
