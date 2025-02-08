package gr.hua.dit.ds.DS_PROJECT.repositories;

import gr.hua.dit.ds.DS_PROJECT.entities.Property;
import gr.hua.dit.ds.DS_PROJECT.entities.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PropertyRepository extends JpaRepository<Property, Integer> {

    List<Property> findAllByStatus(Status status);
    List<Property> findAllByCity(String City);
}
