package com.rachaplusdemo.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
@Table(name = "membro_racha", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"racha_id", "jogador_id"})
})
public class MembroRacha {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double rating = 3.0;

    @ManyToOne
    @JoinColumn(name = "racha_id", nullable = false)
    @JsonIgnore
    private Racha racha;

    @ManyToOne
    @JoinColumn(name = "jogador_id", nullable = false)
    private Jogador jogador;

    public MembroRacha(Racha racha, Jogador jogador) {
        this.racha = racha;
        this.jogador = jogador;
        this.rating = 3.0;
    }
}