package sibsutis.sed.sedsibsutis.app;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.security.Security;

@SpringBootApplication
@PropertySource(value = "classpath:sed-web.properties", encoding = "UTF-8")
@ComponentScan(basePackages = {"sibsutis.sed.sedsibsutis"})
@EnableJpaRepositories(basePackages = "sibsutis.sed.sedsibsutis.repostiory")
@EntityScan(basePackages = "sibsutis.sed.sedsibsutis.model.entity")
public class SedSibsutisApplication {

	public static void main(String[] args) {
		Security.addProvider(new BouncyCastleProvider());
		SpringApplication.run(SedSibsutisApplication.class, args);
	}

}
