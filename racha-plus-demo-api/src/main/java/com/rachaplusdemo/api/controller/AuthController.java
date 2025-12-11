package com.rachaplusdemo.api.controller;

import com.rachaplusdemo.api.dto.CadastroJogadorDto;
import com.rachaplusdemo.api.dto.JogadorResponseDto;
import com.rachaplusdemo.api.dto.LoginDto;
import com.rachaplusdemo.api.model.Jogador;
import com.rachaplusdemo.api.service.JogadorService;
import com.rachaplusdemo.api.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private JogadorService jogadorService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/cadastro")
    public ResponseEntity<JogadorResponseDto> cadastrar(@RequestBody CadastroJogadorDto dto) {
        JogadorResponseDto novoJogador = jogadorService.cadastrar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoJogador);
    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody LoginDto dto) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(dto.email(), dto.senha());
        Authentication auth = authenticationManager.authenticate(usernamePassword);
        String token = tokenService.gerarToken((Jogador) auth.getPrincipal());
        return ResponseEntity.ok(Map.of("token", token));
    }
}
