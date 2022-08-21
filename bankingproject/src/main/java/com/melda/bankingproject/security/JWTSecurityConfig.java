package com.melda.bankingproject.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.melda.bankingproject.services.UserService;


@SuppressWarnings("deprecation")
@Configuration
@EnableWebSecurity
public class JWTSecurityConfig extends WebSecurityConfigurerAdapter{
	
	@Autowired
	private JwtRequestFilter jwtRequestFilter;
	
	@Autowired
	private UserService userService;
	
	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth
		.userDetailsService(this.userService)
		.passwordEncoder(passwordEncoder());
	}
	@Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
	
	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}
	@Override
	protected void configure(HttpSecurity httpSecurity) throws Exception {
		httpSecurity
		.cors()
        .and()
		.csrf()
		.disable()
		.authorizeHttpRequests()
		.antMatchers(HttpMethod.POST,"/auth")
		.permitAll()
		.antMatchers(HttpMethod.POST,"/register")
		.permitAll()
		.antMatchers(HttpMethod.POST,"/banks").hasAuthority("CREATE_BANK")
		.antMatchers(HttpMethod.POST,"/users/**").hasAuthority("ACTIVATE_DEACTIVATE_USER")
		.antMatchers(HttpMethod.DELETE,"/accounts/**").hasAuthority("REMOVE_ACCOUNT")
		.antMatchers(HttpMethod.POST,"/accounts/**").hasAuthority("CREATE_ACCOUNT")
		.anyRequest()
		.authenticated()
		.and().exceptionHandling()
		.accessDeniedHandler(new AccessDeniedHandler() {

			@Override
			public void handle(HttpServletRequest request, HttpServletResponse response,
					AccessDeniedException accessDeniedException) throws IOException, ServletException {
				// TODO Auto-generated method stub
				response.sendError(401);
			}
			
		}).and()
		.sessionManagement()
		.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		
		httpSecurity.addFilterBefore(jwtRequestFilter,UsernamePasswordAuthenticationFilter.class);
	}
}
