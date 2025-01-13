package com.web2.trabalhofinal.service;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.web2.trabalhofinal.exceptions.CustomConstraintViolantionException;
import com.web2.trabalhofinal.model.Aluno;
import com.web2.trabalhofinal.model.Area;
import com.web2.trabalhofinal.model.Curso;
import com.web2.trabalhofinal.model.Disciplina;
import com.web2.trabalhofinal.model.Professor;
import com.web2.trabalhofinal.repository.AlunoRepository;
import com.web2.trabalhofinal.repository.AreaRepository;
import com.web2.trabalhofinal.repository.CursoRepository;
import com.web2.trabalhofinal.repository.DisciplinaRepository;
import com.web2.trabalhofinal.repository.ProfessorRepository;

import jakarta.validation.ConstraintViolationException;

@Service
public class CursoService extends AbstractService<Curso> {

    @Autowired
    private CursoRepository cursoRepository;

    @Autowired
    private AreaRepository areaRepository;

    @Autowired
    private ProfessorRepository profRepository;

    @Autowired
    private DisciplinaRepository disciplinaRepository;

    @Autowired
    private AlunoRepository alunoRepository;

    public List<Curso> findAll() {
        return cursoRepository.findAll();
    }

    public Curso findById(int pIdCurso) {
        return cursoRepository
                .findById(pIdCurso)
                .orElseThrow(() -> new NoSuchElementException("Curso " + pIdCurso + " não cadastrado."));
    }

    public Curso insCurso(Curso pCurso, int pIdArea) {
        Area vArea = areaRepository
                .findById(pIdArea)
                .orElseThrow(() -> new NoSuchElementException("Área " + pIdArea + " não cadastrada."));

        pCurso.setAreaCurso(vArea);

        try {
            return cursoRepository.save(pCurso);
        } catch (ConstraintViolationException e) {
            throw new CustomConstraintViolantionException("Erro na validação dos dados informados.");
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException("Erro na validação dos dados informados.");
        }
    }

    public Curso insProfCurso(int pIdCurso, int pIdProf) {
        Curso vCurso = cursoRepository
                .findById(pIdCurso)
                .orElseThrow(() -> new NoSuchElementException("Curso " + pIdCurso + " não cadastrado."));

        Professor vProfessor = profRepository
                .findById(pIdProf)
                .orElseThrow(() -> new NoSuchElementException("Professor " + pIdProf + " não cadastrado."));

        Curso vCursoAtual = vProfessor.getCursoProfessor();

        if (vCursoAtual.equals(vCurso))
            throw new DataIntegrityViolationException(
                    "Professor " + pIdProf + " já está associado com o curso " + pIdCurso + ".");
        else {
            vCursoAtual.getProfessores().remove(vProfessor);
            cursoRepository.save(vCursoAtual);
        }

        vProfessor.setCursoProfessor(vCurso);

        vCurso.getProfessores().add(vProfessor);

        return cursoRepository.save(vCurso);
    }

    public Curso insDisciplinaCurso(int pIdCurso, int pIdDisciplina) {
        Curso vCurso = cursoRepository
                .findById(pIdCurso)
                .orElseThrow(() -> new NoSuchElementException("Curso " + pIdCurso + " não cadastrado."));

        Disciplina vDisciplina = disciplinaRepository
                .findById(pIdDisciplina)
                .orElseThrow(() -> new NoSuchElementException("Disciplina " + pIdDisciplina + " não cadastrada."));

        if(vCurso.getDisciplinas().contains(vDisciplina))
            throw new DataIntegrityViolationException("Curso " + pIdCurso + " já está associado com a disciplina " + pIdDisciplina + ".");

        vCurso.getDisciplinas().add(vDisciplina);
        vDisciplina.getCursos().add(vCurso);

        disciplinaRepository.save(vDisciplina);
        return cursoRepository.save(vCurso);
    }

    public Curso insAlunoCurso(int pIdCurso, int pIdAluno) {
        Curso vCurso = cursoRepository
                .findById(pIdCurso)
                .orElseThrow(() -> new NoSuchElementException("Curso " + pIdCurso + " não cadastrado."));

        Aluno vAluno = alunoRepository
                .findById(pIdAluno)
                .orElseThrow(() -> new NoSuchElementException("Aluno " + pIdAluno + " não cadastrado."));

        if(vCurso.getAlunos().contains(vAluno))
            throw new DataIntegrityViolationException("Curso " + pIdCurso + " já está associado com o aluno " + pIdAluno + ".");

        vCurso.getAlunos().add(vAluno);
        vAluno.getCursos().add(vCurso);

        alunoRepository.save(vAluno);
        return cursoRepository.save(vCurso);
    }

    public Curso updCurso(Curso pCurso, int pIdArea) {
        int pIdCurso = pCurso.getIdCurso();

        Curso vCursoAtual = cursoRepository
                .findById(pIdCurso)
                .orElseThrow(() -> new NoSuchElementException("Curso " + pIdCurso + " não cadastrado."));

        Area vArea = areaRepository
                .findById(pIdArea)
                .orElseThrow(() -> new NoSuchElementException("Área " + pIdArea + " não cadastrada."));

        if (!vCursoAtual.getAreaCurso().equals(vArea)) {
            Area vAreaAtual = vCursoAtual.getAreaCurso();
            vAreaAtual.getCursos().remove(vCursoAtual);
            areaRepository.save(vAreaAtual);

            vCursoAtual.setAreaCurso(vArea);
            vArea.getCursos().add(vCursoAtual);
            areaRepository.save(vArea);
        }

        try {
            checarAtrib(pCurso, vCursoAtual);
            return cursoRepository.save(vCursoAtual);
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException(
                    "Os dados informados não atendem aos requisitos necessários.");
        } catch (ConstraintViolationException e) {
            throw new CustomConstraintViolantionException(
                    "Os dados informados não atendem aos requisitos necessários.");
        }
    }

    public void delCurso(int pIdCurso) {
        cursoRepository
                .findById(pIdCurso)
                .orElseThrow(() -> new NoSuchElementException("Curso " + pIdCurso + " não cadastrado."));

        cursoRepository.deleteById(pIdCurso);
    }

    @Override
    protected void checarAtrib(Curso pObjeto, Curso vObjAtual) {
        if (pObjeto.getNome() != null)
            vObjAtual.setNome(pObjeto.getNome());

        if (pObjeto.getHoras() != null)
            vObjAtual.setHoras(pObjeto.getHoras());
    }

}
