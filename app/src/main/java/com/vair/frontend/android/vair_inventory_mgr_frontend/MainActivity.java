package com.vair.frontend.android.vair_inventory_mgr_frontend;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;

import com.google.android.gms.common.api.GoogleApiClient;
import com.vair.frontend.android.vair_inventory_mgr_frontend.com.vair.frontend.android.vair.inventory_mgr_frontend.utils.SharePreferenceHelper;

public class MainActivity extends AppCompatActivity
        implements
            NavigationView.OnNavigationItemSelectedListener,
            RegistNewInventory.OnFragmentInteractionListener,
            InventoryGallery.OnFragmentInteractionListener,
            ScanInventory.OnFragmentInteractionListener,
            Toolbox.OnFragmentInteractionListener, View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {


    public static final String GOOGLE_SIGN_IN_TAG = "GOOGLE_SIGN_IN_TAG";
    public static final String ACCT_ID = "GOOGLE_DISPLAY_NAME";
    public static final String ACCT_EMAIL = "GOOGLE_EMAIL";
    GoogleApiClient googleClient;
    private int RC_SIGN_IN = 20;
    GoogleSignInAccount acct;
    SharePreferenceHelper prefHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        prefHelper = SharePreferenceHelper.getInstance(this);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().requestId().requestProfile().build();
        googleClient = new GoogleApiClient.Builder(this).enableAutoManage(this, this).addApi(Auth.GOOGLE_SIGN_IN_API, gso).build();



        if (isSignInUser()) {
            signIn();
        }

        findViewById(R.id.google_sign_in).setOnClickListener(this);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        Fragment frag = null;

        if (id == R.id.nav_camera) {
            // Show add inventory view
            frag = new RegistNewInventory();

        } else if (id == R.id.nav_gallery) {
            // Show all Inventory
            frag = new InventoryGallery();
        } else if (id == R.id.nav_scan) {
            // show barcode scan view
            frag = new ScanInventory();

        } else if (id == R.id.nav_manage) {
            // open toolbox
            frag = new Toolbox();

        } else if (id == R.id.nav_share) {
            // show send email
        }

        if (frag != null) {
            Bundle args = new Bundle();
            frag.setArguments(args);

            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, frag);
            transaction.addToBackStack(null);
            transaction.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.google_sign_in:
                signIn();
                break;
        }
    }

    private void signIn() {
        Intent signIntent = Auth.GoogleSignInApi.getSignInIntent(googleClient);
        startActivityForResult(signIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(GOOGLE_SIGN_IN_TAG, "RequestCode: " + requestCode + " ResultCode: " + resultCode + " RC_SIGN_IN: " + RC_SIGN_IN);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(GOOGLE_SIGN_IN_TAG, "handleSignInResult " + result.isSuccess());
        if (result.isSuccess()) {
            acct = result.getSignInAccount();
            ((TextView)findViewById(R.id.m_display_name)).setText(acct.getDisplayName());
            Log.d(GOOGLE_SIGN_IN_TAG, "handleSignInResult " + acct.getDisplayName());
            saveLoginStatus(acct);
            updateUI(true);
        } else {
            Log.d(GOOGLE_SIGN_IN_TAG, "result status: " + result.getStatus().getStatusCode());
            updateUI(false);
        }

    }

    private Boolean isSignInUser() {

        String id = prefHelper.GetValue(ACCT_ID);
        return !"".equals(id);

    }

    private void saveLoginStatus(GoogleSignInAccount acct) {
        prefHelper.Save(ACCT_ID, acct.getId());
        prefHelper.Save(ACCT_EMAIL, acct.getEmail());
    }

    private void updateUI(boolean b) {
        if (b) {
            findViewById(R.id.google_sign_in).setVisibility(View.GONE);
            Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Login Failed", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d(GOOGLE_SIGN_IN_TAG, "Connection Result: " + connectionResult);
    }
}
