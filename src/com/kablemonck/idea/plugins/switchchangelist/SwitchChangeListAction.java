package com.kablemonck.idea.plugins.switchchangelist;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.DumbAwareAction;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vcs.changes.ChangeListManager;
import com.intellij.openapi.vcs.changes.LocalChangeList;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 * @author Kable Monck
 */
public class SwitchChangeListAction extends DumbAwareAction {

    private final Project project;
    private final LocalChangeList changeList;

    public SwitchChangeListAction(Project project, LocalChangeList changeList, Icon icon) {
        super(changeList.getName(), null, icon);
        this.project = project;
        this.changeList = changeList;
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent anActionEvent) {
        ChangeListManager manager = ChangeListManager.getInstance(project);
        manager.setDefaultChangeList(changeList);
    }
}
