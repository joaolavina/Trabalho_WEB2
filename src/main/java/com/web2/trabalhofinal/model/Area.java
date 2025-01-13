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
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
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
@Table(uniqueConstraints =  @UniqueConstraint(columnNames = "nm_area"))
public class Area implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_area")
    private Integer idArea;

    @NotBlank(message = "O nome da área não pode ser vazio")
    @Length(min= 5, max= 30, message = "O nome da área deve ter entre 5 e 30 caracteres")
    @Column(name = "nm_area")
    private String nome;

    @OneToMany(mappedBy = "areaProfessor")
    @JsonIgnore
    private List<Professor> professores;

    @OneToMany(mappedBy = "areaCurso")
    @JsonIgnore
    private List<Curso> cursos;
}
