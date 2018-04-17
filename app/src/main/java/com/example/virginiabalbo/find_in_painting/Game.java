package com.example.virginiabalbo.find_in_painting;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.util.TypedValue;
import android.view.Display;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.virginiabalbo.find_in_painting.dao.DAO;
import com.example.virginiabalbo.find_in_painting.domain.HiddenObject;
import com.example.virginiabalbo.find_in_painting.domain.Painting;

import java.util.List;

public class Game extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Painting selectedPainting;
    private Menu checklist;
    private RelativeLayout paintingArea;
    private int TAB_HEIGHT = 216;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
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

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void createPainting(){
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int imageID = getResources().getIdentifier(this.selectedPainting.getImageSrc(), null, getPackageName());
        BitmapDrawable bd = (BitmapDrawable) getResources().getDrawable(imageID);
        int height = bd.getBitmap().getHeight();
        int width = bd.getBitmap().getWidth();
        this.paintingArea.setBackground(bd);
        if (size.x*height/width + TAB_HEIGHT <= size.y) {
            this.paintingArea.getLayoutParams().width = size.x;
            this.paintingArea.getLayoutParams().height = size.x*height/width;

        } else {
            System.out.println("HEEEEY");
            this.paintingArea.getLayoutParams().height = size.y - TAB_HEIGHT;
            this.paintingArea.getLayoutParams().width = (size.y - TAB_HEIGHT)*width/height;

        }
        findViewById(R.id.zoomLayout).setBackgroundColor(Color.BLACK);
        findViewById(R.id.zoomLayout).getLayoutParams().width = size.x;
        findViewById(R.id.zoomLayout).getLayoutParams().height = size.y;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void createHiddenObjects(){
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int imageID = getResources().getIdentifier(this.selectedPainting.getImageSrc(), null, getPackageName());
        BitmapDrawable bd = (BitmapDrawable) getResources().getDrawable(imageID);
        int height = bd.getBitmap().getHeight();
        int width = bd.getBitmap().getWidth();

        // get hidden objects of the selectedPainting
        List<HiddenObject> hiddenObjects = getHiddenObjects();

        // Iterate hidden objects and add then to the painting area
        for(int i = 0; i < hiddenObjects.size(); i++){
            HiddenObject obj= hiddenObjects.get(i);

            Button button = new Button(this);
            if (size.x*height/width + TAB_HEIGHT <= size.y) {
                button.setLayoutParams(params(obj.getWidth()*size.x/width, obj.getHeight()*size.x/width, obj.getXPosition()*size.x/width, obj.getYPosition()*size.x/width));
            } else {
                button.setLayoutParams(params(obj.getWidth()*(size.y - TAB_HEIGHT)/height, obj.getHeight()*(size.y - TAB_HEIGHT)/height, obj.getXPosition()*(size.y - TAB_HEIGHT)/height, obj.getYPosition()*(size.y - TAB_HEIGHT)/height));
            }
            button.setTag(obj);
            button.setStateListAnimator(null);
            if (obj.isChecked()){
                button.setBackground(getDrawable(R.drawable.circulo_feio));
            } else {
                button.setBackground(null);
                button.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        float h = view.getHeight();
                        float w = view.getHeight();
                        float zoom = ((ZoomView) findViewById(R.id.zoom)).zoom;
                        if (h*w*zoom > 2000 || zoom > 5) {
                            view.setBackground(getDrawable(R.drawable.circulo_feio));
                            view.setOnLongClickListener(null);
                            findHiddenObject(view);
                            return true;
                        }
                        return false;

                    }
                });
            }

            this.paintingArea.addView(button);
        }
    }

    private RelativeLayout.LayoutParams params(int width, int height, int xPosition, int yPosition){
        float DP = this.getResources().getDisplayMetrics().density;
        RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams((int)(width*DP), (int)(height*DP));
        p.setMargins((int)(xPosition*DP), (int)(yPosition*DP), 0, 0);
        return p;
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
        HiddenObject obj = (HiddenObject) view.getTag();
        Snackbar mySnackbar = Snackbar.make(view, obj.getDescription(), 2500);
        mySnackbar.getView().setBackgroundResource(R.drawable.paper);
        TextView tv = mySnackbar.getView().findViewById(android.support.design.R.id.snackbar_text);
        tv.setTextColor(Color.BLACK);
        mySnackbar.show();
        int hiddenObjectId = (int) obj.getId();
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
