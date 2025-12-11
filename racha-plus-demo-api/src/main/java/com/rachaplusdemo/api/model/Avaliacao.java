package com.rachaplusdemo.api.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "avaliacao", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"avaliador_id", "avaliado_id", "racha_id"})
})
public class Avaliacao {

    @ManyToOne
    @JoinColumn(name = "racha_id", nullable = false)
    private Racha racha;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Double nota;

    @ManyToOne
    @JoinColumn(name = "avaliador_id", nullable = false)
    private Jogador avaliador;

    @ManyToOne
    @JoinColumn(name = "avaliado_id", nullable = false)
    private Jogador avaliado;

    public Avaliacao(Jogador avaliador, Jogador avaliado, Racha racha, Double nota) {
        this.avaliador = avaliador;
        this.avaliado = avaliado;
        this.racha = racha;
        this.nota = nota;
    }
}
