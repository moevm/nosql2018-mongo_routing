function getTileNum(lat, lon, zoom) {
    return [lat2tile(lat, zoom), long2tile(lon, zoom)]
}

function long2tile(lon, zoom) {
    return (Math.floor((lon + 180) / 360 * Math.pow(2, zoom)));
}

function lat2tile(lat, zoom) {
    return (Math.floor((1 - Math.log(Math.tan(lat * Math.PI / 180) + 1 / Math.cos(lat * Math.PI / 180)) / Math.PI) / 2 * Math.pow(2, zoom)));
}


function disablecontext(e) {

    var clickedEl = (e == null) ? event.srcElement.tagName : e.target.tagName;

    if (clickedEl == "IMG") {
        alert(errorMsg);
        return false;
    }
}

var errorMsg = "Вы не можете сохранять изображения с этого сайта.";


var map = L.map('map', {
    contextmenu: true,
    contextmenuWidth: 140,
    contextmenuItems: [{
        text: 'Show coordinates',
        callback: showCoordinates
    }],
    zoomControl: false,
}).setView([51.505, -0.09], 13);


function showCoordinates(e) {
    alert(e.latlng);
}

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


map.on('click', function (e) {
    console.log("Lat, Lon : " + e.latlng.lat + ", " + e.latlng.lng);
    var popup = L.popup()
        .setLatLng([e.latlng.lat, e.latlng.lng])
        .setContent("I am a standalone popup.")
        .openOn(map)
})

function drawLine(coordinates, color, weight, opacity, smooth) {
    let polyline = L.polyline(coordinates,
        {
            color: color,
            weight: weight,
            opacity: opacity,
            smoothFactor: smooth
        }).addTo(map);
    map.fitBounds(polyline.getBounds());
    return polyline;
}

drawLine([[59.991318, 30.319007], [59.994173, 30.316496], [59.977697, 30.317652], [59.972065, 30.307578]], 'red', 3, 0.5, 1);

function drawPolygon(coordinates, color, weight, opacity, smooth) {
    let polygon = L.polygon(coordinates,
        {
            color: color,
            weight: weight,
            opacity: opacity,
            smoothFactor: smooth
        }).addTo(map);
    map.fitBounds(polygon.getBounds());
    return polygon;
}

drawPolygon([[59.969732, 30.301888], [59.967111, 30.309849], [59.964157, 30.308540], [59.959576, 30.301448]], 'blue', 5, 0.5, 1);

var OpenStreetMap_Mapnik = L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
    maxZoom: 19,
    attribution: '&copy; <a href="http://www.openstreetmap.org/copyright">OpenStreetMap</a>'
});

var Wikimedia = L.tileLayer('https://maps.wikimedia.org/osm-intl/{z}/{x}/{y}{r}.png', {
    attribution: '<a href="https://wikimediafoundation.org/wiki/Maps_Terms_of_Use">Wikimedia</a>',
    minZoom: 1,
    maxZoom: 19
});

