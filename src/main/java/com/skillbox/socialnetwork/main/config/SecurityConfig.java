package com.skillbox.socialnetwork.main.config;

import com.skillbox.socialnetwork.main.security.jwt.JwtConfigurer;
import com.skillbox.socialnetwork.main.security.jwt.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    public SecurityConfig() {
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Configuration
    @Order(1)
    public static class ApiWebSecurityConfig extends WebSecurityConfigurerAdapter{

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http
                    .antMatcher("/api/v1/auth/logout")
                    .httpBasic().disable()
                    .csrf().disable()
                    .cors()
                    .and()
                    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    .and()
                    .authorizeRequests()
                    .antMatchers("/api/v1/auth/logout").permitAll();
        }
    }

    @Configuration
    @Order(2)
    public static class ApiTokenSecurityConfig extends WebSecurityConfigurerAdapter{
        private final JwtTokenProvider jwtTokenProvider;
        private static final String LOGIN_ENDPOINT = "/api/v1/auth/login";

        @Autowired
        public ApiTokenSecurityConfig(JwtTokenProvider jwtTokenProvider) {
            this.jwtTokenProvider = jwtTokenProvider;
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http
                    .httpBasic().disable()
                    .csrf().disable()
                    .cors()
                    .and()
                    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    .and()
                    .authorizeRequests()
                    .antMatchers(
                            "/api/v1/auth/*",
                            "/api/v1/account/register",
                            "/api/v1/account/password/recovery",
                            "/api/v1/account/password/set",
                            "/api/v1/platform/languages",
                            "/api/v1/storage/*"
                    ).permitAll()
                    .antMatchers("/api/v1/**").hasRole("USER")
                    .and()
                    .apply(new JwtConfigurer(jwtTokenProvider));
        }

    }
}
