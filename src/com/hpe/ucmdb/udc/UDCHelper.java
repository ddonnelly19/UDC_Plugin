package com.hpe.ucmdb.udc;

import com.hp.ucmdb.api.discovery.types.DiscoveryResData;
import com.hp.ucmdb.api.discovery.types.DiscoveryResType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.module.Module;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class UDCHelper {
    // --Commented out by Inspection (3/1/2017 3:04 PM):public static final String DISCOVERYSCRIPTS = "discoveryScripts";
    private Project project;

    private UDCHelper(final ServerConfig config) {
    }

    public UDCHelper(final Project project, final ServerConfig serverConfig) {
        this(serverConfig);
        this.project = project;
    }

    public Collection<DiscoveryResData> listResources(DiscoveryResType... resTypes) {
        Collection<DiscoveryResData> ret = new ArrayList<>();
        Arrays.asList(resTypes).parallelStream().forEach(resType -> {
            try {
                synchronized (ret) {
                    ret.addAll(UcmdbClient.getUcmdbClient(this.project).listResources(resType));
                }
            } catch (Exception e) {
                System.err.println("Error downloading resource type " + resType);
                e.printStackTrace();
            }
        });

        return ret;
    }

    public byte[] getContent(final DiscoveryResType type, final String name) throws Exception {
        return UcmdbClient.getUcmdbClient(this.project).getResourceContent(type, name);
    }

    public void setContent(final DiscoveryResType type, final String name, final byte[] content) throws Exception {
        UcmdbClient.getUcmdbClient(this.project).setResourceContent(type, name, content);
    }

    public Module convertToModule()
    
/*    @Deprecated
    private int pushByPackage(final Project project, final VirtualFile[] vFiles) throws IOException {
        final List<VirtualFile> allFiles = new ArrayList<>();
        for (final VirtualFile vf : vFiles) {
            if (vf.isDirectory()) {
                final VirtualFile[] children = vf.getChildren();
                allFiles.addAll(Arrays.asList(children));
            }
            else {
                allFiles.add(vf);
            }
        }
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final ZipOutputStream zos = new ZipOutputStream(baos);
        for (final VirtualFile vf : allFiles) {
            zos.putNextEntry(new ZipEntry("discoveryScripts/" + vf.getName()));
            zos.write(vf.contentsToByteArray());
            zos.closeEntry();
        }
        zos.finish();
        zos.close();
        final Runnable runnable = () -> {
            try {
                final UcmdbClient uc = UcmdbClient.getUcmdbClient(project);
                uc.deployPackage(new ByteArrayInputStream(baos.toByteArray()));
            }
            catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
        ProgressManager.getInstance().runProcessWithProgressSynchronously(runnable, "Push", true, project);
        return allFiles.size();
    }*/

// --Commented out by Inspection START (3/1/2017 3:04 PM):
//    public void createPackage(final String name) throws Exception {
//        final ByteArrayOutputStream out = new ByteArrayOutputStream();
//        final ZipOutputStream zos = new ZipOutputStream(out);
//        zos.putNextEntry(new ZipEntry("discoveryResources/dummy_" + name + ".txt"));
//        final String s = "This file is a place holder for creating a empty package.";
//        zos.write(s.getBytes());
//        zos.closeEntry();
//        zos.close();
//        final ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
//        System.out.println("Begin deploy");
//        UcmdbClient.getUcmdbClient(this.project).deployPackage(name, (InputStream)in);
//    }
// --Commented out by Inspection STOP (3/1/2017 3:04 PM)

// --Commented out by Inspection START (3/1/2017 3:04 PM):
//    public Map<String, Object>[] getHistory(final String name) throws Exception {
//        return (Map<String, Object>[])UcmdbClient.getUcmdbClient(this.project).getHistory(name);
//    }
// --Commented out by Inspection STOP (3/1/2017 3:04 PM)

// --Commented out by Inspection START (3/1/2017 3:04 PM):
//    public byte[] getRevisionContent(final String resourceName, final long revision) throws Exception {
//        return UcmdbClient.getUcmdbClient(this.project).getRevisionContent(resourceName, revision);
//    }
// --Commented out by Inspection STOP (3/1/2017 3:04 PM)
}