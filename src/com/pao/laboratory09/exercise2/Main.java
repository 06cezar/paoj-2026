package com.pao.laboratory09.exercise2;

import com.pao.laboratory09.exercise1.TipTranzactie;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.*;

public class Main {
    private static final String OUTPUT_FILE = "output/lab09_ex2.bin";
    private static final int RECORD_SIZE = 32;
    private static final String[] STATUS_NAMES = {"PENDING", "PROCESSED", "REJECTED"};

    public static void main(String[] args) throws Exception {
        // TODO: Implementează conform Readme.md
        //
        // 1. Citește N din stdin, apoi cele N tranzacții (id suma data tip)
        // 2. Scrie toate înregistrările în OUTPUT_FILE cu DataOutputStream (format binar, RECORD_SIZE=32 bytes/înreg.)
        //    - bytes 0-3:   id (int, little-endian via ByteBuffer)
        //    - bytes 4-11:  suma (double, little-endian via ByteBuffer)
        //    - bytes 12-21: data (String, 10 chars ASCII, paddat cu spații la dreapta)
        //    - byte 22:     tip (0=CREDIT, 1=DEBIT)
        //    - byte 23:     status (0=PENDING, 1=PROCESSED, 2=REJECTED)
        //    - bytes 24-31: padding (zerouri)
        // 3. Procesează comenzile din stdin până la EOF cu RandomAccessFile:
        //    - READ idx       → seek(idx * RECORD_SIZE), citește și afișează înregistrarea
        //    - UPDATE idx ST  → seek(idx * RECORD_SIZE + 23), scrie noul status (0/1/2)
        //                       afișează "Updated [idx]: STATUS"
        //    - PRINT_ALL      → citește și afișează toate înregistrările
        //
        // Format linie output:
        //   [idx] id=<id> data=<data> tip=<CREDIT|DEBIT> suma=<suma:.2f> RON status=<STATUS>

        Scanner sc = new Scanner(System.in);

        // 1. Citește N tranzacții
        int n = Integer.parseInt(sc.nextLine().trim());

        int[] ids = new int[n];
        double[] sume = new double[n];
        String[] date = new String[n];
        TipTranzactie[] tipuri = new TipTranzactie[n];

        for (int i = 0; i < n; i++) {
            String[] parts = sc.nextLine().trim().split("\\s+");
            ids[i] = Integer.parseInt(parts[0]);
            sume[i] = Double.parseDouble(parts[1]);
            date[i] = parts[2];
            tipuri[i] = TipTranzactie.valueOf(parts[3]);
        }
        // 2. Scrie fișierul binar
        new File(OUTPUT_FILE).getParentFile().mkdirs();
        try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(OUTPUT_FILE))) {
            for (int i = 0; i < n; i++) {
                // id - 4 bytes little-endian
                dos.write(ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(ids[i]).array());
                // suma - 8 bytes little-endian
                dos.write(ByteBuffer.allocate(8).order(ByteOrder.LITTLE_ENDIAN).putDouble(sume[i]).array());
                // data - 10 bytes ASCII
                byte[] dataBytes = new byte[10];
                byte[] src = date[i].getBytes();
                System.arraycopy(src, 0, dataBytes, 0, src.length);
                dos.write(dataBytes);
                // tip - 1 byte
                dos.write(tipuri[i] == TipTranzactie.CREDIT ? 0 : 1);
                // status - 1 byte (PENDING = 0)
                dos.write(0);
                // padding - 8 bytes
                dos.write(new byte[8]);
            }
        }

        // 3. Procesează comenzi cu RandomAccessFile
        try (RandomAccessFile raf = new RandomAccessFile(OUTPUT_FILE, "rw")) {
            while (sc.hasNextLine()) {
                String line = sc.nextLine().trim();
                if (line.isEmpty()) continue;

                if (line.startsWith("READ ")) {
                    int idx = Integer.parseInt(line.substring(5).trim());
                    System.out.println(readRecord(raf, idx));

                } else if (line.startsWith("UPDATE ")) {
                    String[] parts = line.split("\\s+");
                    int idx = Integer.parseInt(parts[1]);
                    String statusName = parts[2];
                    int statusByte = Arrays.asList(STATUS_NAMES).indexOf(statusName);
                    raf.seek((long) idx * RECORD_SIZE + 23);
                    raf.write(statusByte);
                    System.out.println("Updated [" + idx + "]: " + statusName);

                } else if (line.equals("PRINT_ALL")) {
                    long total = raf.length() / RECORD_SIZE;
                    for (int i = 0; i < total; i++) {
                        System.out.println(readRecord(raf, i));
                    }
                }
            }
        }
    }

    private static String readRecord(RandomAccessFile raf, int idx) throws Exception {
        raf.seek((long) idx * RECORD_SIZE);
        byte[] bytes = new byte[RECORD_SIZE];
        raf.readFully(bytes);

        ByteBuffer buf = ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN);
        int id = buf.getInt();           // bytes 0-3
        double suma = buf.getDouble();   // bytes 4-11
        byte[] dataBytes = new byte[10];
        buf.get(dataBytes);              // bytes 12-21
        String data = new String(dataBytes).trim();
        int tipByte = buf.get() & 0xFF;  // byte 22
        int statusByte = buf.get() & 0xFF; // byte 23

        String tip = tipByte == 0 ? "CREDIT" : "DEBIT";
        String status = STATUS_NAMES[statusByte];

        return String.format("[%d] id=%d data=%s tip=%s suma=%.2f RON status=%s",
                idx, id, data, tip, suma, status);
    }
}
