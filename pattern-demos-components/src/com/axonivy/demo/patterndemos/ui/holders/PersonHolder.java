package com.axonivy.demo.patterndemos.ui.holders;

import com.axonivy.demo.patterndemos.entities.Person;

/**
 * Interface to implement if you are the holder of a {@link Person} object.
 * 
 * Components working with a {@link Person} require a {@link PersonHolder} as parameter. 
 */
public interface PersonHolder {
	public void setPerson(Person person);
	public Person getPerson();
}
