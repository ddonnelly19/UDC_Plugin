package com.hpe.ucmdb.udc;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.progress.PerformInBackgroundOption;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

public class BackgroundUtil {
    public static void background(final Project project, final String title, final boolean startInBackground, final Runnable runnable, final Runnable onSuccess, final Runnable onFailure) {
        final Task.Backgroundable task = new Task.Backgroundable(project, title, false, new PerformInBackgroundOption() {
            public boolean shouldStartInBackground() {
                return startInBackground;
            }

            public void processSentToBackground() {
            }
        }) {
            public void run(@NotNull final ProgressIndicator progressIndicator) {
                progressIndicator.setIndeterminate(true);
                try {
                    runnable.run();
                } catch (Exception e) {
                    ApplicationManager.getApplication().invokeLater(onFailure);
                }
            }

            public void onSuccess() {
                onSuccess.run();
            }
        };
        ProgressManager.getInstance().run(task);
    }

    public static <T> BackgroundContext<T> createContext() {
        return new BackgroundContext<>();
    }

    public static class BackgroundContext<T> {
        T t;

        public void put(final T t) {
            this.t = t;
        }

        public T get() {
            return this.t;
        }
    }
}