package br.com.treinaweb.twprojetos.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import br.com.treinaweb.twprojetos.enums.Perfil;
import br.com.treinaweb.twprojetos.servicos.UserDetailsServiceImpl;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter{

    @Autowired
    private UserDetailsServiceImpl userDetailsServiceImpl;

    //Se comentar todo esse c¢digo desabilita a seguran‡a da aplica‡Æo.

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // Configura as rotas publicas e privadas da aplica‡Æo
        http.authorizeRequests()
            //antMatchers libera rotas
            .antMatchers("/adminlte/**").permitAll()    //obrigatoriamente antes do anyRequest
            .antMatchers("/img/**").permitAll()    //obrigatoriamente antes do anyRequest
            .antMatchers("/js/**").permitAll()    //obrigatoriamente antes do anyRequest
            .antMatchers("/plugins/**").permitAll()    //obrigatoriamente antes do anyRequest
            //Dessa forma abaixo s¢ acessar  as rotas se for do tipo Gerente
            //Controle de Rotas
            .antMatchers("/**/cadastrar").hasAuthority(Perfil.ADMIN.toString())
            .antMatchers("/**/editar").hasAuthority(Perfil.ADMIN.toString())
            .antMatchers("/**/excluir").hasAuthority(Perfil.ADMIN.toString())
            //anyRequest qualquer requisi‡Æo tem que estar autenticada
            .anyRequest().authenticated();

        http.formLogin()
            .loginPage("/login")
            .defaultSuccessUrl("/clientes")
            //Passando o parametro do username fazendo liga‡Æo com a tela de login
            .usernameParameter("email")
            .permitAll();
        //Para aceitar requisi‡Æo de logout via link que ‚ GET
        http.logout()
            .logoutRequestMatcher(
                new AntPathRequestMatcher("/logout", "GET")
            )
            .logoutSuccessUrl("/login");

        http.rememberMe()
            //.tokenValiditySeconds(tokenValiditySeconds) - M‚todo para manter o usuario logado por um determinado tempo      
            .key("chaverememverMe");//Em produ‡Æo essa chave deve ser complexa pois vai para o cache
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //Informa a implementa‡Æo e o algortimo de hash para o login
        auth.userDetailsService(userDetailsServiceImpl)
            .passwordEncoder(new BCryptPasswordEncoder());
    }
}
    

