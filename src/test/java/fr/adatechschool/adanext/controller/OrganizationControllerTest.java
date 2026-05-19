package fr.adatechschool.adanext.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.adatechschool.adanext.dto.request.CreateOrganizationRequest;
import fr.adatechschool.adanext.dto.request.RegisterRequest;
import fr.adatechschool.adanext.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class OrganizationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AuthService authService;

    private MockHttpSession session;

    @BeforeEach
    void setUp() throws Exception {
        RegisterRequest register = new RegisterRequest();
        register.setEmail("test@example.com");
        register.setPassword("password123");
        register.setName("Test User");
        authService.register(register);

        MvcResult loginResult = mockMvc.perform(post("/api/auth/login")
                .with(csrf())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("email", "test@example.com")
                .param("password", "password123"))
            .andExpect(status().isOk())
            .andReturn();

        session = (MockHttpSession) loginResult.getRequest().getSession();
    }

    @Test
    void create_shouldReturn201WithOrganization() throws Exception {
        CreateOrganizationRequest request = new CreateOrganizationRequest();
        request.setName("Equipe Ada");
        request.setDescription("Notre organisation");

        mockMvc.perform(post("/api/organizations")
                .session(session)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.name").value("Equipe Ada"));
    }

    @Test
    void findAll_shouldReturn200WithList() throws Exception {
        mockMvc.perform(get("/api/organizations").session(session))
            .andExpect(status().isOk());
    }

    @Test
    void create_shouldReturn401WhenNotAuthenticated() throws Exception {
        CreateOrganizationRequest request = new CreateOrganizationRequest();
        request.setName("Test");

        mockMvc.perform(post("/api/organizations")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isUnauthorized());
    }
}
