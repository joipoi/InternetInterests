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



    private void readFromFile(String fileName) {
        List<Map<String, Object>> list = new ArrayList<>();
        Map<String, Object> row;
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");;



        String fileNamePath = "src/main/resources/" + fileName;
        System.out.println("reading from " + fileName);
        Path pathToFile = Paths.get(fileNamePath);

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

        if(fileName.equals("mainMedia.csv")) {
            mediaList = new Media[list.size()];
        } else  if(fileName.equals("wishlist.csv")) {
            wishlist = new Media[list.size()];
        }

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

            if(fileName.equals("mainMedia.csv")) {
                mediaList[i] = new Media(name, type, link, stringDate, haveTried, rating);
            } else  if(fileName.equals("wishlist.csv")) {
                wishlist[i] = new Media(name, type, link, stringDate, haveTried, rating);
            }
        }

    }


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

    private void addMediaToFile(String name, String type, String link, String date, boolean haveTried, int rating, String fileName ){
        String writeString = name +  "," + type +  "," + link +  "," + date +  "," + haveTried +  "," + rating;
        writeToFile(fileName, writeString);
    }

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

    private void setMediaAsHaveTried(String mediaName){
       // System.out.println(mediaName);
        for(int i = 0; i < mediaList.length; i++) {
            if(mediaList[i].name.equals(mediaName)) {
                mediaList[i].haveTried = true;
            }
        }
        updateList("mainMedia.csv", mediaList);
    }

    private void removeMedia(String mediaName) {

        for(int i = 0; i < mediaList.length; i++) {
            if(mediaList[i].name.equals(mediaName)) {
                mediaList = removeFromArray(i, mediaList);
                removeFromWishlist(mediaName);
            }
        }
        updateList("mainMedia.csv", mediaList);

    }
    private void addToWishlist(String mediaName) {
        for(int i = 0; i < mediaList.length; i++) {
            if(mediaList[i].name.equals(mediaName)) {
                addMediaToFile(mediaList[i].name, mediaList[i].type, mediaList[i].link,
                        mediaList[i].stringDate, mediaList[i].haveTried, mediaList[i].rating, "wishlist.csv");

            }
        }
        readFromFile("wishlist.csv");
    }

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
    private void removeFromWishlist(String mediaName) {
        for(int i = 0; i < wishlist.length; i++) {
            if(wishlist[i].name.equals(mediaName)) {
                wishlist = removeFromArray(i, wishlist);
            }
        }
        updateList("wishlist.csv", wishlist);
    }



    //mappings for list page
    @GetMapping("/list{type}")
    public String idk(Model model, @RequestParam String type ) {
        readFromFile("mainMedia.csv");
        model.addAttribute("media", new Media());

        model.addAttribute("mediaList", mediaList);


        model.addAttribute("type", type);

        return "list";
    }


    @GetMapping("/list/removeMedia{mediaName}")
    public String aasdaaadfgdfgaa( Model model, @RequestParam String mediaName) {
        removeMedia(mediaName);
        readFromFile("mainMedia.csv");

        model.addAttribute("media", new Media());

        model.addAttribute("mediaList", mediaList);

        return "list";
    }
    @GetMapping("/list/haveTried{mediaName}")
    public String aasdaaadfgdfdsgdgfgaa( Model model, @RequestParam String mediaName) {
        setMediaAsHaveTried(mediaName);
        readFromFile("mainMedia.csv");

        model.addAttribute("media", new Media()); //do we need this?

        model.addAttribute("mediaList", mediaList);

        return "list";
    }
    @GetMapping("/list/addToWishlist{mediaName}")
    public String aasdaaaddfgdfgfgsgdgfgaa( Model model, @RequestParam String mediaName) {
        addToWishlist(mediaName);
        readFromFile("mainMedia.csv");

        model.addAttribute("media", new Media()); //do we need this?

        model.addAttribute("mediaList", mediaList);

        return "list";
    }

    //mappings for media page
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
    }

    //mappings for wishlist page
    @GetMapping("/wishlist")
    public String aasdsdfsaaaaa( Model model) {
        readFromFile("wishlist.csv");

        model.addAttribute("media", new Media());
        model.addAttribute("mediaList", wishlist);
        return "wishlist";
    }
    @GetMapping("/wishlist/removeMedia{mediaName}")
    public String aasdaaadfdfsgdggdfgaa( Model model, @RequestParam String mediaName) {
        removeFromWishlist(mediaName);
        readFromFile("wishlist.csv");

        model.addAttribute("media", new Media());

        model.addAttribute("mediaList", wishlist);

        return "wishlist";
    }

    //post mappings
    @PostMapping("/list/addMedia1")//fix name
    public String addMedia(@ModelAttribute Media media,  Model model) {

        model.addAttribute("media", media);
        addMediaToFile(media.name, media.type, media.link, media.stringDate, media.haveTried, media.rating, "mainMedia.csv");

        readFromFile("mainMedia.csv");

        model.addAttribute("mediaList", mediaList);


        return "list";
    }
}