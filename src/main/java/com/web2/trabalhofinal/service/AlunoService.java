package com.web2.trabalhofinal.service;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.web2.trabalhofinal.exceptions.CustomConstraintViolantionException;
import com.web2.trabalhofinal.model.Aluno;
import com.web2.trabalhofinal.model.Curso;
import com.web2.trabalhofinal.model.Turma;
import com.web2.trabalhofinal.repository.AlunoRepository;
import com.web2.trabalhofinal.repository.CursoRepository;
import com.web2.trabalhofinal.repository.TurmaRepository;

import jakarta.validation.ConstraintViolationException;

@Service
public class AlunoService extends AbstractService<Aluno> {

    @Autowired
    private AlunoRepository alunoRepository;

    @Autowired
    private TurmaRepository turmaRepository;

    @Autowired
    private CursoRepository cursoRepository;

    public List<Aluno> findAll() {
        return alunoRepository.findAll();
    }

    public Aluno findById(int pIdAluno) {
        return alunoRepository
                .findById(pIdAluno)
                .orElseThrow(() -> new NoSuchElementException("Aluno " + pIdAluno + " não cadastrado."));
    }

    public Aluno insAluno(Aluno pAluno) {
        try {
            return alunoRepository.save(pAluno);
        } catch (ConstraintViolationException e) {
            throw new CustomConstraintViolantionException("Erro na validação dos dados informados.");
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException("Erro na validação dos dados informados.");
        }
    }

    public Aluno insTurmaAluno(int pIdTurma, int pIdAluno) {
        Aluno vAluno = alunoRepository
                .findById(pIdAluno)
                .orElseThrow(() -> new NoSuchElementException("Aluno " + pIdAluno + " não cadastrado."));

        Turma vTurma = turmaRepository
                .findById(pIdTurma)
                .orElseThrow(() -> new NoSuchElementException("Turma " + pIdTurma + " não cadastrada."));

        if (vAluno.getTurmas().contains(vTurma))
            throw new DataIntegrityViolationException(
                    "Turma " + pIdTurma + " já está associada com o aluno " + pIdAluno + ".");

        vAluno.getTurmas().add(vTurma);
        vTurma.getAlunos().add(vAluno);

        turmaRepository.save(vTurma);
        return alunoRepository.save(vAluno);
    }

    public Aluno insCursoAluno(int pIdAluno, int pIdCurso) {
        Aluno vAluno = alunoRepository
                .findById(pIdAluno)
                .orElseThrow(() -> new NoSuchElementException("Aluno " + pIdAluno + " não cadastrado."));

        Curso vCurso = cursoRepository
                .findById(pIdCurso)
                .orElseThrow(() -> new NoSuchElementException("Curso " + pIdCurso + " não cadastrado."));

        if (vAluno.getCursos().contains(vCurso))
            throw new DataIntegrityViolationException(
                    "Aluno " + pIdAluno + " já está associado com o curso " + pIdCurso + ".");

        vAluno.getCursos().add(vCurso);
        vCurso.getAlunos().add(vAluno);

        cursoRepository.save(vCurso);
        return alunoRepository.save(vAluno);
    }

    public Aluno updAluno(Aluno pAluno) {
        int pIdAluno = pAluno.getId();

        Aluno vAlunoAtual = alunoRepository
                .findById(pIdAluno)
                .orElseThrow(() -> new NoSuchElementException("Aluno " + pIdAluno + " não cadastrado."));

        checarAtrib(pAluno, vAlunoAtual);

        try {
            return alunoRepository.save(pAluno);
        } catch (ConstraintViolationException e) {
            throw new CustomConstraintViolantionException("Erro na validação dos dados informados.");
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException("Erro na validação dos dados informados.");
        }
    }

    public void delAluno(int pIdAluno) {
        alunoRepository
                .findById(pIdAluno)
                .orElseThrow(() -> new NoSuchElementException("Aluno " + pIdAluno + " não cadastrado."));

        alunoRepository.deleteById(pIdAluno);
    }

    @Override
    protected void checarAtrib(Aluno pObjeto, Aluno vObjAtual) {
        if (pObjeto.getNome() != null)
            vObjAtual.setNome(pObjeto.getNome());

        if (pObjeto.getEmail() != null)
            vObjAtual.setEmail(pObjeto.getEmail());

        if (pObjeto.getDtNascimento() != null)
            vObjAtual.setDtNascimento(pObjeto.getDtNascimento());
    }

}
