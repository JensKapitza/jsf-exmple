package demo;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

@ManagedBean(name = "pprBean")
public class NameBean {
	private String inputname;

	public String getInputname() {
		return inputname;
	}

	public void setInputname(String inpName) {
		this.inputname = inpName;
		System.out.println("NAME: " + inpName);
	}

	public void saveName() {
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage("Welcome " + inputname + " !"));
	}
}