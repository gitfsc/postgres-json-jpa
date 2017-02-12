package eu.java.pg.jsonb.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;


@Data
@MappedSuperclass
@EqualsAndHashCode
@ToString(callSuper = true)
public class Person {
    @Id
    @GeneratedValue
    protected Long id;

    protected String email;


}
