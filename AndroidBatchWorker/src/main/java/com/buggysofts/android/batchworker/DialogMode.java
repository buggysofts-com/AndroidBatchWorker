package com.buggysofts.android.batchworker;

public enum DialogMode {
    MODE_CLASSIC(
        R.layout.dialog_layout_classic,
        R.id.dialog_title,
        R.id.current_label,
        R.id.progress_bar_batch_progress,
        R.id.current_progress_desc,
        -1
    ),
    MODE_BOTTOM_SHEET(
        R.layout.dialog_layout_bottom_sheet,
        R.id.dialog_title,
        R.id.current_label,
        R.id.progress_bar_batch_progress,
        R.id.current_progress_desc,
        R.id.btn_cancel_tasks
    );

    private final int dialogLayoutResId;
    private final int dialogTitleResId;
    private final int labelResId;
    private final int progressBarResId;
    private final int progressDescResId;
    private final int cancelBtnResId;

    DialogMode(int dialogLayout,
               int dialogTitleResId,
               int labelResId,
               int progressBarResId,
               int progressDescResId,
               int cancelBtnResId) {
        this.dialogLayoutResId = dialogLayout;
        this.dialogTitleResId = dialogTitleResId;
        this.labelResId = labelResId;
        this.progressBarResId = progressBarResId;
        this.progressDescResId = progressDescResId;
        this.cancelBtnResId = cancelBtnResId;
    }

    public int getDialogLayoutResId() {
        return dialogLayoutResId;
    }

    public int getDialogTitleResId() {
        return dialogTitleResId;
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
