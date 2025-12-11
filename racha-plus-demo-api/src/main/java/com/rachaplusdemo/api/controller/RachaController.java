package com.rachaplusdemo.api.controller;

import com.rachaplusdemo.api.dto.BalanceamentoResponseDto;
import com.rachaplusdemo.api.dto.RachaDto;
import com.rachaplusdemo.api.dto.VincularJogadorDto;
import com.rachaplusdemo.api.model.Racha;
import com.rachaplusdemo.api.service.BalanceamentoService;
import com.rachaplusdemo.api.service.RachaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/rachas")
public class RachaController {

    @Autowired
    private RachaService rachaService;

    @Autowired
    private BalanceamentoService balanceamentoService;

    @PostMapping
    public ResponseEntity<Racha> criar(@RequestBody RachaDto dto) {
        String emailUsuarioLogado = SecurityContextHolder.getContext().getAuthentication().getName();

        Racha rachaCriado = rachaService.criarRacha(dto, emailUsuarioLogado);

        return ResponseEntity.status(201).body(rachaCriado);
    }

    @GetMapping
    public ResponseEntity<Set<Racha>> listarMeusRachas() {
        String emailUsuarioLogado = SecurityContextHolder.getContext().getAuthentication().getName();

        Set<Racha> meusRachas = rachaService.listarMeusRachas(emailUsuarioLogado);

        return ResponseEntity.ok(meusRachas);
    }

    @PostMapping("/{id}/membros")
    public ResponseEntity<Void> adicionarJogador(@PathVariable Long id, @RequestBody VincularJogadorDto dto) {
        String emailUsuarioLogado = SecurityContextHolder.getContext().getAuthentication().getName();

        rachaService.adicionarJogador(id, dto.email(), emailUsuarioLogado);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/balancear")
    public ResponseEntity<BalanceamentoResponseDto> balancearTimes(@PathVariable Long id) {
        String emailUsuario = SecurityContextHolder.getContext().getAuthentication().getName();

        BalanceamentoResponseDto times = balanceamentoService.balancearTimes(id, emailUsuario);

        return ResponseEntity.ok(times);
    }
}
