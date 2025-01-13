package com.web2.trabalhofinal.model;

import java.io.Serializable;
import java.util.List;

import org.hibernate.validator.constraints.Length;

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
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
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
@Table(uniqueConstraints = @UniqueConstraint(columnNames = "nm_curso"))
public class Curso implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_curso")
    private int idCurso;

    @NotBlank(message = "O nome do curso não pode ser vazio")
    @Length(min = 5, max = 50, message = "Nome do curso deve ter entre 5 e 50 caracteres")
    @Column(name = "nm_curso")
    private String nome;

    @Min(value = 4,  message = "O tempo do curso não pode ser menor que 4 horas")
    @Column(name = "nr_horas")
    private Integer horas;

    @ManyToOne
    @JoinColumn(name = "idArea")
    @JsonIgnore
    private Area areaCurso;

    @OneToMany(mappedBy = "cursoProfessor")
    @JsonIgnore
    private List<Professor> professores;

    @ManyToMany
    @JoinTable(
        name = "curso_aluno",
        joinColumns = @JoinColumn(name = "id_curso"),
        inverseJoinColumns = @JoinColumn(name = "id_aluno"),
        uniqueConstraints = @UniqueConstraint(
            name = "curso_aluno_unique",
            columnNames = {"id_curso", "id_aluno"}
        )
    )
    @JsonIgnore
    private List<Aluno> alunos;

    @ManyToMany
    @JoinTable(
        name = "curso_disciplina",
        joinColumns = @JoinColumn(name = "id_curso"),
        inverseJoinColumns = @JoinColumn(name = "id_disciplina"),
        uniqueConstraints = @UniqueConstraint(
            name = "curso_disciplina_unique",
            columnNames = {"id_curso", "id_disciplina"}
        )
    )
    @JsonIgnore
    private List<Disciplina> disciplinas;
}
