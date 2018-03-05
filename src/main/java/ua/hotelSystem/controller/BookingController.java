package ua.hotelSystem.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ua.hotelSystem.model.Room;
import ua.hotelSystem.service.HotelService;

import java.security.Principal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

@Controller
public class BookingController {

    private static final Logger LOGGER = LoggerFactory.getLogger(BookingController.class);

    @Autowired
    private HotelService hotelService;

    @RequestMapping("/mainPage")
    public String mainPage(Model model) {
        return "main";
    }

    @PostMapping("/showRooms")
    public String showRooms(Model model, @RequestParam("startDate") String startDate,
                            @RequestParam("endDate") String endDate,
                            @RequestParam String breakfast, @RequestParam String roomClean) {
        LocalDate date1;
        LocalDate date2;
        List<Room> availableRooms;
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        try {
            date1 = LocalDate.from(dateTimeFormatter.parse(startDate));
            date2 = LocalDate.from(dateTimeFormatter.parse(endDate));
        } catch (Exception e) {
            model.addAttribute("error", ("Pick date"));
            return "reservation page";
        }
        if (!hotelService.isReservationValid(date1, date2)) {
            model.addAttribute("error", ("Invalid date " + " start date " + date1 + " and end date " + date2));
            return "reservation page";
        }
        availableRooms = hotelService.findAvailableRooms(date1, date2, breakfast, roomClean);
        List<LocalDate> days = hotelService.getDaysOfReservation(date1, date2);
        model.addAttribute("days", days);
        model.addAttribute("rooms", availableRooms);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        return "rooms page";
    }

    @PostMapping("/reserveRooms")
    public String reserveRoom(Model model, @RequestParam int roomNumber,
                              @RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate,
                              @RequestParam String[] days, Principal principal) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date1 = LocalDate.from(dateTimeFormatter.parse(startDate));
        LocalDate date2 = LocalDate.from(dateTimeFormatter.parse(endDate));
        if (hotelService.createReservation(roomNumber, date1, date2, principal.getName(), Arrays.asList(days))) {
            LOGGER.info("Created reservation for room : \" + roomId + \" on date : \" + date1 + \" to \" + date2");
        } else {
            model.addAttribute("error", ("Failed to create reservation for room : " + roomNumber + " on date : " + date1 + " to " + date2));
            LOGGER.error("Failed to create reservation for room : " + roomNumber + " on date : " + date1 + " to " + date2);
            return "reservation page";
        }
        return "redirect:/find reservations";
    }

    @RequestMapping("/reservation page")
    public String reservationPage(Model model) {
        model.addAttribute("breakfastServices", hotelService.getAvailableBreakfastServices());
        model.addAttribute("roomCleanServices", hotelService.getAvailableRoomCleanServices());
        return "reservation page";
    }

    @RequestMapping("/findReservations")
    public String showReservations(Model model, Principal principal) {
        model.addAttribute("reservation", hotelService.findReservations(principal.getName()));
        return "reservation list page";
    }
}
