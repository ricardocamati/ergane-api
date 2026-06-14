package com.ergane.api.service.impl;

import com.ergane.api.dto.request.LoginRequest;
import com.ergane.api.dto.response.LoginResponse;
import com.ergane.api.dto.response.UsuarioResumoResponse;
import com.ergane.api.exception.ApiException;
import com.ergane.api.model.Usuario;
import com.ergane.api.repository.UsuarioRepository;
import com.ergane.api.security.JwtService;
import com.ergane.api.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UsuarioRepository userRepository;
    private final JwtService jwtService;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public LoginResponse login(LoginRequest request) {
        Usuario user = userRepository.findByCpf(request.getCpf()).orElseThrow(() -> new ApiException(HttpStatus.UNAUTHORIZED, "CPF ou senha inválidos."));

        if (!passwordEncoder.matches(request.getSenha(), user.getSenhaHash())) {
            throw new ApiException(HttpStatus.UNAUTHORIZED, "CPF ou senha inválidos.");
        }

        String token = jwtService.generateToken(user.getId(), user.getCpf(), user.getNome());

        return LoginResponse.builder().token(token).usuario(UsuarioResumoResponse.builder().id(user.getId()).nome(user.getNome()).build()).build();
    }

}