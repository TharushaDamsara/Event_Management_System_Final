let currentRating = 0;

        // Event Registration
        function registerForEvent(eventName, price) {
            if (confirm(`Register for "${eventName}" (${price})?\n\nThis will redirect you to the registration form.`)) {
                alert(`Registering for ${eventName}...\nRedirecting to payment gateway.`);
                // In real implementation: window.location.href = `/register/${eventId}`;
            }
        }

        // View Event Details
        function viewEventDetails(eventName) {
            alert(`Opening detailed view for: ${eventName}\n\nThis will show full event information, schedule, speakers, and more.`);
            // In real implementation: window.location.href = `/events/${eventId}`;
        }

        // Apply Filters
        function applyFilters() {
            const searchTerm = document.getElementById('searchInput').value.toLowerCase();
            const category = document.getElementById('categoryFilter').value;
            const sortBy = document.getElementById('sortFilter').value;
            const eventCards = document.querySelectorAll('.event-card');

            let filteredEvents = Array.from(eventCards).filter(card => {
                const title = card.querySelector('.event-title').textContent.toLowerCase();
                const cardCategory = card.dataset.category;
                
                const matchesSearch = !searchTerm || title.includes(searchTerm);
                const matchesCategory = !category || cardCategory === category;
                
                return matchesSearch && matchesCategory;
            });

            // Hide all cards first
            eventCards.forEach(card => card.style.display = 'none');

            // Sort filtered events
            if (sortBy === 'price') {
                filteredEvents.sort((a, b) => {
                    const priceA = parseFloat(a.dataset.price) || 0;
                    const priceB = parseFloat(b.dataset.price) || 0;
                    return priceA - priceB;
                });
            } else if (sortBy === 'rating') {
                filteredEvents.sort((a, b) => {
                    const ratingA = parseFloat(a.querySelector('.event-stat-number:nth-of-type(2)').textContent) || 0;
                    const ratingB = parseFloat(b.querySelector('.event-stat-number:nth-of-type(2)').textContent) || 0;
                    return ratingB - ratingA;
                });
            }

            // Show filtered events
            filteredEvents.forEach((card, index) => {
                card.style.display = 'block';
                card.style.animationDelay = `${index * 0.1}s`;
                card.style.animation = 'fadeInUp 0.5s ease forwards';
            });

            // Show empty state if no results
            if (filteredEvents.length === 0) {
                showEmptyState();
            } else {
                hideEmptyState();
            }
        }

        // Filter Tags
        document.addEventListener('DOMContentLoaded', function() {
            const filterTags = document.querySelectorAll('.filter-tag');
            
            filterTags.forEach(tag => {
                tag.addEventListener('click', function() {
                    // Remove active class from all tags
                    filterTags.forEach(t => t.classList.remove('active'));
                    
                    // Add active class to clicked tag
                    this.classList.add('active');
                    
                    // Apply filter
                    const filter = this.dataset.filter;
                    filterEventsByTag(filter);
                });
            });
        });

        function filterEventsByTag(filter) {
            const eventCards = document.querySelectorAll('.event-card');
            
            eventCards.forEach(card => {
                let shouldShow = true;
                
                switch(filter) {
                    case 'free':
                        shouldShow = card.dataset.price === '0';
                        break;
                    case 'today':
                        // In real implementation, check if event is today
                        shouldShow = Math.random() > 0.5; // Simulate
                        break;
                    case 'weekend':
                        // In real implementation, check if event is on weekend
                        shouldShow = Math.random() > 0.3; // Simulate
                        break;
                    case 'online':
                        // In real implementation, check if event is online
                        shouldShow = Math.random() > 0.6; // Simulate
                        break;
                    case 'all':
                    default:
                        shouldShow = true;
                }
                
                if (shouldShow) {
                    card.style.display = 'block';
                    card.style.animation = 'fadeInUp 0.5s ease';
                } else {
                    card.style.display = 'none';
                }
            });
        }

        // Rating System
        document.addEventListener('DOMContentLoaded', function() {
            const stars = document.querySelectorAll('.star');
            
            stars.forEach(star => {
                star.addEventListener('click', function() {
                    currentRating = parseInt(this.dataset.rating);
                    updateStarDisplay();
                });
                
                star.addEventListener('mouseenter', function() {
                    const hoverRating = parseInt(this.dataset.rating);
                    updateStarDisplay(hoverRating);
                });
            });
            
            document.getElementById('ratingStars').addEventListener('mouseleave', function() {
                updateStarDisplay();
            });
        });

        function updateStarDisplay(hoverRating = null) {
            const stars = document.querySelectorAll('.star');
            const rating = hoverRating || currentRating;
            
            stars.forEach((star, index) => {
                if (index < rating) {
                    star.classList.add('active');
                } else {
                    star.classList.remove('active');
                }
            });
        }

        // Submit Feedback
        function submitFeedback() {
            const event = document.getElementById('feedbackEvent').value;
            const feedback = document.getElementById('feedbackText').value.trim();
            
            if (!event) {
                alert('Please select an event to review.');
                return;
            }
            
            if (currentRating === 0) {
                alert('Please provide a rating.');
                return;
            }
            
            if (!feedback) {
                alert('Please write your feedback.');
                return;
            }
            
            const feedbackData = {
                event: event,
                rating: currentRating,
                feedback: feedback,
                timestamp: new Date().toISOString()
            };
            
            alert(`Feedback submitted successfully!\n\nEvent: ${event}\nRating: ${currentRating}/5 stars\n\nThank you for your feedback!`);
            
            console.log('Feedback Data:', feedbackData);
            
            // Reset form
            document.getElementById('feedbackEvent').value = '';
            document.getElementById('feedbackText').value = '';
            currentRating = 0;
            updateStarDisplay();
        }

        // Toggle Notifications
        function toggleNotifications() {
            const notifications = [
                'New event matches your interests: "Web Development Bootcamp"',
                'Reminder: AI Summit starts in 3 days'
            ];
            
            const message = notifications.join('\n• ');
            alert('Recent Notifications:\n\n• ' + message);
            
            // Remove notification badge
            document.querySelector('.notification-badge').style.display = 'none';
        }

        // Logout Function
        function logout() {
            if (confirm('Are you sure you want to logout?')) {
                alert('Logging out...');
                // In real implementation: window.location.href = '/login';
            }
        }

        // Show/Hide Empty State
        function showEmptyState() {
            const eventsGrid = document.getElementById('eventsGrid');
            
            if (!document.querySelector('.empty-state')) {
                const emptyState = document.createElement('div');
                emptyState.className = 'empty-state';
                emptyState.innerHTML = `
                    <div class="empty-icon">
                        <i class="fas fa-search"></i>
                    </div>
                    <div class="empty-title">No Events Found</div>
                    <div class="empty-description">
                        Try adjusting your search criteria or filters to find events that match your interests.
                    </div>
                `;
                eventsGrid.appendChild(emptyState);
            }
        }

        function hideEmptyState() {
            const emptyState = document.querySelector('.empty-state');
            if (emptyState) {
                emptyState.remove();
            }
        }

        // Search functionality
        document.getElementById('searchInput').addEventListener('input', function() {
            clearTimeout(this.searchTimer);
            this.searchTimer = setTimeout(() => {
                applyFilters();
            }, 300); // Debounce search
        });

        // Enhanced interactions
        document.addEventListener('DOMContentLoaded', function() {
            // Animate cards on load
            const cards = document.querySelectorAll('.dashboard-card, .event-card, .ticket-card');
            cards.forEach((card, index) => {
                card.style.animationDelay = `${index * 0.1}s`;
            });

            // Add hover effects to event cards
            const eventCards = document.querySelectorAll('.event-card');
            eventCards.forEach(card => {
                card.addEventListener('mouseenter', function() {
                    this.querySelector('.event-image').style.transform = 'scale(1.05)';
                });
                
                card.addEventListener('mouseleave', function() {
                    this.querySelector('.event-image').style.transform = 'scale(1)';
                });
            });

            // Ticket QR code interactions
            const qrPlaceholders = document.querySelectorAll('.qr-placeholder');
            qrPlaceholders.forEach(qr => {
                qr.addEventListener('click', function() {
                    if (this.querySelector('.fa-qrcode')) {
                        alert('QR Code clicked! In a real app, this would show a larger view or scanning instructions.');
                    }
                });
            });
        });

        // Keyboard shortcuts
        document.addEventListener('keydown', function(e) {
            // Ctrl/Cmd + F for search focus
            if ((e.ctrlKey || e.metaKey) && e.key === 'f') {
                e.preventDefault();
                document.getElementById('searchInput').focus();
            }
        });

        // Auto-refresh ticket status (simulation)
        setInterval(function() {
            const pendingTickets = document.querySelectorAll('.ticket-status.pending');
            
            pendingTickets.forEach(status => {
                // Simulate random status updates
                if (Math.random() > 0.95) { // 5% chance per check
                    status.textContent = 'Confirmed';
                    status.classList.remove('pending');
                    status.classList.add('confirmed');
                    
                    // Update QR code
                    const qrPlaceholder = status.closest('.ticket-card').querySelector('.qr-placeholder');
                    qrPlaceholder.innerHTML = '<i class="fas fa-qrcode"></i>';
                    
                    // Show notification
                    alert('Great news! Your ticket has been confirmed and your QR code is now available.');
                }
            });
        }, 30000); // Check every 30 seconds