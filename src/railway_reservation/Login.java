package railway_reservation;

import javax.swing.*;
import javax.swing.border.LineBorder;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Login {
	
	public Login() {
		initialize_UI();
		initialize_Connection();
	}
	
	private Connection con;
	PreparedStatement pstmt;
	
	public void initialize_Connection() {
		final String URL = "jdbc:mysql://Localhost:3306/railway";
        try {
            // Load MySQL JDBC Driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            // Establish the connection
            con = DriverManager.getConnection(URL, "root", "Ysingla@25");
            if (con != null) {
                System.out.println("Connection established successfully.");
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("JDBC Driver not found.");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Connection initialization failed: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Unexpected error: " + e.getMessage());
        }
	}
	
	public void initialize_UI() {
		JFrame frame = new JFrame("Login");
		
		JLabel l1 = new JLabel("RAIL RESERVATION SYSTEM");
		l1.setBounds(100,10,300,25);
		l1.setFont(new Font("TIMES NEW ROMAN",Font.BOLD,20));
		l1.setForeground(Color.RED);
		frame.add(l1);
		
		JLabel l2 = new JLabel("Username");
		l2.setBounds(130,50,80,20);
		l2.setFont(new Font("Times New Roman",Font.BOLD,17));
		frame.add(l2);
		
		JTextField t1 = new JTextField();
		t1.setBounds(230,50,120,22);
		t1.setBorder(new LineBorder(Color.black,1));
		frame.add(t1);
		
		JLabel l3 = new JLabel("Password");
		l3.setBounds(130,80,80,22);
		l3.setFont(new Font("Times New Roman",Font.BOLD,17));
		frame.add(l3);
		
		JTextField t2 = new JTextField();
		t2.setBounds(230,80,120,22);
		t2.setBorder(new LineBorder(Color.black,1));
		frame.add(t2);
		
		JButton b1 = new JButton("Login");
		b1.setBounds(140,150,90,25);
		b1.setBackground(Color.BLUE);
		b1.setForeground(Color.white);
		b1.setFont(new Font("Times New Roman",Font.BOLD,17));
		frame.add(b1);
		b1.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent arg0) {
		    	String userid1 = t1.getText();
                String password1 = t2.getText();
                try {
                	pstmt = con.prepareStatement("select * from login where username = '"+userid1+"' and password = '"+password1+"'");
                	ResultSet rs = pstmt.executeQuery();
                	while(rs.next()) {
		                if(userid1.equals(rs.getString("username")) && password1.equals(rs.getString("password"))){
		                    JOptionPane.showMessageDialog(null,"login successfully");
		                    new home_page();
		                    frame.dispose();
		                }else{
		                    JOptionPane.showMessageDialog(null,"Invalid username or password");
		                }
                	}
                }catch(Exception e) {
                	e.printStackTrace();
                }
		    }
		});
		
		JButton b2 = new JButton("Cancel");
		b2.setBounds(260,150,90,25);
		b2.setBackground(Color.BLUE);
		b2.setForeground(Color.white);
		b2.setFont(new Font("Times New Roman",Font.BOLD,17));
		frame.add(b2);
		b2.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent arg0) {
		    	frame.dispose();
		    }
		});
		
		frame.setSize(500,270);
		frame.setLayout(null);
		frame.setBackground(Color.white);
		frame.setLocationRelativeTo(null); // Center the frame
		frame.setVisible(true);
	}
	
	public static void main(String args[]) {
		new Login();
	}
}

