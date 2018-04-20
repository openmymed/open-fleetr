$(document).ready(function () {
    $("#login").click(function () {
        var userName = $("#userName").val();
        var password = $("#password").val();
        postData = {"userName": userName, "password": password};

        if (userName == '' || password == '') {
            $('input[type="text"],input[type="password"]').css("border", "2px solid red");
            $('input[type="text"],input[type="password"]').css("box-shadow", "0 0 3px red");
            alert("Please fill all fields...!!!!!!");
        } else {
            $.ajax({
                url: "/OpenFleetr/auth",
                type: "POST",
                data: JSON.stringify(postData),
                contentType: "application/json; charset=utf-8",
                dataType: "json",
                success: function (data) {
                    window.localStorage.setItem("token", data.token);
                    $(location).attr('href', '/OpenFleetr/app.html');
                }
            });
        }
    });
});