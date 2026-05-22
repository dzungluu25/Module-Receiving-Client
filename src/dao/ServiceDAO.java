package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import model.Service;

public class ServiceDAO extends DAO {
    public ServiceDAO() {
        super();
    }

    public ArrayList<Service> searchService(String key) {
        ArrayList<Service> result = new ArrayList<>();
        String sql = "SELECT * FROM tblService WHERE name LIKE ?";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, "%" + key + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Service s = new Service();
                s.setId(rs.getInt("id"));
                s.setName(rs.getString("name"));
                s.setUnit(rs.getString("category")); // mapping category as unit
                s.setPrice((float) rs.getDouble("unitPrice"));
                s.setDes(rs.getString("description"));
                result.add(s);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
