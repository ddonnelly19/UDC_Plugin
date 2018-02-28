package com.hpe.ucmdb.udc;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.ui.components.JBList;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import com.intellij.util.ui.JBUI;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class UDCConfigUI implements Configurable {
    public static final String SETTING_NAME = "UDC Configuration";
    private final String EMPTY_PANEL;
    private final DefaultListModel<ServerListItemWrapper> serverListModel;
    @SuppressWarnings("StatefulEp")
    private final Project project;
    private JLabel defaultServerLabel;
    private JButton newButton;
    private JButton deleteButton;
    private JButton setAsDefaultButton;
    private JButton connectButton;
    private JBList<ServerListItemWrapper> serverList;
    private JPanel mainPanel;
    private JPanel formPanel;


    UDCConfigUI() {
        this.EMPTY_PANEL = "EMPTY_PANEL";
        this.project = null;
        this.$$$setupUI$$$();
        this.serverListModel = new DefaultListModel<>();
        this.serverList.setModel(this.serverListModel);
        this.serverList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.formPanel.add(new JPanel(), this.EMPTY_PANEL);
        this.newButton.addActionListener(e -> {
            final ServerConfigForm serverConfigPanel = new ServerConfigForm();
            final ServerListItemWrapper element = new ServerListItemWrapper(serverConfigPanel);
            UDCConfigUI.this.serverListModel.addElement(element);
            UDCConfigUI.this.serverList.setSelectedValue(element, true);
            UDCConfigUI.this.formPanel.add(serverConfigPanel, serverConfigPanel.getId());
            UDCConfigUI.this.selectForm();
        });
        this.deleteButton.addActionListener(e -> {
            final int selectedIndex = UDCConfigUI.this.serverList.getSelectedIndex();
            final int i = UDCConfigUI.this.confirmDelete();
            if (i == 0 && selectedIndex != -1) {
                if (UDCConfigUI.this.serverListModel.get(selectedIndex).toString().equals(UDCSettings.getSettings(null).getState().defaultServer)) {
                    UDCConfigUI.this.defaultServerLabel.setText("Default:  NA");
                    UDCSettings.getSettings(this.project).getState().defaultServer = "NA";
                }
                UDCConfigUI.this.serverListModel.remove(selectedIndex);
            }
        });
        this.serverList.addListSelectionListener(e -> UDCConfigUI.this.selectForm());
        this.setAsDefaultButton.addActionListener(e -> {
            final ServerConfigForm serverConfigForm = UDCConfigUI.this.serverList.getSelectedValue().serverConfigForm;
            final UDCSettings.ServerSetting serverSetting = new UDCSettings.ServerSetting();
            try {
                serverConfigForm.getData(serverSetting);
                UDCSettings.getSettings(this.project).getState().defaultServer = serverSetting.getLabel();
                if (serverSetting.getLabel() != null) {
                    UDCConfigUI.this.defaultServerLabel.setText("Default:  " + serverSetting.getLabel());
                } else {
                    UDCConfigUI.this.defaultServerLabel.setText("Default:  " + serverSetting.getServer());
                }
                ApplicationManager.getApplication().getMessageBus().syncPublisher(DefaultServerChangeListener.TOPIC).defaultServerChanged();
            } catch (Exception e3) {
                e3.printStackTrace();
            }
        });
        this.connectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                final ServerConfigForm serverConfigForm = UDCConfigUI.this.serverList.getSelectedValue().serverConfigForm;
                final UDCSettings.ServerSetting serverSetting = new UDCSettings.ServerSetting();
                try {
                    serverConfigForm.getData(serverSetting);
                    final ServerConfig serverConfig = ServerConfigManager.createServerConfig(serverSetting);
                    final JDialog messageDialog = new JDialog();
                    messageDialog.setAlwaysOnTop(true);
                    messageDialog.setLocationRelativeTo(null);
                    messageDialog.setUndecorated(true);
                    final JProgressBar progressBar = new JProgressBar();
                    progressBar.setIndeterminate(true);
                    messageDialog.add(progressBar);
                    messageDialog.pack();
                    progressBar.setIndeterminate(true);
                    progressBar.setString("Test Connection:" + serverConfig.getServer());
                    messageDialog.setVisible(true);
                    final SwingWorker sw = new SwingWorker() {
                        @Override
                        protected Object doInBackground() throws Exception {
                            try {
                                UcmdbClient.getUcmdbClient(serverConfig);
                            } catch (Exception e1) {
                                return false;
                            }
                            return true;
                        }

                        @Override
                        protected void done() {
                            messageDialog.setVisible(false);
                            try {
                                if (this.get() != null) {
                                    Messages.showInfoMessage((Project) null, "Connected to " + serverConfig.getServer(), "Info");
                                } else {
                                    Messages.showErrorDialog((Project) null, "Failed to connect to " + serverConfig.getServer(), "Error");
                                }
                            } catch (InterruptedException | ExecutionException e1) {
                                e1.printStackTrace();
                            }
                        }
                    };
                    sw.execute();
                } catch (Exception e2) {
                    Messages.showErrorDialog((Project) null, e2.getMessage(), "Error");
                    throw new RuntimeException(e2);
                }
            }
        });
    }

    public static void main(final String[] args) {
        final JDialog messageDialog = new JDialog();
        messageDialog.setAlwaysOnTop(true);
        messageDialog.setLocationRelativeTo(null);
        messageDialog.setUndecorated(true);
        final JProgressBar progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);
        messageDialog.add(progressBar);
        messageDialog.pack();
        messageDialog.setVisible(true);
    }

    DefaultListModel<ServerListItemWrapper> getSeverList() {
        final Map<String, UDCSettings.ServerSetting> serverConfigs = UDCSettings.getSettings(this.project).getState().serverConfigs;
        for (final String label : serverConfigs.keySet()) {
            final ServerConfigForm serverConfigForm = new ServerConfigForm();
            try {
                serverConfigForm.setData(serverConfigs.get(label));
            } catch (Exception e) {
                e.printStackTrace();
            }
            this.serverListModel.addElement(new ServerListItemWrapper(serverConfigForm));
        }
        return this.serverListModel;
    }

    void setDefault() {
        final ServerConfigForm serverConfigForm = this.serverListModel.getElementAt(0).serverConfigForm;
        final UDCSettings.ServerSetting serverSetting = new UDCSettings.ServerSetting();
        try {
            serverConfigForm.getData(serverSetting);
            UDCSettings.getSettings(this.project).getState().defaultServer = serverSetting.getLabel();
            if (serverSetting.getLabel() != null) {
                this.defaultServerLabel.setText("Default:  " + serverSetting.getLabel());
            } else {
                this.defaultServerLabel.setText("Default:  " + serverSetting.getServer());
            }
            ApplicationManager.getApplication().getMessageBus().syncPublisher(DefaultServerChangeListener.TOPIC).defaultServerChanged();
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    private void selectForm() {
        final ServerListItemWrapper selectedValue = this.serverList.getSelectedValue();
        if (selectedValue != null) {
            final String constraint = selectedValue.serverConfigForm.getId();
            ((CardLayout) this.formPanel.getLayout()).show(this.formPanel, constraint);
        } else {
            ((CardLayout) this.formPanel.getLayout()).show(this.formPanel, this.EMPTY_PANEL);
        }
    }


    // --Commented out by Inspection STOP (3/1/2017 3:04 PM)

    private int confirmDelete() {
        return Messages.showOkCancelDialog((Project) null, "Are you sure to delete this server ?", "Delete", "Yes", "No", Messages.getInformationIcon());
    }

    @Nls
    public String getDisplayName() {
        return "UDC Configuration";
    }

    @Nullable
    public String getHelpTopic() {
        return null;
    }

    @Nullable
    public JComponent createComponent() {
        return this.mainPanel;
    }

    public boolean isModified() {
        final Map<String, UDCSettings.ServerSetting> serverConfigs = UDCSettings.getSettings(this.project).getState().serverConfigs;
        final Map<String, UDCSettings.ServerSetting> newConfig = new HashMap<>();
        for (int i = 0; i < this.serverListModel.size(); ++i) {
            final ServerConfigForm form = this.serverListModel.get(i).serverConfigForm;
            final UDCSettings.ServerSetting serverSetting = new UDCSettings.ServerSetting();
            try {
                form.getDataForModify(serverSetting);
            } catch (Exception e2) {
                e2.printStackTrace();
            }
            newConfig.put(serverSetting.getLabel(), serverSetting);
        }
        return !Arrays.equals(serverConfigs.values().toArray(), newConfig.values().toArray());
    }

    public void apply() throws ConfigurationException {
        final Map<String, UDCSettings.ServerSetting> serverConfigs = UDCSettings.getSettings(this.project).getState().serverConfigs;
        serverConfigs.clear();
        String firstServer = null;
        for (int i = 0; i < this.serverListModel.size(); ++i) {
            final ServerConfigForm form = this.serverListModel.get(i).serverConfigForm;
            final UDCSettings.ServerSetting serverSetting = new UDCSettings.ServerSetting();
            try {
                final boolean isValid = form.getData(serverSetting);
                if (!isValid) {
                    continue;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            serverConfigs.put(serverSetting.getLabel(), serverSetting);
            if (firstServer == null) {
                firstServer = serverSetting.server;
            }
        }
        final String defaultServer = UDCSettings.getSettings(this.project).getState().defaultServer;
        if (defaultServer == null && !serverConfigs.isEmpty()) {
            UDCSettings.getSettings(this.project).getState().defaultServer = firstServer;
        }
        ApplicationManager.getApplication().getMessageBus().syncPublisher(DefaultServerChangeListener.TOPIC).defaultServerChanged();
    }

    public void reset() {
        this.serverListModel.removeAllElements();
        this.formPanel.removeAll();
        final Map<String, UDCSettings.ServerSetting> serverConfigs = UDCSettings.getSettings(this.project).getState().serverConfigs;
        for (final String label : serverConfigs.keySet()) {
            final ServerConfigForm serverConfigForm = new ServerConfigForm();
            try {
                serverConfigForm.setData(serverConfigs.get(label));
            } catch (Exception e) {
                e.printStackTrace();
            }
            this.serverListModel.addElement(new ServerListItemWrapper(serverConfigForm));
            this.formPanel.add(serverConfigForm, serverConfigForm.getId());
        }
        if (this.serverListModel.getSize() > 0) {
            this.serverList.setSelectedIndex(0);
        }
        final String defaultServer = UDCSettings.getSettings(this.project).getState().defaultServer;
        if (!this.serverListModel.toString().contains(defaultServer)) {
            this.defaultServerLabel.setText("Default:  NA");
        } else {
            this.defaultServerLabel.setText("Default:   " + defaultServer);
        }
    }

// --Commented out by Inspection START (3/1/2017 3:04 PM):
//    public String getModelList() {
//        return this.serverListModel.toString();
//    }
// --Commented out by Inspection STOP (3/1/2017 3:04 PM)

    public void disposeUIResources() {
    }

    private void $$$setupUI$$$() {
        final JPanel mainPanel = new JPanel();
        (this.mainPanel = mainPanel).setLayout(new GridLayoutManager(2, 3, JBUI.emptyInsets(), -1, -1, false, false));
        final JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        mainPanel.add(panel, new GridConstraints(0, 1, 1, 2, 0, 0, 3, 3, null, null, null));
        final JButton newButton = new JButton();
        (this.newButton = newButton).setText("New");
        panel.add(newButton);
        final JButton deleteButton = new JButton();
        (this.deleteButton = deleteButton).setText("Delete");
        panel.add(deleteButton);
        final JButton setAsDefaultButton = new JButton();
        (this.setAsDefaultButton = setAsDefaultButton).setText("Set as Default");
        panel.add(setAsDefaultButton);
        final JButton connectButton = new JButton();
        (this.connectButton = connectButton).setText("Test Connect");
        panel.add(connectButton);
        mainPanel.add(new Spacer(), new GridConstraints(1, 2, 1, 1, 0, 2, 1, 6, null, null, null));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(2, 2, JBUI.emptyInsets(), -1, -1, false, false));
        mainPanel.add(panel2, new GridConstraints(1, 0, 1, 2, 0, 3, 3, 3, null, null, null));
        panel2.add(new Spacer(), new GridConstraints(1, 1, 1, 1, 0, 1, 6, 1, null, null, null));
        final JPanel formPanel = new JPanel();
        (this.formPanel = formPanel).setLayout(new CardLayout(0, 0));
        panel2.add(formPanel, new GridConstraints(0, 1, 1, 1, 0, 3, 3, 3, null, null, null));
        final JBList<ServerListItemWrapper> serverList = new JBList<>();
        (this.serverList = serverList).setModel(new DefaultListModel<>());
        panel2.add(serverList, new GridConstraints(0, 0, 2, 1, 0, 3, 2, 6, null, new Dimension(150, 50), null));
        final JLabel defaultServerLabel = new JLabel();
        (this.defaultServerLabel = defaultServerLabel).setHorizontalAlignment(SwingConstants.LEFT);
        defaultServerLabel.setHorizontalTextPosition(SwingConstants.LEFT);
        defaultServerLabel.setText("Label");
        mainPanel.add(defaultServerLabel, new GridConstraints(0, 0, 1, 1, 8, 0, 3, 3, null, null, null));
    }

    private static class ServerListItemWrapper {
        final ServerConfigForm serverConfigForm;

        ServerListItemWrapper(final ServerConfigForm serverConfigForm) {
            this.serverConfigForm = serverConfigForm;
        }

        @Override
        public String toString() {
            return this.serverConfigForm.getLabel();
        }
    }
}