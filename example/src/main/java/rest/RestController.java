package rest;

import java.util.concurrent.atomic.AtomicLong;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@org.springframework.web.bind.annotation.RestController()
@ComponentScan
public class RestController {

	private static final String template = "Hello, %s!";
	private final AtomicLong counter = new AtomicLong();

	@Autowired
	private BasicDataSource dataSource;

	public void setDataSource(BasicDataSource dataSource) {
		this.dataSource = dataSource;
	}

	public BasicDataSource getDataSource() {
		return dataSource;
	}

	@RequestMapping(value = "/list", produces = "application/json")
	public String list() {
		return dataSource.getUsername();

	}

	@RequestMapping(value = "/greeting", produces = "application/json")
	public Greeting greeting(
			@RequestParam(value = "name", defaultValue = "World") String name) {
		System.out.println("called!!!!!!----" + name);
		return new Greeting(counter.incrementAndGet(), String.format(template,
				name));
	}
}
