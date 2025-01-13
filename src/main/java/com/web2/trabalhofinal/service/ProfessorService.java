package com.web2.trabalhofinal.service;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.dao.DataIntegrityViolationException;

import com.web2.trabalhofinal.repository.AreaRepository;
import com.web2.trabalhofinal.repository.ProfessorRepository;
import com.web2.trabalhofinal.repository.TurmaRepository;
import com.web2.trabalhofinal.repository.CursoRepository;

import com.web2.trabalhofinal.model.Professor;
import com.web2.trabalhofinal.model.Turma;
import com.web2.trabalhofinal.model.Curso;
import com.web2.trabalhofinal.model.Area;

@Service
public class ProfessorService extends AbstractService<Professor> {

    @Autowired
    private ProfessorRepository professorRepository;

    @Autowired
    private AreaRepository areaRepository;

    @Autowired
    private CursoRepository cursoRepository;

    @Autowired
    private TurmaRepository turmaRepository;

    public List<Professor> findAll() {
        return professorRepository.findAll();
    }

    public Professor findById(Integer pIdProfessor) {
        return professorRepository
                .findById(pIdProfessor)
                .orElseThrow(() -> new NoSuchElementException("Professor " + pIdProfessor + " não cadastrado."));
    }

    public Professor insProfessor(Professor pProfessor, int pIdArea, int pIdCurso) {
        Area vArea = areaRepository
                .findById(pIdArea)
                .orElseThrow(() -> new NoSuchElementException("Área " + pIdArea + " não cadastrada."));

        Curso vCurso = cursoRepository
                .findById(pIdCurso)
                .orElseThrow(() -> new NoSuchElementException("Curso " + pIdCurso + " não cadastrado."));

        if (!vArea.getCursos().contains(vCurso))
            throw new DataIntegrityViolationException("Curso " + pIdCurso + " não pertence à área " + pIdArea + ".");

        pProfessor.setCursoProfessor(vCurso);
        pProfessor.setAreaProfessor(vArea);

        try {
            return professorRepository.save(pProfessor);
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException(
                    "Professor " + pProfessor.getId() + " já cadastrado.");
        }
    }

    public Professor insTurmaProf(int pIdProfessor, int pIdTurma) {
        Professor vProfessor = professorRepository
                .findById(pIdProfessor)
                .orElseThrow(() -> new NoSuchElementException("Professor" + pIdProfessor + " não cadastrado."));

        Turma vTurma = turmaRepository
                .findById(pIdTurma)
                .orElseThrow(() -> new NoSuchElementException("Turma" + pIdTurma + " não cadastrado."));

        List<Turma> vTurmas = vProfessor.getTurmas();
        vTurmas.add(vTurma);
        vProfessor.setTurmas(vTurmas);

        try {
            return professorRepository.save(vProfessor);
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException(
                    "Turma " + pIdTurma + " já está associada com o professor " + pIdProfessor);
        }
    }

    public Professor updProfessor(Professor pProfessor, int pIdArea, int pIdCurso) {
        int pIdProfessor = pProfessor.getId();

        Professor vProfAtual = professorRepository
                .findById(pIdProfessor)
                .orElseThrow(() -> new NoSuchElementException("Professor" + pIdProfessor + " não cadastrado."));

        Area vArea = areaRepository
                .findById(pIdArea)
                .orElseThrow(() -> new NoSuchElementException("Área" + pIdArea + " não cadastrada."));

        Curso vCurso = cursoRepository
                .findById(pIdCurso)
                .orElseThrow(() -> new NoSuchElementException("Curso" + pIdCurso + " não cadastrado."));

        if (!vArea.getCursos().contains(vCurso))
            throw new IllegalArgumentException("Curso " + pIdCurso + " não pertence à área " + pIdArea + ".");

            // if (!vCursoAtual.getAreaCurso().equals(vArea)) {
            //     Area vAreaAtual = vCursoAtual.getAreaCurso();
            //     vAreaAtual.getCursos().remove(vCursoAtual);
            //     areaRepository.save(vAreaAtual);
    
            //     vCursoAtual.setAreaCurso(vArea);
            //     vArea.getCursos().add(vCursoAtual);
            //     areaRepository.save(vArea);
            // }
        
        if(!vProfAtual.getCursoProfessor().equals(vCurso)){
            Curso vCursoAtual = vProfAtual.getCursoProfessor();
            vCursoAtual.getProfessores().remove(vProfAtual);
            cursoRepository.save(vCursoAtual);

            vProfAtual.setCursoProfessor(vCurso);
            vCurso.getProfessores().add(vProfAtual);
            cursoRepository.save(vCurso);
        }

        if(!vProfAtual.getAreaProfessor().equals(vArea)){
            Area vAreaAtual = vProfAtual.getAreaProfessor();
            vAreaAtual.getProfessores().remove(vProfAtual);
            areaRepository.save(vAreaAtual);

            vProfAtual.setAreaProfessor(vArea);
            vArea.getProfessores().add(vProfAtual);
            areaRepository.save(vArea);
        }

        vProfAtual.setAreaProfessor(vArea);

        try {
            checarAtrib(pProfessor, vProfAtual); // ! TESTAR RPA VER SE DENTRO DO TRY ATUALIZA
            return professorRepository.save(vProfAtual);
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException(
                    "Professor " + pIdProfessor + " já cadastrado.");
        }
    }

    public void delProfessor(int pIdProfessor) {
        professorRepository
                .findById(pIdProfessor)
                .orElseThrow(() -> new NoSuchElementException("Professor" + pIdProfessor + " não cadastrado."));

        professorRepository.deleteById(pIdProfessor);
    }

    @Override
    protected void checarAtrib(Professor pObjeto, Professor vObjAtual) {
        if (pObjeto.getNome() != null)
            vObjAtual.setNome(pObjeto.getNome());

        if (pObjeto.getDtNascimento() != null)
            vObjAtual.setDtNascimento(pObjeto.getDtNascimento());

        if (pObjeto.getEmail() != null)
            vObjAtual.setEmail(pObjeto.getEmail());
    }

}
