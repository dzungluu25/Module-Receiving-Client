package view.receiving;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
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
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import dao.MaterialDAO;
import dao.ServiceDAO;
import model.Appointment;
import model.AppointmentMaterial;
import model.AppointmentService;
import model.Material;
import model.Service;
import model.User;

public class SearchItemFrm extends JFrame implements ActionListener {
    private User u;
    private Appointment a;
    private Material m;
    private Service s;
    private JTextField txtKey;
    private JTable tblMaterial;
    private JTable tblService;
    private JButton btlSearch;

    private JButton btnSelectService;
    private JButton btnSelectMaterial;
    private JTable tblSelectedItems;
    private JLabel lblTotalAmount;
    private JButton btnAssignStaff;
    private JButton btnBack;

    private ArrayList<Service> listService;
    private ArrayList<Material> listMaterial;

    public SearchItemFrm(User u, Appointment a) {
        super("Add Services & Materials");
        this.u = u;
        this.a = a;
        this.listService = new ArrayList<>();
        this.listMaterial = new ArrayList<>();

        // Main Layout
        JPanel pnMain = new JPanel(new BorderLayout());
        pnMain.setBorder(new EmptyBorder(10, 10, 10, 10));

        // 1. Top Panel: Search controls
        JPanel pnTop = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        pnTop.add(new JLabel("Search Item:"));
        txtKey = new JTextField(20);
        pnTop.add(txtKey);
        btlSearch = new JButton("Search");
        btlSearch.addActionListener(this);
        pnTop.add(btlSearch);
        pnMain.add(pnTop, BorderLayout.NORTH);

        // 2. Center Panel: Split into Search Results (Top) and Selected Items (Bottom)
        JPanel pnCenter = new JPanel(new GridLayout(2, 1, 10, 10));

        // 2a. Search Results Panel (Left: Services, Right: Materials)
        JPanel pnResults = new JPanel(new GridLayout(1, 2, 10, 10));

        // Services Sub-panel
        JPanel pnServiceCol = new JPanel(new BorderLayout());
        pnServiceCol.add(new JLabel("Available Services", JLabel.CENTER), BorderLayout.NORTH);
        String[] serviceCols = {"ID", "Name", "Price", "Unit"};
        DefaultTableModel modelService = new DefaultTableModel(serviceCols, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };
        tblService = new JTable(modelService);
        pnServiceCol.add(new JScrollPane(tblService), BorderLayout.CENTER);
        btnSelectService = new JButton("Add Selected Service");
        btnSelectService.addActionListener(this);
        pnServiceCol.add(btnSelectService, BorderLayout.SOUTH);
        pnResults.add(pnServiceCol);

        // Materials Sub-panel
        JPanel pnMaterialCol = new JPanel(new BorderLayout());
        pnMaterialCol.add(new JLabel("Available Materials", JLabel.CENTER), BorderLayout.NORTH);
        String[] materialCols = {"ID", "Name", "Price", "Unit"};
        DefaultTableModel modelMaterial = new DefaultTableModel(materialCols, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };
        tblMaterial = new JTable(modelMaterial);
        pnMaterialCol.add(new JScrollPane(tblMaterial), BorderLayout.CENTER);
        btnSelectMaterial = new JButton("Add Selected Material");
        btnSelectMaterial.addActionListener(this);
        pnMaterialCol.add(btnSelectMaterial, BorderLayout.SOUTH);
        pnResults.add(pnMaterialCol);

        pnCenter.add(pnResults);

        // 2b. Selected Items Sub-panel (Bottom half of Center)
        JPanel pnSelected = new JPanel(new BorderLayout());
        pnSelected.add(new JLabel("Selected Items (Provisional Invoice)", JLabel.CENTER), BorderLayout.NORTH);
        String[] selectedCols = {"Type", "Name", "Quantity", "Price", "Subtotal"};
        DefaultTableModel modelSelected = new DefaultTableModel(selectedCols, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };
        tblSelectedItems = new JTable(modelSelected);
        pnSelected.add(new JScrollPane(tblSelectedItems), BorderLayout.CENTER);

        // Subtotal & Running Total
        JPanel pnSummary = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 5));
        lblTotalAmount = new JLabel("Total Provisional Amount: 0.00 USD");
        lblTotalAmount.setFont(lblTotalAmount.getFont().deriveFont(Font.BOLD, 15.0f));
        pnSummary.add(lblTotalAmount);
        pnSelected.add(pnSummary, BorderLayout.SOUTH);

        pnCenter.add(pnSelected);
        pnMain.add(pnCenter, BorderLayout.CENTER);

        // 3. Bottom Panel: Actions
        JPanel pnBottom = new JPanel(new FlowLayout(FlowLayout.CENTER, 25, 10));
        btnAssignStaff = new JButton("Proceed to Assign Staff");
        btnAssignStaff.setFont(btnAssignStaff.getFont().deriveFont(Font.BOLD, 14.0f));
        btnAssignStaff.addActionListener(this);
        pnBottom.add(btnAssignStaff);

        btnBack = new JButton("Back");
        btnBack.addActionListener(this);
        pnBottom.add(btnBack);
        pnMain.add(pnBottom, BorderLayout.SOUTH);

        // Load initially selected items in case we came back from next step
        updateSelectedItemsTable();

        // Frame setup
        this.setSize(800, 600);
        this.setLocationRelativeTo(null);
        this.setContentPane(pnMain);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    private void updateSelectedItemsTable() {
        DefaultTableModel model = (DefaultTableModel) tblSelectedItems.getModel();
        model.setRowCount(0);
        double grandTotal = 0;

        // Populate Services
        if (a.getAppointmentService() != null) {
            for (AppointmentService aps : a.getAppointmentService()) {
                double subtotal = aps.getQuantity() * aps.getPrice();
                grandTotal += subtotal;
                model.addRow(new Object[]{"Service", aps.getService().getName(), aps.getQuantity(), aps.getPrice(), subtotal});
            }
        }

        // Populate Materials
        if (a.getAppointmentMaterial() != null) {
            for (AppointmentMaterial apm : a.getAppointmentMaterial()) {
                double subtotal = apm.getQuantity() * apm.getPrice();
                grandTotal += subtotal;
                model.addRow(new Object[]{"Material", apm.getMaterial().getName(), apm.getQuantity(), apm.getPrice(), subtotal});
            }
        }

        lblTotalAmount.setText(String.format("Total Provisional Amount: %.2f USD", grandTotal));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(btlSearch)) {
            String key = txtKey.getText().trim();

            // Search Services
            ServiceDAO sd = new ServiceDAO();
            listService = sd.searchService(key);
            DefaultTableModel modelS = (DefaultTableModel) tblService.getModel();
            modelS.setRowCount(0);
            for (Service srv : listService) {
                modelS.addRow(new Object[]{srv.getId(), srv.getName(), srv.getPrice(), srv.getUnit()});
            }

            // Search Materials
            MaterialDAO md = new MaterialDAO();
            listMaterial = md.searchMaterial(key);
            DefaultTableModel modelM = (DefaultTableModel) tblMaterial.getModel();
            modelM.setRowCount(0);
            for (Material mat : listMaterial) {
                modelM.addRow(new Object[]{mat.getId(), mat.getName(), mat.getPrice(), mat.getUnit()});
            }
        } 
        else if (e.getSource().equals(btnSelectService)) {
            int selectedRow = tblService.getSelectedRow();
            if (selectedRow < 0) {
                JOptionPane.showMessageDialog(this, "Please select a service from the table.");
                return;
            }

            Service selectedService = listService.get(selectedRow);
            String qtyStr = JOptionPane.showInputDialog(this, "Enter quantity for " + selectedService.getName() + ":", "1");
            if (qtyStr == null) return; // cancelled

            int qty = 1;
            try {
                qty = Integer.parseInt(qtyStr.trim());
                if (qty <= 0) throw new NumberFormatException();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid quantity. Using default of 1.");
                qty = 1;
            }

            // Check if already added
            boolean exists = false;
            for (AppointmentService aps : a.getAppointmentService()) {
                if (aps.getService().getId() == selectedService.getId()) {
                    aps.setQuantity(aps.getQuantity() + qty);
                    aps.setTotalAmount(aps.getQuantity() * aps.getPrice());
                    exists = true;
                    break;
                }
            }

            if (!exists) {
                AppointmentService aps = new AppointmentService();
                aps.setService(selectedService);
                aps.setQuantity(qty);
                aps.setPrice(selectedService.getPrice());
                aps.setTotalAmount(qty * selectedService.getPrice());
                a.getAppointmentService().add(aps);
            }

            updateSelectedItemsTable();
        } 
        else if (e.getSource().equals(btnSelectMaterial)) {
            int selectedRow = tblMaterial.getSelectedRow();
            if (selectedRow < 0) {
                JOptionPane.showMessageDialog(this, "Please select a material from the table.");
                return;
            }

            Material selectedMaterial = listMaterial.get(selectedRow);
            String qtyStr = JOptionPane.showInputDialog(this, "Enter quantity for " + selectedMaterial.getName() + ":", "1");
            if (qtyStr == null) return; // cancelled

            int qty = 1;
            try {
                qty = Integer.parseInt(qtyStr.trim());
                if (qty <= 0) throw new NumberFormatException();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid quantity. Using default of 1.");
                qty = 1;
            }

            // Check if already added
            boolean exists = false;
            for (AppointmentMaterial apm : a.getAppointmentMaterial()) {
                if (apm.getMaterial().getId() == selectedMaterial.getId()) {
                    apm.setQuantity(apm.getQuantity() + qty);
                    apm.setTotalAmount(apm.getQuantity() * apm.getPrice());
                    exists = true;
                    break;
                }
            }

            if (!exists) {
                AppointmentMaterial apm = new AppointmentMaterial();
                apm.setMaterial(selectedMaterial);
                apm.setQuantity(qty);
                apm.setPrice(selectedMaterial.getPrice());
                apm.setTotalAmount(qty * selectedMaterial.getPrice());
                a.getAppointmentMaterial().add(apm);
            }

            updateSelectedItemsTable();
        } 
        else if (e.getSource().equals(btnAssignStaff)) {
            if (a.getAppointmentService() == null || a.getAppointmentService().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please add at least one Service to the booking before assigning staff.");
                return;
            }

            // Start the sequential staff assignment wizard (beginning with the first service, index 0)
            new AssignStaffFrm(u, a, 0).setVisible(true);
            this.dispose();
        } 
        else if (e.getSource().equals(btnBack)) {
            new SearchAppointmentFrm(u).setVisible(true);
            this.dispose();
        }
    }
}
