package com.pao.laboratory03.bonus.service;

import com.pao.laboratory03.bonus.model.*;
import com.pao.laboratory03.bonus.exception.*;

import java.util.*;


public class TaskService {
    private static TaskService instance;

    private final Map<String, Task> tasksById;
    private final Map<Priority, List<Task>> tasksByPriority;
    private final List<String> auditLog;
    private int taskCounter;

    private TaskService() {
        tasksById = new HashMap<>();
        tasksByPriority = new HashMap<>();
        for (Priority p : Priority.values()) {
            tasksByPriority.put(p, new ArrayList<>());
        }
        auditLog = new ArrayList<>();
        taskCounter = 0;
    }

    public static TaskService getInstance() {
        if (instance == null) {
            instance = new TaskService();
        }
        return instance;
    }

    private String generateId() {
        taskCounter++;
        return String.format("T%03d", taskCounter);
    }

    public Task addTask(String title, Priority priority) {
        String id = generateId();
        if (tasksById.containsKey(id)) {
            throw new DuplicateTaskException("Task-ul cu id-ul '" + id + "' exista deja.");
        }
        Task task = new Task(id, title, priority);
        tasksById.put(id, task);
        tasksByPriority.get(priority).add(task);
        auditLog.add("[ADD] " + id + ": '" + title + "' (" + priority + ")");
        return task;
    }

    public void assignTask(String taskId, String assignee) {
        Task task = findById(taskId);
        task.setAssignee(assignee);
        auditLog.add("[ASSIGN] " + taskId + " → " + assignee);
    }

    public void changeStatus(String taskId, Status newStatus) {
        Task task = findById(taskId);
        Status current = task.getStatus();
        if (!current.canTransitionTo(newStatus)) {
            throw new InvalidTransitionException(current, newStatus);
        }
        task.setStatus(newStatus);
        auditLog.add("[STATUS] " + taskId + ": " + current + " → " + newStatus);
    }

    public Task findById(String taskId) {
        Task task = tasksById.get(taskId);
        if (task == null) {
            throw new TaskNotFoundException("Task-ul '" + taskId + "' nu a fost găsit.");
        }
        return task;
    }

    public List<Task> getTasksByPriority(Priority priority) {
        return tasksByPriority.getOrDefault(priority, new ArrayList<>());
    }

    public Map<Status, Long> getStatusSummary() {
        Map<Status, Long> summary = new HashMap<>();
        for (Status s : Status.values()) {
            summary.put(s, 0L);
        }
        for (Task task : tasksById.values()) {
            summary.put(task.getStatus(), summary.get(task.getStatus()) + 1);
        }
        return summary;
    }

    public List<Task> getUnassignedTasks() {
        List<Task> result = new ArrayList<>();
        for (Task task : tasksById.values()) {
            if (task.getAssignee() == null) {
                result.add(task);
            }
        }
        return result;
    }

    public void printAuditLog() {
        for (String entry : auditLog) {
            System.out.println(entry);
        }
    }

    public double getTotalUrgencyScore(int baseDays) {
        double total = 0;
        for (Task task : tasksById.values()) {
            if (task.getStatus() != Status.DONE && task.getStatus() != Status.CANCELLED) {
                total += task.getPriority().calculateScore(baseDays);
            }
        }
        return total;
    }
}
