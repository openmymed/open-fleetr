var vehicles = [];
var vehicleMap;
var driversCache = [];
var dipsatcherName;
var notificationSocket;
var websocket = false;
var updateDriversTimeout;
var updateLocationsTimeout;
var updateStatusesTimeout;
var handling = false;
var socketCheckInterval;
var socketAttemptInterval;

$(document).ready(main);

function main() {

    var createCaseFormPopper = new Popper($('#createCaseButton'), $('#createCaseForm'), {
        placement: 'right'
    });
    var ambulanceStatusesPopper = new Popper($('#ambulanceStatusesButton'), $('#ambulanceStatusesList'), {
        placement: 'right'
    });
    var hospitalListPopper = new Popper($('#hospitalsButton'), $('#hospitalsList'), {
        placement: 'right'
    });

    $('#createCaseButton').click(createCaseFormDisplayControl);
    $('#createCaseFormCloseButton').click(createCaseFromClose);
    $('#createCaseFormConfirmButton').click(createCase);
    $('#ambulanceStatusesButton').click(ambulancesListControl);
    $('#hospitalsButton').click(hospitalsListControl);
    $('#jurisdictionsToggle').click(jurisdictionsToggleControl);
    $("#latitude").on("change", getReccomendations);
    $("#longitude").on("change", getReccomendations);

    loadMap();



}

function start() {
    updateDrivers();
    updateVehicles(); //start refreshing the vehicle locations
    getDispatcher();
    socketConnect();
}

function closeAll() {
    if (!handling) {
        $('#createCaseForm').hide();
        $('#ambulanceStatusesList').hide();
        $('#hospitalsList').hide();

    }
}

function ambulancesListControl() {
    if (!$('#ambulanceStatusesList').is(":visible")) {
        closeAll();
        $('#ambulanceStatusesList').show();
    } else {
        $('#ambulanceStatusesList').hide();
    }

}

function hospitalsListControl() {
    if (!$('#hospitalsList').is(":visible")) {
        closeAll();
        $('#hospitalsList').show();
    } else {
        $('#hospitalsList').hide();
    }
}

function jurisdictionsToggleControl(event) {

}

function parseSocketNotification(event) {
    var json = JSON.parse(event.data);
    console.log(json);
    switch (json.type) {
        case "location":
            json.array.forEach(fetchVehicle);
            break;
        case "status":
            json.array.forEach(fetchStatus);
            break;
        case "dispatchOrder":
            json.array.forEach(fetchDispatchOrder);
            break;
        case "notification":
            fetchNotification();
            break;
        case  "recommendation":
            handleRecomendations(json);
            break;
        default:
            break;
    }
}

function fetchStatus(vehicleId) {
    ;
}

function fetchDispatchOrder(orderId) {

}

function handleRecomendations(json) {
    var htmlString = "";
    json.array.forEach(function (item) {
        htmlString = htmlString + ('<option value ="' + item + '">Ambulance ' + item + '</option>\n');
    });
    $("#recomendationList").html(htmlString);
}
function fallbackPolling() {
    websocket = false;
    updateDrivers();
    updateVehicles(); //start refreshing the vehicle locations
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
        notificationSocket.send('');
    }
    checkSocketInterval();

}

function updateDrivers() {
    $.ajax({
        url: "/OpenFleetr/user/driver?token=" + localStorage.getItem("token") + "",
        type: "GET",
        dataType: "json",
        success: updateDriversSuccess,
        error: updateDriversError,
        complete: updateDriversInterval

    });
}

function updateDriversSuccess(data) {
    for (var driver in data) {
        var array = data[driver];
        driversCache[array.id.toString()] = "" + array.firstName + " " + array.lastName;
    }
}

function updateDriversError(jqHXR, textStatus, errorThrown) {
    if (jqHXR.status === 401 || jqHXR.status === 403) {
        alert("Please log in !");
        localStorage.removeItem("token");
        $(location).attr('href', '/OpenFleetr');
    } else {

    }
}

function updateVehicles() {
    $.ajax({
        url: "/OpenFleetr/vehicle?token=" + localStorage.getItem("token") + "",
        type: "GET",
        dataType: "json",
        success: updateVehiclesSuccess,
        error: fetchVehicleError,
        complete: updateVehiclesInterval
    });
}

function updateVehiclesSuccess(data) {
    for (var key in data) {
        if (data.hasOwnProperty(key)) {
            fetchVehicleSuccess(data[key]);
        }
    }
}

