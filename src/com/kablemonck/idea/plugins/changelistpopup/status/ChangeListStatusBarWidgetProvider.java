package com.kablemonck.idea.plugins.changelistpopup.status;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.StatusBar;
import com.intellij.openapi.wm.StatusBarWidget;
import com.intellij.openapi.wm.StatusBarWidgetProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author Kable Monck (kable.monck@fincleartech.com.au)
 */
public class ChangeListStatusBarWidgetProvider implements StatusBarWidgetProvider {

    @Nullable
    @Override
    public StatusBarWidget getWidget(@NotNull Project project) {
        return new ChangeListStatusBarWidget(project);
    }

    @NotNull
    @Override
    public String getAnchor() {
        return StatusBar.Anchors.before(StatusBar.StandardWidgets.ENCODING_PANEL);
    }
}
