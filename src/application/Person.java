package application;

public class Person implements java.io.Serializable{

	private static final long serialVersionUID = 7137244175886211718L;
	private String name;
	private String address;
	private String phone;
	private String email;
	private int animalID;
	private boolean interestedAdoption;
	
	public Person(String name, String address, String phone, String email)
	{
		this.setName(name);
		this.setAddress(address);
		this.setPhone(phone);
		this.setEmail(email);
	}
	public Person(){}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public int getAnimalID() {
		return animalID;
	}

	public void setAnimalID(int animalID) {
		this.animalID = animalID;
	}
	
	//I added these methods as this seemed the best way to register that the person was interested in 
	//adoption. Other than that I would have had to add another list...
	public boolean getInterestedAdoption() {
		return interestedAdoption;
	}
	public void setInterestedAdoption(boolean interestedAdoption) {
		this.interestedAdoption = interestedAdoption;
	}
	
	public String toString()
	{
		String info = this.name + " " + this.address + " " + this.phone + " " + this.email;
		return info;
	}
	
	public void print()
	{
		System.out.println(toString());
	}
}
