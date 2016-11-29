package com.example.jeremy.appsample.web;


import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import fi.iki.elonen.NanoHTTPD;

public class WebInterfaceService extends Service {
    private static final String TAG = "WebInterfaceService";

    public static final String KEY_COMMAND = "command";
    public static final String COMMAND_START_WEB_SERVER = "start_web_server";
    public static final String COMMAND_STOP_WEB_SERVER = "stop_web_server";
    static NanoHTTPD mWebServer = new NanoWebServer();

    public static void startWebServer(Context context) {
        Intent intent = new Intent(context, WebInterfaceService.class);
        intent.putExtra(WebInterfaceService.KEY_COMMAND, WebInterfaceService.COMMAND_START_WEB_SERVER);
        context.startService(intent);
    }

    public static void stopWebServer(Context context) {
        Intent intent = new Intent(context, WebInterfaceService.class);
        intent.putExtra(WebInterfaceService.KEY_COMMAND, WebInterfaceService.COMMAND_STOP_WEB_SERVER);
        context.startService(intent);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String command = (null == intent) ? null : intent.getStringExtra(KEY_COMMAND);
        if (null == command) {
        } else if (COMMAND_START_WEB_SERVER.equals(command)) {
            if (!mWebServer.isAlive()) {
                try {
                    mWebServer.start();
                } catch (Exception e) {
                    Log.d(TAG, "Start web server fail: " + e);
                }
            }
        } else if (COMMAND_STOP_WEB_SERVER.equals(command)) {
            if (mWebServer.isAlive()) {
                mWebServer.stop();
            }
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        if (null != mWebServer) {
            mWebServer.stop();
        }
    }
}
