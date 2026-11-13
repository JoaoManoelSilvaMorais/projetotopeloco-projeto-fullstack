package br.ueg.trindade.projetotopeloco_projeto_fullstack.GarageLog.Security;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.ueg.trindade.projetotopeloco_projeto_fullstack.GarageLog.Model.PermissaoModel;
import br.ueg.trindade.projetotopeloco_projeto_fullstack.GarageLog.Model.UsuarioModel;
import br.ueg.trindade.projetotopeloco_projeto_fullstack.GarageLog.Repository.UsuarioRepository;

@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public LoginResponseDTO autenticar(LoginRequestDTO request) {
        if (request == null || request.getUsername() == null || request.getSenha() == null) {
            throw new BadCredentialsException("Credenciais inválidas");
        }

        UsuarioModel usuario = usuarioRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new BadCredentialsException("Credenciais inválidas"));

        if (usuario.getSenha() == null || !passwordEncoder.matches(request.getSenha(), usuario.getSenha())) {
            throw new BadCredentialsException("Credenciais inválidas");
        }

        List<String> permissoes = usuario.getPermissoes().stream()
                .map(PermissaoModel::getNome)
                .collect(Collectors.toList());

        String token = jwtUtil.generateToken(usuario.getUsername(), permissoes);

        return new LoginResponseDTO(
                token,
                usuario.getNome(),
                usuario.getUsername(),
                permissoes,
                usuario.isPrecisaTrocarSenha());
    }
}
