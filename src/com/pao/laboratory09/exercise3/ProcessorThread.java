package com.pao.laboratory09.exercise3;

public class ProcessorThread implements Runnable {
    public volatile boolean activ = true;
    private final CoadaTranzactii coada;

    public ProcessorThread(CoadaTranzactii coada) {
        this.coada = coada;
    }

    @Override
    public void run() {
        while (activ) {
            try {
                Tranzactie t = coada.extrage();
                System.out.printf("[Processor] Factura #%d - %.2f RON | %s%n", t.id, t.suma, t.data);
                Thread.sleep(80);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}