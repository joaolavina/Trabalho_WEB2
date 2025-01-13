package com.web2.trabalhofinal.service;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.web2.trabalhofinal.exceptions.CustomConstraintViolantionException;
import com.web2.trabalhofinal.model.Aluno;
import com.web2.trabalhofinal.model.Disciplina;
import com.web2.trabalhofinal.model.Professor;
import com.web2.trabalhofinal.model.Turma;
import com.web2.trabalhofinal.repository.AlunoRepository;
import com.web2.trabalhofinal.repository.DisciplinaRepository;
import com.web2.trabalhofinal.repository.ProfessorRepository;
import com.web2.trabalhofinal.repository.TurmaRepository;

import jakarta.validation.ConstraintViolationException;

@Service
public class TurmaService extends AbstractService<Turma> {

    @Autowired
    private TurmaRepository turmaRepository;

    @Autowired
    private DisciplinaRepository disciplinaRepository;

    @Autowired
    private ProfessorRepository profRepository;

    @Autowired
    private AlunoRepository alunoRepository;

    public List<Turma> findAll() {
        return turmaRepository.findAll();
    }

    public Turma findById(int pIdTurma) {
        return turmaRepository
                .findById(pIdTurma)
                .orElseThrow(() -> new NoSuchElementException("Turma " + pIdTurma + " não cadastrada."));
    }

    public Turma insTurma(Turma pTurma, int pIdProf, int pIdDisciplina) {
        Professor vProf = profRepository
                .findById(pIdProf)
                .orElseThrow(() -> new NoSuchElementException("Professor " + pIdProf + " não cadastrado."));

        Disciplina vDisciplina = disciplinaRepository
                .findById(pIdDisciplina)
                .orElseThrow(() -> new NoSuchElementException("Disciplina " + pIdDisciplina + " não cadastrada."));

        pTurma.setDisciplinaTurma(vDisciplina);
        pTurma.setProfessorTurma(vProf);

        try {
            return turmaRepository.save(pTurma);
        } catch (ConstraintViolationException e) {
            throw new CustomConstraintViolantionException("Erro na validação dos dados informados.");
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException("Erro na validação dos dados informados.");
        }
    }

    public Turma insAlunoTurma(int pIdAluno, int pIdTurma) {
        Turma vTurma = turmaRepository
                .findById(pIdTurma)
                .orElseThrow(() -> new NoSuchElementException("Turma " + pIdTurma + " não cadastrada."));

        Aluno vAluno = alunoRepository
                .findById(pIdAluno)
                .orElseThrow(() -> new NoSuchElementException("Aluno " + pIdAluno + " não cadastrado."));

        if (vTurma.getAlunos().contains(vAluno))
            throw new DataIntegrityViolationException(
                    "Aluno " + pIdAluno + " já está associado com a turma " + pIdTurma + ".");

        vTurma.getAlunos().add(vAluno);
        vAluno.getTurmas().add(vTurma);

        alunoRepository.save(vAluno);
        return turmaRepository.save(vTurma);
    }

    public Turma updTurma(Turma pTurma, int pIdProf, int pIdDisciplina) {
        int pIdTurma = pTurma.getId();

        Turma vTurmaAtual = turmaRepository.findById(pIdTurma)
                .orElseThrow(() -> new NoSuchElementException("Turma " + pIdTurma + " não cadastrada."));

        Professor vProf = profRepository
                .findById(pIdProf)
                .orElseThrow(() -> new NoSuchElementException("Professor " + pIdProf + " não cadastrado."));

        Disciplina vDisciplina = disciplinaRepository
                .findById(pIdDisciplina)
                .orElseThrow(() -> new NoSuchElementException("Disciplina " + pIdDisciplina + " não cadastrada."));

        if(!vTurmaAtual.getProfessorTurma().equals(vProf)){
            Professor vProfAtual = vTurmaAtual.getProfessorTurma();
            vProfAtual.getTurmas().remove(vTurmaAtual);
            profRepository.save(vProfAtual);

            vTurmaAtual.setProfessorTurma(vProf);
            vProf.getTurmas().add(vTurmaAtual);
            profRepository.save(vProf);
        }

        if(!vTurmaAtual.getDisciplinaTurma().equals(vDisciplina)){
            Disciplina vDisciplinaAtual = vTurmaAtual.getDisciplinaTurma();
            vDisciplinaAtual.getTurmas().remove(vTurmaAtual);
            disciplinaRepository.save(vDisciplinaAtual);

            vTurmaAtual.setDisciplinaTurma(vDisciplina);
            vDisciplina.getTurmas().add(vTurmaAtual);
            disciplinaRepository.save(vDisciplina);
        }

        try {
            checarAtrib(pTurma, vTurmaAtual);
            return turmaRepository.save(vTurmaAtual);
        } catch (ConstraintViolationException e) {
            throw new CustomConstraintViolantionException("Erro na validação dos dados informados.");
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException("Erro na validação dos dados informados.");
        }
    }

    public void delTurma(int pIdTurma) {
        turmaRepository
                .findById(pIdTurma)
                .orElseThrow(() -> new NoSuchElementException("Turma " + pIdTurma + " não cadastrada."));

        turmaRepository.deleteById(pIdTurma);
    }

    @Override
    protected void checarAtrib(Turma pObjeto, Turma vObjAtual) {
        if (pObjeto.getDtInicio() != null)
            vObjAtual.setDtInicio(pObjeto.getDtInicio());

        if (pObjeto.getDtFim() != null)
            vObjAtual.setDtFim(pObjeto.getDtFim());

        if (pObjeto.getTurno() != null)
            vObjAtual.setTurno(pObjeto.getTurno());
    }

}
