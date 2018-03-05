package ua.hotelSystem.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.hotelSystem.model.Category;
import ua.hotelSystem.model.Reservation;
import ua.hotelSystem.model.Room;
import ua.hotelSystem.repository.ReservationRepository;
import ua.hotelSystem.repository.RoomRepository;
import ua.hotelSystem.repository.UserRepository;
import ua.hotelSystem.service.HotelService;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Transactional
public class HotelServiceImpl implements HotelService {

    private static final Logger LOGGER = LoggerFactory.getLogger(HotelServiceImpl.class);

    private UserRepository userRepository;
    private RoomRepository roomRepository;
    private ReservationRepository reservationRepository;

    @Autowired
    public HotelServiceImpl(UserRepository userRepository, RoomRepository roomRepository, ReservationRepository reservationRepository) {
        this.userRepository = userRepository;
        this.roomRepository = roomRepository;
        this.reservationRepository = reservationRepository;
    }

    @Override
    public boolean isReservationValid(LocalDate startDate, LocalDate endDate) {
        if (startDate.isAfter(endDate)) {
            return false;
        }
        if (startDate.isBefore(LocalDate.now())) {
            return false;
        }

        long days = ChronoUnit.DAYS.between(startDate, endDate);
        if ((days + 1) > 7) {
            return false;
        }
        return true;
    }

    @Override
    public boolean createReservation(int roomNumber, LocalDate startDate, LocalDate endDate, String name, List<String> days) {
        if (!isReservationValid(startDate, endDate)) {
            return false;
        }
        Reservation reservation = new Reservation(startDate, endDate);
        reservation.setReservedBy(name);
        Room roomR = roomRepository.findOne(roomNumber);
        List<Reservation> reservationList = roomR.getReservationList();
        reservationList.add(reservation);
        roomR.setReservationList(reservationList);
        reservation.setRoom(roomR);
        reservationRepository.save(reservation);
        return true;
    }

    @Override
    public boolean createReservation(int roomNumber, LocalDate startTime, LocalDate endTime, String name) {
        if (!isReservationValid(startTime, endTime)) {
            return false;
        }
        Reservation reservation = new Reservation(startTime, endTime);
        reservation.setReservedBy(name);
        Room roomR = roomRepository.findOne(roomNumber);
        List<Reservation> reservationList = roomR.getReservationList();
        reservationList.add(reservation);
        roomR.setReservationList(reservationList);
        reservation.setRoom(roomR);
        reservationRepository.save(reservation);
        return true;
    }

    @Override
    public List<Room> findAvailableRooms(LocalDate date1, LocalDate date2, String breakfast, String roomClean) {
        List<Room> availableRooms = new ArrayList<>();
        if (!breakfast.equals("All") && roomClean.equals("All")) {
            availableRooms = findAvailableRoomsWithBreakfast(date1, date2, breakfast);
            LOGGER.info("Find available rooms with breakfast");
        } else if (breakfast.equals("All") && !roomClean.equals("All")) {
            availableRooms = findAvailableRoomsWithRoomClean(date1, date2, roomClean);
            LOGGER.info("Find available rooms with roomClean");
        } else if (!breakfast.equals("All") && !roomClean.equals("All")) {
            availableRooms = findAvailableRoomsWithBreakfastAndRoomClean(date1, date2, breakfast, roomClean);
            LOGGER.info("Find available rooms with breakfast and roomClean");
        } else {
            availableRooms = findAllAvailableRooms(date1, date2);
            LOGGER.info("Find all available rooms");
        }
        return availableRooms;
    }

    @Override
    public List<Room> findAllAvailableRooms(LocalDate startDate, LocalDate endDate) {
        List<Reservation> reservationList = reservationRepository.findByStartDateLessThanEqualAndEndDateLessThanEqual(startDate, endDate);
        List<Integer> reservationIds = new ArrayList<>();
        for (Reservation reservation : reservationList) {
            reservationIds.add(reservation.getId());
        }
        if (reservationIds.size() != 0) {
            return roomRepository.findByIdIsNotIn(reservationIds);
        }
        return roomRepository.findAll();
    }

    @Override
    public List<Room> findAvailableRoomsWithBreakfast(LocalDate startDate, LocalDate endDate, String breakfast) {
        List<Reservation> reservationList = reservationRepository.findByStartDateLessThanEqualAndEndDateLessThanEqual(startDate, endDate);
        List<Integer> reservationIds = new ArrayList<>();
        for (Reservation reservation : reservationList) {
            reservationIds.add(reservation.getId());
        }
        if (reservationIds.size() != 0) {
            return roomRepository.findByIdIsNotInAndBreakfast(reservationIds, breakfast);
        }
        return roomRepository.findByBreakfast(breakfast);
    }

    @Override
    public List<Room> findAvailableRoomsWithRoomClean(LocalDate startDate, LocalDate endDate, String roomClean) {
        List<Reservation> reservationList = reservationRepository.findByStartDateLessThanEqualAndEndDateLessThanEqual(startDate, endDate);
        List<Integer> reservationIds = new ArrayList<>();
        for (Reservation reservation : reservationList) {
            reservationIds.add(reservation.getId());
        }
        if (reservationIds.size() != 0) {
            return roomRepository.findByIdIsNotInAndRoomClean(reservationIds, roomClean);
        }
        return roomRepository.findByRoomClean(roomClean);
    }

    @Override
    public List<Room> findAvailableRoomsWithBreakfastAndRoomClean(LocalDate date1, LocalDate date2, String breakfast, String roomClean) {
        List<Reservation> reservationList = reservationRepository.findByStartDateLessThanEqualAndEndDateLessThanEqual(date1, date2);
        List<Integer> reservationIds = new ArrayList<>();
        for (Reservation reservation : reservationList) {
            reservationIds.add(reservation.getId());
        }
        if (reservationIds.size() != 0) {
            return roomRepository.findByIdIsNotInAndWithBreakfastAndRoomClean(reservationIds, breakfast, roomClean);
        }
        return roomRepository.findByIdIsNotInAndWithBreakfastAndRoomClean(reservationIds, breakfast, roomClean);
    }

    @Override
    public List<Reservation> findReservations(String name) {
        return reservationRepository.findByReservedBy(name);
    }

    @Override
    public List<Room> findByPrice(List<Room> roomRList, int price) {
        return roomRepository.findByIdIsNotInAndPrice(roomRList, price);
    }

    @Override
    public List<Room> findByCategory(Category category) {
        List<Room> roomRList = new ArrayList<>();
        return roomRepository.findByIdIsNotInAndCategory(roomRList, category);
    }

    @Override
    public List<LocalDate> getDaysOfReservation(LocalDate startDate, LocalDate endDate) {
        return Stream.iterate(startDate, date -> date.plusDays(1))
                .limit((ChronoUnit.DAYS.between(startDate, endDate)) + 1)
                .collect(Collectors.toList());
    }

    @Override
    public List<Reservation> findReservation(String name) {
        return reservationRepository.findByReservedBy(name);
    }

    @Override
    public List<String> getAvailableBreakfastServices() {
        List<String> roomsWithBreakfast = new ArrayList<>();
        Room room = new Room();
        if (room.isBreakfast()){
            roomsWithBreakfast.add(String.valueOf(room));
        }
        return roomsWithBreakfast;
    }

    @Override
    public List<String> getAvailableRoomCleanServices() {
        List<String> roomsWithRoomClean = new ArrayList<>();
        Room room = new Room();
        if (room.isRoomClean()){
            roomsWithRoomClean.add(String.valueOf(room));
        }
        return roomsWithRoomClean;
    }
}
