let currentStep = 1;
let selectedTicketType = null;
let selectedTicketPrice = 0;
let selectedTicketId = null;
let selectedPaymentMethod = null;
let selectedEvent = null;

$(document).ready(function () {
    // 🔹 Check JWT token
    const authToken = localStorage.getItem("authToken");
    const userId = Number(localStorage.getItem("userId"));
    

    if (!authToken || isNaN(userId)) {
        Swal.fire("Not Logged In", "Please log in first!", "warning").then(() => {
            window.location.href = "../index.html";
        });
        return;
    }

    // 🔹 Load Event Data
    function loadEventData() {
        const event = localStorage.getItem("selectedEvent");
        const eventId = event ? JSON.parse(event).id : null;

        if (!eventId) {
            Swal.fire("No Event Selected", "Redirecting to dashboard...", "info").then(() => {
                window.location.href = "../pages/attendeeDash.html";
            });
            return;
        }

        $.ajax({
            url: `http://localhost:8080/api/event/eventbyid/${eventId}`,
            method: "GET",
            headers: { "Authorization": "Bearer " + authToken },
            success: function (res) {
                if (res.code === 200) {
                    selectedEvent = res.data;
                    populateEventDetails();
                    renderTickets();
                } else {
                    Swal.fire("Error", "Failed to load event data.", "error").then(() => {
                        window.location.href = "../pages/attendeeDash.html";
                    });
                }
            },
            error: function (xhr) {
                console.error("Error loading event:", xhr.responseText);
                Swal.fire("Error", "Error loading event data.", "error").then(() => {
                    window.location.href = "../pages/attendeeDash.html";
                });
            }
        });
    }

    // 🔹 Populate Event Details
    function populateEventDetails() {
        $(".registration-title").text(selectedEvent.title);
        $(".registration-subtitle").text("Complete your registration to secure your spot");
        $(".event-category-badge").text(selectedEvent.category);
        const price = selectedEvent.tickets?.length > 0 ? `RS.${selectedEvent.tickets[0].price}` : "FREE";
        $(".event-price-badge").text(price);

        $(".event-details").html(`
            <div class="event-detail"><i class="fas fa-calendar"></i><span>${selectedEvent.eventDate}</span></div>
            <div class="event-detail"><i class="fas fa-map-marker-alt"></i><span>${selectedEvent.location}</span></div>
            <div class="event-detail"><i class="fas fa-clock"></i><span>${selectedEvent.eventTime}</span></div>
            <div class="event-detail"><i class="fas fa-users"></i><span>${selectedEvent.registrations?.length || 0}/${selectedEvent.maxAttendees} registered</span></div>
        `);
    }

    // 🔹 Render Tickets
    function renderTickets() {
        const container = $(".ticket-options");
        container.empty();

        if (!selectedEvent.tickets?.length) {
            container.html("<p>No tickets available for this event.</p>");
            return;
        }

        selectedEvent.tickets.forEach(ticket => {
            const ticketDiv = $(`
                <div class="ticket-option">
                    <div class="ticket-type">${ticket.name}</div>
                    <div class="ticket-price">${ticket.price === 0 ? "FREE" : "RS." + ticket.price}</div>
                </div>
            `);
            ticketDiv.click(() => selectTicket(ticket.name, ticket.price, ticket));
            container.append(ticketDiv);
        });
    }

    // 🔹 Ticket Selection
    function selectTicket(name, price, ticketObj) {
        $(".ticket-option").removeClass("selected");
        $(`.ticket-option:contains("${name}")`).addClass("selected");

        selectedTicketType = name;
        selectedTicketPrice = price;
        selectedTicketId = ticketObj.id;

        const processingFee = selectedTicketPrice > 0 ? 2.50 : 0;
        const total = selectedTicketPrice + processingFee;

        $("#selectedTicketName").text(selectedTicketType);
        $("#basePrice").text(selectedTicketPrice === 0 ? "FREE" : `RS.${selectedTicketPrice}`);
        $("#totalAmount").text(selectedTicketPrice === 0 ? "FREE" : `RS.${total.toFixed(2)}`);
    }

    // 🔹 Payment Selection
    window.selectPayment = function (method, element) {
        $(".payment-method").removeClass("selected");
        $(element).addClass("selected");
        selectedPaymentMethod = method;

        $("#cardForm").toggle(method === "card");

        if (method === "card") {
            const fn = $("#firstName").val();
            const ln = $("#lastName").val();
            $("#cardholderName").val(`${fn} ${ln}`);
        }
    };

    // 🔹 Step Navigation
    window.nextStep = function () {
        if (!validateCurrentStep()) return;

        const totalSteps = 3;
        $(`#section${currentStep}`).removeClass("active");
        $(`#step${currentStep}`).removeClass("active").addClass("completed").find(".step-circle").html('<i class="fas fa-check"></i>');

        currentStep++;
        if (currentStep > totalSteps) {
            processPayment();
            return;
        }

        $(`#section${currentStep}`).addClass("active");
        $(`#step${currentStep}`).addClass("active").find(".step-circle").text(currentStep);
        updateNavigation();
    };

    window.previousStep = function () {
        if (currentStep <= 1) return;
        $(`#section${currentStep}`).removeClass("active");
        $(`#step${currentStep}`).removeClass("active completed");
        currentStep--;
        $(`#section${currentStep}`).addClass("active");
        $(`#step${currentStep}`).addClass("active").find(".step-circle").text(currentStep);
        updateNavigation();
    };

    function updateNavigation() {
        $("#prevBtn").toggle(currentStep > 1);
        $("#nextBtn").html(currentStep === 3
            ? '<i class="fas fa-credit-card"></i><span class="btn-text">Complete Payment</span>'
            : '<span class="btn-text">Next Step</span><i class="fas fa-arrow-right"></i>'
        );
    }

    // 🔹 Validations
    function validateCurrentStep() {
        switch (currentStep) {
            case 1: return validatePersonalInfo();
            case 2: return validateTicketSelection();
            case 3: return validatePayment();
            default: return true;
        }
    }

    function validatePersonalInfo() {
        let valid = true;
        ["firstName", "lastName", "email", "phone"].forEach(id => {
            const val = $(`#${id}`).val().trim();
            if (!val) { $(`#${id}`).css("borderColor", "#f44336"); valid = false; } 
            else $(`#${id}`).css("borderColor", "#4CAF50");
        });

        const email = $("#email").val().trim();
        if (email && !/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email)) valid = false;

        if (!valid) Swal.fire("Invalid Info", "Please fill all required fields correctly.", "warning");
        return valid;
    }

    function validateTicketSelection() {
        if (!selectedTicketType) { 
            Swal.fire("Ticket Required", "Please select a ticket type.", "warning");
            return false; 
        }
        return true;
    }

    function validatePayment() {
        if (selectedTicketPrice === 0) return true;

        if (!selectedPaymentMethod) { 
            Swal.fire("Payment Required", "Select a payment method.", "warning");
            return false; 
        }

        if (!$("#termsAccept").is(":checked")) { 
            Swal.fire("Terms Required", "Please accept terms.", "warning");
            return false; 
        }

        if (selectedPaymentMethod === "card") {
            let valid = true;
            ["cardNumber", "expiryDate", "cvc", "cardholderName"].forEach(id => {
                if (!$(`#${id}`).val().trim()) valid = false;
            });
            if (!valid) Swal.fire("Card Details", "Fill all card details.", "warning");
            return valid;
        }

        return true;
    }

   function processPayment() {
    $("#nextBtn").prop("disabled", true);

    // ⚠️ Make sure to declare the variable used in AJAX
    const registrationData = {
        attendeeId: userId,
        eventId: selectedEvent.id,
        ticketId: selectedTicketId,
        firstName: $("#firstName").val(),
        lastName: $("#lastName").val(),
        email: $("#email").val(),
        phone: $("#phone").val(),
        ticketType: selectedTicketType,
        paymentMethode: selectedPaymentMethod,
        amount: selectedTicketPrice
    };

    if (isNaN(registrationData.attendeeId)) {
        Swal.fire("Error", "User not logged in properly. Please login again.", "error")
            .then(() => window.location.href = "../index.html");
        return;
    }

    $.ajax({
        url: "http://localhost:8080/api/v1/registration/register",
        type: "POST",
        contentType: "application/json",
        data: JSON.stringify(registrationData),  // ✅ use the correct variable
        success: function (res) {
            console.log("✅ Success:", res);
            Swal.fire("Success", res?.message || "Registration completed!", "success");
        },
        error: function (xhr, status) {
            console.error("❌ Error:", xhr);
            let res = null;
            try {
                res = xhr.responseJSON || JSON.parse(xhr.responseText);
            } catch (e) {
                console.warn("Failed to parse error JSON:", e);
            }
            Swal.fire("Error", res?.message || "Unknown error occurred.", "error");
        },
        statusCode: {
            201: function (res) {
                console.log("🎉 Registration success (201):", res);
                Swal.fire("Success", res.message, "success");
            }
        }
    });
}

    // 🔹 Utilities
    window.goBackPage = () => window.history.back();
    window.downloadTicket = () => Swal.fire("Download", "Ticket download simulated.", "info");
    window.goToDashboard = () => window.location.href = "../pages/attendeeDash.html";

    // 🔹 Card Input Formatting
    $("#cardNumber").on("input", e => e.target.value = e.target.value.replace(/\D/g,'').match(/.{1,4}/g)?.join(' ') || e.target.value);
    $("#expiryDate").on("input", e => {
        let val = e.target.value.replace(/\D/g,'');
        if (val.length >= 3) val = val.slice(0,2) + '/' + val.slice(2,4);
        e.target.value = val;
    });
    $("#cvc").on("input", e => e.target.value = e.target.value.replace(/\D/g,''));

    // 🔹 Initialize
    loadEventData();
});
