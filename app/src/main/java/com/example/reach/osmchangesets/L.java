package com.example.reach.osmchangesets;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by reach on 4/29/2018.
 */

public class L {
    public static void m(String message) {
        Log.d("DNK", message);
    }

    public static void s(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
