var selectedRow;
var mediaList;
var haveTriedSelect;
var typeSelect;
var tableDiv;


function init(mediaListTemp) {
    tableDiv = document.getElementById("mediaTableDiv");
    mediaList = mediaListTemp;

    //checks if localStorage has been set, if not, it is the users first connection to the site, so we set localStorage to default values
    if(localStorage.getItem("simpleView") == null) {
        localStorage.setItem("simpleView","true" );
        localStorage.setItem("haveSeenSelect","all");
        localStorage.setItem("typeSelect","all");
    }
    //sets the values of the html select elements to match localStorage
    if(document.getElementById("haveSeenSelect").value != null) { //this is because im reusing code on different pages that dont have this element
        document.getElementById("haveSeenSelect").value = localStorage.getItem("haveSeenSelect");
    }

    document.getElementById("typeSelect").value = localStorage.getItem("typeSelect");

    //initializes html button variables and eventListeners for them which call methods
    var addMediaBtn = document.getElementById("addMediaBtn");
    var submitMediaBtn = document.getElementById("submitMediaBtn");
    var setAsHaveTriedBtn = document.getElementById("haveTriedBtn");
    var removeButton = document.getElementById("removeMediaBtn");
    var changeViewBtn = document.getElementById("changeViewBtn")

    if(addMediaBtn != null) { //addMediaBtn will be null when on wishlist page because that button is not on that page(maybe bad code)
        submitMediaBtn.addEventListener("click", function(){
            submitAddMedia();
        });
        document.getElementById("addDiv").style.display = "none";
        addMediaBtn.addEventListener("click", function(){
            showAddMedia();
        });
    }
    changeViewBtn.addEventListener("click", function() {
        if(localStorage.getItem("simpleView") === "true") {
            generateMediaTableAlt();
        }else {
            generateMediaTable();
        }
    });
}

//generates media based on if the user wants a "simpleView" or not
function generateCorrectMediaList() {
    if(localStorage.getItem("simpleView") === "true") {
        generateMediaTable();
    }else if(localStorage.getItem("simpleView") === "false") {
         generateMediaTableAlt();
    }

}

