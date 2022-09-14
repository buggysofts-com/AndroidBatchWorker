package com.buggysofts.android.batchworker;

import androidx.annotation.NonNull;
import androidx.annotation.UiThread;
import androidx.annotation.WorkerThread;

import java.util.List;

public interface WorkerCallBack<T, V> {
    /**
     * Run instantaneous task on the main/ui thread before starting the batch task.
     * This is the first thing to run and will run before the dialog is visible
     * (if {@link DialogMode} based constructor is used).
     */
    @UiThread
    public void onShortPreWork();

    /**
     * Perform any long running task on the input data (or anything else) before starting the actual
     * batch task.
     *
     * @param dataList The input data list passed to the constructor.
     */
    @WorkerThread
    public void onLongPreWork(@NonNull List<T> dataList);

    /**
     * Description for {@link #onLongPostWork(List)}}.
     */
    public String longPreWorkDescriptor();

    /**
     * Perform the actual task for each item of the input data list.
     *
     * @param dataList        The input data list passed to the constructor.
     * @param activeDataIndex Index of the data item for which the task is going to be performed.
     */
    public V performTask(@NonNull List<T> dataList, int activeDataIndex);

    /**
     * Description for current task executed by {@link #performTask(List, int)}.
     * This can be the name or any other describable property of the data, e.g file name.
     *
     * @param dataList The input data list passed to the constructor.
     * @param activeDataIndex Index of the data item for which the task is going to be performed.
     */
    public String taskLabelDescriptor(@NonNull List<T> dataList, int activeDataIndex);

    /**
     * Progress description for current task executed by {@link #performTask(List, int)}.
     * This can be something like "3/20" where total number of data item is 20 and we are working
     * on a data at position 3.
     *
     * @param dataList The input data list passed to the constructor.
     * @param activeDataIndex Index of the data item for which the task is going to be performed.
     */
    public String taskProgressDescriptor(@NonNull List<T> dataList, int activeDataIndex);

    /**
     * Perform any long running task on the output data (or anything else).
     * It is executed after the actual batch task.
     *
     * @param results A list containing output of each corresponding operations.
     */
    @WorkerThread
    public void onLongPostWork(@NonNull List<V> results);

    public String longPostWorkDescriptor();

    /**
     * Run instantaneous task on the main/ui thread after everything is complete.
     * This is the last thing to run and and will run after the dialog is dismissed
     * (if {@link DialogMode} based constructor is used).
     *
     * @param results A list containing output of each corresponding operations.
     * @param completed If batch task ran for all the input data,
     *                  it is considered to be completed. Otherwise,
     *                  user may have requested a cancellation, and the batch task was interrupted.
     */
    @UiThread
    public void onShortPostWork(@NonNull List<V> results, boolean completed);
}
