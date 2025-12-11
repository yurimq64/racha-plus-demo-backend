package com.rachaplusdemo.api.enums;

public enum Esporte {
    FUTEBOL(11),
    BASQUETE(5);

    private final int quantidadePorTime;

    Esporte(int quantidatePorTime) {
        this.quantidadePorTime = quantidatePorTime;
    }

    public int getQuantidatePorTime() {
        return quantidadePorTime;
    }
}
