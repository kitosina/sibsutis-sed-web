package sibsutis.sed.sedsibsutis.app.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import sibsutis.sed.sedsibsutis.service.security.UserService;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * usersService used by daoAuthenticationProvider
     */
    private UserService usersService;

    /**
     * This method injects UserServiceImpl object
     * @see Autowired
     * @param usersService
     */
    @Autowired
    public void setUsersService(UserService usersService) {
        this.usersService = usersService;
    }

    /**
     * This method restricts user rights depending on the role
     * @see Override
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().authorizeRequests()
                .antMatchers("/home_page", "/incoming", "/refusal", "/send", "/sent", "/signed", "/contragent").hasAuthority("USER")
                .and()
                .formLogin()
                .loginPage("/login")
                .defaultSuccessUrl("/home_page")
                .permitAll()
                .and()
                .logout()
                .permitAll();
    }

    /**
     * ?????? ?????? ?????????????????? ???????????? BCrypt Encoder
     * @return PasswordEncoder
     */
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * This method setting up DaoAuthenticationProvider for security
     * @see Bean
     * @return encoder password object
     */
    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        daoAuthenticationProvider.setUserDetailsService(usersService);
        return daoAuthenticationProvider;
    }

}
