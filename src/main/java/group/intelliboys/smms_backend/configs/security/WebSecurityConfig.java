package group.intelliboys.smms_backend.configs.security;

import group.intelliboys.smms_backend.configs.security.jwt.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
    @Autowired
    private UserDetailsServiceImpl userDetailsService;
    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                // ============================ LOGIN URL'S ============================
                .authorizeHttpRequests(request -> request
                        .requestMatchers(HttpMethod.POST, "/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/login/2fa/verify").permitAll()
                        .requestMatchers(HttpMethod.GET, "/login/2fa/resend/email-otp/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/login/2fa/resend/sms-otp/**").permitAll()
                        // =====================================================================

                        // ============================ REGISTRATION URL'S ============================
                        .requestMatchers(HttpMethod.POST, "/register/is-account-exists").permitAll()
                        .requestMatchers(HttpMethod.POST, "/register/submit").permitAll()
                        .requestMatchers(HttpMethod.POST, "/register/verify").permitAll()
                        .requestMatchers(HttpMethod.GET, "/register/resend/email-otp/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/register/resend/sms-otp/**").permitAll()
                        // ============================================================================

                        // ============================ FORGOT PASSWORD URL'S ============================
                        .requestMatchers(HttpMethod.GET, "/forgot-password/search-account/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/forgot-password/resend/email-otp/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/forgot-password/resend/sms-otp/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/forgot-password/verify-otp").permitAll()
                        .requestMatchers(HttpMethod.POST, "/forgot-password/submit").permitAll()
                        .requestMatchers(HttpMethod.POST, "/push/notification/accident").permitAll()
                        .requestMatchers(HttpMethod.GET, "/push/notification/test").permitAll()
                        .anyRequest()
                        .authenticated())
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .userDetailsService(userDetailsService)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
}
