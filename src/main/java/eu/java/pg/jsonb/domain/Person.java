package eu.java.pg.jsonb.domain;

import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;


@Data
@MappedSuperclass
public class Person {
    @Id
    @GeneratedValue
    protected Long id;

    protected String email;


}
