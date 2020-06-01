package com.skillbox.socialnetwork.main.config;

import com.skillbox.socialnetwork.main.security.UserDetailsServiceImpl;
import com.skillbox.socialnetwork.main.security.jwt.JwtConfigurer;
import com.skillbox.socialnetwork.main.security.jwt.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(jwtTokenProvider.passwordEncoder());
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
                        "/api/v1/auth/**",
                        "/api/v1/account/register/**",
                        "/api/v1/account/password/**",
                        "/api/v1/platform/languages",
                        "/api/v1/storage/*"
                )
                .permitAll()
                .antMatchers("/api/v1/**")
                .hasRole("USER")                //allow access to all other pages only for registered users
                .and()
                .apply(new JwtConfigurer(jwtTokenProvider));
    }
}
