$(document).ready(function () {
    let selectedRole = null;

    // 🔹 Role Selection
    $(".role-option").on("click", function () {
        $(".role-option").removeClass("selected").css("transform", "scale(1)");
        $(this).addClass("selected").css("transform", "scale(1.05)");
        selectedRole = $(this).data("role")?.toUpperCase();

        // Smooth animation
        setTimeout(() => $(this).css("transform", "scale(1.02)"), 150);
    });

    // 🔹 Signup link
    $("#signupLink").on("click", function (e) {
        e.preventDefault();
        window.location.href = "Pages/signUp.html";
    });

    // 🔹 Login form submission
    $("#loginForm").on("submit", function (e) {
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

        const email = $("#email").val().trim();
        const password = $("#password").val().trim();

        if (!email || !password) {
            Swal.fire({
                icon: 'warning',
                title: 'Incomplete Data',
                text: 'Please enter both email and password',
                confirmButtonColor: '#ff9800',
            });
            return;
        }

        const loginBtn = $("#loginBtn");
        loginBtn.addClass("btn-loading").prop("disabled", true).html("");

        console.log("Logging in:", { email, password, role: selectedRole });

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
                if (response.code === 200 && response.data?.token) {
                    const userData = response.data;

                    // Store user info in localStorage
                    localStorage.setItem("authToken", userData.token);
                    localStorage.setItem("userRole", userData.role);
                    localStorage.setItem("userName", userData.name);
                    localStorage.setItem("userId", userData.userId);
                    localStorage.setItem("userEmail", userData.email);

                    console.log("Login successful:", userData);

                    Swal.fire({
                        icon: 'success',
                        title: 'Welcome!',
                        text: 'Login successful 🎉',
                        showConfirmButton: false,
                        timer: 2000
                    });

                    // Redirect based on role
                    if (userData.role === "ORGANIZER") {
                        window.location.href = "./Pages/OrganizerDash.html";
                        
                        
                    } else if (userData.role === "ATTENDEE") {
                        window.location.href = "./Pages/attendeeDash.html";
                        console.log(userData);
                        
                    }
                } else {
                    Swal.fire({
                        icon: 'error',
                        title: 'Login Failed',
                        text: response.message || "Invalid credentials",
                        confirmButtonColor: '#f44336'
                    });
                    console.log("Login failed response:", response);
                }
            },
            error: function (xhr) {
                const res = xhr.responseJSON;
                Swal.fire({
                    icon: 'error',
                    title: 'Login Failed',
                    text: res?.message || xhr.statusText,
                    confirmButtonColor: '#f44336'
                });
                console.error("Login error:", xhr.responseText);
            },
            complete: function () {
                loginBtn.removeClass("btn-loading")
                    .prop("disabled", false)
                    .html('<i class="fas fa-sign-in-alt"></i> Login to EventEase');
            }
        });
    });

    // 🔹 Input focus/blur styling
    $(".form-input").on("blur", function () {
        $(this).css("border-color", $(this).val().trim() ? "#667eea" : "#ff6b6b");
    }).on("focus", function () {
        $(this).css("border-color", "#667eea");
    });

    // 🔹 Floating shape hover effect
    $(".floating-shape").hover(
        function () { $(this).css({ "animation-play-state": "paused", "transform": "scale(1.2)" }); },
        function () { $(this).css({ "animation-play-state": "running", "transform": "scale(1)" }); }
    );
});
