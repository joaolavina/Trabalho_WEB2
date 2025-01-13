package com.web2.trabalhofinal.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = { "nm_aluno", "ds_email" }))
public class Aluno implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_aluno")
    private int id;

    @NotBlank(message = "O nome do aluno não pode ser vazio")
    @Length(min = 2, message = "O nome do aluno deve ter no mínimo 2 caracteres")
    @Column(name = "nm_aluno")
    private String nome;

    @Email
    @Column(name = "ds_email")
    private String email;

    @Past(message = "A data de nascimento deve ser anterior à data atual")
    @Column(name = "dt_nascimento")
    private LocalDate dtNascimento;

    @ManyToMany(mappedBy = "alunos")
    @JsonIgnore
    private List<Turma> turmas;

    @ManyToMany(mappedBy = "alunos")
    @JsonIgnore
    private List<Curso> cursos;
}