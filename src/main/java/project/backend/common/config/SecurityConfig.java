package project.backend.common.config;

import static org.springframework.security.config.Customizer.withDefaults;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer.FrameOptionsConfig;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import project.backend.common.auth.oauth.KakaoUserDetailsService;
import project.backend.common.auth.jwt.JwtAccessDeniedHandler;
import project.backend.common.auth.jwt.JwtAuthenticationFailEntryPoint;
import project.backend.common.auth.jwt.JwtFilter;
import project.backend.common.error.ExceptionHandlerFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFailEntryPoint jwtAuthenticationFailEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final JwtFilter jwtFilter;
    private final KakaoUserDetailsService kakaoUserDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .cors(withDefaults())
                .headers(headers -> headers.frameOptions(FrameOptionsConfig::disable))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .oauth2Login(oauth -> {
                    oauth.userInfoEndpoint(config -> config.userService(kakaoUserDetailsService));
                })
                .authorizeHttpRequests(request -> request
                        .requestMatchers("/h2-console/**").permitAll()
                        .requestMatchers("/v1/auth/**").permitAll()
                        .requestMatchers("/v1/exception/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/posts").permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new ExceptionHandlerFilter(), JwtFilter.class) // JwtFilter 에서 CustomException 사용하기 위해 추가
                .exceptionHandling(exceptionHandling -> {
                    exceptionHandling.authenticationEntryPoint(jwtAuthenticationFailEntryPoint);
                    exceptionHandling.accessDeniedHandler(jwtAccessDeniedHandler);
                });

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
