package application;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;

public class AnimalShelter{
	private static int count = 0;
	private AnimalList animals;
	private ArrayList<Person> possibleSponsors;
	private ArrayList<String> types;

	public boolean save()
	{
		//save the count variable which supplies the ID's for animals
		try{
			PrintWriter pw = new PrintWriter ("count.txt");
			pw.println(count);
			pw.close();
		}catch(Exception e){
			e.printStackTrace();
		}

		//Serialize the AnimalList and the possibleSponsors Arraylist
		ObjectOutputStream oos = null;
		FileOutputStream fout = null;
		ObjectOutputStream sponsOS = null;
		FileOutputStream sponsOut = null;
		try{
			fout = new FileOutputStream("animals.ser" , true);
			oos = new ObjectOutputStream(fout);
			oos.writeObject(animals);
			sponsOut = new FileOutputStream("sponsors.ser" , true);
			sponsOS = new ObjectOutputStream(sponsOut);
			sponsOS.writeObject(possibleSponsors);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				oos.close();
				fout.close();
				sponsOS.close();
				sponsOut.close();

			} catch (IOException e) {
				e.printStackTrace();
				return false; 

			}
		}

	}

	@SuppressWarnings({ "unchecked" })
	public void load()
	{
		animals = new AnimalList();
		possibleSponsors = new ArrayList<Person>();
		types = new ArrayList<String>();
		
		// Load the count from files to give animal ID's
		try{
			File file = new File("count.txt");
			Scanner sc = new Scanner(file);
			count = sc.nextInt();
			sc.close();
		}catch(Exception e){
			e.printStackTrace();
		}

		// Load the file with current types and try to add all contents to types ArrayList
		try{
			File file = new File("types.txt");
			Scanner sc = new Scanner(file);
			while (sc.hasNext()){
				types.add(sc.next());
			}
			sc.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		
		//Load AnimalList from serialized file
		ObjectInputStream animalsInputStream = null;

		try {
			FileInputStream animalsStreamIn = new FileInputStream("animals.ser");
			while (true){
				animalsInputStream = new ObjectInputStream(animalsStreamIn);
				animals = (AnimalList) animalsInputStream.readObject();
			}
		} catch (ClassNotFoundException | IOException ignored) {
			// as expected
		} finally{
			if (animalsInputStream != null)
				try {
					animalsInputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}

		//Load sponsors list from serialized file.
		ObjectInputStream sponsorsInputStream = null;

		try{
			FileInputStream sponsorsStreamIn = new FileInputStream("sponsors.ser");
			while(true){
				sponsorsInputStream = new ObjectInputStream(sponsorsStreamIn);
				possibleSponsors =  (ArrayList<Person>) sponsorsInputStream.readObject();
			}

		}catch( ClassNotFoundException | IOException ignored){
			// as expected
		}finally{
			try {
				if (sponsorsInputStream != null)
					sponsorsInputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}




	}

	public boolean addAnimal(Animal animal) {
		animal.setID(count); //add the ID to the animal
		count++;//increment the count for next ID
		return animals.add(animal);//return result of operation
	}

	public boolean removeAnimal(Animal animal) {
		//return result of remove operation
		return animals.remove(animal);

	}

	public Animal findAnimal(int i)
	{
		//find position of animal in list using passed in int (animals ID)
		int pos = animals.searchByID(i);
		//return animal
		return animals.get(pos);

	}

	public boolean addSponsor(Animal animal) {
		//This method is used when removing a lost animal
		//the sponsor is found from the animals information and added to the sponsors list
		Category category = animal.getCat();
		Person person = category.getContact();
		return possibleSponsors.add(person);

	}

	public boolean addSponsorFound(Person person)
	{
		//This method is used when adding a sponsor through the found,adoption, and interested parties screens
		if(possibleSponsors.add(person)){
			return true;
		}
		else
			return false;
	}

	public void displayForTransfer(TableView<Animal> table)
	{
		ArrayList<Animal> temp = new ArrayList<Animal>();
		int i = animals.getSize();

		for(int j = 0 ; j < i ; j++)
		{
			//Make sure the animal is found
			if (animals.get(j).getCat().getClass().toString().equals("class application.Found"))
			{
				//initialize a date 30 days ago from now
				LocalDate d = LocalDate.now().minusDays(30);
				//if animals date is before 30 days ago, add the animal to temp list
				if (animals.get(j).getCat().getDate().isBefore(d))
					temp.add(animals.get(j)); 
			}
		}
		//convert temp to observable list
		ObservableList <Animal>a = FXCollections.observableArrayList(temp);
		//Pass list and table to tableview factory method for output to table
		tableViewFactory(table,a);
	}

	public void displayAllLost(TableView<Animal> table)
	{

		ArrayList<Animal> templ = new ArrayList<Animal>();
		int i = animals.getSize();

		for(int j = 0 ; j < i ; j++)
		{
			//make sure animal is lost
			if (animals.get(j).getCat().getClass().toString().equals("class application.Lost"))
			{
				//if so add animal to temp
				templ.add(animals.get(j)); 
			}
		}
		//convert temp to observable list
		ObservableList <Animal>a = FXCollections.observableArrayList(templ);
		//Pass list and table to tableview factory method for output to table
		tableViewFactory(table,a);

	}

	public void displayAllFound(TableView<Animal> table)
	{
		ArrayList<Animal> tempf = new ArrayList<Animal>();
		int i = animals.getSize();

		for(int j = 0 ; j < i ; j++)
		{
			//make sure animal is of category found
			if (animals.get(j).getCat().getClass().toString().equals("class application.Found"))
				tempf.add(animals.get(j)); 
		}
		//convert temp to observable list
		ObservableList<Animal> b = FXCollections.observableArrayList(tempf);
		//Pass list and table to tableview factory method for output to table
		tableViewFactory(table,b);
	}

	@SuppressWarnings("unchecked")
	public void displayAllSponsors(TableView<Person> tableS)
	{
		//Convert sponsors list to observable list
		ObservableList<Person> p = FXCollections.observableArrayList(possibleSponsors);

		/*Setup table columns with the type of object they are getting information from 
		 * and the data type of that information. Also set width and the attribute in the 
		 * object that the table will be getting the data from*/
		TableColumn <Person, String>nameCol = new TableColumn<Person, String>("Name");
		nameCol.setPrefWidth(150);
		nameCol.setCellValueFactory(
				new PropertyValueFactory<Person, String>("name"));

		TableColumn <Person, String>addCol = new TableColumn<Person, String>("Address");
		addCol.setPrefWidth(150);
		addCol.setCellValueFactory(
				new PropertyValueFactory<Person, String>("address"));

		TableColumn <Person, String>phoneCol = new TableColumn<Person, String>("Phone Number");
		phoneCol.setPrefWidth(150);
		phoneCol.setCellValueFactory(
				new PropertyValueFactory<Person, String>("phone"));

		TableColumn <Person, String>emailCol = new TableColumn<Person, String>("Email");
		emailCol.setPrefWidth(150);
		emailCol.setCellValueFactory(
				new PropertyValueFactory<Person, String>("email"));

		/*table.setItems will go through the list and add the rows for all the columns 
		 * named above for every item in the list*/
		tableS.setItems(p);
		//make sure the table can only contain one set of columns
		tableS.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		//add all columns to the table
		tableS.getColumns().addAll(nameCol, addCol, phoneCol, emailCol);
	}

	public void displayAllByAge(TableView<Animal> table) {
		ArrayList<Animal> temp = new ArrayList<Animal>();
		int i = animals.getSize();

		//Add all animals to the temp arraylist
		for(int j = 0 ; j < i ; j++)
		{
			temp.add(animals.get(j));
		}
		//Instead of a separate comparator class Java 8 can use lambdas and inner class to make the code needed much less.
		Collections.sort(temp, Comparator.comparingInt(h -> h.getAge()));

		//convert temp to observable list
		ObservableList <Animal>a = FXCollections.observableArrayList(temp);
		//Pass list and table to tableview factory method for output to table
		tableViewFactory(table,a);

	}

	public boolean transferAnimal(Animal animal, String name, Image img, Category newCat) {

		//Try and change category of animal, give it a picture and a name if one is not already present
		try{
			animal.setCat(newCat);
			animal.setPicture(img);
			animal.setName(name);
			return true;
		}catch (Exception e){
			return false;
		}
	}

	public void displayAllAdoption(TableView<Animal> table) {
		ArrayList<Animal> tempf = new ArrayList<Animal>();
		int i = animals.getSize();

		for(int j = 0 ; j < i ; j++)
		{
			//make sure animal is for adoption
			if (animals.get(j).getCat().getClass().toString().equals("class application.Adoption"))
				//if so add to list
				tempf.add(animals.get(j)); 
		}
		//convert temp to observable list
		ObservableList<Animal> obList = FXCollections.observableArrayList(tempf);
		//pass list and table for tableViewFactory to display 
		tableViewFactory(table,obList);

	}

	public void print(String report, TableView<Animal> table) {

		//start a string  for the headings
		report += "------------------------------------------------------------------------------------------------------------------------------------------------------------------\n";
		report += "ID\tName\tAge\tType\tColour\tGender\tDescription\tBreed\n";
		report += "------------------------------------------------------------------------------------------------------------------------------------------------------------------\n";

		if(table.getItems().isEmpty()){
			AlertBox.display("Print", "No information in table, cannot print");
		}
		else{
		@SuppressWarnings("unchecked")
		//get the column with the ID's of all the animals, (column 0)
		TableColumn<Animal,Integer> column = (TableColumn<Animal, Integer>) table.getColumns().get(0) ; 

		//Make a list to hold all the ID numbers
		List<Integer> columnData = new ArrayList<>();
		
		//add all the ID's in a for loop that pulls the ID's from the column
		for (Animal item : table.getItems()) {
			columnData.add(column.getCellObservableValue(item).getValue());
		}
		
		//for every iteration, get the animal using ID and then add info to the report string
		for (int i = 0 ; i < columnData.size(); i++){
			int j = columnData.get(i);
			Animal a = findAnimal(j);
			report += "\n" + a.getID() +
					"\t" + a.getName() + 
					"\t" + a.getAge() +
					"\t" + a.getType() + 
					"\t" + a.getColour() + 
					"\t" + a.getGender() +
					"\t" + a.getDescription() + 
					"\t" + a.getBreed();
		}
		//print report
		PrintingTask.print(report);
		}

	}

	public void printSponsors(String report){

		report += "-------------------------------------------------------------------------------------------------\n";
		report += "Name\tAddress\tPhone Number\tEmail\n";
		report += "-------------------------------------------------------------------------------------------------\n";
		
		//add sponsor information from possibleSponsors using for loop
		for (int i = 0 ; i < possibleSponsors.size() ; i++){
			Person p = possibleSponsors.get(i);
			report += "\n" + p.getName() + 
					"\t" + p.getAddress() + 
					"\t" + p.getPhone() + 
					"\t" + p.getEmail();
		}
		// send to print
		PrintingTask.print(report);
	}

	public void displayAllLostLocation(TableView<Animal> table, String location) {

		ArrayList<Animal> temp = new ArrayList<Animal>();
		int i = animals.getSize();
		for(int j = 0 ; j < i ; j++)
		{
			//Make sure animal is lost
			if (animals.get(j).getCat().getClass().toString().equals("class application.Lost")){
				//check if animals location matches user input location
				if (animals.get(j).getCat().getLoc().toLowerCase().equals(location)){
					//if so add animal to list
					temp.add(animals.get(j));
				}
			}

		}
		//convert to observable list
		ObservableList<Animal> obList = FXCollections.observableArrayList(temp);
		//Pass list and table to tableview factory method for output to table
		tableViewFactory(table,obList);

	}

	@SuppressWarnings("unchecked")
	public TableView<Animal> tableViewFactory(TableView<Animal> table, ObservableList<Animal> obList)
	{
		//Clear anything previously in the table
		table.getColumns().clear();
		
		/*Setup table columns with the type of object they are getting information from 
		 * and the data type of that information. Also set width and the attribute in the 
		 * object that the table will be getting the data from*/
		TableColumn <Animal, Integer>IDCol = new TableColumn<Animal, Integer>("ID");
		IDCol.setPrefWidth(150);
		IDCol.setCellValueFactory(
				new PropertyValueFactory<Animal, Integer>("ID"));

		TableColumn <Animal, String>nameColF = new TableColumn<Animal, String>("Name");
		nameColF.setPrefWidth(150);
		nameColF.setCellValueFactory(
				new PropertyValueFactory<Animal, String>("name"));

		TableColumn <Animal, String>typeColF = new TableColumn<Animal, String>("Type");
		typeColF.setPrefWidth(150);
		typeColF.setCellValueFactory(
				new PropertyValueFactory<Animal, String>("type"));

		TableColumn <Animal, String>breedColF = new TableColumn<Animal, String>("Breed");
		breedColF.setPrefWidth(150);
		breedColF.setCellValueFactory(
				new PropertyValueFactory<Animal, String>("breed"));

		TableColumn <Animal, String>colourColF = new TableColumn<Animal, String>("Colour");
		colourColF.setPrefWidth(150);
		colourColF.setCellValueFactory(
				new PropertyValueFactory<Animal, String>("colour"));

		TableColumn <Animal, String>descColF = new TableColumn<Animal, String>("Description");
		descColF.setPrefWidth(150);
		descColF.setCellValueFactory(
				new PropertyValueFactory<Animal, String>("description"));

		TableColumn <Animal, Integer>ageCol = new TableColumn<Animal, Integer>("Age");
		ageCol.setPrefWidth(150);
		ageCol.setCellValueFactory(
				new PropertyValueFactory<Animal, Integer>("age"));

		TableColumn <Animal, Boolean>genderCol = new TableColumn<Animal, Boolean>("Gender:\nMale=true\nFemale=false");
		genderCol.setPrefWidth(150);
		genderCol.setCellValueFactory(
				new PropertyValueFactory<Animal, Boolean>("gender"));

		/*table.setItems will go through the list and add the rows for all the columns 
		 * named above for every item in the list*/
		table.setItems(obList);
		//make sure the table can only contain one set of columns
		table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		//add all columns to the table
		table.getColumns().addAll( IDCol, nameColF, typeColF, breedColF, colourColF, descColF, ageCol, genderCol);
		//return table to calling method
		return table;

	}

	public void displayCatsLostLocationWithDate(TableView<Animal> table, String location, LocalDate date) {

		ArrayList<Animal> temp = new ArrayList<Animal>();
		int i = animals.getSize();
		for(int j = 0 ; j < i ; j++)
		{
			//make sure animal is lost
			if (animals.get(j).getCat().getClass().toString().equals("class application.Lost")){
				//make sure type is cat
				if (animals.get(j).getType().equals("Cat")){
					//make sure animal location matches user input location
					if (animals.get(j).getCat().getLoc().toLowerCase().equals(location)){
						//make sure animals date matches user input date
						if(animals.get(j).getCat().getDate().equals(date)){
							//if so add animal to temp
							temp.add(animals.get(j));
						}
					}
				}
			}
		}


		//convert to observable list
		ObservableList<Animal> obList = FXCollections.observableArrayList(temp);
		//Pass list and table to tableview factory method for output to table
		tableViewFactory(table,obList);

	}

	public void displayAllFoundLocation(TableView<Animal> table, String locInputByUser) {
		
		ArrayList<Animal> temp = new ArrayList<Animal>();
		int i = animals.getSize();
		
		for(int j = 0 ; j < i ; j++)
		{
			//make sure animal is found
			if (animals.get(j).getCat().getClass().toString().equals("class application.Found")){
				//make sure location matches that inputted by user
				if (animals.get(j).getCat().getLoc().toLowerCase().equals(locInputByUser)){
					//if so add to list
					temp.add(animals.get(j));
				}
			}

		}
		//convert temp to observable list
		ObservableList<Animal> obList = FXCollections.observableArrayList(temp);
		//Pass list and table to tableview factory method for output to table
		tableViewFactory(table,obList);

	}

	public void displayFoundDateGender(TableView<Animal> table, LocalDate date1, LocalDate date2) {
		ArrayList<Animal> temp = new ArrayList<Animal>();
		int i = animals.getSize();
		for(int j = 0 ; j < i ; j++)
		{
			//make sure animal is found
			if (animals.get(j).getCat().getClass().toString().equals("class application.Found")){
				//make sure animals date is after the first user input date
				if (animals.get(j).getCat().getDate().isAfter(date1)){
					//make sure animals date is before 2nd user input date
					if (animals.get(j).getCat().getDate().isBefore(date2)){
						//if so add animal to temp
						temp.add(animals.get(j));
					}
				}
			}

		}
		//use comparator to sort by gender using lambdas and inner class
		Collections.sort(temp, Comparator.comparing(h -> h.getGender()));
		//convert to observable list
		ObservableList<Animal> obList = FXCollections.observableArrayList(temp);
		//Pass list and table to tableview factory method for output to table
		tableViewFactory(table,obList);
	}

	public void displayFoundLocationDate(TableView<Animal> table, String locInputByUser, LocalDate date, LocalDate date2) {

		ArrayList<Animal> temp = new ArrayList<Animal>();
		int i = animals.getSize();
		for(int j = 0 ; j < i ; j++)
		{
			//make sure animal is found
			if (animals.get(j).getCat().getClass().toString().equals("class application.Found")){
				//make sure animal location equals that inputted by user
				if (animals.get(j).getCat().getLoc().toLowerCase().equals(locInputByUser)){
					//make sure animals date is after 1st date input by user
					if(animals.get(j).getCat().getDate().isAfter(date)){
						//make sure animals date is before 2nd date input by user
						if(animals.get(j).getCat().getDate().isBefore(date2)){
							//if so add animal to temp
							temp.add(animals.get(j));
						}
					}
				}
			}
		}


		//convert to observable list 
		ObservableList<Animal> obList = FXCollections.observableArrayList(temp);
		//Pass list and table to tableview factory method for output to table
		tableViewFactory(table,obList);
	}

	public void displayAdoptionAllReadyByName(TableView<Animal> table) {

		ArrayList<Animal> temp = new ArrayList<Animal>();
		int i = animals.getSize();

		for(int j = 0 ; j < i ; j++)
		{
			//make sure animal is for adoption
			if (animals.get(j).getCat().getClass().toString().equals("class application.Adoption")){
				//make sure adoption status is ready
				if(animals.get(j).getCat().getStat().equals("Ready")){
					//add animal to list
					temp.add(animals.get(j)); 
				}
			}

		}
		//use comparator to sort by name using lambdas and inner class
		Collections.sort(temp, Comparator.comparing(h -> h.getName()));
		//convert temp to observable list
		ObservableList<Animal> b = FXCollections.observableArrayList(temp);
		//Pass list and table to tableview factory method for output to table
		tableViewFactory(table,b);
	}

	public void displayAllCatsReadyByAge(TableView<Animal> table) {
		ArrayList<Animal> temp = new ArrayList<Animal>();
		int i = animals.getSize();

		for(int j = 0 ; j < i ; j++)
		{
			//make sure animal is for adoption
			if (animals.get(j).getCat().getClass().toString().equals("class application.Adoption")){
				//make sure animal status is ready
				if(animals.get(j).getCat().getStat().equals("Ready")){
					//make sure its a cat
					if(animals.get(j).getType().equals("Cat")){
						//if so add animal
						temp.add(animals.get(j)); 
					}
				}
			}

		}
		//use comparator to sort by age using lambdas and inner class
		Collections.sort(temp, Comparator.comparingInt(h -> h.getAge()));
		//convert to observable list
		ObservableList<Animal> b = FXCollections.observableArrayList(temp);
		//Pass list and table to tableview factory method for output to table
		tableViewFactory(table,b);

	}

	public void displayAllDogsReadyByAge(TableView<Animal> table) {
		ArrayList<Animal> temp = new ArrayList<Animal>();
		int i = animals.getSize();
		
		//same comments as above method only its a dog instead of cat
		for(int j = 0 ; j < i ; j++)
		{
			if (animals.get(j).getCat().getClass().toString().equals("class application.Adoption")){
				if(animals.get(j).getCat().getStat().equals("Ready")){
					if(animals.get(j).getType().equals("Dog")){
						temp.add(animals.get(j)); 
					}
				}
			}

		}
		//use comparator to sort by age using lambdas and inner class
		Collections.sort(temp, Comparator.comparingInt(h -> h.getAge()));
		//convert to observable list
		ObservableList<Animal> b = FXCollections.observableArrayList(temp);
		//Pass list and table to tableview factory method for output to table
		tableViewFactory(table,b);

	}

	public void displayAllPupsInTraining(TableView<Animal> table) {
		ArrayList<Animal> temp = new ArrayList<Animal>();
		int i = animals.getSize();

		for(int j = 0 ; j < i ; j++)
		{
			//make sure animals is for adoption
			if (animals.get(j).getCat().getClass().toString().equals("class application.Adoption")){
				//make sure it's in training
				if(animals.get(j).getCat().getStat().equals("In Training")){
					//make sure it's a dog
					if(animals.get(j).getType().equals("Dog")){
						temp.add(animals.get(j)); 
					}
				}
			}

		}
		//convert to observable list
		ObservableList<Animal> b = FXCollections.observableArrayList(temp);
		//Pass list and table to tableview factory method for output to table
		tableViewFactory(table,b);

	}

	public boolean editAdoptionDetails(Animal animal, Adoption adoption) {
		//set animal category to new adoption details passed in
		try{
			animal.setCat(adoption);
			return true;
		}catch(Exception e){
			return false;
		}

	}

	@SuppressWarnings("unchecked")
	public void interestedParties(TableView<Person> tableS) {

		ObservableList<Person> p = null;

		ArrayList<Person> temp = new ArrayList<Person>();
		for (int i = 0 ; i < possibleSponsors.size() ; i++){
			//make sure the person is interested in adoption
			if(possibleSponsors.get(i).getInterestedAdoption()){
				//if so add to list
				temp.add(possibleSponsors.get(i));
			}
		}
		//convert temp to observable list
		p = FXCollections.observableArrayList(temp);

		/*Setup table columns with the type of object they are getting information from 
		 * and the data type of that information. Also set width and the attribute in the 
		 * object that the table will be getting the data from*/
		TableColumn <Person, String>nameCol = new TableColumn<Person, String>("Name");
		nameCol.setPrefWidth(150);
		nameCol.setCellValueFactory(
				new PropertyValueFactory<Person, String>("name"));

		TableColumn <Person, String>addCol = new TableColumn<Person, String>("Address");
		addCol.setPrefWidth(150);
		addCol.setCellValueFactory(
				new PropertyValueFactory<Person, String>("address"));

		TableColumn <Person, String>phoneCol = new TableColumn<Person, String>("Phone Number");
		phoneCol.setPrefWidth(150);
		phoneCol.setCellValueFactory(
				new PropertyValueFactory<Person, String>("phone"));

		TableColumn <Person, String>emailCol = new TableColumn<Person, String>("Email");
		emailCol.setPrefWidth(150);
		emailCol.setCellValueFactory(
				new PropertyValueFactory<Person, String>("email"));

		/*table.setItems will go through the list and add the rows for all the columns 
		 * named above for every item in the list*/
		tableS.setItems(p);
		//make sure the table can only contain one set of columns
		tableS.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		//add all columns to the table
		tableS.getColumns().addAll(nameCol, addCol, phoneCol, emailCol);
	}

	
	public void displayTypes(TableView<String> table) {
		
		//clear the types table of any previous information
		table.getColumns().clear();
		//convert types list to observable list
		ObservableList<String> t = FXCollections.observableArrayList(types);
		//Setup table column to get the names of each animal
		TableColumn<String, String> tc = new TableColumn<>("Types");
        tc.setCellValueFactory(p -> {
        	//get the value of the string
            return new ReadOnlyStringWrapper(p.getValue());
        });
        /*table.setItems will go through the list and add the rows for the column
		 * named above for every item in the list*/
		table.setItems(t);
		//make sure the table can only contain one set of columns
		table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		//add column to table
		table.getColumns().add(tc);
	}

	public void addType(String text) {
		//add passed in string to types list
		types.add(text);
	}

	public boolean saveTypes() {
		//try and save types list to text file
		try{
			PrintWriter pw = new PrintWriter ("types.txt");
			for(int i = 0 ; i < types.size() ; i++)
				pw.println(types.get(i));
			pw.close();
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

	public void removeType(String s) {
		//remove the given string from types
		types.remove(s);
		//I know this only removes the first instance but in this case I think it is adequate
	}

	public void populateTypes(ComboBox<String> typeChoice) {
		/*all the add animal methods in main have a combo box where you can choose the type.
		 * this method simply adds the types from types list to the combobox in a for loop*/
		for(int i = 0 ; i < types.size() ; i++){
			typeChoice.getItems().add(types.get(i));
		}
	}

	public boolean validateEmail(TextField f){
		//check email address as it's inputted against a regex pattern
		Pattern p = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(f.getText());
		if (m.find() && m.group().equals(f.getText())){
			return true;
		}
		else{
			return false;
		}
	}


}




