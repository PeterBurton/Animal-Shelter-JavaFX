package application;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.imageio.ImageIO;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

public class Animal implements java.io.Serializable{

	
	private static final long serialVersionUID = -3669722281557818553L;
	
	
	private int ID;
	private int age;
	private String colour;
	private boolean gender;
	private String description;
	private String name;
	private transient Image picture;
	private String breed;
	private Category myCat;
	private String type;
	
	//-------------------------------------
	//Constructor method:
	//-------------------------------------	

	//empty constructor as an animal can have several different sets 
	//of data depending on whether it is lost found or adoption
	public Animal()
	{
	}
	
	//--------------------------------------	
	// Get methods:
	//--------------------------------------	
	
	public int getID()
    {
        return this.ID;
    }
	public int getAge()
    {
        return this.age;
    }
	public String getColour()
    {
        return this.colour;
    }
	public boolean getGender()
    {
        return this.gender;
    }
	public String getDescription()
    {
        return this.description;
    }
	public String getName()
    {
        return this.name;
    }
	public Image getPicture()
	{
		return this.picture;
	}
	public String getBreed()
    {
        return this.breed;
    }
	public Category getCat()
	{
		return this.myCat;
	}
	public String getType()
    {
        return this.type;
    }
	
	//--------------------------------------	
	// Set methods:
	//--------------------------------------
	
	public void setID(int ID)
	{
		this.ID=ID;
	}
	public void setAge(int age)
	{
		this.age=age;
	}
	public void setColour(String colour)
	{
		this.colour=colour;
	}
	public void setGender(boolean gender)
	{
		this.gender=gender;
	}
	public void setDescription(String description)
	{
		this.description=description;
	}
	public void setName(String name)
	{
		this.name=name;
	}
	public void setPicture(Image picture)
	{
		this.picture=picture;
	}
	public void setBreed(String breed)
	{
		this.breed=breed;
	}
	public void setCat(Category cat)
	{
		this.myCat=cat;
	}
	public void setType(String type)
	{
		this.type=type;
	}
	
	//because the image used is a javaFX image, I have to convert 
	//it before I serialize it, to and from a javaFX image
	//javaFX objects don't have great support for serialization
	private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        picture = SwingFXUtils.toFXImage(ImageIO.read(s), null);
    }

    private void writeObject(ObjectOutputStream s) throws IOException {
        s.defaultWriteObject();
        ImageIO.write(SwingFXUtils.fromFXImage(picture, null), "png", s);
    }
	
	//--------------------------------------	
	// toString and print methods:
	//--------------------------------------
	
	public String toString()
	{
		
		return "Animal{" +
        "ID=" + ID +
        ", age=" + age +
        ", type='" + type + '\'' +
        ", colour='" + colour + '\'' +
        ", gender=" + gender +
        ", description='" + description + '\'' +
        ", name='" + name + '\'' +
        ", picture=" + picture +
        ", breed='" + breed + '\'' +
        ", animalCat=" + myCat +
        '}';
	}
	public void print()
	{
		System.out.println(toString());
	}
}
