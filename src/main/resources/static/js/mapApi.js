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

    $.ajax({
        url: "./api/route/node/near?x=" + e.latlng.lat + "&y=" + e.latlng.lng,//id1=659264823&id2=659264399
        success: function (data) {
            let dp = jQuery.parseJSON(data);
            console.log(data);
            var popup = L.popup()
                .setLatLng([dp.point.x, dp.point.y])
                .setContent("" + dp.point.x + " " + dp.point.y + ", " +dp.point.id+"<input type=\"button\" value=\"Отсюда\" class = \"from_to\" onClick=\"clickFrom(" + dp.point.x + ", " + dp.point.y +", " +dp.id+")\"> <input type=\"button\" value=\"Сюда\" class = \"from_to\" onClick=\"clickTo(" + dp.point.x + ", " + dp.point.y + ", " +dp.id+")\">")
                .openOn(map)


        }
    });
    /*var popup = L.popup()
        .setLatLng([e.latlng.lat, e.latlng.lng])
        .setContent("I am a standalone popup.")
        .openOn(map)*/
});

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

//drawLine([[59.991318, 30.319007], [59.994173, 30.316496], [59.977697, 30.317652], [59.972065, 30.307578]], 'red', 3, 0.5, 1);

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

//drawPolygon([[59.969732, 30.301888], [59.967111, 30.309849], [59.964157, 30.308540], [59.959576, 30.301448]], 'blue', 5, 0.5, 1);

var osmUrl1 = 'http://{s}.tile.osm.org/{z}/{x}/{y}.png';
var osmUrl2 = 'http://c.tiles.wmflabs.org/hillshading/{z}/{x}/{y}.png';
var osmUrl3 = 'http://www.openptmap.org/tiles/{z}/{x}/{y}.png';
/* Base Maps */
var grayscale = L.tileLayer(osmUrl1, {id: 'MapID', attribution: '1'});
var streets = L.tileLayer(osmUrl2, {id: 'MapID', attribution: '2'});
var metro = L.tileLayer(osmUrl3, {id: 'MapID', attribution: '3'});

var mixed = {
    "Grayscale": grayscale, // BaseMaps
    "Streets": streets, 		// BaseMaps
    "Metro": metro 				// BaseMaps
};

L.control.layers(null, mixed).addTo(map);

$('#filter-address').on('input', function (e) {
    console.log(e);
    $.ajax({
        url: "./api/tips/get?str=" + $('#filter-address').val(),
        success: function (data) {
            console.log(data);
            let dp = jQuery.parseJSON(data);

            $(".ul-addresses").empty();
            for (n in dp) {
                $(".ul-addresses").append("<li way=\"" + dp[n].wayId + "\">" + dp[n].name + "</li>");
            }
        }
    });
});

function buildRoute(from, to) {
    $.ajax({
        url: "./api/route/build?id1=" + from + "&id2=" + to,
        success: function (data) {
            console.log(data);
            let route = jQuery.parseJSON(data);
            let arr = [];
            for (let i in route.nodes) {
                arr.push([route.nodes[i].point.x, route.nodes[i].point.y]);
            }
            drawLine(arr, 'red', 3, 0.5, 1);
        }
    });
}


function getEventTarget(e) {
    e = e || window.event;
    return e.target || e.srcElement;
}

var ul = document.getElementById('test');
ul.onclick = function (event) {
    let target = getEventTarget(event);
    let wayId = target.getAttribute("way");
    $.ajax({
        url: "./api/way/get?id=" + wayId,//id1=659264823&id2=659264399
        success: function (data) {
            let p = jQuery.parseJSON(data);
            console.log(data);
             var popup = L.popup()
                .setLatLng([p.nodes[0].point.x, p.nodes[0].point.y])
                .setContent("" + p.nodes[0].point.x + " " + p.nodes[0].point.y + "<br>" + p.tags.name + "<br>" +"<input type=\"button\" value=\"Отсюда\" class = \"from_to\" onClick=\"clickFrom(" + p.nodes[0].point.x + ", " + p.nodes[0].point.y + ", '" + p.tags.name +", " + p.id");\"> <input type=\"button\" value=\"Сюда\" class = \"from_to\" onClick=\"clickTo(" + p.nodes[0].point.x + ", " + p.nodes[0].point.y +", '" + p.tags.name + ", " + p.id")\">")
                .openOn(map)
        }
    })
};


function clickGo(){



}


function clickCancel() {
    document.getElementById('div1').style.display = 'none';
    document.getElementById('filter-sidebar').style.display = 'block';
    document.getElementById('from').value = " ";
    document.getElementById('to').value = " ";
    return false;
}

function clickTo(x, y, name, id) {
    document.getElementById('div1').style.display = 'block';
    document.getElementById('filter-sidebar').style.display = 'none';
    document.getElementById('to').routeId = id;
    console.log(id);
 if (name === undefined) document.getElementById('to').value = x+" "+y;
 else document.getElementById('to').value = name;
    return false;

}

function clickFrom(x, y, name, id) {
    document.getElementById('div1').style.display = 'block';
    document.getElementById('filter-sidebar').style.display = 'none';
    document.getElementById('from').routeId = id;
if (name === undefined) document.getElementById('from').value = x+" "+y;
else document.getElementById('from').value = name;
    return false;
}

