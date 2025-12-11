package com.rachaplusdemo.api.dto;

import com.rachaplusdemo.api.enums.Esporte;

public record RachaDto(
        String nome,
        Esporte esporte
) {
}
