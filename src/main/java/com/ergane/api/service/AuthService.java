package com.ergane.api.service;

import com.ergane.api.dto.request.LoginRequest;
import com.ergane.api.dto.response.LoginResponse;

public interface AuthService {

    LoginResponse login(LoginRequest request);

}