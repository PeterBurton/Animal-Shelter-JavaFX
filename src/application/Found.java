package application;

import java.time.LocalDate;

public class Found extends Category {

	private static final long serialVersionUID = 8850169237521850790L;
	private String location;

	public Found(LocalDate date, Person contact, String location) {
		super(date, contact);
		this.setLocation(location);
	}
	
	public Found() {}
	
	public String getLocation() 
	{
		return location;
	}
	public void setLocation(String location) 
	{
		this.location = location;
	}
	public LocalDate getDate()
	{
		return super.getDate();
	}
	public void setDate(LocalDate date)
	{
		super.setDate(date);
	}
	public Person getContact()
	{
		return super.getContact();
	}
	public void setContact(Person contact)
	{
		super.setContact(contact);
	}
	public String toString()
	{
		String info = super.toString() + " " + this.location;
		return info;
	}

}
