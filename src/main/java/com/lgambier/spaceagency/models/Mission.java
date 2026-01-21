package com.lgambier.spaceagency.models;

import com.lgambier.spaceagency.enums.MissionStatus;
import com.lgambier.spaceagency.validations.ShipStatusIsActive;
import com.lgambier.spaceagency.validations.ValidMissionDates;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Entity
@ValidMissionDates
@Table(name = "mission")
public class Mission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false)
    private Integer id;

    @NotNull
    @ShipStatusIsActive
    @ManyToOne(optional = false)
    @JoinColumn(name = "ship_id")
    private Ship ship;

    @NotNull
    @Column
    private LocalDateTime departureDate;

    @NotNull
    @Column
    private LocalDateTime arrivalDate;

    @NotBlank
    @Column
    private String origin;

    @NotBlank
    @Column
    private String destination;

    @NotNull
    @Column
    @Enumerated(EnumType.STRING)
    private MissionStatus status;

    @Column
    private Integer maxPassengers;

    @PrePersist
    @PreUpdate
    private void applyDefaultMaxPassengers() {
        if (maxPassengers == null && ship != null) {
            maxPassengers = ship.getCapacity();
        }
    }

}
