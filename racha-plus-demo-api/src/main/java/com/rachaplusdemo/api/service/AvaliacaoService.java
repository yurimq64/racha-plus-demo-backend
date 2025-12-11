package com.rachaplusdemo.api.service;

import com.rachaplusdemo.api.model.Avaliacao;
import com.rachaplusdemo.api.model.Jogador;
import com.rachaplusdemo.api.model.MembroRacha;
import com.rachaplusdemo.api.model.Racha;
import com.rachaplusdemo.api.repository.AvaliacaoRepository;
import com.rachaplusdemo.api.repository.JogadorRepository;
import com.rachaplusdemo.api.repository.MembroRachaRepository;
import com.rachaplusdemo.api.repository.RachaRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AvaliacaoService {

    @Autowired
    private AvaliacaoRepository avaliacaoRepository;

    @Autowired
    private JogadorRepository jogadorRepository;

    @Autowired
    private RachaRepository rachaRepository;

    @Autowired
    private MembroRachaRepository membroRachaRepository;

    @Transactional
    public void avaliarJogador(Long rachaId, Long idAvaliado, Double novaNota, String emailAvaliador) {
        Jogador avaliador = jogadorRepository.findByEmail(emailAvaliador)
                .orElseThrow(() -> new RuntimeException("Avaliador não encontrado"));
        Jogador avaliado = jogadorRepository.findById(idAvaliado)
                .orElseThrow(() -> new RuntimeException("Jogador avaliado não encontrado"));
        Racha racha = rachaRepository.findById(rachaId)
                .orElseThrow(() -> new RuntimeException("Racha não encontrado"));

        if (membroRachaRepository.findByRachaAndJogador(racha, avaliador).isEmpty()) {
            throw new RuntimeException("Você não é membro deste racha e não pode avaliar.");
        }
        if (membroRachaRepository.findByRachaAndJogador(racha, avaliado).isEmpty()) {
            throw new RuntimeException("O jogador avaliado não pertence a este racha.");
        }
        if (avaliador.getId().equals(avaliado.getId())) {
            throw new RuntimeException("Você não pode avaliar a si mesmo!");
        }
        if (novaNota < 0 || novaNota > 5) {
            throw new RuntimeException("A nota deve ser entre 0 e 5.");
        }
        if (novaNota % 0.5 != 0) {
            throw new RuntimeException("A nota deve ser em incrementos de 0.5 (ex: 3.5, 4.0, 4.5).");
        }

        Avaliacao avaliacao = avaliacaoRepository.findByAvaliadorAndAvaliadoAndRacha(avaliador, avaliado, racha)
                .orElse(new Avaliacao(avaliador, avaliado, racha, novaNota));

        avaliacao.setNota(novaNota);
        avaliacaoRepository.save(avaliacao);
        atualizarMediaDoMembro(avaliado, racha);
    }

    private void atualizarMediaDoMembro(Jogador avaliado, Racha racha) {
        List<Avaliacao> avaliacoesDoRacha = avaliacaoRepository.findByAvaliadoAndRacha(avaliado, racha);

        MembroRacha membro = membroRachaRepository.findByRachaAndJogador(racha, avaliado)
                .orElseThrow(() -> new RuntimeException("Este jogador não é membro deste racha."));

        if (avaliacoesDoRacha.isEmpty()) {
            membro.setRating(3.0);
        } else {
            double soma = avaliacoesDoRacha.stream().mapToDouble(Avaliacao::getNota).sum();
            double media = soma / avaliacoesDoRacha.size();

            membro.setRating(media);
        }

        membroRachaRepository.save(membro);
    }
}
