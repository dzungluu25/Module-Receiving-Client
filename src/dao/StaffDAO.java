package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import model.Staff;
import model.Service;

public class StaffDAO extends DAO {
    public StaffDAO() {
        super();
    }

    public ArrayList<Staff> searchStaff(Service s) {
        ArrayList<Staff> result = new ArrayList<>();
        String sql = "SELECT st.* FROM tblStaff st " +
                     "INNER JOIN tblServiceStaff ss ON st.id = ss.staff_id " +
                     "WHERE ss.service_id = ? AND st.status = 'free'";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, s.getId());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Staff staff = new Staff();
                staff.setId(rs.getInt("id"));
                staff.setName(rs.getString("name"));
                staff.setPhone(rs.getString("phone"));
                staff.setEmail(rs.getString("email"));
                staff.setStatus(rs.getString("status"));
                result.add(staff);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
