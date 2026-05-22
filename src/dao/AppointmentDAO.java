package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import model.Appointment;
import model.AppointmentMaterial;
import model.AppointmentService;
import model.Client;
import model.Material;
import model.Service;
import model.Staff;
import model.User;

public class AppointmentDAO extends DAO {
    public AppointmentDAO() {
        super();
    }

    public ArrayList<Appointment> searchAppointment(Appointment a) {
        ArrayList<Appointment> result = new ArrayList<>();
        String sql = "SELECT a.id, a.status, a.appointmentTime, a.user_id, a.client_id, a.timeslot_id, a.slot_id, " +
                     "c.name as client_name, c.phone as client_phone, c.address as client_address, " +
                     "s.name as slot_name, s.type as slot_type, s.status as slot_status, " +
                     "t.startTime as timeslot_start, t.endTime as timeslot_end, t.description as timeslot_desc " +
                     "FROM tblAppointment a " +
                     "INNER JOIN tblClient c ON a.client_id = c.id " +
                     "LEFT JOIN tblSlot s ON a.slot_id = s.id " +
                     "LEFT JOIN tblTimeSlot t ON a.timeslot_id = t.id " +
                     "WHERE c.name LIKE ? AND (a.status IS NULL OR a.status = 'pending' OR a.status = '' OR a.status = '0' OR a.status = 'false')";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            String nameKey = (a != null && a.getClient() != null) ? a.getClient().getName() : "";
            ps.setString(1, "%" + nameKey + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Appointment app = new Appointment();
                app.setId(rs.getInt("id"));
                String statusStr = rs.getString("status");
                app.setStatus("1".equals(statusStr) || "true".equalsIgnoreCase(statusStr) || "checked in".equalsIgnoreCase(statusStr));
                app.setAppointmenDate(rs.getTimestamp("appointmentTime"));
                
                Client c = new Client();
                c.setId(rs.getInt("client_id"));
                c.setName(rs.getString("client_name"));
                c.setPhone(rs.getString("client_phone"));
                c.setAddress(rs.getString("client_address"));
                app.setClient(c);
                
                User u = new User();
                u.setId(rs.getInt("user_id"));
                app.setUser(u);

                if (rs.getInt("slot_id") > 0) {
                    model.Slot slot = new model.Slot(
                        rs.getInt("slot_id"),
                        rs.getString("slot_name"),
                        rs.getString("slot_type"),
                        rs.getString("slot_status")
                    );
                    app.setSlot(slot);
                }

                if (rs.getInt("timeslot_id") > 0) {
                    model.TimeSlot ts = new model.TimeSlot(
                        rs.getInt("timeslot_id"),
                        rs.getTime("timeslot_start"),
                        rs.getTime("timeslot_end"),
                        rs.getString("timeslot_desc")
                    );
                    app.setTimeSlot(ts);
                }
                
                result.add(app);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public Appointment getAppointmentDetail(Appointment a) {
        if (a == null) return null;
        
        // Populate basic appointment details (slot, timeslot, client, user, etc.) if they are not yet fully populated
        String sqlApp = "SELECT a.id, a.status, a.appointmentTime, a.user_id, a.client_id, a.timeslot_id, a.slot_id, " +
                        "c.name as client_name, c.phone as client_phone, c.address as client_address, " +
                        "s.name as slot_name, s.type as slot_type, s.status as slot_status, " +
                        "t.startTime as timeslot_start, t.endTime as timeslot_end, t.description as timeslot_desc " +
                        "FROM tblAppointment a " +
                        "INNER JOIN tblClient c ON a.client_id = c.id " +
                        "LEFT JOIN tblSlot s ON a.slot_id = s.id " +
                        "LEFT JOIN tblTimeSlot t ON a.timeslot_id = t.id " +
                        "WHERE a.id = ?";
        try {
            PreparedStatement psApp = con.prepareStatement(sqlApp);
            psApp.setInt(1, a.getId());
            ResultSet rsApp = psApp.executeQuery();
            if (rsApp.next()) {
                String statusStr = rsApp.getString("status");
                a.setStatus("1".equals(statusStr) || "true".equalsIgnoreCase(statusStr) || "checked in".equalsIgnoreCase(statusStr));
                a.setAppointmenDate(rsApp.getTimestamp("appointmentTime"));
                
                Client c = new Client();
                c.setId(rsApp.getInt("client_id"));
                c.setName(rsApp.getString("client_name"));
                c.setPhone(rsApp.getString("client_phone"));
                c.setAddress(rsApp.getString("client_address"));
                a.setClient(c);
                
                User u = new User();
                u.setId(rsApp.getInt("user_id"));
                a.setUser(u);

                if (rsApp.getInt("slot_id") > 0) {
                    model.Slot slot = new model.Slot(
                        rsApp.getInt("slot_id"),
                        rsApp.getString("slot_name"),
                        rsApp.getString("slot_type"),
                        rsApp.getString("slot_status")
                    );
                    a.setSlot(slot);
                }
                
                if (rsApp.getInt("timeslot_id") > 0) {
                    model.TimeSlot ts = new model.TimeSlot(
                        rsApp.getInt("timeslot_id"),
                        rsApp.getTime("timeslot_start"),
                        rsApp.getTime("timeslot_end"),
                        rsApp.getString("timeslot_desc")
                    );
                    a.setTimeSlot(ts);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // 1. Get Appointment Services
        ArrayList<AppointmentService> services = new ArrayList<>();
        String sqlService = "SELECT aps.id, aps.quantity, aps.price, " +
                            "s.id as service_id, s.name as service_name, s.category as service_category, s.unitPrice as service_price, s.description as service_des, " +
                            "st.id as staff_id, st.name as staff_name, st.phone as staff_phone, st.email as staff_email, st.status as staff_status " +
                            "FROM tblAppointmentService aps " +
                            "INNER JOIN tblService s ON aps.service_id = s.id " +
                            "LEFT JOIN tblStaff st ON aps.staff_id = st.id " +
                            "WHERE aps.appointment_id = ?";
        try {
            PreparedStatement ps = con.prepareStatement(sqlService);
            ps.setInt(1, a.getId());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                AppointmentService aps = new AppointmentService();
                aps.setId(rs.getInt("id"));
                aps.setQuantity(rs.getInt("quantity"));
                aps.setPrice(rs.getDouble("price"));
                aps.setTotalAmount(aps.getPrice() * aps.getQuantity());
                
                Service s = new Service();
                s.setId(rs.getInt("service_id"));
                s.setName(rs.getString("service_name"));
                s.setUnit(rs.getString("service_category"));
                s.setPrice((float) rs.getDouble("service_price"));
                s.setDes(rs.getString("service_des"));
                aps.setService(s);
                
                if (rs.getInt("staff_id") > 0) {
                    Staff st = new Staff();
                    st.setId(rs.getInt("staff_id"));
                    st.setName(rs.getString("staff_name"));
                    st.setPhone(rs.getString("staff_phone"));
                    st.setEmail(rs.getString("staff_email"));
                    st.setStatus(rs.getString("staff_status"));
                    aps.setStaff(st);
                }
                services.add(aps);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        a.setAppointmentService(services);
        
        // 2. Get Appointment Materials
        ArrayList<AppointmentMaterial> materials = new ArrayList<>();
        String sqlMaterial = "SELECT apm.id, apm.quantity, apm.price, " +
                             "m.id as material_id, m.name as material_name, m.category as material_category, m.unitPrice as material_price, m.description as material_des " +
                             "FROM tblAppointmentMaterial apm " +
                             "INNER JOIN tblMaterial m ON apm.material_id = m.id " +
                             "WHERE apm.appointment_id = ?";
        try {
            PreparedStatement ps = con.prepareStatement(sqlMaterial);
            ps.setInt(1, a.getId());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                AppointmentMaterial apm = new AppointmentMaterial();
                apm.setId(rs.getInt("id"));
                apm.setQuantity(rs.getInt("quantity"));
                apm.setPrice(rs.getDouble("price"));
                apm.setTotalAmount(apm.getPrice() * apm.getQuantity());
                
                Material m = new Material();
                m.setId(rs.getInt("material_id"));
                m.setName(rs.getString("material_name"));
                m.setUnit(rs.getString("material_category"));
                m.setPrice((float) rs.getDouble("material_price"));
                m.setDes(rs.getString("material_des"));
                apm.setMaterial(m);
                
                materials.add(apm);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        a.setAppointmentMaterial(materials);
        
        return a;
    }

    public boolean saveAppointment(Appointment a) {
        if (a == null) return false;
        
        try {
            con.setAutoCommit(false);
            
            // 1. Insert or update tblAppointment
            boolean isNew = (a.getId() <= 0);
            if (isNew) {
                String sqlApp = "INSERT INTO tblAppointment(status, appointmentTime, user_id, client_id, timeslot_id, slot_id) VALUES(?, ?, ?, ?, ?, ?)";
                PreparedStatement psApp = con.prepareStatement(sqlApp, Statement.RETURN_GENERATED_KEYS);
                psApp.setString(1, a.isStatus() ? "checked in" : "pending");
                psApp.setTimestamp(2, new java.sql.Timestamp(a.getAppointmenDate().getTime()));
                if (a.getUser() != null) {
                    psApp.setInt(3, a.getUser().getId());
                } else {
                    psApp.setNull(3, java.sql.Types.INTEGER);
                }
                if (a.getClient() != null) {
                    psApp.setInt(4, a.getClient().getId());
                } else {
                    psApp.setNull(4, java.sql.Types.INTEGER);
                }
                if (a.getTimeSlot() != null) {
                    psApp.setInt(5, a.getTimeSlot().getId());
                } else {
                    psApp.setNull(5, java.sql.Types.INTEGER);
                }
                if (a.getSlot() != null) {
                    psApp.setInt(6, a.getSlot().getId());
                } else {
                    psApp.setNull(6, java.sql.Types.INTEGER);
                }
                psApp.executeUpdate();
                
                ResultSet rsApp = psApp.getGeneratedKeys();
                if (rsApp.next()) {
                    a.setId(rsApp.getInt(1));
                }
            } else {
                String sqlApp = "UPDATE tblAppointment SET status = ?, appointmentTime = ?, user_id = ?, client_id = ?, timeslot_id = ?, slot_id = ? WHERE id = ?";
                PreparedStatement psApp = con.prepareStatement(sqlApp);
                psApp.setString(1, a.isStatus() ? "checked in" : "pending");
                psApp.setTimestamp(2, new java.sql.Timestamp(a.getAppointmenDate().getTime()));
                if (a.getUser() != null) {
                    psApp.setInt(3, a.getUser().getId());
                } else {
                    psApp.setNull(3, java.sql.Types.INTEGER);
                }
                if (a.getClient() != null) {
                    psApp.setInt(4, a.getClient().getId());
                } else {
                    psApp.setNull(4, java.sql.Types.INTEGER);
                }
                if (a.getTimeSlot() != null) {
                    psApp.setInt(5, a.getTimeSlot().getId());
                } else {
                    psApp.setNull(5, java.sql.Types.INTEGER);
                }
                if (a.getSlot() != null) {
                    psApp.setInt(6, a.getSlot().getId());
                } else {
                    psApp.setNull(6, java.sql.Types.INTEGER);
                }
                psApp.setInt(7, a.getId());
                psApp.executeUpdate();
                
                // Delete existing services and materials for update
                String sqlDelServices = "DELETE FROM tblAppointmentService WHERE appointment_id = ?";
                PreparedStatement psDelSer = con.prepareStatement(sqlDelServices);
                psDelSer.setInt(1, a.getId());
                psDelSer.executeUpdate();
                
                String sqlDelMaterials = "DELETE FROM tblAppointmentMaterial WHERE appointment_id = ?";
                PreparedStatement psDelMat = con.prepareStatement(sqlDelMaterials);
                psDelMat.setInt(1, a.getId());
                psDelMat.executeUpdate();
            }
            
            // 2. Insert Appointment Services
            if (a.getAppointmentService() != null) {
                String sqlService = "INSERT INTO tblAppointmentService(appointment_id, service_id, staff_id, quantity, price) VALUES(?, ?, ?, ?, ?)";
                PreparedStatement psSer = con.prepareStatement(sqlService);
                for (AppointmentService aps : a.getAppointmentService()) {
                    psSer.setInt(1, a.getId());
                    psSer.setInt(2, aps.getService().getId());
                    if (aps.getStaff() != null) {
                        psSer.setInt(3, aps.getStaff().getId());
                    } else {
                        psSer.setNull(3, java.sql.Types.INTEGER);
                    }
                    psSer.setInt(4, aps.getQuantity());
                    psSer.setDouble(5, aps.getPrice());
                    psSer.executeUpdate();
                }
            }
            
            // 3. Insert Appointment Materials
            if (a.getAppointmentMaterial() != null) {
                String sqlMaterial = "INSERT INTO tblAppointmentMaterial(appointment_id, material_id, quantity, price) VALUES(?, ?, ?, ?)";
                PreparedStatement psMat = con.prepareStatement(sqlMaterial);
                for (AppointmentMaterial apm : a.getAppointmentMaterial()) {
                    psMat.setInt(1, a.getId());
                    psMat.setInt(2, apm.getMaterial().getId());
                    psMat.setInt(3, apm.getQuantity());
                    psMat.setDouble(4, apm.getPrice());
                    psMat.executeUpdate();
                }
            }
            
            con.commit();
            con.setAutoCommit(true);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            try {
                con.rollback();
                con.setAutoCommit(true);
            } catch (Exception rollbackEx) {
                rollbackEx.printStackTrace();
            }
            return false;
        }
    }
}
