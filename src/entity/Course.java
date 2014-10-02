package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

@Entity
@NamedQueries({
    @NamedQuery(name = "Course.findAll", query = "SELECT c FROM Course c")
})
public class Course implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(length = 50)
    private String name;

    @Column(nullable = false)
    private String description;

    @ManyToMany
    @JoinTable(name = "COURSE_STUDENT",
            joinColumns = {
                @JoinColumn(name = "COURSE_ID", referencedColumnName = "ID")},
            inverseJoinColumns = {
                @JoinColumn(name = "STUDENT_ID", referencedColumnName = "ID")})

    private List<Student> students = new ArrayList();

    @ManyToMany
    @JoinTable(name = "COURSE_TEACHER",
            joinColumns = {
                @JoinColumn(name = "COURSE_ID", referencedColumnName = "ID")},
            inverseJoinColumns = {
                @JoinColumn(name = "TEACHER_ID", referencedColumnName = "ID")})

    private List<Teacher> teachers = new ArrayList();

    @ManyToMany
    @JoinTable(name = "COURSE_ASSTEACHER",
            joinColumns = {
                @JoinColumn(name = "COURSE_ID", referencedColumnName = "ID")},
            inverseJoinColumns = {
                @JoinColumn(name = "ASSTEACHER_ID", referencedColumnName = "ID")})

    private List<AssistantTeacher> assistantTeachers = new ArrayList();

    public Course() {
    }

    public Course(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public void addStudent(Student s) {
        students.add(s);
    }

    public void addTeacher(Teacher t) {
        teachers.add(t);
    }

    public void addAssistantTeacher(AssistantTeacher at) {
        assistantTeachers.add(at);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Course{" + "id=" + id + ", name=" + name + ", description=" + description + '}';
    }
}
