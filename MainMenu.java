import java.awt.EventQueue;
import java.awt.CardLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.Font;
import java.awt.Color;
import javax.swing.SwingConstants;

public class MainMenu extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField textField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainMenu frame = new MainMenu();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public MainMenu() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 626, 562);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new CardLayout(0, 0));
		
		// --- 1. MENU PANEL ---
		JPanel Menu = new JPanel();
		Menu.setBackground(new Color(128, 128, 128));
		Menu.setLayout(null); 
		contentPane.add(Menu, "MENU");
		
		JButton btnNewButton_1_1_2 = new JButton("Load Game");
		btnNewButton_1_1_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnNewButton_1_1_2.setBounds(231, 237, 162, 56);
		Menu.add(btnNewButton_1_1_2);
		
		JButton btnNewButton_2 = new JButton("New Game");
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnNewButton_2.setBounds(231, 157, 162, 56);
		Menu.add(btnNewButton_2);
		
		JButton btnNewButton_1_1_2_1 = new JButton("Quit Game");
		btnNewButton_1_1_2_1.setBounds(231, 317, 162, 56);
		Menu.add(btnNewButton_1_1_2_1);
		
		JLabel lblNewLabel = new JLabel("Welcome to Potion Prodigy!");
		lblNewLabel.setForeground(Color.CYAN);
		lblNewLabel.setFont(new Font("MS UI Gothic", Font.PLAIN, 37));
		lblNewLabel.setBounds(97, 56, 441, 82);
		Menu.add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("Marquez, Panganiban Circa 2026");
		lblNewLabel_1.setBounds(0, 509, 173, 14);
		Menu.add(lblNewLabel_1);
		
		// --- 2. NAME INPUT PANEL ---
		JPanel nameInput = new JPanel();
		nameInput.setBackground(new Color(128, 128, 128));
		nameInput.setLayout(null);
		contentPane.add(nameInput, "NAME_INPUT");
		
		JLabel lblNewLabel_2 = new JLabel("Enter your name, Alchemist:");
		lblNewLabel_2.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_2.setForeground(Color.CYAN);
		lblNewLabel_2.setFont(new Font("MS UI Gothic", Font.PLAIN, 28));
		lblNewLabel_2.setBounds(117, 137, 379, 56);
		nameInput.add(lblNewLabel_2);
		
		textField = new JTextField();
		textField.setBounds(184, 219, 245, 36);
		nameInput.add(textField);
		textField.setColumns(10);
		
		JButton btnNewButton_3 = new JButton("Start Journey");
		btnNewButton_3.setBounds(184, 286, 114, 42);
		nameInput.add(btnNewButton_3);
		
		JButton btnNewButton_4 = new JButton("Back");
		btnNewButton_4.setBounds(315, 286, 114, 42);
		nameInput.add(btnNewButton_4);
		
		// --- 3. DASHBOARD PANEL ---
		JPanel Dashboard = new JPanel();
		Dashboard.setBackground(new Color(128, 128, 128));
		Dashboard.setLayout(null);
		contentPane.add(Dashboard, "DASHBOARD");
		
		JLabel lblNewLabel_3 = new JLabel("Player Dashboard");
		lblNewLabel_3.setForeground(Color.CYAN);
		lblNewLabel_3.setFont(new Font("MS UI Gothic", Font.PLAIN, 30));
		lblNewLabel_3.setBounds(27, 25, 269, 42);
		Dashboard.add(lblNewLabel_3);
		
		JButton btnNewButton_5 = new JButton("Visit Market");
		btnNewButton_5.setBounds(410, 135, 145, 60);
		Dashboard.add(btnNewButton_5);
		
		JButton btnNewButton_6 = new JButton("View Inventory");
		btnNewButton_6.setBounds(410, 225, 145, 60);
		Dashboard.add(btnNewButton_6);
		
		JButton btnNewButton_7 = new JButton("Brew Potion");
		btnNewButton_7.setBounds(410, 315, 145, 60);
		Dashboard.add(btnNewButton_7);
		
		// Placeholder for player stats
		JLabel lblNewLabel_4 = new JLabel("Current Gold: 100g");
		lblNewLabel_4.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblNewLabel_4.setForeground(Color.WHITE);
		lblNewLabel_4.setBounds(27, 90, 162, 28);
		Dashboard.add(lblNewLabel_4);
	}
}