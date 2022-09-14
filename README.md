# AndroidBatchWorker [![](https://jitpack.io/v/buggysofts-com/AndroidBatchWorker.svg)](https://jitpack.io/#buggysofts-com/AndroidBatchWorker)
Execute asynchronous batch tasks with predefined or custom UI in Android.

<br />

## Import
Add JitPack repository to your project level build.gradle file
```
...

allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```
Or, in newer android projects, if you need to the add repository in settings.gradle file...
```
...

dependencyResolutionManagement {
    ...
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```
Finally, add these two dependencies to your app/module level build.gradle file
```
...

dependencies {
    ...
    implementation 'com.github.buggysofts-com:AndroidBatchWorker:v1.0.1'
}
```
And you are done importing the library.

<br />

## Sample codes

Here is a sample that uses Integer as input data type, and Double as the output data type.
You can use any data type as input or output.

```
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
    
        @WorkerThread
        @Override
        public Double performTask(List<Integer> dataList, int activeDataIndex) {
            Integer activeData = dataList.get(activeDataIndex);
    
            // todo - perform actual task(may be long running) on each data item
            double lResult = getLongRunningProcessResult(activeData);
    
            // return a result
            return activeData*lResult;
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
                activeDataIndex+1,
                dataList.size()
            );
        }
    
        @WorkerThread
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
    
        @UiThread
        @Override
        public void onShortPostWork(boolean completed) {
            // todo - perform any instantaneous task - eg. update/finalize ui
        }
    }
).start();
```

<br />

You can access the ui components of the dialog (if you are using built-in dialogs) using the following public getter methods.

1. ```getDialogTitleView()``` The TextView acting as the title of the dialog.
2. ```getSubjectDescriptionView()``` The TextView acting as the current subject descriptor. Current subject is the data item for which the task is currently running.
3. ```getProgressDescriptionView()``` The TextView that is describing (by text) the progress of the total work.
4. ```getProgressBar()``` The ProgressBar that is visualising the progress of the total work.
5. ```getTasksCancellationButton()``` The Button for requesting cancellation of the remaining tasks.

<br />

Please see the image below if you are not sure about the ui components:
<br />
<br />
![UI components](/app/src/main/res/drawable/dlg_components.png)

<br />
<br />

Please share & rate the library if you find it useful.

### Happy coding!