/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


var vehicleMap;
var applicationSocket;
var websocket = false;
var socketCheckInterval;
var socketAttemptInterval;
var checkedOut = false;
var currentOrder;
var myLocationMarker = undefined;
var caseMarker = undefined;
var hospitalMarker = undefined;
$(document).ready(main);

var blueAmbulanceMarker = L.AwesomeMarkers.icon({
    prefix: 'fa',
    icon: 'ambulance',
    markerColor: 'blue'
});
var greenHospitalMarker = L.AwesomeMarkers.icon({
    prefix: 'fa',
    icon: 'h-square',
    markerColor: 'green'
});
var redCaseMarker = L.AwesomeMarkers.icon({
    prefix: 'fa',
    icon: 'plus',
    markerColor: 'red'
});

function main() {

    $("#checkOutButton").click(checkOut);
    $("#checkInButton").click(checkIn);
    $("#statusButton").html("No order");
    $("#statusButton").off("click");

    getAvailableAmbulances();
    loadMap();


}
function start() {

}

function socketConnect() {
    applicationSocket = new WebSocket("wss://" + location.host + "/OpenFleetr/app/driver/" + localStorage.getItem("token"));
    applicationSocket.onopen = checkSocketInterval;
    applicationSocket.onmessage = parseSocketEvent;
    applicationSocket.onerror = socketError;
    applicationSocket.onclose = socketClose;
    websocket = true;
}

function parseSocketEvent(event) {
    var json = JSON.parse(event.data);
    switch(json.type){
        case "dispatchOrder":
            beginOrder(json.value);
            break;
        default :
            break;
    }
}

function beginOrder(orderId){
    currentOrder = orderId;
    getDispatchOrder(orderId);
    $("#statusButton").off("click");
    $("#statusButton").html("Picked Up");
    $("#statusButton").click(pickedUp);
}


function socketClose(event) {
    websocket = false;
    switch (event.code) {
        case 4000 :
            alert("Connection to server gracefully terminated");
            break;
        default :
            fallbackPolling();
            break;
    }
}

function checkOut(event) {
    $.ajax({
        url: "/OpenFleetr/vehicle/checking/" + $("#availableVehicles").val() + "?token=" + localStorage.getItem("token") + "",
        type: "GET",
        dataType: "json",
        success: handleCheckOut,
        error: genericError

    });

}

function checkIn(event) {
    $.ajax({
        url: "/OpenFleetr/vehicle/checking/1?token=" + localStorage.getItem("token") + "",
        type: "PUT",
        data: JSON.stringify({"something": "i know"}),
        dataType: "json",
        success: handleCheckIn,
        error: genericError

    });
}

function handleCheckOut(json) {
    checkedOut = true;
    socketConnect();
}

function handleCheckIn(json) {
    checkedOut = false;
    applicationSocket.close(4000);
}
function getAvailableAmbulances() {
    $.ajax({
        url: "/OpenFleetr/vehicle/checking?token=" + localStorage.getItem("token") + "",
        type: "GET",
        dataType: "json",
        success: handleAvailbleAmbulances,
        error: genericError

    });
}

function handleAvailbleAmbulances(json) {
    var htmlString = "";
    for (var item in json) {
        htmlString = htmlString + ('<option value ="' + item + '">Ambulance ' + item + '</option>\n');
    }
    $("#availableVehicles").html(htmlString);
}

function genericError(jqHXR, textStatus, errorThrown) {
    console.log(jqHXR, textStatus, errorThrown);
    if (jqHXR.status === 401 || jqHXR.status === 403) {
        alert("Please log in !");
        localStorage.removeItem("token");
        $(location).attr('href', '/OpenFleetr');
    } else {

    }
}
function pickedUp(event) {
    console.log(event);
  $.ajax({
        url: "/OpenFleetr/user/driver/dispatch/"+currentOrder+"?token=" + localStorage.getItem("token") + "",
        type: "GET",
        dataType: "json",
        success:continueOrder,
        error: genericError

    });
}

function continueOrder(json){
    
    $("#statusButton").html("Delivered");
    $("#statusButton").off("click");
    $("#statusButton").click(delivered);

}

