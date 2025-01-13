package com.web2.trabalhofinal.controller;

import java.net.URI;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.web2.trabalhofinal.model.Aluno;
import com.web2.trabalhofinal.model.MensagemDTO;
import com.web2.trabalhofinal.service.AlunoService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/aluno")
public class AlunoController {

    @Autowired
    private AlunoService alunoService;

    @GetMapping()
    public ResponseEntity<List<Aluno>> findAll() {
        return ResponseEntity.ok().body(alunoService.findAll());
    }

    @GetMapping("{pIdAluno}")
    public ResponseEntity<Aluno> findById(@PathVariable int pIdAluno) {
        return ResponseEntity.ok().body(alunoService.findById(pIdAluno));
    }

    @PostMapping()
    public ResponseEntity<Aluno> insAluno(@RequestBody Aluno pAluno) {
        Aluno vNovoAluno = alunoService.insAluno(pAluno);
        URI vUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/aluno/{pIdAluno}")
                .buildAndExpand(vNovoAluno.getId()).toUri();

        return ResponseEntity.created(vUri).body(vNovoAluno);
    }

    // @PutMapping("{pIdAluno}/turma/{pIdTurma}")
    // public ResponseEntity<Aluno> insTurmaAluno(@PathVariable int pIdAluno, @PathVariable int pIdTurma) {
    //     Aluno vAluno = alunoService.insTurmaAluno(pIdAluno, pIdTurma);
    //     return ResponseEntity.ok().body(vAluno);
    // }

    @PutMapping("{pIdAluno}/cursos/{pIdCurso}")
    public ResponseEntity<Aluno> insCursoAluno(@PathVariable int pIdAluno, @PathVariable int pIdCurso) {
        Aluno vAluno = alunoService.insCursoAluno(pIdAluno, pIdCurso);
        return ResponseEntity.ok().body(vAluno);
    }

    @PutMapping()
    public ResponseEntity<Aluno> updAluno(@RequestBody Aluno pAluno) {
        Aluno vAlunoAlterada = alunoService.updAluno(pAluno);
        URI vUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/aluno/{pIdAluno}")
                .buildAndExpand(vAlunoAlterada.getId()).toUri();

        return ResponseEntity.created(vUri).body(vAlunoAlterada);
    }

    @DeleteMapping("{pIdAluno}")
    public ResponseEntity<MensagemDTO> delAluno(@PathVariable int pIdAluno) {
        MensagemDTO mensagem = new MensagemDTO("OK", "OK");

        try {
            alunoService.delAluno(pIdAluno);
        } catch (NoSuchElementException e) {
            mensagem = new MensagemDTO("ERRO", e.getMessage());
        }

        return ResponseEntity.ok().body(mensagem);
    }
}
