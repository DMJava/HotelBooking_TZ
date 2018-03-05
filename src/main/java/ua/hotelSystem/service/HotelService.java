package ua.hotelSystem.service;

import ua.hotelSystem.model.Category;
import ua.hotelSystem.model.Reservation;
import ua.hotelSystem.model.Room;

import java.time.LocalDate;
import java.util.List;

public interface HotelService {

    boolean isReservationValid(LocalDate startDate, LocalDate endDate);

    boolean createReservation(int roomNumber, LocalDate startDate, LocalDate endDate, String name, List<String> days);

    boolean createReservation(int roomNumber, LocalDate startTime, LocalDate endTime, String name);

    List<Room> findAvailableRooms(LocalDate date1, LocalDate date2, String breakfast, String roomClean);

    List<Room> findAllAvailableRooms(LocalDate startDate, LocalDate endDate);

    List<Room> findAvailableRoomsWithBreakfast(LocalDate startDate, LocalDate endDate, String breakfast);

    List<Room> findAvailableRoomsWithRoomClean(LocalDate startDate, LocalDate endDate, String roomClean);

    List<Room> findAvailableRoomsWithBreakfastAndRoomClean(LocalDate date1, LocalDate date2, String breakfast, String roomClean);

    List<Reservation> findReservations(String name);

    List<Room> findByPrice(List<Room> roomRList, int price);

    List<Room> findByCategory(Category category);

    List<LocalDate> getDaysOfReservation(LocalDate startDate, LocalDate endDate);

    List<Reservation> findReservation(String name);

    List<String> getAvailableBreakfastServices();

    List<String> getAvailableRoomCleanServices();
}
