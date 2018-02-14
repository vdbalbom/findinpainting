package com.example.virginiabalbo.find_in_painting;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.virginiabalbo.find_in_painting.dao.DAO;
import com.example.virginiabalbo.find_in_painting.domain.Painting;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Painting selectedPainting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.selectedPainting = null;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (!Gallery.isCreated(this)) {
            Gallery.create(this);
        }
        DAO db = new DAO(this);
        List<Painting> paintings = db.listPaintings();
        for(int i = 0; i < paintings.size(); i++) {
            float DP = this.getResources().getDisplayMetrics().density;
            int imageId = getResources().getIdentifier(paintings.get(i).getThumbSrc(), null, getPackageName());
            ImageButton button = new ImageButton(this);
            button.setTag(paintings.get(i));
            button.setAdjustViewBounds(true);
            button.setMinimumWidth((int)(170*DP));
            button.setMinimumHeight((int)(170*DP));
            button.setMaxWidth((int)(170*DP));
            button.setMaxHeight((int)(170*DP));
            button.setImageResource(imageId);
            button.setPadding((int)(8*DP),(int)(10*DP),(int)(8*DP),(int)(10*DP));
            button.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectPainting(v);
                }
            });

            LinearLayout listOfPaintings = findViewById(R.id.list_of_paintings);
            listOfPaintings.addView(button);
        }
        db.close();

        disableStartButton();
    }

    public void startGame(View view) {
        Intent intent = new Intent(this, Game.class);
        intent.putExtra("selectedPaintingId", String.valueOf(this.selectedPainting.getId()));
        startActivity(intent);
    }

    public void selectPainting(View view) {
        Painting painting = (Painting) view.getTag();
        this.selectedPainting = painting;
        ((TextView)findViewById(R.id.titleText)).setText(painting.getTitle());
        ((TextView)findViewById(R.id.artistText)).setText(painting.getArtist());
        DAO db = new  DAO(this);
        int all = db.countAllHiddenObjects(painting);
        int checked = db.countCheckedHiddenObjects(painting);
        db.close();
        String progress = String.valueOf(checked) + " / " + String.valueOf(all);
        ((TextView)findViewById(R.id.progressText)).setText(progress);
        LinearLayout listOfPaintings = findViewById(R.id.list_of_paintings);
        for(int i = 0; i < listOfPaintings.getChildCount(); i++) {
            listOfPaintings.getChildAt(i).setAlpha(1);
        }
        view.setAlpha((float)0.5);

        enableStartButton();
    }

    private void disableStartButton(){
        Button b = findViewById(R.id.startButton);
        b.setClickable(false);
        b.setAlpha(0);
    }

    private void enableStartButton(){
        Button b = findViewById(R.id.startButton);
        b.setClickable(true);
        b.setAlpha(1);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBackPressed() {
        this.finishAffinity();
    }

}
