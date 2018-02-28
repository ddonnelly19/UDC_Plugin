package com.hpe.ucmdb.udc;

import com.hp.ucmdb.api.*;
import com.hp.ucmdb.api.discovery.services.DDMConfigurationService;
import com.hp.ucmdb.api.discovery.types.DiscoveryResData;
import com.hp.ucmdb.api.discovery.types.DiscoveryResType;
import com.hp.ucmdb.api.resources.ResourceManagementService;
import com.intellij.openapi.project.Project;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class UcmdbClient {
    private static UcmdbClient currentClient;
    private static Map<String, UcmdbClient> clientPool;

    static {
        UcmdbClient.clientPool = new HashMap<>();
    }

    UcmdbService ucmdbService;
    private ServerConfig serverConfig;

    UcmdbClient(final ServerConfig serverConfig) throws Exception {

        this.serverConfig = serverConfig;

        System.setProperty("ignoreServerCertValidation", "true");
        System.out.println("Connecting to " + this.serverConfig.getServer());
        UcmdbServiceProvider provider = UcmdbServiceFactory.getServiceProvider(this.serverConfig.getProtocol(), this.serverConfig.getServer(), this.serverConfig.getPort());
        final Credentials credentials = provider.createCredentials(this.serverConfig.getUsername(), DesUtil.decrypt(this.serverConfig.getPassword()));
        final ClientContext clientContext = provider.createClientContext("Example");
        ucmdbService = provider.connect(credentials, clientContext);
        System.out.println("Connected: " + ucmdbService.getUcmdbVersion().getFullServerVersion());


    }

// --Commented out by Inspection START (3/1/2017 3:04 PM):
//    public static UcmdbClient getUcmdbClient(final String server) throws Exception {
//        final UcmdbClient uc = UcmdbClient.clientPool.get(server);
//        if (uc != null) {
//            return uc;
//        }
//        final ServerConfig config = ServerConfigManager.getInstance().getDefaultServerConfig(server);
//        if (config == null) {
//            throw new IllegalArgumentException("Unknown server:" + server + ", please save the setting first.");
//        }
//        final UcmdbClient ucmdbClient = new UcmdbClient(config);
//        return testConnection(ucmdbClient, config);
//    }
// --Commented out by Inspection STOP (3/1/2017 3:04 PM)

    public static UcmdbClient getUcmdbClient(final Project project) throws Exception {
        if (UcmdbClient.currentClient != null) {
            validateConfig(project);
        }
        final ServerConfig config = ServerConfigManager.getInstance().getDefaultServerConfig(project);
        return getUcmdbClient(config);
    }

    public static UcmdbClient getUcmdbClient(final ServerConfig config) throws Exception {
        synchronized (config) {
            if (UcmdbClient.currentClient == null) {
                final UcmdbClient uc = UcmdbClient.clientPool.get(config.getLabel());
                if (uc != null) {
                    UcmdbClient.currentClient = uc;
                }
            }
            if (UcmdbClient.currentClient == null) {
                UcmdbClient.currentClient = new UcmdbClientNew(config);
            }
            UcmdbClient.currentClient = testConnection(UcmdbClient.currentClient, config);
            UcmdbClient.clientPool.put(config.getLabel(), UcmdbClient.currentClient);
            return UcmdbClient.currentClient;
        }
    }

    private static UcmdbClient testConnection(UcmdbClient client, final ServerConfig config) {
        /*if (!client.isConnect || !validateConnectivity(client)) {
            client.connect();
            final String fullServerVersion = client.getUCMDBVersion().getFullServerVersion();
            final String[] split = fullServerVersion.split("\\.");
            final int major = Integer.parseInt(split[0]);
            final int minor = Integer.parseInt(split[1]);
            if (major >= 10 && minor >= 20) {
                client = new UcmdbClientNew(config);
                client.connect();
            }
        }*/
        return client;
    }

// --Commented out by Inspection START (3/2/2017 2:56 PM):
//    private static boolean validateConnectivity(final UcmdbClient client) {
//        try {
//            final UcmdbVersion ucmdbVersion = client.ucmdbService.getUcmdbVersion();
//            return ucmdbVersion != null;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return client.isConnect = false;
//        }
//    }
// --Commented out by Inspection STOP (3/2/2017 2:56 PM)

// --Commented out by Inspection START (3/1/2017 3:04 PM):
//    public static void invalidateClient() {
//        UcmdbClient.currentClient = null;
//    }
// --Commented out by Inspection STOP (3/1/2017 3:04 PM)

    private static void validateConfig(final Project project) {
        final ServerConfig serverConfig = ServerConfigManager.getInstance().getDefaultServerConfig(project);
        if (!UcmdbClient.currentClient.serverConfig.equals(serverConfig)) {
            UcmdbClient.currentClient = null;
        }
    }

    public static void refreshPool() {
        final Collection<ServerConfig> allServerConfig = ServerConfigManager.getInstance().getAllServerConfig();
        final String message = "RefreshPool" + allServerConfig.toString();
        System.out.println(message);
        for (final ServerConfig sc : allServerConfig) {
            try {
                final UcmdbClient ucmdbClient = getUcmdbClient(sc);
                UcmdbClient.clientPool.put(sc.getServer(), ucmdbClient);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(final String[] args) throws Exception {
        UcmdbServiceProvider provider;
        try {
            provider = UcmdbServiceFactory.getServiceProvider("example", 8080);
            final Credentials credentials = provider.createCredentials("userName", "password");
            final ClientContext clientContext = provider.createClientContext("Example");
            final UcmdbService ucmdbService = provider.connect(credentials, clientContext);
            System.out.println(ucmdbService);
            final ResourceManagementService resourceManagementService = ucmdbService.getResourceManagementService();
            System.out.println(resourceManagementService);
            final Method m = resourceManagementService.getClass().getMethod("getRevisions", String.class);
            final Object invoke = m.invoke(resourceManagementService, "dns.py");
            System.out.println(invoke);
            final Map[] mx = (Map[]) invoke;
            for (Map aMx : mx) {
                System.out.println(aMx.get("revision"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

// --Commented out by Inspection START (3/2/2017 2:56 PM):
//    private UcmdbVersion getUCMDBVersion() {
//        return this.ucmdbService.getUcmdbVersion();
//    }
// --Commented out by Inspection STOP (3/2/2017 2:56 PM)

// --Commented out by Inspection START (3/2/2017 2:56 PM):
//    private void connect() throws Exception {
//        if (!this.isConnect) {
//            System.out.println("Begin connect...");
//            this.doConnect();
//            System.out.println("Connected.");
//        }
//        //return this.isConnect;
//    }
// --Commented out by Inspection STOP (3/2/2017 2:56 PM)

// --Commented out by Inspection START (3/2/2017 2:57 PM):
//    private void doConnect() throws Exception {
//        UcmdbServiceProvider provider;
//        System.setProperty("ignoreServerCertValidation", "true");
//        provider = UcmdbServiceFactory.getServiceProvider(this.serverConfig.getProtocol(), this.serverConfig.getServer(), this.serverConfig.getPort());
//        final Credentials credentials = provider.createCredentials(this.serverConfig.getUsername(), DesUtil.decrypt(this.serverConfig.getPassword()));
//        final ClientContext clientContext = provider.createClientContext("Example");
//        final UcmdbService ucmdbService = provider.connect(credentials, clientContext);
//        System.out.println(ucmdbService);
//        if (ucmdbService != null) {
//            this.ucmdbService = ucmdbService;
//            boolean isConnect = true;
//        }
//    }
// --Commented out by Inspection STOP (3/2/2017 2:57 PM)

// --Commented out by Inspection START (3/1/2017 3:04 PM):
//    public DiscoveryResData getResources(final DiscoveryResType type, final String name) {
//        final DDMConfigurationService ddmc = this.ucmdbService.getDDMConfigurationService();
//        final DiscoveryResType x = DiscoveryResType.values()[type.ordinal()];
//        return ddmc.getResource(x, name);
//    }
// --Commented out by Inspection STOP (3/1/2017 3:04 PM)

    public void setResourceContent(final DiscoveryResType type, final String name, final byte[] content) {
        final DDMConfigurationService ddmc = this.ucmdbService.getDDMConfigurationService();
        ddmc.updateResourceData(type, name, content);
    }

    public byte[] getResourceContent(final DiscoveryResType type, final String name) {
        final DDMConfigurationService ddmc = this.ucmdbService.getDDMConfigurationService();
        return ddmc.getResourceData(type, name);
    }

// --Commented out by Inspection START (3/1/2017 3:04 PM):
//    @SuppressWarnings("deprecation")
//    public void startJob(final String job) {
//        final DDMManagementService ddmManagementService = this.ucmdbService.getDDMManagementService();
//        final ArrayList<String> jobs = new ArrayList<String>();
//        jobs.add(job);
//        ddmManagementService.activateJobs(new HashSet<>(jobs));
//    }
// --Commented out by Inspection STOP (3/1/2017 3:04 PM)

// --Commented out by Inspection START (3/1/2017 3:04 PM):
//    @SuppressWarnings("deprecation")
//    public void stopJob(final String job) {
//        final DDMManagementService ddmManagementService = this.ucmdbService.getDDMManagementService();
//        final ArrayList<String> jobs = new ArrayList<String>();
//        jobs.add(job);
//        ddmManagementService.deactivateJobs(new HashSet<String>(jobs));
//    }
// --Commented out by Inspection STOP (3/1/2017 3:04 PM)

    public Collection<DiscoveryResData> listResources(final DiscoveryResType type) {
        final DDMConfigurationService ddmc = this.ucmdbService.getDDMConfigurationService();
        return ddmc.listResources(type);
    }

    public void deployPackage(final File file) throws IOException {
        this.ucmdbService.getResourceManagementService().deployPackage(file);
    }

// --Commented out by Inspection START (3/2/2017 2:56 PM):
//    public void deployPackage(final InputStream packageContent) throws IOException {
//        this.ucmdbService.getResourceManagementService().deployPackage("hello.zip", packageContent);
//    }
// --Commented out by Inspection STOP (3/2/2017 2:56 PM)

// --Commented out by Inspection START (3/2/2017 2:56 PM):
//    public Map<String, Object>[] getHistory(final String name) throws Exception {
//        final ResourceManagementService resourceManagementService = this.ucmdbService.getResourceManagementService();
//        final Method m = resourceManagementService.getClass().getMethod("getRevisions", String.class);
//        //noinspection unchecked
//        return (Map<String, Object>[]) m.invoke(resourceManagementService, name);
//    }
// --Commented out by Inspection STOP (3/2/2017 2:56 PM)

// --Commented out by Inspection START (3/2/2017 2:56 PM):
//    public byte[] getRevisionContent(final String resourceName, final long revision) throws Exception {
//        final ResourceManagementService resourceManagementService = this.ucmdbService.getResourceManagementService();
//        final Method m = resourceManagementService.getClass().getMethod("getRevisionContent", String.class, Long.TYPE);
//        final Object invoke = m.invoke(resourceManagementService, resourceName, revision);
//        return (byte[]) invoke;
//    }
// --Commented out by Inspection STOP (3/2/2017 2:56 PM)
}