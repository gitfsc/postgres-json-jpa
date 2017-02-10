package eu.java.pg.jsonb.domain.info;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode
@ToString
public class StudentInfo implements Info {
    private String firstName;
    private String lastName;
    private int age;

}
