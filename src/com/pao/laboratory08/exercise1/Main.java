package com.pao.laboratory08.exercise1;

import java.io.*;
import java.util.*;

public class Main {
    private static final String FILE_PATH = "src/com/pao/laboratory08/tests/studenti.txt";

    public static void main(String[] args) throws Exception {
        List<Student> studenti = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String linie;
            while ((linie = br.readLine()) != null) {
                if (linie.isBlank()) continue;
                String[] parts = linie.split(",");
                String nume = parts[0].trim();
                int varsta = Integer.parseInt(parts[1].trim());
                String oras = parts[2].trim();
                String strada = parts[3].trim();
                studenti.add(new Student(nume, varsta, new Adresa(oras, strada)));
            }
        }

        BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
        String comanda = stdin.readLine().trim();
        String[] tokens = comanda.split(" ", 2);
        String tip = tokens[0];

        if (tip.equals("PRINT")) {
            for (Student s : studenti) {
                System.out.println(s);
            }
        } else if (tip.equals("SHALLOW") || tip.equals("DEEP")) {
            String nume = tokens[1].trim();
            Student original = studenti.stream()
                    .filter(s -> s.getNume().equals(nume))
                    .findFirst()
                    .orElseThrow();

            Student clona = tip.equals("SHALLOW")
                    ? original.shallowClone()
                    : original.deepClone();

            clona.getAdresa().setOras("MODIFICAT");

            System.out.println("Original: " + original);
            System.out.println("Clona: " + clona);
        }
    }
}