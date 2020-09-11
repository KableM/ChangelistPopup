package com.kablemonck.idea.plugins.changelistpopup;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.ActionPlaces;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.project.DumbAwareAction;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.IconLoader;
import com.intellij.openapi.vcs.VcsDataKeys;
import com.intellij.openapi.vcs.actions.CommonCheckinProjectAction;
import com.intellij.openapi.vcs.changes.Change;
import com.intellij.openapi.vcs.changes.ChangeListManager;
import com.intellij.openapi.vcs.changes.LocalChangeList;
import com.intellij.openapi.vcs.changes.actions.MoveChangesToAnotherListAction;
import com.intellij.openapi.vcs.changes.actions.RemoveChangeListAction;
import com.intellij.openapi.vcs.changes.actions.RenameChangeListAction;
import com.intellij.openapi.vcs.changes.actions.RollbackAction;
import com.intellij.openapi.vcs.changes.actions.SetDefaultChangeListAction;
import com.intellij.openapi.vcs.changes.actions.diff.ShowDiffAction;
import com.intellij.openapi.vcs.changes.shelf.ShelveChangesAction;
import com.intellij.util.ui.EmptyIcon;
import com.kablemonck.idea.plugins.changelistpopup.util.TextUtil;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.function.Supplier;

/**
 * @author Kable Monck
 */
public class ChangeListActionGroup extends DefaultActionGroup {

    private final Project project;
    private final LocalChangeList changeList;

    protected static final Icon ourNotCurrentAction = new IconLoader.LazyIcon() {
        @NotNull
        protected Icon compute() {
            return EmptyIcon.create(AllIcons.Actions.Forward.getIconWidth(), AllIcons.Actions.Forward.getIconHeight());
        }
    };

    public ChangeListActionGroup(Project project, LocalChangeList changeList, boolean isActiveChangelist) {
        super(getActionName(changeList), true);
        this.project = project;
        this.changeList = changeList;
        getTemplatePresentation().setIcon(isActiveChangelist ? AllIcons.Actions.Forward : ourNotCurrentAction);

        if (!isActiveChangelist) {
            DumbAwareAction setActiveAction = createAction(project, changeList, "Set Active", AllIcons.Actions.Selectall, SetDefaultChangeListAction::new);
            add(setActiveAction);
        }

        if (!changeList.getChanges().isEmpty()) {
            DumbAwareAction commitAction = createAction(project, changeList, "Commit...", AllIcons.Actions.Commit, CommonCheckinProjectAction::new);
            DumbAwareAction rollbackAction = createAction(project, changeList, "Rollback...", AllIcons.Actions.Rollback, RollbackAction::new);
            DumbAwareAction moveChangesAction = createAction(project, changeList, "Move Changes...", null, MoveChangesToAnotherListAction::new);
            DumbAwareAction showDiffAction = createAction("Show Diff", AllIcons.Actions.Diff, () -> new ShowDiffAction().actionPerformed(actionEventFromChangeList(project, changeList)));
            DumbAwareAction shelveAction = createAction(project, changeList, "Shelve Changes...", AllIcons.Vcs.Shelve, ShelveChangesAction::new);
            addAll(commitAction, rollbackAction, moveChangesAction, showDiffAction, shelveAction);
            addSeparator();
        }

        DumbAwareAction editAction = createAction(project, changeList, "Edit...", AllIcons.Actions.Edit, RenameChangeListAction::new);
        DumbAwareAction removeAction = createAction(project, changeList, "Remove...", AllIcons.General.Remove, RemoveChangeListAction::new);
        addAll(editAction, removeAction);
    }

    private static DumbAwareAction createAction(Project project,
                                                LocalChangeList changeList,
                                                String text,
                                                Icon icon,
                                                Supplier<AnAction> underlyingActionSupplier) {
        return createAction(text, icon, () -> {
            AnAction underlyingAction = underlyingActionSupplier.get();
            underlyingAction.actionPerformed(actionEventFromChangeList(project, changeList));
        });

    }

    private static DumbAwareAction createAction(String text, Icon icon, Runnable perform) {
        return new DumbAwareAction(text, null, icon) {
            @Override
            public void actionPerformed(@NotNull AnActionEvent anActionEvent) {
                perform.run();
            }
        };
    }

    private static AnActionEvent actionEventFromChangeList(Project project, LocalChangeList changeList) {
        DataContext dataContext = dataId -> {
            if (CommonDataKeys.PROJECT.is(dataId)) {
                return project;
            } else if (VcsDataKeys.CHANGE_LISTS.is(dataId)) {
                return new LocalChangeList[]{changeList};
            } else if (VcsDataKeys.CHANGES.is(dataId)) {
                return changeList.getChanges().toArray(new Change[]{});
            }
            return null;
        };
        return AnActionEvent.createFromDataContext(ActionPlaces.POPUP, null, dataContext);
    }

    private static String getActionName(LocalChangeList changeList) {
        String fullName = changeList.getName();
        int files = changeList.getChanges().size();
        if (files == 1) {
            fullName += " (1 file)";
        } else if (files > 1) {
            fullName += String.format(" (%d files)", files);
        }
        return TextUtil.wrap(fullName, 50, TextUtil.TextWrapStrategy.MIDDLE);
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent anActionEvent) {
        ChangeListManager manager = ChangeListManager.getInstance(project);
        manager.setDefaultChangeList(changeList);
    }
}
