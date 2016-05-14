package com.github.mr5.samples.androidrouter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

/**
 * Created by wscn on 16/5/14.
 */
public class VendorActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextView textView = new TextView(this);
        String vendor = getIntent().getStringExtra("vendor");
        if (vendor == null) vendor = "";
        textView.setText("vendor page: " + vendor);
        setContentView(textView);
    }
}
