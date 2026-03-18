package com.pao.laboratory03.exercise.service;

import com.pao.laboratory03.exercise.model.*;
import com.pao.laboratory03.exercise.exception.*;
import java.util.*;

public class StudentService {

    private static StudentService instance;
    private final List<Student> students;

    private StudentService() {
        students = new ArrayList<>();
    }

    public static StudentService getInstance() {
        if (instance == null) {
            instance = new StudentService();
        }
        return instance;
    }

    public void addStudent(String name, int age) {
        for (Student s : students) {
            if (Objects.equals(s.getName(), name)) throw new RuntimeException("Studentul '" + name + "' exista deja.");
        }
        students.add(new Student(name, age));
    }

    public Student findByName(String name) {
        for (Student s : students) {
            if (Objects.equals(s.getName(), name)) {
                return s;
            }
        }
        throw new StudentNotFoundException("Studentul '" + name + "' nu a fost găsit.");
    }

    public void addGrade(String studentName, Subject subject, double grade) {
        Student s = findByName(studentName);
        s.addGrade(subject, grade);
    }

    public void printAllStudents() {
        if (students.isEmpty()) {
            System.out.println("Nu exista studenti inregistrati.");
            return;
        }
        for (Student s : students) {
            System.out.println(s);
            for (Map.Entry<Subject, Double> entry : s.getGrades().entrySet()) {
                System.out.printf("s -> %.2f%n", entry.getKey().name(), entry.getValue());
            }
        }
    }

    public void printTopStudents() {
        if (students.isEmpty()) {
            System.out.println("Nu exista studenti inregistrati.");
            return;
        }
        List<Student> sorted = new ArrayList<>(students);
        sorted.sort((a, b) -> Double.compare(b.getAverage(), a.getAverage()));
        System.out.println("Top studenti (dupa medie):");
        int rank = 1;
        for (Student s : sorted) {
            System.out.println(rank + ". " + s);
            rank++;
        }
    }

    public Map<Subject, Double> getAveragePerSubject() {
        Map<Subject, List<Double>> accumulated = new HashMap<>();
        for (Subject sub : Subject.values()) {
            accumulated.put(sub, new ArrayList<>());
        }
        for (Student s : students) {
            for (Map.Entry<Subject, Double> entry : s.getGrades().entrySet()) {
                accumulated.get(entry.getKey()).add(entry.getValue());
            }
        }
        Map<Subject, Double> averages = new HashMap<>();
        for (Subject sub : Subject.values()) {
            List<Double> grades = accumulated.get(sub);
            if (!grades.isEmpty()) {
                double sum = 0;
                for (double g : grades) sum += g;
                averages.put(sub, sum / grades.size());
            }
        }
        return averages;
    }
}
