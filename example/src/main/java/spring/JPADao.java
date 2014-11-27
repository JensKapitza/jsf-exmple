package spring;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import jpa.bean.Greeting;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository("jpa")
@Transactional
public class JPADao {

	private static final String template = "Hello, %s!";
	private final AtomicLong counter = new AtomicLong();

	@PersistenceContext
	EntityManager em;

	public List<Greeting> list() {
		List<Greeting> greets = new ArrayList<>();
		List<?> items = em.createQuery("FROM Greeting")
				.getResultList();
		items.forEach((Object o) -> greets.add((Greeting) o));
		return greets;

	}

	public Greeting greeting(String name) {
		Greeting gr = new Greeting(counter.incrementAndGet(), String.format(
				template, name));
		System.out.println(name + " ---> " + gr.getId());
		em.persist(gr);
		return gr;
	}

}
