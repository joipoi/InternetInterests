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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class Mapping {
    Media[] mediaList;
    Media[] wishlist;



    private void readFromFile() {
        List<Map<String, Object>> list = new ArrayList<>();
        Map<String, Object> row;
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");;



        String fileName = "src/main/resources/mainMedia.csv";
        Path pathToFile = Paths.get(fileName);

        try (BufferedReader br = Files.newBufferedReader(pathToFile,
                StandardCharsets.UTF_8)) {

            String line = br.readLine();
            List<String> headers = new ArrayList<>(Arrays.asList(line.split(",")));
            line = br.readLine();

            while (line != null ) {
                row = new HashMap<String, Object>();

                String[] attributes = line.split(",");
                for (int i = 0 ; i < headers.size() ; i ++) {
                    row.put(headers.get(i), attributes[i]);
                }
                list.add(row);



                line = br.readLine();
            }

        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        mediaList = new Media[list.size()];

        for(int i = 0; i < list.size(); i++) {
            String name = list.get(i).get("name").toString();
            String type = list.get(i).get("type").toString();
            String link = list.get(i).get("link").toString();
            String stringDate = list.get(i).get("date").toString();
            boolean haveTried;
            if(list.get(i).get("haveTried").toString().equals("true")) {
                haveTried = true;
            } else {
                haveTried = false;
            }

            int rating = Integer.parseInt(list.get(i).get("rating").toString());

            mediaList[i] = new Media(name, type, link, stringDate, haveTried, rating);
        }

    }
    private void readFromWishlist() {
        List<Map<String, Object>> list = new ArrayList<>();
        Map<String, Object> row;
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");;



        String fileName = "src/main/resources/wishlist.csv";
        Path pathToFile = Paths.get(fileName);

        try (BufferedReader br = Files.newBufferedReader(pathToFile,
                StandardCharsets.UTF_8)) {

            String line = br.readLine();
            List<String> headers = new ArrayList<>(Arrays.asList(line.split(",")));
            line = br.readLine();

            while (line != null ) {
                row = new HashMap<String, Object>();

                String[] attributes = line.split(",");
                for (int i = 0 ; i < headers.size() ; i ++) {
                    row.put(headers.get(i), attributes[i]);
                }
                list.add(row);



                line = br.readLine();
            }

        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        wishlist = new Media[list.size()];

        for(int i = 0; i < list.size(); i++) {
            String name = list.get(i).get("name").toString();
            String type = list.get(i).get("type").toString();
            String link = list.get(i).get("link").toString();
            String stringDate = list.get(i).get("date").toString();
            boolean haveTried;
            if(list.get(i).get("haveTried").toString().equals("true")) {
                haveTried = true;
            } else {
                haveTried = false;
            }

            int rating = Integer.parseInt(list.get(i).get("rating").toString());

            wishlist[i] = new Media(name, type, link, stringDate, haveTried, rating);
        }

    }

    private void printMediaList() {
        for (Media media:mediaList) {
            System.out.println(media.toString());
        }
    }
    private void updateList() { //this fucks everything, date + extra line
        try {
            FileWriter myWriter = new FileWriter("src/main/resources/mainMedia.csv");
            BufferedWriter bw = new BufferedWriter(myWriter);
            bw.write("name" +  "," + "type" +  "," + "link" +  "," +
                    "date" +  "," + "haveTried" +  "," + "rating");
            for(int i = 0; i < mediaList.length; i++) {
                bw.newLine();
                bw.write(mediaList[i].name +  "," + mediaList[i].type +  "," + mediaList[i].link +  "," +
                        mediaList[i].stringDate+  "," + mediaList[i].haveTried +  "," + mediaList[i].rating);
            }
            bw.close();
            myWriter.close();
            System.out.println("updated file");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    private void addMediaToFile(String name, String type, String link, String date, boolean haveTried, int rating ){
        try {
            FileWriter myWriter = new FileWriter("src/main/resources/mainMedia.csv", true);
            BufferedWriter bw = new BufferedWriter(myWriter);
            bw.newLine();
            String writeString = name +  "," + type +  "," + link +  "," + date +  "," + haveTried +  "," + rating;
            bw.write(writeString);
            bw.close();
            myWriter.close();
            System.out.println("Wrote to file: " + writeString);
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

    }

    private void setMediaAsHaveTried(String mediaName){
       // System.out.println(mediaName);
        for(int i = 0; i < mediaList.length; i++) {
            if(mediaList[i].name.equals(mediaName)) {
                mediaList[i].haveTried = true;
            }
        }
        updateList();
    }

    private void removeMedia(String mediaName) {

        for(int i = 0; i < mediaList.length; i++) {
            if(mediaList[i].name.equals(mediaName)) {
                mediaList = removeFromArray(i);
            }
        }
        updateList();

    }
    private Media[] removeFromArray(int index) {

        Media[] newList = new Media[mediaList.length-1];
        for(int i = 0, k = 0; i < mediaList.length; i++) {
            if(i == index) {
                continue;
            }
            newList[k++] = mediaList[i];
        }
        return newList;


    }

    @GetMapping("/wishlist")
    public String aasdsdfsaaaaa( Model model) {
        readFromWishlist();

        model.addAttribute("media", new Media());
        model.addAttribute("wishlist", wishlist);
        return "wishlist";
    }


    @GetMapping("/list{type}")
    public String idk(Model model, @RequestParam String type ) {
        readFromFile();
        model.addAttribute("media", new Media());

        model.addAttribute("mediaList", mediaList);


        model.addAttribute("type", type);

        return "list";
    }
    @GetMapping("/media{mediaName}")
    public String aasdaaaaa( Model model, @RequestParam String mediaName) {
        Media media = null;

        for(int i = 0; i < mediaList.length; i++) {
            if(mediaList[i].name.equals(mediaName)) {
                media = mediaList[i];
            }
        }
        if(media == null) {
            System.out.println("IT IS NULL");
        }

        model.addAttribute("media", media);
        model.addAttribute("mediaList", mediaList);
        return "media";
    } //

    @GetMapping("/list/removeMedia{mediaName}")
    public String aasdaaadfgdfgaa( Model model, @RequestParam String mediaName) {
        removeMedia(mediaName);
        readFromFile();

        model.addAttribute("media", new Media());

        model.addAttribute("mediaList", mediaList);

        return "list";
    }
    @GetMapping("/list/haveTried{mediaName}")
    public String aasdaaadfgdfdsgdgfgaa( Model model, @RequestParam String mediaName) {
        setMediaAsHaveTried(mediaName);
        readFromFile();

        model.addAttribute("media", new Media()); //do we need this?

        model.addAttribute("mediaList", mediaList);

        return "list";
    }

    @PostMapping("/list/addMedia1")//fix name
    public String addMedia(@ModelAttribute Media media,  Model model) {

        model.addAttribute("media", media);
        addMediaToFile(media.name, media.type, media.link, media.stringDate, media.haveTried, media.rating);

        readFromFile();

        model.addAttribute("mediaList", mediaList);


        return "list";
    }
}