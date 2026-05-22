package view.user;

import view.receiving.SearchAppointmentFrm;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import model.User;

public class ReceptionistHomeFrm extends JFrame implements ActionListener {
    private User u;
    private JButton btnReceiveClient;

    public ReceptionistHomeFrm(User u) {
        super("Receptionist Dashboard");
        this.u = u;

        JPanel pnMain = new JPanel(new GridLayout(2, 1, 10, 10));

        JLabel lblWelcome = new JLabel("Welcome back, " + (u != null ? u.getName() : "Receptionist") + "!", JLabel.CENTER);
        pnMain.add(lblWelcome);

        JPanel pnBtn = new JPanel(new FlowLayout());
        btnReceiveClient = new JButton("Receive Customers");
        btnReceiveClient.addActionListener(this);
        pnBtn.add(btnReceiveClient);
        pnMain.add(pnBtn);

        this.setContentPane(pnMain);
        this.setSize(350, 180);
        this.setLocationRelativeTo(null);
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
