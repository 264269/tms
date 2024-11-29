package rkzk.demo.tms.config;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import rkzk.demo.tms.model.persistent.Priority;
import rkzk.demo.tms.repository.PriorityRepository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class PriorityInitializer {

    @Autowired
    private final PriorityRepository priorityRepository;

    @PostConstruct
    public void init() {
        List<Priority> priorities = priorityRepository.findAll();

        Map<Long, Priority> priorityMap = priorities.stream()
                .collect(Collectors.toMap(Priority::getPriorityId, p -> p));

        Priority.PriorityEnum.initialize(priorityMap);
    }
}
