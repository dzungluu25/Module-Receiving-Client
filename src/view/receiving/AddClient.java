package view.receiving;

import dao.ClientDAO;
import dao.SlotDAO;
import dao.TimeSlotDAO;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import model.Appointment;
import model.Client;
import model.Slot;
import model.TimeSlot;
import model.User;

public class AddClient extends JFrame implements ActionListener {
    private User u;
    private JTextField txtName;
    private JTextField txtPhone;
    private JTextField txtAddress;
    private JComboBox<TimeSlot> cbTimeSlot;
    private JComboBox<Slot> cbSlot;
    private JButton btnConfirm;
    private JButton btnBack;

    public AddClient(User u) {
        super("Add Walk-in Client");
        this.u = u;

        JPanel pnMain = new JPanel(new BorderLayout());

        // 1. Client Info Form (GridLayout)
        JPanel pnForm = new JPanel(new GridLayout(6, 1, 5, 5));

        JPanel pnName = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnName.add(new JLabel("Client Name:  "));
        txtName = new JTextField(15);
        pnName.add(txtName);
        pnForm.add(pnName);

        JPanel pnPhone = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnPhone.add(new JLabel("Phone Number: "));
        txtPhone = new JTextField(15);
        pnPhone.add(txtPhone);
        pnForm.add(pnPhone);

        JPanel pnAddress = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnAddress.add(new JLabel("Address:      "));
        txtAddress = new JTextField(15);
        pnAddress.add(txtAddress);
        pnForm.add(pnAddress);

        // Time Slot Choice
        JPanel pnTS = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnTS.add(new JLabel("Select Time:  "));
        cbTimeSlot = new JComboBox<>();
        pnTS.add(cbTimeSlot);
        pnForm.add(pnTS);

        // Available Physical Slot Choice
        JPanel pnSlot = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnSlot.add(new JLabel("Select Slot:  "));
        cbSlot = new JComboBox<>();
        pnSlot.add(cbSlot);
        pnForm.add(pnSlot);

        pnMain.add(pnForm, BorderLayout.CENTER);

        // 2. Control Buttons
        JPanel pnBottom = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnConfirm = new JButton("Confirm & Add Items");
        btnConfirm.addActionListener(this);
        pnBottom.add(btnConfirm);

        btnBack = new JButton("Back");
        btnBack.addActionListener(this);
        pnBottom.add(btnBack);
        pnMain.add(pnBottom, BorderLayout.SOUTH);

        // Load TimeSlots and dynamic Slot updates
        loadTimeSlots();
        cbTimeSlot.addActionListener(e -> updateFreeSlots());
        updateFreeSlots(); // initial load

        // Frame Setup
        this.setContentPane(pnMain);
        this.setSize(400, 320);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    private void loadTimeSlots() {
        TimeSlotDAO tsd = new TimeSlotDAO();
        ArrayList<TimeSlot> list = tsd.getAllTimeSlots();
        for (TimeSlot ts : list) {
            cbTimeSlot.addItem(ts);
        }
    }

    private void updateFreeSlots() {
        cbSlot.removeAllItems();
        TimeSlot selectedTS = (TimeSlot) cbTimeSlot.getSelectedItem();
        if (selectedTS == null) return;

        Date today = new Date(System.currentTimeMillis());
        SlotDAO sd = new SlotDAO();
        ArrayList<Slot> freeSlots = sd.getFreeSlots(today, selectedTS.getId());
        for (Slot s : freeSlots) {
            cbSlot.addItem(s);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(btnBack)) {
            new SearchAppointmentFrm(u).setVisible(true);
            this.dispose();
        } else if (e.getSource().equals(btnConfirm)) {
            String name = txtName.getText().trim();
            String phone = txtPhone.getText().trim();
            String address = txtAddress.getText().trim();

            if (name.isEmpty() || phone.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Client Name and Phone Number are required!");
                return;
            }

            TimeSlot ts = (TimeSlot) cbTimeSlot.getSelectedItem();
            Slot slot = (Slot) cbSlot.getSelectedItem();

            if (ts == null || slot == null) {
                JOptionPane.showMessageDialog(this, "Please ensure a Time Slot and an Available Seat/Slot are selected!");
                return;
            }

            // 1. Create and Save Client
            Client c = new Client();
            c.setName(name);
            c.setPhone(phone);
            c.setAddress(address);
            ClientDAO cd = new ClientDAO();
            if (!cd.addClient(c)) {
                JOptionPane.showMessageDialog(this, "Failed to save client to the database!");
                return;
            }

            // 2. Create Walk-in Appointment context
            Appointment newApp = new Appointment();
            newApp.setId(0); // new appointment
            newApp.setClient(c);
            newApp.setTimeSlot(ts);
            newApp.setSlot(slot);
            newApp.setUser(u);
            newApp.setAppointmenDate(new java.util.Date()); // today
            newApp.setStatus(false); // pending
            newApp.setAppointmentService(new ArrayList<>());
            newApp.setAppointmentMaterial(new ArrayList<>());

            // Proceed to adding services and materials
            new SearchItemFrm(u, newApp).setVisible(true);
            this.dispose();
        }
    }
}
