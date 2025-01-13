package com.web2.trabalhofinal.controller;

import java.net.URI;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.web2.trabalhofinal.model.Area;
import com.web2.trabalhofinal.model.MensagemDTO;
import com.web2.trabalhofinal.service.AreaService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping(value = "/area")
public class AreaController {

    @Autowired
    private AreaService areaService;

    @GetMapping()
    public ResponseEntity<List<Area>> findAll() {
        return ResponseEntity.ok().body(areaService.findAll());
    }

    @GetMapping("/{pIdArea}")
    public ResponseEntity<Area> findById(@PathVariable int pIdArea) {
        return ResponseEntity.ok().body(areaService.findById(pIdArea));
    }

    @PostMapping()
    public ResponseEntity<Area> insArea(@RequestBody Area pNovaArea) {
        Area vNovaArea = areaService.insArea(pNovaArea);
        URI vUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("area/{pIdArea}")
                .buildAndExpand(vNovaArea.getIdArea()).toUri();

        return ResponseEntity.created(vUri).body(vNovaArea);
    }

    @PutMapping("/{pIdArea}/professores/{pIdProfessor}")
    public ResponseEntity<Area> insProfessorArea(@PathVariable int pIdArea, @PathVariable int pIdProf) {
        Area vArea = areaService.insProfessorArea(pIdArea, pIdProf);
        return ResponseEntity.ok().body(vArea);
    }

    @PutMapping("/{pIdArea}/cursos/{pIdCurso}")
    public ResponseEntity<Area> insCursoArea(@PathVariable int pIdArea, @PathVariable int pIdCurso) {
        Area vArea = areaService.insCursoArea(pIdArea, pIdCurso);
        return ResponseEntity.ok().body(vArea);
    }

    @PutMapping()
    public ResponseEntity<Area> updArea(@RequestBody Area pArea) {
        Area vAreaAlterada = areaService.updArea(pArea);
        URI vUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/area/{pIdArea}")
                .buildAndExpand(vAreaAlterada.getIdArea()).toUri();
        
        return ResponseEntity.created(vUri).body(vAreaAlterada);
    }

    @DeleteMapping("/{pIdArea}")
    public ResponseEntity<MensagemDTO> delArea(@PathVariable int pIdArea) {
        MensagemDTO mensagem = new MensagemDTO("OK", "OK");

        try {
            areaService.delProfessor(pIdArea);
        } catch (NoSuchElementException e) {
            mensagem = new MensagemDTO("ERRO", e.getMessage());
        }

        return ResponseEntity.ok().body(mensagem);
    }
}
