package org.othertwink.employeeapp.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.othertwink.employeeapp.model.entity.enums.CompanyDepartment;

@Entity
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "department")
public class Department {

    @jakarta.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private CompanyDepartment department;
}
