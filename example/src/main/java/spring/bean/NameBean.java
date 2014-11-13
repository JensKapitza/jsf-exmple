package spring.bean;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import jpa.bean.Greeting;

import org.hibernate.Criteria;
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

	@SuppressWarnings("unchecked")
	@Transactional
	public static String list(Session session) {
		Criteria q = session.createCriteria(Greeting.class);
		List<Greeting> greets = Collections.checkedList(q.list(), Greeting.class); 

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