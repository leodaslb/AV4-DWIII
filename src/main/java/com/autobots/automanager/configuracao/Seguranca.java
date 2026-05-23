package com.autobots.automanager.configuracao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.autobots.automanager.adaptadores.UserDetailsServiceImpl;
import com.autobots.automanager.filtros.Autenticador;
import com.autobots.automanager.filtros.Autorizador;
import com.autobots.automanager.jwt.ProvedorJwt;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class Seguranca extends WebSecurityConfigurerAdapter {

	@Autowired
	private UserDetailsServiceImpl servico;

	@Autowired
	private ProvedorJwt provedorJwt;

	private static final String[] rotasPublicas = { "/", "/usuario/usuarios" };

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.cors().and().csrf().disable();

		http.authorizeHttpRequests().antMatchers(rotasPublicas).permitAll()
		//servico e mercadoria
		.antMatchers(HttpMethod.GET, "/servico/**", "/mercadoria/**")
        .hasAnyRole("ADMIN", "GERENTE", "VENDEDOR")
    .antMatchers("/servico/**", "/mercadoria/**")
        .hasAnyRole("ADMIN", "GERENTE")

    //venda
	.antMatchers(HttpMethod.GET,"/venda/**")
        .hasAnyRole("ADMIN", "GERENTE", "VENDEDOR", "CLIENTE")
	.antMatchers(HttpMethod.POST,"/venda/**")
        .hasAnyRole("ADMIN", "GERENTE", "VENDEDOR")
		
    .antMatchers("/venda/**")
        .hasAnyRole("ADMIN", "GERENTE")

    // Usuários
	.antMatchers(HttpMethod.GET,"/usuario/**")
        .hasAnyRole("ADMIN", "GERENTE", "VENDEDOR","CLIENTE")
    .antMatchers("/usuario/**")
        .hasAnyRole("ADMIN", "GERENTE", "VENDEDOR")

    // Tudo mais só ADMIN
    .anyRequest().hasRole("ADMIN");
		

		http.addFilter(new Autenticador(authenticationManager(), provedorJwt));
		http.addFilter(new Autorizador(authenticationManager(), provedorJwt, servico));

		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	}

	@Override
	protected void configure(AuthenticationManagerBuilder autenticador) throws Exception {
		autenticador.userDetailsService(servico).passwordEncoder(new BCryptPasswordEncoder());
	}

	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		UrlBasedCorsConfigurationSource fonte = new UrlBasedCorsConfigurationSource();
		fonte.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
		return fonte;
	}
}
