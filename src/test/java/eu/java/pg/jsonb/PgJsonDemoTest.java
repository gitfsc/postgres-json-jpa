package eu.java.pg.jsonb;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opentable.db.postgres.embedded.EmbeddedPostgres;
import eu.java.pg.jsonb.domain.CommonPerson;
import eu.java.pg.jsonb.domain.Professor;
import eu.java.pg.jsonb.domain.Student;
import eu.java.pg.jsonb.domain.info.ProfessorInfo;
import eu.java.pg.jsonb.domain.info.StudentInfo;
import eu.java.pg.jsonb.repository.CommonPersonRepository;
import eu.java.pg.jsonb.repository.ProfessorRepository;
import eu.java.pg.jsonb.repository.StudentRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static com.google.common.collect.Lists.newArrayList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {PgJsonApplication.class, PgJsonDemoTest.TestConfiguration.class})
@Slf4j
public class PgJsonDemoTest {

    @Autowired
    private ProfessorRepository professorRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private CommonPersonRepository commonPersonRepository;

    @Before
    public void setup() {
        commonPersonRepository.deleteAll();
    }

    @Test
    public void shouldSaveAndReadSameStudent() throws JsonProcessingException {

        Student student = studentRepository.save(createStudent());

        Student readStudent = studentRepository.findOne(student.getId());

        assertThat(readStudent, equalTo(student));

        log.error(new ObjectMapper().writeValueAsString(readStudent));
    }

    @Test
    public void shouldSaveAndreadSameProfessor() {

        Professor savedProfessor = professorRepository.save(createProfessor());

        Professor readProfessor = professorRepository.findOne(savedProfessor.getId());

        assertThat(readProfessor, equalTo(savedProfessor));
    }

    @Test
    public void shouldGetAllCommonPersons() {

        Professor professor = createProfessor();

        Student student = createStudent();

        professorRepository.saveAndFlush(professor);
        studentRepository.saveAndFlush(student);

        List<CommonPerson> readCommonPersons = commonPersonRepository.findAll();

        assertThat(readCommonPersons.size(), is(2));
        assertThat(readCommonPersons, contains(professor, student));
    }

    @Test
    public void shouldFindProfessorsByFirstName() {

        Professor searchedProfessor = createProfessor();
        searchedProfessor.getInfo().setFirstName("testFirstName");

        professorRepository.saveAndFlush(searchedProfessor);
        professorRepository.saveAndFlush(createProfessor());

        List<Professor> professorsByName = professorRepository.findProfessorsByName(Optional.of(searchedProfessor.getInfo().getFirstName()), Optional.empty());

        assertThat(professorsByName.size(), is(1));
        assertThat(professorsByName.get(0), is(searchedProfessor));
    }

    @Test
    public void shouldFindProfessorsByLastName() {

        String searchedLastName = "testLastName";

        Professor searchedProfessor1 = createProfessor();
        searchedProfessor1.getInfo().setFirstName("Alice");
        searchedProfessor1.getInfo().setLastName(searchedLastName);

        Professor searchedProfessor2 = createProfessor();
        searchedProfessor2.getInfo().setFirstName("Bob");
        searchedProfessor2.getInfo().setLastName(searchedLastName);

        professorRepository.saveAndFlush(searchedProfessor1);
        professorRepository.saveAndFlush(searchedProfessor2);
        professorRepository.saveAndFlush(createProfessor());

        List<Professor> professorsByName = professorRepository.findProfessorsByName(Optional.empty(),
                Optional.of(searchedLastName));

        assertThat(professorsByName.size(), is(2));
        assertThat(professorsByName, contains(searchedProfessor1, searchedProfessor2));
    }

    @Test
    public void shouldFindProfessorsByFirstAndLastName() {

        String searchedLastName = "testLastName";
        String searchedFirstName = "Alice";

        Professor searchedProfessor = createProfessor();
        searchedProfessor.getInfo().setFirstName(searchedFirstName);
        searchedProfessor.getInfo().setLastName(searchedLastName);

        Professor anyProfessor = createProfessor();
        anyProfessor.getInfo().setFirstName("Bob");
        anyProfessor.getInfo().setLastName(searchedLastName);

        professorRepository.saveAndFlush(searchedProfessor);
        professorRepository.saveAndFlush(anyProfessor);
        professorRepository.saveAndFlush(createProfessor());

        List<Professor> professorsByName = professorRepository.findProfessorsByName(Optional.of(searchedFirstName),
                Optional.of(searchedLastName));

        assertThat(professorsByName.size(), is(1));
        assertThat(professorsByName.get(0), is(searchedProfessor));
    }


    @Test
    public void shouldUpdateProfessorInfo() {

        //given
        Professor professor = professorRepository.saveAndFlush(createProfessor());
        professor.getInfo().setFirstName("New First Name");
        professor.getInfo().setLastName("New Last Name");
        professor.getInfo().setCourses(newArrayList("New course 1", "New course 2"));

        //when
        professorRepository.updateProfessorInfo(professor.getId(), professor.getInfo());

        //then
        List<Professor> allProfessors = professorRepository.findAll();
        assertThat(allProfessors.size(), is(1));
        assertThat(allProfessors.get(0), is(professor));
    }


    private Professor createProfessor() {

        Professor professor = new Professor();
        professor.setEmail("professor@pgjson.org");
        ProfessorInfo professorInfo = new eu.java.pg.jsonb.domain.info.ProfessorInfo();
        professorInfo.setFirstName("Archibald");
        professorInfo.setLastName("Wisconsin");
        professorInfo.setCourses(newArrayList("Physics", "Mathematics"));
        professor.setInfo(professorInfo);
        return professor;
    }

    private Student createStudent() {
        Student student = new Student();
        student.setEmail("student@pgjson.org");
        StudentInfo studentInfo = new StudentInfo();
        studentInfo.setAge(20);
        studentInfo.setFirstName("Johnny");
        studentInfo.setLastName("Ventura");
        student.setInfo(studentInfo);
        return student;
    }

    public static class TestConfiguration {

        @Bean
        public DataSource dataSource() throws IOException {
            return embeddedPostgres().getPostgresDatabase();
        }

        @Bean
        public EmbeddedPostgres embeddedPostgres() throws IOException {
            return EmbeddedPostgres.start();
        }

        @Bean
        public ObjectMapper objectMapper(){
            return new ObjectMapper();
        }
    }


}
