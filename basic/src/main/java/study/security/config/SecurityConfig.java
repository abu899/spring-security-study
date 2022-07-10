package study.security.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.security.config.annotation.authentication.configurers.provisioning.UserDetailsManagerConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.jdbc.JdbcDaoImpl;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
@Slf4j
public class SecurityConfig {

    @Bean
    public SecurityFilterChain httpSecurityFilter(HttpSecurity http) throws Exception {
        log.info("Security Filter chain");

        // 인증
        addUsernamePasswordAuthenticationFilter(http);
        addLogoutFilter(http);
        addSessionManagementFilter(http);

        // 인가
        http
                .authorizeRequests()
                .antMatchers("/user").hasRole("USER")
                .antMatchers("/admin/pay").hasRole("ADMIN")
                .antMatchers("/admin/**").access("hasRole('ADMIN') or hasRole('SYS')")
                .anyRequest().authenticated();

        return http.build();
    }

    @Bean
    public DataSource dataSource() {
        return new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .addScript(JdbcDaoImpl.DEFAULT_USER_SCHEMA_DDL_LOCATION)
                .build();
    }

    @Bean
    public UserDetailsManager users(DataSource dataSource) {
        UserDetails user = User.withDefaultPasswordEncoder()
                .username("user1")
                .password("1111")
                .roles("USER")
                .build();
        UserDetails user2 = User.withDefaultPasswordEncoder()
                .username("sys")
                .password("1111")
                .roles("SYS","USER")
                .build();
        UserDetails user3 = User.withDefaultPasswordEncoder()
                .username("admin")
                .password("1111")
                .roles("ADMIN","USER","SYS")
                .build();
        JdbcUserDetailsManager users = new JdbcUserDetailsManager(dataSource);
        users.createUser(user);
        users.createUser(user2);
        users.createUser(user3);
        return users;
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

    private void addSessionManagementFilter(HttpSecurity http) throws Exception {
        http
                .sessionManagement()
                .maximumSessions(1)
                .maxSessionsPreventsLogin(true);
    }
}
