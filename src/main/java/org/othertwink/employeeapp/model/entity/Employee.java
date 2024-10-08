package org.othertwink.employeeapp.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.othertwink.employeeapp.model.entity.enums.CompanyDepartment;
import org.othertwink.employeeapp.model.entity.enums.EmployeePosition;

import java.math.BigDecimal;

@Entity
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "employee")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;

    private BigDecimal salary;

    @Enumerated(EnumType.STRING)
    private EmployeePosition position;

    @Enumerated(EnumType.STRING)
    private CompanyDepartment department;

}
