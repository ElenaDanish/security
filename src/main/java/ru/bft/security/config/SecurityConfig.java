package ru.bft.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import ru.bft.security.entrypoint.AuthEntryPointJwt;
import ru.bft.security.filters.AuthTokenFilter;
import ru.bft.security.filters.AuthoritiesLoggingAfterFilter;
import ru.bft.security.utils.JwtUtils;

@Configuration
@ComponentScan({"ru.bft.security.entrypoint",
        "ru.bft.security.filters"})
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private AuthEntryPointJwt authEntryPointJwt;

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().  //не создает HttpSession
                //при истечении времени действия токена вернется 401 ошибка
                exceptionHandling().authenticationEntryPoint(authEntryPointJwt).and().
                //чтобы послылать запросы в Postman
                headers().frameOptions().sameOrigin().and().
                csrf().disable().
                //фильтр для аутентификации с помощью токена
                addFilterBefore(new AuthTokenFilter(), UsernamePasswordAuthenticationFilter.class).
                //фильтр для вывода информации о пользователе
                addFilterAfter(new AuthoritiesLoggingAfterFilter(), BasicAuthenticationFilter.class).
                authorizeRequests().
                //ограничения по ролям
                antMatchers("/myInfo").access("hasRole('ROLE_ADMIN')").
                antMatchers("/contact").access("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')").
                antMatchers("/signIn").permitAll().
                and().formLogin().and().httpBasic();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtUtils jwtUtils() {
        return new JwtUtils();
    }

}
