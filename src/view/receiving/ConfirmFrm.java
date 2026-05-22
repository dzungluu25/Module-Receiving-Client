package view.receiving;

import view.user.ReceptionistHomeFrm;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import dao.AppointmentDAO;
import model.Appointment;
import model.AppointmentMaterial;
import model.AppointmentService;
import model.User;

public class ConfirmFrm extends JFrame implements ActionListener {
    private Appointment a;
    private JButton btlConfirm;
    private JButton btnBack;
    private JButton btnCancel;
    private JTable tblDetails;
    private JLabel lblTotalAmount;
    private JLabel lblClientInfo;

    public ConfirmFrm(Appointment a) {
        super("Confirm Customer Receipt");
        this.a = a;

        // Main Layout
        JPanel pnMain = new JPanel(new BorderLayout());
        pnMain.setBorder(new EmptyBorder(15, 15, 15, 15));

        // Top Panel: Header & Client Details
        JPanel pnTop = new JPanel();
        pnTop.setLayout(new BoxLayout(pnTop, BoxLayout.Y_AXIS));

        JLabel lblTitle = new JLabel("Receipt & Invoice Summary");
        lblTitle.setFont(lblTitle.getFont().deriveFont(Font.BOLD, 20.0f));
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        pnTop.add(lblTitle);
        pnTop.add(Box.createRigidArea(new Dimension(0, 10)));

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String clientText = String.format("Client: %s (Phone: %s)  |  Time: %s", 
            a.getClient() != null ? a.getClient().getName() : "N/A",
            a.getClient() != null ? a.getClient().getPhone() : "N/A",
            a.getAppointmenDate() != null ? sdf.format(a.getAppointmenDate()) : "N/A"
        );
        lblClientInfo = new JLabel(clientText);
        lblClientInfo.setFont(lblClientInfo.getFont().deriveFont(14.0f));
        lblClientInfo.setAlignmentX(Component.CENTER_ALIGNMENT);
        pnTop.add(lblClientInfo);
        pnTop.add(Box.createRigidArea(new Dimension(0, 15)));

        pnMain.add(pnTop, BorderLayout.NORTH);

        // Center Panel: Summary Table of Services & Materials
        String[] columnNames = {"Type", "Item Name", "Quantity", "Assigned Staff", "Unit Price", "Subtotal"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };
        tblDetails = new JTable(model);
        tblDetails.setRowHeight(25);
        pnMain.add(new JScrollPane(tblDetails), BorderLayout.CENTER);

        // Load details and calculate total amount
        double grandTotal = 0;

        // Populate Services
        if (a.getAppointmentService() != null) {
            for (AppointmentService aps : a.getAppointmentService()) {
                double subtotal = aps.getQuantity() * aps.getPrice();
                grandTotal += subtotal;
                model.addRow(new Object[]{
                    "Service",
                    aps.getService().getName(),
                    aps.getQuantity(),
                    aps.getStaff() != null ? aps.getStaff().getName() : "Unassigned",
                    aps.getPrice(),
                    subtotal
                });
            }
        }

        // Populate Materials
        if (a.getAppointmentMaterial() != null) {
            for (AppointmentMaterial apm : a.getAppointmentMaterial()) {
                double subtotal = apm.getQuantity() * apm.getPrice();
                grandTotal += subtotal;
                model.addRow(new Object[]{
                    "Material",
                    apm.getMaterial().getName(),
                    apm.getQuantity(),
                    "N/A",
                    apm.getPrice(),
                    subtotal
                });
            }
        }

        // Summary Row Panel
        JPanel pnSummary = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 5));
        lblTotalAmount = new JLabel(String.format("Grand Total: %.2f USD", grandTotal));
        lblTotalAmount.setFont(lblTotalAmount.getFont().deriveFont(Font.BOLD, 16.0f));
        pnSummary.add(lblTotalAmount);

        JPanel pnCenterWrapper = new JPanel(new BorderLayout());
        pnCenterWrapper.add(new JScrollPane(tblDetails), BorderLayout.CENTER);
        pnCenterWrapper.add(pnSummary, BorderLayout.SOUTH);
        pnMain.add(pnCenterWrapper, BorderLayout.CENTER);

        // Bottom Panel: Confirm/Back/Cancel Buttons
        JPanel pnBottom = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        
        btlConfirm = new JButton("Confirm & Save Receipt");
        btlConfirm.setFont(btlConfirm.getFont().deriveFont(Font.BOLD, 14.0f));
        btlConfirm.addActionListener(this);
        pnBottom.add(btlConfirm);

        btnBack = new JButton("Back to Assignments");
        btnBack.addActionListener(this);
        pnBottom.add(btnBack);

        btnCancel = new JButton("Cancel");
        btnCancel.addActionListener(this);
        pnBottom.add(btnCancel);

        pnMain.add(pnBottom, BorderLayout.SOUTH);

        // Frame setup
        this.setSize(750, 480);
        this.setLocationRelativeTo(null);
        this.setContentPane(pnMain);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(btlConfirm)) {
            // Save the appointment
            a.setStatus(true); // set receipt status to true (completed/processed)
            AppointmentDAO ad = new AppointmentDAO();
            if (ad.saveAppointment(a)) {
                JOptionPane.showMessageDialog(this, "Customer transaction successfully received and saved to MySQL database!");
                
                // Navigate back to ReceptionistHomeFrm
                new ReceptionistHomeFrm(a.getUser()).setVisible(true);
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to save appointment. Please check database logs.");
            }
        } 
        else if (e.getSource().equals(btnBack)) {
            // Go back to the last service's staff assignment screen
            if (a.getAppointmentService() != null && !a.getAppointmentService().isEmpty()) {
                new AssignStaffFrm(a.getUser(), a, a.getAppointmentService().size() - 1).setVisible(true);
            } else {
                new SearchItemFrm(a.getUser(), a).setVisible(true);
            }
            this.dispose();
        } 
        else if (e.getSource().equals(btnCancel)) {
            int option = JOptionPane.showConfirmDialog(this, 
                "Are you sure you want to discard this transaction and return to dashboard?", 
                "Discard Transaction", JOptionPane.YES_NO_OPTION);
            if (option == JOptionPane.YES_OPTION) {
                new ReceptionistHomeFrm(a.getUser()).setVisible(true);
                this.dispose();
            }
        }
    }
}
