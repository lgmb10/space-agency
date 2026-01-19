package com.lgambier.spaceagency.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "passenger", uniqueConstraints = {@UniqueConstraint(name = "uk_passenger_email", columnNames = "email")})
public class Passenger {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false)
    private Integer id;

    @NotBlank
    @Column
    private String firstName;

    @NotBlank
    @Column
    private String lastName;

    @NotBlank
    @Column
    @Email
    private String email;

    @NotNull
    @Column
    @Positive
    private Integer weight;

    @NotNull
    @Column
    @AssertTrue
    private Boolean medicalClearance;

}
