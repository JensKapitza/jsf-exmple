package spring.bean;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import jpa.bean.Greeting;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component("nameBean")
@Scope("view")
@Transactional
public class NameBean {
	private String inputname;

	@Autowired
	SessionFactory sessionFactory;

	@Transactional
	public static String list(Session session) {
		List<Greeting> greets = new ArrayList<>();
		List<?> items = session.createCriteria(Greeting.class).list();
		items.forEach((Object o) -> greets.add((Greeting) o));

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