//simple media list with table view
function generateMediaTable() {
localStorage.setItem("simpleView","true");

    //local storage shows us what was being shown to the user the last time they looked at the mediaTable
    typeSelect = localStorage.getItem("typeSelect");
    haveTriedSelect = localStorage.getItem("haveSeenSelect");

    //resets the tableDiv and creates a new table for a new mediaList
    tableDiv.innerHTML = "";
    var table = document.createElement('table');
    table.id = 'mediaTable';
    tableDiv.appendChild(table);

    var tr = document.createElement('tr');
     //the first tableRow has the headers which include an onclick event which sorts the columns
    tr.innerHTML = '   <th onclick="sortTable(0)">Name</th> <th onclick="sortTable(1)">Type</th>  <th onclick="sortTable(2)">Link</th>  <th onclick="sortTable(3)">ViewedDate</th>  <th onclick="sortTable(4)">haveTried</th>  <th onclick="sortTable(5)">Rating</th>';
    table.appendChild(tr);

    //loops throught mediaList, shows media if it matches the 'type' and 'haveTried' attributes
    for(var row = 0; row < mediaList.length; row++) {
        if( (mediaList[row].type == typeSelect || typeSelect === "all") && (mediaList[row].haveTried == isStringTrue(haveTriedSelect) || haveTriedSelect === 'all')) {
            var tr = document.createElement('tr');

            //makes every row selectable
            tr.addEventListener("click", function() {
                setAsSelected(this);
            });

            var name = document.createElement('td');
            //clicking on the name brings you to the media page along with the mediaName as url paramter
            name.innerHTML = '<a href="media?mediaName=' + mediaList[row].name +  '">' + mediaList[row].name + '</a>';
            tr.appendChild(name);

            var type = document.createElement('td');
            type.innerHTML = mediaList[row].type;
            tr.appendChild(type);

            var link = document.createElement('td');
            link.innerHTML = "<a href=" +mediaList[row].link + ">"+ mediaList[row].link +  "</a>";
            tr.appendChild(link);

            var date = document.createElement('td');
            date.innerHTML =  mediaList[row].stringDate;
            tr.appendChild(date);

            var haveTried = document.createElement('td');
            haveTried.innerHTML = mediaList[row].haveTried;
            tr.appendChild(haveTried);

            var rating = document.createElement('td');
            //if the media has no rating we give it -1
            if(mediaList[row].rating == -1) {
                rating.innerHTML = ""
            }else{
                rating.innerHTML = mediaList[row].rating;
            }
            tr.appendChild(rating);

            table.appendChild(tr)
        }
    }
}
//media list with images
function generateMediaTableAlt() {
    localStorage.setItem("simpleView","false");

    typeSelect = localStorage.getItem("typeSelect");
    haveTriedSelect = localStorage.getItem("haveSeenSelect");

    tableDiv.innerHTML = "";

    //loops throught mediaList, shows media if it matches the 'type' and 'haveTried' attributes. adds image from getImgSrc method and rating from generateStarsFromRating
    for(var row = 0; row < mediaList.length; row++) {

        if( (mediaList[row].type == typeSelect || typeSelect === "all") && (mediaList[row].haveTried == isStringTrue(haveTriedSelect) || haveTriedSelect === 'all')) {
            var mediaDiv = document.createElement('div');
            var innerHtmlString = "";

            innerHtmlString += '<div class="mediaDiv"> <img src="' + getImgSrc(mediaList[row]) +  '"  width="150" height="225"> <p>' + mediaList[row].name +  '</p>';

            innerHtmlString += generateStarsFromRating(mediaList[row].rating);

            innerHtmlString += '</div>';

            mediaDiv.innerHTML = innerHtmlString;

            //clicking on a media brings you to media page with the mediaName as url paramater
            mediaDiv.addEventListener("click", function() {
                var name = this.getElementsByTagName('p')[0].innerHTML;
                window.location.replace("media?mediaName=" + name);
            });

            tableDiv.appendChild(mediaDiv);
        }
    }

}

function generateMediaPage(media) {
    //creates a div with data from media including img, name, date, link, description
    var mediaPageDiv = document.getElementById("mediaPageDiv");
    var innerHtmlString = "";
    innerHtmlString += '<div id="mediaBox"> <img src="' + getImgSrc(media) +  '"  width="100%" height="100%"> <p>' + media.name + "</p> <p>" + media.date + "</p> <p>";
    innerHtmlString += generateStarsFromRating(media.rating);
    innerHtmlString += " </p> <a href=" +media.link + ">"+ media.link +  "</a>"  + '</div>';
    innerHtmlString += "<div id='mediaTextBox'> <p>" + getDescription(media.name) +  "</p> </div>"
    mediaPageDiv.innerHTML += innerHtmlString;
}

//returns a string of html code which has stars based on rating. black star means no star, gold star means star. rating = 3 --> 3 gold stars
//the stars are stolen from the internet i link to another css page from my html pages
function generateStarsFromRating(rating) {
    var htmlStarString = "";
    var htmlBlackStar = '<span class="fa fa-star"></span>';
    var htmlGoldStar = '<span class="fa fa-star checked"></span>';
    switch(rating) {
        case 0:
            htmlStarString = htmlBlackStar + htmlBlackStar + htmlBlackStar;
            break;
        case 1:
            htmlStarString = htmlGoldStar + htmlBlackStar + htmlBlackStar;
            break;
        case 2:
            htmlStarString = htmlGoldStar + htmlGoldStar + htmlBlackStar;
            break;
        case 3:
            htmlStarString = htmlGoldStar + htmlGoldStar + htmlGoldStar;
            break;

    }
    return htmlStarString;
}

