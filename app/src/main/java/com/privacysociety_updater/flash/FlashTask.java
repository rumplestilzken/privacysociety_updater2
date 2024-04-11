package com.privacysociety_updater.flash;

import javafx.concurrent.Task;

public class FlashTask<V> extends Task<Void> {
    public void setProgressBarProgress(double progress) {
        updateProgress(progress, 1);
    }
    public void setProgressMessage(String message) {
        updateMessage(message);
    }
    @Override
    protected Void call() throws Exception {
        return null;
    }
}
