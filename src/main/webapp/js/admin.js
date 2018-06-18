var expanded = false;
var hospitalsMap;
var handling = false;
var hospitalMarkers = [];
$(document).ready(function () {
    $('#hospitalsTab').click(drawHospitalsMap);
    $('#createNewApiUserFormButton').click(createApiUser);
    $('#createNewDriverFormButton').click(createDriver);
    $('#createNewVehicleFormButton').click(createVehicle);
    $('#createNewDispatcherFormButton').click(createDispatcher);
    $('#addHospital').click(toggleHandling);
    $('#createNewHospitalFormButton').click(createHospital);

    main();
});

function main() {
    fetchApiUsers();
    fetchDispatchers();
    fetchDrivers();
    fetchVehicles();
}

function toggleHandling(){
    handling = !handling;
}
function showCheckboxes() {
    var checkboxes = document.getElementById("checkboxes");
    if (!expanded) {
        checkboxes.style.display = "block";
        expanded = true;
    } else {
        checkboxes.style.display = "none";
        expanded = false;
    }
}

function drawHospitalsMap() {
    hospitalsMap = L.map('hospitalsMap', {zoomControl: false}).setView([31.7683, 35.2137], 13);
    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
        attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
    }).addTo(hospitalsMap);
    hospitalsMap.on("click", getLatLng);
    hospitalsMap.doubleClickZoom.disable();
    fetchHospitals();
}

function getLatLng(event) {
    console.log(event);
    if (handling === true) {
        hospitalLatitude = event.latlng.lat;
        hospitalLongitude = event.latlng.lng;
        toggleHandling();
        $('#hospitalModal').modal('show');
    }

}

function createHospital(event) {
    var postData = {
        "latitude": hospitalLatitude,
        "longitude": hospitalLongitude,
        "name": $("#hospitalName").val()
    };
    $.ajax({//new ajax request
        url: "/OpenFleetr/hospital/manager?token=" + localStorage.getItem("token") + "", //to this url
        type: "POST", //HTTP request type get
        data: JSON.stringify(postData), //Data sent to the server
        success: function (response) {
            fetchHospitals();
        }, //on success, call updateLocationsSuccess
        error: function (xhr, resp, text) {
            console.log(xhr, resp, text);
        }
    });
    toggleHandling();
}
function createDispatcher(event) {
    $.ajax({//new ajax request
        url: "/OpenFleetr/user/dispatcher/manager/" + "?token=" + localStorage.getItem("token") + "", //to this url
        type: "POST", //HTTP request type get
        data: JSON.stringify($("#dispatchersForm").serializeObject()), //Data sent to the server
        success: function (response) {
            fetchDispatchers();
        }, //on success, call updateLocationsSuccess
        error: function (xhr, resp, text) {
            console.log(xhr, resp, text);
        }
    });
}

function createDriver(event) {
    $.ajax({//new ajax request
        url: "/OpenFleetr/user/driver/manager/" + "?token=" + localStorage.getItem("token") + "", //to this url
        type: "POST", //HTTP request type get
        data: JSON.stringify($("#driversForm").serializeObject()), //Data sent to the server
        datatype: 'json',
        success: function (response) {
            fetchDrivers();
        }, //on success, call updateLocationsSuccess
        error: function (xhr, resp, text) {
            console.log(xhr, resp, text);
        }
    });
}

function createVehicle(event) {
    $.ajax({//new ajax request
        url: "/OpenFleetr/vehicle/manager/" + "?token=" + localStorage.getItem("token") + "", //to this url
        type: "POST", //HTTP request type get
        data: JSON.stringify($("#vehiclesForm").serializeObject()), //Data sent to the server
        datatype: 'json',
        success: function (response) {
            console.log(response);
            fetchVehicles();
        }, //on success, call updateLocationsSuccess
        error: function (xhr, resp, text) {
            console.log(xhr, resp, text);
        }
    });
}

