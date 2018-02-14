package com.example.virginiabalbo.find_in_painting.domain;

/**
 * Created by virginia on 10/02/18.
 */

public class HiddenObject {

    private Integer id; // primary key
    private Integer paintingId; // foreign key
    private String description;
    private Integer xPosition, yPosition, width, height;
    private Integer checked; // 0 or 1

    // Constructors
    public HiddenObject (Integer id, Integer paintingId,
                         String description,
                         Integer xPosition, Integer yPosition, Integer width, Integer height, Integer checked) {
        this.id = id;
        this.paintingId = paintingId;
        this.description = description;
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.width = width;
        this.height = height;
        this.checked = checked;
    }
    public HiddenObject (Integer paintingId,
                         String description,
                         Integer xPosition, Integer yPosition, Integer width, Integer height) {
        this.paintingId = paintingId;
        this.description = description;
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.width = width;
        this.height = height;
        this.checked = 0;
    }

    // Getters
    public Integer getId() { return this.id; }
    public Integer getPaintingId() { return this.paintingId; }
    public String getDescription() { return this.description; }
    public Integer getXPosition() { return this.xPosition; }
    public Integer getYPosition() { return this.yPosition; }
    public Integer getWidth() { return this.width; }
    public Integer getHeight() { return this.height; }
    public Integer getChecked() { return this.checked; }

    public boolean isChecked(){
        return (this.checked == 1);
    }
}
