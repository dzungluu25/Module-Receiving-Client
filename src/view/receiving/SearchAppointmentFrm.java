package view.receiving;

import view.user.ReceptionistHomeFrm;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import dao.AppointmentDAO;
import model.Appointment;
import model.Client;
import model.User;

public class SearchAppointmentFrm extends JFrame implements ActionListener {
    private User u;
    private JTable tblAppointment;
    private JButton btlSearch;
    private JTextField txtClientName;
    private JButton btnSelect;
    private JButton btnWalkin;
    private JButton btnBack;
    private ArrayList<Appointment> listApp;

    public SearchAppointmentFrm(User u) {
        super("Search Booked Appointment");
        this.u = u;
        this.listApp = new ArrayList<>();

        JPanel pnMain = new JPanel(new BorderLayout());

        // Top Panel: Search Fields
        JPanel pnTop = new JPanel(new FlowLayout());
        pnTop.add(new JLabel("Customer Name:"));
        txtClientName = new JTextField(15);
        pnTop.add(txtClientName);
        btlSearch = new JButton("Search");
        btlSearch.addActionListener(this);
        pnTop.add(btlSearch);
        pnMain.add(pnTop, BorderLayout.NORTH);

        // Center Panel: Table
        String[] columnNames = { "Appointment ID", "Client Name", "Phone", "Scheduled Date", "Time Slot", "Seat/Slot" };
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblAppointment = new JTable(model);
        pnMain.add(new JScrollPane(tblAppointment), BorderLayout.CENTER);

        // Bottom Panel: Control Buttons
        JPanel pnBottom = new JPanel(new FlowLayout());
        btnSelect = new JButton("Select Appointment");
        btnSelect.addActionListener(this);
        pnBottom.add(btnSelect);

        btnWalkin = new JButton("Add Walk-in Client");
        btnWalkin.addActionListener(this);
        pnBottom.add(btnWalkin);

        btnBack = new JButton("Back");
        btnBack.addActionListener(this);
        pnBottom.add(btnBack);

        pnMain.add(pnBottom, BorderLayout.SOUTH);

        // Frame configuration
        this.setSize(750, 450);
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
            SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");

            for (Appointment app : listApp) {
                String tsInfo = "None";
                if (app.getTimeSlot() != null) {
                    tsInfo = app.getTimeSlot().getStartTime() + " - " + app.getTimeSlot().getEndTime() + " (" + app.getTimeSlot().getDescription() + ")";
                }
                model.addRow(new Object[] {
                        app.getId(),
                        app.getClient().getName(),
                        app.getClient().getPhone(),
                        app.getAppointmenDate() != null ? sdfDate.format(app.getAppointmenDate()) : "N/A",
                        tsInfo,
                        app.getSlot() != null ? app.getSlot().getName() : "None"
                });
            }

            if (listApp.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No appointment found.");
            }
        } else if (e.getSource().equals(btnSelect)) {
            int selectedRow = tblAppointment.getSelectedRow();
            if (selectedRow < 0) {
                JOptionPane.showMessageDialog(this, "Please select an appointment from the table.");
                return;
            }

            Appointment selectedApp = listApp.get(selectedRow);
            AppointmentDAO ad = new AppointmentDAO();
            selectedApp = ad.getAppointmentDetail(selectedApp);

            new SearchItemFrm(u, selectedApp).setVisible(true);
            this.dispose();
        } else if (e.getSource().equals(btnWalkin)) {
            new AddClient(u).setVisible(true);
            this.dispose();
        } else if (e.getSource().equals(btnBack)) {
            new ReceptionistHomeFrm(u).setVisible(true);
            this.dispose();
        }
    }
}
