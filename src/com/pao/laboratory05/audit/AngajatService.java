package com.pao.laboratory05.audit;

import com.pao.laboratory05.angajati.Angajat;
import java.time.LocalDateTime;
import java.util.Arrays;

// AngajatService.java
public class AngajatService {
    private Angajat[] angajati = new Angajat[0];
    private AuditEntry[] auditLog = new AuditEntry[0];

    private AngajatService() {}

    private static class Holder {
        private static final AngajatService INSTANCE = new AngajatService();
    }

    public static AngajatService getInstance() {
        return Holder.INSTANCE;
    }

    private void logAction(String action, String target) {
        AuditEntry entry = new AuditEntry(action, target, LocalDateTime.now().toString());
        AuditEntry[] nou = new AuditEntry[auditLog.length + 1];
        System.arraycopy(auditLog, 0, nou, 0, auditLog.length);
        nou[auditLog.length] = entry;
        auditLog = nou;
    }

    public void addAngajat(Angajat a) {
        Angajat[] nou = new Angajat[angajati.length + 1];
        System.arraycopy(angajati, 0, nou, 0, angajati.length);
        nou[angajati.length] = a;
        angajati = nou;
        System.out.println("Adăugat: " + a);
        logAction("ADD", a.getNume());
    }

    public void printAll() {
        for (Angajat a : angajati) System.out.println(a);
    }

    public void listBySalary() {
        Angajat[] copy = angajati.clone();
        Arrays.sort(copy);
        for (Angajat a : copy) System.out.println(a);
    }

    public void findByDepartament(String numeDept) {
        logAction("FIND_BY_DEPT", numeDept);
        boolean gasit = false;
        for (Angajat a : angajati) {
            if (a.getDepartament().nume().equalsIgnoreCase(numeDept)) {
                System.out.println(a);
                gasit = true;
            }
        }
        if (!gasit) System.out.println("Niciun angajat în departamentul: " + numeDept);
    }

    public void printAuditLog() {
        for (AuditEntry entry : auditLog) {
            System.out.println("[" + entry.timestamp() + "] " + entry.action() + " → " + entry.target());
        }
    }
}