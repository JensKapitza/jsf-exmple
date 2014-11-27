package spring.bean;

import java.util.List;
import java.util.stream.Collectors;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import jpa.bean.Greeting;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import spring.JPADao;

@Component("nameBean")
@Scope("view")
public class NameBean {
	private String inputname;

	@Autowired
	JPADao sessionFactory;

	public String list() {
		List<Greeting> greets = sessionFactory.list();

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

		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage("Welcome " + inputname + " !" + list()));
	}
}