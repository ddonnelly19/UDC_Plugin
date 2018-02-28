package com.hpe.ucmdb.udc.action;

import com.hp.ucmdb.api.discovery.types.DiscoveryResType;
import com.hpe.ucmdb.udc.*;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.progress.PerformInBackgroundOption;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.MessageType;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.ui.popup.util.PopupUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.wm.StatusBar;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class PushResources extends AnAction {
    public void actionPerformed(final AnActionEvent event) {
        final Project project = event.getData(PlatformDataKeys.PROJECT);
        final VirtualFile[] vFiles = event.getData(PlatformDataKeys.VIRTUAL_FILE_ARRAY);
        if (ServerConfigManager.getInstance().getDefaultServerConfig(project) == null) {
            return;
        }
        if (vFiles == null || vFiles.length == 0) {
            Messages.showWarningDialog("No file to be review.", "Error");
            return;
        }
        ApplicationManager.getApplication().runWriteAction(() -> FileDocumentManager.getInstance().saveAllDocuments());
        final List<String> vfName = new ArrayList<>();
        for (final VirtualFile vf : vFiles) {
            vfName.add(vf.getName());
        }
        final String defaultServer = UDCSettings.getSettings(project).getState().defaultServer;
        final String confirm = "Are you sure that you want to push " + vfName + " to server " + defaultServer + " ?";
        int conf;
        conf = Messages.showOkCancelDialog(project, confirm, "Push Resource to Server", "Yes", "No", Messages.getInformationIcon());
        if (conf == 0) {
            try {
                ApplicationManager.getApplication().runReadAction(() -> {
                    final int total = 0;
                    try {
                        StatusBar.Info.set("Pushing...", project);
                        PushResources.this.push(project, vFiles);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Messages.showErrorDialog(e.getMessage(), "Error");
                        StatusBar.Info.set("Push error." + e.getMessage(), project);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                Messages.showErrorDialog(e.getMessage(), "Error");
            }
        }
    }

    private void push(final Project project, final VirtualFile[] vFiles) {
        final String[] msg = {""};
        //final boolean[] isOK = { false };
        final float[] finished = {0.0f};
        final ServerConfig serverConfig = ServerConfigManager.getInstance().getDefaultServerConfig(project);
        if (serverConfig == null) {
            return;
        }
        final Task.Backgroundable task = new Task.Backgroundable(project, "Pushing to " + serverConfig.getServer(), false, new PerformInBackgroundOption() {
            public boolean shouldStartInBackground() {
                return true;
            }

            public void processSentToBackground() {
            }
        }) {
            public void onSuccess() {
                if (!msg[0].isEmpty()) {
                    PopupUtil.showBalloonForActiveComponent("Error:" + msg[0], MessageType.ERROR);
                } else {
                    StatusBar.Info.set("Done. Total " + finished[0] + " files were uploaded.", project);
                    PopupUtil.showBalloonForActiveComponent("Done. Total " + (int) finished[0] + " files were uploaded.", MessageType.INFO);
                }
            }

            public void run(@NotNull final ProgressIndicator progressIndicator) {
                final List<VirtualFile> allFiles = PushResources.this.getVirtualFilesByRecursive(vFiles);
                final Runnable runnable = () -> {
                    try {
                        final UDCHelper UDCHelper = new UDCHelper(project, ServerConfigManager.getInstance().getDefaultServerConfig(project));
                        final ProgressIndicator pi = ProgressManager.getInstance().getProgressIndicator();
                        final float total = allFiles.size();
                        for (final VirtualFile vf : allFiles) {
                            final Object[] resourceName = ResTypeUtil.getResourceName(vf);
                            if (resourceName == null) {
                                break;
                            }
                            final DiscoveryResType x = (DiscoveryResType) resourceName[0];
                            final String y = (String) resourceName[1];
                            UDCHelper.setContent(x, y, vf.contentsToByteArray());
                            final int n = 0;
                            ++finished[n];
                            pi.setText(vf.getName());
                            pi.setFraction((double) (finished[0] / total));
                        }
                        //isOK[0] = true;
                    } catch (Exception e) {
                        msg[0] = e.getMessage();
                        throw new RuntimeException(e);
                    }
                };
                runnable.run();
            }
        };
        ProgressManager.getInstance().run(task);
    }

    @NotNull
    private List<VirtualFile> getVirtualFilesByRecursive(final VirtualFile[] vFiles) {
        final List<VirtualFile> allFiles = new ArrayList<>();
        for (final VirtualFile vf : vFiles) {
            if (vf.isDirectory()) {
                final VirtualFile[] children = vf.getChildren();
                allFiles.addAll(this.getVirtualFilesByRecursive(children));
            } else {
                allFiles.add(vf);
            }
        }
        return allFiles;
    }

    public void update(final AnActionEvent e) {
        boolean x;
        final VirtualFile[] vFiles = e.getData(PlatformDataKeys.VIRTUAL_FILE_ARRAY);
        final Project project = e.getData(PlatformDataKeys.PROJECT);
        final float[] finished1 = {0.0f};
        if (vFiles == null) {
            e.getPresentation().setEnabled(false);
            return;
        }
        final List<VirtualFile> allFiles = this.getVirtualFilesByRecursive(vFiles);
        for (final VirtualFile vf : allFiles) {
            final Object[] resourceName = ResTypeUtil.getResourceName(vf);
            if (resourceName == null) {
                break;
            }
            final int n = 0;
            ++finished1[n];
        }
        x = (finished1[0] > 0.0f);
        e.getPresentation().setEnabled(x);
    }
}