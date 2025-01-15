package gr.hua.dit.ds.DS_PROJECT.services;

import gr.hua.dit.ds.DS_PROJECT.entities.Booking;
import gr.hua.dit.ds.DS_PROJECT.entities.Property;
import gr.hua.dit.ds.DS_PROJECT.entities.User;
import gr.hua.dit.ds.DS_PROJECT.repositories.BookingRepository;
import gr.hua.dit.ds.DS_PROJECT.repositories.PropertyRepository;
import gr.hua.dit.ds.DS_PROJECT.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookingService {

    private BookingRepository bookingRepository;
    private PropertyRepository propertyRepository;
    private UserRepository userRepository;

    public BookingService(BookingRepository bookingRepository, PropertyRepository propertyRepository, UserRepository userRepository) {
        this.bookingRepository = bookingRepository;
        this.propertyRepository = propertyRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    @Transactional
    public Booking getBookingById(int id) {
        return bookingRepository.findById(id).get();
    }


    @Transactional
    public void assignPropertyAndTenantToBooking(int property_id, int user_id, Booking booking) {
        Property property = propertyRepository.findById(property_id).get();
        User user = userRepository.findById((long) user_id).get();
        booking.setProperty(property);
        booking.setUser(user);
        bookingRepository.save(booking);
    }

    @Transactional
    public void save(Booking booking) {
        bookingRepository.save(booking);
    }

    @Transactional
    public void removeBooking(int id) {
        bookingRepository.deleteById(id);
    }
}
