package demo;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;

import org.apache.commons.dbcp2.BasicDataSource;

@ManagedBean(name = "pprBean")
public class NameBean {
	private String inputname;
	@ManagedProperty("#{dataSource}") 
	private BasicDataSource dataSource;

	public void setDataSource(BasicDataSource dataSource) {
		this.dataSource = dataSource;
	}

	public BasicDataSource getDataSource() {
		return dataSource;
	}

	public String getInputname() {
		return inputname;
	}

	public void setInputname(String inpName) {
		this.inputname = inpName;
		System.out.println("NAME: " + inpName);
		dataSource.setUsername(inputname);
	}

	public void saveName() {
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage("Welcome " + inputname + " !"));
	}
}