package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import model.User;
public class UserDAO extends DAO{

    public UserDAO(){
        super();
    }

    public boolean checkLogin(User user){
        String sql = "SELECT * FROM tblUser WHERE username = ? AND password = ?";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());

            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                user.setId(rs.getInt("id"));
                user.setName(rs.getString("name"));
                user.setPosition(rs.getString("position"));
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
