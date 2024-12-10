package at.ac.fhsalzburg.swd.spring.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class SecurityConfiguration {

    @Autowired
    private TokenService tokenService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().and().cors().and()
            .addFilterBefore(new JwtAuthenticationFilter(tokenService),
                    UsernamePasswordAuthenticationFilter.class)
            .authorizeRequests()
                .antMatchers("/api/**") 
                    .hasAnyRole("USER", "ADMIN") // alles unter /api benötigt entweder Benutzer- oder Admin-Rolle
                .antMatchers("/admin/**")
                    .hasAnyRole("ADMIN") // alles unter /admin benötigt Admin-Rolle
                .antMatchers("/user/**")
                    .hasAnyRole("USER", "ADMIN") // alles unter /user benötigt entweder Benutzer- oder Admin-Rolle
                .antMatchers("/returnMedia") 
                    .authenticated() // alles unter /returnMedia benötigt eine Authentifizierung
                .anyRequest().permitAll() // andere Seiten sind für alle zugänglich
            .and().csrf().disable().formLogin() 
                .loginPage("/login") // Die Login-Seite
                .defaultSuccessUrl("/", true) // Weiterleitung nach erfolgreichem Login
                .failureUrl("/login-error")
            .and().csrf().disable().logout()     
                .logoutSuccessUrl("/") // Weiterleitung nach Logout
                .invalidateHttpSession(true) // Logout führt zur Invalidierung der Session
                .deleteCookies("JSESSIONID"); // Löscht das Session-Cookie

        http.headers().frameOptions().disable(); // h2-console würde sonst nicht funktionieren

        return http.build();      
    }
}
