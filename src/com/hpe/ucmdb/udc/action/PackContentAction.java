package com.hpe.ucmdb.udc.action;

import com.hp.ucmdb.api.discovery.types.DiscoveryResType;
import com.hpe.ucmdb.udc.ResTypeUtil;
import com.intellij.ide.FileSelectInContext;
import com.intellij.ide.SelectInTarget;
import com.intellij.ide.projectView.ProjectView;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VfsUtilCore;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileVisitor;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;

public class PackContentAction extends AnAction {
    private final List<String> packageCategory;

    public PackContentAction() {
        (this.packageCategory = new ArrayList<>()).add("discoveryPatterns");
        this.packageCategory.add("discoveryModules");
        this.packageCategory.add("discoveryScripts");
        this.packageCategory.add("discoveryConfigFiles");
        this.packageCategory.add("discoveryResources");
        this.packageCategory.add("discoveryJobs");
        this.packageCategory.add("discoveryWizards");
        this.packageCategory.add("docs");
        this.packageCategory.add("discoverySaiResources");
        this.packageCategory.add("discoveryScannerConfiguration");
        this.packageCategory.add("discoveryMultiScannerPackage");
        this.packageCategory.add("discoveryManagementZones");
        this.packageCategory.add("serviceDiscoveryActivityType");
        this.packageCategory.add("serviceDiscoveryActivityTemplate");
    }

    private static void writeSingleResource(final VirtualFile resTypeFolder, final String resourceName, final byte[] content) {
        ApplicationManager.getApplication().runWriteAction(() -> {
            try {
                final VirtualFile child = resTypeFolder.findOrCreateChildData(null, resourceName);
                child.setBinaryContent(content);
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("Write to file:" + resourceName);
        });
    }

// --Commented out by Inspection START (3/1/2017 3:04 PM):
//    private void execute(final Project project, final DataContext dataContext, final VirtualFile[] vFiles, final String s) throws Exception {
//        final JarOutputStream zos = new JarOutputStream(new FileOutputStream(s));
//        try {
//            for (final VirtualFile vf : vFiles) {
//                final VirtualFile[] relatedVF;
//                final VirtualFile[] children = relatedVF = this.getRelatedVF(vf);
//                for (final VirtualFile child : relatedVF) {
//                    final JarEntry ze = this.getEntry(child);
//                    if (ze != null) {
//                        zos.putNextEntry(ze);
//                        zos.write(child.contentsToByteArray());
//                        zos.closeEntry();
//                        zos.flush();
//                    }
//                }
//            }
//            zos.finish();
//        }
//        finally {
//            zos.close();
//        }
//    }
// --Commented out by Inspection STOP (3/1/2017 3:04 PM)

    public void actionPerformed(@NotNull final AnActionEvent event) {
        final Project project = event.getProject();
        final VirtualFile root = event.getData(PlatformDataKeys.VIRTUAL_FILE);
        int i1 = 0;
        assert root != null;
        if (root.getParent().findChild(root.getName() + ".zip") != null) {
            final String s = "This destination already contains a ZIP file named '" + root.getName() + "'.zip. Do you still want to overwrite this ZIP file ? ";
            i1 = Messages.showOkCancelDialog(project, s, "Confirmation", "Yes", "No", Messages.getInformationIcon());
        }
        final List<VirtualFile> children = new ArrayList<>();
        for (final String packageN : this.packageCategory) {
            if (root.findChild(packageN) != null) {
                children.add(root.findChild(packageN));
            }
        }
        if (children.size() != 0) {
            if (i1 == 0) {
                try {
                    final byte[] zipContent = this.getZipContent(project, event.getDataContext(), root);
                    final String dirZipName = root.getName() + ".zip";
                    writeSingleResource(root.getParent(), dirZipName, zipContent);
                    Messages.showInfoMessage("The ZIP file '" + root.getName() + "' has been packed successfully and was saved to " + root.getCanonicalPath() + ".zip.", "Information");
                    final VirtualFile vf = root.getParent().findChild(dirZipName);
                    assert vf != null;
                    ProjectView.getInstance(event.getProject()).getSelectInTargets().toArray(new SelectInTarget[0])[0].selectIn(new FileSelectInContext(event.getProject(), vf), true);
                } catch (Exception e) {
                    e.printStackTrace();
                    Messages.showErrorDialog(e.getMessage(), "Error");
                }
            }
        }
    }

    private byte[] getZipContent(final Project project, final DataContext dataContext, final VirtualFile root) throws Exception {
        JarOutputStream zos;
        final ByteArrayOutputStream bos = new ByteArrayOutputStream();
        zos = new JarOutputStream(bos);
        try {
            final Set<VirtualFile[]> childrenList = new HashSet<>();
            for (final String packageN : this.packageCategory) {
                if (root.findChild(packageN) != null) {
                    childrenList.add(this.getRelatedVF(root.findChild(packageN)));
                }
            }
            if (root.findChild("descriptor.xml") != null) {
                childrenList.add(this.getRelatedVF(root.findChild("descriptor.xml")));
            }
            for (final VirtualFile[] array : childrenList) {
                for (final VirtualFile child : array) {
                    final JarEntry ze = this.getEntry(child);
                    if (ze != null) {
                        zos.putNextEntry(ze);
                        zos.write(child.contentsToByteArray());
                        zos.closeEntry();
                        zos.flush();
                    }
                }
            }
            zos.finish();
            return bos.toByteArray();
        } finally {
            try {
                zos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private VirtualFile[] getRelatedVF(final VirtualFile vf) {
        final List<VirtualFile> list = new ArrayList<>();
        final VirtualFileVisitor visitor = new VirtualFileVisitor() {
            public boolean visitFile(@NotNull final VirtualFile file) {
                if (!file.isDirectory()) {
                    list.add(file);
                }
                return true;
            }
        };
        VfsUtilCore.visitChildrenRecursively(vf, visitor);
        return list.toArray(new VirtualFile[list.size()]);
    }

    private JarEntry getEntry(final VirtualFile vf) {
        final Object[] resourceName = ResTypeUtil.getResourceName(vf);
        if (resourceName != null && resourceName[0] != null && resourceName[1] != null) {
            final DiscoveryResType resType = (DiscoveryResType) resourceName[0];
            final String pathInZip = ResTypeUtil.getResName(resType) + "/" + resourceName[1];
            final JarEntry je = new JarEntry(pathInZip);
            je.setSize(vf.getLength());
            return je;
        }
        if (vf.getName().equals("descriptor.xml")) {
            final String pathInZip2 = "descriptor.xml";
            final JarEntry je2 = new JarEntry(pathInZip2);
            je2.setSize(vf.getLength());
            return je2;
        }
        return null;
    }

    public void update(final AnActionEvent e) {
        final VirtualFile data = e.getData(PlatformDataKeys.VIRTUAL_FILE);
        if (data == null) {
            e.getPresentation().setEnabled(false);
            return;
        }
        Boolean contain;
        final List<VirtualFile> children = new ArrayList<>();
        for (final String packageN : this.packageCategory) {
            if (data.findChild(packageN) != null) {
                children.add(data.findChild(packageN));
            }
        }
        contain = children.size() != 0;
        e.getPresentation().setEnabled(contain);
    }
}