package com.github.mr5.samples.androidrouter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TabHost;



public class MainActivity extends Activity {

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tabhost_container);

        TabHost tabs = (TabHost)this.findViewById(R.id.tabhost);
        tabs.setup();

        tabs.addTab(tabs.newTabSpec("one").setContent(R.id.tab1content).setIndicator("TAB 1"));
        tabs.addTab(tabs.newTabSpec("two").setContent(R.id.tab2content).setIndicator("TAB 2"));
        tabs.setCurrentTab(0);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
