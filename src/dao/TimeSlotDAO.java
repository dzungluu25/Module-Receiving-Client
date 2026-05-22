package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import model.TimeSlot;

public class TimeSlotDAO extends DAO {
    public TimeSlotDAO() {
        super();
    }

    public ArrayList<TimeSlot> getAllTimeSlots() {
        ArrayList<TimeSlot> list = new ArrayList<>();
        String sql = "SELECT * FROM tblTimeSlot";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                TimeSlot ts = new TimeSlot(
                    rs.getInt("id"),
                    rs.getTime("startTime"),
                    rs.getTime("endTime"),
                    rs.getString("description")
                );
                list.add(ts);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
