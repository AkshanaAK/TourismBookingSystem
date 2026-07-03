const images=[

    "images/tourism1.jpg",
    "images/tourism2.jpg",
    "images/tourism3.jpg",
    "images/tourism4.jpg",
    "images/tourism5.jpg",
    "images/tourism6.jpg",
    "images/tourism7.jpg",
    "images/tourism8.jpg",
    "images/tourism9.jpg",
    "images/tourism10.jpg",
    "images/tourism11.jpg"


];

let current=0;


function changeBackground(){

    document.body.style.backgroundImage=

        `url('${images[current]}')`;

    current++;

    if(
        current>=images.length
    ){

        current=0;

    }

}


changeBackground();

setInterval(

    changeBackground,

    4000

);
if (params.get("status") === "duplicate") {
    document.getElementById("duplicateMessage").style.display = "block";
}