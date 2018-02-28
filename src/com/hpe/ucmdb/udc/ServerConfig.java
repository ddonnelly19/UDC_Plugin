package com.hpe.ucmdb.udc;

public class ServerConfig implements Cloneable {
    private final String label;
    private final String server;
    private final String username;
    private final String password;
    private final String protocol;
    private int port;

    public ServerConfig(final String label, final String server, final int port, final String username, final String password, final String protocol) {
        this.port = 8080;
        this.label = label;
        this.server = server;
        this.port = port;
        this.username = username;
        this.password = password;
        this.protocol = protocol;
    }

// --Commented out by Inspection START (3/1/2017 3:04 PM):
//    public ServerConfig() {
//        this.port = 8080;
//    }
// --Commented out by Inspection STOP (3/1/2017 3:04 PM)

    private ServerConfig(final ServerConfig serverConfig) {
        this.port = 8080;
        this.label = serverConfig.label;
        this.server = serverConfig.server;
        this.port = serverConfig.port;
        this.username = serverConfig.username;
        this.password = serverConfig.password;
        this.protocol = serverConfig.protocol;
    }

    @Override
    protected ServerConfig clone() throws CloneNotSupportedException {
        ServerConfig serverConfig = (ServerConfig) super.clone();
        return new ServerConfig(this);
    }

    public String getServer() {
        return this.server;
    }

    public String getLabel() {
        return this.label;
    }

// --Commented out by Inspection START (3/1/2017 3:04 PM):
//    public void setLabel(final String label) {
//        this.label = label;
//    }
// --Commented out by Inspection STOP (3/1/2017 3:04 PM)

// --Commented out by Inspection START (3/1/2017 3:04 PM):
//    public void setServer(final String server) {
//        this.server = server;
//    }
// --Commented out by Inspection STOP (3/1/2017 3:04 PM)

    public int getPort() {
        return this.port;
    }

// --Commented out by Inspection START (3/1/2017 3:04 PM):
//    public void setPort(final int port) {
//        this.port = port;
//    }
// --Commented out by Inspection STOP (3/1/2017 3:04 PM)

    public String getUsername() {
        return this.username;
    }

// --Commented out by Inspection START (3/1/2017 3:04 PM):
//    public void setUsername(final String username) {
//        this.username = username;
//    }
// --Commented out by Inspection STOP (3/1/2017 3:04 PM)

    public String getPassword() {
        return this.password;
    }

// --Commented out by Inspection START (3/1/2017 3:04 PM):
//    public void setPassword(final String password) {
//        this.password = password;
//    }
// --Commented out by Inspection STOP (3/1/2017 3:04 PM)

    public String getProtocol() {
        return this.protocol;
    }

// --Commented out by Inspection START (3/1/2017 3:04 PM):
//    public void setProtocol(final String protocol) {
//        this.protocol = protocol;
//    }
// --Commented out by Inspection STOP (3/1/2017 3:04 PM)

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final ServerConfig that = (ServerConfig) o;
        if (this.port != that.port) {
            return false;
        }
        Label_0075:
        {
            if (this.label != null) {
                if (this.label.equals(that.label)) {
                    break Label_0075;
                }
            } else if (that.label == null) {
                break Label_0075;
            }
            return false;
        }
        Label_0108:
        {
            if (this.password != null) {
                if (this.password.equals(that.password)) {
                    break Label_0108;
                }
            } else if (that.password == null) {
                break Label_0108;
            }
            return false;
        }
        Label_0141:
        {
            if (this.server != null) {
                if (this.server.equals(that.server)) {
                    break Label_0141;
                }
            } else if (that.server == null) {
                break Label_0141;
            }
            return false;
        }
        if (this.username != null) {
            if (this.username.equals(that.username)) {
                return true;
            }
        } else if (that.username == null) {
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        int result = (this.label != null) ? this.label.hashCode() : 0;
        result = 31 * result + ((this.server != null) ? this.server.hashCode() : 0);
        result = 31 * result + this.port;
        result = 31 * result + ((this.username != null) ? this.username.hashCode() : 0);
        result = 31 * result + ((this.password != null) ? this.password.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return this.label + " (" + this.server + ")";
    }
}