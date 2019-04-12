package cc.jerrywang.androidexamples;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.VisibilityAwareImageButton;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import cc.jerrywang.androidexamples.account.Account;
import cc.jerrywang.androidexamples.account.GoogleAccount;
import cc.jerrywang.androidexamples.account.metadata.UserMetadata;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Toolbar toolbar;
    private FloatingActionButton fab;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;
    private Account account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initToolbar();
        initFloatingActionButton();
        initActionBarDrawerToggle();
        initNavigationView();
        initAccount();
    }

    // ==================================================================================
    //  Toolbar

    private void initToolbar() {
        if (isNullorEmpty(toolbar)) {
            setToolbar();
        }
    }

    private boolean isNullorEmpty(Object object) {
        return object == null || object.equals("");
    }

    private void setToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private Toolbar getToolbar() {
        if (isNullorEmpty(toolbar)) {
            initToolbar();
        }
        return toolbar;
    }

    // ==================================================================================
    //  ActionBarDrawerToggle

    private void initActionBarDrawerToggle() {
        if (isNullorEmpty(toolbar)) {
            setToggle();
        }
    }

    private void setToggle() {
        toggle = new ActionBarDrawerToggle(this, getDrawer(), getToolbar(),
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        getDrawer().addDrawerListener(toggle);
        toggle.syncState();
    }

    private DrawerLayout getDrawer() {
        if (isNullorEmpty(drawer)) {
            setDrawer();
        }
        return drawer;
    }

    private void setDrawer() {
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
    }

    @Override
    public void onBackPressed() {
        if (isDrawerOpen()) {
            closeDrawer();
        } else {
            super.onBackPressed();
        }
    }

    private boolean isDrawerOpen() {
        return getDrawer().isDrawerOpen(GravityCompat.START);
    }

    private void closeDrawer() {
        getDrawer().closeDrawer(GravityCompat.START);
    }

    // ==================================================================================
    //  FloatingActionButton

    private void initFloatingActionButton() {
        if (isNullorEmpty(fab)) {
            setFloatingActionButton();
        }
    }

    private void setFloatingActionButton() {
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    // ==================================================================================
    //  Menu

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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

    // ==================================================================================
    //  NavigationView

    private void initNavigationView() {
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        Class activityClass = getActivityClass(item.getItemId());
        startActivity(activityClass);
        return true;
    }

    private Class getActivityClass(int menuItemId) {
        Class activityClass = null;
        switch (menuItemId) {
            case R.id.nav_camera:
                break;
            case R.id.nav_gallery:
                break;
            case R.id.nav_slideshow:
                break;
            case R.id.nav_manage:
                break;
            case R.id.nav_share:
                break;
            case R.id.nav_send:
                break;
            default:
        }
        return activityClass;
    }

    // ==================================================================================

    private void startActivity(Class activityClass) {
        closeDrawer();
        if (!isNullorEmpty(activityClass)) {
            startNewActivity(activityClass);
            stopThisActivity();
        }
    }

    private void startNewActivity(Class activityClass) {
        Intent intent = new Intent();
        intent.setClass(MainActivity.this, activityClass);
        startActivity(intent);
    }

    private void stopThisActivity() {
        MainActivity.this.finish();
    }

    // ==================================================================================
    //  Account

    private void initAccount() {
        setAccountViewItem();
        setAccountAuth();
        setAccountButton();
        setAccountText();
    }

    private void setAccountViewItem() {
        initNavigationView();
    }

    private void setAccountAuth() {
        account = new GoogleAccount(this);
    }

    private void setAccountButton() {
        LinearLayout accountLayout = (LinearLayout) findViewById(R.id.account_layout);
        accountLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // startActivity(AccountActivity.class)
            }
        });
    }

    private void setAccountText() {
        String topText = getAccountDisplayNameText("top");
        String downText = "down";
        this.updateAccountText(topText, downText);
    }

    protected void updateAccountText(String topText, String downText) {
        TextView textView01 = (TextView) findViewById(R.id.account_text_01);
        TextView textView02 = (TextView) findViewById(R.id.account_text_02);
        textView01.setText(topText);
        textView02.setText(downText);
    }

    private String getAccountDisplayNameText(String defValue) {
        return account.getDisplayName(defValue);
    }

}
