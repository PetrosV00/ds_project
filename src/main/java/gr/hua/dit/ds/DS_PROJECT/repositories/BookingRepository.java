package gr.hua.dit.ds.DS_PROJECT.repositories;

import gr.hua.dit.ds.DS_PROJECT.entities.Booking;
import gr.hua.dit.ds.DS_PROJECT.entities.Property;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Integer> {

    List<Booking> getAllBookingsByProperty(Property property);
}
