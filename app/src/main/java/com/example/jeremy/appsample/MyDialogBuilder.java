package com.example.jeremy.appsample;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.WindowManager;

/**
 * Created by jeremy on 21/10/2016.
 */

public class MyDialogBuilder extends AlertDialog.Builder {
    AlertDialog mDialog;

    public MyDialogBuilder(Context context) {
       super(context);
    }

    @Override
    public AlertDialog show() {
        this.setTitle("Test title");
        this.setMessage("this is a test message");
        this.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mDialog.dismiss();
            }
        });
        this.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mDialog.dismiss();
            }
        });
        mDialog = super.create();
        //mDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        mDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_APPLICATION);
        mDialog.show();
        return mDialog;
    }
}

