package com.lgambier.spaceagency.dto.booking;

import com.lgambier.spaceagency.models.Booking;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingDTO {

    private Integer id;

    private Integer passengerId;

    private Integer missionId;

    public static BookingDTO toDTO(Booking booking) {
        return BookingDTO
                       .builder()
                       .id(booking.getId())
                       .passengerId(booking.getPassengerId())
                       .missionId(booking.getMissionId())
                       .build();
    }
}
