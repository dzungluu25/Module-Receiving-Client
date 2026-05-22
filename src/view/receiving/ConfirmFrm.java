package view.receiving;

import view.user.ReceptionistHomeFrm;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import dao.AppointmentDAO;
import model.Appointment;
import model.AppointmentMaterial;
import model.AppointmentService;

public class ConfirmFrm extends JFrame implements ActionListener {
    private Appointment a;
    private JButton btlConfirm;
    private JButton btnBack;
    private JButton btnCancel;

    public ConfirmFrm(Appointment a) {
        super("Confirm Appointment Check-in");
        this.a = a;

        JPanel pnMain = new JPanel(new BorderLayout());

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String slotInfo = "";
        if (a.getSlot() != null) {
            slotInfo += "  |  Slot: " + a.getSlot().getName() + " (" + a.getSlot().getType() + ")";
        }
        if (a.getTimeSlot() != null) {
            slotInfo += "  |  TimeSlot: " + a.getTimeSlot().getStartTime() + " - " + a.getTimeSlot().getEndTime() + " (" + a.getTimeSlot().getDescription() + ")";
        }
        String clientText = String.format("Client: %s (Phone: %s)  |  Time: %s%s", 
            a.getClient() != null ? a.getClient().getName() : "N/A",
            a.getClient() != null ? a.getClient().getPhone() : "N/A",
            a.getAppointmenDate() != null ? sdf.format(a.getAppointmenDate()) : "N/A",
            slotInfo
        );
        JLabel lblClientInfo = new JLabel(clientText, JLabel.CENTER);
        pnMain.add(lblClientInfo, BorderLayout.NORTH);

        String[] columnNames = {"Type", "Item Name", "Quantity", "Assigned Staff"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };
        JTable tblDetails = new JTable(model);

        if (a.getAppointmentService() != null) {
            for (AppointmentService aps : a.getAppointmentService()) {
                model.addRow(new Object[]{
                    "Service",
                    aps.getService().getName(),
                    aps.getQuantity(),
                    aps.getStaff() != null ? aps.getStaff().getName() : "Unassigned"
                });
            }
        }

        if (a.getAppointmentMaterial() != null) {
            for (AppointmentMaterial apm : a.getAppointmentMaterial()) {
                model.addRow(new Object[]{
                    "Material",
                    apm.getMaterial().getName(),
                    apm.getQuantity(),
                    "N/A"
                });
            }
        }

        pnMain.add(new JScrollPane(tblDetails), BorderLayout.CENTER);

        btlConfirm = new JButton("Confirm Check-in");
        btnBack = new JButton("Back to Assignments");
        btnCancel = new JButton("Cancel");

        btlConfirm.addActionListener(this);
        btnBack.addActionListener(this);
        btnCancel.addActionListener(this);

        JPanel pnBottom = new JPanel(new FlowLayout());
        pnBottom.add(btlConfirm);
        pnBottom.add(btnBack);
        pnBottom.add(btnCancel);
        pnMain.add(pnBottom, BorderLayout.SOUTH);

        setContentPane(pnMain);
        setSize(750, 480);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(btlConfirm)) {
            a.setStatus(true);
            if (new AppointmentDAO().saveAppointment(a)) {
                JOptionPane.showMessageDialog(this, "Customer appointment check-in successfully confirmed!");
                new ReceptionistHomeFrm(a.getUser()).setVisible(true);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to save appointment. Please check database logs.");
            }
        } else if (e.getSource().equals(btnBack)) {
            if (a.getAppointmentService() != null && !a.getAppointmentService().isEmpty()) {
                new AssignStaffFrm(a.getUser(), a, a.getAppointmentService().size() - 1).setVisible(true);
            } else {
                new SearchItemFrm(a.getUser(), a).setVisible(true);
            }
            dispose();
        } else if (e.getSource().equals(btnCancel)) {
            int option = JOptionPane.showConfirmDialog(this, 
                "Are you sure you want to discard this check-in process and return to dashboard?", 
                "Discard Check-in", JOptionPane.YES_NO_OPTION);
            if (option == JOptionPane.YES_OPTION) {
                new ReceptionistHomeFrm(a.getUser()).setVisible(true);
                dispose();
            }
        }
    }
}
