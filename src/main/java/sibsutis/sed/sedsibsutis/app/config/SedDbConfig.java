package sibsutis.sed.sedsibsutis.app.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
public class SedDbConfig {

    /**
     * Получение DataSource в базе sed
     *
     * @return DataSource -  A factory for connections to the physical data source
     * that this DataSource object represents.
     */
    @Primary
    @Bean(name = "sedDataSource")
    @ConfigurationProperties(prefix = "sed.datasource")
    public DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }

    /**
     * Получение LocalContainerEntityManagerFactoryBean в базе billing
     *
     * @param builder - builder for JPA EntityManagerFactory instances
     * @param dataSource - factory for connections to the physical data source
     *
     * @return LocalContainerEntityManagerFactoryBean -  FactoryBean that creates
     * a JPA EntityManagerFactory according to JPA's standard container
     * bootstrap contract.
     */
    @Primary
    @Bean(name = "entityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
            final EntityManagerFactoryBuilder builder,
            @Qualifier("sedDataSource") final DataSource dataSource) {
        return builder
                .dataSource(dataSource)
                .packages("sibsutis.sed.sedsibsutis.model.entity")
                .build();
    }

    /**
     * Получение transaction manager-а в базе sed
     *
     * @param entityManagerFactory - FactoryBean that creates a JPA EntityManagerFactory
     *
     * @return JpaTransactionManager - Binds a JPA EntityManager from the
     * specified factory to the thread, potentially allowing for one
     * thread-bound EntityManager per factory.
     */
    @Primary
    @Bean(name = "transactionManager")
    public PlatformTransactionManager transactionManager(
           @Qualifier("entityManagerFactory") final EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }

}
