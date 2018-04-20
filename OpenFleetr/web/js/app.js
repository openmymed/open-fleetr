$(document).ready(function () {
    var text = "";
    $.ajax({
        url: "/OpenFleetr/location?token=" + localStorage.getItem("token") + "",
        type: "GET",
        dataType: "json",
        success: function (data) {

            var gpsLocation;
            for (gpsLocation in data) {
                text += "<li>" + JSON.stringify(data[gpsLocation]) + "</li>";
            }
            $("#locations").append(text);
        },
        error: function (jqHXR, textStatus, errorThrown) {
            if (jqHXR.status = 401 || jqHXR.status == 403) {
                alert("Please log in !")
                localStorage.removeItem("token");
                $(location).attr('href', '/OpenFleetr');
            } else {
                $("#locations").append("no locations found");
            }
        }
    });
});