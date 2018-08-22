/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Bean;

import java.util.Properties;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableJpaRepositories(basePackages = "Repository")
//khai bao day la noi cau hinh
@Configuration
//kich hoat transaction tuong duong annotation-driven 
@EnableTransactionManagement
//scan packe chua cau hinh
@ComponentScan({"Bean"})
//import file luu cau hinh
@PropertySource("classpath:SpringConfig/jdbc-mysql.properties")
public class HibernateConfiguration {

    //
    @Autowired
    private Environment environment;

    //set up entityfactory
    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean entitymanager = new LocalContainerEntityManagerFactoryBean();
        entitymanager.setDataSource((javax.sql.DataSource) dataSource);
        entitymanager.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        entitymanager.setPackagesToScan("Entity");

        //jpa
        Properties p = new Properties();
        p.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
        p.setProperty("hibernate.hbm2ddl.auto", "update");
        p.setProperty("hibernate.id.new_generator_mappings", "false");
        entitymanager.setJpaProperties(p);
        return entitymanager;
    }

    //cau hinh ket noi
    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();

        dataSource.setDriverClassName(environment.getRequiredProperty("jdbc.driverClassName"));

        dataSource.setUrl(environment.getRequiredProperty("jdbc.url"));

        dataSource.setUsername(environment.getRequiredProperty("jdbc.user"));

        dataSource.setPassword(environment.getRequiredProperty("jdbc.pass"));
        return dataSource;
    }

    //set up transaction
    @Bean
    public JpaTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        JpaTransactionManager jpatran = new JpaTransactionManager();
        jpatran.setEntityManagerFactory(entityManagerFactory);
        return jpatran;
    }
}
