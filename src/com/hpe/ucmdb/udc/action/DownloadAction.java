package com.hpe.ucmdb.udc.action;

import com.hp.ucmdb.api.discovery.types.DiscoveryResData;
import com.hp.ucmdb.api.discovery.types.DiscoveryResType;
import com.hpe.ucmdb.udc.*;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.command.CommandProcessor;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtil;
import com.intellij.openapi.progress.PerformInBackgroundOption;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.*;
import com.intellij.openapi.roots.libraries.Library;
import com.intellij.openapi.roots.libraries.LibraryTable;
import com.intellij.openapi.ui.MessageType;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.ui.popup.util.PopupUtil;
import com.intellij.openapi.vfs.JarFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class DownloadAction extends AnAction {
    private VirtualFile root;
    //private static UDCHelper udcHelper;
    private ModuleRootManager module;
    private Project project;

    private static boolean isRoot(@NotNull final Module module, @NotNull final VirtualFile dir) {
        //if (module == null) {
        //    throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", "module", "com/hpe/ucmdb/udc/action/DownloadAction", "isRoot"));
        //}
        //if (dir == null) {
        //    throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", "dir", "com/hpe/ucmdb/udc/action/DownloadAction", "isRoot"));
        //}
        return dir.equals(ProjectFileIndex.SERVICE.getInstance(module.getProject()).getContentRootForFile(dir));
    }

// --Commented out by Inspection START (3/2/2017 2:39 PM):
//    private static void runModelWriteAction(Runnable writeAction) {
//        Application application = ApplicationManager.getApplication();
//        if (modModule.isWritable() && application.isWriteAccessAllowed()) {
//            System.out.println("Running " + writeAction.getClass());
//            writeAction.run();
//        } else
//            application.invokeLater(() -> runModelWriteAction(writeAction));
//    }
// --Commented out by Inspection STOP (3/2/2017 2:39 PM)

    private static Map<String, Map<DiscoveryResType, List<DiscoveryResData>>> buildResourcesPerPackageMap(Collection<DiscoveryResData> discoveryResList) {
        Map<String, Map<DiscoveryResType, List<DiscoveryResData>>> map2 = new HashMap<>();
        discoveryResList.stream().collect(Collectors.groupingBy(resData -> {
            String packageName = resData.getPackageName();
            if (packageName != null && packageName.endsWith(".zip")) {
                packageName = packageName.substring(0, packageName.length() - 4);
            }
            return packageName == null ? "No Package" : packageName;
        })).forEach((key, value) -> map2.computeIfAbsent(key, value2 -> value.stream().collect(Collectors.groupingBy(res2 ->
                ResTypeUtil.getResType(res2.getResourceType())
        ))));
        return map2;
    }

    private void runWriteAction(@NotNull final Runnable writeAction) {
        ApplicationManager.getApplication().invokeLater(() -> CommandProcessor.getInstance().executeCommand(project, () -> ApplicationManager.getApplication().runWriteAction(writeAction), "DiskRead", null));
    }

// --Commented out by Inspection START (3/1/2017 2:57 PM):
//    private static void writePackage(final List<TempData> tempDataList) throws Exception {
//        for (final TempData tempData : tempDataList) {
//            final List<String> subFols = tempData.subFol;
//            final File childDirectory = new File(DownloadAction.root.getPath(), tempData.packageName);
//            if (!childDirectory.exists()) {
//                childDirectory.mkdirs();
//            }
//            final File resTypeFolder = new File(childDirectory.getPath(), ResTypeUtil.getResName(tempData.resType));
//            if (!resTypeFolder.exists()) {
//                resTypeFolder.mkdirs();
//            }
//            File sub = null;
//            final List<File> temp = new ArrayList<File>();
//            if (subFols.size() > 0) {
//                for (int i = 0; i < subFols.size(); ++i) {
//                    if (i == 0) {
//                        sub = new File(resTypeFolder, subFols.get(0));
//                        if (!sub.exists()) {
//                            sub.mkdirs();
//                        }
//                        temp.add(sub);
//                    }
//                    else {
//                        sub = new File(temp.get(temp.size() - 1), subFols.get(i));
//                        if (!sub.exists()) {
//                            sub.mkdirs();
//                        }
//                        temp.add(sub);
//                    }
//                }
//            }
//            else {
//                sub = resTypeFolder;
//            }
//            final byte[] content = (byte[])tempData.content;
//            String dataName = tempData.dataName;
//            if (resTypeFolder.getName().equals("discoveryPatterns")) {
//                dataName += ".xml";
//            }
//            final File aim = new File(sub.getPath() + "\\" + dataName);
//            BufferedOutputStream bos = null;
//            FileOutputStream fos = null;
//            fos = new FileOutputStream(aim);
//            bos = new BufferedOutputStream(fos);
//            bos.write(content);
//            bos.close();
//            fos.close();
//        }
//    }
// --Commented out by Inspection STOP (3/1/2017 2:57 PM)

// --Commented out by Inspection START (3/1/2017 2:57 PM):
//    public static void downloadSinglePackage(final Map<String, Map<DiscoveryResType, List<DiscoveryResData>>> tempDataMap, final List<TempData> tempDataList, final String packageName, final Map<DiscoveryResType, List<DiscoveryResData>> discoveryResourceTypeListMap) throws Exception {
//        for (final DiscoveryResType resType : discoveryResourceTypeListMap.keySet()) {
//            final List<DiscoveryResData> discoveryResDataList = discoveryResourceTypeListMap.get(resType);
//            for (final DiscoveryResData resData : discoveryResDataList) {
//                final byte[] content = DownloadAction.udcHelper.getContent(resType, resData.getResourceName());
//                final int nameLength = resData.getResourceName().split("/").length;
//                final List<String> subFol = new ArrayList<String>();
//                if (nameLength >= 2) {
//                    for (int i = 0; i < nameLength - 1; ++i) {
//                        subFol.add(resData.getResourceName().split("/")[i]);
//                    }
//                }
//                final String dataName = resData.getResourceName().split("/")[nameLength - 1];
//                final TempData tempData = new TempData(resType, dataName, content, packageName, subFol);
//                tempDataList.add(tempData);
//            }
//        }
//    }
// --Commented out by Inspection STOP (3/1/2017 2:57 PM)

    private VirtualFile findOrCreateChildDirectory(final VirtualFile parent, @Nullable final String childName) {
        // ApplicationManager.getApplication().runWriteAction(() -> {
        try {
            assert childName != null;
            final VirtualFile childDirectory = parent.findChild(childName);
            if (childDirectory == null || !childDirectory.exists()) {
                parent.createChildDirectory(null, childName);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        //System.out.println("create folder to file:" + childName);
        // });
        return parent.findChild(childName);
    }

    private void downloadSinglePackage1(final String packageName, final Map<DiscoveryResType, List<DiscoveryResData>> discoveryResourceTypeListMap, ProgressIndicator progressIndicator, UDCHelper udcHelper) {
        //final List<TempData> tempList = new ArrayList<>();
        runWriteAction(() -> {

            List<VirtualFile> jars = new ArrayList<>();
            final VirtualFile[] sourceFolder = {null};

            for (final DiscoveryResType resType : discoveryResourceTypeListMap.keySet()) {
                final List<DiscoveryResData> discoveryResDataList = discoveryResourceTypeListMap.get(resType);
                for (final DiscoveryResData resData : discoveryResDataList) {

                    progressIndicator.setText("Downloading " + packageName + ": " + resData.getResourceName());

                    try {
                        final byte[] content = udcHelper.getContent(resType, resData.getResourceName());
                        final int nameLength = resData.getResourceName().split("/").length;
                        final List<String> subFol = new ArrayList<>();
                        if (nameLength >= 2) {
                            for (int i = 0; i < nameLength - 1; ++i) {
                                subFol.add(resData.getResourceName().split("/")[i]);
                            }
                        }
                        String dataName = resData.getResourceName().split("/")[nameLength - 1];

                        final VirtualFile childDirectory = findOrCreateChildDirectory(root, packageName);
                        final VirtualFile resTypeFolder = findOrCreateChildDirectory(childDirectory, ResTypeUtil.getResName(resType));
                        VirtualFile sub = null;
                        final List<VirtualFile> temp = new ArrayList<>();
                        if (subFol.size() > 0) {
                            for (int i = 0; i < subFol.size(); ++i) {
                                if (i == 0) {
                                    sub = findOrCreateChildDirectory(resTypeFolder, subFol.get(0));
                                    temp.add(sub);
                                } else {
                                    sub = findOrCreateChildDirectory(temp.get(temp.size() - 1), subFol.get(i));
                                    temp.add(sub);
                                }
                            }
                        } else {
                            sub = resTypeFolder;
                        }
                        //final byte[] content = (byte[]) tempData.content;
                        //String dataName = tempData.dataName;
                        if (resTypeFolder.getName().equals("discoveryPatterns")) {
                            dataName += ".xml";
                        }


                        assert sub != null;
                        final VirtualFile child = sub.findOrCreateChildData(null, dataName);
                        child.setBinaryContent(content);

                        if (Objects.equals(child.getExtension(), "py"))
                            sourceFolder[0] = child.getParent();
                        else if (Objects.equals(child.getExtension(), "jar")) {
                            VirtualFile jarFile = JarFileSystem.getInstance().getJarRootForLocalFile(child);
                            jars.add(jarFile);
                        }

                    } catch (Exception e) {
                        System.err.println("Error processing " + resData.getResourceName());
                        e.printStackTrace();
                    }


                }

            }

            if (!jars.isEmpty()) {
                ModifiableRootModel modModule = module.getModifiableModel();
                //runModelWriteAction(() -> {
                LibraryTable libraryTable = modModule.getModuleLibraryTable();
                Library library = libraryTable.getLibraryByName(packageName);
                if (library == null)
                    library = libraryTable.createLibrary(packageName);
                Library.ModifiableModel modifiableLibrary = library.getModifiableModel();

                for (OrderRootType orderRootType : OrderRootType.getAllTypes()) {
                    for (VirtualFile jarFile : jars) {
                        modifiableLibrary.addRoot(jarFile, orderRootType);
                    }
                }

                modifiableLibrary.commit();

                LibraryOrderEntry libraryOrderEntry = modModule.findLibraryOrderEntry(library);
                if (libraryOrderEntry != null) libraryOrderEntry.setScope(DependencyScope.PROVIDED);
                modModule.commit();
                // });
            }

            if (sourceFolder[0] != null) {
                //VirtualFile finalSourceFolder = sourceFolder;
                //runModelWriteAction(() -> {
                ModifiableRootModel modModule = module.getModifiableModel();
                ContentEntry rootEntry = modModule.getContentEntries()[0];
                rootEntry.addSourceFolder(sourceFolder[0], false);

                modModule.commit();
                //});
            }
        });
    }

    public void actionPerformed(final AnActionEvent e) {
        this.project = e.getProject();

        this.getRootFolder(e);
        final ServerConfig serverConfig = ServerConfigManager.getInstance().getDefaultServerConfig(e.getProject());
        if (serverConfig == null) {
            return;
        }

        ProjectFileIndex fileIndex = ProjectRootManager.getInstance(project).getFileIndex();

        final DiscoveryResType[] resTypes = {DiscoveryResType.ADAPTER, DiscoveryResType.JOB, DiscoveryResType.SCRIPT, DiscoveryResType.SERVER_DATA, DiscoveryResType.USER_EXT};
        module = ModuleRootManager.getInstance(fileIndex.getModuleForFile(e.getData(PlatformDataKeys.VIRTUAL_FILE)));
        try {
            this.connectSeverInBackground(new UDCHelper(this.project, serverConfig), resTypes);
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    private void download(Map<String, Map<DiscoveryResType, List<DiscoveryResData>>> resGroup, UDCHelper udcHelper) {
        //final Map<String, Map<DiscoveryResType, List<DiscoveryResData>>> resGroup = buildResourcesPerPackageMap(udcHelper.listResources(resTypes));
        /*for (final DiscoveryResType resType : resTypes) {
            final Collection<DiscoveryResData> discoveryResList = udcHelper.listResources(resType);
            for (final DiscoveryResData res : discoveryResList) {
                String packageName = res.getPackageName();
                if (packageName == null) {
                    continue;
                }
                if (packageName.endsWith(".zip")) {
                    packageName = packageName.substring(0, packageName.length() - 4);
                }
                Map<DiscoveryResType, List<DiscoveryResData>> resourcesPerPackageMap = resGroup.computeIfAbsent(packageName, k -> new HashMap<>());
                List<DiscoveryResData> resourcePerPackage = resourcesPerPackageMap.computeIfAbsent(resType, k -> new ArrayList<>());
                resourcePerPackage.add(res);
            }
        }*/
        final String[] chosenPackageNames = this.showChooseResourceUI(resGroup);
        final List<String> loadedPackages = new ArrayList<>();
        if (chosenPackageNames != null) {
            for (final String packageName2 : chosenPackageNames) {
                if (root.findChild(packageName2) != null) {
                    loadedPackages.add(packageName2);
                }
            }
        }
        if (chosenPackageNames != null) {
            if (loadedPackages.size() != 0) {
                loadedPackages.sort(Comparator.naturalOrder());
                if (Messages.showOkCancelDialog(this.project, "This destination already contains  package(s) named " + loadedPackages.toString() + ". If any files have the same names, you will be asked if you want to replace those files. Do you still want to merge the folder(s)? ", "Confirmation", "Yes", "No", Messages.getInformationIcon()) == 0) {
                    this.pullInBackground(resGroup, chosenPackageNames, udcHelper);
                }
            } else {
                this.pullInBackground(resGroup, chosenPackageNames, udcHelper);
            }
        }
    }

    private void pullInBackground(final Map<String, Map<DiscoveryResType, List<DiscoveryResData>>> resGroup, final String[] chosenPackageNames, UDCHelper udcHelper) {
        final Task.Backgroundable task = new Task.Backgroundable(this.project, "Running", false, getPerformInBackgroundOption()) {

            public void onSuccess() {
                runWriteAction(() -> {
                    System.out.println("Begin write to disk...");
                    VirtualFileManager.getInstance().syncRefresh();
                    PopupUtil.showBalloonForActiveComponent("Done. Total " + chosenPackageNames.length + " packages have been downloaded.", MessageType.INFO);
                });
            }

            public void run(@NotNull final ProgressIndicator progressIndicator) {
                try {
                    progressIndicator.setText("Fetching file list...");
                    progressIndicator.setFraction(0.2);
                    for (int i = 0; i < chosenPackageNames.length; ++i) {
                        final String packageName = chosenPackageNames[i];
                        progressIndicator.setText("Downloading " + packageName);
                        downloadSinglePackage1(packageName, resGroup.get(packageName), progressIndicator, udcHelper);

                        ApplicationManager.getApplication().invokeLater(() -> root.getFileSystem().refresh(true));
                        progressIndicator.setFraction((i + 1.0) / chosenPackageNames.length);
                    }
                } catch (Exception e) {
                    PopupUtil.showBalloonForActiveFrame((e.getMessage() != null) ? e.getMessage() : "Error", MessageType.ERROR);
                    throw new RuntimeException(e);
                }
            }
        };
        ProgressManager.getInstance().run(task);
    }

    private String[] showChooseResourceUI(final Map<String, Map<DiscoveryResType, List<DiscoveryResData>>> resGroup) {
        final PackageList packageList = new PackageList(resGroup);
        packageList.setVisible(true);
        return packageList.getChosenPackages();
    }

    private void getRootFolder(final AnActionEvent e) {
        if (root == null) {
            final VirtualFile[] vFiles = e.getData(PlatformDataKeys.VIRTUAL_FILE_ARRAY);
            if (vFiles == null || vFiles.length != 1) {
                Messages.showWarningDialog("Only need one folder.", "Error");
            } else {
                root = vFiles[0];
            }
        }
    }

    private PerformInBackgroundOption getPerformInBackgroundOption() {
        return new PerformInBackgroundOption() {
            public boolean shouldStartInBackground() {
                return false;
            }

            public void processSentToBackground() {
            }
        };
    }

    private void connectSeverInBackground(final UDCHelper udcHelper, final DiscoveryResType[] resTypes) {
        final Task.Backgroundable task = new Task.Backgroundable(this.project, "Running", false, getPerformInBackgroundOption()) {
            // --Commented out by Inspection (3/1/2017 2:57 PM):int total1 = 0;

            Map<String, Map<DiscoveryResType, List<DiscoveryResData>>> resGroup;

            public void onSuccess() {
                //runWriteAction(() -> {
                download(resGroup, udcHelper);
                //});
            }

            public void run(@NotNull final ProgressIndicator progressIndicator) {
                try {
                    progressIndicator.setText("Connecting to the server and loading the data...");
                    resGroup = DownloadAction.buildResourcesPerPackageMap(udcHelper.listResources(resTypes));
                    /*final Map<String, Map<DiscoveryResType, List<DiscoveryResData>>> resGroup = new HashMap<>();
                    for (final DiscoveryResType resType : resTypes) {
                        final Collection<DiscoveryResData> discoveryResList = udcHelper.listResources(resType);
                        for (final DiscoveryResData res : discoveryResList) {
                            String packageName = res.getPackageName();
                            if (packageName == null) {
                                continue;
                            }
                            if (packageName.endsWith(".zip")) {
                                packageName = packageName.substring(0, packageName.length() - 4);
                            }
                            progressIndicator.setText("Connecting to the server and loading the data..." + packageName);
                            Map<DiscoveryResType, List<DiscoveryResData>> resourcesPerPackageMap = resGroup.computeIfAbsent(packageName, k -> new HashMap<>());
                            List<DiscoveryResData> resourcePerPackage = resourcesPerPackageMap.computeIfAbsent(resType, k -> new ArrayList<>());
                            resourcePerPackage.add(res);
                        }
                    }*/

                } catch (Exception e) {
                    PopupUtil.showBalloonForActiveFrame((e.getMessage() != null) ? e.getMessage() : "Error", MessageType.ERROR);
                    throw new RuntimeException(e);
                }
            }
        };
        ProgressManager.getInstance().run(task);
    }

    public void update(final AnActionEvent e) {
        final VirtualFile data = e.getData(PlatformDataKeys.VIRTUAL_FILE);
        if (data == null) {
            e.getPresentation().setEnabled(false);
            return;
        }
        final Project project = e.getProject();
        if (project == null) {
            e.getPresentation().setEnabled(false);
            return;
        }
        final Module moduleForFile = ModuleUtil.findModuleForFile(data, project);
        if (moduleForFile == null) {
            e.getPresentation().setEnabled(false);
            return;
        }
        final boolean x = isRoot(moduleForFile, data);
        e.getPresentation().setEnabled(x);

    }

// --Commented out by Inspection START (3/2/2017 2:39 PM):
//    static class TempData {
//        //resType, dataName, content, packageName
//        final DiscoveryResType resType;
//        final String dataName;
//        final Object content;
//        final String packageName;
//        final List<String> subFol;
//
//// --Commented out by Inspection START (3/2/2017 2:39 PM):
////        TempData(final DiscoveryResType resType, final String dataName, final Object content, final String packageName, final List<String> subFol) {
////            this.resType = resType;
////            this.dataName = dataName;
////            this.content = content;
////            this.packageName = packageName;
// --Commented out by Inspection STOP (3/2/2017 2:39 PM)
//            this.subFol = subFol;
//        }
// --Commented out by Inspection STOP (3/2/2017 2:39 PM)
}
