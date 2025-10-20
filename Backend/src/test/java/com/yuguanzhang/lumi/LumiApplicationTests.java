package com.yuguanzhang.lumi;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.Disabled;

@SpringBootTest
class LumiApplicationTests {

	@Test
	@Disabled("CI 환경에서는 application.yml placeholder 문제로 비활성화")
	void contextLoads() {
	}
}
