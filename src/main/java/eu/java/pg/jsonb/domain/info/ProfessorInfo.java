package eu.java.pg.jsonb.domain.info;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Singular;
import lombok.ToString;

import java.util.List;
import java.util.Objects;

@Data
@EqualsAndHashCode
@ToString
public class ProfessorInfo implements Info {
    private String firstName;
    private String lastName;

    @Singular
    private List<String> courses;

}
