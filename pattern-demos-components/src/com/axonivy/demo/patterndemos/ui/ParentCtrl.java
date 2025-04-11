package com.axonivy.demo.patterndemos.ui;

import javax.faces.event.ActionEvent;

import com.axonivy.demo.patterndemos.entities.Person;
import com.axonivy.demo.patterndemos.ui.components.ChildCtrl;
import com.axonivy.demo.patterndemos.ui.holders.PersonHolder;

public class ParentCtrl implements PersonHolder {

	private ChildCtrl childCtrl;
	private Person person;

	public ParentCtrl() {
		init();
	}

	/**
	 * Method to initialize controllers.
	 * 
	 **/
	public void init() {
		person = new Person();
		person.setFirstName("");
		person.setLastName("");
		this.childCtrl = new ChildCtrl(this);
	}

	/**
	 * Save person.
	 * 
	 * Simulates saving data to the database which creates a new object.
	 * Note, that the child will automatically see this new object because
	 * we are the person holder.
	 *
	 * 
	 **/
	public void save(ActionEvent event) {
		person = save(person);
	}

	/**
	 * Simulate a typical save in a JPA environment, where the save will create a new object.
	 * 
	 * @param person
	 * @return
	 */
	protected Person save(Person person) {
		var savedPerson = new Person();
		savedPerson.setFirstName(person.getFirstName());
		savedPerson.setLastName(person.getLastName());
		return savedPerson;
	}

	public ChildCtrl getChildCtrl() {
		return childCtrl;
	}

	public void setChildCtrl(ChildCtrl childCtrl) {
		this.childCtrl = childCtrl;
	}

	@Override
	public Person getPerson() {
		return person;
	}

	@Override
	public void setPerson(Person person) {
		this.person = person;
	}
}
