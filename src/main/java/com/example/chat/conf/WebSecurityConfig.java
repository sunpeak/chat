package com.example.chat.conf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.provisioning.InMemoryUserDetailsManagerConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by gg on 2018/1/24.
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${custom.user}")
    private String users;

    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }

    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }

    @Bean
    public AuthenticationSuccessHandler getAuthenticationSuccessHandler() {
        return new SavedRequestAwareAuthenticationSuccessHandler() {

            @Autowired
            private SessionManager sessionManager;

            @Override
            public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
                this.setDefaultTargetUrl("/chat");
                this.setAlwaysUseDefaultTargetUrl(false);
                super.onAuthenticationSuccess(request, response, authentication);
                sessionManager.sendUsers(authentication);
            }
        };
    }

    @Component
    public class SessionManager {

        @Autowired
        private SimpMessagingTemplate simpMessagingTemplate;

        @Autowired
        private SessionRegistry sessionRegistry;

        public void sendUsers(Authentication authentication) {
            if (authentication != null && authentication.isAuthenticated()) {
                Set<String> userSet = sessionRegistry.getAllPrincipals().stream().map(user -> ((User) user).getUsername()).collect(Collectors.toSet());

                userSet.forEach(user -> {
                    if (authentication.getName().equals(user)) {
                        return;
                    }
                    Set<String> friend = userSet.stream().filter(u -> !u.equals(user)).collect(Collectors.toSet());
                    simpMessagingTemplate.convertAndSendToUser(user, "/queue/user", String.join(",", friend));
                });
            }
        }
    }

    @Bean
    public LogoutSuccessHandler getLogoutSuccessHandler() {
        return new LogoutSuccessHandler() {


            @Autowired
            private SessionManager sessionManager;

            @Override
            public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
                sessionManager.sendUsers(authentication);
            }
        };
    }

    @Bean
    public LogoutHandler getLogoutHandler() {
        return new LogoutHandler() {

            @Autowired
            private SessionManager sessionManager;

            @Override
            public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
                sessionManager.sendUsers(authentication);
            }
        };
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/", "/login").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin().successHandler(getAuthenticationSuccessHandler())
                .permitAll()
                .and()
                .logout().logoutUrl("/logout").invalidateHttpSession(true).logoutSuccessHandler(getLogoutSuccessHandler())
                .permitAll()
                .and()
                .csrf().disable()
                .sessionManagement().sessionFixation().changeSessionId().maximumSessions(1).maxSessionsPreventsLogin(true).sessionRegistry(sessionRegistry());
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        InMemoryUserDetailsManagerConfigurer<AuthenticationManagerBuilder> authenticationManagerBuilderInMemoryUserDetailsManagerConfigurer = auth.inMemoryAuthentication();
        Arrays.stream(users.split(",")).forEach(up -> {
            String user = up.split(":")[0];
            String password = up.split(":")[1];
            authenticationManagerBuilderInMemoryUserDetailsManagerConfigurer.withUser(user).password(password).roles("USER")
                    .and();
        });
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/static/**");
    }
}
