package com.example.DBTest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

//This class uses java spring to map url endpoints to a method.
//@GetMapping is used for get requests and  @PostMapping is used for post requests
//@RequestParam is used to get the url parameters if present
//model.addAttribute is used to send data to the front end and can be accesed in the html or javascript code
@Controller
public class Mapping {
    Util util = new Util();

    //mappings for list page
    @GetMapping("/mainPage{type}")
    public String generateListPage(Model model, @RequestParam String type ) {
        util.readFromFile("mainMedia.csv");
        model.addAttribute("media", new Media());

        model.addAttribute("mediaList", util.mediaList);

        model.addAttribute("type", type);

        return "mainPage";
    }

    @GetMapping("/mainPage/removeMedia{mediaName}")
    public String removeMedia( Model model, @RequestParam String mediaName) {
        util.removeMediaFromList(mediaName, "mainMedia.csv", util.mediaList);
        util.readFromFile("mainMedia.csv");

        model.addAttribute("media", new Media());

        model.addAttribute("mediaList", util.mediaList);

        return "mainPage";
    }
    @GetMapping("/mainPage/haveTried{mediaName}")
    public String setMediaAsHaveTried( Model model, @RequestParam String mediaName) {
        util.setMediaAsHaveTried(mediaName);
        util.readFromFile("mainMedia.csv");

        model.addAttribute("media", new Media());

        model.addAttribute("mediaList", util.mediaList);

        return "mainPage";
    }
    @GetMapping("/mainPage/addToWishlist{mediaName}")
    public String addToWishlist( Model model, @RequestParam String mediaName) {
        util.addMediaToFile(util.getMediaFromMediaName(mediaName), "wishlist.csv");
        util.readFromFile("mainMedia.csv");

        model.addAttribute("media", new Media());

        model.addAttribute("mediaList", util.mediaList);

        return "mainPage";
    }

    //mappings for media page
    @GetMapping("/media{mediaName}")
    public String generateMediaPage( Model model, @RequestParam String mediaName) {
        Media media = util.getMediaFromMediaName(mediaName);

        model.addAttribute("media", media);
        return "media";
    }

    //mappings for wishlist page
    @GetMapping("/wishlist")
    public String generateWishlist( Model model) {
        util.readFromFile("wishlist.csv");

        model.addAttribute("media", new Media());
        model.addAttribute("mediaList", util.wishlist);
        return "wishlist";
    }
    @GetMapping("/wishlist/removeMedia{mediaName}")
    public String removeFromWishlist( Model model, @RequestParam String mediaName) {
        util.removeMediaFromList(mediaName, "wishlist.csv", util.wishlist);
        util.readFromFile("wishlist.csv");

        model.addAttribute("media", new Media());

        model.addAttribute("mediaList", util.wishlist);

        return "wishlist";
    }

    //post mappings
    @PostMapping("/mainPage/addMedia")
    public String addMediaToMainList(@ModelAttribute Media media,  Model model) {

        model.addAttribute("media", media);
        util.addMediaToFile(media, "mainMedia.csv");

        util.readFromFile("mainMedia.csv");

        model.addAttribute("mediaList", util.mediaList);

        return "mainPage";
    }
}