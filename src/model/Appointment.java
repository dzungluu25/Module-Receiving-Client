package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class Appointment implements Serializable{
    private int id;
    private boolean status;
    private Date appointmenDate;
    private User user;
    private Client client;
    private ArrayList<AppointmentService> appointmentService;
    private ArrayList<AppointmentMaterial> appointmentMaterial;
    private Slot slot;
    private TimeSlot timeSlot;

    public Appointment(){
        super();
    }
    
    public Appointment(int id, boolean status, Date appointmenDate, User user, Client client,
                       ArrayList<AppointmentService> appointmentService,
                       ArrayList<AppointmentMaterial> appointmentMaterial) {
        super();
        this.id = id;
        this.status = status;
        this.appointmenDate = appointmenDate;
        this.user = user;
        this.client = client;
        this.appointmentService = appointmentService;
        this.appointmentMaterial = appointmentMaterial;
    }

    public Slot getSlot() {
        return slot;
    }

    public void setSlot(Slot slot) {
        this.slot = slot;
    }

    public TimeSlot getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(TimeSlot timeSlot) {
        this.timeSlot = timeSlot;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public Date getAppointmenDate() {
        return appointmenDate;
    }

    public void setAppointmenDate(Date appointmenDate) {
        this.appointmenDate = appointmenDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public ArrayList<AppointmentService> getAppointmentService() {
        return appointmentService;
    }

    public void setAppointmentService(ArrayList<AppointmentService> appointmentService) {
        this.appointmentService = appointmentService;
    }

    public ArrayList<AppointmentMaterial> getAppointmentMaterial() {
        return appointmentMaterial;
    }

    public void setAppointmentMaterial(ArrayList<AppointmentMaterial> appointmentMaterial) {
        this.appointmentMaterial = appointmentMaterial;
    }
}
