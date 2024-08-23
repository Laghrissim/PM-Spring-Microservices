package ma.appsegov.authservice;

import ma.appsegov.authservice.model.Account;
import ma.appsegov.authservice.model.Role;
import ma.appsegov.authservice.model.User;
import ma.appsegov.authservice.service.AccountService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class AuthServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthServiceApplication.class, args);
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    //	@Bean
    CommandLineRunner start(AccountService accountService) {
        return args -> {
            accountService.addUser(User.builder().username("Zakaria Achour").email("zakariaAchoor@example.com").password("123456").account(Account.builder().activation_date(LocalDate.now()).isActive(true).build()).build());

            accountService.addUser(User.builder().username("Mohamed Laghrissi").email("mohamedLaghrissi@example.com").password("123456").account(Account.builder().activation_date(LocalDate.now()).isActive(true).build()).build());

            accountService.addUser(User.builder().username("Moad").email("Moad@gmail.com").password("123456").account(Account.builder().activation_date(LocalDate.now()).isActive(true).build()).build());

            accountService.addRole(Role.builder().nom("PMO").build());
            accountService.addRole(Role.builder().nom("ADMIN").build());
            accountService.addRole(Role.builder().nom("PROJECT_MANAGER").build());

            accountService.addRoleToUser("Zakaria Achour", "PMO");
            accountService.addRoleToUser("Mohamed Laghrissi", "PMO");
            accountService.addRoleToUser("Moad", "ADMIN");
        };
    }

}
