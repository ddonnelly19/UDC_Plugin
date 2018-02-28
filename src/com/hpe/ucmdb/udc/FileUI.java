package com.hpe.ucmdb.udc;

import com.intellij.lang.Language;
import com.intellij.openapi.project.Project;
import com.intellij.ui.EditorTextField;
import com.intellij.ui.LanguageTextField;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import com.intellij.util.ui.JBUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class FileUI extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JPanel JP1;
    // --Commented out by Inspection (3/1/2017 3:04 PM):public LanguageTextField editorPane1;

    public FileUI(final Language xml, final Project project, final String text) {
        this.$$$setupUI$$$();
        this.setContentPane(this.contentPane);
        this.setModal(true);
        final EditorTextField etf = new LanguageTextField(xml, project, text, false);
        final JBScrollPane scroll = new JBScrollPane(etf.getComponent(), ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        final BorderLayout mgr = new BorderLayout();
        this.JP1.setLayout(mgr);
        this.JP1.add(scroll, "Center");
        this.getRootPane().setDefaultButton(this.buttonOK);
        this.setBounds(0, 0, 800, 600);
        this.setLocationRelativeTo(null);
        this.buttonOK.addActionListener(e -> {
            try {
                FileUI.this.quit();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        });
        this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(final WindowEvent e) {
                FileUI.this.onCancel();
            }
        });
        this.contentPane.registerKeyboardAction(e -> FileUI.this.onCancel(), KeyStroke.getKeyStroke(27, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    public static void main(final String[] args) {
        final FileUI dialog = new FileUI(null, null, null);
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }

// --Commented out by Inspection START (3/1/2017 3:04 PM):
//    private void onOK() {
//        this.dispose();
//    }
// --Commented out by Inspection STOP (3/1/2017 3:04 PM)

    private void quit() {
        this.dispatchEvent(new WindowEvent(this, 201));
    }

    private void onCancel() {
        this.dispose();
    }

    private void $$$setupUI$$$() {
        final JPanel contentPane = new JPanel();
        (this.contentPane = contentPane).setLayout(new GridLayoutManager(2, 1, JBUI.insets(10), -1, -1, false, false));
        final JPanel panel = new JPanel();
        panel.setLayout(new GridLayoutManager(1, 2, JBUI.emptyInsets(), -1, -1, false, false));
        contentPane.add(panel, new GridConstraints(1, 0, 1, 1, 0, 3, 3, 1, null, null, null));
        panel.add(new Spacer(), new GridConstraints(0, 0, 1, 1, 0, 1, 6, 1, null, null, null));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(1, 1, JBUI.emptyInsets(), -1, -1, false, false));
        panel.add(panel2, new GridConstraints(0, 1, 1, 1, 0, 3, 3, 3, null, null, null));
        final JButton buttonOK = new JButton();
        (this.buttonOK = buttonOK).setText("OK");
        panel2.add(buttonOK, new GridConstraints(0, 0, 1, 1, 0, 1, 3, 0, null, null, null));
        final JPanel jp1 = new JPanel();
        (this.JP1 = jp1).setLayout(new GridLayoutManager(1, 1, JBUI.emptyInsets(), -1, -1, false, false));
        contentPane.add(jp1, new GridConstraints(0, 0, 1, 1, 0, 3, 3, 3, null, null, null));
    }
}