function createApiUser(event) {
    $.ajax({//new ajax request
        url: "/OpenFleetr/user/api/manager/" + "?token=" + localStorage.getItem("token") + "", //to this url
        type: "POST", //HTTP request type get
        data: JSON.stringify($("#apiUsersForm").serializeObject()), //Data sent to the server
        datatype: 'json',
        success: function (response) {
            fetchApiUsers();
        },
        error: function (xhr, resp, text) {
            console.log(xhr, resp, text);
        }
    });
}
function fetchDispatchers() {
    $.ajax({//new ajax request
        url: "/OpenFleetr/user/dispatcher/manager/" + "?token=" + localStorage.getItem("token") + "", //to this url
        type: "GET", //HTTP request type get
        datatype: 'json',
        success: function (response) {
            var html = ""
            for (var item in response) {
                html = html + "<tr>";
                html = html + "<td>" + response[item].firstName + "</td>";
                html = html + "<td>" + response[item].lastName + "</td>";
                html = html + "<td>" + response[item].birthDate + "</td>";
                html = html + "<td>" + response[item].phoneNumber + "</td>";
                html = html + "<td>" + "" + "</td>";
                html = html + "<td>" + response[item].userName + "</td>";
                html = html + "<td>" + response[item].password + "</td>";
                html = html + "<td>" + "" + "</td>";
                html = html + "</tr>";
            }
            $("#dispatchersTable").html(html);

        }
        ,
        error: function (xhr, resp, text) {
        }
    });
}
function fetchDrivers() {
    $.ajax({//new ajax request
        url: "/OpenFleetr/user/driver/manager/" + "?token=" + localStorage.getItem("token") + "", //to this url
        type: "GET", //HTTP request type get
        datatype: 'json',
        success: function (response) {
            console.log(response);
            var html = ""
            for (var item in response) {
                html = html + "<tr>";
                html = html + "<td>" + response[item].firstName + "</td>";
                html = html + "<td>" + response[item].lastName + "</td>";
                html = html + "<td>" + response[item].birthDate + "</td>";
                html = html + "<td>" + response[item].phoneNumber + "</td>";
                html = html + "<td>" + response[item].userName + "</td>";
                html = html + "<td>" + response[item].password + "</td>";
                html = html + "<td>" + "" + "</td>";
                html = html + "</tr>";
            }
            $("#driversTable").html(html);
        },
        error: function (xhr, resp, text) {
        }
    });
}

function fetchVehicles() {
    $.ajax({//new ajax request
        url: "/OpenFleetr/vehicle/manager/" + "?token=" + localStorage.getItem("token") + "", //to this url
        type: "GET", //HTTP request type get
        datatype: 'json',
        success: function (response) {
            console.log(response);
            var html = "";
            for (var item in response) {
                html = html + "<tr>";
                html = html + "<td>" + response[item].vehicleLicensePlate + "</td>";
                html = html + "<td>" + response[item].vehicleDescription + "</td>";
                html = html + "<td>" + "" + "</td>";
                html = html + "</tr>";
            }
            $("#vehiclesTable").html(html);
        },
        error: function (xhr, resp, text) {
        }
    });
}
function fetchHospitals() {
    $.ajax({//new ajax request
        url: "/OpenFleetr/hospital/manager/" + "?token=" + localStorage.getItem("token") + "", //to this url
        type: "GET", //HTTP request type get
        datatype: 'json',
        success: fetchHospitalsSuccess,
        error: function (xhr, resp, text) {
        }
    });

}

function fetchHospitalsSuccess(json) {
    for (var key in json) {
        if (json.hasOwnProperty(key)) {
            var response = json[key];
            if (!hospitalMarkers.hasOwnProperty(response.id.toString())) {
                hospitalMarkers[response.id.toString()] = L.marker([response.latitude, response.longitude]).addTo(hospitalsMap);
            } else {
                hospitalMarkers[response.id.toString()].setLatLng([response.latitude, response.longitude]).update();
            }
            hospitalMarkers[response.id.toString()].bindPopup(response.name);
            hospitalMarkers[response.id.toString()].on('click', function (e) {
                console.log(e.target);
                console.log(getKeyByValue(hospitalMarkers,e.target));
            });
        }
    }
}

function getKeyByValue(object, value) {
  return Object.keys(object).find(key => object[key] === value);
}

function fetchApiUsers() {
    $.ajax({//new ajax request
        url: "/OpenFleetr/user/api/manager/" + "?token=" + localStorage.getItem("token") + "", //to this url
        type: "GET", //HTTP request type get
        datatype: 'json',
        success: function (response) {
            var html = "";
            for (var item in response) {
                html = html + "<tr>";
                html = html + "<td>" + response[item].applicationName + "</td>";
                html = html + "<td>" + response[item].maintainerEmail + "</td>";
                html = html + "<td>" + response[item].token + "</td>";
                html = html + "<td>" + "" + "</td>";
                html = html + "</tr>";
            }
            $("#apiUsersTable").html(html);

        },
        error: function (xhr, resp, text) {
        }
    });
}

