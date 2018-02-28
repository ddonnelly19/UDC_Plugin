package com.hpe.ucmdb.udc;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.ui.MessageType;
import com.intellij.openapi.ui.popup.util.PopupUtil;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.util.ui.JBUI;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;

public class ServerConfigForm extends JPanel {
    private final String id;
    private JTextField serverTextField;
    private JTextField usernameTextField;
    private JPasswordField passwordField;
    private JPanel rootComponent;
    private JTextField portField;
    private JTextField serverLabelTextField;
    private JRadioButton HTTPRadioButton;
    private JRadioButton HTTPSRadioButton;
    private boolean passwordChanged;

    public ServerConfigForm() {
        this.$$$setupUI$$$();
        this.id = String.valueOf(Math.random());
        this.passwordField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(final DocumentEvent e) {
                ServerConfigForm.this.passwordChanged = true;
            }

            @Override
            public void removeUpdate(final DocumentEvent e) {
                ServerConfigForm.this.passwordChanged = true;
            }

            @Override
            public void changedUpdate(final DocumentEvent e) {
                ServerConfigForm.this.passwordChanged = true;
            }
        });
        this.setLayout(new BorderLayout());
        this.add(this.getRootComponent(), "North");
    }

    public String getLabel() {
        final String text = this.serverLabelTextField.getText();
        if (text != null && !text.trim().isEmpty()) {
            return text;
        }
        return "New Server";
    }

    public String getId() {
        return this.id;
    }

    private JComponent getRootComponent() {
        return this.rootComponent;
    }

    public void setData(final UDCSettings.ServerSetting config) throws Exception {
        this.serverTextField.setText(config.getServer());
        this.serverLabelTextField.setText(config.getLabel());
        this.portField.setText(String.valueOf(config.getPort()));
        this.usernameTextField.setText(config.getUsername());
        this.passwordField.setText(DesUtil.decrypt(config.getPassword()));
        if (config.getProtocol().equals("http")) {
            this.HTTPRadioButton.setSelected(true);
            this.HTTPSRadioButton.setSelected(false);
        } else {
            this.HTTPSRadioButton.setSelected(true);
            this.HTTPRadioButton.setSelected(false);
        }
    }

    public boolean getData(final UDCSettings.ServerSetting config) throws Exception {
        final String server = this.serverTextField.getText();
        final String label = this.serverLabelTextField.getText();
        final String port = this.portField.getText();
        final String userName = this.usernameTextField.getText();
        @SuppressWarnings("deprecation") final String password = this.passwordField.getText();
        if (label == null || label.trim().length() == 0) {
            this.setFocus(this.serverLabelTextField);
            PopupUtil.showBalloonForActiveComponent("Label must be supplied.", MessageType.ERROR);
            return false;
        }
        if (server == null || server.trim().length() == 0) {
            this.setFocus(this.serverTextField);
            PopupUtil.showBalloonForActiveComponent("Server must be supplied.", MessageType.ERROR);
            return false;
        }
        if (port == null || port.trim().length() == 0) {
            this.setFocus(this.portField);
            PopupUtil.showBalloonForActiveComponent("Port must be supplied.", MessageType.ERROR);
            return false;
        }
        if (userName == null || userName.trim().length() == 0) {
            this.setFocus(this.usernameTextField);
            PopupUtil.showBalloonForActiveComponent("User name must be supplied.", MessageType.ERROR);
            return false;
        }
        if (password == null || password.trim().length() == 0) {
            this.setFocus(this.passwordField);
            PopupUtil.showBalloonForActiveComponent("Password must be supplied.", MessageType.ERROR);
            return false;
        }
        if (this.HTTPRadioButton.isSelected()) {
            config.setProtocol("http");
        } else {
            config.setProtocol("https");
        }
        config.setLabel(this.serverLabelTextField.getText().trim());
        config.setServer(server.trim());
        config.setPort(Integer.parseInt(this.portField.getText().trim()));
        config.setUsername(this.usernameTextField.getText().trim());
        config.setPassword(DesUtil.encrypt(String.valueOf(this.passwordField.getPassword())));
        return true;
    }

    public void getDataForModify(final UDCSettings.ServerSetting config) throws Exception {
        final String server = this.serverTextField.getText();
        config.setServer(server);
        config.setLabel(this.serverLabelTextField.getText());
        try {
            config.setPort(Integer.parseInt(this.portField.getText()));
        } catch (NumberFormatException e) {
            config.setPort(0);
        }
        config.setUsername(this.usernameTextField.getText());
        config.setPassword(DesUtil.encrypt(String.valueOf(this.passwordField.getPassword())));
        if (this.HTTPRadioButton.isSelected()) {
            config.setProtocol("http");
        } else {
            config.setProtocol("https");
        }
    }

    private void setFocus(final JComponent component) {
        ApplicationManager.getApplication().invokeLater(component::requestFocus);
    }

