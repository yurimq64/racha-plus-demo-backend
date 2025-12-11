package com.rachaplusdemo.api.config;

import com.rachaplusdemo.api.enums.Esporte;
import com.rachaplusdemo.api.model.Jogador;
import com.rachaplusdemo.api.model.MembroRacha;
import com.rachaplusdemo.api.model.Racha;
import com.rachaplusdemo.api.repository.JogadorRepository;
import com.rachaplusdemo.api.repository.MembroRachaRepository;
import com.rachaplusdemo.api.repository.RachaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Configuration
public class SeedDataConfig implements CommandLineRunner {

    @Autowired
    private JogadorRepository jogadorRepository;

    @Autowired
    private RachaRepository rachaRepository;

    @Autowired
    private MembroRachaRepository membroRachaRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Evita duplicar se já rodou
        if (jogadorRepository.count() > 0) {
            System.out.println("Banco de dados já populado. Pulando seed.");
            return;
        }

        System.out.println("Iniciando população do banco de dados para DEMO...");

        String senhaPadrao = passwordEncoder.encode("123456");
        Random random = new Random();

        // 1. Criar Jogador Dono
        Jogador dono = new Jogador();
        dono.setNome("Yuri Organizador");
        dono.setEmail("dono@racha.com");
        dono.setSenha(senhaPadrao);
        jogadorRepository.save(dono);

        // ==========================================
        // CENÁRIO 1: BASQUETE (5 vs 5)
        // ==========================================

        Racha rachaBasket = new Racha();
        rachaBasket.setNome("Rapaziada do Baska"); // Nome atualizado
        rachaBasket.setEsporte(Esporte.BASQUETE);
        rachaBasket.setDono(dono);
        rachaRepository.save(rachaBasket);

        // Adiciona dono ao basquete
        criarMembro(rachaBasket, dono, 4.5);

        // Jogadores da NBA
        List<String> nomesBasket = Arrays.asList(
                "Lebron James", "Stephen Curry", "Kevin Durant", "Giannis A.",
                "Luka Doncic", "Nikola Jokic", "Joel Embiid", "Jayson Tatum",
                "Jimmy Butler", "Devin Booker", "Ja Morant", "Zion Williamson"
        );

        for (int i = 0; i < nomesBasket.size(); i++) {
            Jogador j = criarJogador(nomesBasket.get(i), "nba" + i + "@teste.com", senhaPadrao);
            // Notas variadas entre 3.0 e 5.0 para o basquete
            criarMembro(rachaBasket, j, 3.0 + (random.nextDouble() * 2.0));
        }

        // ==========================================
        // CENÁRIO 2: FUTEBOL (11 vs 11)
        // ==========================================

        Racha rachaFut = new Racha();
        rachaFut.setNome("Pelada de Domingo");
        rachaFut.setEsporte(Esporte.FUTEBOL);
        rachaFut.setDono(dono);
        rachaRepository.save(rachaFut);

        // Adiciona dono ao futebol (pode ter nota diferente do basquete!)
        criarMembro(rachaFut, dono, 3.5);

        // Jogadores de Futebol (Criando 23 para ter 2 times de 11 e 1 reserva)
        List<String> nomesFut = Arrays.asList(
                "Vini Jr", "Neymar Jr", "Messi", "Cristiano Ronaldo", "Mbappé",
                "Haaland", "De Bruyne", "Bellingham", "Harry Kane", "Salah",
                "Rodri", "Bernardo Silva", "Phil Foden", "Saka", "Osimhen",
                "Lautaro Martinez", "Griezmann", "Musiala", "Pedri", "Modric",
                "Toni Kroos", "Van Dijk", "Alisson Becker"
        );

        for (int i = 0; i < nomesFut.size(); i++) {
            Jogador j = criarJogador(nomesFut.get(i), "fut" + i + "@teste.com", senhaPadrao);
            // Notas variadas entre 2.5 e 5.0
            criarMembro(rachaFut, j, 2.5 + (random.nextDouble() * 2.5));
        }

        System.out.println("------------------------------------------------");
        System.out.println("✅ BANCO POPULADO!");
        System.out.println("🏀 Racha 1: 'Rapaziada do Baska' (Basquete - ID provável: 1)");
        System.out.println("⚽ Racha 2: 'Pelada de Domingo' (Futebol - ID provável: 2)");
        System.out.println("👤 Login: dono@racha.com | 123456");
        System.out.println("------------------------------------------------");
    }

    // Métodos auxiliares para limpar o código
    private Jogador criarJogador(String nome, String email, String senha) {
        Jogador j = new Jogador();
        j.setNome(nome);
        j.setEmail(email);
        j.setSenha(senha);
        return jogadorRepository.save(j);
    }

    private void criarMembro(Racha racha, Jogador jogador, Double nota) {
        double notaArredondada = Math.round(nota * 100.0) / 100.0;
        if (notaArredondada > 5.0) notaArredondada = 5.0;

        MembroRacha membro = new MembroRacha(racha, jogador);
        membro.setRating(nota);
        membroRachaRepository.save(membro);
    }
}