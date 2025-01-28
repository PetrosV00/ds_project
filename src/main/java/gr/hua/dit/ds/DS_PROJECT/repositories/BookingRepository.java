package gr.hua.dit.ds.DS_PROJECT.repositories;

import gr.hua.dit.ds.DS_PROJECT.entities.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<Booking, Integer> {
}
