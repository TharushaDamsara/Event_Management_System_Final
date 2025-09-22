$(document).ready(function () {
    const authToken = localStorage.getItem("authToken");
    const uId = localStorage.getItem("userId");
    const userName = localStorage.getItem("userName");
    const userEmail = localStorage.getItem("userEmail");

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
                const events = response.data;
                const eventsGrid = $("#eventsGrid");
                eventsGrid.empty();

                if (!events || events.length === 0) {
                    eventsGrid.html(`<p style="text-align:center;color:#777;">No events created yet.</p>`);
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
                                <span><i class="fas fa-clock"></i> ${event.duration || 0} hours</span>
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

    // 🔹 Filter Events
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

    // 🔹 Notifications toggle
    window.toggleNotifications = function () {
        Swal.fire("Notifications", "This is a placeholder for notifications.", "info");
    };

    // 🔹 Event Creation
    const now = new Date();
    now.setHours(now.getHours() + 1);
    function formatDateTimeLocal(date) {
        const pad = n => n.toString().padStart(2, '0');
        return date.getFullYear() + '-' + pad(date.getMonth() + 1) + '-' + pad(date.getDate()) +
            'T' + pad(date.getHours()) + ':' + pad(date.getMinutes());
    }
    $('#eventDateTime').val(formatDateTimeLocal(now));

    // Restore draft
    const savedDraft = JSON.parse(localStorage.getItem("eventDraft") || "{}");
    if (Object.keys(savedDraft).length) {
        $('#eventTitle').val(savedDraft.title || '');
        $('#eventDesc').val(savedDraft.description || '');
        if (savedDraft.eventDate && savedDraft.eventTime) $('#eventDateTime').val(savedDraft.eventDate + 'T' + savedDraft.eventTime);
        $('#eventDuration').val(savedDraft.duration || '');
        $('#eventLocation').val(savedDraft.location || '');
        $('#maxAttendees').val(savedDraft.maxAttendees || '');
        $('#eventCategory').val(savedDraft.category || '');
        $('#eventFee').val(savedDraft.fee || '');
        if(savedDraft.tickets) savedDraft.tickets.forEach(t => addTicketToList(t, true));
    }

    // Ticket modal
    $('#addTicketBtn').click(() => $('#ticketModal').addClass('active'));
    $('#closeModal').click(() => {
        $('#ticketModal').removeClass('active');
        $('#ticketForm')[0].reset();
        $('.benefit-option').removeClass('selected');
        $('.benefit-checkbox').prop('checked', false);
    });

    // Benefit selection
    $('.benefit-option').click(function () {
        $(this).toggleClass('selected');
        const checkbox = $(this).find('.benefit-checkbox');
        checkbox.prop('checked', !checkbox.prop('checked'));
    });

    // Add ticket
    $('#ticketForm').submit(function (e) {
        e.preventDefault();
        const ticket = {
            name: $('#ticketName').val(),
            price: parseFloat($('#ticketPrice').val()),
            quantity: parseInt($('#ticketQuantity').val()),
            description: $('#ticketDescription').val(),
            benefits: $('.benefit-checkbox:checked').map(function () { return $(this).closest('.benefit-option').data('benefit'); }).get()
        };
        addTicketToList(ticket);
        $('#ticketModal').removeClass('active');
        $(this)[0].reset();
        $('.benefit-option').removeClass('selected');
        $('.benefit-checkbox').prop('checked', false);
    });

    function addTicketToList(ticket, restore = false) {
        if ($('#ticketList .ticket-empty').length) $('#ticketList').empty();
        let benefitsHtml = '';
        if (ticket.benefits.length) {
            benefitsHtml = '<div class="ticket-benefits">';
            ticket.benefits.forEach(b => benefitsHtml += `<span class="benefit-tag">${b} <i class="fas fa-times" onclick="removeBenefit(this)"></i></span>`);
            benefitsHtml += '</div>';
        }
        const ticketHtml = `<div class="ticket-item">
            <div class="ticket-item-header">
                <div class="ticket-type">${ticket.name}</div>
                <div class="ticket-actions">
                    <button type="button" class="btn btn-danger btn-tiny" onclick="removeTicket(this)">
                        <i class="fas fa-trash"></i>
                    </button>
                </div>
            </div>
            <div class="ticket-details">
                <div><strong>Price:</strong> $<span class="ticket-price">${ticket.price}</span></div>
                <div><strong>Quantity:</strong> <span class="ticket-quantity">${ticket.quantity}</span></div>
                <div class="ticket-description" style="display:none;">${ticket.description}</div>
            </div>
            ${benefitsHtml}
        </div>`;
        $('#ticketList').append(ticketHtml);
    }

    window.removeTicket = function (el) {
        $(el).closest('.ticket-item').remove();
        if ($('#ticketList').children().length === 0) {
            $('#ticketList').html('<div class="ticket-empty" style="text-align:center; padding:20px; color:#666;">' +
                '<i class="fas fa-ticket-alt" style="font-size:2rem; margin-bottom:10px;"></i>' +
                '<p>No ticket types added yet. Click "Add Ticket Type" to get started.</p></div>');
        }
    };

    window.removeBenefit = function (el) {
        $(el).parent().remove();
    };

    // Event submission
    $('#eventForm').submit(function (e) {
        e.preventDefault();
        if ($('.ticket-item').length === 0) { Swal.fire('Error', 'Please add at least one ticket type!', 'error'); return; }
        const datetime = $('#eventDateTime').val();
        if (!datetime) { Swal.fire("Error", "Please select event date and time!", "error"); return; }
        const [datePart, timePart] = datetime.split('T');

        const tickets = $('.ticket-item').map(function () {
            return {
                name: $(this).find('.ticket-type').text(),
                price: parseFloat($(this).find('.ticket-price').text()),
                quantity: parseInt($(this).find('.ticket-quantity').text()),
                description: $(this).find('.ticket-description').text(),
                benefits: $(this).find('.benefit-tag').map(function () { return $(this).text().replace('×', '').trim(); }).get()
            };
        }).get();

        const eventData = {
            title: $('#eventTitle').val(),
            description: $('#eventDesc').val(),
            category: $('#eventCategory').val(),
            location: $('#eventLocation').val(),
            maxAttendees: parseInt($('#maxAttendees').val()),
            eventDate: datePart,
            eventTime: timePart,
            organizerId: uId,
            tickets: tickets
        };

        const submitBtn = $(this).find('button[type="submit"]');
        const originalText = submitBtn.html();
        submitBtn.html('<span class="spinner"></span> Creating Event...').prop('disabled', true);

        $.ajax({
            url: 'http://localhost:8080/api/event/create',
            method: 'POST',
            contentType: 'application/json',
            headers: { Authorization: 'Bearer ' + authToken },
            data: JSON.stringify(eventData),
            complete: function (xhr) {
                if (xhr.status === 200 || xhr.status === 201) {
                    Swal.fire('Success', 'Event created successfully!', 'success');
                    $('#eventForm')[0].reset();
                    $('#ticketList').html('<div class="ticket-empty" style="text-align:center; padding:20px; color:#666;">' +
                        '<i class="fas fa-ticket-alt" style="font-size:2rem; margin-bottom:10px;"></i>' +
                        '<p>No ticket types added yet. Click "Add Ticket Type" to get started.</p></div>');
                    localStorage.removeItem("eventDraft");
                    loadEvents(); // reload dashboard events
                } else if (xhr.status === 403) {
                    Swal.fire('Forbidden', 'You are not authorized to create events.', 'error');
                } else {
                    let errMsg = 'Unknown error occurred.';
                    try { errMsg = JSON.parse(xhr.responseText).message || errMsg; } catch (e) { }
                    Swal.fire('Error', errMsg, 'error');
                }
                submitBtn.html(originalText).prop('disabled', false);
            }
        });
    });

    // Auto-save draft
    let draftTimer;
    $('#eventForm input, #eventForm textarea, #eventForm select').on('input', function () {
        clearTimeout(draftTimer);
        draftTimer = setTimeout(saveDraft, 2000);
    });

    function saveDraft() {
        const datetime = $('#eventDateTime').val();
        const [datePart, timePart] = datetime ? datetime.split('T') : ["", ""];
        const draftData = {
            title: $('#eventTitle').val(),
            description: $('#eventDesc').val(),
            category: $('#eventCategory').val(),
            location: $('#eventLocation').val(),
            maxAttendees: $('#maxAttendees').val(),
            eventDate: datePart,
            eventTime: timePart,
            tickets: []
        };
        $('.ticket-item').each(function () {
            draftData.tickets.push({
                name: $(this).find('.ticket-type').text(),
                price: $(this).find('.ticket-price').text(),
                quantity: $(this).find('.ticket-quantity').text(),
                description: $(this).find('.ticket-description').text(),
                benefits: $(this).find('.benefit-tag').map(function () { return $(this).text().replace('×', '').trim(); }).get()
            });
        });
        localStorage.setItem("eventDraft", JSON.stringify(draftData));
        const indicator = $('<div><i class="fas fa-save"></i> Draft saved</div>').css({
            position: 'fixed', top: '90px', right: '30px', background: 'rgba(76,175,80,0.9)',
            color: '#fff', padding: '10px 15px', borderRadius: '10px', fontSize: '14px', fontWeight: '600', zIndex: 10000
        }).appendTo('body');
        setTimeout(() => indicator.fadeOut(300, () => indicator.remove()), 2000);
    }

    // 🔹 Initial Load
    loadDashboardStats();
    loadEvents();
});
