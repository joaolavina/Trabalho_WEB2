package com.web2.trabalhofinal.controller;

import java.net.URI;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.web2.trabalhofinal.model.Disciplina;
import com.web2.trabalhofinal.model.MensagemDTO;
import com.web2.trabalhofinal.service.DisciplinaService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping(value = "/disciplina")
public class DisciplinaController {

    @Autowired
    private DisciplinaService disciplinaService;

    @GetMapping()
    public ResponseEntity<List<Disciplina>> findAll() {
        return ResponseEntity.ok().body(disciplinaService.findAll());
    }

    @GetMapping("/{pIdDisciplina}")
    public ResponseEntity<Disciplina> findById(@PathVariable int pIdDisciplina) {
        return ResponseEntity.ok().body(disciplinaService.findById(pIdDisciplina));
    }

    @PostMapping()
    public ResponseEntity<Disciplina> insDisciplina(@RequestBody Disciplina pDisciplina) {
        Disciplina vNovaDisciplina = disciplinaService.insDisciplina(pDisciplina);
        URI vUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/disciplina/{pIdDisciplina}")
                .buildAndExpand(vNovaDisciplina.getId()).toUri();

        return ResponseEntity.created(vUri).body(vNovaDisciplina);
    }

    @PutMapping("/{pIdDisciplina}/turmas/{pIdTurma}")
    public ResponseEntity<Disciplina> insTurmaDisciplina(@PathVariable int pIdDisciplina, @PathVariable int pIdTurma) {
        Disciplina vDisciplina = disciplinaService.insTurmDisciplina(pIdDisciplina, pIdTurma);
        return ResponseEntity.ok().body(vDisciplina);
    }

    @PutMapping("/{pIdDisciplina}/cursos/{pIdCurso}")
    public ResponseEntity<Disciplina> insCursoDisciplina(@PathVariable int pIdDisciplina, @PathVariable int pIdCurso) {
        Disciplina vDisciplina = disciplinaService.insCursoDisciplina(pIdDisciplina, pIdCurso);
        return ResponseEntity.ok().body(vDisciplina);
    }

    @PutMapping()
    public ResponseEntity<Disciplina> updDisciplina(@RequestBody Disciplina pDisciplina) {
        Disciplina vDisciplinaAlterada = disciplinaService.updDisciplina(pDisciplina);
        return ResponseEntity.ok().body(vDisciplinaAlterada);
    }

    @DeleteMapping("/{pIdDisciplina}")
    public ResponseEntity<MensagemDTO> delDisciplina(@PathVariable int pIdDisciplina) {
        MensagemDTO mensagem = new MensagemDTO("OK", "OK");

        try {
            disciplinaService.delDisciplina(pIdDisciplina);
        } catch (NoSuchElementException e) {
            mensagem = new MensagemDTO("ERRO", e.getMessage());
        }

        return ResponseEntity.ok().body(mensagem);
    }

}
