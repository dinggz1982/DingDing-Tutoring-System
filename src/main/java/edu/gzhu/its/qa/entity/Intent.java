package edu.gzhu.its.qa.entity;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;



/**
 * 意图
 * @author dingguozhu
 *
 */

@Entity(name="qa_intent")
public class Intent {
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private int id;
	
	private String text;
	
	private String intent;
	@OneToMany(fetch = FetchType.EAGER, cascade = { CascadeType.REFRESH})
	@JoinTable(name = "qa_intent_entity", joinColumns = { @JoinColumn(name = "intent_id") }, inverseJoinColumns = {
			@JoinColumn(name = "entity_id") })
	@OrderBy(value = "id DESC")
	private Set<edu.gzhu.its.qa.entity.Entity> entities;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getIntent() {
		return intent;
	}

	public void setIntent(String intent) {
		this.intent = intent;
	}

	public Set<edu.gzhu.its.qa.entity.Entity> getEntities() {
		return entities;
	}

	public void setEntities(Set<edu.gzhu.its.qa.entity.Entity> entities) {
		this.entities = entities;
	}
}
