// Initialize and add the map
let map;

async function initMap() {
    // The location of Uluru
    // fetch location
    console.log("Generating map");
    let username = window.location.search;
    username = username.split("?")[1];
    let formData = new URLSearchParams();
    formData.append('username', username);
    let gpsData = "";
    await fetch("http://localhost:8080/SAFEty/api/user/gps",
        {
            method: "POST",
            body: formData,
            headers: {
                "Content-type" : "application/x-www-form-urlencoded"
            }
        }
    ).then(
        response => {
            return response.text();
        }
        ).then(
            data => {
                console.log(data);
                gpsData = data;
            }
    )
    let gpsDataSplit = gpsData.split(",");
    const latitude = parseFloat(gpsDataSplit[0]);
    console.log(latitude);
    const longitude = parseFloat(gpsDataSplit[1]);
    console.log(longitude);
    const position = { lat: latitude, lng: longitude };

    // Request needed libraries.
    //@ts-ignore
    const { Map } = await google.maps.importLibrary("maps");
    const { AdvancedMarkerElement } = await google.maps.importLibrary("marker");

    // The map, centered at Uluru
    map = new Map(document.getElementById("map"), {
        zoom: 4,
        center: position,
        mapId: "DEMO_MAP_ID",
    });

    // The marker, positioned at Uluru
    const marker = new AdvancedMarkerElement({
        map: map,
        position: position,
        title: "Uluru",
    });
}
initMap();

async function resetMap() {
    document.getElementById("map").remove();
    initMap();
}

