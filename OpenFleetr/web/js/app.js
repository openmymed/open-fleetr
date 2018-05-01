/*
 * 
 * The main application page 
 * 
 */
var vehicles = [];
var vehicleMap;
var driversCache = [];
var dipsatcherName;
$(document).ready(main);

function main() {
	getDispatcher();
	document.getElementById("dispatcher").innerHTML = dispatcherName;
    loadMap();
    updateDriversInterval();
    updateLocationsInterval();//start refreshing the vehicle locations
    updateStatusesInterval();

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
    for (driver in data) {//iterate over the JSON data from a successful json request
        var array = data[driver];
        driversCache[array.id.toString()] = "" + array.firstName + " " + array.lastName;
    }
}

function updateDriversError(jqHXR, textStatus, errorThrown) {

    if (jqHXR.status === 401 || jqHXR.status === 403) {//check if the error is an authorisation or authentication error
        alert("Please log in !");//alert for a login
        localStorage.removeItem("token");//delete the user token from storage
        $(location).attr('href', '/OpenFleetr');//go to the home page
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

   updateLocationsTimeout = setTimeout(updateLocations, 1000);//every 1000ms, update the location

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
        html += "<li>" + JSON.stringify(array) + "</li>";//formate it into an ordered list
        if (vehicles[array.vehicleId.toString()] === undefined) {
        } else {
            var statusText = "";
            switch (array.status) {
                case 1 :
                    statusText = "Available";
                    break;
                case 2 :
                    if(driversCache[array.driverId.toString()]=== undefined){
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

    updateStatusesTimeout = setTimeout(updateStatuses, 1000);//every 1000ms, update the location

}

function updateDriversInterval() {
    
    updateDriversTimeout = setTimeout(updateDrivers,30000);

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

function getDispatcher(){
	    $.ajax({
        url: "/OpenFleetr/user/dispatcher?token=" + localStorage.getItem("token") + "",
        type: "GET",
        dataType: "json",
        success: getDispatcherSuccess,
        error: getDispatcherError
    });
}
function getDispatcherSuccess(data) {
    var dispatcher = data[0].firstName + " " +  data[0].lastName;
	dispatcherName = dispatcher;
}
function getDispatcherError(jqHXR, textStatus, errorThrown) {
	if (jqHXR.status === 401 || jqHXR.status === 403) {//check if the error is an authorisation or authentication error
        alert("Please log in !");//alert for a login
        localStorage.removeItem("token");//delete the user token from storage
        $(location).attr('href', '/OpenFleetr');//go to the home page
    } else {
        dispatcherName = "Name not found";
    }
}

