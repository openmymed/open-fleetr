$(document).ready(function () {
    localStorage.removeItem("token");
    $("#login").click(login);

});

function login() {
    var userName = $("#userName").val();
    var password = $("#password").val();
    postData = {"userName": userName, "password": password};
    if (userName === '' || password === '') {
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
            success: loginSuccess,
            error: loginError
        });
    }
}

function loginSuccess(data) {
        localStorage.setItem("token", data.token);
        $(location).attr('href', '/OpenFleetr/app.html');

}

function loginError(jqHXR, textStatus, errorThrown) {
        if (jqHXR.status === 401 || jqHXR.status === 403) {
            $('input[type="text"],input[type="password"]').css("border", "2px solid red");
            $('input[type="text"],input[type="password"]').css("box-shadow", "0 0 3px red");
            alert("Username or password wrong!!!");
        } else {
            alert("Something wrong seems to have happend. Please contact your system administrator");
        }
    
}