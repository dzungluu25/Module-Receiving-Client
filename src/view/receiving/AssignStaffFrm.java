package view.receiving;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
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
import dao.StaffDAO;
import model.Appointment;
import model.AppointmentService;
import model.Service;
import model.Staff;
import model.User;

public class AssignStaffFrm extends JFrame implements ActionListener {
    private Staff staff;
    private JTable tblStaff;
    private JButton btlAdd;
    private JButton btlSearch;
    private Service service;

    // Wizard parameters
    private User u;
    private Appointment a;
    private int serviceIndex;
    private JLabel lblTitle;
    private JLabel lblServiceInfo;
    private JButton btnBack;
    private ArrayList<Staff> listStaff;

    // Default constructor conforming strictly to the UML diagram
    public AssignStaffFrm(Service s) {
        super("Assign Staff");
        this.service = s;
        initUI();
    }

    // Overloaded wizard constructor to carry context through the receiving flow
    public AssignStaffFrm(User u, Appointment a, int serviceIndex) {
        super("Assign Service Staff");
        this.u = u;
        this.a = a;
        this.serviceIndex = serviceIndex;
        
        AppointmentService activeAppService = a.getAppointmentService().get(serviceIndex);
        this.service = activeAppService.getService();

        initUI();
        loadFreeStaff();
    }

    private void initUI() {
        JPanel pnMain = new JPanel(new BorderLayout());
        pnMain.setBorder(new EmptyBorder(15, 15, 15, 15));

        // Top Panel: Service details & step info
        JPanel pnTop = new JPanel();
        pnTop.setLayout(new BoxLayout(pnTop, BoxLayout.Y_AXIS));

        lblTitle = new JLabel("Assign Staff to Service");
        lblTitle.setFont(lblTitle.getFont().deriveFont(Font.BOLD, 18.0f));
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        pnTop.add(lblTitle);
        pnTop.add(Box.createRigidArea(new Dimension(0, 10)));

        String serviceText = String.format("Service [%d/%d]: %s (Category: %s)", 
            (serviceIndex + 1), 
            (a != null && a.getAppointmentService() != null ? a.getAppointmentService().size() : 1),
            service != null ? service.getName() : "N/A", 
            service != null ? service.getUnit() : "N/A"
        );
        lblServiceInfo = new JLabel(serviceText);
        lblServiceInfo.setFont(lblServiceInfo.getFont().deriveFont(14.0f));
        lblServiceInfo.setAlignmentX(Component.CENTER_ALIGNMENT);
        pnTop.add(lblServiceInfo);
        pnTop.add(Box.createRigidArea(new Dimension(0, 15)));

        pnMain.add(pnTop, BorderLayout.NORTH);

        // Center Panel: Free Staff JTable
        String[] columnNames = {"Staff ID", "Name", "Phone", "Email", "Status"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };
        tblStaff = new JTable(model);
        tblStaff.setRowHeight(25);
        pnMain.add(new JScrollPane(tblStaff), BorderLayout.CENTER);

        // Bottom Panel: Control buttons
        JPanel pnBottom = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        
        btlAdd = new JButton("Assign Selected Staff");
        btlAdd.addActionListener(this);
        pnBottom.add(btlAdd);

        btlSearch = new JButton("Refresh Staff List");
        btlSearch.addActionListener(this);
        pnBottom.add(btlSearch);

        btnBack = new JButton("Back");
        btnBack.addActionListener(this);
        pnBottom.add(btnBack);

        pnMain.add(pnBottom, BorderLayout.SOUTH);

        // Frame setup
        this.setSize(550, 420);
        this.setLocationRelativeTo(null);
        this.setContentPane(pnMain);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    private void loadFreeStaff() {
        if (service == null) return;
        StaffDAO sd = new StaffDAO();
        listStaff = sd.searchStaff(service);

        DefaultTableModel model = (DefaultTableModel) tblStaff.getModel();
        model.setRowCount(0);

        for (Staff st : listStaff) {
            model.addRow(new Object[]{
                st.getId(),
                st.getName(),
                st.getPhone(),
                st.getEmail(),
                st.getStatus()
            });
        }

        if (listStaff.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No free specialized staff found for this service at the moment.");
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(btlSearch)) {
            loadFreeStaff();
        } 
        else if (e.getSource().equals(btlAdd)) {
            int selectedRow = tblStaff.getSelectedRow();
            if (selectedRow < 0) {
                JOptionPane.showMessageDialog(this, "Please select a staff member from the table.");
                return;
            }

            Staff selectedStaff = listStaff.get(selectedRow);
            this.staff = selectedStaff;

            // Set the assigned staff member to the active service item
            if (a != null && a.getAppointmentService() != null) {
                a.getAppointmentService().get(serviceIndex).setStaff(selectedStaff);
                
                // If there are more services to assign, open next AssignStaffFrm
                if (serviceIndex + 1 < a.getAppointmentService().size()) {
                    new AssignStaffFrm(u, a, serviceIndex + 1).setVisible(true);
                    this.dispose();
                } else {
                    // Last service staff assigned $\rightarrow$ Proceed to Confirmation Screen
                    JOptionPane.showMessageDialog(this, "All service staff assigned successfully! Proceeding to Confirmation.");
                    new ConfirmFrm(a).setVisible(true);
                    this.dispose();
                }
            } else {
                JOptionPane.showMessageDialog(this, "Selected staff: " + selectedStaff.getName());
            }
        } 
        else if (e.getSource().equals(btnBack)) {
            if (serviceIndex > 0) {
                // Go back to the previous service assignment
                new AssignStaffFrm(u, a, serviceIndex - 1).setVisible(true);
            } else {
                // Go back to service/material selection
                new SearchItemFrm(u, a).setVisible(true);
            }
            this.dispose();
        }
    }
}
