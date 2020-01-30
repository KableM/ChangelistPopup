package com.kablemonck.idea.plugins.changelistpopup;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.project.DumbAwareAction;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.IconLoader;
import com.intellij.openapi.vcs.VcsDataKeys;
import com.intellij.openapi.vcs.changes.ChangeListManager;
import com.intellij.openapi.vcs.changes.LocalChangeList;
import com.intellij.openapi.vcs.changes.actions.RemoveChangeListAction;
import com.intellij.openapi.vcs.changes.ui.EditChangelistDialog;
import com.intellij.util.ui.EmptyIcon;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * @author Kable Monck
 */
public class ChangeListActionGroup extends DefaultActionGroup {

    private final Project project;
    private final LocalChangeList changeList;
    private final ChangeListManager changeListManager;

    protected static final Icon ourNotCurrentAction = new IconLoader.LazyIcon() {
        @NotNull
        protected Icon compute() {
            return EmptyIcon.create(AllIcons.Actions.Forward.getIconWidth(), AllIcons.Actions.Forward.getIconHeight());
        }
    };

    public ChangeListActionGroup(Project project, LocalChangeList changeList, boolean isActiveChangelist) {
        super(changeList.getName(), true);
        this.project = project;
        this.changeList = changeList;
        changeListManager = ChangeListManager.getInstance(project);
        getTemplatePresentation().setIcon(isActiveChangelist ? AllIcons.Actions.Forward : ourNotCurrentAction);

        if (!isActiveChangelist) {
            DumbAwareAction setActiveAction = new DumbAwareAction("Set Active", null, AllIcons.Actions.Selectall) {
                @Override
                public void actionPerformed(@NotNull AnActionEvent anActionEvent) {
                    changeListManager.setDefaultChangeList(changeList);
                }
            };
            add(setActiveAction);
        }

        DumbAwareAction editAction = new DumbAwareAction("Edit...", null, AllIcons.Actions.Edit) {
            @Override
            public void actionPerformed(@NotNull AnActionEvent anActionEvent) {
                new EditChangelistDialog(project, changeList).show();
            }
        };
        DumbAwareAction removeAction = new DumbAwareAction("Remove...", null, AllIcons.General.Remove) {
            @Override
            public void actionPerformed(@NotNull AnActionEvent anActionEvent) {
                DataContext dataContext = new DataContext() {
                    @Nullable
                    @Override
                    public Object getData(@NotNull String dataId) {
                        if (CommonDataKeys.PROJECT.is(dataId)) {
                            return project;
                        } else if (VcsDataKeys.CHANGE_LISTS.is(dataId)) {
                            return new LocalChangeList[]{changeList};
                        }
                        return null;
                    }
                };
                RemoveChangeListAction action = new RemoveChangeListAction();

                action.actionPerformed(AnActionEvent.createFromDataContext("", null, dataContext));
            }
        };

        addAll(editAction, removeAction);
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent anActionEvent) {
        ChangeListManager manager = ChangeListManager.getInstance(project);
        manager.setDefaultChangeList(changeList);
    }
}
