package com.Ijse.EventEase.service.impl;

import com.Ijse.EventEase.dto.ApiResponce;
import com.Ijse.EventEase.dto.RegistrationDto;
import com.Ijse.EventEase.entity.Event;
import com.Ijse.EventEase.entity.Registration;
import com.Ijse.EventEase.entity.Ticket;
import com.Ijse.EventEase.entity.User;
import com.Ijse.EventEase.exception.EventNotFoundException;
import com.Ijse.EventEase.exception.RegistrationNotFoundException;
import com.Ijse.EventEase.exception.UserNotFoundException;
import com.Ijse.EventEase.repository.EventRepository;
import com.Ijse.EventEase.repository.RegistrationRepository;
import com.Ijse.EventEase.repository.TicketRepository;
import com.Ijse.EventEase.repository.UserRepository;
import com.Ijse.EventEase.service.RegistrationService;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RegistrationServiceImpl implements RegistrationService {

    private final RegistrationRepository registrationRepository;
    private final EventRepository eventRepository;
    private final UserRepository attendeeRepository;
    private final TicketRepository ticketRepository;
    private final JavaMailSender mailSender; // ✅ Inject mail sender

    /**
     * 🔹 Utility method to generate QR Code PNG and return its path
     */
    private String generateQRCode(String data, String folderPath) {
        try {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(data, BarcodeFormat.QR_CODE, 300, 300);

            // Ensure directory exists
            File dir = new File(folderPath);
            if (!dir.exists()) dir.mkdirs();

            // Unique file name
            String fileName = "qrcode_" + UUID.randomUUID() + ".png";
            Path path = FileSystems.getDefault().getPath(folderPath + File.separator + fileName);

            MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);
            return path.toString();
        } catch (WriterException | IOException e) {
            throw new RuntimeException("Failed to generate QR code", e);
        }
    }

    /**
     * 🔹 Utility method to send email with QR code attachment
     */
    private void sendEmailWithQRCode(String to, String subject, String body, String qrCodePath) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, true);

            if (qrCodePath != null) {
                FileSystemResource file = new FileSystemResource(new File(qrCodePath));
                helper.addAttachment("ticket_qrcode.png", file);
            }

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email with QR code", e);
        }
    }

    @Override
    @Transactional
    public ApiResponce registerRegistration(RegistrationDto registrationDto)
            throws EventNotFoundException, UserNotFoundException {

        // 🔹 Check if Event exists
        Event event = eventRepository.findById(registrationDto.getEventId())
                .orElseThrow(() -> new EventNotFoundException("Event not found with ID: " + registrationDto.getEventId()));

        // 🔹 Check if User exists
        User attendee = attendeeRepository.findById(registrationDto.getAttendeeId())
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + registrationDto.getAttendeeId()));

        // 🔹 Check if already registered
        Registration existing = registrationRepository.findByEvent_IdAndAttendee_Id(
                registrationDto.getEventId(),
                registrationDto.getAttendeeId()
        );
        if (existing != null) {
            return new ApiResponce(409, "User already registered for this event", existing);
        }

        // 🔹 Validate ticket type & availability
        Ticket ticket = ticketRepository.findByEventIdAndName(
                registrationDto.getEventId(),
                registrationDto.getTicketType()
        ).orElseThrow(() -> new RuntimeException("Ticket type not found for this event"));

        if (ticket.getQuantity() <= 0) {
            return new ApiResponce(400, "Selected ticket type is sold out", null);
        }

        // 🔹 Deduct ticket count
        ticket.setQuantity(ticket.getQuantity() - 1);
        ticketRepository.save(ticket);

        // 🔹 Generate QR Code with details
        String qrData = "Event: " + event.getTitle() +
                "\nUser: " + registrationDto.getFirstName() + " " + registrationDto.getLastName() +
                "\nTicket: " + ticket.getName() +
                "\nAmount: " + ticket.getPrice() +
                "\nType: " + registrationDto.getTicketType() +
                "\nDate: " + LocalDateTime.now();

        String qrCodePath = generateQRCode(qrData, "qrcodes");

        // 🔹 Build new Registration
        Registration registration = Registration.builder()
                .firstName(registrationDto.getFirstName())
                .lastName(registrationDto.getLastName())
                .email(registrationDto.getEmail())
                .phone(registrationDto.getPhone())
                .ticketType(registrationDto.getTicketType())
                .paymentMethode(registrationDto.getPaymentMethode())
                .amount(ticket.getPrice()) // ✅ always take from DB
                .attendee(attendee)
                .event(event)
                .registrationTime(LocalDateTime.now())
                .qrCodePath(qrCodePath) // ✅ saved QR path
                .emailSent(false)
                .whatsappSent(false)
                .build();

        Registration saved = registrationRepository.save(registration);

        if (saved != null) {
            String emailBody = "<h2>Thank you for registering!</h2>" +
                    "<p>Event: " + event.getTitle() + "</p>" +
                    "<p>Ticket: " + ticket.getName() + " (LKR " + ticket.getPrice() + ")</p>" +
                    "<p>Please find your QR code attached.</p>";
            sendEmailWithQRCode(registrationDto.getEmail(), "Event Ticket - " + event.getTitle(), emailBody, qrCodePath);

            // 🔹 Update status
            saved.setEmailSent(true);
            registrationRepository.save(saved);
        }
        // TODO: 🔹 Integrate WhatsApp API (e.g., Twilio, Meta Cloud API)
        // sendWhatsAppMessage(saved.getPhone(), qrCodePath);

        return new ApiResponce(201, "Registration successful. Ticket sent via email.", saved);
    }

    @Override
    public ApiResponce getAllRegistration() {
        List<Registration> registrations = registrationRepository.findAll();
        if (registrations.isEmpty()) {
            return new ApiResponce(204, "No registrations found", null);
        }
        return new ApiResponce(200, "Registrations retrieved successfully", registrations);
    }

    @Override
    public ApiResponce getRegistrationById(Long id) throws RegistrationNotFoundException {
        Registration registration = registrationRepository.findById(id)
                .orElseThrow(() -> new RegistrationNotFoundException("Registration not found with ID: " + id));
        return new ApiResponce(200, "Registration found", registration);
    }

    @Override
    public ApiResponce deleteRegistration(Long id) throws RegistrationNotFoundException {
        Registration registration = registrationRepository.findById(id)
                .orElseThrow(() -> new RegistrationNotFoundException("Registration not found with ID: " + id));
        registrationRepository.delete(registration);
        return new ApiResponce(200, "Registration deleted successfully", null);
    }

    @Override
    @Transactional
    public ApiResponce updateRegistration(Long id, RegistrationDto registrationDto)
            throws RegistrationNotFoundException, EventNotFoundException, UserNotFoundException {

        Registration registration = registrationRepository.findById(id)
                .orElseThrow(() -> new RegistrationNotFoundException("Registration not found with ID: " + id));

        // 🔹 Check Event
        Event event = eventRepository.findById(registrationDto.getEventId())
                .orElseThrow(() -> new EventNotFoundException("Event not found with ID: " + registrationDto.getEventId()));

        // 🔹 Check User
        User attendee = attendeeRepository.findById(registrationDto.getAttendeeId())
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + registrationDto.getAttendeeId()));

        // 🔹 Handle ticket change
        if (!registration.getTicketType().equals(registrationDto.getTicketType())) {
            // Restore old ticket
            ticketRepository.findByEventIdAndName(registration.getEvent().getId(), registration.getTicketType())
                    .ifPresent(oldTicket -> {
                        oldTicket.setQuantity(oldTicket.getQuantity() + 1);
                        ticketRepository.save(oldTicket);
                    });

            // Deduct new ticket
            Ticket newTicket = ticketRepository.findByEventIdAndName(
                    registrationDto.getEventId(), registrationDto.getTicketType()
            ).orElseThrow(() -> new RuntimeException("New ticket type not found for this event"));

            if (newTicket.getQuantity() <= 0) {
                return new ApiResponce(400, "Selected ticket type is sold out", null);
            }

            newTicket.setQuantity(newTicket.getQuantity() - 1);
            ticketRepository.save(newTicket);

            // Set updated ticket type & price
            registration.setTicketType(newTicket.getName());
            registration.setAmount(newTicket.getPrice());

            // 🔹 Regenerate QR Code since ticket type changed
            String qrData = "Event: " + event.getTitle() +
                    "\nUser: " + registrationDto.getFirstName() + " " + registrationDto.getLastName() +
                    "\nTicket: " + newTicket.getName() +
                    "\nAmount: " + newTicket.getPrice() +
                    "\nType: " + registrationDto.getTicketType() +
                    "\nUpdated: " + LocalDateTime.now();

            String qrCodePath = generateQRCode(qrData, "qrcodes");
            registration.setQrCodePath(qrCodePath);

            // Resend updated ticket via email
            String emailBody = "<h2>Your ticket has been updated!</h2>" +
                    "<p>Event: " + event.getTitle() + "</p>" +
                    "<p>New Ticket: " + newTicket.getName() + " (LKR " + newTicket.getPrice() + ")</p>" +
                    "<p>Attached is your updated QR code.</p>";
            sendEmailWithQRCode(registration.getEmail(), "Updated Ticket - " + event.getTitle(), emailBody, qrCodePath);

            registration.setEmailSent(true);
        }

        // 🔹 Update other fields
        registration.setFirstName(registrationDto.getFirstName());
        registration.setLastName(registrationDto.getLastName());
        registration.setEmail(registrationDto.getEmail());
        registration.setPhone(registrationDto.getPhone());
        registration.setPaymentMethode(registrationDto.getPaymentMethode());
        registration.setEvent(event);
        registration.setAttendee(attendee);

        Registration updated = registrationRepository.save(registration);
        return new ApiResponce(200, "Registration updated successfully", updated);
    }

    @Override
    public ApiResponce getRegistrationByEventId(Long eventId) throws RegistrationNotFoundException {
        List<Registration> registrations = registrationRepository.findByEvent_Id(eventId);
        if (registrations.isEmpty()) {
            throw new RegistrationNotFoundException("No registrations found for event ID: " + eventId);
        }
        return new ApiResponce(200, "Registrations found", registrations);
    }

    @Override
    public ApiResponce getRegistrationByAttendeeId(Long attendeeId) throws RegistrationNotFoundException {
        List<Registration> registrations = registrationRepository.findByAttendee_Id(attendeeId);
        if (registrations == null || registrations.isEmpty()) {
            throw new RegistrationNotFoundException("No registrations found for attendee with ID: " + attendeeId);
        }
        return new ApiResponce(200, "Registrations found successfully", registrations);
    }
}
