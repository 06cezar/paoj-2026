package com.pao.laboratory05.audit;

import com.pao.laboratory05.angajati.Angajat;
import com.pao.laboratory05.audit.AngajatService;
import com.pao.laboratory05.angajati.Departament;

import java.util.Scanner;

/**
 * Exercise 4 (Bonus) — Audit Log
 *
 * Cerințele complete se află în:
 *   src/com/pao/laboratory05/Readme.md  →  secțiunea "Exercise 4 (Bonus) — Audit"
 *
 * Extinde soluția de la Exercise 3 cu un sistem de audit bazat pe record.
 * Creează fișierele de la zero în acest pachet, apoi rulează Main.java
 * pentru a verifica output-ul așteptat din Readme.
 */
public class Main {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        AngajatService service = AngajatService.getInstance();

        while (true) {
            System.out.println("\n===== Gestionare Angajați =====");
            System.out.println("1. Adaugă angajat");
            System.out.println("2. Listare după salariu");
            System.out.println("3. Caută după departament");
            System.out.println("4. Afiseaza audit log");
            System.out.println("0. Ieșire");
            System.out.print("Opțiune: ");

            String optiune = scanner.nextLine().trim();

            switch (optiune) {
                case "1":
                    System.out.print("Nume: ");
                    String nume = scanner.nextLine().trim();
                    System.out.print("Departament (nume): ");
                    String numeDept = scanner.nextLine().trim();
                    System.out.print("Locatie departament: ");
                    String locatie = scanner.nextLine().trim();
                    System.out.print("Salariu: ");
                    double salariu = scanner.nextDouble();
                    service.addAngajat(new Angajat(nume, new Departament(numeDept, locatie), salariu));
                    break;

                case "2":
                    service.listBySalary();
                    break;

                case "3":
                    System.out.print("Nume departament: ");
                    String dept = scanner.nextLine().trim();
                    service.findByDepartament(dept);
                    break;

                case "4":
                    service.printAuditLog();
                    break;

                case "0":
                    System.out.println("Pa pa!");
                    scanner.close();
                    return;

                default:
                    System.out.println("Optiune invalida.");
            }
        }
    }
}

