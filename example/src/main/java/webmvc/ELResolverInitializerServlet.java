package webmvc;

import javax.faces.context.FacesContext;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.springframework.web.jsf.el.SpringBeanFacesELResolver;

public final class ELResolverInitializerServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public void init(ServletConfig config) throws ServletException {
		FacesContext context = FacesContext.getCurrentInstance();
		context.getApplication().addELResolver(new SpringBeanFacesELResolver());
	}

}