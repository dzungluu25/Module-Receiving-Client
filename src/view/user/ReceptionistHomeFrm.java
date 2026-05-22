package view.user;

import view.receiving.SearchAppointmentFrm;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import model.User;

public class ReceptionistHomeFrm extends JFrame implements ActionListener {
    private User u;
    private JButton btnReceiveClient;

    public ReceptionistHomeFrm(User u) {
        super("Receptionist Dashboard");
        this.u = u;

        // Set layout & padding
        JPanel pnMain = new JPanel();
        pnMain.setLayout(new BoxLayout(pnMain, BoxLayout.Y_AXIS));
        pnMain.setBorder(new EmptyBorder(25, 25, 25, 25));

        // Header Title
        JLabel lblTitle = new JLabel("Receptionist Dashboard");
        lblTitle.setFont(lblTitle.getFont().deriveFont(Font.BOLD, 22.0f));
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        pnMain.add(lblTitle);
        pnMain.add(Box.createRigidArea(new Dimension(0, 15)));

        // User Greeting
        JLabel lblWelcome = new JLabel("Welcome back, " + (u != null ? u.getName() : "Receptionist") + "!");
        lblWelcome.setFont(lblWelcome.getFont().deriveFont(15.0f));
        lblWelcome.setAlignmentX(Component.CENTER_ALIGNMENT);
        pnMain.add(lblWelcome);
        pnMain.add(Box.createRigidArea(new Dimension(0, 30)));

        // Receive Button
        btnReceiveClient = new JButton("Receive Customers");
        btnReceiveClient.setFont(btnReceiveClient.getFont().deriveFont(Font.BOLD, 16.0f));
        btnReceiveClient.setPreferredSize(new Dimension(200, 50));
        btnReceiveClient.setMaximumSize(new Dimension(220, 50));
        btnReceiveClient.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnReceiveClient.addActionListener(this);
        pnMain.add(btnReceiveClient);

        pnMain.add(Box.createRigidArea(new Dimension(0, 20)));

        // Set Frame defaults
        this.setSize(450, 280);
        this.setLocationRelativeTo(null);
        this.setContentPane(pnMain);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(btnReceiveClient)) {
            new SearchAppointmentFrm(u).setVisible(true);
            this.dispose();
        }
    }
}
