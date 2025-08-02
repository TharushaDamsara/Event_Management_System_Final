 const form = document.getElementById('signupForm');
        form.addEventListener('submit', function(e) {
            e.preventDefault();
            let valid = true;

            // Name validation
            const name = document.getElementById('name');
            if (!name.value.trim()) {
                name.classList.add('is-invalid');
                valid = false;
            } else {
                name.classList.remove('is-invalid');
            }

            // Email validation
            const email = document.getElementById('email');
            const emailPattern = /^[^@\s]+@[^@\s]+\.[^@\s]+$/;
            if (!emailPattern.test(email.value)) {
                email.classList.add('is-invalid');
                valid = false;
            } else {
                email.classList.remove('is-invalid');
            }

            // Password validation
            const password = document.getElementById('password');
            if (password.value.length < 6) {
                password.classList.add('is-invalid');
                valid = false;
            } else {
                password.classList.remove('is-invalid');
            }

            // Confirm password validation
            const confirmPassword = document.getElementById('confirmPassword');
            if (confirmPassword.value !== password.value || !confirmPassword.value) {
                confirmPassword.classList.add('is-invalid');
                valid = false;
            } else {
                confirmPassword.classList.remove('is-invalid');
            }

            if (valid) {
                alert('Sign up successful!');
                form.reset();
            }
        });