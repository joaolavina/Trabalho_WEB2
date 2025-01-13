package com.web2.trabalhofinal.service;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.web2.trabalhofinal.exceptions.CustomConstraintViolantionException;
import com.web2.trabalhofinal.model.Area;
import com.web2.trabalhofinal.model.Curso;
import com.web2.trabalhofinal.model.Professor;
import com.web2.trabalhofinal.repository.AreaRepository;
import com.web2.trabalhofinal.repository.CursoRepository;
import com.web2.trabalhofinal.repository.ProfessorRepository;

import jakarta.validation.ConstraintViolationException;

@Service
public class AreaService extends AbstractService<Area> {

    @Autowired
    private AreaRepository areaRepository;

    @Autowired
    private ProfessorRepository professorRepository;

    @Autowired
    private CursoRepository cursoRepository;

    public List<Area> findAll() {
        return areaRepository.findAll();
    }

    public Area findById(int pIdArea) {
        return areaRepository
                .findById(pIdArea)
                .orElseThrow(() -> new NoSuchElementException("Área " + pIdArea + " não cadastrada."));
    }

    public Area insArea(Area pArea) {
        try {
            return areaRepository.save(pArea);
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException("Os dados informados não atendem aos requisitos necessários.");
        } catch (ConstraintViolationException e) {
            throw new CustomConstraintViolantionException("Os dados informados não atendem aos requisitos necessários.");
        }
    }

    public Area insProfessorArea(int pIdArea, int pIdProfessor) {
        Area vNovaArea = areaRepository
                .findById(pIdArea)
                .orElseThrow(() -> new NoSuchElementException("Área " + pIdArea + " não cadastrada."));

        Professor vProfessor = professorRepository
                .findById(pIdProfessor)
                .orElseThrow(() -> new NoSuchElementException("Professor " + pIdProfessor + " não cadastrado."));

        Area vAreaAtual = vProfessor.getAreaProfessor();

        if (vAreaAtual.equals(vNovaArea))
            throw new DataIntegrityViolationException(
                    "Professor " + pIdProfessor + " já está associado com a área " + pIdArea + ".");
        else {
            vAreaAtual.getProfessores().remove(vProfessor);
            areaRepository.save(vAreaAtual);
        }

        vProfessor.setAreaProfessor(vNovaArea);

        vNovaArea.getProfessores().add(vProfessor);

        return areaRepository.save(vNovaArea);
    }

    public Area insCursoArea(int pIdArea, int pIdCurso) {
        Area vNovaArea = areaRepository
                .findById(pIdArea)
                .orElseThrow(() -> new NoSuchElementException("Área " + pIdArea + " não cadastrada."));

        Curso vCurso = cursoRepository
                .findById(pIdCurso)
                .orElseThrow(() -> new NoSuchElementException("Professor " + pIdCurso + " não cadastrado."));

        Area vAreaAtual = vCurso.getAreaCurso();

        if (vAreaAtual.equals(vNovaArea))
            throw new DataIntegrityViolationException(
                    "Curso " + pIdCurso + " já está associado com a área " + pIdArea + ".");
        else {
            vAreaAtual.getCursos().remove(vCurso);
            areaRepository.save(vAreaAtual);
        }

        vCurso.setAreaCurso(vNovaArea);

        vNovaArea.getCursos().add(vCurso);

        return areaRepository.save(vNovaArea);
    }

    public Area updArea(Area pArea) {
        int pIdArea = pArea.getIdArea();

        Area vAreaAtual = areaRepository
                .findById(pIdArea)
                .orElseThrow(() -> new NoSuchElementException("Área " + pIdArea + " não cadastrada."));

        try {
            checarAtrib(pArea, vAreaAtual);
            return areaRepository.save(vAreaAtual);
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException("Os dados informados não atendem aos requisitos necessários.");
        } catch (ConstraintViolationException e) {
            throw new CustomConstraintViolantionException("Os dados informados não atendem aos requisitos necessários.");
        }
    }

    public void delProfessor(int pIdArea) {
        areaRepository
                .findById(pIdArea)
                .orElseThrow(() -> new NoSuchElementException("Área " + pIdArea + " não cadastrada."));

        areaRepository.deleteById(pIdArea);
    }

    @Override
    protected void checarAtrib(Area pObjeto, Area vObjAtual) {
        if (pObjeto.getNome() != null)
            vObjAtual.setNome(pObjeto.getNome());
    }
}
