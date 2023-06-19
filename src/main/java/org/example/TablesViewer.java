package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class TablesViewer extends JFrame implements ActionListener {
    private JFrame frame;
    private JPanel panel1 = new JPanel();
    private JPanel panel2 = new JPanel();
    private JPanel panel3 = new JPanel();
    private JTextField adres = new JTextField();
    private JLabel lAdres = new JLabel("Введите адрес БД:", SwingConstants.CENTER);
    private JLabel lTable = new JLabel("Выберите таблицу:", SwingConstants.CENTER);
    private JLabel lError = new JLabel("", SwingConstants.CENTER);
    private JButton button = new JButton("Загрузить");
    private JTable table = new JTable();
    private MyTableModel tableModel;
    private Font font = new Font("Sherif", Font.TRUETYPE_FONT, 15);
    public Connection conn;
    private JScrollPane tableScrollPane = new JScrollPane(table);

    private String[] items = new String[0];      //элементы выпадающего списка

    private JComboBox<String> editComboBox = new JComboBox<>(items);

    private SpringLayout layout = new SpringLayout();

    public TablesViewer() {
        frame = new JFrame();
    }

    public void init() {
        setTitle("Tables viewer");
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setLayout(null);
        setResizable(false);

        panel1.setBounds(0, 0, 1200, 30);
        lError.setFont(font);
        panel1.add(lError);
        add(panel1);

        panel2.setBounds(0, 31, 1200, 30);
        add(panel2);
        panel2.setLayout(layout);
        button.addActionListener(this);
        adres.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                try (Connection conn = DriverManager.getConnection(
                        adres.getText(),
                        "root", "password");
                     Statement s = conn.createStatement()) {
                    ResultSet r = s.executeQuery("select count(*)  FROM INFORMATION_SCHEMA.TABLES\n" +
                            "WHERE TABLE_TYPE = 'BASE TABLE' AND TABLE_SCHEMA='order_accounting_system'");
                    while (r.next()) {
                        items = new String[r.getInt(1)];
                    }

                    ResultSet rs = s.executeQuery("SELECT TABLE_NAME \n" +
                            "FROM INFORMATION_SCHEMA.TABLES\n" +
                            "WHERE TABLE_TYPE = 'BASE TABLE' AND TABLE_SCHEMA='order_accounting_system' ");
                    int u = 0;
                    while (rs.next()) {
                        items[u] = rs.getString(1);
                        u++;
                    }
                    editComboBox.setModel(new DefaultComboBoxModel<String>(items));
                } catch (SQLException y) {
                    items = new String[0];
                    editComboBox.setModel(new DefaultComboBoxModel<String>(items));
                    System.out.println("Exception e: " + y.getMessage());
                }
            }
        });
        editComboBox.addActionListener(this);
        panel2.add(lAdres);
        panel2.add(adres);
        panel2.add(lTable);
        panel2.add(editComboBox);
        panel2.add(button);

        adres.setColumns(30);
        editComboBox.setPrototypeDisplayValue("                                                          ");

        layout.putConstraint(SpringLayout.WEST, lAdres, 60,
                SpringLayout.WEST, panel2);
        layout.putConstraint(SpringLayout.NORTH, lAdres, 5,
                SpringLayout.NORTH, panel2);
        layout.putConstraint(SpringLayout.NORTH, adres, 5,
                SpringLayout.NORTH, panel2);
        layout.putConstraint(SpringLayout.WEST, adres, 20,
                SpringLayout.EAST, lAdres);
        layout.putConstraint(SpringLayout.NORTH, lTable, 5,
                SpringLayout.NORTH, panel2);
        layout.putConstraint(SpringLayout.WEST, lTable, 100,
                SpringLayout.EAST, adres);
        layout.putConstraint(SpringLayout.NORTH, editComboBox, 5,
                SpringLayout.NORTH, panel2);
        layout.putConstraint(SpringLayout.WEST, editComboBox, 20,
                SpringLayout.EAST, lTable);
        layout.putConstraint(SpringLayout.NORTH, button, 5,
                SpringLayout.NORTH, panel2);
        layout.putConstraint(SpringLayout.WEST, button, 60,
                SpringLayout.EAST, editComboBox);


        panel3.setBounds(0, 61, 1200, 770);
        add(panel3);
        tableScrollPane.setPreferredSize(new Dimension(1200, 770));

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == button) {
            try {
                lError.setText("");
                conn = DriverManager.getConnection(
                        adres.getText(),
                        "root", "password");

                try {
                    lError.setText("");
                    tableModel = new MyTableModel(conn, (String) editComboBox.getSelectedItem());
                    table.setModel(tableModel);
                    panel3.add(tableScrollPane);
                    setVisible(true);
                    try {
                        conn.close();
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                } catch (SQLException ex) {
                    lError.setText("Подключение провалено. Неизвестная таблица.");
                }

            } catch (SQLException ex) {
                lError.setText("Подключение провалено. Неизвестная база данных.");
            }
        }
    }
}
