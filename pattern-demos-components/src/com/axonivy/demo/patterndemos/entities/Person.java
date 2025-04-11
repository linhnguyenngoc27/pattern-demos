package com.axonivy.demo.patterndemos.entities;

public class Person {

	private String firstName;
	private String lastName;

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	@Override
	public String toString() {
		return "%s %s (instance: %x)".formatted(firstName, lastName, System.identityHashCode(this));
	}
}
