package com.rachaplusdemo.api.repository;

import com.rachaplusdemo.api.model.Jogador;
import com.rachaplusdemo.api.model.MembroRacha;
import com.rachaplusdemo.api.model.Racha;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MembroRachaRepository extends JpaRepository<MembroRacha, Long> {
    Optional<MembroRacha> findByRachaAndJogador(Racha racha, Jogador jogador);
}
