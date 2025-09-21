let currentStep = 1;
let selectedRole = null;
const totalSteps = 3;

// Next Step
function nextStep(step) {
    if (!validateStep(step)) return;

    if (step < totalSteps) {
        $(`#step${step}`).addClass('completed').html('<i class="fas fa-check"></i>');
        $(`#step${step + 1}`).addClass('active');

        $(`#formStep${step}`).removeClass('active');
        $(`#formStep${step + 1}`).addClass('active');

        currentStep = step + 1;
    }
}

// Previous Step
function prevStep(step) {
    if (step > 1) {
        $(`#step${step}`).removeClass('active');
        $(`#step${step - 1}`).removeClass('completed').addClass('active').text(step - 1);

        $(`#formStep${step}`).removeClass('active');
        $(`#formStep${step - 1}`).addClass('active');

        currentStep = step - 1;
    }
}

// Validate Steps
function validateStep(step) {
    switch(step) {
        case 1:
            const firstName = $('#firstName').val().trim();
            const lastName = $('#lastName').val().trim();
            const email = $('#email').val().trim();
            
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
            const password = $('#password').val();
            const confirmPassword = $('#confirmPassword').val();
            
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
            if (!$('#termsCheckbox').is(':checked')) {
                alert('Please accept the terms and conditions');
                return false;
            }
            break;
    }
    return true;
}

// Email & Password validation
function isValidEmail(email) {
    return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email);
}

function isPasswordValid(password) {
    return password.length >= 8 && /[A-Z]/.test(password) && /\d/.test(password);
}

// Role selection
function selectRole(element, role) {
    $('.role-option').removeClass('selected');
    $(element).addClass('selected');
    selectedRole = role.toUpperCase();
}

// Password strength
function updatePasswordStrength() {
    const password = $('#password').val();
    const bars = $('.strength-bar');
    let score = 0;

    // Reset
    bars.removeClass('active medium weak');
    ['req-length','req-upper','req-number'].forEach(id => {
        $(`#${id}`).removeClass('met').find('i').attr('class','fas fa-times');
    });

    if (password.length >= 8) { score++; $('#req-length').addClass('met').find('i').attr('class','fas fa-check'); }
    if (/[A-Z]/.test(password)) { score++; $('#req-upper').addClass('met').find('i').attr('class','fas fa-check'); }
    if (/\d/.test(password)) { score++; $('#req-number').addClass('met').find('i').attr('class','fas fa-check'); }

    bars.each((i, bar) => {
        if (i < score) {
            if (score === 1) $(bar).addClass('weak');
            else if (score === 2) $(bar).addClass('medium');
            else $(bar).addClass('active');
        }
    });
}

// Go to login
function goToLogin() {
    window.location.href = '../index.html';
}

// Document ready
$(document).ready(function() {
    // Password input
    $('#password').on('input', updatePasswordStrength);

    // Form submit with API call
    $('#signupForm').on('submit', function(e) {
        e.preventDefault();
        if (!validateStep(3)) return;

        const submitBtn = $('#submitBtn').addClass('btn-loading');

        const formData = {
            name: $('#firstName').val().trim() + ' ' + $('#lastName').val().trim(),
            email: $('#email').val(),
            password: $('#password').val(),
            role: selectedRole,
            newsletter: $('#newsletterCheckbox').is(':checked')
        };

        // Example API call
        $.ajax({
            url: 'http://localhost:8080/api/auth/register',
            method: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(formData),
            success: function(response) {
                submitBtn.removeClass('btn-loading');

                // Mark final step
                $('#step3').addClass('completed').html('<i class="fas fa-check"></i>');
                $('#formStep3').removeClass('active');
                $('#successStep').addClass('active');

                console.log('Account created successfully!', response);
            },
            error: function(xhr) {
                submitBtn.removeClass('btn-loading');
                alert(xhr.responseJSON?.message || 'Signup failed!');
            }
        });
    });

    // Input blur/focus validation
    $('.form-input').on('blur', function() {
        const val = $(this).val().trim();
        if (this.required && !val) $(this).css('border-color','#ff6b6b');
        else if (this.type === 'email' && !isValidEmail(val)) $(this).css('border-color','#ff6b6b');
        else $(this).css('border-color','#667eea');
    }).on('focus', function() {
        $(this).css('border-color','#667eea');
    });
});
