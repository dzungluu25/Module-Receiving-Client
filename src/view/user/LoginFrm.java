package view.user;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import dao.UserDAO;
import model.User;

public class LoginFrm extends JFrame implements ActionListener {
	private JTextField txtUsername;
	private JPasswordField txtPassword;
	private JButton btnLogin;

	public LoginFrm() {
		super("Login");

		JPanel pnMain = new JPanel(new GridLayout(3, 1, 5, 5));

		JPanel pnUsername = new JPanel(new FlowLayout());
		pnUsername.add(new JLabel("Username:"));
		txtUsername = new JTextField(15);
		pnUsername.add(txtUsername);
		pnMain.add(pnUsername);

		JPanel pnPass = new JPanel(new FlowLayout());
		pnPass.add(new JLabel("Password:"));
		txtPassword = new JPasswordField(15);
		txtPassword.setEchoChar('*');
		pnPass.add(txtPassword);
		pnMain.add(pnPass);

		JPanel pnBtn = new JPanel(new FlowLayout());
		btnLogin = new JButton("Login");
		btnLogin.addActionListener(this);
		pnBtn.add(btnLogin);
		pnMain.add(pnBtn);

		this.setContentPane(pnMain);
		this.setSize(350, 180);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(btnLogin)) {
			User user = new User();
			user.setUsername(txtUsername.getText().trim());
			user.setPassword(txtPassword.getText().trim());

			UserDAO ud = new UserDAO();
			if (ud.checkLogin(user)) {
				if (user.getPosition() != null && user.getPosition().equalsIgnoreCase("receptionist")) {
					new ReceptionistHomeFrm(user).setVisible(true);
					this.dispose();
				} else {
					JOptionPane.showMessageDialog(this,
							"The function of the role " + user.getPosition() + " is under construction!");
				}
			} else {
				JOptionPane.showMessageDialog(this, "Incorrect username and/or password!");
			}
		}
	}

	public static void main(String[] args) {
		new LoginFrm().setVisible(true);
	}
}
