package com.poweringa.api.models;

import com.poweringa.api.enums.UserRole;
import lombok.Data;
import org.jspecify.annotations.Nullable;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
@Document(collection = "users")
public class User implements UserDetails {
    @Id
    private String id;

    private String email;
    private String senha;
    private UserRole cargo;
    private String descricao;
    private Integer pontos = 0;

    public User(String email, String senha, UserRole cargo, String descricao) {
        this.email = email;
        this.senha = senha;
        this.cargo = cargo;
        this.descricao = descricao;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (cargo == UserRole.GESTOR) {
            return List.of(
                    new SimpleGrantedAuthority("ROLE_GESTOR"),
                    new SimpleGrantedAuthority("ROLE_CLIENTE")
            );
        }

        return List.of(new SimpleGrantedAuthority("ROLE_CLIENTE"));
    }

    @Override
    public @Nullable String getPassword() {
        return senha;
    }

    @Override
    public String getUsername() {
        return email;
    }

}
