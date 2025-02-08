package gr.hua.dit.ds.DS_PROJECT.entities;

import jakarta.persistence.*;

import java.util.Date;


@Entity
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int Id;

    @Column
    private String startDate;

    @Column
    private String endDate;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    @JoinColumn(name = "property_id")
    private Property property;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated
    private Status status;

    public Status getStatus() { return status; }

    public void setStatus(Status status) { this.status = status; }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        this.Id = id;
    }

    public Property getProperty() {
        return property;
    }

    public void setProperty(Property property) {
        this.property = property;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public Booking(String startDate, String endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Booking() {
    }

    @Override
    public String toString() {
        return "Booking{" +
                "startDate=" + startDate +
                ", endDate=" + endDate +
                '}';
    }
}
