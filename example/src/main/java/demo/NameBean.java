package demo;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import rest.Greeting;

@Component
@Scope("view")
@Transactional
public class NameBean {
	private String inputname;

	@Autowired
	SessionFactory sessionFactory;
	
	@Transactional
	public static String list(Session session) {
		List<Greeting> greets = new ArrayList<>();
		session.createQuery("SELECT g FROM Greeting g").list()
				.forEach(o -> greets.add((Greeting) o));

		return greets.stream().map(g -> g.getId() + ": " + g.getContent())
				.collect(Collectors.joining(", "));

	}

	public String getInputname() {
		return inputname;
	}

	public void setInputname(String inpName) {
		this.inputname = inpName;
		System.out.println("NAME: " + inpName);
	}

	public void saveName() {

		FacesContext.getCurrentInstance().addMessage(
				null,
				new FacesMessage("Welcome " + inputname + " !"
						+ list(sessionFactory.getCurrentSession())));
	}
}