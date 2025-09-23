import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class PlacementPortalApplication {

    public static void main(String[] args) {
        SpringApplication.run(PlacementPortalApplication.class, args);
    }

    @Bean
    CommandLineRunner testConnection(JdbcTemplate jdbcTemplate) {
        return args -> {
            Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM student_details", Integer.class);
            System.out.println("Number of students in DB: " + count);
        };
    }
}
