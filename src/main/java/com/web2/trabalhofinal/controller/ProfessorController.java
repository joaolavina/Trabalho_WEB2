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
import com.web2.trabalhofinal.model.Professor;
import com.web2.trabalhofinal.service.ProfessorService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;


@RestController
@RequestMapping(value = "/professor")
public class ProfessorController {

    @Autowired
    private ProfessorService professorService;

    @GetMapping()
    public ResponseEntity<List<Professor>> findAll() {
        return ResponseEntity.ok().body(professorService.findAll());
    }

    @GetMapping("/{pIdProfessor}")
    public ResponseEntity<Professor> findById(@PathVariable int pIdProfessor) {
        return ResponseEntity.ok().body(professorService.findById(pIdProfessor));
    }

    @PostMapping("/area/{pIdArea}/curso/{pIdCurso}")
    public ResponseEntity<Professor> insProfessor(@PathVariable int pIdArea, @PathVariable int pIdCurso,
            @RequestBody Professor pNovoProfessor) {
        Professor vNovoProfessor = professorService.insProfessor(pNovoProfessor, pIdArea, pIdCurso);
        URI vUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("professor/{pIdProfessor}")
                .buildAndExpand(vNovoProfessor.getId()).toUri();

        return ResponseEntity.created(vUri).body(vNovoProfessor);
    }

    @PutMapping("/{pIdProfessor}/turmas/{pIdTurma}")
    public ResponseEntity<Professor> insTurmaProf(@PathVariable int pIdProfessor, @PathVariable int pIdTurma) {
        Professor vProfessor = professorService.insTurmaProf(pIdProfessor, pIdTurma);
        return ResponseEntity.ok().body(vProfessor);
    }

    @PutMapping("/area/{pIdArea}/curso/{pIdCurso}")
    public ResponseEntity<Professor> updProfessor(@PathVariable int pIdArea, @PathVariable int pIdCurso,
            @RequestBody Professor pProfessor) {
        Professor vProfAlterado = professorService.updProfessor(pProfessor, pIdArea, pIdCurso);
        URI vUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/professor/{pIdProfessor}")
                .buildAndExpand(vProfAlterado.getId()).toUri();

        return ResponseEntity.created(vUri).body(vProfAlterado);
    }

    @DeleteMapping("/{pIdProfessor}")
    public ResponseEntity<MensagemDTO> delProfessor(@PathVariable int pIdProfessor) {

        MensagemDTO mensagem = new MensagemDTO("OK", "OK");

        try {
            professorService.delProfessor(pIdProfessor);
        } catch (NoSuchElementException e) {
            mensagem = new MensagemDTO("ERRO", e.getMessage());
        } 

        return ResponseEntity.ok().body(mensagem);
    }

}
