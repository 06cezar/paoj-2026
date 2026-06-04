package com.pao.project.fooddelivery.service;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;

public class AuditService {
    private static AuditService instance;
    private static final String AUDIT_FILE = "audit.csv";

    private AuditService() {}

    public static synchronized AuditService getInstance() {
        if (instance == null) {
            instance = new AuditService();
        }
        return instance;
    }

    public synchronized void log(String actionName) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(AUDIT_FILE, true))) {
            writer.println(actionName + "," + LocalDateTime.now());
        } catch (IOException e) {
            System.err.println("[AUDIT ERROR] Nu s-a putut scrie in audit.csv: " + e.getMessage());
        }
    }
}
