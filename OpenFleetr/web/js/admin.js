var expanded = false;
var jurisdictionsMap;
var hospitalsMap

$(document).ready(main);

function main(){
    $('#hospitalsTab').click(drawHospitalsMap);
    $('#jurisdictionsTab').click(drawJurisdictionsMap);
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

function drawJurisdictionsMap() {
	jurisdictionsMap = L.map('jurisdictionsMap', {
			zoomControl: false
		}).setView([31.7683, 35.2137], 13);
	L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
		attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
	}).addTo(jurisdictionsMap);

}

function drawHospitalsMap() {
    hospitalsMap = L.map('hospitalsMap',{zoomControl:false}).setView([31.7683, 35.2137], 13);
    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
        attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
    }).addTo(hospitalsMap);    
}

function createDispatcher() {
	$.ajax({ //new ajax request
		url: "/OpenFleetr/user/dispatcher/manager/" + "?token=" + localStorage.getItem("token") + "", //to this url
		type: "POST", //HTTP request type get
		data: $("#dispatchersForm").serialize(), //Data sent to the server
		success: function (response) {
			console.log("response");
		}, //on success, call updateLocationsSuccess
		error: function (xhr, resp, text) {
			console.log(xhr, resp, text);
		}
	}));
}
function createDriver() {
	$.ajax({ //new ajax request
		url: "/OpenFleetr/user/driver/manager/" + "?token=" + localStorage.getItem("token") + "", //to this url
		type: "POST", //HTTP request type get
		data: $("#driversForm").serialize(), //Data sent to the server
		datatype: 'json',
		success: function (response) {
			console.log("response");
		}, //on success, call updateLocationsSuccess
		error: function (xhr, resp, text) {
			console.log(xhr, resp, text);
		}
	});
}
function createVehicle() {
	$.ajax({ //new ajax request
		url: "/OpenFleetr/user/vehicle/manage/" + "?token=" + localStorage.getItem("token") + "", //to this url
		type: "POST", //HTTP request type get
		data: $("#driversForm").serialize(), //Data sent to the server
		datatype: 'json',
		success: function (response) {
			console.log("response");
		}, //on success, call updateLocationsSuccess
		error: function (xhr, resp, text) {
			console.log(xhr, resp, text);
		}
	});
}
function createApiUser() {
	$.ajax({ //new ajax request
		url: "/OpenFleetr/user/api/management/" + "?token=" + localStorage.getItem("token") + "", //to this url
		type: "POST", //HTTP request type get
		data: $("#driversForm").serialize(), //Data sent to the server
		datatype: 'json',
		success: function (response) {
			console.log("response");
		},
		error: function (xhr, resp, text) {
			console.log(xhr, resp, text);
		}
	});
}
function fetchDispatchers() {
	$.ajax({ //new ajax request
		url: "/OpenFleetr/user/dispatcher/manager/" + "?token=" + localStorage.getItem("token") + "", //to this url
		type: "GET", //HTTP request type get
		datatype: 'json',
		success: function (response) {
			console.log("response");
		},
		error: function (xhr, resp, text) {
			console.log(xhr, resp, text);
		}
	});
}
