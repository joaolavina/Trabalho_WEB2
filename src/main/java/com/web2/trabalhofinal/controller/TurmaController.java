package com.web2.trabalhofinal.controller;

import java.net.URI;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.web2.trabalhofinal.model.MensagemDTO;
import com.web2.trabalhofinal.model.Turma;
import com.web2.trabalhofinal.service.TurmaService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping(value = "/turma")
public class TurmaController {

    @Autowired
    private TurmaService turmaService;

    @GetMapping()
    public ResponseEntity<List<Turma>> findAll() {
        return ResponseEntity.ok().body(turmaService.findAll());
    }

    @GetMapping("/{pIdTurma}")
    public ResponseEntity<Turma> findById(@PathVariable int pIdTurma) {
        return ResponseEntity.ok().body(turmaService.findById(pIdTurma));
    }

    @PostMapping("/disciplina/{pIdDisciplina}/professor/{pIdProf}")
    public ResponseEntity<Turma> insTurma(@PathVariable int pIdDisciplina, @PathVariable int pIdProf,
            @RequestBody Turma pTurma) {

        Turma vTurma = turmaService.insTurma(pTurma, pIdProf, pIdDisciplina);
        URI vUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("turma/{pIdTurma}")
                .buildAndExpand(vTurma.getId()).toUri();

        return ResponseEntity.created(vUri).body(vTurma);   
    }

    @PutMapping("{pIdTurma}/alunos/{pIdAluno}")
    public ResponseEntity<Turma> insAlunoTurma(@PathVariable int pIdTurma, @PathVariable int pIdAluno) {
        Turma vTurma = turmaService.insAlunoTurma(pIdAluno, pIdTurma);
        return ResponseEntity.ok().body(vTurma);
    }

    @PutMapping("/disciplina/{pIdDisciplina}/professor/{pIdProf}")
    public ResponseEntity<Turma> updTurma(@PathVariable int pIdDisciplina, @PathVariable int pIdProf,
            @RequestBody Turma pTurma) {
        Turma vTurma = turmaService.updTurma(pTurma, pIdProf, pIdDisciplina);


        return ResponseEntity.ok().body(vTurma);
    }

    @DeleteMapping("/{pIdTurma}")
    public ResponseEntity<MensagemDTO> delProfessor(@PathVariable int pIdTurma) {

        MensagemDTO mensagem = new MensagemDTO("OK", "OK");

        try {
            turmaService.delTurma(pIdTurma);
        } catch (NoSuchElementException e) {
            mensagem = new MensagemDTO("ERRO", e.getMessage());
        }

        return ResponseEntity.ok().body(mensagem);
    }

}
