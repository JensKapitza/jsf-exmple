package jpa.bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "\"test_entities\"")
public class Greeting {

	public Greeting() {
	}

	@Id
	@Column(name = "\"id\"", unique = true, nullable = false)
	private long id;

	@Column(name = "\"text\"")
	private String content;

	public Greeting(long id, String content) {
		this.id = id;
		this.content = content;
	}

	public long getId() {
		return id;
	}

	public String getContent() {
		return content;
	}
}
