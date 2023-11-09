function signup() {
    let username = document.getElementById("username").value;
    let serialNumber = document.getElementById("serial").value;
    let password = document.getElementById("password").value;
    let password2 = document.getElementById("password2").value;
    let formData = new URLSearchParams();
    formData.append('username', username);
    formData.append("password", password);
    if (username !== "" && serialNumber !== "" && password !== "" && password2 !== "") {
        if (username.length < 5 || username.length > 15) {
            document.getElementById("mistakeMessage").innerText = "Username has to be between 5 and 15 characters long";
        } else  if (password.length !== 4 || isNaN(password)){
            document.getElementById("mistakeMessage").innerText = "Password has to be exactly 4 digits";
        } else if (password !== password2) {
                document.getElementById("mistakeMessage").innerText = "The passwords have to be the same";
            } else {
            console.log("fetching")
                fetch("http://localhost:8080/SAFEty/api/user/signup",
                    {
                        method: "POST",
                        body: formData,
                        headers: {
                            "Content-type" : "application/x-www-form-urlencoded"
                        }
                    }
                    ).then(
                    response => {
                        if (response.ok) {
                            window.location.href = "../index.html";
                        } else {
                            document.getElementById("mistakeMessage").innerText = "A user with this password could not be made";
                        }
                    }
                )
            }
    } else {
        document.getElementById("mistakeMessage").innerText = "Please fill in every field";
    }
}