package com.buggysofts.android.batchworkerimpl;

import android.os.Bundle;

import androidx.annotation.UiThread;
import androidx.annotation.WorkerThread;
import androidx.appcompat.app.AppCompatActivity;

import com.buggysofts.android.batchworker.BatchWorker;
import com.buggysofts.android.batchworker.DialogMode;
import com.buggysofts.android.batchworker.WorkerCallBack;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new BatchWorker<Integer, Double>(
            MainActivity.this,
            "Title",
            Arrays.asList(1, 2, 3, 4, 5),
            DialogMode.MODE_CLASSIC,
            new WorkerCallBack<Integer, Double>() {
                @UiThread
                @Override
                public void onShortPreWork() {
                    // todo - perform any instantaneous task - eg. update/initialize ui
                }

                @WorkerThread
                @Override
                public void onLongPreWork(List<Integer> dataList) {
                    // todo - perform any long running task on the data
                    // eg. initialize one or more property of each data item.
                }

                @Override
                public String longPreWorkDescriptor() {
                    // todo - return anything you like, eg. "initializing", "connecting" etc.
                    return "Pre-Processing...";
                }

                @Override
                public Double performTask(List<Integer> dataList, int activeDataIndex) {
                    Integer activeData = dataList.get(activeDataIndex);

                    // todo - perform actual task(may be long running) on each data item
                    double lResult = getLongRunningProcessResult(activeData);

                    // return a result
                    return activeData * lResult;
                }

                @Override
                public String taskLabelDescriptor(List<Integer> dataList, int activeDataIndex) {
                    // todo - return any short details about the operation on the active data
                    // e.g. it's name or any other details etc.
                    return String.format("%s", dataList.get(activeDataIndex));
                }

                @Override
                public String taskProgressDescriptor(List<Integer> dataList, int activeDataIndex) {
                    // todo - return a text representation of the progress, for example...
                    return String.format(
                        "%s/%s",
                        activeDataIndex + 1,
                        dataList.size()
                    );
                }

                @Override
                public void onLongPostWork(List<Double> results) {
                    // todo - perform any long running task on the result list
                    // eg. finalize works, free used resources, anything.
                }

                @Override
                public String longPostWorkDescriptor() {
                    // todo - return anything you like, eg. "Finalizing", "Clearing temporary resources" etc.
                    return "Post-Processing...";
                }

                @Override
                public void onShortPostWork(boolean completed) {
                    // todo - perform any instantaneous task - eg. update/finalize ui
                }
            }
        ).start();
    }

    private double getLongRunningProcessResult(Integer activeData) {
        return activeData * 1.5;
    }
}