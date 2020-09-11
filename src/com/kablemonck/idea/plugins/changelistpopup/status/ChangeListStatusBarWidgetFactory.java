package com.kablemonck.idea.plugins.changelistpopup.status;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Disposer;
import com.intellij.openapi.wm.StatusBar;
import com.intellij.openapi.wm.StatusBarWidget;
import com.intellij.openapi.wm.StatusBarWidgetFactory;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;

/**
 * @author Kable Monck (kable.monck@fincleartech.com.au)
 */
public class ChangeListStatusBarWidgetFactory implements StatusBarWidgetFactory {

    @NotNull
    @Override
    public String getId() {
        return ChangeListStatusBarWidget.class.getName();
    }

    @Nls
    @NotNull
    @Override
    public String getDisplayName() {
        return "Changelists";
    }

    @Override
    public boolean isAvailable(@NotNull Project project) {
        return true;
    }

    @NotNull
    @Override
    public StatusBarWidget createWidget(@NotNull Project project) {
        return new ChangeListStatusBarWidget(project);
    }

    @Override
    public void disposeWidget(@NotNull StatusBarWidget statusBarWidget) {
        Disposer.dispose(statusBarWidget);
    }

    @Override
    public boolean canBeEnabledOn(@NotNull StatusBar statusBar) {
        return true;
    }
}
