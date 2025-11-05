package railway_reservation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
//import java.awt.event.ItemListener;
import java.sql.*;
//import java.util.*;

public class Cancellation{
	public Cancellation() {
		
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
	
	@SuppressWarnings("serial")
	public void initialize_UI() {
	    JFrame frame = new JFrame("Cancellation");
	    
	    JPanel panel = new JPanel() {
	        private Image background = new ImageIcon("D:\\java\\railway_reservation\\src\\r2.jpg").getImage();

	        @Override
	        protected void paintComponent(Graphics g) {
	            super.paintComponent(g);
	            g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
	        }
	    };
	    panel.setLayout(null);
	    frame.add(panel);
	    
	    // Title label
	    JLabel l1 = new JLabel("RAIL RESERVATION SYSTEM");
	    l1.setBounds(340, 25, 450, 25);
	    l1.setFont(new Font("ALGERIAN", Font.BOLD, 30));
	    l1.setForeground(Color.red); // Set label color to red
	    panel.add(l1);
	    
	    JLabel l2 = new JLabel("PNR No.");
	    l2.setBounds(400,90,120,20);
	    l2.setFont(new Font("Arial",Font.BOLD,18));
	    l2.setForeground(Color.YELLOW); // Set label color to yellow
	    panel.add(l2);
	    
	    JComboBox<String> cbPNR = new JComboBox<>();
	    cbPNR.setBounds(520,90,100,20);
	    panel.add(cbPNR);
	    
	    populateComboBox(cbPNR,"select pnr from reservation");
	    
	    JLabel l3 = new JLabel("Train No.");
	    l3.setBounds(400,130,100,20);
	    l3.setFont(new Font("Arial",Font.BOLD,18));
	    l3.setForeground(Color.YELLOW); // Set label color to yellow
	    panel.add(l3);
	    
	    JTextField t1 = new JTextField();
	    t1.setBounds(520,130,100,20);
	    t1.setEditable(false);
	    panel.add(t1);
	    
	    JLabel l4 = new JLabel("Train Name");
	    l4.setBounds(400,160,120,20);
	    l4.setFont(new Font("Arial",Font.BOLD,18));
	    l4.setForeground(Color.YELLOW); // Set label color to yellow
	    panel.add(l4);
	    
	    JTextField t2 = new JTextField();
	    t2.setBounds(520,160,100,20);
	    t2.setEditable(false);
	    panel.add(t2);
	    
	    JLabel l5 = new JLabel("Class");
	    l5.setBounds(400,190,100,20);
	    l5.setFont(new Font("Arial",Font.BOLD,18));
	    l5.setForeground(Color.YELLOW); // Set label color to yellow
	    panel.add(l5);
	    
	    JTextField t3 = new JTextField();
	    t3.setBounds(520,190,100,20);
	    t3.setEditable(false);
	    panel.add(t3);
	    
	    JLabel l6 = new JLabel("Date of Journey");
	    l6.setBounds(400,220,150,20);
	    l6.setFont(new Font("Arial",Font.BOLD,18));
	    l6.setForeground(Color.YELLOW); // Set label color to yellow
	    panel.add(l6);
	    
	    JTextField t4 = new JTextField();
	    t4.setBounds(560,220,100,20);
	    t4.setEditable(false);
	    panel.add(t4);
	      
	    JLabel l7 = new JLabel("From");
	    l7.setBounds(400,270,50,20);
	    l7.setFont(new Font("Arial",Font.BOLD,18));
	    l7.setForeground(Color.YELLOW); // Set label color to yellow
	    panel.add(l7);
	    JTextField t5 = new JTextField();
	    t5.setBounds(470,270,100,20);
	    t5.setEditable(false);
	    panel.add(t5);
	    JLabel l8 = new JLabel("To");
	    l8.setBounds(590,270,50,20);
	    l8.setFont(new Font("Arial",Font.BOLD,18));
	    l8.setForeground(Color.YELLOW); // Set label color to yellow
	    panel.add(l8);
	    JTextField t6 = new JTextField();
	    t6.setBounds(640,270,100,20);
	    t6.setEditable(false);
	    panel.add(t6);
	    
	    JLabel l9 = new JLabel("Boarding At");
	    l9.setBounds(400,310,150,20);
	    l9.setFont(new Font("Arial",Font.BOLD,18));
	    l9.setForeground(Color.YELLOW); // Set label color to yellow
	    panel.add(l9);
	    
	    JTextField t7 = new JTextField();
	    t7.setBounds(520,310,100,20);
	    t7.setEditable(false);
	    panel.add(t7);
	    
	    JLabel l10 = new JLabel("Passenger id");
	    l10.setBounds(400,340,150,20);
	    l10.setFont(new Font("Arial",Font.BOLD,18));
	    l10.setForeground(Color.YELLOW); // Set label color to yellow
	    panel.add(l10);
	    
	    JTextField t8 = new JTextField();
	    t8.setBounds(520,340,100,20);
	    t8.setEditable(false);
	    panel.add(t8);
	    
	    JButton b1 = new JButton("Search");
	    b1.setBounds(300,410,110,40);
	    b1.setForeground(Color.BLACK);
	    b1.setBackground(Color.GREEN);
	    b1.setFont(new Font("Times New Roman",Font.BOLD,22));
	    panel.add(b1);
	    b1.addActionListener(new ActionListener() {
	        public void actionPerformed(ActionEvent arg0) {
	            try {
	                pstmt = con.prepareStatement("select * from reservation where PNR = ?");
	                pstmt.setString(1, cbPNR.getSelectedItem().toString());
	                ResultSet rs = pstmt.executeQuery();
	                if (!rs.isBeforeFirst()) {
	                    JOptionPane.showMessageDialog(null, "PNR not found");
	                }else {
	                    while(rs.next()) {
	                        t1.setText(rs.getString("Train_no"));
	                        t2.setText(rs.getString("Train_name"));
	                        t3.setText(rs.getString("class"));
	                        t4.setText(rs.getString("Date_of_journey"));
	                        t5.setText(rs.getString("j_begin"));
	                        t6.setText(rs.getString("destination"));
	                        t7.setText(rs.getString("boarding_at"));
	                        t8.setText(rs.getString("pid"));
	                    }
	                    pstmt.close();
	                }
	            }
	            catch(SQLException e) {
	                e.printStackTrace();
	            }
	        }
	    });
	    
	    JButton b2 = new JButton("Home");
	    b2.setBounds(630,410,110,40);
	    b2.setForeground(Color.BLACK);
	    b2.setBackground(Color.GREEN);
	    b2.setFont(new Font("Times New Roman",Font.BOLD,22));
	    panel.add(b2);
	    b2.addActionListener(new ActionListener() {
	        public void actionPerformed(ActionEvent arg0) {
	            new home_page();
	            frame.dispose();
	        }
	    });
	    
	    JButton b3 = new JButton("Delete");
	    b3.setBounds(470,410,110,40);
	    b3.setForeground(Color.BLACK);
	    b3.setBackground(Color.GREEN);
	    b3.setFont(new Font("Times New Roman",Font.BOLD,22));
	    panel.add(b3);
	    b3.addActionListener(new ActionListener() {
	        public void actionPerformed(ActionEvent arg0) {
	            int a = JOptionPane.showConfirmDialog(frame, "Do You want to delete details?", "Select", JOptionPane.YES_NO_OPTION);
	            try {
	                if(cbPNR.getSelectedItem().toString()=="") {
	                    JOptionPane.showMessageDialog(null, "Choose PNR.");
	                    return;
	                }
	                if(a==JOptionPane.YES_OPTION) {
	                    pstmt = con.prepareStatement("SELECT train_no, class FROM reservation WHERE PNR = ?");
	                    pstmt.setString(1, cbPNR.getSelectedItem().toString());
	                    ResultSet rs = pstmt.executeQuery();

	                    if (rs.next()) {
	                        String trainNo = rs.getString("train_no");
	                        String reservedClass = rs.getString("class");

	                        // Update train availability based on retrieved details
	                        updateTrainAvailability(trainNo, reservedClass);
	                    } else {
	                        System.err.println("Reservation details not found for PNR: " + cbPNR.getSelectedItem().toString());
	                    }
	                }
	            }catch(SQLException e) {
	                e.printStackTrace();
	            }
	            
	            try {
	                if(a==JOptionPane.YES_OPTION){
	                    pstmt = con.prepareStatement("delete from reservation where PNR=?");
	                    pstmt.setString(1, cbPNR.getSelectedItem().toString());

	                    pstmt.executeUpdate();
	                    pstmt.close();
	                    JOptionPane.showMessageDialog(null,"data delete successfully");
	                    pstmt.close();
	                }
	            }catch(SQLException e) {
	                e.printStackTrace();
	            }
	            
	            try {
	                if(a==JOptionPane.YES_OPTION){
	                    pstmt = con.prepareStatement("delete from passenger where pid=?");
	                    pstmt.setString(1, t8.getText());

	                    pstmt.executeUpdate();
	                    pstmt.close();
	                }
	            }catch(SQLException e) {
	                e.printStackTrace();
	            }
	            
	            frame.dispose();
	            new Cancellation();
	        }
	    });
	    
	    frame.setSize(1100,800);
	    frame.setBackground(Color.white);
	    frame.setLocationRelativeTo(null); // Center the frame
	    frame.setVisible(true);
	}
	
	
	private void populateComboBox(JComboBox<String> comboBox, String query) {
        ResultSet rs = null;
        comboBox.addItem("");
        try {
        	initialize_Connection();
            pstmt = con.prepareStatement(query);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                comboBox.addItem(rs.getString(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
	
	private void updateTrainAvailability(String trainNo, String reservedClass) throws SQLException {
        pstmt = con.prepareStatement("UPDATE train_avail SET " + reservedClass + " = " + reservedClass + " + 1"
                + " WHERE t_no = ?");
        pstmt.setString(1, trainNo);
        pstmt.executeUpdate();
    }

	
	public static void main(String args[]) {
		
		new Cancellation();
	}
}