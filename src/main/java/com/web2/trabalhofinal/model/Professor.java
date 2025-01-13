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
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
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
@Table(uniqueConstraints = @UniqueConstraint(columnNames = { "nm_professor", "ds_email" }))
public class Professor implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_professor")
    private int id;

    @NotBlank(message = "O nome do professor não pode ser vazio")
    @Length(min = 2, message = "O nome do professor deve ter no mínimo 2 caracteres")
    @Column(name = "nm_professor")
    private String nome;

    @Past(message = "A data de nascimento deve ser anterior à data atual")
    @Column(name = "dt_nascimento")
    private LocalDate dtNascimento;

    @Email
    @Column(name = "ds_email")
    private String email;

    @ManyToOne
    @JoinColumn(name = "id_curso")
    @JsonIgnore
    private Curso cursoProfessor;

    @ManyToOne
    @JoinColumn(name = "id_area")
    @JsonIgnore
    private Area areaProfessor;

    @OneToMany(mappedBy = "professorTurma")
    @JsonIgnore
    private List<Turma> turmas;

}