// --Commented out by Inspection START (3/1/2017 3:04 PM):
//    public boolean isModified(final ServerConfig config) {
//        Label_0039: {
//            if (this.serverTextField.getText() != null) {
//                if (this.serverTextField.getText().equals(config.getServer())) {
//                    break Label_0039;
//                }
//            }
//            else if (config.getServer() == null) {
//                break Label_0039;
//            }
//            return true;
//        }
//        Label_0081: {
//            if (this.portField.getText() != null) {
//                if (this.portField.getText().equals(String.valueOf(config.getPort()))) {
//                    break Label_0081;
//                }
//            }
//            else if (config.getPort() == 0) {
//                break Label_0081;
//            }
//            return true;
//        }
//        if (this.usernameTextField.getText() != null) {
//            if (this.usernameTextField.getText().equals(config.getUsername())) {
//                return this.passwordChanged;
//            }
//        }
//        else if (config.getUsername() == null) {
//            return this.passwordChanged;
//        }
//        return true;
//    }
// --Commented out by Inspection STOP (3/1/2017 3:04 PM)

    private void $$$setupUI$$$() {
        final JPanel rootComponent = new JPanel();
        (this.rootComponent = rootComponent).setLayout(new GridLayoutManager(1, 1, JBUI.emptyInsets(), -1, -1, true, false));
        rootComponent.setMinimumSize(new Dimension(-1, -1));
        final JPanel panel = new JPanel();
        panel.setLayout(new GridLayoutManager(1, 1, JBUI.emptyInsets(), -1, -1, false, false));
        rootComponent.add(panel, new GridConstraints(0, 0, 1, 1, 0, 1, 6, 3, null, null, null));
        panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), null, 0, 0, null, null));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(6, 4, JBUI.insetsLeft(10), -1, -1, false, false));
        panel.add(panel2, new GridConstraints(0, 0, 1, 1, 0, 3, 7, 3, null, null, null));
        final JLabel label = new JLabel();
        label.setToolTipText("The hostname or IP address of the outgoing mailserver");
        label.setText("Server:");
        panel2.add(label, new GridConstraints(1, 0, 1, 1, 8, 0, 0, 0, null, null, null));
        final JTextField labelFor = new JTextField();
        (this.serverTextField = labelFor).setText("");
        labelFor.setToolTipText("Ucmdb server address");
        panel2.add(labelFor, new GridConstraints(1, 1, 1, 3, 8, 1, 6, 0, null, new Dimension(150, -1), null));
        final JTextField textField = new JTextField();
        (this.usernameTextField = textField).setText("admin");
        panel2.add(textField, new GridConstraints(4, 1, 1, 3, 8, 1, 6, 0, null, new Dimension(382, 28), null));
        final JLabel label2 = new JLabel();
        label2.setToolTipText("The username to log into the mailserver with, if required");
        label2.setText("Username:");
        panel2.add(label2, new GridConstraints(4, 0, 1, 1, 8, 0, 0, 0, null, new Dimension(86, 14), null));
        final JLabel label3 = new JLabel();
        label3.setToolTipText("The password to log in to the mailserver with, if required");
        label3.setText("Password:");
        panel2.add(label3, new GridConstraints(5, 0, 1, 1, 8, 0, 0, 0, null, new Dimension(86, 14), null));
        final JPasswordField passwordField = new JPasswordField();
        (this.passwordField = passwordField).setText("");
        panel2.add(passwordField, new GridConstraints(5, 1, 1, 3, 8, 1, 6, 0, null, new Dimension(382, 28), null));
        final JTextField portField = new JTextField();
        (this.portField = portField).setText("8080");
        portField.setToolTipText("Ucmdb server port");
        panel2.add(portField, new GridConstraints(3, 1, 1, 3, 8, 1, 6, 0, null, new Dimension(382, 28), null));
        final JLabel label4 = new JLabel();
        label4.setToolTipText("The hostname or IP address of the outgoing mailserver");
        label4.setText("Port:");
        panel2.add(label4, new GridConstraints(3, 0, 1, 1, 8, 0, 0, 0, null, new Dimension(86, 14), null));
        final JLabel label5 = new JLabel();
        label5.setText("Label:");
        panel2.add(label5, new GridConstraints(0, 0, 1, 1, 8, 0, 0, 0, null, null, null));
        final JTextField serverLabelTextField = new JTextField();
        (this.serverLabelTextField = serverLabelTextField).setText("");
        panel2.add(serverLabelTextField, new GridConstraints(0, 1, 1, 3, 8, 1, 6, 0, null, new Dimension(150, -1), null));
        final JLabel label6 = new JLabel();
        label6.setText("Protocol:");
        panel2.add(label6, new GridConstraints(2, 0, 1, 1, 8, 0, 0, 0, null, null, null));
        final JRadioButton httpRadioButton = new JRadioButton();
        (this.HTTPRadioButton = httpRadioButton).setText("HTTP");
        httpRadioButton.setEnabled(true);
        httpRadioButton.setSelected(true);
        panel2.add(httpRadioButton, new GridConstraints(2, 1, 1, 1, 8, 0, 3, 0, null, null, null));
        final JRadioButton httpsRadioButton = new JRadioButton();
        (this.HTTPSRadioButton = httpsRadioButton).setText("HTTPS");
        panel2.add(httpsRadioButton, new GridConstraints(2, 2, 1, 1, 8, 0, 3, 0, null, null, null));
        label.setLabelFor(labelFor);
        label2.setLabelFor(textField);
        label3.setLabelFor(passwordField);
        label4.setLabelFor(labelFor);
        final ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(httpRadioButton);
        buttonGroup.add(httpsRadioButton);
    }
}