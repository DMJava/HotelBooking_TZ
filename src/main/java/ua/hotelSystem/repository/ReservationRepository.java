package ua.hotelSystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.hotelSystem.model.Reservation;

import java.time.LocalDate;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Integer> {

    List<Reservation> findByReservedBy(String name);

    List<Reservation> findByStartDateLessThanEqualAndEndDateLessThanEqual(LocalDate date1, LocalDate date2);

}
