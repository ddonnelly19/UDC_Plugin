package com.hpe.ucmdb.udc;

import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class UDCProject implements ProjectComponent {
    private final Project project;

    public UDCProject(final Project project) {
        this.project = project;
    }

    public void initComponent() {
    }

    public void disposeComponent() {
    }

    @NotNull
    public String getComponentName() {
        return "UDCProject";
    }

    public void projectOpened() {
        StatusBarUtil.setDefaultServerStatus(this.project);
        this.syncPool();
    }

    private void syncPool() {
        System.out.println("Sync pool");
        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    UcmdbClient.refreshPool();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        }, TimeUnit.SECONDS.toMillis(10L), TimeUnit.MINUTES.toMillis(10L));
    }

    public void projectClosed() {
    }
}