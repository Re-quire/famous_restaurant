package module;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.groom.yummy.controller.UserController;
import com.groom.yummy.jwt.JwtProvider;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

//@WebMvcTest(controllers = {UserController.class})
//@ExtendWith(MockitoExtension.class) // Mockito 확장 사용
//public class ControllerTestSupport {
//
//    @Autowired
//    protected MockMvc mockMvc;
//    @Autowired
//    protected ObjectMapper objectMapper;
//    @Mock
//    protected JwtProvider jwtProvider;
//}
