// package com.placementportal.placement_website;

// import org.junit.jupiter.api.Test;
// import org.springframework.boot.test.context.SpringBootTest;

// @SpringBootTest
// class PlacementWebsiteApplicationTests {

// 	@Test
// 	void contextLoads() {
// 	}

// }

package com.placementportal.placement_website;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class PlacementWebsiteApplication {
    public static void main(String[] args) {
        SpringApplication.run(PlacementWebsiteApplication.class, args);
    }
}
