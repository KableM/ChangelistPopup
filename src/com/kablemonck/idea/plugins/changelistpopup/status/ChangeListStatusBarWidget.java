package com.kablemonck.idea.plugins.changelistpopup.status;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.actionSystem.impl.SimpleDataContext;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.ui.popup.ListPopup;
import com.intellij.openapi.vcs.changes.ChangeList;
import com.intellij.openapi.vcs.changes.ChangeListListener;
import com.intellij.openapi.vcs.changes.ChangeListManager;
import com.intellij.openapi.wm.IdeFocusManager;
import com.intellij.openapi.wm.StatusBarWidget;
import com.intellij.openapi.wm.impl.status.EditorBasedWidget;
import com.intellij.util.Consumer;
import com.kablemonck.idea.plugins.changelistpopup.ChangeListPopupAction;
import com.kablemonck.idea.plugins.changelistpopup.util.TextUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;

/**
 * @author Kable Monck (kable.monck@fincleartech.com.au)
 */
public class ChangeListStatusBarWidget extends EditorBasedWidget implements StatusBarWidget.MultipleTextValuesPresentation {

    private final ChangeListManager changeListManager;
    private String text;
    private String tooltipText;

    public ChangeListStatusBarWidget(Project project) {
        super(project);
        changeListManager = ChangeListManager.getInstance(myProject);
        changeListManager.addChangeListListener(new ChangeListListener() {
            @Override
            public void defaultListChanged(ChangeList oldDefaultList, ChangeList newDefaultList) {
                repaint();
            }

            @Override
            public void changeListRenamed(ChangeList list, String oldName) {
                repaint();
            }
        });
        repaint();
    }

    private void repaint() {
        String changeListName = changeListManager.getDefaultListName();
        text = TextUtil.wrap(changeListName, 50);
        tooltipText = "Active Changelist: " + changeListName;
        ApplicationManager.getApplication().invokeLater(() -> {
            if (myStatusBar != null) {
                myStatusBar.updateWidget(ID());
            }
        });
    }

    @Nullable
    @Override
    public WidgetPresentation getPresentation() {
        return this;
    }

    @Nullable("null means the widget is unable to show the popup")
    @Override
    public ListPopup getPopupStep() {
        JBPopupFactory.ActionSelectionAid aid = JBPopupFactory.ActionSelectionAid.SPEEDSEARCH;
        DefaultActionGroup group = new DefaultActionGroup();
        ChangeListPopupAction.addActions(myProject, group);
        DataContext projectContext = SimpleDataContext.getProjectContext(myProject);
        Component component = IdeFocusManager.getInstance(myProject).getFocusOwner();
        DataContext dataContext = SimpleDataContext.getSimpleContext(PlatformDataKeys.CONTEXT_COMPONENT, component, projectContext);

        ListPopup popup = JBPopupFactory.getInstance().createActionGroupPopup("Changelists", group, dataContext, aid, true);
        return popup;
    }

    @Nullable
    @Override
    public String getSelectedValue() {
        return text;
    }

    @Nullable
    @Override
    public String getTooltipText() {
        return tooltipText;
    }

    @Nullable
    @Override
    public Consumer<MouseEvent> getClickConsumer() {
        return null;
    }

    @Nullable
    @Override
    public Icon getIcon() {
        return AllIcons.Vcs.Changelist;
    }

    @NotNull
    @Override
    public String ID() {
        return ChangeListStatusBarWidget.class.getName();
    }
}
