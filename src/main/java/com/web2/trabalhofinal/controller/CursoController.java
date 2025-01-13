package com.web2.trabalhofinal.controller;

import java.net.URI;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.web2.trabalhofinal.model.Curso;
import com.web2.trabalhofinal.model.MensagemDTO;
import com.web2.trabalhofinal.service.CursoService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping(value = "/curso")
public class CursoController {

    @Autowired
    private CursoService cursoService;

    @GetMapping()
    public ResponseEntity<List<Curso>> findAll() {
        return ResponseEntity.ok().body(cursoService.findAll());
    }

    @GetMapping("/{pIdCurso}")
    public ResponseEntity<Curso> findById(@PathVariable int pIdCurso) {
        return ResponseEntity.ok().body(cursoService.findById(pIdCurso));
    }

    @PostMapping("/area/{pIdArea}")
    public ResponseEntity<Curso> insCurso(@PathVariable int pIdArea, @RequestBody Curso pCurso) {
        Curso vNovoCurso = cursoService.insCurso(pCurso, pIdArea);
        URI vUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/curso/{pIdCurso}")
                .buildAndExpand(vNovoCurso.getIdCurso()).toUri();

        return ResponseEntity.created(vUri).body(vNovoCurso);
    }

    @PutMapping("/{pIdCurso}/professores/{pIdProf}")
    public ResponseEntity<Curso> insProfCurso(@PathVariable int pIdCurso, @PathVariable int pIdProf) {
        Curso vCurso = cursoService.insProfCurso(pIdCurso, pIdProf);
        return ResponseEntity.ok().body(vCurso);
    }

    @PutMapping("/{pIdCurso}/disciplinas/{pIdDisciplina}")
    public ResponseEntity<Curso> insDisciplinaCurso(@PathVariable int pIdCurso, @PathVariable int pIdDisciplina) {
        Curso vCurso = cursoService.insDisciplinaCurso(pIdCurso, pIdDisciplina);
        return ResponseEntity.ok().body(vCurso);
    }

    @PutMapping("/{pIdCurso}/alunos/{pIdAluno}")
    public ResponseEntity<Curso> insAlunoCurso(@PathVariable int pIdCurso, @PathVariable int pIdAluno) {
        Curso vCurso = cursoService.insAlunoCurso(pIdCurso, pIdAluno);
        return ResponseEntity.ok().body(vCurso);
    }

    @PutMapping("/area/{pIdArea}")
    public ResponseEntity<Curso> updCurso(@PathVariable int pIdArea, @RequestBody Curso pCurso) {
        Curso vCursoAlterado = cursoService.insCurso(pCurso, pIdArea);
        URI vUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/curso/{pIdCurso}")
                .buildAndExpand(vCursoAlterado.getIdCurso()).toUri();

        return ResponseEntity.created(vUri).body(vCursoAlterado);
    }

    @DeleteMapping("/{pIdCurso}")
    public ResponseEntity<MensagemDTO> delCurso(@PathVariable int pIdCurso) {
        MensagemDTO mensagem = new MensagemDTO("OK", "OK");

        try {
            cursoService.delCurso(pIdCurso);
        } catch (NoSuchElementException e) {
            mensagem = new MensagemDTO("ERRO", e.getMessage());
        }

        return ResponseEntity.ok().body(mensagem);
    }
}
