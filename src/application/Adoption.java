package application;

import java.time.LocalDate;

public class Adoption extends Category implements java.io.Serializable{
	
	private static final long serialVersionUID = 8762978795694638193L;
	private boolean neutered;
	private boolean chipped;
	private boolean vaccinated;
	private String status;
	private boolean reserved;

	public Adoption(LocalDate date, boolean neutered, boolean chipped, boolean vaccinated, String status, boolean reserved) 
	{
		super(date);
		this.neutered=neutered;
		this.chipped=chipped;
		this.vaccinated=vaccinated;
		this.status=status;
		this.reserved=reserved;
	}
	
	public Adoption(){}
	
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
	public boolean getReserved() {
		return reserved;
	}
	public void setReserved(boolean reserved) {
		this.reserved = reserved;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public boolean getVaccinated() {
		return vaccinated;
	}
	public void setVaccinated(boolean vaccinated) {
		this.vaccinated = vaccinated;
	}
	public boolean getChipped() {
		return chipped;
	}
	public void setChipped(boolean chipped) {
		this.chipped = chipped;
	}
	public boolean getNeutered() {
		return neutered;
	}
	public void setNeutered(boolean neutered) {
		this.neutered = neutered;
	}

}
