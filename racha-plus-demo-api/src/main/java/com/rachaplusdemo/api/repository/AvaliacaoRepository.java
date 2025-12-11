package com.rachaplusdemo.api.repository;

import com.rachaplusdemo.api.model.Avaliacao;
import com.rachaplusdemo.api.model.Jogador;
import com.rachaplusdemo.api.model.Racha;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AvaliacaoRepository extends JpaRepository<Avaliacao, Long> {

    Optional<Avaliacao> findByAvaliadorAndAvaliadoAndRacha(Jogador avaliador, Jogador avaliado, Racha racha);

    List<Avaliacao> findByAvaliadoAndRacha(Jogador avaliado, Racha racha);
}
