package com.hpe.ucmdb.udc.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.options.ShowSettingsUtil;
import com.intellij.openapi.project.Project;

public class Settings extends AnAction {
    public void actionPerformed(final AnActionEvent e) {
        final Project project = e.getData(PlatformDataKeys.PROJECT);
        ShowSettingsUtil.getInstance().showSettingsDialog(project, "UDC Configuration");
    }
}