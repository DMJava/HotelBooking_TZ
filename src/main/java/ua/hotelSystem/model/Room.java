package ua.hotelSystem.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "room")
public class Room {

    @Id
    private int number;
    @Enumerated(EnumType.STRING)
    private Category category;
    private int price;
    private boolean breakfast;
    private boolean roomClean;
    @OneToMany(mappedBy = "room")
    private List<Reservation> reservationList = new ArrayList<>();

    public Room() {
    }

    public Room(int number, Category category, int price, boolean breakfast, boolean roomClean) {
        this.number = number;
        this.category = category;
        this.price = price;
        this.breakfast = breakfast;
        this.roomClean = roomClean;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public boolean isBreakfast() {
        return breakfast;
    }

    public void setBreakfast(boolean breakfast) {
        this.breakfast = breakfast;
    }

    public boolean isRoomClean() {
        return roomClean;
    }

    public void setRoomClean(boolean roomClean) {
        this.roomClean = roomClean;
    }

    public List<Reservation> getReservationList() {
        return reservationList;
    }

    public void setReservationList(List<Reservation> reservationList) {
        this.reservationList = reservationList;
    }
}
