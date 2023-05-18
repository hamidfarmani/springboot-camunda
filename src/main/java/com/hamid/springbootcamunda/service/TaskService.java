package com.hamid.springbootcamunda.service;

import com.hamid.springbootcamunda.dto.TaskDto;
import com.hamid.springbootcamunda.model.Task;
import com.hamid.springbootcamunda.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;

    public List<TaskDto> getAllTasks() {
        List<Task> tasks = taskRepository.findAll();
        return tasks.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public TaskDto createTask(TaskDto taskDTO) {
        Task task = convertToEntity(taskDTO);
        Task savedTask = taskRepository.save(task);
        return convertToDto(savedTask);
    }

    public TaskDto markAsDone(String id) {
        Task task = taskRepository.findById(Long.valueOf(id)).orElseThrow(RuntimeException::new);
        task.setStatus("Done");
        return convertToDto(task);
    }

    private TaskDto convertToDto(Task task) {
        return new TaskDto(
                task.getTitle(),
                task.getDescription(),
                task.getStatus(),
                task.getDueDate()
        );
    }

    private Task convertToEntity(TaskDto taskDTO) {
        return new Task(null,
                taskDTO.getTitle(),
                taskDTO.getDescription(),
                taskDTO.getStatus(),
                taskDTO.getDueDate()
        );
    }


}
