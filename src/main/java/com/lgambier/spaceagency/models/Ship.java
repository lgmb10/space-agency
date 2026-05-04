package com.lgambier.spaceagency.models;

import com.lgambier.spaceagency.enums.ShipStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "ship", uniqueConstraints = {@UniqueConstraint(name = "uk_ship_name", columnNames = "name")})
public class Ship {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false)
    private Integer id;

    @NotBlank
    @Column(nullable = false)
    @Length(max = 100)
    private String name;

    @NotNull
    @Column
    @Positive
    private Integer capacity;

    @NotNull
    @Column
    @Positive
    private Integer maxWeight;

    @NotNull
    @Column
    @Enumerated(EnumType.STRING)
    private ShipStatus status = ShipStatus.ACTIVE;

}
