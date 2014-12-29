/**
 * 
 */
package com.hume.mydota;

import android.app.Application;

/**
 * @author Sergey Tarasevich (nostra13[at]gmail[dot]com)
 */
public class DefaultApplication extends Application {

    /**
     * singleton
     */
    private static DefaultApplication globalContext = null;

    @Override
    public void onCreate() {
        super.onCreate();
        globalContext = this;
    }

    /**
     * Global Context
     * 
     * @return
     */
    public static DefaultApplication getInstance() {
        return globalContext;
    }
}
