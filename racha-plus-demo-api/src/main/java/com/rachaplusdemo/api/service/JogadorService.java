package com.rachaplusdemo.api.service;

import com.rachaplusdemo.api.dto.CadastroJogadorDto;
import com.rachaplusdemo.api.dto.JogadorResponseDto;
import com.rachaplusdemo.api.model.Jogador;
import com.rachaplusdemo.api.repository.JogadorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class JogadorService {

    @Autowired
    private JogadorRepository jogadorRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public JogadorResponseDto cadastrar(CadastroJogadorDto dto) {
        if (jogadorRepository.findByEmail(dto.email()).isPresent()) {
            throw new RuntimeException("Este email já está cadastrado!");
        }

        Jogador jogador = new Jogador();
        jogador.setNome(dto.nome());
        jogador.setEmail(dto.email());
        jogador.setSenha(passwordEncoder.encode(dto.senha()));

        Jogador jogadorSalvo = jogadorRepository.save(jogador);

        return new JogadorResponseDto(
                jogadorSalvo.getId(),
                jogadorSalvo.getNome(),
                jogadorSalvo.getEmail()
        );
    }
}
