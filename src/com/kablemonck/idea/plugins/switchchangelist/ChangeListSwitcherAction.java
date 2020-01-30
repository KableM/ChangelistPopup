package com.kablemonck.idea.plugins.switchchangelist;

import com.intellij.icons.AllIcons;
import com.intellij.ide.actions.QuickSwitchSchemeAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.DumbAwareAction;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vcs.changes.ChangeListManager;
import com.intellij.openapi.vcs.changes.LocalChangeList;
import com.intellij.openapi.vcs.changes.actions.AddChangeListAction;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.Comparator;
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
        changeLists.sort(Comparator.comparing(LocalChangeList::getName));
        for (LocalChangeList changeList : changeLists) {
            boolean isDefaultChangeList = defaultChangeList.getName().equals(changeList.getName());
            Icon icon = isDefaultChangeList ? AllIcons.Actions.Forward : ourNotCurrentAction;
            group.add(new SwitchChangeListAction(project, changeList, icon));
        }

        group.add(new DumbAwareAction("New Changelist...", null, AllIcons.General.Add) {
            @Override
            public void actionPerformed(@NotNull AnActionEvent anActionEvent) {
                AddChangeListAction addChangeListAction = new AddChangeListAction();
                addChangeListAction.actionPerformed(anActionEvent);
            }
        });
    }
}
