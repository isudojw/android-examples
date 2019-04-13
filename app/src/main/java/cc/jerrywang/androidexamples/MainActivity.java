package cc.jerrywang.androidexamples;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import cc.jerrywang.androidexamples.account.Account;
import cc.jerrywang.androidexamples.account.GoogleAccount;
import cc.jerrywang.androidexamples.account.activity.AccountActivity;
import cc.jerrywang.androidexamples.account.bundle.AccountBundle;
import cc.jerrywang.androidexamples.account.task.AuthTask;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Toolbar toolbar;
    private FloatingActionButton fab;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;
    private Account account;
    private ProgressBar toolbarProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initToolbar();
        initActionBarDrawerToggle();
        initFloatingActionButton();
        initNavigationView();
        initAccountAtNavigationView();
    }

    // ==================================================================================
    //  Toolbar

    private void initToolbar() {
        getToolbar();
        setToolbarProgressBar();
    }

    private Toolbar getToolbar() {
        if (isNullorEmpty(toolbar)) {
            setToolbar();
        }
        return toolbar;
    }

    private boolean isNullorEmpty(Object object) {
        return object == null || object.equals("");
    }

    private void setToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void setToolbarProgressBar() {
        toolbarProgressBar = findViewById(R.id.toolbarProgressBar);
        toolbarProgressBar.setVisibility(View.GONE);
    }

    // ==================================================================================
    //  ActionBarDrawerToggle

    private void initActionBarDrawerToggle() {
        setDrawer();
        setToggle();
    }

    private void setDrawer() {
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
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

    @Override
    public void onBackPressed() {
        closeDrawer();
        super.onBackPressed();
    }

    private void closeDrawer() {
        if (isDrawerOpen()) {
            getDrawer().closeDrawer(GravityCompat.START);
        }
    }

    private boolean isDrawerOpen() {
        return getDrawer().isDrawerOpen(GravityCompat.START);
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
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
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
        startActivityEvent(activityClass, true);
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

    private void startActivityEvent(Class activityClass, boolean finishThisActivity) {
        closeDrawer();
        if (!isNullorEmpty(activityClass)) {
            startNewActivity(activityClass, finishThisActivity);
        }
    }

    private void startNewActivity(Class activityClass, boolean finishThisActivity) {
        Intent intent = new Intent();
        intent.setClass(MainActivity.this, activityClass);
        startActivity(intent);
        if (finishThisActivity) {
            MainActivity.this.finish();
        }
    }

    // ==================================================================================
    //  Account at NavigationView

    private void initAccountAtNavigationView() {
        setAccount();
        updateAccount();
        addSignEvent();
    }

    private void setAccount() {
        account = new GoogleAccount(this);
    }

    private void updateAccount() {
        if (getAccount().isSignedIn()) {
            updateAccountItems();
        }
    }

    private Account getAccount() {
        if (account == null) {
            setAccount();
        }
        return account;
    }

    private void updateAccountItems() {
        TextView textView01 = (TextView) getAccountLayoutItem(R.id.account_text_01);
        TextView textView02 = (TextView) getAccountLayoutItem(R.id.account_text_02);
        textView01.setText(getAccount().getUid("userId"));
        textView02.setText(getAccount().getDisplayName("Jay"));
    }

    private Object getAccountLayoutItem(int id) {
        return getNavigationView().getHeaderView(0).findViewById(id);
    }

    private NavigationView getNavigationView() {
        return navigationView;
    }

    private void addSignEvent() {
        getAccountLayout().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSignInClickEvent();
            }
        });
    }

    private LinearLayout getAccountLayout() {
        return (LinearLayout) getAccountLayoutItem(R.id.account_layout);
    }

    private void setSignInClickEvent() {
        closeDrawer();
        if (getAccount().isSignedIn()) {
            startAccountActivity();
        } else {
            startSignInEvent();
        }
    }

    private void startAccountActivity() {
        Class accountActivity = AccountActivity.class;
        startActivityEvent(accountActivity, false);
    }

    private void startSignInEvent() {
        addSignInAnimation();
        getAccount().startActivityForResult();
    }

    private void addSignInAnimation() {
        setProgressAnimation(true);
    }

    private void setProgressAnimation(boolean show) {
        getProgressBar().setVisibility(show ? View.VISIBLE : View.GONE);
    }

    private ProgressBar getProgressBar() {
        return toolbarProgressBar;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (getAccount().isSuccessful(requestCode)) {
            AccountBundle<Intent> bundle = new AccountBundle<>();
            bundle.putIntent(data);
            signIn(bundle);
        }
    }

    private void signIn(AccountBundle bundle) {
        getAccount().signIn(bundle, new AuthTask() {
            @Override
            public void onComplete(int resultCode) {
                updateAccount();
                removeSignInAnimation();
            }
        });
    }

    private void removeSignInAnimation() {
        setProgressAnimation(false);
    }

}
