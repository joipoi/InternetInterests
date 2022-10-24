package com.example.DBTest;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

public class Media {
    String name;
    String type;
    String link;
    String stringDate;
    Date date;
    boolean haveTried;
    int rating;

    public String getStringDate() {
        return stringDate;
    }

    public void setStringDate(String stringDate) {
        this.stringDate = stringDate;
    }

    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");;

    public boolean isHaveTried() {
        return haveTried;
    }

    public void setHaveTried(boolean haveTried) {
        this.haveTried = haveTried;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDate() {
        return stringDate;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Media(String name, String type, String link, String stringDate, boolean haveTried, int rating)  {
        this.name = name;
        this.type = type;
        this.link = link;
        this.stringDate = stringDate;
        this.haveTried = haveTried;
        this.rating = rating;
    }
    public Media() {

    }

    public String[] getStringArray() {
        String[] strings = new String[4];
        strings[0] = type;
        strings[1] = name;
        strings[2] = link;
        strings[3] = stringDate;
        return strings;

    }

    @Override
    public String toString() {
        return "Media{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", link='" + link + '\'' +
                ", Stringdate=" + stringDate +
                ", haveTried=" + haveTried +
                ", rating=" + rating +
                '}';
    }
}
