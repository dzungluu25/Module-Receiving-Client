package test.dao;

import org.junit.Assert;
import org.junit.Test;
import dao.UserDAO;
import model.User;

public class UserDAOTest {

    @Test
    public void testCheckLoginSuccess() {
        UserDAO ud = new UserDAO();
        User u = new User();
        u.setUsername("dungluuanh");
        u.setPassword("123456");
        
        boolean result = ud.checkLogin(u);
        Assert.assertTrue(result);
        Assert.assertEquals("dungluuanh", u.getUsername());
        Assert.assertEquals("receptionist", u.getPosition());
    }

    @Test
    public void testCheckLoginFailWrongPassword() {
        UserDAO ud = new UserDAO();
        User u = new User();
        u.setUsername("dungluuanh");
        u.setPassword("wrong_password");
        
        boolean result = ud.checkLogin(u);
        Assert.assertFalse(result);
    }

    @Test
    public void testCheckLoginFailWrongUsername() {
        UserDAO ud = new UserDAO();
        User u = new User();
        u.setUsername("wrong_user");
        u.setPassword("admin");
        
        boolean result = ud.checkLogin(u);
        Assert.assertFalse(result);
    }

    @Test
    public void testCheckLoginFailEmpty() {
        UserDAO ud = new UserDAO();
        User u = new User();
        u.setUsername("");
        u.setPassword("");
        
        boolean result = ud.checkLogin(u);
        Assert.assertFalse(result);
    }
}
