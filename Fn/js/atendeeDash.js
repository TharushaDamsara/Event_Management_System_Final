let eventsData = []; // store events globally

$(document).ready(function () {
    // 🔹 Check JWT token
    const authToken = localStorage.getItem("authToken");
    const userName = localStorage.getItem("userName");
    const userId = localStorage.getItem("userId");

    if (!authToken) {
        alert("Please log in first!");
        window.location.href = "../index.html";
        return;
    }

    // 🔹 Load user profile dynamically
    function getInitials(name) {
        return name.split(' ').map(n => n[0]).join('').toUpperCase();
    }

    $(".user-profile span").text(userName);
    $(".user-avatar").text(getInitials(userName));
    $(".welcome-title").html(`<i class="fas fa-ticket-alt"></i> Welcome, ${userName}!`);

    // 🔹 Load Events from backend
    function loadEvents() {
        $.ajax({
            url: "http://localhost:8080/api/event/all",
            method: "GET",
            headers: { "Authorization": "Bearer " + authToken },
            success: function (response) {
                if (response.code === 200) {
                    eventsData = response.data;
                    renderEvents(eventsData);
                } else {
                    console.error("Failed to load events:", response.message);
                }
            },
            error: function (xhr, status, error) {
                console.error("AJAX Error:", status, error, xhr.responseText);
            }
        });
    }

    // 🔹 Render Events Dynamically
    function renderEvents(events) {
        const $grid = $("#eventsGrid");
        $grid.empty();

        if (!events || events.length === 0) {
            showEmptyState();
            return;
        }
        hideEmptyState();

        events.forEach((event, index) => {
            const price = event.tickets?.[0]?.price || 0;
            const priceText = price === 0 ? "FREE" : "RS." + price;
            const registeredCount = event.registrations?.length || 0;

            const safeEvent = {
                id: event.id,
                title: event.title,
                category: event.category,
                eventDate: event.eventDate,
                eventTime: event.eventTime,
                location: event.location,
                description: event.description,
                maxAttendees: event.maxAttendees,
                tickets: event.tickets
            };

            const card = `
                <div class="event-card" data-category="${event.category}" data-price="${price}">
                    <div class="event-image">
                        <div class="event-category">${event.category}</div>
                        <div class="event-price">${priceText}</div>
                        <i class="fas fa-calendar"></i>
                    </div>
                    <h3 class="event-title">${event.title}</h3>
                    <div class="event-meta">
                        <span><i class="fas fa-calendar"></i> ${event.eventDate}</span>
                        <span><i class="fas fa-map-marker-alt"></i> ${event.location}</span>
                        <span><i class="fas fa-clock"></i> ${event.eventTime}</span>
                    </div>
                    <p class="event-description">${event.description}</p>
                    <div class="event-stats">
                        <div class="event-stat">
                            <div class="event-stat-number">${registeredCount}</div>
                            <div class="event-stat-label">Registered</div>
                        </div>
                        <div class="event-stat">
                            <div class="event-stat-number">${event.maxAttendees}</div>
                            <div class="event-stat-label">Capacity</div>
                        </div>
                    </div>
                    <div class="event-actions">
                        <button class="btn btn-success btn-sm" onclick='registerForEvent(${JSON.stringify(safeEvent)})'>
                            <i class="fas fa-ticket-alt"></i> Register
                        </button>
                        <button class="btn btn-info btn-sm" onclick="viewEventDetails('${event.title}')">
                            <i class="fas fa-info-circle"></i> Details
                        </button>
                    </div>
                </div>
            `;
            $grid.append(card);
        });

        $(".event-card").each(function (index) {
            $(this).css("animation-delay", `${index * 0.1}s`);
        });
    }

    // 🔹 Render Tickets with QR Codes
    function loadTickets() {
        $.ajax({
            url: `http://localhost:8080/api/v1/registration/byAttendeeId/${userId}`,
            method: "GET",
            headers: { "Authorization": "Bearer " + authToken },
            success: function (response) {
                if (response.code === 200) {
                    renderTickets(response.data);
                } else {
                    console.error("Failed to load tickets:", response.message);
                }
            },
            error: function (xhr, status, error) {
                console.error("AJAX Error:", status, error, xhr.responseText);
            }
        });
    }

    function renderTickets(tickets) {
        const $grid = $(".tickets-grid");
        $grid.empty();

        if (!tickets || tickets.length === 0) {
            $grid.append(`<p>No tickets available</p>`);
            return;
        }

        tickets.forEach(ticket => {
            const statusClass = (ticket.status || "").toLowerCase();

            const card = $(`
                <div class="ticket-card">
                    <div class="ticket-header">
                        <div class="ticket-id">#${ticket.ticketId}</div>
                        <div class="ticket-status ${statusClass}">${ticket.status}</div>
                    </div>
                    <h3 class="event-title">${ticket.event.title}</h3>
                    <div class="event-meta">
                        <span><i class="fas fa-calendar"></i> ${ticket.event.eventDate}</span>
                        <span><i class="fas fa-map-marker-alt"></i> ${ticket.event.location}</span>
                    </div>
                    <div class="qr-section" id="qr-${ticket.ticketId}"></div>
                </div>
            `);

            $grid.append(card);

            // Generate QR code dynamically
            new QRCode(document.getElementById(`qr-${ticket.ticketId}`), {
                text: `TicketID:${ticket.ticketId}|Event:${ticket.event.title}`,
                width: 150,
                height: 150,
                colorDark: "#000000",
                colorLight: "#ffffff",
            });
        });
    }

    // 🔹 Send Feedback
    window.sendFeedback = function (ticketId, eventId) {
        const feedbackText = $(`#feedback-${ticketId}`).val().trim();
        if (!feedbackText) {
            alert("Please enter feedback before submitting!");
            return;
        }

        $.ajax({
            url: "http://localhost:8080/api/feedback",
            method: "POST",
            headers: { 
                "Authorization": "Bearer " + authToken,
                "Content-Type": "application/json" 
            },
            data: JSON.stringify({
                eventId: eventId,
                attendeeId: userId,
                feedback: feedbackText
            }),
            success: function (response) {
                if (response.code === 200) {
                    alert("Feedback submitted successfully!");
                    $(`#feedback-${ticketId}`).val("");
                } else {
                    alert("Failed to submit feedback: " + response.message);
                }
            },
            error: function (xhr, status, error) {
                console.error("Feedback AJAX Error:", status, error, xhr.responseText);
                alert("Error while submitting feedback.");
            }
        });
    };

    // 🔹 Global Functions
    window.registerForEvent = function (eventObj) {
        if (!eventObj?.id) {
            alert("Cannot proceed: invalid event data.");
            return;
        }
        localStorage.setItem("selectedEvent", JSON.stringify(eventObj));
        window.location.href = "../Pages/registration.html";
    };

    window.viewEventDetails = function (eventName) {
        alert(`Opening detailed view for: ${eventName}`);
    };

    // 🔹 Filters
    function applyFilters() {
        const searchTerm = $("#searchInput").val().toLowerCase();
        const category = $("#categoryFilter").val();
        const sortBy = $("#sortFilter").val();

        let filteredEvents = eventsData.filter(event => {
            const matchesSearch = !searchTerm || event.title.toLowerCase().includes(searchTerm);
            const matchesCategory = !category || event.category === category;
            return matchesSearch && matchesCategory;
        });

        if (sortBy === "price") {
            filteredEvents.sort((a, b) => (a.tickets?.[0]?.price || 0) - (b.tickets?.[0]?.price || 0));
        }

        renderEvents(filteredEvents);
    }

    // 🔹 Empty State
    function showEmptyState() {
        const $eventsGrid = $("#eventsGrid");
        if ($(".empty-state").length === 0) {
            $eventsGrid.append(`
                <div class="empty-state">
                    <div class="empty-icon"><i class="fas fa-search"></i></div>
                    <div class="empty-title">No Events Found</div>
                    <div class="empty-description">
                        Try adjusting your search criteria or filters.
                    </div>
                </div>
            `);
        }
    }
    function hideEmptyState() { $(".empty-state").remove(); }

    // 🔹 Search & Filters
    let searchTimer;
    $("#searchInput").on("input", function () {
        clearTimeout(searchTimer);
        searchTimer = setTimeout(applyFilters, 300);
    });
    $("#categoryFilter, #sortFilter").on("change", applyFilters);

    // 🔹 Logout
    window.logout = function () {
        localStorage.clear();
        window.location.href = "../login.html";
    };

    // 🔹 Load initial data
    loadEvents();
    loadTickets();
});
