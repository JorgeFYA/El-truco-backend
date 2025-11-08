package com.marketminds.ecommerce.elTruco.config;

import com.marketminds.ecommerce.elTruco.repositories.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final UsersRepository usersRepository;

    @Bean
    UserDetailsService userDetailsService() {
        return username -> usersRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    // Verifica las credenciales del usuario
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    //
    @Bean
    // 3. INYECTAMOS EL FILTRO AQUÍ, COMO PARÁMETRO DEL METODO
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtRequestFilter jwtAuthFilter) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // Públicos
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll() // preflight CORS
                        .requestMatchers("/images/**").permitAll()
                        .requestMatchers("/api/auth/**").permitAll()

                        // Productos
                        .requestMatchers(HttpMethod.GET, "/api/products/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/products/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/products/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/products/**").hasRole("ADMIN")

                        // Recetas
                        .requestMatchers(HttpMethod.GET, "/api/recipes/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/recipes/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/recipes/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/recipes/**").hasRole("ADMIN")

                        // Newsletter
                        .requestMatchers(HttpMethod.GET, "/api/newsletter/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/newsletter/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/newsletter/**").permitAll()

                        .requestMatchers("/api/users/**").hasRole("ADMIN") // Protegido
                        .anyRequest().authenticated() // El resto, protegido
                )
                .authenticationProvider(authenticationProvider())
                // 4. Usamos el filtro que Spring inyectó al metodo
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    //para evitar cors
    @Bean
    public WebMvcConfigurer corsConfigurer(){
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("*")
                        .allowedMethods("GET", "POST", "DELETE", "PUT", "OPTIONS")
                        .allowedHeaders("*");
            }
        };
    }
}