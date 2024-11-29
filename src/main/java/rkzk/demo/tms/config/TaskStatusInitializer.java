package rkzk.demo.tms.config;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import rkzk.demo.tms.repository.TaskStatusRepository;
import rkzk.demo.tms.model.persistent.TaskStatus;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class TaskStatusInitializer {

    @Autowired
    private final TaskStatusRepository taskStatusRepository;

    @PostConstruct
    public void init() {
        List<TaskStatus> statuses = taskStatusRepository.findAll();

        Map<Long, TaskStatus> taskStatusMap = statuses.stream()
                .collect(Collectors.toMap(TaskStatus::getStatusId, s -> s));

        TaskStatus.TaskStatusEnum.initialize(taskStatusMap);
    }
}
