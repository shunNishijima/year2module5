function loadUsername() {
    let username = window.location.search;
    username = username.split("?")[1];
    console.log(username);
    document.getElementById("userTitle").innerHTML = "        <a id=\"userTitle\"><img class=\"user-image\" src=\"userLogo.png\"> " + username +  "</a>\n";
}