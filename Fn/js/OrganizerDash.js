$(document).ready(function () {
    const authToken = localStorage.getItem("authToken");
    const uId = localStorage.getItem("userId");
    const userName = localStorage.getItem("userName"); // stored at login
    const userEmail =localStorage.getItem("userEmail"); // stored at login
    console.log("Auth Token:", authToken, "UserId:", uId);
    console.log("User Name:", userName);
    

    if (!authToken || !uId) {
        Swal.fire("Not Logged In", "Please log in first!", "warning").then(() => {
            window.location.href = "/index.html";
        });
        return;
    }

    // 🔹 Update User Profile UI
    if (userName) {
        $(".user-profile span").text(userName);
        $(".welcome-title").html(`<i class="fas fa-users-cog"></i> Welcome back, ${userName}!`);
        $(".user-avatar").text(
            userName.charAt(0).toUpperCase() +
            (userName.split(" ")[1]?.charAt(0).toUpperCase() || "")
        );
    }

    // 🔹 Load Dashboard Stats
    function loadDashboardStats() {
        $.ajax({
            url: `http://localhost:8080/api/event/statistics/${uId}`,
            method: "GET",
            headers: { Authorization: "Bearer " + authToken },
            success: function (res) {
                $(".quick-stats .quick-stat:nth-child(1) .stat-number").text(res.data.todayEvents || 0);
                $(".quick-stats .quick-stat:nth-child(2) .stat-number").text(res.data.todayRegistrations || 0);
                $(".quick-stats .quick-stat:nth-child(3) .stat-number").text(res.data.totalRegistrations || 0);
                $(".quick-stats .quick-stat:nth-child(4) .stat-number").text(res.data.averageRating?.toFixed(1) || "0.0");
                console.log("Dashboard stats loaded:", res);
            },
            error: function (xhr) {
                console.error("Failed to load stats:", xhr.responseText);
            }
        });
    }

    // 🔹 Load Organizer Events
    function loadEvents() {
        $.ajax({
            url: `http://localhost:8080/api/event/eventbyorganizerid/${uId}`,
            method: "GET",
            headers: { Authorization: "Bearer " + authToken },
success: function (response) {
    const events = response.data; // <-- extract the array
    const eventsGrid = $("#eventsGrid");
    eventsGrid.empty();

    if (!events || events.length === 0) {
        eventsGrid.html(`<p style="text-align:center;color:#777;">No events created yet.</p>`);
        console.log(events);
        return;
    }

    events.forEach(event => {
        const eventCard = `
            <div class="event-card">
                <div class="event-status ${event.status?.toLowerCase() || 'upcoming'}">${event.status || 'Upcoming'}</div>
                <h3 class="event-title">${event.title}</h3>
                <div class="event-meta">
                    <span><i class="fas fa-calendar"></i> ${event.eventDate}</span>
                    <span><i class="fas fa-map-marker-alt"></i> ${event.location}</span>
                    <span><i class="fas fa-clock"></i> ${event.duration} hours</span>
                </div>
                <p class="event-description">${event.description}</p>
                <div class="event-stats">
                    <div class="event-stat">
                        <div class="event-stat-number">${event.registered || 0}/${event.maxAttendees || 0}</div>
                        <div class="event-stat-label">Registered</div>
                    </div>
                    <div class="event-stat">
                        <div class="event-stat-number">${event.rating?.toFixed(1) || "0.0"}</div>
                        <div class="event-stat-label">Rating</div>
                    </div>
                    <div class="event-stat">
                        <div class="event-stat-number">$${event.revenue || 0}</div>
                        <div class="event-stat-label">Revenue</div>
                    </div>
                </div>
                <div class="event-actions">
                    <button class="btn btn-warning btn-sm">
                        <i class="fas fa-bullhorn"></i> Promote
                    </button>
                    <button class="btn btn-secondary btn-sm">
                        <i class="fas fa-users"></i> Attendees
                    </button>
                </div>
            </div>
        `;
        eventsGrid.append(eventCard);
    });
},

            error: function (xhr) {
                console.error("Failed to load events:", xhr.responseText);
            }
        });
    }

    // 🔹 Filter Events (active / upcoming / completed)
    window.filterEvents = function (filter) {
        $.ajax({
            url: `http://localhost:8080/api/event/organizer/${uId}?filter=${filter}`,
            method: "GET",
            headers: { Authorization: "Bearer " + authToken },
            success: function (events) {
                const eventsGrid = $("#eventsGrid");
                eventsGrid.empty();

                if (!events.length) {
                    eventsGrid.html(`<p style="text-align:center;color:#777;">No ${filter} events found.</p>`);
                    return;
                }

                events.forEach(event => {
                    eventsGrid.append(`
                        <div class="event-card">
                            <div class="event-status ${event.status.toLowerCase()}">${event.status}</div>
                            <h3 class="event-title">${event.title}</h3>
                            <div class="event-meta">
                                <span><i class="fas fa-calendar"></i> ${event.eventDate}</span>
                                <span><i class="fas fa-map-marker-alt"></i> ${event.location}</span>
                                <span><i class="fas fa-clock"></i> ${event.duration} hours</span>
                            </div>
                        </div>
                    `);
                });
            },
            error: function (xhr) {
                console.error("Failed to filter events:", xhr.responseText);
            }
        });
    };

    // 🔹 Logout
    window.logout = function () {
        localStorage.clear();
        Swal.fire("Logged Out", "You have been logged out successfully.", "success").then(() => {
            window.location.href = "../index.html";
        });
    };

    // 🔹 Notifications Toggle (dummy for now)
    window.toggleNotifications = function () {
        Swal.fire("Notifications", "This is a placeholder for notifications.", "info");
    };

    // 🔹 Initial Load
    loadDashboardStats();
    loadEvents();
});
