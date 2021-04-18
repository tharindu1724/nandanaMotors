package lk.nandana_motors.configuration;

import lk.nandana_motors.asset.user_management.service.UserDetailsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.session.HttpSessionEventPublisher;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  private final String[] ALL_PERMIT_URL = {"/favicon.ico", "/img/**", "/css/**", "/js/**", "/webjars/**",
      "/login", "/select/**", "/", "/index"};

  @Bean
  public UserDetailsServiceImpl userDetailsService() {
    return new UserDetailsServiceImpl();
  }

  @Bean
  public BCryptPasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public DaoAuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
    authProvider.setUserDetailsService(userDetailsService());
    authProvider.setPasswordEncoder(passwordEncoder());
    return authProvider;
  }

  /*Session management - bean start*/
  @Bean
  public HttpSessionEventPublisher httpSessionEventPublisher() {
    return new HttpSessionEventPublisher();
  }

  @Bean
  public SessionRegistry sessionRegistry() {
    return new SessionRegistryImpl();
  }
  /*Session management - bean end*/

  @Bean
  public AuthenticationSuccessHandler customAuthenticationSuccessHandler() {
    return new CustomAuthenticationSuccessHandler();
  }

  @Bean
  public LogoutSuccessHandler customLogoutSuccessHandler() {
    return new CustomLogoutSuccessHandler();
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) {
    auth.authenticationProvider(authenticationProvider());
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
/*    http.csrf().disable();
    http.authorizeRequests().antMatchers("/").permitAll();*/
    // For developing easy to give permission all lin

    http
        .authorizeRequests(
            authorizeRequests ->
                authorizeRequests
                    //Anytime users can access without login
                    //to see actuator details
                    .antMatchers(ALL_PERMIT_URL).permitAll()
                    //Need to login for access those are
                    .antMatchers("/customer/**").hasAnyRole("IN_OFFICER", "MANAGER")
                    .antMatchers("/discountRatio/**").hasAnyRole("CASHIER", "MANAGER")
                    .antMatchers("/employee/**").hasAnyRole("ADMIN", "MANAGER")
                    .antMatchers("/inspection/**").hasAnyRole("IN_OFFICER", "MANAGER")
                    .antMatchers("/payment/**").hasAnyRole("CASHIER", "MANAGER")
                    .antMatchers("/report/**").hasAnyRole("ADMIN", "IN_OFFICER", "MANAGER", "TECH", "CASHIER")
                    .antMatchers("/role/**").hasAnyRole("ADMIN", "MANAGER")
                    .antMatchers("/serviceType/**").hasAnyRole("ADMIN", "MANAGER","TECH")
                    .antMatchers("/serviceTypeParameter/**").hasAnyRole("ADMIN", "MANAGER","TECH")
                    .antMatchers("/user/**").hasAnyRole("ADMIN", "MANAGER")
                    .antMatchers("/vehicle/**").hasAnyRole("ADMIN", "MANAGER", "IN_OFFICER")
                    .antMatchers("/inspection/**").hasAnyRole("ADMIN", "MANAGER", "IN_OFFICER")
                    .antMatchers("/vehicleServiceStationProcess/**").hasAnyRole("ADMIN", "MANAGER", "TECH","CASHIER","IN_OFFICER")
                    .anyRequest()
                    .authenticated())
        // Login form
        .formLogin(
            formLogin ->
                formLogin
                    .loginPage("/login")
                    .loginProcessingUrl("/login")
                    //Username and password for validation
                    .usernameParameter("username")
                    .passwordParameter("password")
                    .successHandler(customAuthenticationSuccessHandler())
                    .failureUrl("/login?error")
                  )
        //Logout controlling
        .logout(
            logout ->
                logout
                    .logoutUrl("/logout")
                    .logoutSuccessHandler(customLogoutSuccessHandler())
                    .deleteCookies("JSESSIONID")
                    .invalidateHttpSession(true)
                    .clearAuthentication(true))
        //session management
        .sessionManagement(
            sessionManagement ->
                sessionManagement
                    .sessionFixation().migrateSession()
                    .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                    .invalidSessionUrl("/login")
                    .maximumSessions(5)
                    .expiredUrl("/logout")
                    .sessionRegistry(sessionRegistry()))
        //Cross site disable
        .csrf(AbstractHttpConfigurer::disable)
        .exceptionHandling();
  }
}