function fetchVehicle(vehicleId) {
    $.ajax({
        url: "/OpenFleetr/vehicle/" + vehicleId + "?token=" + localStorage.getItem("token") + "", //to this url
        type: "GET",
        dataType: "json",
        success: fetchVehicleSuccess,
        error: fetchVehicleError
    });
}
function fetchVehicleError(jqHXR, textStatus, errorThrown) {
    if (jqHXR.status === 401 || jqHXR.status === 403) {//check if the error is an authorisation or authentication error
        alert("Please log in !"); //alert for a login
        localStorage.removeItem("token"); //delete the user token from storage
        $(location).attr('href', '/OpenFleetr'); //go to the home page
    } else {

    }
}
function fetchVehicleSuccess(response) {
    if (!vehicles.hasOwnProperty(response.id.toString())) {
        vehicles[response.id.toString()] = L.marker([response.latitude, response.longitude]).addTo(vehicleMap);
    } else {
        vehicles[response.id.toString()].setLatLng([response.latitude, response.longitude]).update();
    }
    var statusText = "";
    switch (response.status) {
        case 0 :
            statusText = "Idle";
            break;
        case 1 :
            if (driversCache[response.toString()] === undefined && false) {
                clearTimeout(updateDriversTimeout);
                updateDrivers();
            }
            statusText = driversCache[response.driver.toString()] + "Available";
            break;
        case 2 :
            if (driversCache[response.toString()] === undefined && false) {
                clearTimeout(updateDriversTimeout);
                updateDrivers();
            }
            statusText = driversCache[response.driver.toString()] + "Busy";
            break;
        default :
            statusText = "Unavailable";
            break;
    }
    vehicles[response.id.toString()].bindPopup("" + statusText);
}
function updateVehiclesInterval() {
    if (websocket === false) {
        updateLocationsTimeout = setTimeout(updateVehicles, 1000);
    } else {
        if (updateLocationsTimeout !== undefined) {
            clearTimeout(updateLocationsTimeout);
        }
    }
}

function updateDriversInterval() {
    if (websocket === false) {
        updateDriversTimeout = setTimeout(updateDrivers, 30000);
    } else {
        if (updateDriversTimeout !== undefined) {
            clearTimeout(updateDriversTimeout);
        }
    }
}
function geolocationSuccess(position) {

    var crds = position.coords;
    var latitude = crds.latitude;
    var longitude = crds.longitude;
    vehicleMap = L.map('vehicleMapDiv', {
        zoomControl: false
    }).setView([latitude, longitude], 13);
    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
        attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
    }).addTo(vehicleMap);
    vehicleMap.on('click', closeAll);
    vehicleMap.on("click", getLatLng)
    vehicleMap.doubleClickZoom.disable();
    start();
}

function geolocationError() {
    vehicleMap = L.map('vehicleMapDiv', {
        zoomControl: false
    }).setView([31.7683, 35.2137], 13);
    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
        attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
    }).addTo(vehicleMap);
    vehicleMap.on('click', closeAll);
    vehicleMap.on("click", getLatLng);
    vehicleMap.doubleClickZoom.disable();
    start();

}

function loadMap() {
    var options = {
        enableHighAccuracy: true,
        timeout: 5000,
        maximumAge: 0
    };
    navigator.geolocation.getCurrentPosition(geolocationSuccess, geolocationError, options);

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

function socketConnect() {
    notificationSocket = new WebSocket("ws://" + location.host + "/OpenFleetr/notifications/" + localStorage.getItem("token"));
    notificationSocket.onopen = checkSocketInterval;
    notificationSocket.onmessage = parseSocketNotification;
    notificationSocket.onerror = socketError;
    notificationSocket.onclose = socketClose;
    websocket = true;
    clearTimeout();
}

function getDispatcher() {
    $.ajax({
        url: "/OpenFleetr/user/dispatcher?token=" + localStorage.getItem("token") + "",
        type: "GET",
        dataType: "json",
        success: getDispatcherSuccess,
        error: getDispatcherError
    });
}

function getDispatcherSuccess(data) {
    var dispatcher = data.firstName + " " + data.lastName;
    $("#dispatcherName").html(dispatcher);
}

function getDispatcherError(jqHXR, textStatus, errorThrown) {
    if (jqHXR.status === 401 || jqHXR.status === 403) {//check if the error is an authorisation or authentication error
        alert("Please log in !"); //alert for a login
        localStorage.removeItem("token"); //delete the user token from storage
        $(location).attr('href', '/OpenFleetr'); //go to the home page
    } else {
        $("#dispatcherName").html("Error");
    }
}


function createCaseFormDisplayControl() {
    if (!$('#createCaseForm').is(":visible")) {
        closeAll();
        handling = true;
        $('#createCaseForm').show();
    } else {
        handling = false;
        $('#createCaseForm').hide();

    }
}

function createCase() {
    return "ok";
}

function createCaseFromClose() {
    if (handling == true) {
        $('#fullName').val("");
        $('#phoneNumber').val("");
        $('#notes').val("");
        $('#latitude').val("");
        $('#longitude').val("");
        $("recommendationList").html("");
        createCaseFormDisplayControl();
    }

}

function fetchNotification() {
    $.ajax({
        url: "/OpenFleetr/user/driver?token=" + localStorage.getItem("token") + "",
        type: "GET",
        dataType: "json",
        success: fetchNotificationSuccess,
        error: fetchNotificationError,
    });
}

function fetchNotificationSuccess(response) {
}
function fetchNotificationError(response) {

}

function notificationCreateCase(event) {

}


function getLatLng(event) {
    if (handling == true) {
        $("#latitude").val(event.latlng.lat);
        $("#longitude").val(event.latlng.lng)
        getReccomendations();
    }

}


function getReccomendations() {
    if (websocket === true) {
        notificationSocket.send(JSON.stringify({
            latitude: $("#latitude").val(),
            longitude: $("#longitude").val()
        }));
    } else {
        fallbackPolling();
    }
}

