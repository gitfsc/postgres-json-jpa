package eu.java.pg.jsonb.repository;

import eu.java.pg.jsonb.domain.Professor;
import eu.java.pg.jsonb.domain.info.ProfessorInfo;

import java.util.List;
import java.util.Optional;


public interface ProfessorRepositoryCustom {

    void updateProfessorInfo (long professorId, ProfessorInfo newProfessorInfo);

    List<Professor> findProfessorsByName(Optional<String> firstName, Optional<String> lastName);
}
