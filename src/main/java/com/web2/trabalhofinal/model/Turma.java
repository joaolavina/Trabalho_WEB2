package com.web2.trabalhofinal.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Entity
public class Turma implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_turma")
    private int id;

    @Column(name = "dt_inicio")
    private LocalDate dtInicio;

    @Column(name = "dt_fim")
    private LocalDate dtFim;

    @NotNull
    @Pattern(regexp = "[MVN]", message = "O turno deve ser M (matutino),  V (vespertino) ou N (noturno)")
    @Column(name = "sg_turno")
    private String turno;

    @ManyToOne
    @JoinColumn(name = "id_professor")
    @JsonIgnore
    private Professor professorTurma;

    @ManyToOne
    @JoinColumn(name = "id_disciplina")
    @JsonIgnore
    private Disciplina disciplinaTurma;

    @ManyToMany
    @JoinTable(
        name = "turma_aluno",
        joinColumns = @JoinColumn(name = "id_turma"),
        inverseJoinColumns = @JoinColumn(name = "id_aluno"),
        uniqueConstraints = @UniqueConstraint(
            name = "turma_aluno_unique",
            columnNames = {"id_turma", "id_aluno"}
        )
    )
    @JsonIgnore
    private List<Aluno> alunos;
}
