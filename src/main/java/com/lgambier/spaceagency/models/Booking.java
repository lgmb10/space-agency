package com.lgambier.spaceagency.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "booking", uniqueConstraints = {@UniqueConstraint(name = "uk_booking_passenger_mission", columnNames = {"passengerId", "missionId"})})
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false)
    private Integer id;

    @NotNull
    private Integer passengerId;

    @NotNull
    private Integer missionId;
}
