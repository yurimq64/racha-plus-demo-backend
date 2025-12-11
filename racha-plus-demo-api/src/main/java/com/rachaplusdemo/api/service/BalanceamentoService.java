package com.rachaplusdemo.api.service;

import com.rachaplusdemo.api.dto.BalanceamentoResponseDto;
import com.rachaplusdemo.api.dto.MembroDto;
import com.rachaplusdemo.api.dto.TimeDto;
import com.rachaplusdemo.api.model.MembroRacha;
import com.rachaplusdemo.api.model.Racha;
import com.rachaplusdemo.api.repository.RachaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class BalanceamentoService {

    @Autowired
    private RachaRepository rachaRepository;

    public BalanceamentoResponseDto balancearTimes(Long rachaId, String emailSolicitante) {
        Racha racha = rachaRepository.findById(rachaId)
                .orElseThrow(() -> new RuntimeException("Racha não encontrado"));
        boolean isMembro = racha.getMembros().stream()
                .anyMatch(m -> m.getJogador().getEmail().equals(emailSolicitante));

        if (!isMembro) {
            throw new RuntimeException("Acesso negado: Você não é membro deste racha.");
        }

        List<MembroRacha> todosMembros = racha.getMembros().stream()
                .sorted(Comparator.comparing(MembroRacha::getRating).reversed())
                .toList();

        int tamanhoTimeOficial = racha.getEsporte().getQuantidatePorTime();
        int totalJogadores = todosMembros.size();

        int qtdTimesCompletos = totalJogadores / tamanhoTimeOficial;

        List<TimeDto> timesFormados;

        if (qtdTimesCompletos < 2) {
            timesFormados = distribuirJogadoresEmTimes(todosMembros, 2);
        }
        else {
            int corte = qtdTimesCompletos * tamanhoTimeOficial;
            List<MembroRacha> jogadoresPrincipais = todosMembros.subList(0, corte);
            List<MembroRacha> jogadoresReserva = todosMembros.subList(corte, totalJogadores);

            timesFormados = distribuirJogadoresEmTimes(jogadoresPrincipais, qtdTimesCompletos);

            if (!jogadoresReserva.isEmpty()) {
                TimeDto timeReserva = criarTimeDto("Time de Fora / Reserva", jogadoresReserva);
                timesFormados.add(timeReserva);
            }
        }

        return new BalanceamentoResponseDto(timesFormados, calcularDiferenca(timesFormados));
    }

    private List<TimeDto> distribuirJogadoresEmTimes(List<MembroRacha> jogadores, int numeroDeTimes) {
        List<List<MembroRacha>> buckets = new ArrayList<>();
        double[] forcas = new double[numeroDeTimes];

        for (int i = 0; i < numeroDeTimes; i++) {
            buckets.add(new ArrayList<>());
            forcas[i] = 0.0;
        }

        for (MembroRacha jogador : jogadores) {
            int timeMaisFracoIndex = getIndiceTimeMaisFraco(forcas);

            buckets.get(timeMaisFracoIndex).add(jogador);
            forcas[timeMaisFracoIndex] += jogador.getRating();
        }

        List<TimeDto> resultado = new ArrayList<>();
        for (int i = 0; i < numeroDeTimes; i++) {
            resultado.add(criarTimeDto("Time " + (i + 1), buckets.get(i)));
        }
        return resultado;
    }

    private int getIndiceTimeMaisFraco(double[] forcas) {
        int index = 0;
        double minForca = forcas[0];
        for (int i = 1; i < forcas.length; i++) {
            if (forcas[i] < minForca) {
                minForca = forcas[i];
                index = i;
            }
        }
        return index;
    }

    private TimeDto criarTimeDto(String nome, List<MembroRacha> membros) {
        List<MembroDto> membrosDto = membros.stream()
                .map(m -> new MembroDto(m.getJogador().getNome(), m.getRating()))
                .collect(Collectors.toList());

        double forcaTotal = membros.stream().mapToDouble(MembroRacha::getRating).sum();

        return new TimeDto(nome, membrosDto, Math.round(forcaTotal * 100.0) / 100.0);
    }

    private Double calcularDiferenca(List<TimeDto> times) {
        List<Double> forcasTitulares = times.stream()
                .filter(t -> !t.nome().contains("Reserva"))
                .map(TimeDto::forcaTotal)
                .toList();

        if (forcasTitulares.isEmpty()) return 0.0;

        double max = Collections.max(forcasTitulares);
        double min = Collections.min(forcasTitulares);

        return Math.round((max - min) * 100.0) / 100.0;
    }
}