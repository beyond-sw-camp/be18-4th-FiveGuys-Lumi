package com.yuguanzhang.lumi;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yuguanzhang.lumi.channel.dto.ChannelRequestDto;
import com.yuguanzhang.lumi.channel.entity.Channel;
import com.yuguanzhang.lumi.channel.repository.ChannelRepository;
import com.yuguanzhang.lumi.role.entity.Role;
import com.yuguanzhang.lumi.role.entity.RoleName;
import com.yuguanzhang.lumi.role.repositiry.RoleRepository;
import com.yuguanzhang.lumi.user.entity.User;
import com.yuguanzhang.lumi.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * CI 환경에서도 H2 DB를 이용해 완전히 독립적으로 실행 가능하도록 구성된 통합 테스트 클래스
 */
@AutoConfigureMockMvc(addFilters = false)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        exclude = {
                SecurityAutoConfiguration.class, // ✅ 스프링 시큐리티 비활성화
                org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration.class,
                org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration.class,
                org.springframework.boot.autoconfigure.mail.MailSenderAutoConfiguration.class
        }
)
@Transactional
public class ChannelTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ChannelRepository channelRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    private static final String TEST_USER_EMAIL = "test@test.com";

    @BeforeEach
    void setUp() {
        // 테스트 환경 정리
        userRepository.deleteAll();
        roleRepository.deleteAll();
        channelRepository.deleteAll();

        // 테스트용 유저 생성
        User testUser = User.builder()
                .name("테스트유저")
                .password("1234")
                .email(TEST_USER_EMAIL)
                .isDeleted("N")
                .isPrivacyAgreement(true)
                .isVerified(true)
                .build();
        userRepository.save(testUser);

        // 테스트용 역할 생성
        roleRepository.save(Role.builder()
                .roleName(RoleName.TUTOR)
                .build());
    }

    @Test
    @Rollback
    void channelTest() throws Exception {
        // 1️⃣ 채널 생성
        ChannelRequestDto createDto = ChannelRequestDto.builder()
                .name("통합 테스트 채널")
                .subject("SpringBoot")
                .build();

        mockMvc.perform(post("/api/channels")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data[0].name").value("통합 테스트 채널"))
                .andExpect(jsonPath("$.data[0].subject").value("SpringBoot"));

        // 2️⃣ 생성 확인
        Channel created = channelRepository.findAll().get(0);
        Long channelId = created.getChannelId();

        // 3️⃣ 조회 테스트
        mockMvc.perform(get("/api/channels/" + channelId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].name").value("통합 테스트 채널"));

        // 4️⃣ 수정 테스트
        ChannelRequestDto updateDto = ChannelRequestDto.builder()
                .name("수정 된 채널명")
                .subject("JPA")
                .build();

        mockMvc.perform(put("/api/channels/" + channelId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].name").value("수정 된 채널명"))
                .andExpect(jsonPath("$.data[0].subject").value("JPA"));

        // 5️⃣ 삭제 테스트
        mockMvc.perform(delete("/api/channels/" + channelId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].channelId").value(channelId));
    }

    /**
     * 테스트 환경에서만 동작하는 시큐리티 대체 Bean 구성
     */
    @TestConfiguration
    static class TestSecurityConfig {
        @Bean
        @Primary
        public UserDetailsService testUserDetailsService(UserRepository userRepository) {
            return username -> {
                User user = userRepository.findByEmail(username)
                        .orElseThrow(() -> new RuntimeException("테스트 유저가 존재하지 않습니다."));
                return new com.yuguanzhang.lumi.user.dto.UserDetailsDto(user);
            };
        }
    }
}
