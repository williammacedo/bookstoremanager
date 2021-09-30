package com.williammacedo.bookstoremanager.config;

import com.williammacedo.bookstoremanager.config.filters.JwtRequestFilter;
import com.williammacedo.bookstoremanager.user.enums.Role;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@AllArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private static final String AUTH_API_URL = "/v1/auth";
    private static final String OPEN_USERS_API_URL = "/v1/users";
    private static final String AUTHENTICATED_USERS_API_URL = "/v1/users/**";
    private static final String USERS_BOOKS_API_URL = "/v1/users/books";
    private static final String PUBLISHERS_API_URL = "/v1/publishers/**";
    private static final String AUTHORS_API_URL = "/v1/authors/**";
    private static final String BOOKS_API_URL = "/v1/books/**";
    private static final String H2_CONSOLE_URL = "/h2-console/**";
    private static final String SWAGGER_URL = "/swagger-ui/index.html";
    private static final String ROLE_ADMIN = Role.ADMIN.getDescription();
    private static final String ROLE_USER = Role.USER.getDescription();

    private static final String[] SWAGGER_RESOURCES = {
            // -- swagger ui
            "/v2/api-docs",
            "/swagger-ui/**",
            "/swagger-resources/**",
            "/swagger-ui/index.html",
            "/webjars/**"
    };

    private JwtAuthenticationEntrypoint jwtAuthenticationEntrypoint;
    private UserDetailsService userDetailsService;
    private JwtRequestFilter jwtRequestFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration().applyPermitDefaultValues();
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and()
                .headers().frameOptions().disable()
                .and().csrf().disable()
                .authorizeRequests()
                .antMatchers(HttpMethod.OPTIONS).permitAll()
                .antMatchers(HttpMethod.HEAD).permitAll()
                .antMatchers(AUTH_API_URL).permitAll()
                .antMatchers(HttpMethod.GET, OPEN_USERS_API_URL).permitAll()
                .antMatchers(HttpMethod.POST, OPEN_USERS_API_URL).permitAll()
                .antMatchers(USERS_BOOKS_API_URL).authenticated()
                .antMatchers(AUTHENTICATED_USERS_API_URL).hasRole(ROLE_ADMIN)
                .antMatchers(H2_CONSOLE_URL, SWAGGER_URL).permitAll()
                .antMatchers(PUBLISHERS_API_URL, AUTHORS_API_URL).hasRole(ROLE_ADMIN)
                .antMatchers(BOOKS_API_URL).hasAnyRole(ROLE_ADMIN, ROLE_USER)
                .anyRequest().authenticated()
                .and().exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntrypoint)
                .and().sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers(SWAGGER_RESOURCES);
    }
}
