<img src="src/main/resources/static/images/1984.jpg" align="right" />

# Media List Web UI
## Introduction
This is a Web UI where you can create lists of media. 
Media includes movie, tvshow, book, manga, music, cooking, baking.
You can include both media you wish to see/try in the future or that you 
have already seen/tried.

## Languages/libraries
This project uses Java Spring Boot and Maven.
The Spring libraries used are: Spring Web for running and mapping the WebUI and Thymeleaf for html template rendering,


## Classes/Pages/files
Includes java classes 
- MainFrame.java for running program and creating jFrame for starting webUI
- Mapping.java for mapping urls to java methods
- Media.java is a class representing a media object
- Util.java has methods for accessing the list of media in csv files. Also has some general util methods.
Includes html pages
- mainPage.html which has the main media list and links to the other pages
- media.html which is a page for viewing one specific media
- wishlist.html is similar to mainPage but is a different media list called wishlist
Includes csv files:
- mainMedia.csv is the main list of media 
- wishlist.csv is also lsit of medias but just for wishlist

## How to Launch
Either clone with git or download as zip file and extract.
Then run the file called MediaList.jar

##Media class
Contains fields
- String name
- String type
- String link
- String stringDate
- Boolean haveTried
- int rating
