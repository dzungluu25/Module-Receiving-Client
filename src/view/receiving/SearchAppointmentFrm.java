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
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import dao.AppointmentDAO;
import model.Appointment;
import model.Client;
import model.User;

public class SearchAppointmentFrm extends JFrame implements ActionListener {
    private User u;
    private Appointment a;
    private JTable tblAppointment;
    private JButton btlSearch;
    private JTextField txtClientName;
    private JButton btnSelect;
    private JButton btnBack;
    private ArrayList<Appointment> listApp;

    public SearchAppointmentFrm(User u) {
        super("Search Booked Appointment");
        this.u = u;
        this.listApp = new ArrayList<>();

        // Main Layout
        JPanel pnMain = new JPanel(new BorderLayout());
        pnMain.setBorder(new EmptyBorder(15, 15, 15, 15));

        // Top Panel: Title & Search Fields
        JPanel pnTop = new JPanel();
        pnTop.setLayout(new BoxLayout(pnTop, BoxLayout.Y_AXIS));

        JLabel lblTitle = new JLabel("Search Customer Appointment");
        lblTitle.setFont(lblTitle.getFont().deriveFont(Font.BOLD, 18.0f));
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        pnTop.add(lblTitle);
        pnTop.add(Box.createRigidArea(new Dimension(0, 15)));

        JPanel pnSearchField = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pnSearchField.add(new JLabel("Customer Name:"));
        txtClientName = new JTextField(20);
        pnSearchField.add(txtClientName);
        btlSearch = new JButton("Search");
        btlSearch.addActionListener(this);
        pnSearchField.add(btlSearch);
        pnTop.add(pnSearchField);

        pnMain.add(pnTop, BorderLayout.NORTH);

        // Center Panel: Table to display Appointments
        String[] columnNames = {"Appointment ID", "Client Name", "Phone", "Scheduled Time"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblAppointment = new JTable(model);
        tblAppointment.setRowHeight(25);
        JScrollPane scrollPane = new JScrollPane(tblAppointment);
        pnMain.add(scrollPane, BorderLayout.CENTER);

        // Bottom Panel: Control Buttons
        JPanel pnBottom = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        
        btnSelect = new JButton("Select Appointment");
        btnSelect.addActionListener(this);
        pnBottom.add(btnSelect);

        btnBack = new JButton("Back");
        btnBack.addActionListener(this);
        pnBottom.add(btnBack);

        pnMain.add(pnBottom, BorderLayout.SOUTH);

        // Frame configuration
        this.setSize(650, 450);
        this.setLocationRelativeTo(null);
        this.setContentPane(pnMain);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(btlSearch)) {
            String name = txtClientName.getText().trim();
            Appointment searchKey = new Appointment();
            Client c = new Client();
            c.setName(name);
            searchKey.setClient(c);

            AppointmentDAO ad = new AppointmentDAO();
            listApp = ad.searchAppointment(searchKey);

            DefaultTableModel model = (DefaultTableModel) tblAppointment.getModel();
            model.setRowCount(0);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            for (Appointment app : listApp) {
                model.addRow(new Object[]{
                    app.getId(),
                    app.getClient().getName(),
                    app.getClient().getPhone(),
                    app.getAppointmenDate() != null ? sdf.format(app.getAppointmenDate()) : "N/A"
                });
            }

            if (listApp.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No appointment found matching that name.");
            }
        } 
        else if (e.getSource().equals(btnSelect)) {
            int selectedRow = tblAppointment.getSelectedRow();
            if (selectedRow < 0) {
                JOptionPane.showMessageDialog(this, "Please select an appointment from the table.");
                return;
            }

            Appointment selectedApp = listApp.get(selectedRow);
            // Load detail lists (services and materials) using DAO
            AppointmentDAO ad = new AppointmentDAO();
            selectedApp = ad.getAppointmentDetail(selectedApp);

            // Navigate to SearchItemFrm with loaded details
            new SearchItemFrm(u, selectedApp).setVisible(true);
            this.dispose();
        } 
        else if (e.getSource().equals(btnBack)) {
            new ReceptionistHomeFrm(u).setVisible(true);
            this.dispose();
        }
    }
}
