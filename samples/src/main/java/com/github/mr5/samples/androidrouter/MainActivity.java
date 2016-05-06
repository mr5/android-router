package com.github.mr5.samples.androidrouter;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.FrameStats;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivity extends ActionBarActivity {

    @BindView(R.id.tab_host)
    protected TabHost tabhost;
    @BindView(R.id.tabs)
    protected TabWidget tabs;
    @BindView(R.id.tab_content)
    protected FrameLayout tabContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        TabHost.TabSpec newsPage = tabhost.newTabSpec("news")
                .setIndicator(findViewById(R.id.tab_news))
                .setContent(R.id.tab_news);
        TabHost.TabSpec livePage = tabhost.newTabSpec("live")
                .setIndicator(findViewById(R.id.tab_live))
                .setContent(R.id.tab_live);
        TabHost.TabSpec marketsPage = tabhost.newTabSpec("markets")
                .setIndicator(findViewById(R.id.tab_markets))
                .setContent(R.id.tab_markets);
        TabHost.TabSpec mePage = tabhost.newTabSpec("me")
                .setIndicator(findViewById(R.id.tab_me))
                .setContent(R.id.tab_me);
        tabhost.addTab(newsPage);
        tabhost.addTab(livePage);
        tabhost.addTab(marketsPage);
        tabhost.addTab(mePage);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        startActivityForResult(new Intent(), 111);
        return true;
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
