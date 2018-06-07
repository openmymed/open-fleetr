var expanded = false;
var areaMap;
var hospitalsMap;

$(document).ready(main);

function main() {
    $('#hospitalsTab').click(drawHospitalsMap);
    $('#areasTab').click(drawAreaMap);
    fetchApiUsers();
    fetchDispatchers();
    fetchDrivers();
    fetchHospitals();
    fetchAreas();
    fetchVehicles();
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

function drawAreaMap() {
    areaMap = L.map('areasMap', {
        zoomControl: false
    }).setView([31.7683, 35.2137], 13);
    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
        attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
    }).addTo(areaMap);

}

function drawHospitalsMap() {
    hospitalsMap = L.map('hospitalsMap', {zoomControl: false}).setView([31.7683, 35.2137], 13);
    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
        attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
    }).addTo(hospitalsMap);
}

function createDispatcher() {
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
function createDriver() {
    $.ajax({//new ajax request
        url: "/OpenFleetr/user/driver/manager/" + "?token=" + localStorage.getItem("token") + "", //to this url
        type: "POST", //HTTP request type get
        data: $("#driversForm").serialize(), //Data sent to the server
        datatype: 'json',
        success: function (response) {
            console.log(response);
        }, //on success, call updateLocationsSuccess
        error: function (xhr, resp, text) {
            console.log(xhr, resp, text);
        }
    });
}
function createVehicle() {
    $.ajax({//new ajax request
        url: "/OpenFleetr/vehicle/manager/" + "?token=" + localStorage.getItem("token") + "", //to this url
        type: "POST", //HTTP request type get
        data: $("#driversForm").serialize(), //Data sent to the server
        datatype: 'json',
        success: function (response) {
            console.log(response);
        }, //on success, call updateLocationsSuccess
        error: function (xhr, resp, text) {
            console.log(xhr, resp, text);
        }
    });
}
function createApiUser() {
    $.ajax({//new ajax request
        url: "/OpenFleetr/user/api/manager/" + "?token=" + localStorage.getItem("token") + "", //to this url
        type: "POST", //HTTP request type get
        data: $("#driversForm").serialize(), //Data sent to the server
        datatype: 'json',
        success: function (response) {
            console.log(response);
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
            console.log(response);
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
            console.log("response");

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
        success: function (response) {
            console.log(response);
        },
        error: function (xhr, resp, text) {
        }
    });
}
function fetchAreas() {
    $.ajax({//new ajax request
        url: "/OpenFleetr/geographicalarea/manager" + "?token=" + localStorage.getItem("token") + "", //to this url
        type: "GET", //HTTP request type get
        datatype: 'json',
        success: function (response) {
            console.log(response);
        },
        error: function (xhr, resp, text) {
        }
    });
}
function fetchApiUsers() {
    $.ajax({//new ajax request
        url: "/OpenFleetr/user/api/manager/" + "?token=" + localStorage.getItem("token") + "", //to this url
        type: "GET", //HTTP request type get
        datatype: 'json',
        success: function (response) {
            console.log(response);

        },
        error: function (xhr, resp, text) {
        }
    });
}

