$(document).ready(function () {
    $("#login").click(login);//bind the click even tot function login

});

function login() {
    var userName = $("#userName").val();//fetch the username
    var password = $("#password").val();//fetch the password
    postData = {"userName": userName, "password": password};//create the post data array.
    if (userName === '' || password === '') {//if the username or password are empty
        $('input[type="text"],input[type="password"]').css("border", "2px solid red");
        $('input[type="text"],input[type="password"]').css("box-shadow", "0 0 3px red");
        alert("Please fill all fields...!!!!!!");//do some CSS stuff and alrert the user
    } else {
        $.ajax({//new ajax request
            url: "/OpenFleetr/auth",//to this endpoint
            type: "POST",//HTTP request type post
            data: JSON.stringify(postData),//data is the json arrray from the array above
            contentType: "application/json; charset=utf-8",//the content we want to send is of type json
            dataType: "json",//the content we want to receive is of type json
            success: loginSuccess,//on success, call loginSuccess();
            error: loginError//on failure, call loginError()
        });
    }
}

function loginSuccess(data) {
        localStorage.removeItem("token");//delete the old login token
        localStorage.setItem("token", data.token);//store the token 
        $(location).attr('href', '/OpenFleetr/app.html');//move to them main app page

}

function loginError(jqHXR, textStatus, errorThrown) {
        if (jqHXR.status === 401 || jqHXR.status === 403) {//if there is an authorisation error 
            $('input[type="text"],input[type="password"]').css("border", "2px solid red");
            $('input[type="text"],input[type="password"]').css("box-shadow", "0 0 3px red");
            alert("Username or password wrong!!!");//tell the user there is an authorisation error
        } else {//if it is another kind of error, give them an alert that something is wrong in the system.
            alert("Something wrong seems to have happend. Please contact your system administrator");
        }
    
}