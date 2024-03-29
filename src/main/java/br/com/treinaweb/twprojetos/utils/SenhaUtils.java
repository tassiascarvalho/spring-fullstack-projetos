package br.com.treinaweb.twprojetos.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class SenhaUtils {
    
    public static String encode(String senha){
        //criptografia de senha
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.encode(senha);
    }

    //M�todo para compara��o de senha
    public static boolean matches(String senha, String hash){
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        return encoder.matches(senha, hash);

    }
}
