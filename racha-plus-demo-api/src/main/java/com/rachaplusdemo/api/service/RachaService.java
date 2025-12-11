package com.rachaplusdemo.api.service;

import com.rachaplusdemo.api.dto.RachaDto;
import com.rachaplusdemo.api.model.Jogador;
import com.rachaplusdemo.api.model.MembroRacha;
import com.rachaplusdemo.api.model.Racha;
import com.rachaplusdemo.api.repository.JogadorRepository;
import com.rachaplusdemo.api.repository.MembroRachaRepository;
import com.rachaplusdemo.api.repository.RachaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RachaService {

    @Autowired
    private RachaRepository rachaRepository;

    @Autowired
    private JogadorRepository jogadorRepository;

    @Autowired
    private MembroRachaRepository membroRachaRepository;

    public Racha criarRacha(RachaDto dto, String emailCriador) {
        Jogador criador = jogadorRepository.findByEmail(emailCriador)
                .orElseThrow(() -> new RuntimeException("Jogador não encontrado"));

        Racha novoRacha = new Racha();
        novoRacha.setNome(dto.nome());
        novoRacha.setEsporte(dto.esporte());
        novoRacha.setDono(criador);
        Racha rachaSalvo = rachaRepository.save(novoRacha);
        MembroRacha membroDono = new MembroRacha(rachaSalvo, criador);
        membroRachaRepository.save(membroDono);

        return rachaSalvo;
    }

    public Set<Racha> listarMeusRachas(String emailUsuario) {
        Jogador jogador = jogadorRepository.findByEmail(emailUsuario)
                .orElseThrow(() -> new RuntimeException("Jogador não encontrado"));

        return jogador.getMembros().stream()
                .map(MembroRacha::getRacha)
                .collect(Collectors.toSet());
    }

    public void adicionarJogador(Long rachaId, String emailNovoJogador, String emailLogado) {
        Racha racha = rachaRepository.findById(rachaId)
                .orElseThrow(() -> new RuntimeException("Racha não encontrado"));

        if (!emailLogado.equals(racha.getDono().getEmail())) {
            throw new RuntimeException("Apenas o dono do racha pode adicionar membros");
        }

        Jogador novoJogador = jogadorRepository.findByEmail(emailNovoJogador)
                .orElseThrow(() -> new RuntimeException("Jogador não encontrado"));

        if (membroRachaRepository.findByRachaAndJogador(racha, novoJogador).isPresent()) {
            throw new RuntimeException("Este jogador já faz parte do racha");
        }

        MembroRacha novoMembro = new MembroRacha(racha, novoJogador);
        membroRachaRepository.save(novoMembro);
    }
}
