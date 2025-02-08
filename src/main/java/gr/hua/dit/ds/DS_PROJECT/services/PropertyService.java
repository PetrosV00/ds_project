package gr.hua.dit.ds.DS_PROJECT.services;

import gr.hua.dit.ds.DS_PROJECT.entities.Property;
import gr.hua.dit.ds.DS_PROJECT.entities.Status;
import gr.hua.dit.ds.DS_PROJECT.entities.User;
import gr.hua.dit.ds.DS_PROJECT.repositories.PropertyRepository;
import gr.hua.dit.ds.DS_PROJECT.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PropertyService {

    private PropertyRepository propertyRepository;
    private UserRepository userRepository;


    public PropertyService(PropertyRepository propertyRepository, UserRepository userRepository) {
        this.propertyRepository = propertyRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public List<Property> getProperties() {
        return propertyRepository.findAll();
    }

    @Transactional
    public Property getProperty(int id) {
        return propertyRepository.findById(id).get();
    }

    @Transactional
    public void save(Property property) {
        propertyRepository.save(property);
    }

    @Transactional
    public void assignLandlordToProperty(int user_id, Property property) {
        User user = userRepository.findById((long) user_id).get();
        property.setUser(user);
        propertyRepository.save(property);
    }

    @Transactional
    public List<Property> getApprovedProperties() {
        return propertyRepository.findAllByStatus(Status.APPROVED);
    }

    @Transactional
    public List<Property> getPendingProperties() {
        return propertyRepository.findAllByStatus(Status.PENDING);
    }

    @Transactional
    public List<Property> getPropertiesByCity(String city) {
        return propertyRepository.findAllByCity(city);
    }



}
