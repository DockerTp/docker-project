
package fr.takima.training.simpleapi.controller;

import fr.takima.training.simpleapi.dto.StudentRequest;
import fr.takima.training.simpleapi.entity.Department;
import fr.takima.training.simpleapi.entity.Student;
import fr.takima.training.simpleapi.service.DepartmentService;
import fr.takima.training.simpleapi.service.StudentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping(value = "/students")
public class StudentController {

    private final StudentService studentService;
    private final DepartmentService departmentService;

    @Autowired
    public StudentController(
            StudentService studentService,
            DepartmentService departmentService) {
        this.studentService = studentService;
        this.departmentService = departmentService;
    }

    @GetMapping
    public ResponseEntity<Object> getStudents() {
        return ResponseEntity.ok(studentService.getAll());
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Object> getStudentById(
            @PathVariable(name = "id") long id) {

        Optional<Student> studentOptional = Optional.ofNullable(
                studentService.getStudentById(id));

        if (studentOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(studentOptional.get());
    }

    @PostMapping
    public ResponseEntity<Object> addStudent(
            @RequestBody StudentRequest request) {

        try {
            // Convert validated request fields into a JPA entity.
            Student student = toStudent(request);
            Student savedStudent = studentService.addStudent(student);

            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(savedStudent.getId())
                    .toUri();

            return ResponseEntity.created(location).build();

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<Object> updateStudent(
            @RequestBody StudentRequest request,
            @PathVariable(name = "id") long id) {

        Optional<Student> studentOptional = Optional.ofNullable(
                studentService.getStudentById(id));

        if (studentOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        try {
            // The path ID determines which student is updated.
            Student student = toStudent(request);
            student.setId(id);

            Student savedStudent = studentService.addStudent(student);
            return ResponseEntity.ok(savedStudent);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Object> removeStudent(
            @PathVariable(name = "id") long id) {

        Optional<Student> studentOptional = Optional.ofNullable(
                studentService.getStudentById(id));

        if (studentOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        studentService.removeStudentById(id);
        return ResponseEntity.ok().build();
    }

    // Explicitly map permitted request properties.
    // Never deserialize user input directly into a persistent entity.
    private Student toStudent(StudentRequest request) {
        if (request == null || request.department() == null) {
            throw new IllegalArgumentException("Department is required");
        }

        Department department = departmentService
                .getDepartmentById(request.department().id());

        Student student = new Student();
        student.setFirstname(request.firstname());
        student.setLastname(request.lastname());
        student.setDepartment(department);

        return student;
    }
}
