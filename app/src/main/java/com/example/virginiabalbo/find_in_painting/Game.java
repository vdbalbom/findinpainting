package com.example.virginiabalbo.find_in_painting;

import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RelativeLayout;

import com.example.virginiabalbo.find_in_painting.dao.DAO;
import com.example.virginiabalbo.find_in_painting.domain.HiddenObject;
import com.example.virginiabalbo.find_in_painting.domain.Painting;

import java.util.List;

public class Game extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final long DOUBLE_CLICK_TIME_DELTA = 200; //milliseconds

    private Painting selectedPainting;
    private Menu checklist;
    private RelativeLayout paintingArea;
    long lastClickTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        this.checklist = navigationView.getMenu();

        DAO db = new DAO(this);
        this.selectedPainting = db.findPainting(Integer.valueOf(getIntent().getStringExtra("selectedPaintingId")));
        db.close();

        this.paintingArea = findViewById(R.id.paintingArea);

        createHiddenObjects();
        createChecklist();
        createPainting();
    }

    public void zoomOut(){
        Intent intent = new Intent(this, ZoomOut.class);
        intent.putExtra("src", this.selectedPainting.getImageSrc());
        startActivity(intent);
    }

    public void createPainting(){
        this.paintingArea.setBackgroundResource(getResources().getIdentifier(
                this.selectedPainting.getImageSrc(), null, getPackageName()));

        this.paintingArea.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                long clickTime = System.currentTimeMillis();
                if (clickTime - lastClickTime < DOUBLE_CLICK_TIME_DELTA){
                    zoomOut();
                    lastClickTime = 0;
                }
                lastClickTime = clickTime;
            }
        });


    }

    public void createHiddenObjects(){
        // get hidden objects of the selectedPainting
        List<HiddenObject> hiddenObjects = getHiddenObjects();

        // Iterate hidden objects and add then to the painting area
        for(int i = 0; i < hiddenObjects.size(); i++){
            HiddenObject obj= hiddenObjects.get(i);
            Button button = new Button(this);
            button.setLayoutParams(params(obj.getWidth(), obj.getHeight(), obj.getXPosition(), obj.getYPosition()));
            button.setTag(obj.getId());
            button.setBackgroundResource(transparent());
            button.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    findHiddenObject(view);
                    return true;
                }
            });

            this.paintingArea.addView(button);
        }
    }

    private RelativeLayout.LayoutParams params(int width, int height, int xPosition, int yPosition){
        float DP = this.getResources().getDisplayMetrics().density;
        RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams((int)(width*DP), (int)(height*DP));
        p.setMargins((int)(xPosition*DP), (int)(yPosition*DP), 0, 0);
        return p;
    }

    private int transparent(){
        //  To set button items transparent:
        TypedValue outValue = new TypedValue();
        this.getTheme().resolveAttribute(android.R.attr.selectableItemBackground, outValue, true);
        return outValue.resourceId;
    }

    public void createChecklist(){
        // get hidden objects of the selectedPainting
        List<HiddenObject> hiddenObjects = getHiddenObjects();

        // Iterate hidden objects and add items to menu
        for(int i = 0; i < hiddenObjects.size(); i++){
            HiddenObject hiddenObject = hiddenObjects.get(i);
            CheckBox checkBox = new CheckBox(this);
            checkBox.setClickable(false);
            MenuItem item = this.checklist.add(hiddenObject.getDescription());
            item.setActionView(checkBox);
            if (hiddenObject.isChecked()) checkBox.setChecked(true);
        }
    }

    public void refreshChecklist(){
        // clean menu
        this.checklist.clear();

        // create it again
        createChecklist();
    }

    public List<HiddenObject> getHiddenObjects(){
        DAO db = new DAO(this);
        List<HiddenObject> hiddenObjects = db.listHiddenObjects(this.selectedPainting.getId());
        db.close();
        return hiddenObjects;
    }

    public void findHiddenObject(View view) {
        int hiddenObjectId = (int) view.getTag();
        DAO db = new DAO(this);
        db.checkHiddenObject(hiddenObjectId);
        db.close();
        refreshChecklist();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
