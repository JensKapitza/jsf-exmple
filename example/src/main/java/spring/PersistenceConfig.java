package spring;

import java.util.Arrays;
import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.CustomScopeConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.http.MediaType;
import org.springframework.orm.jpa.JpaDialect;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.support.PersistenceAnnotationBeanPostProcessor;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaDialect;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.accept.ContentNegotiationManager;
import org.springframework.web.accept.ContentNegotiationManagerFactoryBean;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.view.ContentNegotiatingViewResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

@EnableWebMvc
@Configuration
@EnableTransactionManagement
@PropertySource({ "/WEB-INF/jdbc.properties" })
@ComponentScan({ "spring", "spring.bean", "rest" })
public class PersistenceConfig {

	@Bean
	public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
		return new PropertySourcesPlaceholderConfigurer();
	}

	@Bean
	@Autowired
	public JpaVendorAdapter jpaVendorAdapter(Environment env) {
		HibernateJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
		adapter.setDatabase(Database.valueOf(env.getProperty("jpa.database")));
		adapter.setShowSql(true);
		adapter.setGenerateDdl(true); // should be false!
		adapter.setDatabasePlatform(env.getProperty("hibernate.dialect"));
		return adapter;
	}

	@Bean()
	@Autowired
	public LocalContainerEntityManagerFactoryBean emf(DataSource dataSource,
			JpaVendorAdapter adapter,
			@Qualifier("hibernateProperties") Properties hibernateProperties) {
		LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
		emf.setPersistenceUnitName("jpapu");
		emf.setDataSource(dataSource);
		emf.setJpaVendorAdapter(adapter);
		emf.setPackagesToScan(new String[] { "jpa", "jpa.bean" });
		emf.setJpaProperties(hibernateProperties);
		return emf;
	}

	@Bean
	public JpaDialect jpaDialect() {
		return new HibernateJpaDialect();
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
	@Autowired
	public DataSource restDataSource(Environment env) {
		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setDriverClassName(env.getProperty("jdbc.driverClassName"));
		dataSource.setUrl(env.getProperty("jdbc.url"));
		dataSource.setUsername(env.getProperty("jdbc.username"));
		dataSource.setPassword(env.getProperty("jdbc.password"));

		return dataSource;
	}

	@Bean
	@Autowired
	public JpaTransactionManager transactionManager(EntityManagerFactory emf,
			JpaDialect dialect) {

		JpaTransactionManager txManager = new JpaTransactionManager();
		txManager.setEntityManagerFactory(emf);
		txManager.setJpaDialect(dialect);
		return txManager;
	}

	@Bean
	public static AutowiredAnnotationBeanPostProcessor autowiredAnnotationBeanPostProcessor() {
		return new AutowiredAnnotationBeanPostProcessor();
	}

	@Bean
	public static PersistenceAnnotationBeanPostProcessor persistenceAnnotationBeanPostProcessor() {
		return new PersistenceAnnotationBeanPostProcessor();
	}

	@Bean
	public static PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
		return new PersistenceExceptionTranslationPostProcessor();
	}

	@Bean()
	@Autowired
	public Properties hibernateProperties(Environment env) {
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
	public ContentNegotiationManagerFactoryBean contentNegotiationManagerFactory() {
		ContentNegotiationManagerFactoryBean fac = new ContentNegotiationManagerFactoryBean();
		fac.setDefaultContentType(MediaType.APPLICATION_JSON);
		fac.addMediaType("json", MediaType.APPLICATION_JSON);
		fac.addMediaType("xml", MediaType.APPLICATION_XML);
		return fac;
	}

	@Autowired
	@Bean
	public ContentNegotiatingViewResolver contentViewResolver(
			@Qualifier("contentNegotiationManagerFactory") ContentNegotiationManager contentNegotiationManager) {
		ContentNegotiatingViewResolver res = new ContentNegotiatingViewResolver();
		res.setContentNegotiationManager(contentNegotiationManager);
		res.setDefaultViews(Arrays.asList(new MappingJackson2JsonView()));
		return res;

	}

}