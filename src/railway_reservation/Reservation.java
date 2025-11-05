package railway_reservation;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.UUID;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;

public class Reservation {
    private Connection con;
    private PreparedStatement pstmt;
    private DefaultTableModel tableModel;
    private JTable table;
    protected JComboBox<String> cbSource;
    protected JComboBox<String> cbDestination;
    private JComboBox<String> cbTrainNo;
    private JComboBox<String> cbClass;
    private JFrame frame;
    private JTextField pidField;
    private String PNR = generatePNR();
    
    public Reservation() {
        initializeConnection();
        initializeUI();
    }
    
    private void initializeConnection() {
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
	public void initializeUI() {
        frame = new JFrame("Reservation");
        
        JLabel title = new JLabel("RAIL RESERVATION SYSTEM");
        title.setBounds(380, 35, 450, 25);
        title.setFont(new Font("Algerian", Font.BOLD, 30));
        title.setForeground(Color.DARK_GRAY);
        frame.add(title);
        
        JLabel sourceLabel = new JLabel("Source:");
        sourceLabel.setBounds(390, 100, 100, 10);
        sourceLabel.setFont(new Font("Arial", Font.BOLD, 15));
        frame.add(sourceLabel);
        
        cbSource = new JComboBox<>();
        cbSource.setBounds(500, 95, 100, 20);
        frame.add(cbSource);
        
        JLabel destinationLabel = new JLabel("Destination:");
        destinationLabel.setBounds(390, 140, 100, 10);
        destinationLabel.setFont(new Font("Arial", Font.BOLD, 15));
        frame.add(destinationLabel);
        
        cbDestination = new JComboBox<>();
        cbDestination.setBounds(500, 135, 100, 20);
        frame.add(cbDestination);
        
        populateComboBox(cbSource, "SELECT DISTINCT source FROM train_info");
        populateComboBox(cbDestination, "SELECT DISTINCT dest FROM train_info");
        
        JButton searchButton = new JButton("Search");
        searchButton.setBounds(650, 110, 120, 40);
        searchButton.setForeground(Color.BLACK);
        searchButton.setBackground(Color.GREEN);
        searchButton.setFont(new Font("Times New Roman", Font.BOLD, 20));
        frame.add(searchButton);
        searchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                if (cbSource.getSelectedItem().toString().isEmpty() || cbDestination.getSelectedItem().toString().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Source or destination cannot be empty");
                } else {
                    updateTable();
                }
            }
        });
        
        String[] column = {"Train no", "Train name", "2S", "Sleeper", "3AC", "2AC", "1AC"};
        tableModel = new DefaultTableModel(column, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make all cells non-editable
            }
        };
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(300, 250, 600, 100);
        frame.add(scrollPane);
        
        JButton bookButton = new JButton("Book Ticket");
        bookButton.setBounds(350, 400, 150, 40);
        bookButton.setForeground(Color.BLACK);
        bookButton.setBackground(Color.GREEN);
        bookButton.setFont(new Font("Times New Roman", Font.BOLD, 20));
        frame.add(bookButton);
        bookButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                book();
            }
        });
        
        JButton homeButton = new JButton("Home");
        homeButton.setBounds(680, 400, 150, 40);
        homeButton.setForeground(Color.BLACK);
        homeButton.setBackground(Color.GREEN);
        homeButton.setFont(new Font("Times New Roman", Font.BOLD, 20));
        frame.add(homeButton);
        homeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                frame.dispose();
            	new home_page();
            }
        });
        
        frame.setSize(1100, 800);
        frame.setLayout(null);
        frame.setBackground(Color.WHITE);
        frame.setLocationRelativeTo(null); // Center the frame
        frame.setVisible(true);
    }
    
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
    
    protected void updateTable() {
        ResultSet rs = null;
        try {
            pstmt = con.prepareStatement("SELECT train_avail.t_no, train_avail.t_name, General, sleeper, 3ac, 2ac, 1ac "
                    + "FROM train_avail "
                    + "JOIN train_info ON train_avail.t_no = train_info.t_no "
                    + "WHERE source = ? AND dest = ?");
            pstmt.setString(1, cbSource.getSelectedItem().toString());
            pstmt.setString(2, cbDestination.getSelectedItem().toString());
            
            if (cbSource.getSelectedItem().toString().equals(cbDestination.getSelectedItem().toString())) {
                JOptionPane.showMessageDialog(null, "Source and destination could not be same");
                return;
            }
            
            rs = pstmt.executeQuery();
            
            // Clear existing rows
            tableModel.setRowCount(0);
            
            while (rs.next()) {
                Object[] row = {
                    rs.getString("t_no"),
                    rs.getString("t_name"),
                    rs.getString("General"),
                    rs.getString("sleeper"),
                    rs.getString("3ac"),
                    rs.getString("2ac"),
                    rs.getString("1ac")
                };
                tableModel.addRow(row);
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
    
    private void book() {
        ResultSet rs = null;

        try {
            pstmt = con.prepareStatement("SELECT train_avail.t_no, train_avail.t_name, General, sleeper, 3ac, 2ac, 1ac "
                    + "FROM train_avail "
                    + "JOIN train_info ON train_avail.t_no = train_info.t_no "
                    + "WHERE source = ? AND dest = ?");
            pstmt.setString(1, cbSource.getSelectedItem().toString());
            pstmt.setString(2, cbDestination.getSelectedItem().toString());

            if (cbSource.getSelectedItem().toString().equals(cbDestination.getSelectedItem().toString())) {
                JOptionPane.showMessageDialog(null, "Source and destination cannot be the same");
                return;
            }

            rs = pstmt.executeQuery();

            boolean ticketsAvailable = false;

            while (rs.next()) {
                if (rs.getInt("General") > 0) {
                    ticketsAvailable = true;
                    break;
                } else if (rs.getInt("sleeper") > 0) {
                    ticketsAvailable = true;
                    break;
                } else if (rs.getInt("3ac") > 0) {
                    ticketsAvailable = true;
                    break;
                } else if (rs.getInt("2ac") > 0) {
                    ticketsAvailable = true;
                    break;
                } else if (rs.getInt("1ac") > 0) {
                    ticketsAvailable = true;
                    break;
                }
            }

            if (!ticketsAvailable) {
                JOptionPane.showMessageDialog(null, "Tickets not available");
                return;
            }

            frame.dispose();
            bookTicket();

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


    private void bookTicket() {
        JFrame frame1 = new JFrame("Reservation");

        JLabel title = new JLabel("RAIL RESERVATION SYSTEM");
        title.setBounds(380, 35, 450, 25);
        title.setFont(new Font("Algerian", Font.BOLD, 30));
        title.setForeground(Color.DARK_GRAY);
        frame1.add(title);

        JLabel detailsLabel = new JLabel("Enter details of reservation:");
        detailsLabel.setBounds(390, 100, 250, 10);
        detailsLabel.setFont(new Font("Arial", Font.BOLD, 15));
        frame1.add(detailsLabel);

        JLabel trainNoLabel = new JLabel("Train No.");
        trainNoLabel.setBounds(400, 130, 100, 20);
        trainNoLabel.setFont(new Font("Arial", Font.PLAIN, 15));
        frame1.add(trainNoLabel);

        cbTrainNo = new JComboBox<>();
        cbTrainNo.setBounds(540, 130, 100, 20);
        frame1.add(cbTrainNo);
        populateComboBox(cbTrainNo, "SELECT train_avail.t_no FROM train_avail JOIN train_info ON train_avail.t_no = train_info.t_no WHERE source = '" + cbSource.getSelectedItem().toString() + "' AND dest = '" + cbDestination.getSelectedItem().toString() + "'");

        JLabel trainNameLabel = new JLabel("Train Name");
        trainNameLabel.setBounds(400, 160, 100, 20);
        trainNameLabel.setFont(new Font("Arial", Font.PLAIN, 15));
        frame1.add(trainNameLabel);

        JTextField trainNameField = new JTextField();
        trainNameField.setBounds(540, 160, 100, 20);
        trainNameField.setEditable(false);
        frame1.add(trainNameField);
        
        JLabel classLabel = new JLabel("Class");
        classLabel.setBounds(400, 190, 100, 20);
        classLabel.setFont(new Font("Arial", Font.PLAIN, 15));
        frame1.add(classLabel);

        cbClass = new JComboBox<>();
        cbClass.setBounds(540, 190, 100, 20);
        frame1.add(cbClass);

        cbTrainNo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                try {
                    ResultSet rs = null;
                    PreparedStatement pstmt = null;
                    pstmt = con.prepareStatement("SELECT t_name FROM train_info WHERE t_no = ?");
                    pstmt.setString(1, cbTrainNo.getSelectedItem().toString());
                    rs = pstmt.executeQuery();
                    if (rs.next()) {
                        trainNameField.setText(rs.getString("t_name"));
                    } else {
                        trainNameField.setText("");
                    }
                    populateClassComboBox(cbClass, "SELECT General, sleeper, 3ac, 2ac, 1ac FROM train_avail WHERE train_avail.t_no = ?");
                    if (rs != null) rs.close();
                    
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });
        
        
        JLabel boardLabel = new JLabel("Boarding at:");
        boardLabel.setBounds(400,220,100,20);
        boardLabel.setFont(new Font("Arial", Font.PLAIN, 15));
        frame1.add(boardLabel);
        
        JTextField boardField = new JTextField(cbSource.getSelectedItem().toString());
        boardField.setBounds(540,220,100,20);
        boardField.setEditable(false);
        frame1.add(boardField);
        
                
        JLabel todayLabel = new JLabel("Date of Journey:");
        todayLabel.setBounds(400, 250, 150, 20);
        todayLabel.setFont(new Font("Arial", Font.PLAIN, 15));
        frame1.add(todayLabel);
        
        LocalDate current_Date = LocalDate.now();
        System.out.println(current_Date);
        
		JTextField dateField = new JTextField();
		dateField.setBounds(540,250,100,20);
        dateField.setText(current_Date.toString());
        frame1.add(dateField);
        
        
        JButton next = new JButton("Next");
        next.setBounds(380,320,110,30);
        next.setForeground(Color.BLACK);
        next.setBackground(Color.GREEN);
        next.setFont(new Font("Times New Roman", Font.BOLD, 20));
        frame1.add(next);
        next.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
            	
                try {
                	String pid = passenger();
                	
                	pstmt = con.prepareStatement("Insert into reservation(Train_no,Train_name,class,Date_of_journey,j_begin,boarding_at,destination,PNR,pid) values(?,?,?,?,?,?,?,?,?)");
                	pstmt.setString(1, cbTrainNo.getSelectedItem().toString());
                	pstmt.setString(2, trainNameField.getText());
                	pstmt.setString(3, cbClass.getSelectedItem().toString());
                	pstmt.setString(4, todayLabel.getText());
                	pstmt.setString(5, cbSource.getSelectedItem().toString());
                	pstmt.setString(6, boardField.getText());
                	pstmt.setString(7, cbDestination.getSelectedItem().toString());
                	pstmt.setString(8, PNR);
                	pstmt.setString(9, pid);
                	
                	pstmt.executeUpdate();
                	
                	frame1.dispose();
                	
                }catch(SQLException e) {
                	e.printStackTrace();
                }
            }
        });
        
        JButton back = new JButton("Back");
        back.setBounds(580,320,110,30);
        back.setForeground(Color.BLACK);
        back.setBackground(Color.GREEN);
        back.setFont(new Font("Times New Roman", Font.BOLD, 20));
        frame1.add(back);
        back.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                frame1.dispose();
                new Reservation();
            }
        });
        
        frame1.setSize(1100, 800);
        frame1.setLayout(null);
        frame1.setBackground(Color.WHITE);
        frame1.setLocationRelativeTo(null); // Center the frame
        frame1.setVisible(true);
    }
    
    public static String generatePNR() {
        String pnr = UUID.randomUUID().toString().replace("-", "").substring(0, 10).toUpperCase();
        return pnr;
    }
    
    private void populateClassComboBox(JComboBox<String> comboBox, String query) {
        ResultSet rs = null;
        try {
            pstmt = con.prepareStatement(query);
            pstmt.setString(1, cbTrainNo.getSelectedItem().toString());
            rs = pstmt.executeQuery();
            comboBox.removeAllItems();
            while (rs.next()) {
                if (rs.getInt("General") > 0) {
                    comboBox.addItem("General");
                }
                if (rs.getInt("sleeper") > 0) {
                    comboBox.addItem("sleeper");
                }
                if (rs.getInt("3ac") > 0) {
                    comboBox.addItem("3ac");
                }
                if (rs.getInt("2ac") > 0) {
                    comboBox.addItem("2ac");
                }
                if (rs.getInt("1ac") > 0) {
                    comboBox.addItem("1ac");
                }
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
    
    
    
    public String passenger() {
    	JFrame frame2 = new JFrame("Passenger");
    	
    	JLabel title = new JLabel("RAIL RESERVATION SYSTEM");
        title.setBounds(380, 35, 450, 25);
        title.setFont(new Font("Algerian", Font.BOLD, 30));
        title.setForeground(Color.DARK_GRAY);
        frame2.add(title);
        
        JLabel detailsLabel = new JLabel("Enter details of Passenger:");
        detailsLabel.setBounds(390, 100, 250, 10);
        detailsLabel.setFont(new Font("Arial", Font.BOLD, 15));
        frame2.add(detailsLabel);
        
        JLabel pidLabel = new JLabel("Passenger id:");
        pidLabel.setBounds(400, 130, 100, 20);
        pidLabel.setFont(new Font("Arial", Font.PLAIN, 15));
        frame2.add(pidLabel);
        
        pidField = new JTextField();
        pidField.setBounds(540, 130, 100, 20);
        frame2.add(pidField);
        
        JLabel name = new JLabel("Name");
		name.setBounds(400,160,160,20);
		name.setFont(new Font("Plain",Font.BOLD,15));
		frame2.add(name);
		
		JTextField nameField = new JTextField();
		nameField.setBounds(540,160,120,20);
		frame2.add(nameField);
		
		JLabel age = new JLabel("Age");
		age.setBounds(400,190,100,20);
		age.setFont(new Font("Plain",Font.BOLD,15));
		frame2.add(age);
		
		JTextField ageField = new JTextField();
		ageField.setBounds(540,190,120,20);
		frame2.add(ageField);
		
		JLabel gender = new JLabel("Gender");
		gender.setBounds(400,220,100,20);
		gender.setFont(new Font("Plain",Font.BOLD,15));
		frame2.add(gender);
		
		String Gender[] = {"Select","Male","Female"};
		JComboBox<Object> cbGender = new JComboBox<Object>(Gender);
		cbGender.setBounds(540,220,120,20);
		frame2.add(cbGender);
		
		JLabel category = new JLabel("Category");
		category.setBounds(400,250,100,20);
		category.setFont(new Font("Plain",Font.BOLD,15));
		frame2.add(category);
		
		JRadioButton r1 = new JRadioButton("General");
		r1.setBounds(530,250,100,20);
		frame2.add(r1);
		JRadioButton r2 = new JRadioButton("Sr.Citizen");
		r2.setBounds(630,250,100,20);
		frame2.add(r2);
		JRadioButton r3 = new JRadioButton("Ex-Servicemen");
		r3.setBounds(730,250,150,20);
		frame2.add(r3);
		ButtonGroup bg = new ButtonGroup();
		bg.add(r3); bg.add(r2); bg.add(r1);
		
		JLabel address = new JLabel("Address");
		address.setBounds(400,280,100,20);
		address.setFont(new Font("Plain",Font.BOLD,15));
		frame2.add(address);
		
		JTextArea addressArea = new JTextArea();
		addressArea.setBounds(540,280,150,40);
		Border lineBorder = BorderFactory.createLineBorder(Color.BLACK, 1);
		addressArea.setBorder(lineBorder);
        frame2.add(addressArea);
        
        JButton confirm = new JButton("Confirm booking");
        confirm.setBounds(380,350,200,30);
        confirm.setForeground(Color.BLACK);
        confirm.setBackground(Color.GREEN);
        confirm.setFont(new Font("Times New Roman", Font.BOLD, 20));
        frame2.add(confirm);
        confirm.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
        		int a = JOptionPane.showConfirmDialog(frame2, "Do You want to Confirm your booking", "Select", JOptionPane.YES_NO_OPTION);
        		
            	try {
            		if(a==JOptionPane.YES_OPTION){
            			JOptionPane.showMessageDialog(null, "Your booking has been confirmed");
            			JOptionPane.showMessageDialog(null, "Your PNR no. is:"+PNR);
            			
            			String Category = null;
    		    		if(r1.isSelected()) {
    		    			Category="General";
    		    		}else if(r2.isSelected()) {
    		    			Category="Sr.Citizen";
    		    		}else if(r3.isSelected()) {
    		    			Category="Ex-Servicemen";
    		    		}
            			
            			pstmt = con.prepareStatement("Insert into passenger(pid,name,age,gender,category,address) values(?,?,?,?,?,?)");
            			pstmt.setString(1, pidField.getText());
            			pstmt.setString(2, nameField.getText());
            			pstmt.setString(3, ageField.getText());
            			pstmt.setString(4, cbGender.getSelectedItem().toString());
            			pstmt.setString(5, Category);
            			pstmt.setString(6, addressArea.getText());
            			
            			pstmt.executeUpdate();
            			
            			pstmt = con.prepareStatement("update reservation set pid=? where PNR=?");
            			pstmt.setString(1, pidField.getText());
            			pstmt.setString(2, PNR);
            			
            			pstmt.executeUpdate();
            			
            			try {
            				pstmt = con.prepareStatement("SELECT train_no, class FROM reservation WHERE PNR = ?");
            		        pstmt.setString(1, PNR);
            		        ResultSet rs = pstmt.executeQuery();

            		        if (rs.next()) {
            		            String trainNo = rs.getString("train_no");
            		            String reservedClass = rs.getString("class");

            		            // Update train availability based on retrieved details
            		            updateTrainAvailability(trainNo, reservedClass);
            		        } else {
            		            System.err.println("Reservation details not found for PNR: " + PNR);
            		        }

            		        rs.close();
            			}catch(SQLException e) {
                    		e.printStackTrace();
                    	}
            			
            			frame2.dispose();
            			new Reservation();
            		}else {
            			
            		}
            	}catch(Exception e) {
            		e.printStackTrace();
            	}finally {
            		if(pstmt!=null)
						try {
							pstmt.close();
						} catch (SQLException e) {
							e.printStackTrace();
						}
            	}
            	
            	
            }
        });
        
        JButton cancel = new JButton("Cancel");
        cancel.setBounds(680,350,110,30);
        cancel.setForeground(Color.BLACK);
        cancel.setBackground(Color.GREEN);
        cancel.setFont(new Font("Times New Roman", Font.BOLD, 20));
        frame2.add(cancel);
        cancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
            	try {
            		int a =JOptionPane.showConfirmDialog(frame2, "Do You want to Cancel your booking", "Select", JOptionPane.YES_NO_OPTION);
            		if(a==JOptionPane.YES_OPTION) {
            			frame2.dispose();
            			new Reservation();
        			
            			pstmt = con.prepareStatement("delete from reservation where pnr=?");
            			pstmt.setString(1, PNR);
        			
            			pstmt.executeUpdate();
            		}
            	}catch(Exception e) {
            		e.printStackTrace();
            	}
            }
        });
    	
    	frame2.setSize(1100, 800);
        frame2.setLayout(null);
        frame2.setBackground(Color.WHITE);
        frame2.setLocationRelativeTo(null); // Center the frame
        frame2.setVisible(true);
        
    	return pidField.getText();
    }
    
    
    private void updateTrainAvailability(String trainNo, String reservedClass) throws SQLException {
        pstmt = con.prepareStatement("UPDATE train_avail SET " + reservedClass + " = " + reservedClass + " - 1"
                + " WHERE t_no = ?");
        pstmt.setString(1, trainNo);
        pstmt.executeUpdate();
    }

    
	public static void main(String[] args) {
	    new Reservation();
	}
}
