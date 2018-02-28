package com.hpe.ucmdb.udc.viewer;

import javax.swing.*;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

class CheckHeaderCellRenderer implements TableCellRenderer {
    private final CheckTableModle tableModel;
    private final JTableHeader tableHeader;
    private final JCheckBox selectBox;

    public CheckHeaderCellRenderer(final JTable table) {
        this.tableModel = (CheckTableModle) table.getModel();
        this.tableHeader = table.getTableHeader();
        (this.selectBox = new JCheckBox(this.tableModel.getColumnName(1))).setSelected(false);
        this.tableHeader.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(final MouseEvent e) {
                if (e.getClickCount() > 0) {
                    final int selectColumn = CheckHeaderCellRenderer.this.tableHeader.columnAtPoint(e.getPoint());
                    if (selectColumn == 1) {
                        final boolean value = !CheckHeaderCellRenderer.this.selectBox.isSelected();
                        CheckHeaderCellRenderer.this.selectBox.setSelected(value);
                        CheckHeaderCellRenderer.this.tableModel.selectAllOrNull(value);
                        CheckHeaderCellRenderer.this.tableHeader.repaint();
                    }
                }
            }
        });
    }

    @Override
    public Component getTableCellRendererComponent(final JTable table, final Object value, final boolean isSelected, final boolean hasFocus, final int row, final int column) {
        final String valueStr = (String) value;
        final JLabel label = new JLabel(valueStr);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        this.selectBox.setHorizontalAlignment(SwingConstants.CENTER);
        this.selectBox.setBorderPainted(true);
        final JComponent component = (column == 1) ? this.selectBox : label;
        component.setForeground(this.tableHeader.getForeground());
        component.setBackground(this.tableHeader.getBackground());
        component.setFont(this.tableHeader.getFont());
        component.setBorder(UIManager.getBorder("TableHeader.cellBorder"));
        return component;
    }
}