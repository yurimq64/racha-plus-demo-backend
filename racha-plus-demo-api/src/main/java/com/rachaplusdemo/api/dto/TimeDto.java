package com.rachaplusdemo.api.dto;

import java.util.List;

public record TimeDto(
        String nome,
        List<MembroDto> jogadores,
        Double forcaTotal
) {
}