//the image icons are hardcoded because I don't have a database for images. would not look like this in production
function getImgSrc(media) {
    var imgSrc;
    switch(media.name) {
        case 'Robocop':
            imgSrc = "/images/robocop.jpg";
            break;
        case "Alien":
            imgSrc = "/images/alien.jpg"
            break;
        case "Skyrim":
            imgSrc = "/images/skyrim.jpg"
            break;
        case '1984':
            imgSrc = "/images/1984.jpg";
            break;
        case "Berserk":
            imgSrc = "/images/berserk.jpg"
            break;
        case "COMMUNITY":
            imgSrc = "/images/community.jpg"
            break;
        case "Ice Cream":
            imgSrc = "/images/IceCream.jpg"
            break;
        case "Sushi":
            imgSrc = "/images/sushi.jpg"
            break;
        default:
            imgSrc = "/images/" + media.type + ".jpg"
     }
     return imgSrc;
}
//the descriptions are hardcoded which would be bad if this was a real project. i just decided not to implement this properly
function getDescription(mediaName) {
    var description;
    switch(mediaName) {
        case 'Robocop':
            description = "I ett framtida Detroit är kriminaliteten extrem. Polisen har privatiserats och ägs av företaget OCP, som lanserar ett nytt vapen mot skurkarna: en polisrobot. När polismannen Alex Murphy har mördats av ett gatugäng får hans kropp ett skal av stål. Resultatet blir Robocop, som gör succé som brottsbekämpare. Men hans framgång gör att superskurken Boddicker måste göra allt för att röja honom ur vägen.Land: USA Robocop är en sci-fi-, action- och thrillerfilm producerad i USA och släpptes 1987. Den har ett utmärkt betyg på IMDb: 7.6 stjärnor av 10. Den är en långfilm med en speltid på 1h 42min. Robocop finns nu att hyra och att köpa på iTunes, SF Anytime, Google Play och Blockbuster. Klicka på en länk för att se den nu!"
            break;
        case "COMMUNITY":
            description = "När Jeff Wingers juristexamen visar sig vara förfalskad tvingas han tillbaka till college, där han bildar en udda studiegrupp. (Ett avsnitt saknas i säsong 2.)Land: USACommunity är en komediserie producerad i USA och släpptes 2009. Den har ett mycket högt betyg på IMDb: 8.5 stjärnor av 10. Community finns nu att streama på Netflix och Viaplay. Klicka på en länk för att se den nu!"
            break;
        case "Skyrim":
            description = "Winner of more than 200 Game of the Year Awards, Skyrim Special Edition brings the epic fantasy to life in stunning detail. The Special Edition includes the critically acclaimed game and add-ons with all-new features like remastered art and effects, volumetric god rays, dynamic depth of field, screen-space"
            break;
        case '1984':
            description = "Nineteen Eighty-Four (also stylised as 1984) is a dystopian social science fiction novel and cautionary tale written by the English writer George Orwell. It was published on 8 June 1949 by Secker & Warburg as Orwell's ninth and final book completed in his lifetime. Thematically, it centres on the consequences of totalitarianism, mass surveillance and repressive regimentation of people and behaviours within society.[2][3] Orwell, a democratic socialist, modelled the authoritarian state in the novel on Stalinist Russia and Nazi Germany.[2][3][4] More broadly, the novel examines the role of truth and facts within societies and the ways in which they can be manipulated.";
            break;
        case "Berserk":
            description = "Berserk (Japanese: ベルセルク, Hepburn: Beruseruku) is a Japanese manga series written and illustrated by Kentaro Miura. Set in a medieval Europe-inspired dark fantasy world, the story centers on the characters of Guts, a lone swordsman, and Griffith, the leader of a mercenary band called the Band of the Hawk. Miura premiered a prototype of Berserk in 1988. The series began the following year in the Hakusensha's now-defunct magazine Monthly Animal House, which was replaced in 1992 by the semimonthly magazine Young Animal, where Berserk has continued its publication. "
            break;
        case "COMMUNITY":
            description = "/images/community.jpg"
            break;
        case "Ice Cream":
            description = "i want to make ice cream"
            break;
        case "Sushi":
            description = "sushi is from japan and is very tasty"
            break;
        default:
            description = "no description found"
    }

    return description;
}

