var table 
var selectedRow;
var mediaList;
var haveTriedSelectBool;
var typeSelectValue
var tableDiv;
var isSimpleView = true;


function init(mediaList1) {
    tableDiv = document.getElementById("mediaTableDiv");
    table = document.getElementById("mediaTable");
    mediaList = mediaList1;
    console.log(mediaList);



    haveTriedSelectBool = document.getElementById("haveSeenSelect").value;
    typeSelectValue = document.getElementById("typeSelect").value;
    generateMediaTable(haveTriedSelectBool, typeSelectValue);
    selectedRow = table.rows[0];
    document.getElementById("addDiv").style.display = "none";
    document.getElementById("removeDiv").style.display = "none";

      var addMediaBtn = document.getElementById("addMediaBtn");

        var submitMediaBtn = document.getElementById("submitMediaBtn");

        var setAsHaveTriedBtn = document.getElementById("haveTriedBtn");

        var removeButton = document.getElementById("removeMediaBtn");

        var changeViewBtn = document.getElementById("changeViewBtn")

        submitMediaBtn.addEventListener("click", function(){
                              addRowToDB();
                          });

        addMediaBtn.addEventListener("click", function(){
            showAddMedia();
        });

        setAsHaveTriedBtn.addEventListener("click", function() {
            setAsHaveTried();
        });
       /* removeButton.addEventListener("click", function() {
                removeSelectedMedia();
            }); */

            changeViewBtn.addEventListener("click", function() {
                                      if(isSimpleView) {
                                        generateMediaTableAlt(typeSelectValue, haveTriedSelectBool);
                                      }else {
                                        generateMediaTable(typeSelectValue, haveTriedSelectBool);
                                      }
                                  });



}


//simple media list with table view
function generateMediaTable(typeSelect, haveTriedSelect) {

        if(typeSelect == "" || typeSelect == null) {
            typeSelect = "all";
            typeSelectValue = "all"
        }
        tableDiv.innerHTML = "";
        table = document.createElement('table');
        table.id = 'mediaTable';
        tableDiv.appendChild(table);

        var allTypes = false;
        var allHaveTried = false;

        if(typeSelect == "all") {
            allTypes = true;
        }
        if(haveTriedSelect == "all") {
            allHaveTried = true;
        }

        var tr = document.createElement('tr');
        tr.innerHTML = '   <th onclick="sortTable(0)">Name</th> <th onclick="sortTable(1)">Type</th>  <th onclick="sortTable(2)">Link</th>  <th onclick="sortTable(3)">Date</th>  <th onclick="sortTable(4)">haveTried</th>  <th onclick="sortTable(5)">Rating</th>';
        table.appendChild(tr);
    for(var row = 0; row < mediaList.length; row++) {

        if( (mediaList[row].type == typeSelect || allTypes) && (mediaList[row].haveTried == haveTriedSelect || allHaveTried)) {
            var tr = document.createElement('tr');

        tr.addEventListener("click", function() {
                    setAsSelected(this);

                 });

        var name = document.createElement('td');
        name.innerHTML = '<a href="media?mediaName=' + mediaList[row].name +  '">' + mediaList[row].name + '</a>';

        /* name.addEventListener("click", function() {
                            window.location.replace("media?mediaName=" + mediaList[row].name);
                         }); */

        tr.appendChild(name);
        var type = document.createElement('td');
        type.innerHTML = mediaList[row].type;
        tr.appendChild(type);
        var link = document.createElement('td');
        link.innerHTML = mediaList[row].link;
        tr.appendChild(link);
        var date = document.createElement('td');
        date.innerHTML =  mediaList[row].stringDate;
        tr.appendChild(date);
        var haveTried = document.createElement('td');
        haveTried.innerHTML = mediaList[row].haveTried;
        tr.appendChild(haveTried);
        var rating = document.createElement('td');
        rating.innerHTML = mediaList[row].rating;
        tr.appendChild(rating);

        table.appendChild(tr)
        }
    }
    isSimpleView = true;
}


//media list with images
function generateMediaTableAlt(typeSelect, haveTriedSelect) {

if(typeSelect == "" || typeSelect == null) {
            typeSelect = "all";
            typeSelectValue = "all"
        }

        tableDiv.innerHTML = "";

        //this is something
        var allTypes = false;
        var allHaveTried = false;

        if(typeSelect == "all") {
            allTypes = true;
        }
        if(haveTriedSelect == "all") {
            allHaveTried = true;
        }


        //here we make html elements


    for(var row = 0; row < mediaList.length; row++) {

        if( (mediaList[row].type == typeSelect || allTypes) && (mediaList[row].haveTried == haveTriedSelect || allHaveTried)) {
            var mediaDiv = document.createElement('div');
            var innerHtmlString = "";

            innerHtmlString += '<div class="mediaDiv"> <img src="' + getImgSrc(mediaList[row]) +  '"  width="150" height="225"> <p>' + mediaList[row].name +  '</p>';

            innerHtmlString += generateStarsFromRating(mediaList[row].rating);

            innerHtmlString += '</div>';

        mediaDiv.innerHTML = innerHtmlString;
        mediaDiv.addEventListener("click", function() {
        var name = this.getElementsByTagName('p')[0].innerHTML;
                    window.location.replace("media?mediaName=" + name);
                 });

        tableDiv.appendChild(mediaDiv);
        }
    }
    isSimpleView = false;

}

