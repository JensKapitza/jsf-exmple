package database;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.CustomScopeConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.view.ContentNegotiatingViewResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

@EnableWebMvc
@Configuration
@EnableTransactionManagement
@PropertySource({ "/WEB-INF/jdbc.properties" })
@ComponentScan({ "rest", "demo" })
public class PersistenceConfig {

	@Autowired
	private Environment env;

	@Bean()
	public LocalSessionFactoryBean sessionFactory() {
		LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
		sessionFactory.setDataSource(restDataSource());
		sessionFactory.setPackagesToScan(new String[] { "rest" });
		sessionFactory.setHibernateProperties(hibernateProperties());

		return sessionFactory;
	}

	@Bean
	// must be static
	// http://stackoverflow.com/questions/14942304/springs-javaconfig-and-customscopeconfigurer-issue
	public static CustomScopeConfigurer customScope() {
		CustomScopeConfigurer cus = new CustomScopeConfigurer();
		cus.addScope("view", new ViewScope());
		return cus;
	}

	@Bean
	public DataSource restDataSource() {
		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setDriverClassName(env.getProperty("jdbc.driverClassName"));
		dataSource.setUrl(env.getProperty("jdbc.url"));
		dataSource.setUsername(env.getProperty("jdbc.username"));
		dataSource.setPassword(env.getProperty("jdbc.password"));

		return dataSource;
	}

	@Bean
	@Autowired
	public HibernateTransactionManager transactionManager(
			SessionFactory sessionFactory) {
		HibernateTransactionManager txManager = new HibernateTransactionManager();
		txManager.setSessionFactory(sessionFactory);

		return txManager;
	}

	@Bean
	public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
		return new PersistenceExceptionTranslationPostProcessor();
	}

	Properties hibernateProperties() {
		return new Properties() {
			private static final long serialVersionUID = -1552867857638585834L;

			{
				setProperty("hibernate.hbm2ddl.auto",
						env.getProperty("hibernate.hbm2ddl.auto"));
				setProperty("hibernate.dialect",
						env.getProperty("hibernate.dialect"));
				setProperty("hibernate.globally_quoted_identifiers", "true");
				setProperty("hibernate.show_sql", "true");
				setProperty("hibernate.format_sql", "true");

				setProperty("cache.provider_class",
						env.getProperty("hibernate.cache.provider_class"));
			}
		};
	}

	@Bean
	public InternalResourceViewResolver viewResolver() {
		InternalResourceViewResolver res = new InternalResourceViewResolver();
		res.setPrefix("/");
		res.setSuffix("*.xhtml");
		return res;

	}

	@Bean
	public ContentNegotiatingViewResolver contentViewResolver() {

		ContentNegotiatingViewResolver res = new ContentNegotiatingViewResolver();

		Map<String, String> map = new HashMap<>();
		map.put("json", "application/json");
		map.put("xml", "application/xml");

		res.setMediaTypes(map);
		res.setDefaultViews(Arrays.asList(new MappingJackson2JsonView()));
		return res;

	}

}