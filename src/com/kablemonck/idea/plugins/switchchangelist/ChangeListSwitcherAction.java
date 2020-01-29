package com.kablemonck.idea.plugins.switchchangelist;

import com.intellij.icons.AllIcons;
import com.intellij.ide.actions.QuickSwitchSchemeAction;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vcs.changes.ChangeListManager;
import com.intellij.openapi.vcs.changes.LocalChangeList;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.List;

/**
 * @author Kable Monck
 */
public class ChangeListSwitcherAction extends QuickSwitchSchemeAction implements DumbAware {

    @Override
    protected void fillActions(Project project, @NotNull DefaultActionGroup group, @NotNull DataContext dataContext) {
        ChangeListManager manager = ChangeListManager.getInstance(project);
        LocalChangeList defaultChangeList = manager.getDefaultChangeList();
        List<LocalChangeList> changeLists = manager.getChangeLists();
        for (LocalChangeList changeList : changeLists) {
            boolean isDefaultChangeList = defaultChangeList.getName().equals(changeList.getName());
            Icon icon = isDefaultChangeList ? AllIcons.Actions.Forward : ourNotCurrentAction;
            group.add(new SwitchChangeListAction(project, changeList, icon));
        }
    }
}
