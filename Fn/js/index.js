$(document).ready(function() {
    let selectedRole = null;

    $(".role-option").on("click", function () {
        $(".role-option").removeClass("selected").css("transform", "scale(1)");
        $(this).addClass("selected").css("transform", "scale(1.05)");
        selectedRole = $(this).data("role").toUpperCase();

        setTimeout(() => {
            $(this).css("transform", "scale(1.02)");
        }, 150);
    });

    $("#signupLink").on("click", function(e){
        e.preventDefault();
        window.location.href = "Pages/signUp.html";
    });

    $("#loginForm").on("submit", function(e){
        e.preventDefault();

    if (!selectedRole) {
        Swal.fire({
            icon: 'warning',
            title: 'Role Required',
            text: 'Please select your role (Organizer or Attendee)',
            confirmButtonColor: '#ff9800',
            confirmButtonText: 'Got it!'
        });
        return;
        }


        const email = $("#email").val();
        const password = $("#password").val();
        const loginBtn = $("#loginBtn");

        loginBtn.addClass("btn-loading").prop("disabled", true).html("");

        $.ajax({
        url: "http://localhost:8080/api/auth/login",
        type: "POST",
        contentType: "application/json",
        data: JSON.stringify({
            email: email,
            password: password,
            role: selectedRole
        }),
        success: function (response) {
            if (response.code === 200 && response.data && response.data.token) {
            const userData = response.data;
            localStorage.setItem("authToken", userData.token);
            localStorage.setItem("userRole", userData.role);
            console.log("userData:", userData);
            console.log(response);

                Swal.fire({
                    icon: 'success',
                    title: 'Welcome!',
                    text: 'Login successful 🎉',
                    showConfirmButton: false,
                    timer: 2000
                    });


            if (userData.role === "ORGANIZER") {
                window.location.href = "./Pages/OrganizerDash.html";
                

            } else if (userData.role === "ATTENDEE") {
                window.location.href = "./Pages/attendeeDash.html";
            }
            } else {
            alert("Login failed: " + response.message);
            console.log(response);
            }
        },
        error: function (xhr) {
            let res = xhr.responseJSON;
            alert("Login failed: " + (res?.message || xhr.statusText));
        },
        complete: function () {
            loginBtn.removeClass("btn-loading")
            .prop("disabled", false)
            .html('<i class="fas fa-sign-in-alt"></i> Login to EventEase');
        }
        });

    });

    $(".floating-shape").hover(
        function(){ $(this).css({"animation-play-state":"paused","transform":"scale(1.2)"}); },
        function(){ $(this).css({"animation-play-state":"running","transform":"scale(1)"}); }
    );

        $(".form-input").on("blur", function() {
            if ($(this).val().trim() === "") {
                $(this).css("border-color", "#ff6b6b");
            } else {
                $(this).css("border-color", "#667eea");
            }
        }).on("focus", function() {
            $(this).css("border-color", "#667eea");
        });
    }); // <-- make sure this closing brace is here




