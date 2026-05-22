package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.sql.Date;
import model.Slot;

public class SlotDAO extends DAO {
    public SlotDAO() {
        super();
    }

    public ArrayList<Slot> getFreeSlots(Date bookingDate, int timeSlotId) {
        ArrayList<Slot> list = new ArrayList<>();
        String sql = "SELECT * FROM tblSlot " +
                "WHERE status = 'active' " +
                "AND id NOT IN (" +
                "    SELECT slot_id FROM tblAppointment " +
                "    WHERE DATE(appointmentTime) = ? AND timeslot_id = ?" +
                ")";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setDate(1, bookingDate);
            ps.setInt(2, timeSlotId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Slot slot = new Slot(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("type"),
                        rs.getString("status"));
                list.add(slot);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
