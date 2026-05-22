package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import model.Material;

public class MaterialDAO extends DAO {
    public MaterialDAO() {
        super();
    }

    public ArrayList<Material> searchMaterial(String key) {
        ArrayList<Material> result = new ArrayList<>();
        String sql = "SELECT * FROM tblMaterial WHERE name LIKE ?";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, "%" + key + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Material m = new Material();
                m.setId(rs.getInt("id"));
                m.setName(rs.getString("name"));
                m.setUnit(rs.getString("category")); // mapping category as unit
                m.setPrice((float) rs.getDouble("unitPrice"));
                m.setDes(rs.getString("description"));
                result.add(m);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
