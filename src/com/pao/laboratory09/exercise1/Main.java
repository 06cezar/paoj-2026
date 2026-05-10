package com.pao.laboratory09.exercise1;

import java.io.*;
import java.util.*;

public class Main {
    private static final String OUTPUT_FILE = "output/lab09_ex1.ser";

    public static void main(String[] args) throws Exception {

        // TODO: Implementează conform Readme.md
        //
        // 1. Citește N din stdin, apoi cele N tranzacții (id suma data contSursa contDestinatie tip)
        // 2. Setează câmpul note = "procesat" pe fiecare tranzacție înainte de serializare
        // 3. Serializează lista de tranzacții în OUTPUT_FILE cu ObjectOutputStream (try-with-resources)
        // 4. Deserializează lista din OUTPUT_FILE cu ObjectInputStream (try-with-resources)
        // 5. Procesează comenzile din stdin până la EOF:
        //    - LIST          → afișează toate tranzacțiile, câte una pe linie
        //    - FILTER yyyy-MM → afișează tranzacțiile cu data care începe cu yyyy-MM
        //                       sau "Niciun rezultat." dacă nu există
        //    - NOTE id        → afișează "NOTE[id]: <valoarea câmpului note>"
        //                       sau "NOTE[id]: not found" dacă id-ul nu există
        //
        // Format linie tranzacție:
        //   [id] data tip: suma RON | contSursa -> contDestinatie
        //   Ex: [1] 2024-01-15 CREDIT: 1500.00 RON | RO01SRC1 -> RO01DST1
        Scanner sc = new Scanner(System.in);

        // 1. Citește N tranzacții
        int n = Integer.parseInt(sc.nextLine().trim());
        List<Tranzactie> tranzactii = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            String[] parts = sc.nextLine().trim().split("\\s+");
            int id = Integer.parseInt(parts[0]);
            double suma = Double.parseDouble(parts[1]);
            String data = parts[2];
            String contSursa = parts[3];
            String contDestinatie = parts[4];
            TipTranzactie tip = TipTranzactie.valueOf(parts[5]);

            Tranzactie t = new Tranzactie(id, suma, data, contSursa, contDestinatie, tip);
            t.setNote("procesat"); // 2. setează note înainte de serializare
            tranzactii.add(t);
        }

        // 3. Serializare
        new File(OUTPUT_FILE).getParentFile().mkdirs();
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(OUTPUT_FILE))) {
            oos.writeObject(tranzactii);
        }
        // 4. Deserializare
        List<Tranzactie> loaded;
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(OUTPUT_FILE))) {
            loaded = (List<Tranzactie>) ois.readObject();
        }

        // Indexare după id pentru NOTE
        Map<Integer, Tranzactie> byId = new HashMap<>();
        for (Tranzactie t : loaded) byId.put(t.getId(), t);

        // 5. Procesare comenzi
        while (sc.hasNextLine()) {
            String line = sc.nextLine().trim();
            if (line.isEmpty()) continue;

            if (line.equals("LIST")) {
                for (Tranzactie t : loaded) {
                    System.out.println(t);
                }
            } else if (line.startsWith("FILTER ")) {
                String prefix = line.substring(7).trim();
                boolean found = false;
                for (Tranzactie t : loaded) {
                    if (t.getData().startsWith(prefix)) {
                        System.out.println(t);
                        found = true;
                    }
                }
                if (!found) System.out.println("Niciun rezultat.");
            } else if (line.startsWith("NOTE ")) {
                int id = Integer.parseInt(line.substring(5).trim());
                Tranzactie t = byId.get(id);
                if (t == null) {
                    System.out.println("NOTE[" + id + "]: not found");
                } else {
                    System.out.println("NOTE[" + id + "]: " + t.getNote());
                }
            }
        }
    }
}