//gets called when 'haveSeenSelect's value changes, sets localStorage for the haveSeenSelect value and refreshes the media list
function haveSeenSelectFunc() {
    var haveSeenSelectValue = document.getElementById("haveSeenSelect").value;

    if(haveSeenSelectValue === "all") {
        localStorage.setItem("haveSeenSelect",haveSeenSelectValue );
    }else if(haveSeenSelectValue === "true") {
        localStorage.setItem("haveSeenSelect",true );
    }else if(haveSeenSelectValue === "false") {
        localStorage.setItem("haveSeenSelect",false );
    }
   generateCorrectMediaList();
}
//gets called when 'typeSelect's value changes, sets localStorage for the typeSelect value and refreshes the media list
function typeSelectFunc() {
    typeSelect = document.getElementById("typeSelect").value;
    localStorage.setItem("typeSelect",typeSelect);

    generateCorrectMediaList();
}

//shows the html elements that lets the user add a media to the mediaList
function showAddMedia() {
    if(document.getElementById("addDiv").style.display == "block") {
        document.getElementById("addDiv").style.display = "none";
    }else {
        document.getElementById("addDiv").style.display = "block";
    }
}
//submits the form that adds media to mediaList
function submitAddMedia() {
        var myForm = document.getElementById("myForm");
        myForm.submit();
}

//sort an html table either alphabetically or numerically. n = index of clicked element. (stole this from the internet)
function sortTable(n) {
    var rows, switching, i, x, y, shouldSwitch, dir, switchcount = 0;
    switching = true;
    // Set the sorting direction to ascending:
    dir = "asc";
    /* Make a loop that will continue until
    no switching has been done: */
    while (switching) {
      // Start by saying: no switching is done:
      switching = false;
      rows = table.rows;
      /* Loop through all table rows (except the
      first, which contains table headers): */
      for (i = 1; i < (rows.length - 1); i++) {
        // Start by saying there should be no switching:
        shouldSwitch = false;
        /* Get the two elements you want to compare,
        one from current row and one from the next: */
        x = rows[i].getElementsByTagName("TD")[n];
        y = rows[i + 1].getElementsByTagName("TD")[n];
        /* Check if the two rows should switch place,
        based on the direction, asc or desc: */
        if (dir == "asc") {
          if (x.innerHTML.toLowerCase() > y.innerHTML.toLowerCase()) {
            // If so, mark as a switch and break the loop:
            shouldSwitch = true;
            break;
          }
        } else if (dir == "desc") {
          if (x.innerHTML.toLowerCase() < y.innerHTML.toLowerCase()) {
            // If so, mark as a switch and break the loop:
            shouldSwitch = true;
            break;
          }
        }
      }
      if (shouldSwitch) {
        /* If a switch has been marked, make the switch
        and mark that a switch has been done: */
        rows[i].parentNode.insertBefore(rows[i + 1], rows[i]);
        switching = true;
        // Each time a switch is done, increase this count by 1:
        switchcount ++;
      } else {
        /* If no switching has been done AND the direction is "asc",
        set the direction to "desc" and run the while loop again. */
        if (switchcount == 0 && dir == "asc") {
          dir = "desc";
          switching = true;
        }
      }
    }
  }

//sets the clicked row of the mediaTable as selected with a variable and some style
function setAsSelected(rowObj) {
    //if a row is selected already, make it white so as to de-select
    if(selectedRow != null) {
        selectedRow.style.backgroundColor = "white";
    }
    selectedRow = rowObj;
    selectedRow.style.backgroundColor = "#997f7d";
}

//i call this from an html page in the script tag, cause i cant access variables there.
function getSelectedRowText() {
return selectedRow.children[0].innerText;
}
//general function for converting string to bool
function isStringTrue(s) {
    if(s === 'true') {
        return true;
    }
    else {
        return false;
    }
}
