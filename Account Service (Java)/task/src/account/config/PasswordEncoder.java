package account.config;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class PasswordEncoder {


   // private final static BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(13);
   @Bean
   @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
   public BCryptPasswordEncoder getEncoder() {
       return new BCryptPasswordEncoder(13);
   }


}
