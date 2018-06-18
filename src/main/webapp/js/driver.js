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
$(document).ready(main);

function main() {

    $("#checkOutButton").click(checkOut);
    $("#checkInButton").click(checkIn);
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
    clearTimeout();
}

function parseSocketEvent(event) {

}
function socketClose(event) {
    websocket = false;
    switch (event.code) {
        case 1000 :
            alert("You have been logged out");
            localStorage.removeItem("token"); //delete the user token from storage
            window.history.back();
            break;
        case 4000 :
            alert("Connection to server gracefully terminated");
            break;
        default :
            fallbackPolling();
            break;
    }
}

function checkOut(event) {
    console.log(event, $(event));
    console.log($("#availableVehicles").val());
    $.ajax({
        url: "/OpenFleetr/vehicle/checking/" + $("#availableVehicles").val() + "?token=" + localStorage.getItem("token") + "",
        type: "GET",
        dataType: "json",
        success: handleCheckOut,
        error: genericError

    });

}

function checkIn(event) {
    console.log(event, $(event));
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
    console.log(json);
    checkedOut = true;
    socketConnect();
}

function handleCheckIn(json) {
    console.log(json);
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
    for(var item in json){
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
function pickedUp() {

}

function delivered() {

}

function getHospitalName() {

}

function getDriverName() {

}

function updateLocation(coords) {
    var latitude = coords.latitude;
    var longitude = coords.longitude;
    var postData = {"latitude": latitude ,"longitude": longitude};
    console.log(postData);
                
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
    if(checkedOut === true){
    console.log(event);
    updateLocation(event.coords);
}
}


function watchPositionError(event) {
    console.log(event);
}

