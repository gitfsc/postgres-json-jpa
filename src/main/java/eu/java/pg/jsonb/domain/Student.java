package eu.java.pg.jsonb.domain;

import eu.java.pg.jsonb.domain.info.StudentInfo;
import eu.java.pg.jsonb.types.JSONBUserType;
import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.Column;
import javax.persistence.Entity;

@Data
@ToString
@Entity
@TypeDef(name = "studentJsonb", typeClass = JSONBUserType.class, parameters = {
        @Parameter(name = JSONBUserType.CLASS, value = "eu.java.pg.jsonb.domain.info.StudentInfo")})
public class Student extends CommonPerson<StudentInfo> {

    @Type(type = "studentJsonb")
    @Column(name = "info")
    private StudentInfo info;

}
