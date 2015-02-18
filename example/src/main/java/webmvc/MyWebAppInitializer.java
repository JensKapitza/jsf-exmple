package webmvc;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration.Dynamic;

import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import spring.PersistenceConfig;

//import com.sun.faces.config.ConfigureListener;

public class MyWebAppInitializer extends
		AbstractAnnotationConfigDispatcherServletInitializer {

	@Override
	public void onStartup(ServletContext servletContext)
			throws ServletException {
		super.onStartup(servletContext);
		Dynamic faces = servletContext.addServlet("Faces Servlet",
				"javax.faces.webapp.FacesServlet");
		faces.addMapping("*.html");
		//ConfigureListener listener = new ConfigureListener();
		//servletContext.addListener(listener);
		servletContext.addListener(new RequestContextListener());
		Dynamic elResolverInitializer = servletContext.addServlet(
				"elResolverInit", new ELResolverInitializerServlet());
		elResolverInitializer.setLoadOnStartup(2);
	}

	@Override
	protected Class<?>[] getRootConfigClasses() {
		return new Class[] { PersistenceConfig.class };
	}

	@Override
	protected Class<?>[] getServletConfigClasses() {
		return new Class[0];
	}

	@Override
	protected String[] getServletMappings() {
		return new String[] { "/", "/rest/*", "*.action" };
	}

}
