
package fr.takima.training.simpleapi.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

// Only allow the fields required to create or update a student.
// The student's database ID is not accepted from the request.
@JsonIgnoreProperties(ignoreUnknown = true)
public record StudentRequest(
        String firstname,
        String lastname,
        DepartmentReference department
) {
    // The client can select an existing department by ID,
    // but cannot modify its database properties.
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record DepartmentReference(Long id) {
    }
}
