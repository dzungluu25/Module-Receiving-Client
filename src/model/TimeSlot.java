package model;

import java.io.Serializable;
import java.sql.Time;

public class TimeSlot implements Serializable {
    private int id;
    private Time startTime;
    private Time endTime;
    private String description;

    public TimeSlot() {
    }

    public TimeSlot(int id, Time startTime, Time endTime, String description) {
        this.id = id;
        this.startTime = startTime;
        this.endTime = endTime;
        this.description = description;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Time getStartTime() {
        return startTime;
    }

    public void setStartTime(Time startTime) {
        this.startTime = startTime;
    }

    public Time getEndTime() {
        return endTime;
    }

    public void setEndTime(Time endTime) {
        this.endTime = endTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return startTime + " - " + endTime + " (" + description + ")";
    }
}
