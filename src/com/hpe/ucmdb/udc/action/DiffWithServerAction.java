package com.hpe.ucmdb.udc.action;

import com.hp.ucmdb.api.discovery.types.DiscoveryResType;
import com.hpe.ucmdb.udc.*;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.diff.*;
import com.intellij.openapi.progress.PerformInBackgroundOption;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.MessageType;
import com.intellij.openapi.ui.popup.util.PopupUtil;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;

public class DiffWithServerAction extends AnAction {
    private static boolean isRoot(final Object[] resourceName) {
        return resourceName != null;
    }

    public void actionPerformed(final AnActionEvent e) {
        final VirtualFile file = e.getData(PlatformDataKeys.VIRTUAL_FILE_ARRAY)[0];
        @SuppressWarnings("deprecation") final DiffContent local = new FileContent(e.getProject(), file);
        final ServerConfig serverConfig = ServerConfigManager.getInstance().getDefaultServerConfig(e.getProject());
        if (serverConfig == null) {
            return;
        }
        final UDCHelper helper = new UDCHelper(e.getProject(), serverConfig);
        final Object[] resourceName = ResTypeUtil.getResourceName(file);
        if (resourceName == null) {
            PopupUtil.showBalloonForActiveComponent("Unknown type: " + file.getPath() + ".", MessageType.ERROR);
        } else {
            this.diff(e.getProject(), file, local, helper, resourceName);
        }
    }

    private void diff(final Project project, final VirtualFile file, @SuppressWarnings("deprecation") final DiffContent local, final UDCHelper helper, final Object[] resourceName) {
        final Task.Backgroundable task = new Task.Backgroundable(project, "Diffing @" + ServerConfigManager.getInstance().getDefaultServerConfig(project).getServer(), false, new PerformInBackgroundOption() {
            public boolean shouldStartInBackground() {
                return true;
            }

            public void processSentToBackground() {
            }
        }) {
            byte[] content;

            public void onSuccess() {
                if (this.content != null) {
                    @SuppressWarnings("deprecation") final DiffContent server = new SimpleContent(new String(this.content));
                    server.getDocument().setReadOnly(false);
                    //noinspection deprecation
                    @SuppressWarnings("deprecation") final DiffRequest dr = new DiffRequest(project) {
                        @NotNull
                        public DiffContent[] getContents() {
                            @SuppressWarnings("deprecation") final DiffContent[] array = {local, server};
                            if (array == null) {
                                throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", "com/hpe/ucmdb/udc/action/DiffWithServerAction$2$1", "getContents"));
                            }
                            return array;
                        }

                        public String[] getContentTitles() {
                            final String defaultServer = UDCSettings.getSettings(project).getState().defaultServer;
                            return new String[]{"Local", "Server : " + defaultServer};
                        }

                        public String getWindowTitle() {
                            return file.getName();
                        }
                    };
                    //noinspection deprecation
                    dr.addHint(DiffTool.HINT_SHOW_NOT_MODAL_DIALOG);
                    dr.setOnOkRunnable(() -> {
                        try {
                            helper.setContent((DiscoveryResType) resourceName[0], (String) resourceName[1], server.getDocument().getText().getBytes());
                            PopupUtil.showBalloonForActiveFrame("Update server successfully.", MessageType.INFO);
                        } catch (Exception e) {
                            PopupUtil.showBalloonForActiveFrame("Update server failure.", MessageType.ERROR);
                        }
                    });
                    //noinspection deprecation
                    DiffManager.getInstance().getDiffTool().show(dr);
                } else {
                    PopupUtil.showBalloonForActiveComponent("Can't get content from remote server.", MessageType.ERROR);
                }
            }

            public void run(@NotNull final ProgressIndicator progressIndicator) {
                progressIndicator.setIndeterminate(true);
                try {
                    this.content = helper.getContent((DiscoveryResType) resourceName[0], (String) resourceName[1]);
                } catch (Exception e1) {
                    e1.printStackTrace();
                    PopupUtil.showBalloonForActiveComponent("No such file in server.", MessageType.ERROR);
                    this.content = "".getBytes();
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
        final VirtualFile file = e.getData(PlatformDataKeys.VIRTUAL_FILE_ARRAY)[0];
        final Object[] resourceName = ResTypeUtil.getResourceName(file);
        final boolean x = isRoot(resourceName);
        e.getPresentation().setEnabled(x);
    }
}