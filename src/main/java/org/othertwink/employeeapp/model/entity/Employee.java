package org.othertwink.employeeapp.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.othertwink.employeeapp.model.entity.enums.CompanyDepartment;
import org.othertwink.employeeapp.model.entity.enums.EmployeePosition;
import org.othertwink.employeeapp.model.entity.enums.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

@Entity
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "employee")
public class Employee implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;
    private String email;

    @Column(unique = true, nullable = false)
    private String username;

    private BigDecimal salary;

    @Enumerated(EnumType.STRING)
    private EmployeePosition position;

    @Enumerated(EnumType.STRING)
    private CompanyDepartment department;

    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    private boolean isAccountNonLocked;

    private int failedLoginAttempts;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return isAccountNonLocked;
    }
}