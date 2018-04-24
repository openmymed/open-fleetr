/*
 * 
 * the Main application page 
 * 
 */
var vehicles = [];
var vehicleMap;
$(document).ready(function () {

    loadMap();
    updateLocationsInterval();//start refreshing the vehicle locations
    updateStatusesInterval();
    



});

function updateLocations() {
    $.ajax({//new ajax request
        url: "/OpenFleetr/location?token=" + localStorage.getItem("token") + "", //to this url
        type: "GET", //HTTP request type get
        dataType: "json", //expected return data type json
        success: updateLocationsSuccess, //on success, call updateLocationsSuccess
        error: updateLocationsError, //on failure, call updateLocationsFailure
        complete: updateLocationsInterval//In all cases, call updateLocationsInterval
    });

}

function updateLocationsSuccess(data) {
    var html = "";
    var gpsLocation;
    for (gpsLocation in data) {//iterate over the JSON data from a successful json request
        var array = data[gpsLocation];
        html += "<li>" + JSON.stringify(array) + "</li>";//formate it into an ordered list
        if (vehicles[array.vehicleId.toString()] === undefined) {
            vehicles[array.vehicleId.toString()] = L.marker([array.latitude, array.longitude]).addTo(vehicleMap);
        } else {
            vehicles[array.vehicleId.toString()].setLatLng([array.latitude, array.longitude]).update();
        }
    }
    $("#locationsList").html(html);//set the output

}

function updateLocationsError(jqHXR, textStatus, errorThrown) {
    if (jqHXR.status === 401 || jqHXR.status === 403) {//check if the error is an authorisation or authentication error
        alert("Please log in !");//alert for a login
        localStorage.removeItem("token");//delete the user token from storage
        $(location).attr('href', '/OpenFleetr');//go to the home page
    } else {
        $("#locationsList").text("No locations found");//if it is a not found error, simply say there are no locations
    }
}

function updateLocationsInterval() {
    setTimeout(updateLocations, 1000);//every 1000ms, update the location
}


function updateStatuses() {
    $.ajax({//new ajax request
        url: "/OpenFleetr/status?token=" + localStorage.getItem("token") + "", //to this url
        type: "GET", //HTTP request type get
        dataType: "json", //expected return data type json
        success: updateStatusesSuccess, //on success, call updateLocationsSuccess
        error: updateStatusesError, //on failure, call updateLocationsFailure
        complete: updateStatusesInterval//In all cases, call updateLocationsInterval
    });

}

function updateStatusesSuccess(data) {
    var html = "";
    var gpsLocation;
    for (gpsLocation in data) {
        var array = data[gpsLocation];
        html += "<li>" + JSON.stringify(array) + "</li>";//formate it into an ordered list
        if (vehicles[array.vehicleId.toString()] === undefined) {
        } else {
            var statusText = "";
            switch (array.status) {
                case 1 :
                    statusText = "Available";
                    break;
                case 2 :
                    statusText = "In Use by:" + fetchDriver(array.driverId);
                    break;
                default :
                    statusText = "Unavailable";
                    break;
            }
            vehicles[array.vehicleId.toString()].bindPopup("<b>CurrentStatus</b><br>" + statusText)
        }
    }

    $("#statusesList").html(html);//set the output

}

function updateStatusesError(jqHXR, textStatus, errorThrown) {
    if (jqHXR.status === 401 || jqHXR.status === 403) {//check if the error is an authorisation or authentication error
        alert("Please log in !");//alert for a login
        localStorage.removeItem("token");//delete the user token from storage
        $(location).attr('href', '/OpenFleetr');//go to the home page
    } else {
        $("#statusesList").text("No Statuses found");//if it is a not found error, simply say there are no locations
    }
}

function updateStatusesInterval() {
    setTimeout(updateStatuses, 1000);//every 1000ms, update the location
}

function geolocationSuccess(position) {

    vehicleMap = L.map('vehicleMapDiv').setView([position.coords.latitude, position.coords.longitude], 13);
    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
        attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
    }).addTo(vehicleMap);

}

function geolocationError() {

    vehicleMap = L.map('vehicleMapDiv').setView([31.7683, 35.2137], 13);
    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
        attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
    }).addTo(vehicleMap);

}

function loadMap() {

        navigator.geolocation.getCurrentPosition(geolocationSuccess, geolocationError, {timeout: 1000});


}

function fetchDriver(driverId) {

    $.ajax({//new ajax request
        url: "/OpenFleetr/status/" + driverId + "?token=" + localStorage.getItem("token") + "", //to this url
        type: "GET", //HTTP request type get
        dataType: "json", //expected return data type json
        success: fetchDriverSuccess, //on success, call updateLocationsSuccess
        error: fetchDriverError //on failure, call updateLocationsFailure
    });

}

function fetchDriverSuccess(data) {
    return data.firstname + " " + data.lastName;
}

function fetchDriverError(jqHXR, textStatus, errorThrown) {
    if (jqHXR.status === 401 || jqHXR.status === 403) {//check if the error is an authorisation or authentication error
        alert("Please log in !");//alert for a login
        localStorage.removeItem("token");//delete the user token from storage
    } else {
        alert("Something wrong seems to have happend. Please contact your system administrator");
    }
    $(location).attr('href', '/OpenFleetr');//go to the home page
}