package com.example.DBTest;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

//this class has methods for add/removing/changing/accessing data from the csv files: mainMedia.csv and wishlist.csv

public class Util {
    Media[] mediaList;
    Media[] wishlist;

    public Media[] getMediaList() {
        return mediaList;
    }

    public void setMediaList(Media[] mediaList) {
        this.mediaList = mediaList;
    }

    public Media[] getWishlist() {
        return wishlist;
    }

    public void setWishlist(Media[] wishlist) {
        this.wishlist = wishlist;
    }


    ///General methods

    //can read from either mainMedia.csv or wishlist.csv
    void readFromFile(String fileName) {
        List<Map<String, Object>> tempList = new ArrayList<>(); //this list will contain the full data from the csv file which will then go into mediaList or wishlist
        Map<String, Object> row;

        String fileNamePath = "src/main/resources/" + fileName;
        Path pathToFile = Paths.get(fileNamePath);

        try (BufferedReader br = Files.newBufferedReader(pathToFile,
                StandardCharsets.UTF_8)) {

            String line = br.readLine();
            List<String> headers = new ArrayList<>(Arrays.asList(line.split(",")));
            line = br.readLine();

            while (line != null ) {
                row = new HashMap<>();
                String[] attributes = line.split(",");

                for (int i = 0 ; i < headers.size() ; i ++) {
                    row.put(headers.get(i), attributes[i]);
                }
                tempList.add(row);
                line = br.readLine();
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        //checks if it's the mainMedia list or the wishlist(not a great solution)
        if(fileName.equals("mainMedia.csv")) {
            mediaList = new Media[tempList.size()];
        } else  if(fileName.equals("wishlist.csv")) {
            wishlist = new Media[tempList.size()];
        }

        //loops through tempList and adds data to mediaList/wishlist
        for(int i = 0; i < tempList.size(); i++) {
            String name = tempList.get(i).get("name").toString();
            String type = tempList.get(i).get("type").toString();
            String link = tempList.get(i).get("link").toString();
            String stringDate = tempList.get(i).get("date").toString();
            boolean haveTried;
            if(tempList.get(i).get("haveTried").toString().equals("true")) { //converts haveTried from String to boolean
                haveTried = true;
            } else {
                haveTried = false;
            }

            int rating = Integer.parseInt(tempList.get(i).get("rating").toString());

            if(fileName.equals("mainMedia.csv")) {
                mediaList[i] = new Media(name, type, link, stringDate, haveTried, rating);
            } else  if(fileName.equals("wishlist.csv")) {
                wishlist[i] = new Media(name, type, link, stringDate, haveTried, rating);
            }
        }

    }

    //updates the csv file so it matches the media array we have in this class: mediaList or wishlist
    private void updateList(String fileName, Media[] list) {
        try {
            FileWriter myWriter = new FileWriter("src/main/resources/" + fileName);
            BufferedWriter bw = new BufferedWriter(myWriter);
            bw.write("name" +  "," + "type" +  "," + "link" +  "," +
                    "date" +  "," + "haveTried" +  "," + "rating");
            for(int i = 0; i < list.length; i++) {
                bw.newLine();
                bw.write(list[i].name +  "," + list[i].type +  "," + list[i].link +  "," +
                        list[i].stringDate+  "," + list[i].haveTried +  "," + list[i].rating);
            }
            bw.close();
            myWriter.close();
            System.out.println("updated file");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    //returns a media from mediaList that matches the mediaName
     Media getMediaFromMediaName(String mediaName) {
        for(int i = 0; i < mediaList.length; i++) {
            if(mediaList[i].name.equals(mediaName)) {
                return mediaList[i];
            }
        }
        return null;
    }

    //uses writeToFile method to write to either mainMedia.csv or wishlist.csv
    void addMediaToFile(Media media, String fileName ){
        String writeString = media.name +  "," +  media.type +  "," + media. link +  "," +  media.stringDate +  "," +  media.haveTried +  "," +  media.rating;
        writeToFile(fileName, writeString);
    }

    //general purpose method for rating a number of Strings into a file(fileName)
    private void writeToFile(String fileName, String... lines) {
        try {
            FileWriter myWriter = new FileWriter("src/main/resources/" + fileName, true);
            BufferedWriter bw = new BufferedWriter(myWriter);
            bw.newLine();
            for (String line : lines) {
                bw.write(line);
            }
            bw.close();
            myWriter.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    //General method for removing a element from a array at an index
    private Media[] removeFromArray(int index, Media[] list) {

        Media[] newList = new Media[list.length-1];
        for(int i = 0, k = 0; i < list.length; i++) {
            if(i == index) {
                continue;
            }
            newList[k++] = list[i];
        }
        return newList;
    }
    //remove media from list(mediaList or wishlist) and updates file(fileName) [very messy method]
    void removeMediaFromList(String mediaName, String fileName, Media[] list) {

        for(int i = 0; i < list.length; i++) {
            if(list[i].name.equals(mediaName)) {
                list = removeFromArray(i, list);
            }
        }
        updateList(fileName, list);

    }

    ///mainMedia/mediaList methods

    //Changes the attribute 'haveTried' to true for the media with the name mediaName
     void setMediaAsHaveTried(String mediaName){
        // System.out.println(mediaName);
        for(int i = 0; i < mediaList.length; i++) {
            if(mediaList[i].name.equals(mediaName)) {
                mediaList[i].haveTried = true;
            }
        }
        updateList("mainMedia.csv", mediaList);
    }



}
