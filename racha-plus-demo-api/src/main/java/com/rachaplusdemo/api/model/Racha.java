package com.rachaplusdemo.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.rachaplusdemo.api.enums.Esporte;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "racha")
public class Racha {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Esporte esporte;

    @ManyToOne
    @JoinColumn(name = "dono_id", nullable = false)
    @JsonIgnoreProperties("membros")
    private Jogador dono;

    @OneToMany(mappedBy = "racha", cascade = CascadeType.ALL)
    private Set<MembroRacha> membros = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Racha racha = (Racha) o;
        return Objects.equals(id, racha.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}