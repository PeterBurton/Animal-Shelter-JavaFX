package application;

import java.util.ArrayList;

public class AnimalList implements java.io.Serializable{

	
	private static final long serialVersionUID = -7835300593658721751L;
	private ArrayList<Animal> animalList;
	
	
	public AnimalList()
	{
		animalList = new ArrayList<Animal>();
	}
	public boolean add(Animal animal)
	{
		if(animalList.add(animal))
			return true;
		else
			return false;
	}
	public void removeByPos(int i)
	{
		animalList.remove(i);
	}
	
	public boolean remove(Animal animal)
	{
		int pos = find(animal);
		if (pos != -1)
		{
			animalList.remove(pos);
			return true;
		}
		return false;
	}
	public int find(Animal animal)
	{
		int pos = animalList.indexOf(animal);
		return pos;
	}
	public int searchByID(int ID)
	{
		int i = -1;	

		int count = animalList.size();
		for (int j = 0 ; j < count ; j++)
		{
			if (animalList.get(j).getID() == ID)
			{
				return j;
			}
		}
		return i;
	}
	public Animal get(int i)
	{
		return animalList.get(i);
	}
	
	public int getSize()
	{
		return animalList.size();
	}
	
	public void printList()
	{
		for (int i = 0 ; i < animalList.size(); i++)
		{
			System.out.println(animalList.get(i).toString());
		}
	}
}
