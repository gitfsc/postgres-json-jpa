package eu.java.pg.jsonb.domain;

import eu.java.pg.jsonb.types.JSONBUserType;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import lombok.Data;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Entity
@TypeDef(name = "professorJsonb", typeClass = JSONBUserType.class, parameters = {
        @Parameter(name = JSONBUserType.CLASS, value = "eu.java.pg.jsonb.domain.info.ProfessorInfo")})
public class Professor extends CommonPerson<eu.java.pg.jsonb.domain.info.ProfessorInfo> {

    @Type(type = "professorJsonb")
    @Column(name = "info")
    private eu.java.pg.jsonb.domain.info.ProfessorInfo info;

}
