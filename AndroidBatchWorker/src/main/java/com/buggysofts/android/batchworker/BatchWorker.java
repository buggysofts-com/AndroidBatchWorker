package com.buggysofts.android.batchworker;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.UiContext;
import androidx.appcompat.app.AlertDialog;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class BatchWorker<T, V> {
    // constructor passed components
    private final Context context;
    private final List<T> dataItems;
    private final DialogMode dialogMode;
    private final WorkerCallBack<T, V> workerCallBack;

    // dialog refs
    private AlertDialog classicDialog;
    private BottomSheetDialog bottomSheetDialog;
    private TextView dialogTitleView;
    private TextView subjectDescriptionView;
    private ProgressBar progressBar;
    private TextView progressDescriptionView;
    private TextView tasksCancellationButton;

    // control vars
    private boolean cancelOperations;

    /**
     * Construct a batch worker that will execute the defined task on all the items of the input data list with a built-in dialog window.
     *
     * @param context         the context in which the window will appear.
     * @param dialogTitleView optional title of the dialog.
     * @param dataItems       the actual data items that the we will work upon.
     * @param dialogMode      mode of the dialog, either {@link DialogMode#MODE_CLASSIC} (for using classic {@link AlertDialog} style dialog) or {@link DialogMode#MODE_BOTTOM_SHEET} (for using a {@link BottomSheetDialog} style dialog).
     * @param workerCallBack  callback interface for defining the task for each data item, and more.
     */
    public BatchWorker(@NonNull @UiContext Context context,
                       @Nullable CharSequence dialogTitleView,
                       @NonNull List<T> dataItems,
                       @NonNull DialogMode dialogMode,
                       @NonNull WorkerCallBack<T, V> workerCallBack) {
        this.context = context;
        this.dataItems = dataItems;
        this.dialogMode = dialogMode;
        this.workerCallBack = workerCallBack;

        if (dialogMode == DialogMode.MODE_CLASSIC) {
            classicDialog =
                new AlertDialog.Builder(context)
                    .setView(dialogMode.getDialogLayoutResId())
                    .setCancelable(false)
                    .create();

            classicDialog.setOnShowListener(
                new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialog) {
                        AlertDialog activeDialog = ((AlertDialog) dialog);

                        // init refs
                        BatchWorker.this.dialogTitleView = activeDialog.findViewById(dialogMode.getDialogTitleResId());
                        subjectDescriptionView = activeDialog.findViewById(dialogMode.getLabelResId());
                        progressBar = activeDialog.findViewById(dialogMode.getProgressBarResId());
                        progressDescriptionView = activeDialog.findViewById(dialogMode.getProgressDescResId());
                        tasksCancellationButton = activeDialog.findViewById(dialogMode.getCancelBtnResId());

                        // init properties
                        if (dialogTitleView != null) {
                            BatchWorker.this.dialogTitleView.setText(dialogTitleView);
                            BatchWorker.this.dialogTitleView.setVisibility(View.VISIBLE);
                        } else {
                            BatchWorker.this.dialogTitleView.setVisibility(View.GONE);
                        }
                        progressBar.setIndeterminate(true);
                        progressDescriptionView.setText(R.string.three_dots_);
                        subjectDescriptionView.setText(workerCallBack.longPreWorkDescriptor());

                        // init listeners
                        tasksCancellationButton.setOnClickListener(
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    // mark cancellation
                                    cancelOperations = true;

                                    // dismiss dialog on operation cancellation
                                    bottomSheetDialog.dismiss();

                                    // perform post
                                    // do the specified short postWork
                                    workerCallBack.onShortPostWork(!cancelOperations);
                                }
                            }
                        );
                    }
                }
            );
        } else {
            bottomSheetDialog = new BottomSheetDialog(context);
            bottomSheetDialog.setContentView(dialogMode.getDialogLayoutResId());
            bottomSheetDialog.setCancelable(false);
            bottomSheetDialog.setOnShowListener(
                new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialog) {
                        BottomSheetDialog activeDialog = ((BottomSheetDialog) dialog);

                        // init refs
                        BatchWorker.this.dialogTitleView = activeDialog.findViewById(dialogMode.getDialogTitleResId());
                        subjectDescriptionView = activeDialog.findViewById(dialogMode.getLabelResId());
                        progressBar = activeDialog.findViewById(dialogMode.getProgressBarResId());
                        progressDescriptionView = activeDialog.findViewById(dialogMode.getProgressDescResId());
                        tasksCancellationButton = activeDialog.findViewById(dialogMode.getCancelBtnResId());

                        // init properties
                        if (dialogTitleView != null) {
                            BatchWorker.this.dialogTitleView.setText(dialogTitleView);
                            BatchWorker.this.dialogTitleView.setVisibility(View.VISIBLE);
                        } else {
                            BatchWorker.this.dialogTitleView.setVisibility(View.GONE);
                        }
                        progressBar.setIndeterminate(true);
                        progressDescriptionView.setText(R.string.three_dots_);
                        subjectDescriptionView.setText(workerCallBack.longPreWorkDescriptor());

                        // init listeners
                        tasksCancellationButton.setOnClickListener(
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    // mark cancellation
                                    cancelOperations = true;

                                    // dismiss dialog on operation cancellation
                                    bottomSheetDialog.dismiss();

                                    // perform post
                                    // do the specified short postWork
                                    workerCallBack.onShortPostWork(!cancelOperations);
                                }
                            }
                        );
                    }
                }
            );
        }
    }

    /**
     * Construct a batch worker that will execute the defined task on all the items of the input data list with a built-in dialog window.
     *
     * @param context              the context in which the window will appear.
     * @param dialogTitleView      optional title of the dialog.
     * @param dataItems            the actual data items that the we will work upon.
     * @param uiComponentsSelector container to hold external view resource ids required for publishing progress info of the tasks.
     * @param workerCallBack       callback interface for defining the task for each data item, and more.
     */
    public BatchWorker(@NonNull @UiContext Context context,
                       @Nullable CharSequence dialogTitleView,
                       @NonNull List<T> dataItems,
                       @NonNull UiComponentsSelector uiComponentsSelector,
                       @NonNull WorkerCallBack<T, V> workerCallBack) {
        this.context = context;
        this.dataItems = dataItems;
        this.dialogMode = null;
        this.workerCallBack = workerCallBack;

        // init refs
        View parent = uiComponentsSelector.getParentView();
        this.dialogTitleView = parent.findViewById(uiComponentsSelector.getTitleResId());
        subjectDescriptionView = parent.findViewById(uiComponentsSelector.getLabelResId());
        progressBar = parent.findViewById(uiComponentsSelector.getProgressBarResId());
        progressDescriptionView = parent.findViewById(uiComponentsSelector.getProgressDescResId());
        tasksCancellationButton = parent.findViewById(uiComponentsSelector.getCancelBtnResId());

        // init properties
        progressBar.setIndeterminate(true);
        progressDescriptionView.setText(R.string.three_dots_);
        subjectDescriptionView.setText(workerCallBack.longPreWorkDescriptor());

        // init listeners
        tasksCancellationButton.setOnClickListener(
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // mark cancellation
                    cancelOperations = true;

                    // dismiss dialog on operation cancellation
                    bottomSheetDialog.dismiss();

                    // perform post
                    // do the specified short postWork
                    workerCallBack.onShortPostWork(!cancelOperations);
                }
            }
        );
    }

    /**
     * Start the batch task.
     */
    public void start() {
        new Thread(
            new Runnable() {
                /**
                 * Runs the specified runnable in the specified looper while locking/awaiting
                 * the calling thread.
                 * */
                private void runLocked(Looper looper, Runnable runnable) {
                    CountDownLatch latch = new CountDownLatch(1);
                    new Handler(looper).post(
                        new Runnable() {
                            @Override
                            public void run() {
                                // run specified task
                                runnable.run();

                                // exit thread lock
                                latch.countDown();
                            }
                        }
                    );
                    try {
                        latch.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        // exit
                    }
                }

                @Override
                public void run() {
                    // create a results holder
                    List<V> results = new ArrayList<>(0);

                    // run short preWork on ui thread - block the enclosing thread.
                    // the short preWork will be run prior to opening the dialog.
                    runLocked(
                        Looper.getMainLooper(),
                        new Runnable() {
                            @Override
                            public void run() {
                                // do the specified short preWork
                                workerCallBack.onShortPreWork();

                                // show dialog
                                if (dialogMode == DialogMode.MODE_CLASSIC) {
                                    classicDialog.show();
                                } else {
                                    bottomSheetDialog.show();
                                }
                            }
                        }
                    );

                    // run long preWork on the enclosing thread.
                    // it will run right after the dialog is opened.
                    // progress bar will be in indeterminate state.
                    workerCallBack.onLongPreWork(dataItems);

                    // update progress bar properties.
                    // set max, make non-indeterminate etc.
                    // will run on ui thread.
                    runLocked(
                        Looper.getMainLooper(),
                        new Runnable() {
                            @Override
                            public void run() {
                                progressBar.setIndeterminate(false);
                                progressBar.setMax(dataItems.size());
                            }
                        }
                    );

                    // when the preWorks are done, run the main tasks
                    for (int i = 0; !cancelOperations && (i < dataItems.size()); ++i) {
                        int finalI = i;

                        // update current task label descriptor
                        runLocked(
                            Looper.getMainLooper(),
                            new Runnable() {
                                @Override
                                public void run() {
                                    // update label & progress descriptor to specified value
                                    subjectDescriptionView.setText(
                                        workerCallBack.taskLabelDescriptor(
                                            dataItems,
                                            finalI
                                        )
                                    );
                                    progressDescriptionView.setText(
                                        workerCallBack.taskProgressDescriptor(
                                            dataItems,
                                            finalI
                                        )
                                    );
                                }
                            }
                        );

                        // do the task in this enclosing thread
                        results.add(workerCallBack.performTask(dataItems, i));

                        // update progress bar
                        runLocked(
                            Looper.getMainLooper(),
                            new Runnable() {
                                @Override
                                public void run() {
                                    // update progress descriptor to specified value
                                    progressDescriptionView.setText(
                                        workerCallBack.taskProgressDescriptor(
                                            dataItems,
                                            finalI
                                        )
                                    );

                                    // update progress bar to the last completed task index
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                        progressBar.setProgress(
                                            finalI + 1,
                                            true
                                        );
                                    } else {
                                        progressBar.setProgress(finalI + 1);
                                    }
                                }
                            }
                        );
                    }

                    if (!cancelOperations) {
                        // update progress bar properties - set indeterminate
                        // will be done in ui thread
                        runLocked(
                            Looper.getMainLooper(),
                            new Runnable() {
                                @Override
                                public void run() {
                                    progressBar.setIndeterminate(true);
                                    progressDescriptionView.setText(R.string.three_dots_);
                                    subjectDescriptionView.setText(workerCallBack.longPostWorkDescriptor());
                                }
                            }
                        );

                        // run long postWork in the enclosing thread.
                        // it will run right after the actual batch tasks are completed.
                        // progress bar will be in indeterminate state.
                        workerCallBack.onLongPostWork(results);

                        // run short preWork on ui thread - block the enclosing thread
                        // the short preWork will run after all the other calls of this callback
                        // has finished & the dialog is closed.
                        runLocked(
                            Looper.getMainLooper(),
                            new Runnable() {
                                @Override
                                public void run() {
                                    // show dialog
                                    if (dialogMode == DialogMode.MODE_CLASSIC) {
                                        classicDialog.dismiss();
                                    } else {
                                        bottomSheetDialog.dismiss();
                                    }

                                    // perform post
                                    // do the specified short postWork
                                    workerCallBack.onShortPostWork(!cancelOperations);
                                }
                            }
                        );
                    }
                }
            }
        ).start();
    }


    public TextView getDialogTitleView() {
        return dialogTitleView;
    }

    public TextView getSubjectDescriptionView() {
        return subjectDescriptionView;
    }

    public ProgressBar getProgressBar() {
        return progressBar;
    }

    public TextView getProgressDescriptionView() {
        return progressDescriptionView;
    }

    public TextView getTasksCancellationButton() {
        return tasksCancellationButton;
    }
}
