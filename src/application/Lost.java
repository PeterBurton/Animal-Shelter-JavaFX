package application;

import java.time.LocalDate;

public class Lost extends Category {

	private static final long serialVersionUID = 2298927562385781791L;
	private String location;

	public Lost(LocalDate localDate, Person contact, String location) 
	{
		super(localDate, contact);
		this.setLocation(location);
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
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
