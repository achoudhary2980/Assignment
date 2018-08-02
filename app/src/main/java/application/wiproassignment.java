/*
 * Copyright (c) 2017. Truiton (http://www.truiton.com/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Contributors:
 * Mohit Gupt (https://github.com/mohitgupt)
 *
 */

package application;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;




public class wiproassignment extends Application {

    public static boolean isNotificationReceived = false;
    static boolean executed = false;
    private static wiproassignment sframeworkapp;

    private StackTraceElement stack = Thread.currentThread().getStackTrace()[2];
   
    private boolean mIsPINPadActivityOpened = false;
    private OnAppInvisibleListener mOnAppInvisibleListeners;
 

    public static wiproassignment getInstance() {
        return sframeworkapp;
    }



    public static void setExecuted(boolean isexceuted) {
        executed = isexceuted;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
//        ACRA.init(this);
//        if (MyUtility.isDebugBuild()) {
//
//            ACRA.getErrorReporter().putCustomData("Build Type", "Debug");
//
//        } else {
//            ACRA.getErrorReporter().putCustomData("Build Type", "Production");
//        }

        sframeworkapp = this;

    }




   


    public void setOnAppInvisibleListener(OnAppInvisibleListener listener) {
        mOnAppInvisibleListeners = listener;
    }

    public interface OnAppInvisibleListener {
        void onAppInvisible();
    }

}