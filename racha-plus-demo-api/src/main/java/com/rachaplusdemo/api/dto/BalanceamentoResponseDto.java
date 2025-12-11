package com.rachaplusdemo.api.dto;

import java.util.List;

public record BalanceamentoResponseDto(
        List<TimeDto> times,
        Double diferencaForcaEntreTitulares
) {
}