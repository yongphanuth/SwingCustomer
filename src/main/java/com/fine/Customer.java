package com.fine;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.jar.JarEntry;

public class Customer extends JFrame {
    private JLabel labelId, labelLastName, labelFirstName, labelPhone;
    private JTextField textId, textLastName, textFirstName, textPhone;
    private JButton btnPrevious, btnNext;
    private Connection con;
    private PreparedStatement pstmt;
    private ResultSet rs;

    public Customer(){
        initComponents();
        DBconnection();
        loadCustomerData();
    }

    private void initComponents() {
        labelId = new JLabel("ID: ");
        labelLastName = new JLabel("Last Name: ");
        labelFirstName = new JLabel("First Name: ");
        labelPhone = new JLabel("Phone: ");

        textId = new JTextField(5);
        textLastName = new JTextField(20);
        textFirstName = new JTextField(20);
        textPhone = new JTextField(15);

        textId.setEditable(false);
        textLastName.setEditable(false);
        textFirstName.setEditable(false);
        textPhone.setEditable(false);

        btnPrevious = new JButton("Previous");
        btnNext = new JButton("Next");

        setLayout(new GridLayout(5,2));
        add(labelId);
        add(textId);
        add(labelLastName);
        add(textLastName);
        add(labelFirstName);
        add(textFirstName);
        add(labelPhone);
        add(textPhone);
        add(btnPrevious);
        add(btnNext);

        btnPrevious.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loadPreviousCustomer();
            }
        });

        btnNext.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loadNextCustomer();
            }
        });

        setTitle("Customer");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void DBconnection() {
        try {
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/final-java", "root", "pwd@123");
            System.out.println("Connected to database");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadCustomerData() {
        try {
            String sql = "SELECT * FROM customer";
            pstmt = con.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                displayCustomerData();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadPreviousCustomer() {
        try {
            if (rs.previous()) {
                displayCustomerData();
            } else {
                rs.next();
                JOptionPane.showMessageDialog(this, "No previous record.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadNextCustomer() {
        try {
            if (rs.next()) {
                displayCustomerData();
            } else {
                rs.previous();
                JOptionPane.showMessageDialog(this, "No next record.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    private void displayCustomerData() {
        try {
            textId.setText(rs.getString("customer_id"));
            textLastName.setText(rs.getString("customer_last_name"));
            textFirstName.setText(rs.getString("customer_first_name"));
            textPhone.setText(rs.getString("customer_phone"));

            btnPrevious.setEnabled(!rs.isFirst());

            btnNext.setEnabled(!rs.isLast());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}