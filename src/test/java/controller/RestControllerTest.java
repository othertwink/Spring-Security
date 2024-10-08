package controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.othertwink.employeeapp.EmployeeAppApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = EmployeeAppApplication.class)
@AutoConfigureMockMvc
public class RestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        // Настройка, если необходимо
    }

    @Test
    @WithMockUser(roles = "USER") // Имитация пользователя с ролью USER
    void testAccessEmployeePageAsUser() throws Exception {
        mockMvc.perform(get("/employee/1"))
                .andExpect(status().isForbidden()); // Ожидаем ошибку 403
    }

    @Test
    @WithMockUser(roles = "ADMIN") // Имитация пользователя с ролью ADMIN
    void testAccessEmployeePageAsAdmin() throws Exception {
        mockMvc.perform(get("/employee/1"))
                .andExpect(status().isOk()); // Ожидаем успешный доступ
    }
}