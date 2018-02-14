package com.example.virginiabalbo.find_in_painting;

import android.content.Context;

import com.example.virginiabalbo.find_in_painting.dao.DAO;
import com.example.virginiabalbo.find_in_painting.domain.HiddenObject;
import com.example.virginiabalbo.find_in_painting.domain.Painting;

import java.io.InputStream;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by virginia on 11/02/18.
 */

public class Gallery{

    public static boolean isCreated(Context context){
        DAO db = new DAO(context);
        List<Painting> paintings = db.listPaintings();
        db.close();
        return (paintings.size() != 0);
    }

    public static void create(Context context) {
        InputStream file = context.getResources().openRawResource(R.raw.gallery);
        Scanner fileIn = new Scanner(file);
        addValues(fileIn, context);
    }

    private static void addValues(Scanner fileIn, Context context) {
        while(fileIn.hasNextLine()) {
            String title = fileIn.nextLine();
            String artist = fileIn.nextLine();
            Integer paintingId = addPainting(title, artist, context);
            String line = fileIn.nextLine();
            while((!line.isEmpty()) && fileIn.hasNextLine()){
                addHiddenObject(line, paintingId, context);
                line = fileIn.nextLine();
            }
        }
    }

    private static Integer addPainting(String title, String artist, Context context){
        String imageSrc = "@drawable/" + title.toLowerCase().replace(" ", "_");
        String thumbSrc = "@drawable/" + title.toLowerCase().replace(" ", "_") + "_thumb";
        Painting painting = new Painting(title, artist, imageSrc, thumbSrc);
        DAO db = new DAO(context);
        Integer id = (int)(long) db.insertPainting(painting);
        db.close();
        return id;
    }

    private static void addHiddenObject(String line, Integer paintingId, Context context){
        String regex = "([a-zA-Z0-9\\s]+) ([0-9]+) ([0-9]+) ([0-9]+) ([0-9]+)";
        Matcher m = Pattern.compile(regex).matcher(line);
        m.find();
        String description = m.group( 1 );
        Integer xPosition = Integer.parseInt(m.group( 2 ));
        Integer yPosition = Integer.parseInt(m.group( 3 ));
        Integer width = Integer.parseInt(m.group( 4 ));
        Integer height = Integer.parseInt(m.group( 5 ));
        HiddenObject hiddenObject = new HiddenObject(paintingId, description, xPosition, yPosition, width, height);
        DAO db = new DAO(context);
        db.insertHiddenObject(hiddenObject);
        db.close();
    }
}