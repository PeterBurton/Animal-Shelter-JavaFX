package application;
//import application.Found;
//import application.Lost;

import java.time.LocalDate;

public abstract class Category implements java.io.Serializable{

	private static final long serialVersionUID = 4608802434628611136L;
	private LocalDate date;
	private Person contact;

	public Category(LocalDate localDate , Person contact)
	{
		this.date=localDate;
		this.contact=contact;
	}

	public Category(LocalDate localDate)
	{
		this.date = localDate;
	}

	public Category(){}

	//Method to get the location from lost/found object
	public String getLoc()
	{
		if (this.getClass().toString().equals("class application.Found")){
			return ((Found) this).getLocation();
		}
		else if(this.getClass().toString().equals("class application.Lost")){
			return ((Lost) this).getLocation();
		}
		else{
			return "-";
		}

	}
	
	//Method to get the adoption status from adoption object
	public String getStat()
	{
		if (this.getClass().toString().equals("class application.Adoption")){
			return ((Adoption) this).getStatus();
		}
		else{
			return "-";
		}
	}
	public LocalDate getDate()
	{
		return this.date;
	}
	public Person getContact()
	{
		return this.contact;
	}
	public void setDate(LocalDate date)
	{
		this.date=date;
	}
	public void setContact(Person contact)
	{
		this.contact=contact;
	}
	public String toString()
	{
		String info = this.date + " " + this.contact + " " + this.getClass();
		return info;
	}
	public void print()
	{
		System.out.println(toString());
	}

}
