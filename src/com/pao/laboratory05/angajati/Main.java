package com.pao.laboratory05.angajati;

import java.util.Scanner;

/**
 * Exercise 3 — Angajați
 *
 * Cerințele complete se află în:
 *   src/com/pao/laboratory05/Readme.md  →  secțiunea "Exercise 3 — Angajați"
 *
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
