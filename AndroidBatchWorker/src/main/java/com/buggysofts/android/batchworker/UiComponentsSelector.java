package com.buggysofts.android.batchworker;

import android.view.View;

import androidx.annotation.NonNull;

public class UiComponentsSelector {
    private final View parentView;
    private final int titleResId;
    private final int labelResId;
    private final int progressBarResId;
    private final int progressDescResId;
    private final int cancelBtnResId;

    public UiComponentsSelector(@NonNull View parentView,
                                int titleResId,
                                int labelResId,
                                int progressBarResId,
                                int progressDescResId,
                                int cancelBtnResId) {
        this.parentView = parentView;
        this.titleResId = titleResId;
        this.labelResId = labelResId;
        this.progressBarResId = progressBarResId;
        this.progressDescResId = progressDescResId;
        this.cancelBtnResId = cancelBtnResId;
    }

    public View getParentView() {
        return parentView;
    }

    public int getTitleResId() {
        return titleResId;
    }

    public int getLabelResId() {
        return labelResId;
    }

    public int getProgressBarResId() {
        return progressBarResId;
    }

    public int getProgressDescResId() {
        return progressDescResId;
    }

    public int getCancelBtnResId() {
        return cancelBtnResId;
    }
}
