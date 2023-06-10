package org.example;

import javax.swing.table.AbstractTableModel;
import java.sql.*;
import java.util.ArrayList;

public class MyTableModel extends AbstractTableModel {
    private Connection connection;
    private Statement statement;
    private String tableN;
    private String[] columnHead;
    private ArrayList<String[]> dataArrayList = new ArrayList<>();

    public MyTableModel(Connection conn, String tableN) throws SQLException {
        this.connection = conn;
        this.tableN = tableN;
        addDate();
    }

    public void addDate() throws SQLException {
        try {
            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select * from " + tableN);
            ResultSetMetaData md = resultSet.getMetaData();
            columnHead = new String[md.getColumnCount()];
            while (resultSet.next()) {
                String[] row = new String[md.getColumnCount()];
                for (int x = 1; x < columnHead.length + 1; x++) {
                    row[x - 1] = resultSet.getString(x);
                    columnHead[x - 1] = md.getColumnName(x);
                }
                dataArrayList.add(row);
            }
        } catch (SQLException e) {
            System.out.println("Exception e: " + e.getMessage());
            throw e;
        }
    }


    @Override
    public int getRowCount() {
        return dataArrayList.size();
    }

    @Override
    public int getColumnCount() {
        return columnHead.length;
    }

    @Override
    public String getColumnName(int columnIndex) {
        switch (columnIndex) {
            case 0:
                return columnHead[0];
            case 1:
                return columnHead[1];
            case 2:
                return columnHead[2];
            case 3:
                return columnHead[3];
            case 4:
                return columnHead[4];
            case 5:
                return columnHead[5];
            case 6:
                return columnHead[6];
            case 7:
                return columnHead[7];
        }
        return null;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        String[] rows = dataArrayList.get(rowIndex);
        return rows[columnIndex];
    }
}
