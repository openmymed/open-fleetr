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

$(document).ready(main);

function main() {

    loadMap();

}
function start() {
    socketConnect();
}

function socketConnect() {
    applicationSocket = new WebSocket("ws://" + location.host + "/OpenFleetr/app/driver" + localStorage.getItem("token"));
    applicationSocket.onopen = checkSocketInterval;
    applicationSocket.onmessage = parseSocketEvent;
    applicationSocket.onerror = socketError;
    applicationSocket.onclose = socketClose;
    websocket = true;
    clearTimeout();
}

function parseSocketEvent(event){
    
}
function socketClose(event) {
    websocket = false;
    switch (event.code) {
        case 1000 :
            alert("You have been logged out");
            localStorage.removeItem("token"); //delete the user token from storage
            window.history.back();
            break;
        default :
            fallbackPolling();
            break;
    }
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
    vehicleMap = L.map('map', {
        zoomControl: false
    }).setView([31.7683, 35.2137], 13);
    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
        attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
    }).addTo(vehicleMap);
    start();

}

function loadMap() {
    var options = {
        enableHighAccuracy: true,
        timeout: 15000,
        maximumAge: 0
    };
    navigator.geolocation.getCurrentPosition(geolocationSuccess, geolocationError, options);
    id = navigator.geolocation.watchPosition(watchPositionSuccess, watchPositionError,options);
}

function watchPositionSuccess(event){
    console.log(event,event.coords);
}


function watchPositionError(event){
    console.log(event);
}

