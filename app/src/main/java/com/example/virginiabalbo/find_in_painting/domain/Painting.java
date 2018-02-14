package com.example.virginiabalbo.find_in_painting.domain;

/**
 * Created by virginia on 10/02/18.
 */

public class Painting {

    private Integer id; // primary key
    private String title, artist, imageSrc, thumbSrc;

    // Constructors
    public Painting(Integer id, String title, String artist, String imageSrc, String thumbSrc) {
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.imageSrc = imageSrc;
        this.thumbSrc = thumbSrc;
    }
    public Painting(String title, String artist, String imageSrc, String thumbSrc) {
        this.title = title;
        this.artist = artist;
        this.imageSrc = imageSrc;
        this.thumbSrc = thumbSrc;
    }

    // Getters
    public Integer getId() { return this.id; }
    public String getTitle() { return this.title; }
    public String getArtist() { return this.artist; }
    public String getImageSrc() { return this.imageSrc; }
    public String getThumbSrc() { return this.thumbSrc; }

    // Setters

}
