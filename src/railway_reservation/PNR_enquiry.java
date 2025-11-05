package railway_reservation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.ResultSetMetaData;

public class PNR_enquiry {
    private JFrame frame;
    private Connection con;
    private PreparedStatement pstmt;
    private JComboBox<String> cbPNR;
    private JTextArea a1, a2;

    public PNR_enquiry() {
        initialize_Connection();
        initialize_UI();
    }

    public void initialize_Connection() {
        final String URL = "jdbc:mysql://localhost:3306/railway";
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
        frame = new JFrame("PNR Enquiry");
        

        // Background panel
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
        l1.setBounds(350, 20, 450, 25);
        l1.setFont(new Font("TIMES NEW ROMAN", Font.BOLD, 30));
        l1.setForeground(Color.RED);
        panel.add(l1);

        // PNR input label
        JLabel l2 = new JLabel("Enter your PNR No.");
        l2.setBounds(250, 100, 250, 20);
        l2.setFont(new Font("Times New Roman", Font.BOLD, 20));
        l2.setForeground(Color.YELLOW);
        panel.add(l2);

        // PNR selection combobox
        cbPNR = new JComboBox<>();
        cbPNR.setBounds(450, 100, 140, 20);
        panel.add(cbPNR);
        populateComboBox(cbPNR, "select pnr from reservation");

        // PNR details label
        JLabel l3 = new JLabel("PNR Details");
        l3.setBounds(280, 190, 250, 20);
        l3.setFont(new Font("Times New Roman", Font.BOLD, 20));
        l3.setForeground(Color.YELLOW);
        panel.add(l3);

        // PNR details text area with scroll pane
        a1 = new JTextArea();
        a1.setFont(new Font("Monospaced", Font.PLAIN, 14));
        a1.setEditable(false);
        JScrollPane sp1 = new JScrollPane(a1);
        sp1.setBounds(270, 220, 600, 120);
        panel.add(sp1);

        // Passenger details label
        JLabel l4 = new JLabel("Passenger Details");
        l4.setBounds(280, 370, 250, 20);
        l4.setFont(new Font("Times New Roman", Font.BOLD, 25));
        l4.setForeground(Color.YELLOW);
        panel.add(l4);

        // Passenger details text area with scroll pane
        a2 = new JTextArea();
        a2.setFont(new Font("Monospaced", Font.PLAIN, 14));
        a2.setEditable(false);
        JScrollPane sp2 = new JScrollPane(a2);
        sp2.setBounds(270, 400, 600, 120);
        panel.add(sp2);

        // Search button
        JButton b1 = new JButton("Search");
        b1.setBounds(670, 95, 120, 35);
        b1.setForeground(Color.WHITE);
        b1.setBackground(Color.BLUE);
        b1.setFont(new Font("Times New Roman", Font.BOLD, 20));
        panel.add(b1);
        b1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                searchPNR();
            }
        });

        // Home button
        JButton b2 = new JButton("Home");
        b2.setBounds(840, 95, 120, 35);
        b2.setForeground(Color.WHITE);
        b2.setBackground(Color.BLUE);
        b2.setFont(new Font("Times New Roman", Font.BOLD, 20));
        panel.add(b2);
        b2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                navigateToHome();
            }
        });

        // Frame properties
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1100, 700);
        frame.setLocationRelativeTo(null); // Center the frame
        frame.setVisible(true);
    }

    // Method to populate the PNR ComboBox
    private void populateComboBox(JComboBox<String> comboBox, String query) {
        ResultSet rs = null;
        comboBox.addItem("");
        try {
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

    // Method to search for PNR details
    private void searchPNR() {
        try {
            pstmt = con.prepareStatement("select * from reservation where PNR=?");
            pstmt.setString(1, cbPNR.getSelectedItem().toString());
            ResultSet rs = pstmt.executeQuery();

            a1.setText("");
            a2.setText("");

            if (!rs.isBeforeFirst()) {
                JOptionPane.showMessageDialog(null, "PNR not found");
            } else {
                // Display PNR details
                ResultSetMetaData rsmd = rs.getMetaData();
                int columnCount = rsmd.getColumnCount();
                while (rs.next()) {
                    StringBuilder row = new StringBuilder();
                    for (int i = 1; i <= columnCount; i++) {
                        row.append(rsmd.getColumnName(i)).append(": ").append(rs.getString(i)).append("  ");
                        row.append("\n");
                    }
                    row.append("\n");
                    a1.append(row.toString());

                    // Retrieve passenger details
                    String pid = rs.getString("pid");
                    pstmt = con.prepareStatement("select * from passenger where pid=?");
                    pstmt.setString(1, pid);
                    ResultSet rs1 = pstmt.executeQuery();

                    ResultSetMetaData rsmd1 = rs1.getMetaData();
                    int columnCount1 = rsmd1.getColumnCount();
                    while (rs1.next()) {
                        StringBuilder row1 = new StringBuilder();
                        for (int j = 1; j <= columnCount1; j++) {
                            row1.append(rsmd1.getColumnName(j)).append(": ").append(rs1.getString(j)).append("  ");
                            row1.append("\n");
                        }
                        row1.append("\n");
                        a2.append(row1.toString());
                    }
                    pstmt.close();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to navigate back to home page
    private void navigateToHome() {
        new home_page();
        frame.dispose();
    }

    public static void main(String[] args) {
        new PNR_enquiry();
    }
}
