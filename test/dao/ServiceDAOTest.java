package test.dao;

import org.junit.Assert;
import org.junit.Test;
import java.util.ArrayList;
import dao.ServiceDAO;
import model.Service;

public class ServiceDAOTest {

    @Test
    public void testSearchServiceAll() {
        ServiceDAO sd = new ServiceDAO();
        ArrayList<Service> result = sd.searchService("");
        Assert.assertNotNull(result);
        Assert.assertTrue(result.size() >= 0);
    }

    @Test
    public void testSearchServiceNoMatch() {
        ServiceDAO sd = new ServiceDAO();
        ArrayList<Service> result = sd.searchService("xyz_non_existent_service_name_12345");
        Assert.assertNotNull(result);
        Assert.assertEquals(0, result.size());
    }

    @Test
    public void testSearchServiceWithKey() {
        ServiceDAO sd = new ServiceDAO();
        // search empty key to get any existing service
        ArrayList<Service> allServices = sd.searchService("");
        if (allServices != null && !allServices.isEmpty()) {
            Service sample = allServices.get(0);
            String name = sample.getName();
            
            // Search by name
            ArrayList<Service> matched = sd.searchService(name);
            Assert.assertTrue(matched.size() > 0);
            
            // Search by partial name
            if (name.length() > 2) {
                ArrayList<Service> matchedPartial = sd.searchService(name.substring(0, 2));
                Assert.assertTrue(matchedPartial.size() > 0);
            }
        }
    }
}
