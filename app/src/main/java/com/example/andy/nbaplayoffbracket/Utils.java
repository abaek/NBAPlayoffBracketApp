package com.example.andy.nbaplayoffbracket;

import android.content.Context;
import android.view.View;
import android.view.ViewTreeObserver;

public class Utils {
    public interface OnMeasuredCallback {
        void onMeasured(View view, int width, int height);
    }

    public static void waitForMeasure(final View view, final OnMeasuredCallback callback) {
        int width = view.getWidth();
        int height = view.getHeight();

        if (width > 0 && height > 0) {
            callback.onMeasured(view, width, height);
            return;
        }

        final ViewTreeObserver observer = view.getViewTreeObserver();
        observer.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                if (observer.isAlive()) {
                    observer.removeOnPreDrawListener(this);
                }

                callback.onMeasured(view, view.getWidth(), view.getHeight());

                return true;
            }
        });
    }

    public static void inject(Context context, View view) {
        ((Injector) context).inject(view);
    }

    private Utils() {
    }
}
