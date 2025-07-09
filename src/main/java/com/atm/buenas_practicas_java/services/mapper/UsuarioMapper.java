package com.atm.buenas_practicas_java.services.mapper;

import com.atm.buenas_practicas_java.dtos.UsuarioDto;
import com.atm.buenas_practicas_java.dtos.UsuarioRegistroDto;
import com.atm.buenas_practicas_java.entities.Usuario;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class UsuarioMapper extends AbstractServiceMapper<Usuario, UsuarioDto> {

    @Override
    public UsuarioDto toDto(Usuario entidad) {
        UsuarioDto dto = new UsuarioDto();
        new ModelMapper().map(entidad, dto);
        return dto;
    }

    @Override
    public Usuario toEntity(UsuarioDto dto) {
        Usuario entidad = new Usuario();
        new ModelMapper().map(dto, entidad);
        return entidad;
    }

    public Usuario toEntity(UsuarioRegistroDto dto) {
        Usuario u = new Usuario();
        u.setNombre(dto.getNombre());
        u.setApellidos(dto.getApellidos());
        u.setNickname(dto.getNickname());
        u.setEmail(dto.getEmail());
        u.setPassword(dto.getPassword()); // Aún sin encriptar
        u.setToken(0);
        return u;
    }
}