/*function linkToMediaPage(media) {
window.location.replace("media?name=" + media);
}*/
function generateMediaPage(mediaList2, media) {
var mediaPageDiv = document.getElementById("mediaPageDiv");
var innerHtmlString = "";
 innerHtmlString += '<div id="mediaBox"> <img src="' + getImgSrc(media) +  '"  width="100%" height="100%"> <p>' + media.name + " " + media.date;
innerHtmlString += generateStarsFromRating(media.rating);
innerHtmlString += '</p>  </div>';
 innerHtmlString += "<div id='mediaTextBox'> <p> Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum. </p> </div>"


 mediaPageDiv.innerHTML += innerHtmlString;

    for(var i = 0; i < mediaList2.length; i++) {
        if(mediaList2[i].name == media.name) {
            console.log(mediaList2[i].name + " type: " + mediaList2[i].type);
        }
    }

//img(poster), p(name), stars(rating), p(date), p(description), a(link), haveTried(??)
}

function generateStarsFromRating(rating) {
    var htmlStarString = "";
    switch(rating) {
                 case 0: htmlStarString = '<span class="fa fa-star"></span> <span class="fa fa-star"></span>  <span class="fa fa-star"></span> '
                 break;
                 case 1: htmlStarString = '<span class="fa fa-star checked"></span> <span class="fa fa-star"></span>  <span class="fa fa-star"></span> '
                 break;
                 case 2: htmlStarString = '<span class="fa fa-star checked"></span> <span class="fa fa-star checked"></span>  <span class="fa fa-star"></span> '
                 break;
                 case 3: htmlStarString = '<span class="fa fa-star checked"></span> <span class="fa fa-star checked"></span>  <span class="fa fa-star checked"></span> '
                 break;

}
return htmlStarString;

}

function getImgSrc(media) {
var imgSrc;
 switch(media.name) {
                case 'robocop':
                    imgSrc = "/images/robocop.jpg";
                    break;
                case "Alien":
                    imgSrc = "/images/alien.jpg"
                    break;
                default:
                imgSrc = "/images/" + media.type + ".jpg"
            }

         return imgSrc;
}

function haveSeenSelectFunc() {
    var haveSeenSelectValue = document.getElementById("haveSeenSelect").value;

    if(haveSeenSelectValue == "all") {
    haveTriedSelectBool = "all";
     if(isSimpleView) {
        generateMediaTable(typeSelectValue, haveTriedSelectBool);
        } else {
        generateMediaTableAlt(typeSelectValue, haveTriedSelectBool);
        }
    }else if(haveSeenSelectValue == "past") {
    haveTriedSelectBool = true;
          if(isSimpleView) {
             generateMediaTable(typeSelectValue, true);
             } else {
             generateMediaTableAlt(typeSelectValue, true);
             }
    }else if(haveSeenSelectValue == "future") {
    haveTriedSelectBool = false;
            if(isSimpleView) {
               generateMediaTable(typeSelectValue, false);
               } else {
               generateMediaTableAlt(typeSelectValue, false);
               }
         }



}
function typeSelectFunc() {
    typeSelectValue = document.getElementById("typeSelect").value;

    if(isSimpleView) {
    generateMediaTable(typeSelectValue, haveTriedSelectBool);
    } else {
    generateMediaTableAlt(typeSelectValue, haveTriedSelectBool);
    }


}

function showAddMedia() {
document.getElementById("haveTriedFormInput").value = "test";
     document.getElementById("mediaNameInput").value = "testi";
     console.log(document.getElementById("mediaNameInput").value + " hello hello");

if(document.getElementById("addDiv").style.display == "block") {
    document.getElementById("addDiv").style.display = "none";
}else {
    document.getElementById("addDiv").style.display = "block";

}


}
function addRowToDB() {

        var myForm = document.getElementById("myForm");

        /* document.getElementById("nameInput").value = selectedRow.children[0].innerHTML;
        document.getElementById("typeInput").value = selectedRow.children[1].innerHTML;
        document.getElementById("linkInput").value = selectedRow.children[2].innerHTML;
        document.getElementById("dateInput").value = selectedRow.children[3].innerHTML;
        document.getElementById("haveTriedInput").value = selectedRow.children[4].innerHTML;
        document.getElementById("ratingInput").value = selectedRow.children[5].innerHTML; */
              myForm.submit();
}
function removeSelectedMedia() {
var removeForm = document.getElementById("removeForm");
document.getElementById("mediaNameInput").value = selectedRow.children[0].innerText;

removeForm.submit();
}

function setAsHaveTried() {
 var myForm = document.getElementById("haveTriedForm");
         document.getElementById("haveTriedFormInput").value = selectedRow.children[0].innerText;

        myForm.action = "/haveTried"; //do i need this
              myForm.submit();

}

//stole this from the internet
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

function setAsSelected(rowObj) {

    selectedRow.style.backgroundColor = "white";

    selectedRow = rowObj;
    selectedRow.style.backgroundColor = "#997f7d";
}
function getSelectedRowText() {
return selectedRow.children[0].innerText;
}

function setAsSelectedDiv() {

}

//todo let user change database for example mark media as "havetried".
//todo make the table have a maxHeight so you scroll in a box instead of having to scroll the whole page.