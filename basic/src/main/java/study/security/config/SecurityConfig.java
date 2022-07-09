package study.security.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@Slf4j
public class SecurityConfig {

    @Bean
    public SecurityFilterChain httpSecurityFilter(HttpSecurity http) throws Exception {
        log.info("Security Filter chain");
        http
                .authorizeRequests()
                .anyRequest().authenticated();

        addUsernamePasswordAuthenticationFilter(http);
        addLogoutFilter(http);

        return http.build();
    }

    private void addUsernamePasswordAuthenticationFilter(HttpSecurity http) throws Exception {
        http
                .formLogin()
//                .loginPage("/loginPage")
                .defaultSuccessUrl("/")
                .failureUrl("/login")
                .usernameParameter("userId")
                .passwordParameter("passwd")
                .loginProcessingUrl("/login_proc")
                .successHandler(
                        (request, response, authentication) -> {
                            System.out.println(" Authentication = " + authentication.getName());
                            response.sendRedirect("/");
                        })
                .failureHandler(
                        (request, response, exception) -> {
                            System.out.println("Authentication fail" + exception.getMessage());
                            response.sendRedirect("/login");
                        }
                );
    }

    private void addLogoutFilter(HttpSecurity http) throws Exception {
        // Logout Filter (default : POST 방식)
        http
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login")
                .addLogoutHandler((request, response, authentication) -> {
                    System.out.println("Call Logout Handler => " + authentication.getName());
                })
                .logoutSuccessHandler((request, response, authentication) -> {
                    System.out.println("Call Logout Success Handler");
                    response.sendRedirect("/login");
                })
                .deleteCookies("remember-me");
    }

    private void addRememberMeFilter(HttpSecurity http) throws Exception {
        http.
                rememberMe()
                .rememberMeParameter("remember")
                .tokenValiditySeconds(3600)
                .alwaysRemember(true);
    }
}
