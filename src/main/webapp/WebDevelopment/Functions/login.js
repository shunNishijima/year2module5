function login() {
    try {
        let username = document.getElementById('username').value;
        let password = document.getElementById('password').value;
        let login = false;
        let formData = new URLSearchParams();
        formData.append('username', username);
        formData.append("password", password);

        if (username !== '' && password !== '') {
            let json = {username: username, password: password}
            fetch("http://localhost:8080/SAFEty/api/user/login",
                {
                    method: "POST",
                    body: formData,
                    headers: {
                        "Content-type" : "application/x-www-form-urlencoded"
                    }
                }
            ).then(
                response => {
                    login = response.ok
                    console.log("Username: " + username + "\nPassword: " + password);

                    if (login && username.startsWith("admin")) {
                        console.log("It worked bitches");
                        window.location.href = "../SAFEty/WebDevelopment/homePage.html?" + username;
                    } else {
                        document.getElementById("mistakeMessage").innerText = "Entered username or password incorrect";
                    }
                }
            )
        } else {
            document.getElementById("mistakeMessage").innerText = "Please fill in both the username and password";
            console.log("Fill in both username and password");
        }
    } catch(e)
{
    console.log("error");
}

}