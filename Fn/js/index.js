document.getElementById('signInForm').addEventListener('submit', function(e) {
            e.preventDefault();
            // Simple validation and demo feedback
            const email = document.getElementById('email').value.trim();
            const password = document.getElementById('password').value.trim();
            if(email && password) {
                alert('Sign In successful (demo)');
                // Here you would add AJAX/fetch to your backend
            } else {
                alert('Please enter both email and password.');
            }
        });