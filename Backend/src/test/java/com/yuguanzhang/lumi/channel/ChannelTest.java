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
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest
@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS) // @BeforeAll에서 static 제거 가능
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

    @BeforeAll
    void setUp() {
        // 테스트용 유저 저장
        if (userRepository.findByEmail(TEST_USER_EMAIL).isEmpty()) {
            User testUser = User.builder()
                    .name("테스트유저")
                    .password("1234")
                    .email(TEST_USER_EMAIL)
                    .isDeleted("N")
                    .isPrivacyAgreement(true)
                    .isVerified(true)
                    .build();
            userRepository.save(testUser);
        }

        // TUTOR 역할 없으면 생성
        if (roleRepository.findByRoleName(RoleName.TUTOR).isEmpty()) {
            roleRepository.save(Role.builder()
                    .roleName(RoleName.TUTOR)
                    .build());
        }
    }

    @Test
    @Rollback
    @WithUserDetails(value = TEST_USER_EMAIL, userDetailsServiceBeanName = "testUserDetailsService")
    void channelTest() throws Exception {
        // 채널 생성 요청
        ChannelRequestDto createDto = ChannelRequestDto.builder()
                .name("통합 테스트 채널")
                .subject("SpringBoot")
                .build();

        String createResponse = mockMvc.perform(post("/api/channels")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data[0].name").value("통합 테스트 채널"))
                .andExpect(jsonPath("$.data[0].subject").value("SpringBoot"))
                .andReturn()
                .getResponse()
                .getContentAsString();

        // DB에서 생성된 채널 ID 가져오기
        Channel created = channelRepository.findAll().get(0);
        Long channelId = created.getChannelId();

        // 채널 상세 조회
        mockMvc.perform(get("/api/channels/" + channelId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].name").value("통합 테스트 채널"))
                .andExpect(jsonPath("$.data[0].subject").value("SpringBoot"));

        // 채널 수정
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

        // DELETE 테스트 - null-safe
        mockMvc.perform(delete("/api/channels/" + channelId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].channelId").value(channelId))
                .andExpect(jsonPath("$.data[0].name").exists())
                .andExpect(jsonPath("$.data[0].subject").exists())
                // 삭제 후 roleName / requestUserId는 null 가능
                .andExpect(jsonPath("$.data[0].roleName").isEmpty())
                .andExpect(jsonPath("$.data[0].requestUserId").isEmpty());
    }

    @TestConfiguration
    static class TestSecurityConfig {
        // 테스트용 UserDetailsService를 정의하여 @WithUserDetails 사용 가능하게 함
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
