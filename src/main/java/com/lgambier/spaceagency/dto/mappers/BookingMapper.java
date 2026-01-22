package com.lgambier.spaceagency.dto.mappers;

import com.lgambier.spaceagency.dto.booking.BookingDTO;
import com.lgambier.spaceagency.dto.passenger.PassengerDTO;
import com.lgambier.spaceagency.models.Booking;
import com.lgambier.spaceagency.models.Passenger;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface BookingMapper {
    BookingMapper INSTANCE = Mappers.getMapper(BookingMapper.class);

    BookingDTO bookingToBookingDto(Booking booking);

    Booking bookingDtoToBooking(BookingDTO bookingDTO);
}
