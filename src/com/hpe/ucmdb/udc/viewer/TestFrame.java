package com.hpe.ucmdb.udc.viewer;

import com.hp.ucmdb.api.discovery.types.DiscoveryResData;
import com.hp.ucmdb.api.discovery.types.DiscoveryResType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.table.JBTable;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.*;
import java.util.List;

class TestFrame extends JDialog {
    private final JTable table;
    private Project project;
    private String[] chosenPackages;

    public TestFrame(final Map<String, Map<DiscoveryResType, List<DiscoveryResData>>> resGroup) {
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        this.setTitle("Package List");
        this.setPreferredSize(new Dimension(400, 300));
        this.setModal(true);
        JPanel contentPane;
        (contentPane = new JPanel()).setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(0, 0));
        this.setContentPane(contentPane);
        this.table = new JBTable();
        final JBScrollPane scrollPane = new JBScrollPane(this.table, 22, 30);
        contentPane.add(scrollPane, "Center");
        final String[] list = resGroup.keySet().toArray(new String[0]);
        List<String> l = Arrays.asList(list);
        l.sort(Comparator.naturalOrder());
        final String[] t = l.toArray(new String[l.size()]);
        final Vector<Vector<java.io.Serializable>> data = this.getData(t);
        this.initTable(data);
        JButton downloadButton = new JButton("Download");
        contentPane.add(downloadButton, "Last");
        downloadButton.addActionListener(e -> {
            final int tableCount = TestFrame.this.table.getRowCount();
            final List<String> packageList = new ArrayList<>();
            for (int i = 0; i < tableCount; ++i) {
                if (TestFrame.this.table.getValueAt(i, 0) != null) {
                    packageList.add((String) TestFrame.this.table.getValueAt(i, 1));
                }
            }
            if (packageList.isEmpty()) {
                Messages.showMessageDialog(TestFrame.this.project, "No package has been chosen,please choose a package", "Information", Messages.getInformationIcon());
            } else {
                TestFrame.this.chosenPackages = packageList.toArray(new String[0]);
                Messages.showMessageDialog(TestFrame.this.project, "The package" + packageList.toString() + " is being downloaded. Please wait for a moment", "Information", Messages.getInformationIcon());
            }
            TestFrame.this.dispose();
        });
        this.pack();
    }

    public static void sort(final String[] list) {
        final int len = list.length;
        String temp;
        for (int i = 0; i < len - 1; ++i) {
            for (int j = 0; j < len - 1 - i; ++j) {
                if (list[j].compareTo(list[j + 1]) > 0) {
                    temp = list[j];
                    list[j] = list[j + 1];
                    list[j + 1] = temp;
                }
            }
        }
    }

    public String[] getChosenPackages() {
        return this.chosenPackages;
    }

    public int tableCount() {
        return this.table.getRowCount();
    }

    public List<String> chosenPackages(final int tableCount) {
        final List<String> packageList = new ArrayList<>();
        for (int i = 0; i < tableCount; ++i) {
            if (this.table.getValueAt(i, 0).equals(true)) {
                packageList.add((String) this.table.getValueAt(i, 1));
            }
        }
        return packageList;
    }

    private void initTable(final Vector<Vector<java.io.Serializable>> data) {
        final Vector<String> headerNames = new Vector<>();
        headerNames.add("Select All");
        headerNames.add("Package Name");
        final CheckTableModle tableModel = new CheckTableModle(data, headerNames);
        this.table.setModel(tableModel);
        this.table.getTableHeader().setDefaultRenderer(new CheckHeaderCellRenderer(this.table));
    }

    private Vector<Vector<java.io.Serializable>> getData(final String[] packageNames) {
        final Vector<Vector<java.io.Serializable>> data = new Vector<>();
        for (String packageName : packageNames) {
            if (packageName.endsWith(".zip")) {
                packageName = packageName.substring(0, packageName.length() - 4);
            }
            final Vector<java.io.Serializable> rowVector1 = new Vector<>();
            rowVector1.add(false);
            rowVector1.add(packageName);
            data.add(rowVector1);
        }
        return data;
    }

    public int compare(final Object o1, final Object o2) {
        final String str1 = o1.toString().toUpperCase();
        final String str2 = o2.toString().toUpperCase();
        return str1.compareTo(str2);
    }
}