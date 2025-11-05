package railway_reservation;

import javax.swing.*;
import java.awt.*;

public class Welcome implements Runnable {
    JFrame frame;
    Thread t;

    public Welcome() {
        initialize_UI();
    }

    @SuppressWarnings("serial")
	public void initialize_UI() {
        frame = new JFrame("Welcome!");
        t = new Thread(this);

        // Create a panel with a background image
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon icon = new ImageIcon("D:/java/railway_reservation/src/r3.jpg"); // Add your background image path here
                g.drawImage(icon.getImage(), 0, 0, getWidth(), getHeight(), this);
            }
        };
        panel.setLayout(null);

        // Enhanced label for the system title
        JLabel l1 = new JLabel("Railway Reservation System");
        l1.setBounds(100, 100, 500, 50);
        l1.setFont(new Font("Algerian", Font.BOLD, 30));
        l1.setForeground(Color.RED);
        panel.add(l1);

        // Enhanced label for the author name
        JLabel l2 = new JLabel("_YASH KUMAR");
        l2.setBounds(400, 160, 200, 30);
        l2.setFont(new Font("Algerian", Font.BOLD, 25));
        l2.setForeground(Color.blue);
        panel.add(l2);

        frame.add(panel);
        frame.setSize(800, 500);
        frame.setLocationRelativeTo(null); // Center the frame
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        t.start();
    }

    public void run() {
        int x = 1;
        while (x <= 10) {
            try {
                Thread.sleep(600);
            } catch (Exception e) {
                e.printStackTrace();
            }
            x++;
        }
        frame.dispose();
        new Login();
    }

    public static void main(String[] args) {
        new Welcome();
    }
}
