package at.ac.fhsalzburg.swd.spring.config;

import com.zaxxer.hikari.HikariDataSource;

import at.ac.fhsalzburg.swd.spring.model.Customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.Properties;

import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "at.ac.fhsalzburg.swd.spring",
        entityManagerFactoryRef = "dataEntityManagerFactory",
        transactionManagerRef= "dataTransactionManager"
)
public class myappDataSourceConfiguration {

	@Autowired
	private Environment env;


    @Bean
    @ConfigurationProperties("myapp.datasource")
    @Primary
    public DataSourceProperties dataDataSourceProperties() {
        return new DataSourceProperties();
    }


    @Bean
    @ConfigurationProperties("myapp.datasource")
    @Primary
    public DataSource dataDataSource() {
        return dataDataSourceProperties().initializeDataSourceBuilder()
                .type(HikariDataSource.class).build();
    }


    @Bean(name = "dataEntityManagerFactory")
    @Primary
    public LocalContainerEntityManagerFactoryBean dataEntityManagerFactory(EntityManagerFactoryBuilder builder) {
    	Properties prop = new Properties();
    	prop.setProperty("hibernate.hbm2ddl.auto", env.getProperty("spring.jpa.hibernate.ddl-auto"));
        LocalContainerEntityManagerFactoryBean bean =builder
                .dataSource(dataDataSource())
                .packages(Customer.class)
                .build();
        bean.setJpaProperties(prop);
        return bean;
    }

    @Bean(name = "dataTransactionManager")
    @Primary
    public PlatformTransactionManager dataTransactionManager(
            final @Qualifier("dataEntityManagerFactory") LocalContainerEntityManagerFactoryBean memberEntityManagerFactory) {
        return new JpaTransactionManager(memberEntityManagerFactory.getObject());
    }

}

