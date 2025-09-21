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
        return name
            ? name.split(" ").map(n => n[0]).join("").toUpperCase()
            : "";
    }

    $(".user-profile span").text(userName);
    $(".user-avatar").text(getInitials(userName));
    $(".welcome-title").html(`<i class="fas fa-ticket-alt"></i> Welcome, ${userName}!`);

    // =====================================================
    // 🔹 EVENTS SECTION
    // =====================================================

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
                console.error("AJAX Error (Events):", status, error, xhr.responseText);
            }
        });
    }

    function renderEvents(events) {
        const $grid = $("#eventsGrid");
        $grid.empty();

        if (!events || events.length === 0) {
            showEmptyState();
            return;
        }
        hideEmptyState();

        events.forEach((event, index) => {
            const price = event.tickets && event.tickets.length > 0 ? event.tickets[0].price : 0;
            const priceText = price === 0 ? "FREE" : "RS." + price;
            const registeredCount = event.registrations ? event.registrations.length : 0;

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
                        <button class="btn btn-success btn-sm" onclick='registerForEvent(${JSON.stringify(event)})'>
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

    // =====================================================
    // 🔹 TICKETS SECTION
    // =====================================================

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
                console.error("AJAX Error (Tickets):", status, error, xhr.responseText);
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

            const card = `
                <div class="ticket-card">
                    <div class="ticket-header">
                        <div class="ticket-id">#${ticket.ticketId || ticket.id}</div>
                        <div class="ticket-status ${statusClass}">${ticket.status || "PENDING"}</div>
                    </div>
                    <h3 class="event-title">${ticket.eventTitle || ""}</h3>
                    <div class="event-meta">
                        <span><i class="fas fa-calendar"></i> ${ticket.eventDate || ""}</span>
                        <span><i class="fas fa-map-marker-alt"></i> ${ticket.location || ""}</span>
                    </div>
                    <div class="qr-section">
                        ${
                            ticket.status === "CONFIRMED"
                                ? `<img src="${ticket.qrCode}" alt="QR Code" class="qr-image"/>`
                                : `<p style="color:#666;font-size:14px;">QR code will be generated upon payment confirmation</p>`
                        }
                    </div>
                </div>
            `;
            $grid.append(card);
        });
    }

    // =====================================================
    // 🔹 FILTERS & UTILITIES
    // =====================================================

    function applyFilters() {
        const searchTerm = ($("#searchInput").val() || "").toLowerCase();
        const category = $("#categoryFilter").val();
        const sortBy = $("#sortFilter").val();

        let filteredEvents = eventsData.filter(event => {
            const matchesSearch = !searchTerm || (event.title || "").toLowerCase().includes(searchTerm);
            const matchesCategory = !category || event.category === category;
            return matchesSearch && matchesCategory;
        });

        if (sortBy === "price") {
            filteredEvents.sort((a, b) => {
                const priceA = a.tickets?.[0]?.price || 0;
                const priceB = b.tickets?.[0]?.price || 0;
                return priceA - priceB;
            });
        }

        renderEvents(filteredEvents);
    }

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

    function hideEmptyState() {
        $(".empty-state").remove();
    }

    // =====================================================
    // 🔹 GLOBAL FUNCTIONS
    // =====================================================

    window.registerForEvent = function (eventObj) {
        localStorage.setItem("selectedEvent", JSON.stringify(eventObj));
        window.location.href = "../pages/registration.html";
    };

    window.viewEventDetails = function (eventName) {
        alert(`Opening detailed view for: ${eventName}`);
    };

    window.logout = function () {
        localStorage.removeItem("authToken");
        localStorage.removeItem("userRole");
        localStorage.removeItem("userName");
        localStorage.removeItem("userId");
        localStorage.removeItem("userEmail");
        window.location.href = "../login.html";
    };

    // =====================================================
    // 🔹 EVENT BINDINGS
    // =====================================================

    let searchTimer;
    $("#searchInput").on("input", function () {
        clearTimeout(searchTimer);
        searchTimer = setTimeout(applyFilters, 300);
    });
    $("#categoryFilter, #sortFilter").on("change", applyFilters);

    // =====================================================
    // 🔹 INITIAL LOAD
    // =====================================================

    loadEvents();
    loadTickets();
});
