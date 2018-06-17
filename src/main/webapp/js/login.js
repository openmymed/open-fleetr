$(document).ready(main);

function main() {
    $("#login").click(login);
}

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
            url: "/OpenFleetr/user/auth",
            type: "POST",
            data: JSON.stringify(postData),
            dataType: "json",
            success: loginSuccess,
            error: loginError
        });
    }
}

function loginSuccess(data) {
    localStorage.removeItem("token");
    localStorage.setItem("token", data.token);
    if (data.level === 4) {
        $(location).attr('href', '/OpenFleetr/admin.html');
    } else if (data.level === 3) {
        $(location).attr('href', '/OpenFleetr/app.html');
    } else if (data.level === 2) {
        $(location).attr('href', '/OpenFleetr/help.html');
    } else if (data.level === 1) {
        $(location).attr('href', '/OpenFleetr/driver.html');
    }
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