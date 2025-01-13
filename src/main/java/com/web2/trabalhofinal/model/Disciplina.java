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
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
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
@Table(uniqueConstraints = @UniqueConstraint(columnNames = "nm_disciplina"))
public class Disciplina implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_disciplina")
    private int id;

    @NotBlank(message = "O nome da disciplina não pode ser vazio")
    @Length(min = 2, message = "O nome da disciplina deve ter no mínimo 5 caracteres")
    @Column(name = "nm_disciplina")
    private String nome;

    @Positive(message = "O número de horas deve ser positivo")
    @Column(name = "nr_horas")
    private Integer horas;

    @Column(name = "ds_objetivos")
    private String objetivos;

    @Column(name = "ds_conteudo")
    private String conteudo;

    @OneToMany(mappedBy = "disciplinaTurma")
    @JsonIgnore
    private List<Turma> turmas;

    @ManyToMany(mappedBy = "disciplinas")
    @JsonIgnore
    private List<Curso> cursos;
}
