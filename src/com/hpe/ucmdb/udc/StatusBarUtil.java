package com.hpe.ucmdb.udc;

import com.intellij.concurrency.JobScheduler;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.popup.ListPopup;
import com.intellij.openapi.ui.popup.ListPopupStep;
import com.intellij.openapi.ui.popup.PopupStep;
import com.intellij.openapi.ui.popup.util.BaseListPopupStep;
import com.intellij.openapi.wm.StatusBar;
import com.intellij.openapi.wm.StatusBarWidget;
import com.intellij.openapi.wm.WindowManager;
import com.intellij.ui.popup.list.ListPopupImpl;
import com.intellij.util.Consumer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.event.MouseEvent;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

class StatusBarUtil {
    // --Commented out by Inspection (3/1/2017 3:04 PM):public static final String DEFAULT_SERVER = "UDC Default Server";

// --Commented out by Inspection START (3/1/2017 3:04 PM):
//    public static void setDefaultServerStatus(final Project project, final UDCSettings.UDCState state) {
//        final StatusBar statusBar = WindowManager.getInstance().getStatusBar(project);
//        assert statusBar != null;
//        DefaultServerStatusBarWidget widget = (DefaultServerStatusBarWidget)statusBar.getWidget("UDC Default Server");
//        if (widget == null) {
//            widget = new DefaultServerStatusBarWidget(state);
//            statusBar.addWidget((StatusBarWidget)widget);
//        }
//        widget.setDefaultServer();
//    }
// --Commented out by Inspection STOP (3/1/2017 3:04 PM)

    public static void setDefaultServerStatus(final Project project) {
        final UDCSettings.State state = UDCSettings.getSettings().getState();
        final StatusBar statusBar = WindowManager.getInstance().getStatusBar(project);
        assert statusBar != null;
        DefaultServerStatusBarWidget widget = (DefaultServerStatusBarWidget) statusBar.getWidget("UDC Default Server");
        if (widget == null) {
            widget = new DefaultServerStatusBarWidget(state);
            statusBar.addWidget(widget);
        }
        widget.setDefaultServer();
    }

    static class DefaultServerStatusBarWidget implements StatusBarWidget {
        private final UDCSettings.State state;
        StatusBar statusBar;
        private ScheduledFuture<?> myFuture;

        public DefaultServerStatusBarWidget(final UDCSettings.State state) {
            this.state = state;
            this.myFuture = JobScheduler.getScheduler().scheduleWithFixedDelay(DefaultServerStatusBarWidget.this::setDefaultServer, 1L, 5L, TimeUnit.SECONDS);
            ApplicationManager.getApplication().getMessageBus().connect(this).subscribe(DefaultServerChangeListener.TOPIC, DefaultServerStatusBarWidget.this::setDefaultServer);
        }

        @NotNull
        public String ID() {
            //final String s = "UDC Default Server";
            //if (s == null) {
            //    throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", "com/hpe/ucmdb/udc/StatusBarUtil$DefaultServerStatusBarWidget", "ID"));
            //}
            return "UDC Default Server";
        }

        @Nullable
        public StatusBarWidget.WidgetPresentation getPresentation(@NotNull final StatusBarWidget.PlatformType platformType) {
            //if (platformType == null) {
            //    throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", "platformType", "com/hpe/ucmdb/udc/StatusBarUtil$DefaultServerStatusBarWidget", "getPresentation"));
            //}
            return new MultipleTextValuesPresentation() {
                @NotNull
                public ListPopup getPopupStep() {
                    String[] options = DefaultServerStatusBarWidget.this.state.serverConfigs.keySet().toArray(new String[0]);
                    if (options.length == 0) {
                        options = new String[]{"NA"};
                    }
                    final ListPopupStep<String> step = new BaseListPopupStep<String>("DefaultServer", options) {
                        public PopupStep onChosen(final String selectedValue, final boolean finalChoice) {
                            if (finalChoice) {
                                DefaultServerStatusBarWidget.this.state.defaultServer = selectedValue;
                                DefaultServerStatusBarWidget.this.statusBar.updateWidget(DefaultServerStatusBarWidget.this.ID());
                                ApplicationManager.getApplication().getMessageBus().syncPublisher(DefaultServerChangeListener.TOPIC).defaultServerChanged();
                            }
                            return super.onChosen(selectedValue, finalChoice);
                        }
                    };
                    return new ListPopupImpl(step);
                }

                @NotNull
                public String getSelectedValue() {
                    return "UDC: " + DefaultServerStatusBarWidget.this.state.defaultServer;
                }

                @NotNull
                @Override
                public String getMaxValue() {
                    return "1234567890";
                }

                //@NotNull
                //public String getMaxValue() {
                //    final String s = "1234567890";
                //    if (s == null) {
                //        throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", "com/hpe/ucmdb/udc/StatusBarUtil$DefaultServerStatusBarWidget$3", "getMaxValue"));
                //    }
                //    return s;
                //}

                @Nullable
                public String getTooltipText() {
                    return null;
                }

                @Nullable
                public Consumer<MouseEvent> getClickConsumer() {
                    return null;
                }
            };
        }

        public void install(@NotNull final StatusBar statusBar) {
            //if (statusBar == null) {
            //    throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", "statusBar", "com/hpe/ucmdb/udc/StatusBarUtil$DefaultServerStatusBarWidget", "install"));
            //}
            this.statusBar = statusBar;
        }

        public void setDefaultServer() {
            this.statusBar.updateWidget(this.ID());
        }

        public void dispose() {
            if (this.myFuture != null) {
                this.myFuture.cancel(true);
                this.myFuture = null;
            }
        }
    }
}