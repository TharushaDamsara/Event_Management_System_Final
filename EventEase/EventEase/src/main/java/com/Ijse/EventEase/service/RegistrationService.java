package com.Ijse.EventEase.service;

import com.Ijse.EventEase.dto.ApiResponce;
import com.Ijse.EventEase.dto.RegistrationDto;
import com.Ijse.EventEase.exception.EventNotFoundException;
import com.Ijse.EventEase.exception.RegistrationNotFoundException;
import com.Ijse.EventEase.exception.UserNotFoundException;

public interface RegistrationService {
    ApiResponce registerRegistration(RegistrationDto registrationDto) throws EventNotFoundException, UserNotFoundException;
    ApiResponce getAllRegistration();
    ApiResponce getRegistrationById(Long id) throws RegistrationNotFoundException;
    ApiResponce deleteRegistration(Long id) throws RegistrationNotFoundException;
    ApiResponce updateRegistration(Long id, RegistrationDto registrationDto) throws RegistrationNotFoundException, EventNotFoundException, UserNotFoundException;
    ApiResponce getRegistrationByEventId(Long eventId) throws RegistrationNotFoundException;
    ApiResponce getRegistrationByAttendeeId(Long attendeeId) throws RegistrationNotFoundException;

}
