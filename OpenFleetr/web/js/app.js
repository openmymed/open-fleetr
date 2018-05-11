/*
 * 
 * The main application page 
 * 
 */
var vehicles = [];
var vehicleMap;
var driversCache = [];
var dipsatcherName;
var notificationSocket;
var websocket = false;
var updateDriversTimeout;
var updateLocationsTimeout;
var updateStatusesTimeout;
var handling;
$(document).ready(main);

function main() {

    var createCaseFormPopper = new Popper($('#createCaseButton'), $('#createCaseForm'), {placement: 'right'});
    var ambulanceStatusesPopper = new Popper($('#ambulanceStatusesButton'), $('#ambulanceStatusesList'), {placement: 'right'});
    var hospitalListPopper = new Popper($('#hospitalsButton'), $('#hospitalsList'), {placement: 'right'});

    $('#createCaseButton').click(createCaseFormDisplayControl);
    $('#createCaseFormCloseButton').click(createCaseFromClose);
    $('#createCaseFormConfirmButton').click(createCase);
    $('#ambulanceStatusesButton').click(ambulancesListControl);
    $('#hospitalsButton').click(hospitalsListControl);
    $('#jurisdictionsToggle').click(jurisdictionsToggleControl);


    requestGeolocationPermission();
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

function jurisdictionsToggleControl() {

}
function parseSocketNotification(event) {
    var json = JSON.parse(event.data);
    switch (json.type) {
        case "location" :
            json.array.forEach(fetchLocation);
            break;
        case "status" :
            for (var key in json.array) {
                json.array.forEach(fetchStatus);
            }
            break;
        case "dispatchOrder" :
            for (var key in json.array) {
                json.array.forEach(fetchDispatchOrder);
            }
            break;
        case "notification" :
            fetchNotification();
            break;
        default :
            break;
    }
}


function fallbackPolling(event) {
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
    if (websocket === true) {
        socketCheckInterval = setTimeout(socketPing, 10000);
    } else {
        if (socketCheckInterval !== undefined) {
            clearTimeout(socketCheckInterval);
        }
    }

}

function socketPing() {

    if (websocket === true) {
        notificationSocket.send('');
        checkSocketInterval();
    }

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
    for (driver in json) {//iterate over the JSON data from a successful json request
        var array = data[driver];
        driversCache[array.id.toString()] = "" + array.firstName + " " + array.lastName;
    }
}

function updateDriversError(jqHXR, textStatus, errorThrown) {

    if (jqHXR.status === 401 || jqHXR.status === 403) {//check if the error is an authorisation or authentication error
        alert("Please log in !"); //alert for a login
        localStorage.removeItem("token"); //delete the user token from storage
        $(location).attr('href', '/OpenFleetr'); //go to the home page
    } else {

    }

}

function updateLocations() {
    $.ajax({//new ajax request
        url: "/OpenFleetr/vehicle/location?token=" + localStorage.getItem("token") + "", //to this url
        type: "GET", //HTTP request type get
        dataType: "json", //expected return data type json
        success: updateLocationsSuccess, //on success, call updateLocationsSuccess
        error: updateLocationsError, //on failure, call updateLocationsFailure
        complete: updateLocationsInterval//In all cases, call updateLocationsInterval
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
    $.ajax({//new ajax request
        url: "/OpenFleetr/vehicle/location/" + vehicleId + "?token=" + localStorage.getItem("token") + "", //to this url
        type: "GET", //HTTP request type get
        dataType: "json", //expected return data type json
        success: fetchLocationSuccess, //on success, call updateLocationsSuccess
        error: updateLocationsError //on failure, call updateLocationsFailure
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

    if (jqHXR.status === 401 || jqHXR.status === 403) {//check if the error is an authorisation or authentication error
        alert("Please log in !"); //alert for a login
        localStorage.removeItem("token"); //delete the user token from storage
        $(location).attr('href', '/OpenFleetr'); //go to the home page
    } else {
        $("#locationsList").text("No locations found"); //if it is a not found error, simply say there are no locations
    }
}



function updateStatuses() {
    $.ajax({//new ajax request
        url: "/OpenFleetr/vehicle/status?token=" + localStorage.getItem("token") + "", //to this url
        type: "GET", //HTTP request type get
        dataType: "json", //expected return data type json
        success: updateStatusesSuccess, //on success, call updateLocationsSuccess
        error: updateStatusesError, //on failure, call updateLocationsFailure
        complete: updateStatusesInterval//In all cases, call updateLocationsInterval
    });
}

function updateStatusesSuccess(data) {

    var html = "";
    var status;
    for (status in data) {
        var array = data[status];
        html += "<li>" + JSON.stringify(array) + "</li>"; //formate it into an ordered list
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

    if (jqHXR.status === 401 || jqHXR.status === 403) {//check if the error is an authorisation or authentication error
        alert("Please log in !"); //alert for a login
        localStorage.removeItem("token"); //delete the user token from storage
        $(location).attr('href', '/OpenFleetr'); //go to the home page
    } else {
        $("#statusesList").text("No Statuses found"); //if it is a not found error, simply say there are no locations
    }

}

function updateLocationsInterval() {
    if (websocket === false) {
        updateLocationsTimeout = setTimeout(updateLocations, 1000); //every 1000ms, update the location
    } else {
        if (updateLocationsTimeout !== undefined) {
            clearTimeout(updateLocationsTimeout);
        }
    }

}
function updateStatusesInterval() {
    if (websocket === false) {
        updateStatusesTimeout = setTimeout(updateStatuses, 1000); //every 1000ms, update the location
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
    console.log(position);
    console.log(position.coords);
    var latitude = position.coords.latitude;
    var longitude = position.coords.longitude;
    vehicleMap = L.map('vehicleMapDiv', {zoomControl: false}).setView([latitude, longitude], 13);
    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
        attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
    }).addTo(vehicleMap);
    vehicleMap.on('click', closeAll);

}

function geolocationError() {
    console.log("no gis");
    vehicleMap = L.map('vehicleMapDiv', {zoomControl: false}).setView([31.7683, 35.2137], 13);
    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
        attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
    }).addTo(vehicleMap);
    vehicleMap.on('click', closeAll);

}

function loadMap() {
    if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(geolocationSuccess);
    } else {
        geolocationError();
    }


}

function socketClose(event) {

    switch (event.code) {
        case 1000 :
            alert("You have been logged out");
            localStorage.removeItem("token"); //delete the user token from storage
            $(location).attr('href', '/OpenFleetr'); //go to the home page
            break;
        case 1003 :
            clearTimeout(socketAttemptInterval);
            break
        case 1001 :
            websocket = false;
            fallbackPolling();
            break;
        case 1006 :
            websocket = false;
            fallbackPolling();
            break;
    }

}
function socketError(event) {
    console.log("an error has happened");
}

function socketConnect() {
    notificationSocket = new WebSocket("wss://" + location.host + "/OpenFleetr/notifications/" + localStorage.getItem("token"));
    websocket = true;
    notificationSocket.onopen = checkSocketInterval;
    notificationSocket.onmessage = parseSocketNotification;
    notificationSocket.onerror = socketError;
    notificationSocket.onclose = socketClose;
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
        dispatcherName = "Error"
                ;
        document.getElementById("dispatcherName").innerHTML = dispatcherName;
    }

}

function requestGeolocationPermission() {
    navigator.permissions.query({name: 'geolocation'}).then(function (result) {
        if (result.state === 'granted') {
            geolocationSuccess();
        } else if (result.state === 'prompt') {
            loadMap();
        } else if (result.state === 'denied') {
            geolocationError();
        }

    });


}

function createCaseFormDisplayControl() {
    if (!$('#createCaseForm').is(":visible")) {
        closeAll();
        $('#createCaseForm').show();
    } else {
        $('#createCaseForm').hide();
    }
}

function createCase() {
    return "ok";
}

function createCaseFromClose() {
    $('#fullName').val("");
    $('#phoneNumber').val("");
    $('#notes').val("");
    createCaseFormDisplayControl();

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
//	for (var i in data) {
    //var json = JSON.parse(data[i]);
//	}	
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