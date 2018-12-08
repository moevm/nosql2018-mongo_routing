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
    console.log("Lat, Lon : " + e.latlng.lat + ", " + e.latlng.lng);
    var popup = L.popup()
        .setLatLng([e.latlng.lat, e.latlng.lng])
        .setContent("I am a standalone popup.")
        .openOn(map)
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





//функция поиска совпадений вводимых символов
function findEl(el, array, value) {
   var coincidence = false;
   el.empty();//очищаем список совпадений
   for (var i = 0; i < array.length; i++){
         if (array[i].match('^'+value)){//проверяем каждый елемент на совпадение побуквенно
            el.children('li').each(function (){//проверяем есть ли совпавшие елементы среди выведенных
               if(array[i] === $(this).text()) {
                  coincidence = true;//если есть совпадения то true
               }
            });
            if(coincidence === false){
               el.append('<li class="js-filter-address">'+array[i]+'</li>');//если нету совпадений то добавляем уникальное название в список
            }
         }
   }
}

var filterInput = $('#filter-address'),
   filterUl = $('.ul-addresses');

//проверка при каждом вводе символа
filterInput.bind('input propertychange', function(){
   if($(this).val() !== ''){
      filterUl.fadeIn(100);
      findEl(filterUl,$(this).data('address'),$(this).val());
   }
   else{
      filterUl.fadeOut(100);
   }
});

//при клике на елемент выпадалки присваиваем значение в инпут и ставим триггер на его изменение
filterUl.on('click','.js-filter-address', function(e){
   $('#filter-address').val('');
   filterInput.val($(this).text());
   filterInput.trigger('change');
   filterUl.fadeOut(100);


function addQuestion() {
        var newdiv = document.createElement("div");
        newdiv.innerHTML = "<div> fgb </div>";
         //newdiv.appendTo('div#quest');
         document.getElementById("parentId").appendChild(newdiv);
         return false;
  }
});



