package com.radioaudit.configuration;

import java.util.Properties;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableScheduling
@EnableTransactionManagement
@ComponentScan({ "com.radioaudit.domain", "com.radioaudit.service", "com.radioaudit.thread" })
public class MainContextConfiguration {

	@Bean
	public TaskScheduler taskScheduler() {
		ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
		scheduler.setPoolSize(20);
		return scheduler;
	}

	@Bean(name = "datasource")
	public DriverManagerDataSource dataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName("com.mysql.jdbc.Driver");
		dataSource.setUrl("jdbc:mysql://localhost:3306/radioaudit"); // env.getProperty("jdbc.url")
		dataSource.setUsername("radioaudit_app"); // env.getProperty("jdbc.user")
		dataSource.setPassword("cielo01"); // env.getProperty("jdbc.pass")
		return dataSource;
	}

	@Bean(name = "sessionFactory")
	public LocalSessionFactoryBean sessionFactory() {
		LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
		sessionFactory.setDataSource(this.dataSource());
		sessionFactory.setPackagesToScan(new String[] { "com.radioaudit.domain.model", "com.radioaudit.domain.dao" });

		Properties hibernateProperties = new Properties();
		hibernateProperties.put("hibernate.connection.driver_class", "com.mysql.jdbc.Driver");
		hibernateProperties.put(" hibernate.c3p0.min_size", "5");
		hibernateProperties.put(" hibernate.c3p0.max_size", "20");
		hibernateProperties.put("hibernate.c3p0.timeout", "1800");
		hibernateProperties.put("hibernate.c3p0.max_statements", "50");

		hibernateProperties.put("hibernate.show_sql", "false");
		hibernateProperties.put("hibernate.format_sql", "true");
		hibernateProperties.put("hibernate.use_sql_comments", "true");
		hibernateProperties.put("hibernate.dialect", "org.hibernate.dialect.MySQL5InnoDBDialect");
		hibernateProperties.put("hibernate.connection.autocommit", "true");
		hibernateProperties.put("hibernate.bytecode.use_reflection_optimizer", "true");
		sessionFactory.setHibernateProperties(hibernateProperties);
		return sessionFactory;
	}

	@Bean
	@Autowired
	public HibernateTransactionManager transactionManager(SessionFactory sessionFactory) {
		HibernateTransactionManager txManager = new HibernateTransactionManager();
		txManager.setSessionFactory(sessionFactory);
		return txManager;
	}

}
