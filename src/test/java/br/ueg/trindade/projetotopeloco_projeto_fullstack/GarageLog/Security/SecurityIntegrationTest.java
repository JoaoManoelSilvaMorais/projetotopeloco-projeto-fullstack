package br.ueg.trindade.projetotopeloco_projeto_fullstack.GarageLog.Security;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.ueg.trindade.projetotopeloco_projeto_fullstack.GarageLog.Service.UsuarioService;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@TestExecutionListeners(listeners = {
        DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class
})
public class SecurityIntegrationTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
    }

    private String loginAndGetToken(String username, String senha) throws Exception {
        LoginRequestDTO request = new LoginRequestDTO(username, senha);
        MvcResult result = mockMvc.perform(post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn();

        JsonNode root = objectMapper.readTree(result.getResponse().getContentAsString());
        return root.get("token").asText();
    }

    @Test
    @DisplayName("Login com credenciais de admin válidas deve retornar 200, JWT token, nome e permissões")
    void testLoginAdminSuccess() throws Exception {
        LoginRequestDTO request = new LoginRequestDTO("admin", "admin123");

        mockMvc.perform(post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token", notNullValue()))
                .andExpect(jsonPath("$.username", is("admin")))
                .andExpect(jsonPath("$.nome", is("Administrador")))
                .andExpect(jsonPath("$.permissoes", hasItem("ADMIN")));
    }

    @Test
    @DisplayName("Login com credenciais de user válidas deve retornar 200, JWT token, nome e permissões")
    void testLoginUserSuccess() throws Exception {
        LoginRequestDTO request = new LoginRequestDTO("user", "user123");

        mockMvc.perform(post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token", notNullValue()))
                .andExpect(jsonPath("$.username", is("user")))
                .andExpect(jsonPath("$.nome", is("Usuário Comum")))
                .andExpect(jsonPath("$.permissoes", hasItem("USER")));
    }

    @Test
    @DisplayName("Login com senha incorreta deve retornar 401 Unauthorized")
    void testLoginInvalidPasswordReturns401() throws Exception {
        LoginRequestDTO request = new LoginRequestDTO("admin", "senhaIncorreta");

        mockMvc.perform(post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.mensagem", is("Credenciais inválidas")));
    }

    @Test
    @DisplayName("Login com usuário inexistente deve retornar 401 Unauthorized")
    void testLoginNonExistentUserReturns401() throws Exception {
        LoginRequestDTO request = new LoginRequestDTO("usuarioInexistente", "qualquerSenha");

        mockMvc.perform(post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.mensagem", is("Credenciais inválidas")));
    }

    @Test
    @DisplayName("Requisição não autenticada para endpoint protegido (/api/usuarios) deve retornar 401")
    void testUnauthenticatedRequestReturns401() throws Exception {
        mockMvc.perform(get("/api/usuarios"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Requisição com perfil USER para endpoint restrito (/api/usuarios) deve retornar 403 Forbidden")
    void testUserRoleForbiddenFromUsuarios() throws Exception {
        String userToken = loginAndGetToken("user", "user123");

        mockMvc.perform(get("/api/usuarios")
                        .header("Authorization", "Bearer " + userToken))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Requisição com perfil ADMIN para endpoint restrito (/api/usuarios) deve retornar 200 OK")
    void testAdminRoleAllowedToUsuarios() throws Exception {
        String adminToken = loginAndGetToken("admin", "admin123");

        mockMvc.perform(get("/api/usuarios")
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Requisição com perfil USER para /api/permissoes deve retornar 403 Forbidden")
    void testUserRoleForbiddenFromPermissoes() throws Exception {
        String userToken = loginAndGetToken("user", "user123");

        mockMvc.perform(get("/api/permissoes")
                        .header("Authorization", "Bearer " + userToken))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Requisição com perfil ADMIN para /api/permissoes deve retornar 200 OK")
    void testAdminRoleAllowedToPermissoes() throws Exception {
        String adminToken = loginAndGetToken("admin", "admin123");

        mockMvc.perform(get("/api/permissoes")
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Requisição autenticada como USER para endpoint comum (/api/veiculos) deve retornar 200 OK")
    void testUserRoleCanAccessVeiculos() throws Exception {
        String userToken = loginAndGetToken("user", "user123");

        mockMvc.perform(get("/api/veiculos")
                        .header("Authorization", "Bearer " + userToken))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("JwtUtil gera token válido e extrai claims corretamente")
    void testJwtUtilOperations() {
        String token = jwtUtil.generateToken("testeUsuario", List.of("USER", "ADMIN"));
        assertTrue(jwtUtil.validateToken(token));
        assertEquals("testeUsuario", jwtUtil.extractUsername(token));
        List<String> roles = jwtUtil.extractRoles(token);
        assertTrue(roles.contains("USER"));
        assertTrue(roles.contains("ADMIN"));
        assertFalse(jwtUtil.validateToken("token.invalido.qualquer"));
    }

    @Test
    @DisplayName("Endpoint de senha padrão exige ADMIN e retorna senha padrão")
    void testSenhaPadraoEndpointAuthorization() throws Exception {
        String userToken = loginAndGetToken("user", "user123");
        mockMvc.perform(get("/api/usuarios/senha-padrao")
                        .header("Authorization", "Bearer " + userToken))
                .andExpect(status().isForbidden());

        String adminToken = loginAndGetToken("admin", "admin123");
        mockMvc.perform(get("/api/usuarios/senha-padrao")
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.senhaPadrao", is(UsuarioService.SENHA_PADRAO)));
    }

    @Test
    @DisplayName("Admin cria usuário sem senha -> recebe senha padrão, precisaTrocarSenha=true e troca via /api/alterar-senha")
    void testAdminCreatesUserWithDefaultPasswordAndUserChangesPassword() throws Exception {
        String adminToken = loginAndGetToken("admin", "admin123");

        Map<String, String> novoUsuario = Map.of(
                "nome", "Novo Usuário",
                "username", "novousuario",
                "email", "novousuario@garagelog.com");

        mockMvc.perform(post("/api/usuarios")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(novoUsuario)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", is("novousuario")))
                .andExpect(jsonPath("$.precisaTrocarSenha", is(true)))
                .andExpect(jsonPath("$.senha").doesNotExist());

        // Login inicial com senha padrão retorna precisaTrocarSenha = true e perfil USER
        MvcResult loginResult = mockMvc.perform(post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                new LoginRequestDTO("novousuario", UsuarioService.SENHA_PADRAO))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token", notNullValue()))
                .andExpect(jsonPath("$.precisaTrocarSenha", is(true)))
                .andExpect(jsonPath("$.permissoes", hasItem("USER")))
                .andReturn();

        String novoUserToken = objectMapper.readTree(loginResult.getResponse().getContentAsString())
                .get("token").asText();

        // Requisição sem autenticação para alterar senha deve retornar 401
        mockMvc.perform(post("/api/alterar-senha")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                new AlterarSenhaRequestDTO(UsuarioService.SENHA_PADRAO, "NovaSenha@2026"))))
                .andExpect(status().isUnauthorized());

        // Senha atual incorreta deve retornar 400
        mockMvc.perform(post("/api/alterar-senha")
                        .header("Authorization", "Bearer " + novoUserToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                new AlterarSenhaRequestDTO("errada", "NovaSenha@2026"))))
                .andExpect(status().isBadRequest());

        // Tentar manter a senha padrão deve retornar 400
        mockMvc.perform(post("/api/alterar-senha")
                        .header("Authorization", "Bearer " + novoUserToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                new AlterarSenhaRequestDTO(UsuarioService.SENHA_PADRAO, UsuarioService.SENHA_PADRAO))))
                .andExpect(status().isBadRequest());

        // Troca válida limpa precisaTrocarSenha
        mockMvc.perform(post("/api/alterar-senha")
                        .header("Authorization", "Bearer " + novoUserToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                new AlterarSenhaRequestDTO(UsuarioService.SENHA_PADRAO, "NovaSenha@2026"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.precisaTrocarSenha", is(false)));

        // Login antigo com senha padrão falha
        mockMvc.perform(post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                new LoginRequestDTO("novousuario", UsuarioService.SENHA_PADRAO))))
                .andExpect(status().isUnauthorized());

        // Novo login com a senha alterada retorna precisaTrocarSenha = false
        mockMvc.perform(post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                new LoginRequestDTO("novousuario", "NovaSenha@2026"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.precisaTrocarSenha", is(false)));
    }
}
