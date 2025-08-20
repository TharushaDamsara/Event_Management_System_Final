package com.Ijse.EventEase.service;

import com.Ijse.EventEase.dto.AuthDTO;
import com.Ijse.EventEase.dto.AuthResponceDto;
import com.Ijse.EventEase.dto.RegisterDTO;
import com.Ijse.EventEase.exception.EmailDuplicateException;
import com.Ijse.EventEase.exception.UserEmailNotFoundException;

public interface UserService {
  AuthResponceDto authenticate(AuthDTO authDTO) throws UserEmailNotFoundException;
  String registerUser(RegisterDTO registerDTO) throws  EmailDuplicateException;
}
