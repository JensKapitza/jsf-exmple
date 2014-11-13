package rest;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import jpa.bean.Greeting;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@org.springframework.web.bind.annotation.RestController
public class RestController {

	private static final String template = "Hello, %s!";
	private final AtomicLong counter = new AtomicLong();

	@Autowired
	SessionFactory sessionFactory;

	protected Session getCurrentSession() {
		return sessionFactory.getCurrentSession();
	}

	@Transactional
	@RequestMapping(value = "/list.action", produces = "application/json")
	public String list() {
		List<Greeting> greets = new ArrayList<>();
		List<?> items = getCurrentSession().createCriteria(Greeting.class).list();
		items.forEach((Object o) -> greets.add((Greeting) o));
		return greets.stream().map(g -> g.getId() + ": " + g.getContent())
				.collect(Collectors.joining(", "));

	}

	@Transactional
	@RequestMapping(value = "/greeting.action", produces = "application/json")
	public Greeting greeting(
			@RequestParam(value = "name", defaultValue = "World") String name) {
		Greeting gr = new Greeting(counter.incrementAndGet(), String.format(
				template, name));
		getCurrentSession().save(gr);
		return gr;
	}
}
