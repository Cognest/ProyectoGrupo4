package com.atm.buenas_practicas_java.services;

import com.atm.buenas_practicas_java.dtos.UsuarioDto;
import com.atm.buenas_practicas_java.dtos.UsuarioRegistroDto;
import com.atm.buenas_practicas_java.entities.UserRol;
import com.atm.buenas_practicas_java.entities.Usuario;
import com.atm.buenas_practicas_java.repositories.UsuarioRepo;
import com.atm.buenas_practicas_java.services.mapper.UsuarioMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService extends AbstractBusinessService<Usuario, Integer, UsuarioDto,
        UsuarioRepo, UsuarioMapper> {

    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private UsuarioRepo usuarioRepo;

    @Autowired
    private UsuarioMapper usuarioMapper;


    public UsuarioService(UsuarioRepo repo, UsuarioMapper mapper, BCryptPasswordEncoder passwordEncoder) {
        super(repo, mapper);
        this.passwordEncoder = passwordEncoder;
    }

    public boolean emailONicknameExiste(String email, String nickname) {
        return getRepo().findAll().stream()
                .anyMatch(u -> u.getEmail().equalsIgnoreCase(email) || u.getNickname().equalsIgnoreCase(nickname));
    }

    @Transactional
    public void registrarUsuario(UsuarioRegistroDto dto) {
        Usuario usuario = usuarioMapper.toEntity(dto);
        //se pone el rol por defecto
        usuario.setRol(UserRol.valueOf("USUARIO"));
        usuario.setPassword(passwordEncoder.encode(dto.getPassword()));
        usuario.setToken(0); // si es necesario
        System.out.println("🧪 Usuario antes de guardar: " + usuario);
        Usuario usuarioGuardado = usuarioRepo.save(usuario);
        System.out.println("Usuario guardado con ID: {}" + usuarioGuardado.getId());

        List<Usuario> usuarios = usuarioRepo.findAll();
        System.out.println("📋 Lista de usuarios en la base de datos:");
        for (Usuario u : usuarios) {
            System.out.println(" - ID: " + u.getId() + ", Email: " + u.getEmail() + ", Nickname: " + u.getNickname());
        }
    }

    @Transactional
    public UsuarioDto autenticarUsuario(String emailONickname, String password) {
        Optional<Usuario> usuarioOpt = usuarioRepo.findByEmailOrNickname(emailONickname, emailONickname);
        if (usuarioOpt.isPresent()) {
            Usuario u = usuarioOpt.get();
            if (passwordEncoder.matches(password, u.getPassword())) {
                return usuarioMapper.toDto(u);
            }
        }
        return null;
    }

}
