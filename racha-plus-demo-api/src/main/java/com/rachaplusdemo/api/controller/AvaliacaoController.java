package com.rachaplusdemo.api.controller;

import com.rachaplusdemo.api.dto.AtualizarRatingDto;
import com.rachaplusdemo.api.service.AvaliacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/avaliacoes")
public class AvaliacaoController {

    @Autowired
    private AvaliacaoService avaliacaoService;

    @PostMapping("/racha/{rachaId}/jogador/{id}")
    public ResponseEntity<Void> avaliar(@PathVariable Long rachaId, @PathVariable Long id, @RequestBody AtualizarRatingDto dto) {
        String emailAvaliador = SecurityContextHolder.getContext().getAuthentication().getName();

        avaliacaoService.avaliarJogador(rachaId, id, dto.novaNota(), emailAvaliador);

        return ResponseEntity.ok().build();
    }
}
