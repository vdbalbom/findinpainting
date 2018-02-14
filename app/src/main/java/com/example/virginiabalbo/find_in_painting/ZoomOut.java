package com.example.virginiabalbo.find_in_painting;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

public class ZoomOut extends AppCompatActivity {

    private static final long DOUBLE_CLICK_TIME_DELTA = 200; //milliseconds
    long lastClickTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zoom_out);
//        ImageView painting = findViewById(R.id.paintingZoomOut);
//        painting.setImageResource(Integer.valueOf(getResources().getIdentifier(
//                getIntent().getStringExtra("src"), null, getPackageName())));
//        painting.setOnClickListener(new View.OnClickListener(){
//            public void onClick(View v) {
//                long clickTime = System.currentTimeMillis();
//                if (clickTime - lastClickTime < DOUBLE_CLICK_TIME_DELTA){
//                    zoomIn();
//                    lastClickTime = 0;
//                }
//                lastClickTime = clickTime;
//            }
//        });

        ZoomLayout zoomLayout = new ZoomLayout(this);
        zoomLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        setContentView(zoomLayout);
        RelativeLayout painting = new RelativeLayout(this);
        painting.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT));
        painting.setBackgroundResource(Integer.valueOf(getResources().getIdentifier(
                getIntent().getStringExtra("src"), null, getPackageName())));
        zoomLayout.addView(painting);
    }

    private void zoomIn(){
        this.onBackPressed();
    }
}
