package com.github.mr5.samples.androidrouter;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class RepositoryActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextView textView = new TextView(this);
        String vendor = getIntent().getStringExtra("vendor");
        String repository = getIntent().getStringExtra("repository");
        if (vendor == null) vendor = "";
        if (repository == null) repository = "";
        textView.setText(String.format("repository page, vendor: %s, repository: %s", vendor, repository));
        setContentView(textView);
    }
}