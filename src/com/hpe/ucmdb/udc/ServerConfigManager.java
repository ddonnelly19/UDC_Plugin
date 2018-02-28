package com.hpe.ucmdb.udc;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.ModalityState;
import com.intellij.openapi.options.ShowSettingsUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ServerConfigManager {
    private static final ServerConfigManager INSTANCE;

    static {
        INSTANCE = new ServerConfigManager();
    }

    private Project project;

    public static ServerConfigManager getInstance() {
        return ServerConfigManager.INSTANCE;
    }

    public static ServerConfig createServerConfig(final UDCSettings.ServerSetting ss) {
        return new ServerConfig(ss.label, ss.server, ss.port, ss.username, ss.password, ss.protocol);
    }

    public ServerConfig getDefaultServerConfig(final Project project) {
        this.project = project;
        final UDCConfigUI ui = new UDCConfigUI();
        final UDCSettings.ServerSetting defaultServerSetting = UDCSettings.getDefaultServerSetting(project);
        if (defaultServerSetting == null) {
            ApplicationManager.getApplication().invokeAndWait(() -> {
                if (ui.getSeverList().size() == 0) {
                    Messages.showInfoMessage("You need to configure UDC first. Click OK to proceed.", "Info");
                    ShowSettingsUtil.getInstance().showSettingsDialog(project, "UDC Configuration");
                } else {
                    ui.setDefault();
                }
            }, ModalityState.defaultModalityState());
            return null;
        }
        if (defaultServerSetting.getServer() == null || defaultServerSetting.getServer().isEmpty()) {
            throw new RuntimeException("No any server configured.");
        }
        return createServerConfig(defaultServerSetting);
    }

    public ServerConfig getDefaultServerConfig(final String server) {
        final UDCSettings.ServerSetting serverSetting = UDCSettings.getServerSetting(this.project, server);
        if (serverSetting != null) {
            return createServerConfig(serverSetting);
        }
        return null;
    }

    public Collection<ServerConfig> getAllServerConfig() {
        final Collection<UDCSettings.ServerSetting> allSettings = UDCSettings.getAllSettings(this.project);
        final List<ServerConfig> list = new ArrayList<>();
        for (final UDCSettings.ServerSetting ss : allSettings) {
            list.add(createServerConfig(ss));
        }
        return list;
    }
}