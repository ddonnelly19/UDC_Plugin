package com.hpe.ucmdb.udc;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@State(name = "UDCSettings", storages = {@com.intellij.openapi.components.Storage(file = "$APP_CONFIG$/UDC.xml")})
public class UDCSettings
        implements PersistentStateComponent<UDCSettings.State> {
    private State myState = new State();

    private UDCSettings() {
    }

    public static UDCSettings getSettings(Project project) {
        if (project == null) {
            return ServiceManager.getService(UDCSettings.class);
        }
        return ServiceManager.getService(project, UDCSettings.class);
    }

    public static UDCSettings getSettings() {
        return ServiceManager.getService(UDCSettings.class);
    }

    public static ServerSetting getServerSetting(Project project, String name) {
        return getSettings(project).getState().serverConfigs.get(name);
    }

    public static ServerSetting getDefaultServerSetting(Project project) {
        State state = getSettings(project).getState();
        if (state.defaultServer != null) {
            return state.serverConfigs.get(state.defaultServer);
        }
        Iterator<ServerSetting> iterator = state.serverConfigs.values().iterator();
        if (iterator.hasNext()) {
            return iterator.next();
        }
        return null;
    }

    public static Collection<ServerSetting> getAllSettings(Project project) {
        return getSettings(project).getState().serverConfigs.values();
    }

    @NotNull
    public State getState() {
        State tmp4_1 = this.myState;
        if (tmp4_1 == null) {
            throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[]{"com/hpe/ucmdb/udc/UDCSettings", "getState"}));
        }
        return tmp4_1;
    }

    public void loadState(State state) {
        if (state != null) {
            this.myState = state;
        }
    }

    @SuppressWarnings("CanBeFinal")
    public static class State {
        public String defaultServer;
        public Map<String, UDCSettings.ServerSetting> serverConfigs = new HashMap<>();

        public State() {
        }
    }

    public static class ServerSetting {
        String server;
        int port;
        String username;
        String password;
        String label;
        String protocol;

        public ServerSetting() {
        }

        public String getServer() {
            return this.server;
        }

        public void setServer(String server) {
            this.server = server;
        }

        public int getPort() {
            return this.port;
        }

        public void setPort(int port) {
            this.port = port;
        }

        public String getUsername() {
            return this.username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return this.password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getProtocol() {
            return this.protocol;
        }

        public void setProtocol(String protocol) {
            this.protocol = protocol;
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if ((o == null) || (getClass() != o.getClass())) {
                return false;
            }
            ServerSetting that = (ServerSetting) o;
            return this.port == that.port && (this.password != null ? this.password.equals(that.password) : that.password == null) && this.server.equals(that.server) && this.username.equals(that.username) && this.label.equals(that.label) && this.protocol.equals(that.protocol);
        }

        public int hashCode() {
            return this.server.hashCode();
        }

        public String getLabel() {
            return this.label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public String toString() {
            return this.label + " (" + this.server + ")";
        }
    }
}
