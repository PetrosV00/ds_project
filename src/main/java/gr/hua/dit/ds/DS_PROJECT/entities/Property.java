package gr.hua.dit.ds.DS_PROJECT.entities;

import jakarta.persistence.*;

import java.util.Date;
import java.util.List;

@Entity
public class Property {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column
    private String title;

    @Column
    private String description;

    @Column
    private String address;

    @Column
    private String city;

    @Column
    private float price;

    @Column
    private Date createdAt;

    @OneToMany(mappedBy = "property", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    private List<Booking> bookings;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    @JoinColumn(name= "user_id")
    private User user;

    @Enumerated
    private Status status;

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Booking> getBookings() {
        return bookings;
    }

    public void setBookings(List<Booking> bookings) {
        this.bookings = bookings;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getCity() { return city; }

    public void setCity(String city) { this.city = city; }

    public Property(String title, String description, String address, String city, float price) {
        this.title = title;
        this.description = description;
        this.address = address;
        this.city = city;
        this.price = price;
    }

    public Property() {
    }

    @Override
    public String toString() {
        return "Property{" +
                "Title='" + title + '\'' +
                ", Description='" + description + '\'' +
                ", address='" + address + '\'' +
                ", price=" + price +
                '}';
    }
}
