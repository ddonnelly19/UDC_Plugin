package com.hpe.ucmdb.udc;

import com.hp.ucmdb.api.discovery.types.DiscoveryResData;
import com.hp.ucmdb.api.discovery.types.DiscoveryResType;
import com.hpe.ucmdb.udc.model.PackageTable;
import com.hpe.ucmdb.udc.viewer.CheckTableModle;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.table.JBTable;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import com.intellij.util.ui.JBUI;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.Serializable;
import java.util.*;
import java.util.List;

public class PackageList extends JDialog {
    private final Set<PackageTable> selected;
    private JPanel contentPane;
    private JButton buttonDownload;
    private JButton buttonCancel;
    private JTable table;
    private JTextField searchField;
    private JButton selectAllButton;
    private JButton unselectAllButton;
    private Project project;
    private String[] chosenPackages;

    public PackageList(final Map<String, Map<DiscoveryResType, List<DiscoveryResData>>> resGroup) {
        this.$$$setupUI$$$();
        this.selected = new HashSet<>();
        this.setLocationRelativeTo(null);
        this.setContentPane(this.contentPane);
        this.setTitle("Package List");
        this.setModal(true);
        this.setBounds(500, 220, 600, 500);
        final String[] list = resGroup.keySet().toArray(new String[0]);
        List<String> l;
        l = Arrays.asList(list);
        l.sort(Comparator.naturalOrder());
        final String[] t = l.toArray(new String[l.size()]);
        final List<PackageTable> data = this.getData(t);
        final Vector<String> headerNames = new Vector<>();
        headerNames.add("Package Name");
        headerNames.add("");
        if (this.selected.size() != 0) {
            for (final PackageTable pt : this.selected) {
                for (final PackageTable packageName : data) {
                    if (pt.getPackageName().equals(packageName.getPackageName())) {
                        packageName.setSelected();
                    }
                }
            }
        }
        final Vector<Vector<java.io.Serializable>> v = new Vector<>();
        for (PackageTable aData : data) {
            final Vector<Serializable> v2 = new Vector<>();
            v2.add(aData.getPackageName());
            v2.add(aData.isSelected());
            v.add(v2);
        }
        final CheckTableModle tableModel = new CheckTableModle(v, headerNames);
        this.table.setModel(tableModel);
        final TableColumn firsetColumn = this.table.getColumnModel().getColumn(1);
        firsetColumn.setPreferredWidth(100);
        firsetColumn.setMaxWidth(100);
        firsetColumn.setMinWidth(100);
        this.table.setShowGrid(false);
        this.table.setRowHeight(22);
        this.table.setRowMargin(2);
        this.getRootPane().setDefaultButton(this.buttonDownload);
        this.table.getModel().addTableModelListener(e -> {
            if (e.getLastRow() != -1 && e.getColumn() != -1) {
                if (PackageList.this.table.getValueAt(e.getLastRow(), e.getColumn()) != null) {
                    final String selectedPackageName = PackageList.this.table.getValueAt(e.getLastRow(), e.getColumn() - 1).toString();
                    if (!selectedPackageName.equals("Package Name")) {
                        final PackageTable s = new PackageTable(selectedPackageName, true);
                        PackageList.this.selected.add(s);
                    }
                } else {
                    final String selectedPackageName = PackageList.this.table.getValueAt(e.getLastRow(), e.getColumn() - 1).toString();
                    PackageTable remove = null;
                    for (final PackageTable pt : PackageList.this.selected) {
                        if (pt.getPackageName().equals(selectedPackageName)) {
                            remove = pt;
                        }
                    }
                    PackageList.this.selected.remove(remove);
                }
            }
        });
        this.searchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(final DocumentEvent e) {
                final String query = PackageList.this.searchField.getText();
                final List<String> queryResults = new ArrayList<>();
                for (final String q : t) {
                    if (q.toLowerCase().contains(query.toLowerCase())) {
                        queryResults.add(q);
                    }
                }
                queryResults.sort(Comparator.naturalOrder());
                final String[] t1 = queryResults.toArray(new String[queryResults.size()]);
                final Vector<PackageTable> data = this.getData(t1);
                PackageList.this.initTable(data, tableModel);
                final TableColumn firsetColumn = PackageList.this.table.getColumnModel().getColumn(1);
                firsetColumn.setPreferredWidth(100);
                firsetColumn.setMaxWidth(100);
                firsetColumn.setMinWidth(100);
                PackageList.this.table.setShowGrid(false);
                PackageList.this.getRootPane().setDefaultButton(PackageList.this.buttonDownload);
                PackageList.this.table.getModel().addTableModelListener(e13 -> {
                    if (e13.getLastRow() != -1 && e13.getColumn() != -1) {
                        if (PackageList.this.table.getValueAt(e13.getLastRow(), e13.getColumn()) != null) {
                            final String selectedPackageName = PackageList.this.table.getValueAt(e13.getLastRow(), e13.getColumn() - 1).toString();
                            if (!selectedPackageName.equals("Package Name")) {
                                final PackageTable s = new PackageTable(selectedPackageName, true);
                                PackageList.this.selected.add(s);
                            }
                        } else {
                            final String selectedPackageName = PackageList.this.table.getValueAt(e13.getLastRow(), e13.getColumn() - 1).toString();
                            PackageTable remove = null;
                            for (final PackageTable pt : PackageList.this.selected) {
                                if (pt.getPackageName().equals(selectedPackageName)) {
                                    remove = pt;
                                }
                            }
                            PackageList.this.selected.remove(remove);
                        }
                    }
                });
            }

            Vector<PackageTable> getData(final String[] packageNames) {
                final Vector<PackageTable> data = new Vector<>();
                for (String packageName : packageNames) {
                    if (packageName.endsWith(".zip")) {
                        packageName = packageName.substring(0, packageName.length() - 4);
                    }
                    final PackageTable pt = new PackageTable(packageName, false);
                    data.add(pt);
                }
                return data;
            }

            @Override
            public void removeUpdate(final DocumentEvent e) {
                final String query = PackageList.this.searchField.getText();
                final List<String> queryResults = new ArrayList<>();
                for (final String q : t) {
                    if (q.toLowerCase().contains(query.toLowerCase())) {
                        queryResults.add(q);
                    }
                }
                queryResults.sort(Comparator.naturalOrder());
                final String[] t1 = queryResults.toArray(new String[queryResults.size()]);
                final Vector<PackageTable> data = this.getData(t1);
                PackageList.this.initTable(data, tableModel);
                final TableColumn firsetColumn = PackageList.this.table.getColumnModel().getColumn(1);
                firsetColumn.setPreferredWidth(100);
                firsetColumn.setMaxWidth(100);
                firsetColumn.setMinWidth(100);
                PackageList.this.table.setShowGrid(false);
                PackageList.this.getRootPane().setDefaultButton(PackageList.this.buttonDownload);
                PackageList.this.table.getModel().addTableModelListener(e12 -> {
                    if (e12.getLastRow() != -1 && e12.getColumn() != -1) {
                        if (PackageList.this.table.getValueAt(e12.getLastRow(), e12.getColumn()) != null) {
                            final String selectedPackageName = PackageList.this.table.getValueAt(e12.getLastRow(), e12.getColumn() - 1).toString();
                            if (!selectedPackageName.equals("Package Name")) {
                                final PackageTable s = new PackageTable(selectedPackageName, true);
                                PackageList.this.selected.add(s);
                            }
                        } else {
                            final String selectedPackageName = PackageList.this.table.getValueAt(e12.getLastRow(), e12.getColumn() - 1).toString();
                            PackageTable remove = null;
                            for (final PackageTable pt : PackageList.this.selected) {
                                if (pt.getPackageName().equals(selectedPackageName)) {
                                    remove = pt;
                                }
                            }
                            PackageList.this.selected.remove(remove);
                        }
                    }
                });
            }

            @Override
            public void changedUpdate(final DocumentEvent e) {
                final String query = PackageList.this.searchField.getText();
                final List<String> queryResults = new ArrayList<>();
                for (final String q : t) {
                    if (q.toLowerCase().contains(query.toLowerCase())) {
                        queryResults.add(q);
                    }
                }
                queryResults.sort(Comparator.naturalOrder());
                final String[] t1 = queryResults.toArray(new String[queryResults.size()]);
                final Vector<PackageTable> data = this.getData(t1);
                PackageList.this.initTable(data, tableModel);
                final TableColumn firsetColumn = PackageList.this.table.getColumnModel().getColumn(1);
                firsetColumn.setPreferredWidth(100);
                firsetColumn.setMaxWidth(100);
                firsetColumn.setMinWidth(100);
                PackageList.this.table.setShowGrid(false);
                PackageList.this.getRootPane().setDefaultButton(PackageList.this.buttonDownload);
                PackageList.this.table.getModel().addTableModelListener(e1 -> {
                    if (e1.getLastRow() != -1 && e1.getColumn() != -1) {
                        if (PackageList.this.table.getValueAt(e1.getLastRow(), e1.getColumn()) != null) {
                            final String selectedPackageName = PackageList.this.table.getValueAt(e1.getLastRow(), e1.getColumn() - 1).toString();
                            if (!selectedPackageName.equals("Package Name")) {
                                final PackageTable s = new PackageTable(selectedPackageName, true);
                                PackageList.this.selected.add(s);
                            }
                        } else {
                            final String selectedPackageName = PackageList.this.table.getValueAt(e1.getLastRow(), e1.getColumn() - 1).toString();
                            PackageTable remove = null;
                            for (final PackageTable pt : PackageList.this.selected) {
                                if (pt.getPackageName().equals(selectedPackageName)) {
                                    remove = pt;
                                }
                            }
                            PackageList.this.selected.remove(remove);
                        }
                    }
                });
            }
        });
        this.buttonDownload.addActionListener(e -> {
            final Set<String> packageList = new HashSet<>();
            for (final PackageTable pt : PackageList.this.selected) {
                if (!packageList.contains(pt.getPackageName())) {
                    packageList.add(pt.getPackageName());
                }
            }
            if (packageList.isEmpty()) {
                Messages.showMessageDialog(PackageList.this.project, "No package has been chosen,please choose a package", "Information", Messages.getInformationIcon());
                PackageList.this.chosenPackages = packageList.toArray(new String[0]);
            } else {
                PackageList.this.chosenPackages = packageList.toArray(new String[0]);
                PackageList.this.dispose();
            }
        });
        this.selectAllButton.addActionListener(e -> {
            final int rowC = tableModel.getRowCount();
            final Iterator<PackageTable> it1 = PackageList.this.selected.iterator();
            final Set<String> selectedS = new HashSet<>();
            while (it1.hasNext()) {
                selectedS.add(it1.next().getPackageName());
            }
            if (rowC > 0) {
                for (int i = 0; i < rowC; ++i) {
                    if (!selectedS.contains(tableModel.getValueAt(i, 0).toString())) {
                        tableModel.setValueAt(true, i, 1);
                    }
                }
            }
        });
        this.unselectAllButton.addActionListener(e -> {
            final int rowC = tableModel.getRowCount();
            if (rowC > 0) {
                for (int i = 0; i < rowC; ++i) {
                    tableModel.setValueAt(false, i, 1);
                }
            }
        });
        this.buttonCancel.addActionListener(e -> {
            try {
                PackageList.this.quit();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        });
        this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(final WindowEvent e) {
                PackageList.this.onCancel();
            }
        });
        this.contentPane.registerKeyboardAction(e -> PackageList.this.onCancel(), KeyStroke.getKeyStroke(27, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

// --Commented out by Inspection START (3/1/2017 3:04 PM):
//    private void onOK() {
//        this.dispose();
//    }
// --Commented out by Inspection STOP (3/1/2017 3:04 PM)

    private void onCancel() {
        this.dispose();
    }

    private void initTable(final List<PackageTable> data, final CheckTableModle tableModel) {
        if (this.selected.size() != 0) {
            for (final PackageTable pt : this.selected) {
                for (final PackageTable packageName : data) {
                    if (pt.getPackageName().equals(packageName.getPackageName())) {
                        packageName.setSelected();
                    }
                }
            }
        }
        for (int i = tableModel.getRowCount() - 1; i >= 0; --i) {
            tableModel.removeRow(i);
        }
        for (PackageTable aData : data) {
            final Vector<Serializable> v1 = new Vector<>();
            v1.add(aData.getPackageName());
            v1.add(aData.isSelected());
            tableModel.addRow(v1);
        }
    }

    private List<PackageTable> getData(final String[] packageNames) {
        final List<PackageTable> data = new ArrayList<>();
        for (String packageName : packageNames) {
            if (packageName.endsWith(".zip")) {
                packageName = packageName.substring(0, packageName.length() - 4);
            }
            final PackageTable pt = new PackageTable(packageName, false);
            data.add(pt);
        }
        return data;
    }

    public String[] getChosenPackages() {
        return this.chosenPackages;
    }

    private void quit() {
        this.dispatchEvent(new WindowEvent(this, 201));
    }

    private void $$$setupUI$$$() {
        final JPanel contentPane = new JPanel();
        (this.contentPane = contentPane).setLayout(new GridLayoutManager(3, 1, JBUI.insets(10), -1, -1, false, false));
        final JPanel panel = new JPanel();
        panel.setLayout(new GridLayoutManager(1, 2, JBUI.emptyInsets(), -1, -1, false, false));
        contentPane.add(panel, new GridConstraints(2, 0, 1, 1, 0, 3, 3, 1, null, null, null));
        panel.add(new Spacer(), new GridConstraints(0, 0, 1, 1, 0, 1, 6, 1, null, null, null));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(1, 4, JBUI.emptyInsets(), -1, -1, false, false));
        panel.add(panel2, new GridConstraints(0, 1, 1, 1, 0, 3, 3, 3, null, null, null));
        final JButton buttonDownload = new JButton();
        (this.buttonDownload = buttonDownload).setText("Download");
        panel2.add(buttonDownload, new GridConstraints(0, 2, 1, 1, 0, 1, 3, 0, null, null, null));
        final JButton buttonCancel = new JButton();
        (this.buttonCancel = buttonCancel).setText("Cancel");
        panel2.add(buttonCancel, new GridConstraints(0, 3, 1, 1, 0, 1, 3, 0, null, null, null));
        final JButton selectAllButton = new JButton();
        (this.selectAllButton = selectAllButton).setText("Select All");
        panel2.add(selectAllButton, new GridConstraints(0, 0, 1, 1, 0, 1, 3, 0, null, null, null));
        final JButton unselectAllButton = new JButton();
        (this.unselectAllButton = unselectAllButton).setText("Unselect All");
        panel2.add(unselectAllButton, new GridConstraints(0, 1, 1, 1, 0, 1, 3, 0, null, null, null));
        final JPanel jp1 = new JPanel();
        JPanel JP1;
        jp1.setLayout(new GridLayoutManager(1, 1, JBUI.emptyInsets(), -1, -1, false, false));
        contentPane.add(jp1, new GridConstraints(1, 0, 1, 1, 0, 3, 3, 3, null, null, null));
        final JBScrollPane jScroll = new JBScrollPane();
        JBScrollPane JScroll;
        jScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        jp1.add(jScroll, new GridConstraints(0, 0, 1, 1, 0, 3, 7, 7, null, null, null));
        jScroll.setViewportView(this.table = new JBTable());
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(1, 1, JBUI.emptyInsets(), -1, -1, false, false));
        contentPane.add(panel3, new GridConstraints(0, 0, 1, 1, 0, 3, 3, 3, null, null, null));
        panel3.add(this.searchField = new JTextField(), new GridConstraints(0, 0, 1, 1, 8, 1, 6, 0, null, new Dimension(150, -1), null));
    }
}