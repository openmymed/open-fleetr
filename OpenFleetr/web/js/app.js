$(document).ready(function () {
    updateLocationsInterval();
});

function updateLocations() {
    $.ajax({
        url: "/OpenFleetr/location?token=" + localStorage.getItem("token") + "",
        type: "GET",
        dataType: "json",
        success: updateLocationsSuccess,
        error: updateLocationsError,
        complete: updateLocationsInterval
    });

}

function updateLocationsSuccess(data) {
    var html = "";
    var gpsLocation;
    for (gpsLocation in data) {
        html += "<li>" + JSON.stringify(data[gpsLocation]) + "</li>";
    }
    $("#locations").html(html);

}

function updateLocationsError(jqHXR, textStatus, errorThrown) {
    if (jqHXR.status === 401 || jqHXR.status === 403) {
        alert("Please log in !");
        localStorage.removeItem("token");
        $(location).attr('href', '/OpenFleetr');
    } else {
        $("#locations").text("No locations found");
    }
}

function updateLocationsInterval() {
    setTimeout(updateLocations, 1000);
}