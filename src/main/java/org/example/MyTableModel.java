package org.example;

import javax.swing.table.AbstractTableModel;
import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class MyTableModel extends AbstractTableModel {
    private Connection connection;
    private String tableName;
    private String[] columnHead;
    private List<String[]> dataArrayList = new LinkedList<>();

    public MyTableModel(Connection conn, String tableN) throws SQLException {
        this.connection = conn;
        this.tableName = tableN;
        addDate();
    }

    public void addDate() throws SQLException {
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select * from " + tableName);
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
