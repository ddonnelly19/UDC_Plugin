package com.hpe.ucmdb.udc.action;

import com.hpe.ucmdb.udc.ServerConfig;
import com.hpe.ucmdb.udc.ServerConfigManager;
import com.hpe.ucmdb.udc.UDCSettings;
import com.hpe.ucmdb.udc.UcmdbClient;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.progress.PerformInBackgroundOption;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.MessageType;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.ui.popup.util.PopupUtil;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public class DeployPackage extends AnAction {
    // --Commented out by Inspection (3/1/2017 3:04 PM):private Project project;
    private UcmdbClient ucmdbClient;
    private VirtualFile root;
    private Project project;
    // --Commented out by Inspection (3/1/2017 3:04 PM):private final UDCSettings.ServerSetting serverSetting;

    public DeployPackage() {
        this.ucmdbClient = null;
        this.root = null;
        UDCSettings.ServerSetting serverSetting = new UDCSettings.ServerSetting();
    }

    public void actionPerformed(final AnActionEvent e) {
        this.project = e.getProject();
        this.root = e.getData(PlatformDataKeys.VIRTUAL_FILE);
        final ServerConfig serverConfig = ServerConfigManager.getInstance().getDefaultServerConfig(e.getProject());
        if (serverConfig == null) {
            return;
        }
        if (!this.root.getName().endsWith(".zip")) {
            Messages.showErrorDialog("Only ZIP files are allowed to be deployed to the remote server.", "Error");
            return;
        }
        final int i = this.confirm(project);
        if (i == 0) {
            try {
                this.deployInBackground();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }

    private int confirm(final Project project) {
        final String defaultServer = UDCSettings.getSettings(project).getState().defaultServer;
        return Messages.showOkCancelDialog(project, "Are you sure you want to deploy the ZIP file '" + this.root.getName() + "' to the remote server " + defaultServer + " ?", "Confirmation", "Yes", "No", Messages.getInformationIcon());
    }

    private void prompt(final Project project) {
        final String defaultServer = UDCSettings.getSettings(project).getState().defaultServer;
        Messages.showMessageDialog(project, "The ZIP file '" + this.root.getName() + "' has been deployed to server " + defaultServer + " successfully.", "Information", Messages.getInformationIcon());
    }

    private void deployInBackground() {
        final Task.Backgroundable task = new Task.Backgroundable(this.project, "Running", false, new PerformInBackgroundOption() {
            public boolean shouldStartInBackground() {
                return false;
            }

            public void processSentToBackground() {
            }
        }) {
            // --Commented out by Inspection (3/1/2017 3:04 PM):int total1 = 0;

            public void onSuccess() {
                ApplicationManager.getApplication().runWriteAction(() -> DeployPackage.this.prompt(DeployPackage.this.project));
            }

            public void run(@NotNull final ProgressIndicator progressIndicator) {
                try {
                    final String defaultServer = UDCSettings.getSettings(DeployPackage.this.project).getState().defaultServer;
                    progressIndicator.setText("Deploying the ZIP file '" + DeployPackage.this.root.getName() + "' to server '" + defaultServer + "' ...");
                    DeployPackage.this.ucmdbClient = UcmdbClient.getUcmdbClient(DeployPackage.this.project);
                    final File debug = new File(DeployPackage.this.root.getPath());
                    DeployPackage.this.ucmdbClient.deployPackage(debug);
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
        boolean x = true;
        if (!data.getName().endsWith(".zip")) {
            x = false;
        }
        e.getPresentation().setEnabled(x);
    }
}