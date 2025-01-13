package com.web2.trabalhofinal.service;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.web2.trabalhofinal.model.Curso;
import com.web2.trabalhofinal.model.Disciplina;
import com.web2.trabalhofinal.model.Turma;
import com.web2.trabalhofinal.repository.CursoRepository;
import com.web2.trabalhofinal.repository.DisciplinaRepository;
import com.web2.trabalhofinal.repository.TurmaRepository;

@Service
public class DisciplinaService extends AbstractService<Disciplina> {

    @Autowired
    private DisciplinaRepository disciplinaRepository;

    @Autowired
    private TurmaRepository turmaRepository;

    @Autowired
    private CursoRepository cursoRepository;

    public List<Disciplina> findAll() {
        return disciplinaRepository.findAll();
    }

    public Disciplina findById(int pIdDisciplina) {
        return disciplinaRepository
                .findById(pIdDisciplina)
                .orElseThrow(() -> new NoSuchElementException("Disciplina " + pIdDisciplina + " não cadastrada."));
    }

    public Disciplina insDisciplina(Disciplina pDisciplina) {
        try {
            return disciplinaRepository.save(pDisciplina);
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException(
                    "Disciplina " + pDisciplina.getId() + " já cadastrada.");
        }
    }

    public Disciplina insTurmDisciplina(int pIdDisciplina, int pIdTurma) {
        Disciplina vDisciplina = disciplinaRepository
                .findById(pIdDisciplina)
                .orElseThrow(() -> new NoSuchElementException("Disciplina " + pIdDisciplina + " não cadastrada."));

        Turma vTurma = turmaRepository
                .findById(pIdTurma)
                .orElseThrow(() -> new NoSuchElementException("Turma " + pIdTurma + " não cadastrada."));

        List<Turma> turmas = vDisciplina.getTurmas();
        turmas.add(vTurma);
        vDisciplina.setTurmas(turmas);

        try {
            return disciplinaRepository.save(vDisciplina);
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException(
                    "Disciplina " + vDisciplina.getId() + " já cadastrada.");
        }
    }

    public Disciplina insCursoDisciplina(int pIdDisciplina, int pIdCurso) {
        Disciplina vDisciplina = disciplinaRepository
                .findById(pIdDisciplina)
                .orElseThrow(() -> new NoSuchElementException("Disciplina " + pIdDisciplina + " não cadastrada."));

        Curso vCurso = cursoRepository
                .findById(pIdCurso)
                .orElseThrow(() -> new NoSuchElementException("Curso " + pIdCurso + " não cadastrado."));

        if (vDisciplina.getCursos().contains(vCurso))
            throw new DataIntegrityViolationException(
                    "Curso " + pIdCurso + " já está associado com a disciplina " + pIdDisciplina + ".");

        vDisciplina.getCursos().add(vCurso);
        vCurso.getDisciplinas().add(vDisciplina);

        cursoRepository.save(vCurso);
        return disciplinaRepository.save(vDisciplina);
    }

    public Disciplina updDisciplina(Disciplina pDisciplina) {
        int pIdDisciplina = pDisciplina.getId();

        Disciplina vDisciplinaAtual = disciplinaRepository
                .findById(pIdDisciplina)
                .orElseThrow(() -> new NoSuchElementException("Disciplina " + pIdDisciplina + " não cadastrada."));

        checarAtrib(pDisciplina, vDisciplinaAtual);

        try {
            return disciplinaRepository.save(vDisciplinaAtual);
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException(
                    "Disciplina " + vDisciplinaAtual.getId() + " já cadastrada.");
        }
    }

    public void delDisciplina(int pIdDisciplina) {
        disciplinaRepository
                .findById(pIdDisciplina)
                .orElseThrow(() -> new NoSuchElementException("Disciplina " + pIdDisciplina + " não cadastrada."));

        disciplinaRepository.deleteById(pIdDisciplina);
    }

    @Override
    protected void checarAtrib(Disciplina pObjeto, Disciplina vObjAtual) {
        if (pObjeto.getNome() != null)
            pObjeto.setNome(vObjAtual.getNome());

        if (pObjeto.getHoras() != null)
            pObjeto.setHoras(vObjAtual.getHoras());

        if (pObjeto.getObjetivos() != null)
            pObjeto.setObjetivos(vObjAtual.getObjetivos());

        if (pObjeto.getConteudo() != null)
            pObjeto.setConteudo(vObjAtual.getConteudo());
    }
}
