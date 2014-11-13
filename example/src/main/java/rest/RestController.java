package rest;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@org.springframework.web.bind.annotation.RestController()
@EnableTransactionManagement
@Transactional
public class RestController {

	private static final String template = "Hello, %s!";
	private final AtomicLong counter = new AtomicLong();

	@PersistenceContext
	private EntityManager entityManager;

	@RequestMapping(value = "/list", produces = "application/json")
	public String list() {
		List<Greeting> greets = new ArrayList<>();
		entityManager.createQuery("SELECT g FROM Greeting g").getResultList()
				.forEach(o -> greets.add((Greeting) o));

		return greets.stream().map(g -> g.getId() + ": " + g.getContent())
				.collect(Collectors.joining(", "));

	}

	@Transactional
	@RequestMapping(value = "/greeting", produces = "application/json")
	public Greeting greeting(
			@RequestParam(value = "name", defaultValue = "World") String name) {
		System.out.println("called!!!!!!----" + name);
		Greeting gr = new Greeting(counter.incrementAndGet(), String.format(
				template, name));
		entityManager.persist(gr);
		entityManager.flush();
		return gr;
	}
}
