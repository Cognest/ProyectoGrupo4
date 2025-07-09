package com.atm.buenas_practicas_java.config;

import com.atm.buenas_practicas_java.entities.Usuario;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class CustomUserDetails implements UserDetails {
    private final Usuario usuario;

    public CustomUserDetails(Usuario usuario) {
        this.usuario = usuario;
    }

    @Override public Collection<? extends GrantedAuthority> getAuthorities() {
        return usuario.getToken() != null && usuario.getToken() == 1
                ? List.of((GrantedAuthority) () -> "ROLE_ADMIN")
                : List.of((GrantedAuthority) () -> "ROLE_USER");
    }

    @Override public String getPassword() { return usuario.getPassword(); }
    @Override public String getUsername() { return usuario.getNickname(); }

    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked()  { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }

    public String getEmail() { return usuario.getEmail(); }
}
