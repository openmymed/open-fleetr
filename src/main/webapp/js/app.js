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
    updateLocations(); //start refreshing the vehicle locations
    updateStatuses();
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

function jurisdictionsToggleControl() {}
function parseSocketNotification(event) {
    console.log(event.data);
    var json = JSON.parse(event.data);
    console.log(json);
    switch (json.type) {
        case "location":
            json.array.forEach(fetchLocation);
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

function fetchStatus(vehicleId){
    console.log(vehicleId);
}

function fetchDispatchOrder(orderId){
    console.log(orderId);
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
    updateLocations(); //start refreshing the vehicle locations
    updateStatuses();
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
    var driver;
    var json = JSON.parse(data)
    for (driver in json) {
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

function updateLocations() {
    $.ajax({
        url: "/OpenFleetr/vehicle/location?token=" + localStorage.getItem("token") + "",
        type: "GET",
        dataType: "json",
        success: updateLocationsSuccess,
        error: updateLocationsError,
        complete: updateLocationsInterval
    });
}

function updateLocationsSuccess(data) {
    for (var key in data) {
        if (data.hasOwnProperty(key)) {
            fetchLocationSuccess(data[key]);
        }
    }
}

function fetchLocation(vehicleId) {
    $.ajax({
        url: "/OpenFleetr/vehicle/location/" + vehicleId + "?token=" + localStorage.getItem("token") + "", //to this url
        type: "GET",
        dataType: "json",
        success: fetchLocationSuccess,
        error: updateLocationsError
    });
}

function fetchLocationSuccess(location) {
    if (!vehicles.hasOwnProperty(location.vehicleId.toString())) {
        console.log(location.latitude);
        console.log(location.longitude);
        console.log(vehicleMap);
        vehicles[location.vehicleId.toString()] = L.marker([location.latitude, location.longitude]).addTo(vehicleMap);
    } else {
        vehicles[location.vehicleId.toString()].setLatLng([location.latitude, location.longitude]).update();
    }
}

function updateLocationsError(jqHXR, textStatus, errorThrown) {
    if (jqHXR.status === 401 || jqHXR.status === 403) {
        alert("Please log in !");
        localStorage.removeItem("token");
        $(location).attr('href', '/OpenFleetr');
    } else {
        $("#locationsList").text("No locations found");
    }
}

function updateStatuses() {
    $.ajax({
        url: "/OpenFleetr/vehicle/status?token=" + localStorage.getItem("token") + "", //to this url
        type: "GET",
        dataType: "json",
        success: updateStatusesSuccess,
        error: updateStatusesError,
        complete: updateStatusesInterval
    });
}

function updateStatusesSuccess(data) {
    var html = "";
    var status;
    for (status in data) {
        var array = data[status];
        html += "<li>" + JSON.stringify(array) + "</li>";
        if (vehicles[array.vehicleId.toString()] === undefined) {
        } else {
            var statusText = "";
            switch (array.status) {
                case 1 :
                    statusText = "Available";
                    break;
                case 2 :
                    if (driversCache[array.driverId.toString()] === undefined && false) {
                        clearTimeout(updateDriversTimeout);
                        updateDrivers();
                    }
                    statusText = "In Use by " + driversCache[array.driverId.toString()];
                    break;
                default :
                    statusText = "Unavailable";
                    break;
            }
            vehicles[array.vehicleId.toString()].bindPopup("" + statusText);
        }
    }
    $("#statusesList").html(html); //set the output
}

function updateStatusesError(jqHXR, textStatus, errorThrown) {
    if (jqHXR.status === 401 || jqHXR.status === 403) {
        alert("Please log in !");
        localStorage.removeItem("token");
        $(location).attr('href', '/OpenFleetr');
    } else {
        $("#statusesList").text("No Statuses found");
    }
}

function updateLocationsInterval() {
    if (websocket === false) {
        updateLocationsTimeout = setTimeout(updateLocations, 1000);
    } else {
        if (updateLocationsTimeout !== undefined) {
            clearTimeout(updateLocationsTimeout);
        }
    }
}

function updateStatusesInterval() {
    if (websocket === false) {
        updateStatusesTimeout = setTimeout(updateStatuses, 1000);
    } else {
        if (updateStatusesTimeout !== undefined) {
            clearTimeout(updateStatusesTimeout);
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
    console.log(event);
    switch (event.code) {
        case 1000 :
            alert("You have been logged out");
            localStorage.removeItem("token"); //delete the user token from storage
            window.history.back();
            break;
        case 1001:
            fallbackPolling();
            break;
        default :
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
    var dispatcher = data[0].firstName + " " + data[0].lastName;

    dispatcherName = dispatcher;
    document.getElementById("dispatcherName").innerHTML = dispatcherName;
}

function getDispatcherError(jqHXR, textStatus, errorThrown) {
    if (jqHXR.status === 401 || jqHXR.status === 403) {//check if the error is an authorisation or authentication error
        alert("Please log in !"); //alert for a login
        localStorage.removeItem("token"); //delete the user token from storage
        $(location).attr('href', '/OpenFleetr'); //go to the home page
    } else {
        dispatcherName = "Error";
        document.getElementById("dispatcherName").innerHTML = dispatcherName;
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
        complete: fetchNotificationInterval
    });
}

function fetchNotificationSuccess() {
    $.ajax({
        url: "/OpenFleetr/user/driver?token=" + localStorage.getItem("token") + "",
        type: "GET",
        dataType: "json",
        success: fetchNotificationSuccess,
        error: fetchNotificationError,
        complete: fetchNotificationInterval
    });
}

function fetchNotificationSuccess(data) {
    //	for (var i in data) {
    //var json = JSON.parse(data[i]);
    //	}	currently function is in test()
}

function test() {
    var arr = ["bob", "12.1231", "12.2312"]
    let notifBox = document.createElement("li");
    notifBox.className = "notification-box";
    notifBox.setAttribute("onlcick", "notifcationCreateCase()");
    let col1 = document.createElement("div");
    col1.className = "col-lg-1 col-sm-1 col-1 text-center";
    let col2 = document.createElement("div");
    col2.className = "col-lg-8 col-sm-8 col-8";
    let name = document.createElement("strong");
    name.className = "text-info";
    name.innerHTML = "" + arr[0];
    let div = document.createElement("div");
    div.innerHTML = "An emergency notification has been posted";
    let location = document.createElement("small");
    location.className = "text-warning";
    location.innerHTML = "Location: " + arr[1].toString() + ", " + arr[2].toString();
    col2.appendChild(name);
    col2.appendChild(div);
    col2.appendChild(location);
    notifBox.appendChild(col1);
    notifBox.appendChild(col2);
    var ul = document.getElementById("notis");
    ul.insertBefore(notifBox, ul.children[ul.children.length - 1]);
}

function notificationCreateCase() {
    $(document).ready(function () {
        $("li").click(function () {
            $(this).hide();
        });
    });
}


function getLatLng(event) {
    if (handling == true) {
        $("#latitude").val(event.latlng.lat);
        $("#longitude").val(event.latlng.lng)
        getReccomendations();
        console.log($("#latitude").val());
        console.log($("#longitude").val());
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

