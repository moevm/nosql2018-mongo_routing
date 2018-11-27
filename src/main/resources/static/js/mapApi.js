
function getTileNum(lat, lon, zoom) {
    return [lat2tile(lat, zoom), long2tile(lon, zoom)]
}

function long2tile(lon, zoom) {
    return (Math.floor((lon + 180) / 360 * Math.pow(2, zoom)));
}

function lat2tile(lat, zoom) {
    return (Math.floor((1 - Math.log(Math.tan(lat * Math.PI / 180) + 1 / Math.cos(lat * Math.PI / 180)) / Math.PI) / 2 * Math.pow(2, zoom)));
}

var map = L.map('map', {
    zoomControl: false
}).setView([51.505, -0.09], 13);

L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
    attribution: '<a>zMaps</a>'
}).addTo(map);

L.control.zoom({
    position: 'bottomright'
}).addTo(map);

function showPosition(position) {
    map.setView([position.coords.latitude, position.coords.longitude], 10);
}

if (navigator.geolocation) {
    navigator.geolocation.getCurrentPosition(showPosition);
} else {
    console.log("Geolocation is not supported by this browser.");
}


map.on('click', function(e) {
    console.log("Lat, Lon : " + e.latlng.lat + ", " + e.latlng.lng);
    var popup = L.popup()
        .setLatLng([e.latlng.lat, e.latlng.lng])
        .setContent("I am a standalone popup.")
        .openOn(map)
})


