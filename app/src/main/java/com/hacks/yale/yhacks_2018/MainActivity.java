package com.hacks.yale.yhacks_2018;

import android.app.ActionBar;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.hacks.yale.yhacks_2018.adr.ADRActivity;
import com.hacks.yale.yhacks_2018.firebase.PatientInfo;
import com.hacks.yale.yhacks_2018.notification.NotificationService;
import com.hacks.yale.yhacks_2018.ocr.OCRCaptureActivity;
import com.hacks.yale.yhacks_2018.patient.PatientActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final int TEST_RESPONSE = 1;
    private int countdown = 5000;
    private PatientInfo patient;
    private PatientActivity patientActivity;
    NotificationService notificationService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // initialize the patient
        ArrayList<String> allergies = new ArrayList<>();
        allergies.add("cephalosporins");

        ArrayList<String> conditions = new ArrayList<>();
        conditions.add("hypertension");
        conditions.add("T2DM");

        ArrayList<String> medications = new ArrayList<>();
        medications.add("65862-010-01");
        medications.add("68071-4133-03");

        patient = new PatientInfo("John Lee", 89, 65, "Male", 26, allergies , conditions, medications);

        // Recycler view stuff
        RecyclerView rvMain = (RecyclerView) findViewById(R.id.rvMain);

        ArrayList<Alert> alerts = new ArrayList<Alert>();
        for (int i = 1; i <= 4; i++) {
            alerts.add(new Alert("hi", "bye"));
        }

        MainAdapter adapter = new MainAdapter(alerts);
        rvMain.setAdapter(adapter);
        rvMain.setLayoutManager(new LinearLayoutManager(this));

        // Begin notifications demo
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationService = new NotificationService(this, notificationManager);

        // disable navigation drawer button
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(false);
        }

    }

    @Override
    public void onStop() {
        super.onStop();

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
            notificationService.showNotification();
            }
        }, countdown);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.END);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            Log.i("RESULT OK", "-----------------------");
            if (requestCode == TEST_RESPONSE) {
                Log.i("TEST RESPONSE", "-----------------------");
            }
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            Intent intent = new Intent(MainActivity.this, OCRCaptureActivity.class);
            startActivityForResult(intent, TEST_RESPONSE);
        } else if (id == R.id.nav_drugs) {
            Intent intent = new Intent(this, DrugActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_alerts) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {
            PatientActivity patientActivity = new PatientActivity();
            patientActivity.loadPatient(patient);
            Intent intent = new Intent(this, PatientActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_send) {
            Intent intent = new Intent(this, ADRActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.END);
        return true;
    }
}
