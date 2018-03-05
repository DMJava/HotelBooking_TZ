package ua.hotelSystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.hotelSystem.model.Category;
import ua.hotelSystem.model.Room;

import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room, Integer> {

    List<Room> findByIdIsNotIn(List<Integer> list);

    List<Room> findByIdIsNotInAndBreakfast(List<Integer> list, String breakFast);

    List<Room> findByIdIsNotInAndRoomClean(List<Integer> list, String roomClean);

    List<Room> findByBreakfast(String breakfast);

    List<Room> findByRoomClean(String roomClean);

    List<Room> findByIdIsNotInAndWithBreakfastAndRoomClean (List<Integer> list, String breakfast, String roomClean);

    List<Room> findByIdIsNotInAndPrice(List<Room> list, int price);

    List<Room> findByIdIsNotInAndCategory(List<Room> list, Category category);
}