function delivered(event) {
        console.log(event);

 $.ajax({
        url: "/OpenFleetr/user/driver/dispatch/"+currentOrder+"?token=" + localStorage.getItem("token") + "",
        type: "PUT",
        data: JSON.stringify({"something":"i know"}),
        dataType: "json",
        success: function(e){
                $("#statusButton").off("click");
                $("#statusButton").html("No order");
                vehicleMap.removeLayer(hospitalMarker);
                vehicleMap.removeLayer(caseMarker);
                caseMarker = undefined;
                hospitalMarker = undefined;
        },
        error: genericError

    });
}



function updateLocation(coords) {
    var latitude = coords.latitude;
    var longitude = coords.longitude;
    var postData = {"latitude": latitude, "longitude": longitude};
    $.ajax({//new ajax request
        url: "/OpenFleetr/vehicle?token=" + localStorage.getItem("token") + "", //to this url
        type: "POST", //HTTP request type get
        data: JSON.stringify(postData),
        datatype: 'json',
        success: function (response) {
            console.log(response);
        }, //on success, call updateLocationsSuccess
        error: genericError
    });
    myLocationMarker.setLatLng([latitude,longitude]).update();

}

function socketError(event) {
    console.log(event);
    fallbackPolling();
}

function fallbackPolling() {
    websocket = false;
    attemptSocketInterval();
}

function attemptSocketInterval() {
    if (websocket === false) {
        socketAttemptInterval = setTimeout(socketConnect, 3000);
    } else {
        clearTimeout(socketAttemptInterval);
    }
}

function checkSocketInterval(event) {
    socketCheckInterval = setTimeout(socketPing, 10000);
}

function socketPing() {
    if (websocket === true) {
        applicationSocket.send('');
    }
    checkSocketInterval();

}

function geolocationSuccess(position) {

    var crds = position.coords;
    var latitude = crds.latitude;
    var longitude = crds.longitude;
    vehicleMap = L.map('map', {
        zoomControl: false
    }).setView([latitude, longitude], 13);
    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
        attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
    }).addTo(vehicleMap);
    vehicleMap.doubleClickZoom.disable();
    myLocationMarker = L.marker([latitude, longitude], {icon: blueAmbulanceMarker}).addTo(vehicleMap);

    start();
}

function geolocationError() {
    alert("Geolocation Is nescarry for the functioning of this application, logging user out");
    localStorage.removeItem("token");
    $(location).attr('href', '/OpenFleetr');
}

function loadMap() {
    var options = {
        enableHighAccuracy: true,
        timeout: 15000,
        maximumAge: 0
    };
    navigator.geolocation.getCurrentPosition(geolocationSuccess, geolocationError, options);
    id = navigator.geolocation.watchPosition(watchPositionSuccess, watchPositionError, options);
}

function watchPositionSuccess(event) {
    if (checkedOut === true) {
        updateLocation(event.coords);
    }
}


function watchPositionError(event) {
}

function getDispatchOrder(orderId){
    $.ajax({//new ajax request
        url: "/OpenFleetr/vehicle/dispatch/" + orderId + "?token=" + localStorage.getItem("token") + "", //to this url
        type: "GET", //HTTP request type get
        datatype: 'json',
        success: getDispatchOrderSuccess,
        error: function (xhr, resp, text) {
        }
    });
}

function getDispatchOrderSuccess(json){
    var latitude  = json.startLatitude;
    var longitude = json.startLongitude;
    caseMarker = L.marker([latitude, longitude], {icon: redCaseMarker}).addTo(vehicleMap);
    getHospital(json.destinationHospitalId);

}


function getHospital(destinationHospitalId){
    
    $.ajax({//new ajax request
        url: "/OpenFleetr/hospital/"+destinationHospitalId+"?token=" + localStorage.getItem("token") + "", //to this url
        type: "GET", //HTTP request type get
        datatype: 'json',
        success: getHospitalSuccess,
        error: function (xhr, resp, text) {
            console.log(xhr,resp,text);
        }
    });

}

function getHospitalSuccess(json){
    var latitude = json.latitude;
    var longitude = json.longitude;
    hospitalMarker = L.marker([latitude, longitude], {icon: greenHospitalMarker}).addTo(vehicleMap);
    hospitalMarker.bindPopup(json.name);
}