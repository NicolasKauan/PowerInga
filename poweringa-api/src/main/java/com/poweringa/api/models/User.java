package com.poweringa.api.models;

import com.poweringa.api.enums.UserRole;
import lombok.Data;
import org.jspecify.annotations.Nullable;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Data
@Document(collection = "users")
public class User implements UserDetails {
    @Id
    private String idUsers;
    private String email;
    private String senha;
    private UserRole cargo;
    private String descricao;
    private Integer pontos;

    public User(String email, String senha, UserRole cargo, String descricao, Integer pontos) {
        this.email = email;
        this.senha = senha;
        this.cargo = cargo;
        this.descricao = descricao;
        this.pontos = pontos;
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
    public String getUsername() {
        return email;
    }

    @Override
    public @Nullable String getPassword() {
        return senha;
    }
}
