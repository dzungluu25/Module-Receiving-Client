package test.dao;

import org.junit.Assert;
import org.junit.Test;
import java.util.ArrayList;
import dao.AppointmentDAO;
import model.Appointment;
import model.Client;

public class AppointmentDAOTest {

    @Test
    public void testSearchAppointmentAll() {
        AppointmentDAO ad = new AppointmentDAO();
        Appointment param = new Appointment();
        Client c = new Client();
        c.setName("");
        param.setClient(c);

        ArrayList<Appointment> result = ad.searchAppointment(param);
        Assert.assertNotNull(result);
        Assert.assertTrue(result.size() >= 0);
    }

    @Test
    public void testSearchAppointmentNoMatch() {
        AppointmentDAO ad = new AppointmentDAO();
        Appointment param = new Appointment();
        Client c = new Client();
        c.setName("non_existent_client_name_987654");
        param.setClient(c);

        ArrayList<Appointment> result = ad.searchAppointment(param);
        Assert.assertNotNull(result);
        Assert.assertEquals(0, result.size());
    }

    @Test
    public void testGetAppointmentDetailNull() {
        AppointmentDAO ad = new AppointmentDAO();
        Appointment result = ad.getAppointmentDetail(null);
        Assert.assertNull(result);
    }

    @Test
    public void testGetDetailAndSaveAppointment() {
        AppointmentDAO ad = new AppointmentDAO();
        Appointment param = new Appointment();
        Client c = new Client();
        c.setName("");
        param.setClient(c);

        ArrayList<Appointment> allApp = ad.searchAppointment(param);
        if (allApp != null && !allApp.isEmpty()) {
            Appointment sample = allApp.get(0);
            
            // 1. Fetch details
            Appointment detailed = ad.getAppointmentDetail(sample);
            Assert.assertNotNull(detailed);
            Assert.assertNotNull(detailed.getAppointmentService());
            Assert.assertNotNull(detailed.getAppointmentMaterial());

            // 2. Save appointment transaction
            boolean originalStatus = detailed.isStatus();
            detailed.setStatus(!originalStatus); // toggle status
            
            boolean saveResult = ad.saveAppointment(detailed);
            Assert.assertTrue(saveResult);

            // Recheck status in DB
            Appointment rechecked = ad.getAppointmentDetail(detailed);
            Assert.assertNotNull(rechecked);
            Assert.assertEquals(!originalStatus, rechecked.isStatus());

            // Restore original status
            detailed.setStatus(originalStatus);
            ad.saveAppointment(detailed);
        }
    }
}
