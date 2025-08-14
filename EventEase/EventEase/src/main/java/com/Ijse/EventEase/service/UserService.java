package com.Ijse.EventEase.service;

import com.Ijse.EventEase.dto.AuthDTO;
import com.Ijse.EventEase.dto.AuthResponceDto;
import com.Ijse.EventEase.dto.RegisterDTO;

public interface UserService {
  AuthResponceDto authenticate(AuthDTO authDTO);
  String registerUser(RegisterDTO registerDTO);
}
