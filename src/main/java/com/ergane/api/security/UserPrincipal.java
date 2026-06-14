package com.ergane.api.security;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class UserPrincipal {

    String userId;
    String cpf;
    String nome;

}