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
    

    



}
