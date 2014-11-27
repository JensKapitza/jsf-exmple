package rest;

import java.util.List;
import java.util.stream.Collectors;

import jpa.bean.Greeting;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import spring.JPADao;

@org.springframework.web.bind.annotation.RestController
public class RestController {

	@Autowired
	JPADao sessionFactory;

	@RequestMapping(value = "/list.action", produces = "application/json")
	public String list() {
		List<Greeting> greets = sessionFactory.list();
		return greets.stream().map(g -> g.getId() + ": " + g.getContent())
				.collect(Collectors.joining(", "));

	}

	@RequestMapping(value = "/greeting.action", produces = "application/json")
	public Greeting greeting(
			@RequestParam(value = "name", defaultValue = "World") String name) {
		return sessionFactory.greeting(name);
	}
}
