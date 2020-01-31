package com.kablemonck.idea.plugins.changelistpopup;

import com.intellij.icons.AllIcons;
import com.intellij.ide.actions.QuickSwitchSchemeAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.DumbAwareAction;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.vcs.changes.ChangeListManager;
import com.intellij.openapi.vcs.changes.LocalChangeList;
import com.intellij.openapi.vcs.changes.actions.AddChangeListAction;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.List;

/**
 * @author Kable Monck
 */
public class ChangeListPopupAction extends QuickSwitchSchemeAction implements DumbAware {

    @Override
    protected void fillActions(Project project, @NotNull DefaultActionGroup group, @NotNull DataContext dataContext) {
        addActions(project, group);
    }

    public static void addActions(Project project, @NotNull DefaultActionGroup group) {
        ChangeListManager manager = ChangeListManager.getInstance(project);
        LocalChangeList defaultChangeList = manager.getDefaultChangeList();
        List<LocalChangeList> changeLists = manager.getChangeLists();
        changeLists.sort(Comparator.comparing(LocalChangeList::getName));
        for (LocalChangeList changeList : changeLists) {
            boolean isDefaultChangeList = defaultChangeList.getName().equals(changeList.getName());
            group.add(new ChangeListActionGroup(project, changeList, isDefaultChangeList));
        }

        group.addSeparator();
        group.add(new DumbAwareAction("New Changelist...", null, AllIcons.General.Add) {
            @Override
            public void actionPerformed(@NotNull AnActionEvent anActionEvent) {
                AddChangeListAction addChangeListAction = new AddChangeListAction();
                addChangeListAction.actionPerformed(anActionEvent);
            }
        });
    }

    @Override
    protected JBPopupFactory.ActionSelectionAid getAidMethod() {
        return JBPopupFactory.ActionSelectionAid.SPEEDSEARCH;
    }
}
