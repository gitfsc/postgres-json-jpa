package eu.java.pg.jsonb.repository;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.java.pg.jsonb.domain.Professor;
import eu.java.pg.jsonb.domain.info.ProfessorInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

public class ProfessorRepositoryImpl implements ProfessorRepositoryCustom{

    @Autowired
    EntityManager em;

    @Autowired
    ObjectMapper objectMapper;

    @Override
    @Transactional
    public void updateProfessorInfo(long professorId, ProfessorInfo newProfessorInfo) {

        try {
            //TODO should be possible to do the conversion internally
            String professorInfoJson = objectMapper.writeValueAsString(newProfessorInfo);

            String query = "UPDATE person SET info = '"+professorInfoJson+"' WHERE id="+professorId;

            em.createNativeQuery(query).executeUpdate();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Professor> findProfessorsByName(Optional<String> firstName, Optional<String> lastName) {

        String query = buildFindByNameQueryString(firstName, lastName);

        return em.createNativeQuery(query, Professor.class).getResultList();
    }

    private String buildFindByNameQueryString(Optional<String> firstName, Optional<String> lastName){

        String queryString = "SELECT * FROM person p";

        if(firstName.isPresent() || lastName.isPresent()){
            queryString += " WHERE ";
        }

        if(firstName.isPresent()){
            queryString += "p.info ->> 'firstName' = '"+firstName.get()+"'";

            if(lastName.isPresent()){
                queryString += " AND ";
            }
        }

        if(lastName.isPresent()){
            queryString += "p.info ->> 'lastName' = '"+lastName.get()+"'";
        }

        return queryString;
    }

}
