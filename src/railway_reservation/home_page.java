package railway_reservation;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class home_page {

    public home_page() {
        initialize_UI();
    }

    @SuppressWarnings("serial")
	public void initialize_UI() {
        JFrame frame = new JFrame("Home");
        
        // Background panel
        JPanel panel = new JPanel() {
            private Image background = new ImageIcon("D:\\java\\railway_reservation\\src\\r3.jpg").getImage();

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
            }
        };
        panel.setLayout(null);
        frame.add(panel);
        
        Color customColor = new Color(245, 65, 5);
        
        JLabel l1 = new JLabel("RAIL RESERVATION SYSTEM");
        l1.setBounds(250, 50, 600, 50);
        l1.setFont(new Font("Algerian", Font.BOLD, 30));
        l1.setForeground(customColor);
        panel.add(l1);

        JButton b1 = new JButton("Reservation");
        b1.setBounds(350, 150, 200, 50);
        b1.setForeground(Color.WHITE);
        b1.setBackground(new Color(0, 128, 128));
        b1.setFont(new Font("Times New Roman", Font.BOLD, 20));
        panel.add(b1);
        b1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                new Reservation();
                frame.dispose();
            }
        });

        JButton b2 = new JButton("Cancellation");
        b2.setBounds(350, 220, 200, 50);
        b2.setForeground(Color.WHITE);
        b2.setBackground(new Color(0, 128, 128));
        b2.setFont(new Font("Times New Roman", Font.BOLD, 20));
        panel.add(b2);
        b2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                new Cancellation();
                frame.dispose();
            }
        });

        JButton b3 = new JButton("PNR Enquiry");
        b3.setBounds(350, 290, 200, 50);
        b3.setForeground(Color.WHITE);
        b3.setBackground(new Color(0, 128, 128));
        b3.setFont(new Font("Times New Roman", Font.BOLD, 20));
        panel.add(b3);
        b3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                new PNR_enquiry();
                frame.dispose();
            }
        });

        JButton b4 = new JButton("Exit");
        b4.setBounds(350, 360, 200, 50);
        b4.setForeground(Color.WHITE);
        b4.setBackground(new Color(0, 128, 128));
        b4.setFont(new Font("Times New Roman", Font.BOLD, 20));
        panel.add(b4);
        b4.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                frame.dispose();
            }
        });

        frame.setSize(900, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null); // Center the frame
        frame.setVisible(true);
    }

    public static void main(String args[]) {
        new home_page();
    }
}
