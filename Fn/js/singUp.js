let currentStep = 1;
        let selectedRole = null;
        const totalSteps = 3;

        function nextStep(step) {
            if (!validateStep(step)) return;
            
            if (step < totalSteps) {
                // Update progress
                document.getElementById(`step${step}`).classList.add('completed');
                document.getElementById(`step${step}`).innerHTML = '<i class="fas fa-check"></i>';
                document.getElementById(`step${step + 1}`).classList.add('active');
                
                // Switch form steps
                document.getElementById(`formStep${step}`).classList.remove('active');
                document.getElementById(`formStep${step + 1}`).classList.add('active');
                
                currentStep = step + 1;
            }
        }

        function prevStep(step) {
            if (step > 1) {
                // Update progress
                document.getElementById(`step${step}`).classList.remove('active');
                document.getElementById(`step${step - 1}`).classList.remove('completed');
                document.getElementById(`step${step - 1}`).classList.add('active');
                document.getElementById(`step${step - 1}`).innerHTML = step - 1;
                
                // Switch form steps
                document.getElementById(`formStep${step}`).classList.remove('active');
                document.getElementById(`formStep${step - 1}`).classList.add('active');
                
                currentStep = step - 1;
            }
        }

        function validateStep(step) {
            switch(step) {
                case 1:
                    const firstName = document.getElementById('firstName').value.trim();
                    const lastName = document.getElementById('lastName').value.trim();
                    const email = document.getElementById('email').value.trim();
                    
                    if (!firstName || !lastName || !email) {
                        alert('Please fill in all required fields');
                        return false;
                    }
                    
                    if (!isValidEmail(email)) {
                        alert('Please enter a valid email address');
                        return false;
                    }
                    break;
                    
                case 2:
                    const password = document.getElementById('password').value;
                    const confirmPassword = document.getElementById('confirmPassword').value;
                    
                    if (!selectedRole) {
                        alert('Please select your role');
                        return false;
                    }
                    
                    if (!password || !confirmPassword) {
                        alert('Please fill in password fields');
                        return false;
                    }
                    
                    if (password !== confirmPassword) {
                        alert('Passwords do not match');
                        return false;
                    }
                    
                    if (!isPasswordValid(password)) {
                        alert('Please meet all password requirements');
                        return false;
                    }
                    break;
                    
                case 3:
                    const termsAccepted = document.getElementById('termsCheckbox').checked;
                    
                    if (!termsAccepted) {
                        alert('Please accept the terms and conditions');
                        return false;
                    }
                    break;
            }
            return true;
        }

        function isValidEmail(email) {
            const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
            return emailRegex.test(email);
        }

        function isPasswordValid(password) {
            const hasLength = password.length >= 8;
            const hasUpper = /[A-Z]/.test(password);
            const hasNumber = /\d/.test(password);
            
            return hasLength && hasUpper && hasNumber;
        }

        function selectRole(element, role) {
            document.querySelectorAll('.role-option').forEach(option => {
                option.classList.remove('selected');
            });
            
            element.classList.add('selected');
            selectedRole = role;
        }

        function updatePasswordStrength() {
            const password = document.getElementById('password').value;
            const strengthBars = document.querySelectorAll('.strength-bar');
            const requirements = ['req-length', 'req-upper', 'req-number'];
            
            // Reset bars
            strengthBars.forEach(bar => {
                bar.classList.remove('active', 'medium', 'weak');
            });
            
            let score = 0;
            
            // Check length
            if (password.length >= 8) {
                score++;
                document.getElementById('req-length').classList.add('met');
                document.getElementById('req-length').querySelector('i').className = 'fas fa-check';
            } else {
                document.getElementById('req-length').classList.remove('met');
                document.getElementById('req-length').querySelector('i').className = 'fas fa-times';
            }
            
            // Check uppercase
            if (/[A-Z]/.test(password)) {
                score++;
                document.getElementById('req-upper').classList.add('met');
                document.getElementById('req-upper').querySelector('i').className = 'fas fa-check';
            } else {
                document.getElementById('req-upper').classList.remove('met');
                document.getElementById('req-upper').querySelector('i').className = 'fas fa-times';
            }
            
            // Check number
            if (/\d/.test(password)) {
                score++;
                document.getElementById('req-number').classList.add('met');
                document.getElementById('req-number').querySelector('i').className = 'fas fa-check';
            } else {
                document.getElementById('req-number').classList.remove('met');
                document.getElementById('req-number').querySelector('i').className = 'fas fa-times';
            }
            
            // Update strength bars
            for (let i = 0; i < score && i < 4; i++) {
                if (score <= 1) {
                    strengthBars[i].classList.add('weak');
                } else if (score <= 2) {
                    strengthBars[i].classList.add('medium');
                } else {
                    strengthBars[i].classList.add('active');
                }
            }
        }

        function goToLogin() {
             window.location.href = '../index.html'; 
        }

        // Event Listeners
        document.getElementById('password').addEventListener('input', updatePasswordStrength);

        document.getElementById('signupForm').addEventListener('submit', function(e) {
            e.preventDefault();
            
            if (!validateStep(3)) return;
            
            const submitBtn = document.getElementById('submitBtn');
            submitBtn.classList.add('btn-loading');
            
            // Simulate API call
            setTimeout(() => {
                submitBtn.classList.remove('btn-loading');
                
                // Mark final step as completed
                document.getElementById('step3').classList.add('completed');
                document.getElementById('step3').innerHTML = '<i class="fas fa-check"></i>';
                
                // Show success step
                document.getElementById('formStep3').classList.remove('active');
                document.getElementById('successStep').classList.add('active');
                
                console.log('Account created successfully!', {
                    firstName: document.getElementById('firstName').value,
                    lastName: document.getElementById('lastName').value,
                    email: document.getElementById('email').value,
                    role: selectedRole,
                    newsletter: document.getElementById('newsletterCheckbox').checked
                });
            }, 2000);
        });

        // Enhanced form validation
        const inputs = document.querySelectorAll('.form-input');
        inputs.forEach(input => {
            input.addEventListener('blur', function() {
                if (this.required && this.value.trim() === '') {
                    this.style.borderColor = '#ff6b6b';
                } else if (this.type === 'email' && !isValidEmail(this.value)) {
                    this.style.borderColor = '#ff6b6b';
                } else {
                    this.style.borderColor = '#667eea';
                }
            });

            input.addEventListener('focus', function() {
                this.style.borderColor = '#667eea';
            });
        });