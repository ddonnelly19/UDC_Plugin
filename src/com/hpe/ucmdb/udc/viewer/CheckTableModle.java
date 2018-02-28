package com.hpe.ucmdb.udc.viewer;

import javax.swing.table.DefaultTableModel;
import java.util.Vector;

public class CheckTableModle extends DefaultTableModel {
    public CheckTableModle(final Vector<Vector<java.io.Serializable>> data, final Vector<String> columnNames) {
        super(data, columnNames);
    }

    @Override
    public Class getColumnClass(final int c) {
        return this.getValueAt(0, c).getClass();
    }

    public void selectAllOrNull(final boolean value) {
        for (int i = 0; i < this.getRowCount(); ++i) {
            this.setValueAt(value, i, 1);
        }
    }

    @Override
    public boolean isCellEditable(final int row, final int column) {
        return column != 0;
    }
}