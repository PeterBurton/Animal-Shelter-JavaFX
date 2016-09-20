package application;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import com.aquafx_project.AquaFx;

public class Main extends Application {

	private Stage window;
	private Scene scene;
	private BorderPane root;
	private ImageView imgView;
	private AnimalShelter as;

	@Override
	public void start(Stage primaryStage) {
		//Create new animal shelter object
		as = new AnimalShelter();
		//call to animalshelter to load saved data if present
		as.load();
		window = primaryStage;
		window.setTitle("Pete's Animal Shelter");

		root = new BorderPane();


		MenuBar mainMenu = new MenuBar();  //Creates the main menu to hold the Sub-Menus.

		//Declare sub-menus and add to main menu.
		Menu lost = new Menu("Lost");
		Menu found = new Menu("Found");
		Menu adoption = new Menu("Adoption");
		Menu reports = new Menu("Reports");
		Menu save = new Menu("Save/Close");
		Menu themes = new Menu("Theme");
		Menu maintenance = new Menu("Maintenance");

		mainMenu.getMenus().addAll(lost, found, adoption, reports, save, themes, maintenance);

		// Create and add maintenance sub-menu options
		MenuItem addAnimalType = new MenuItem("Add Animal Type");
		addAnimalType.setOnAction(e-> maintenanceAddAnimal());
		maintenance.getItems().addAll(addAnimalType);

		//Create and add the "Lost" sub-menu options. 
		MenuItem addAnimalL = new MenuItem("Add Animal");
		addAnimalL.setOnAction(e -> lostAdd());
		MenuItem displayL = new MenuItem("Display/Remove lost animals");
		displayL.setOnAction(e->displayAllLost());

		lost.getItems().addAll(addAnimalL,displayL);

		//Create and add the "Found" sub-menu options.
		MenuItem addAnimalF = new MenuItem("Add Animal");
		addAnimalF.setOnAction(e -> foundAdd());
		MenuItem displayF = new MenuItem("Display/Remove found animals");
		displayF.setOnAction(e->displayAllFound());

		found.getItems().addAll(addAnimalF,displayF);

		//Create and add the "Adoption" sub-menu options.
		MenuItem addAnimalA = new MenuItem("Add Animal");
		addAnimalA.setOnAction(e->addAdoptionAnimal());
		MenuItem transferAnimal = new MenuItem("Transfer existing animal category to Adoption");
		transferAnimal.setOnAction(e->transferAnimal());
		MenuItem interestedParties = new MenuItem("Interested Parties");
		interestedParties.setOnAction(e-> interestedParties());
		MenuItem displayA = new MenuItem("Display and Edit or Remove Adoption animals");
		displayA.setOnAction(e->displayAllAdoption());

		adoption.getItems().addAll(addAnimalA, transferAnimal,interestedParties,displayA);

		//Create and add the "Reports" sub-menu options.
		SeparatorMenuItem general = new SeparatorMenuItem();
		Text generalReports = new Text("General Reports");
		generalReports.setUnderline(true);
		general.setContent(generalReports);
		MenuItem animalsByAge = new MenuItem("All Animals by Age");
		animalsByAge.setOnAction(e->displayAllAnimalsByAge());
		MenuItem sponsors = new MenuItem("All Sponsors");
		sponsors.setOnAction(e -> displaySponsors());

		SeparatorMenuItem lostR = new SeparatorMenuItem();
		Text lostReports = new Text("Lost Reports");
		lostReports.setUnderline(true);
		lostR.setContent(lostReports);
		MenuItem disLostLoc = new MenuItem("All animals lost in a certain location");
		disLostLoc.setOnAction(e->displayLostCertainLocation());
		MenuItem disCatLostLocDate = new MenuItem("All cats lost in a certain location on a specific date");
		disCatLostLocDate.setOnAction(e->displayCatsLostLocationDate());

		SeparatorMenuItem foundR = new SeparatorMenuItem();
		Text foundReports = new Text("Found Reports");
		foundReports.setUnderline(true);
		foundR.setContent(foundReports);
		MenuItem fLoc = new MenuItem("All animals found in a certain location");
		fLoc.setOnAction(e-> displayAllFoundLocation());
		MenuItem fDateGen = new MenuItem("All animals found between certain dates, order by gender");
		fDateGen.setOnAction(e->displayFoundDatesGender());
		MenuItem fLocDate = new MenuItem("All animals found in a certain location between certain dates");
		fLocDate.setOnAction(e->displayFoundLocDate());

		SeparatorMenuItem adoptR = new SeparatorMenuItem();
		Text adoptReports = new Text("Adoption Reports");
		adoptReports.setUnderline(true);
		adoptR.setContent(adoptReports);
		MenuItem aReadyByName = new MenuItem("All animals ready for adoption, order by name");
		aReadyByName.setOnAction(e->displayAllReadyByName());
		MenuItem catReadyByAge = new MenuItem("All Cats ready for adoption, order by age");
		catReadyByAge.setOnAction(e->displayCatsReadyByAge());
		MenuItem dogReadyByAge = new MenuItem("All Dogs ready for adoption, order by age");
		dogReadyByAge.setOnAction(e->displayDogsReadyByAge());
		MenuItem pupsInTraining = new MenuItem("All pups, still in training up for adoption");
		pupsInTraining.setOnAction(e->displayPupsInTraining());

		reports.getItems().addAll(general,animalsByAge,sponsors,lostR,disLostLoc, disCatLostLocDate,foundR,
				fLoc, fDateGen,fLocDate,adoptR,aReadyByName,catReadyByAge, dogReadyByAge, pupsInTraining);

		//Create and add the "Save" sub-menu options.
		MenuItem saveItem = new MenuItem("Save");
		saveItem.setOnAction(e -> 
		{
			if (as.save())
			{
				AlertBox.display("Save", "Save files succesful");
			}
			else
			{
				AlertBox.display("Save", "It didn't work :-(");
			}
		});

		MenuItem saveAndClose = new MenuItem("Save and Close");
		saveAndClose.setOnAction(e ->{
			boolean saved = as.save();
			clickCloseButton(saved);
		});

		save.getItems().addAll(saveItem, saveAndClose);

		//Create and add Theme SubMenu items
		//Modena and Caspian are JAVAFX built in styles, they just have to be invoked.
		RadioMenuItem modena = new RadioMenuItem("Modena");
		modena.setOnAction(e->setUserAgentStylesheet(STYLESHEET_MODENA));
		RadioMenuItem caspian = new RadioMenuItem("Caspian");
		caspian.setOnAction(e->setUserAgentStylesheet(STYLESHEET_CASPIAN));
		//Aqua-FX is an open source theme from guigarage.com, I just added it's jar file to the build path
		RadioMenuItem aquaFX = new RadioMenuItem("Aqua-FX");
		aquaFX.setOnAction(e->AquaFx.style());

		ToggleGroup theme = new ToggleGroup();
		modena.setToggleGroup(theme);
		caspian.setToggleGroup(theme);
		aquaFX.setToggleGroup(theme);
		themes.getItems().addAll(modena,caspian,aquaFX);

		Image img = new Image("file:shelter.png");//get image from file
		imgView = new ImageView(img);//create Imageview from image
		imgView.setPreserveRatio(true);//make sure image maintains its aspect ratio when resizing window
		imgView.fitWidthProperty().bind(root.widthProperty());//bind the image to root borderpane so it resizes horizontally
		imgView.fitHeightProperty().bind(root.heightProperty());//bind the image to root borderpane so it resizes vertically

		//Add paw icon to window instead of generic icon
		window.getIcons().add(new Image("file:paw.png"));

		//set the menubar to the top of the border pane
		root.setTop(mainMenu);
		//set the image as centre of borderpane
		root.setCenter(imgView);

		//add root borderpane to scene, set to 1200x800 pixels
		scene = new Scene(root,1200,800);
		//Apply the scene to the window
		window.setScene(scene);

		window.setOnCloseRequest(e->{
			//Consume the close request and deal with it myself by calling my own method
			e.consume();
			//send false to closeButton method as save wasn't pressed.
			clickCloseButton(false);
		});
		//Show the window
		window.show();

	}

	private void maintenanceAddAnimal() {

		//Set up GridPane using column constraints to set % widths, add padding, and set HGap and VGaps
		ColumnConstraints col1 = new ColumnConstraints();
		col1.setPercentWidth(25);
		ColumnConstraints col2 = new ColumnConstraints();
		col2.setPercentWidth(25);
		ColumnConstraints col3 = new ColumnConstraints();
		col3.setPercentWidth(25);
		ColumnConstraints col4 = new ColumnConstraints();
		col4.setPercentWidth(25);
		GridPane gridDisplay = new GridPane();
		gridDisplay.getColumnConstraints().addAll(col1, col2, col3, col4);
		gridDisplay.setPadding(new Insets(50, 50, 50, 50));
		gridDisplay.setVgap(15);
		gridDisplay.setHgap(15);

		Text title = new Text("MAINTENANCE: Add a new animal type to the Animal Shelter");
		title.setFont(Font.font("default", FontWeight.BOLD, 30));
		GridPane.setConstraints(title, 0, 0, 4, 1, HPos.CENTER, VPos.BASELINE);

		Label currentTypes = new Label("Current types in the shelter:");
		currentTypes.setFont(Font.font("default", FontPosture.ITALIC, 18));
		GridPane.setConstraints(currentTypes, 0, 1, 1, 1, HPos.CENTER, VPos.BASELINE);

		TableView <String> table = new TableView<String>();
		//Populate table in animal shelter class
		as.displayTypes(table);
		GridPane.setConstraints(table, 0, 2, 1, 12);

		Label addTypes = new Label("Add an animal type to the Animal Shelter program below:");
		addTypes.setFont(Font.font("default", FontPosture.ITALIC, 18));
		GridPane.setConstraints(addTypes, 1, 1, 3, 1, HPos.CENTER, VPos.BASELINE);

		Label enterType = new Label("Enter type and press Add Type:");
		GridPane.setConstraints(enterType, 1, 2, 1, 1, HPos.RIGHT, VPos.BASELINE);

		TextField typeInput = new TextField();
		GridPane.setConstraints(typeInput,2,2);

		Button addButton = new Button("Add Type");
		addButton.setOnAction(e->{
			as.addType(typeInput.getText());
			as.displayTypes(table);//Refresh the table
		});
		GridPane.setConstraints(addButton,3,2);

		Label removeTypes = new Label("Remove an animal type from the Animal Shelter program below:");
		removeTypes.setFont(Font.font("default", FontPosture.ITALIC, 18));
		GridPane.setConstraints(removeTypes, 1, 6, 3, 1, HPos.CENTER, VPos.BASELINE);

		Label removeLabel = new Label("Select Animal and hit Remove Type button");
		GridPane.setConstraints(removeLabel, 1, 7, 3, 1, HPos.CENTER, VPos.BASELINE);

		Button removeButton = new Button("Remove Type");
		removeButton.setOnAction(e->{
			String s = table.getSelectionModel().getSelectedItem();
			as.removeType(s);
			as.displayTypes(table);//Refresh the table
		});
		GridPane.setConstraints(removeButton,1, 8, 3, 1, HPos.CENTER, VPos.BASELINE);

		Label saveConfig = new Label("Save current configuration for permanent use:");
		saveConfig.setFont(Font.font("default", FontPosture.ITALIC, 18));
		GridPane.setConstraints(saveConfig, 1, 11, 3, 1, HPos.CENTER, VPos.BASELINE);

		Button saveButton = new Button("Save this configuration");
		saveButton.setOnAction(e->{
			if(as.saveTypes()){
				AlertBox.display("Save Types to file", "Save Types succesful");
			}
			else
			{
				AlertBox.display("Save Types to file", "It didn't work :-(");
			}

		});
		GridPane.setConstraints(saveButton, 1, 12, 3, 1, HPos.CENTER, VPos.BASELINE);



		gridDisplay.getChildren().addAll(title, table, currentTypes, addTypes, enterType, typeInput, addButton, saveConfig,
				saveButton, removeTypes, removeLabel, removeButton);
		root.setCenter(gridDisplay);
	}

	private void clickCloseButton(boolean saved) {
		boolean confirm = false;
		String quit;
		if(saved) //i.e. if you press close and save
			quit = "Files saved successfully.\nAre you sure you want to quit?";
		else //all other close scenarios, i.e. if you used the red x button to close the window
			quit = "Files were not saved.\nAre you sure you want to quit?";
		confirm = ConfirmationBox.show(
				quit, "Close Program",
				"Yes", "No");
		if (confirm){ // if the boolean is returned true because user clicked yes
			window.close(); //close the program
		}
	}

	private void interestedParties() {
		ColumnConstraints col1 = new ColumnConstraints();
		col1.setPercentWidth(25);
		ColumnConstraints col2 = new ColumnConstraints();
		col2.setPercentWidth(25);
		ColumnConstraints col3 = new ColumnConstraints();
		col3.setPercentWidth(25);
		ColumnConstraints col4 = new ColumnConstraints();
		col4.setPercentWidth(25);
		GridPane gridParties = new GridPane();
		gridParties.getColumnConstraints().addAll(col1, col2, col3, col4);
		gridParties.setPadding(new Insets(50, 50, 50, 50));
		gridParties.setVgap(15);
		gridParties.setHgap(15);

		//Title
		Text title = new Text("All the people who have registered their interest in adopting:");
		title.setFont(Font.font("default", FontWeight.BOLD, 30));
		GridPane.setConstraints(title, 0, 0, 4, 1, HPos.CENTER, VPos.BASELINE);

		//Search results table
		TableView<Person> table = new TableView<Person>();
		as.interestedParties(table);
		GridPane.setConstraints(table, 0, 1, 4, 1);	

		//Add Person Label
		Label addPersonLabel = new Label("Add Person to this list below:");
		addPersonLabel.setFont(Font.font("default", FontPosture.ITALIC, 20));
		GridPane.setConstraints(addPersonLabel, 0, 2, 2, 1, HPos.CENTER, VPos.BASELINE);

		Label oNameLabel = new Label("Name:");
		GridPane.setConstraints(oNameLabel, 0, 3);

		//Owner Name Input
		TextField oNameInput = new TextField();
		GridPane.setConstraints(oNameInput, 1, 3);

		//Owner Address Label
		Label oAddLabel = new Label("Address:");
		GridPane.setConstraints(oAddLabel, 0, 4);

		//OwnerAddress Input
		TextField oAddInput = new TextField();
		GridPane.setConstraints(oAddInput, 1, 4);

		//owner phone label
		Label oPhoneLabel = new Label("Phone Number:");
		GridPane.setConstraints(oPhoneLabel, 0, 5);

		//owner phone input
		TextField oPhoneInput = new TextField();
		GridPane.setConstraints(oPhoneInput, 1, 5);

		//owner email label
		Label oEmailLabel = new Label("Email Address:");
		GridPane.setConstraints(oEmailLabel, 0, 6);

		//owner email input
		TextField oEmailInput = new TextField();
		handleEmail( oEmailInput);
		GridPane.setConstraints(oEmailInput, 1, 6);

		//Add Button
		Button addPerson = new Button("Add Person");
		addPerson.setOnAction(e->{
			if (as.validateEmail(oEmailInput)){
				Person person = new Person(oNameInput.getText(),oAddInput.getText(),
						oPhoneInput.getText(),oEmailInput.getText());
				person.setInterestedAdoption(true);
				if (as.addSponsorFound(person)){
					AlertBox.display("Add Person", "Person added successfully");
				}
				else{
					AlertBox.display("Add Person", "It didn't work :-(");
				}
				interestedParties();
			}
			else{
				AlertBox.display("Email incorrect or not present", "Please enter valid Email");
			}

		});


		//Cancel Button
		Button cancel = new Button("Cancel");
		cancel.setOnAction(e->root.setCenter(imgView));

		//HBox for Buttons
		HBox buttons = new HBox(30,addPerson, cancel);
		GridPane.setConstraints(buttons, 1, 7);

		//Remove Person Label
		Label removePersonLabel = new Label("Remove Person from this list below:");
		removePersonLabel.setFont(Font.font("default", FontPosture.ITALIC, 20));
		GridPane.setConstraints(removePersonLabel, 2, 2, 2, 1, HPos.CENTER, VPos.BASELINE);

		Label removePerson = new Label("Select Person from this list and press remove:");
		GridPane.setConstraints(removePerson, 2, 3, 2, 1, HPos.CENTER, VPos.BASELINE);

		//Remove Button
		Button remPerson = new Button("Remove Person");
		remPerson.setOnAction(e->{
			Person person = table.getSelectionModel().getSelectedItem();
			person.setInterestedAdoption(false);
			if (person.getInterestedAdoption() == false)
			{
				AlertBox.display("Remove Person", "Person removed successfully");
			}
			else
			{
				AlertBox.display("Remove Person", "It didn't work :-(");
			}
			interestedParties();

		});

		//Cancel Button
		Button remCancel = new Button("Cancel");
		remCancel.setOnAction(e->root.setCenter(imgView));

		//HBox for Buttons
		HBox remButtons = new HBox(30,remPerson, remCancel);
		GridPane.setConstraints(remButtons, 3, 4, 2, 1, HPos.CENTER, VPos.CENTER);

		gridParties.getChildren().addAll(title, table, addPersonLabel, oNameLabel, oNameInput, oAddLabel, oAddInput,
				oPhoneLabel, oPhoneInput, oEmailLabel, oEmailInput,buttons, removePersonLabel, removePerson, remButtons);
		root.setCenter(gridParties);
	}

	private void addAdoptionAnimal() {

		ColumnConstraints col1 = new ColumnConstraints();
		col1.setPercentWidth(20);
		ColumnConstraints col2 = new ColumnConstraints();
		col2.setPercentWidth(30);
		ColumnConstraints col3 = new ColumnConstraints();
		col3.setPercentWidth(20);
		ColumnConstraints col4 = new ColumnConstraints();
		col4.setPercentWidth(30);
		GridPane gridAdAdd = new GridPane();
		gridAdAdd.getColumnConstraints().addAll(col1, col2, col3, col4);
		gridAdAdd.setPadding(new Insets(50, 50, 50, 50));
		gridAdAdd.setVgap(15);
		gridAdAdd.setHgap(15);

		//Title
		Text addNewAnimal = new Text("Add an Adoption animal to the system");
		addNewAnimal.setFont(Font.font("default", FontWeight.BOLD, 30));
		GridPane.setConstraints(addNewAnimal, 0, 0, 4, 1, HPos.CENTER, VPos.BASELINE);

		//Add Animal Label
		Label addAnimalLabel = new Label("Add an Animal below:");
		addAnimalLabel.setFont(Font.font("default", FontPosture.ITALIC, 20));
		GridPane.setConstraints(addAnimalLabel, 0, 1, 2, 1);

		//Name Label
		Label nameLabel = new Label("Name:");
		GridPane.setConstraints(nameLabel, 0, 2);

		//Name Input
		TextField nameInput = new TextField();
		GridPane.setConstraints(nameInput, 1, 2);

		//Type label
		Label typeLabel = new Label("Type:");
		GridPane.setConstraints(typeLabel, 0, 3);

		//Type combo box
		ComboBox <String> typeChoice = new ComboBox<String>();
		as.populateTypes(typeChoice);
		typeChoice.setPromptText("Choose a Type");
		GridPane.setConstraints(typeChoice, 1, 3);

		//Breed Label
		Label breedLabel = new Label("Breed:");
		GridPane.setConstraints(breedLabel, 0, 4);

		//Breed Input
		TextField breedInput = new TextField();
		GridPane.setConstraints(breedInput, 1, 4);

		//Colour Label
		Label colourLabel = new Label("Colour:");
		GridPane.setConstraints(colourLabel, 0, 5);

		//ColourInput
		TextField colourInput = new TextField();
		GridPane.setConstraints(colourInput, 1, 5);

		//Description Label
		Label descriptionLabel = new Label("Description:");
		GridPane.setConstraints(descriptionLabel, 0, 6);

		//Description Input
		TextField descriptionInput = new TextField();
		GridPane.setConstraints(descriptionInput, 1, 6);

		//Age label
		Label ageLabel = new Label("Age:");
		GridPane.setConstraints(ageLabel, 0, 7);

		//Age Input
		TextField ageInput = new TextField(){
			/*override the replaceText and replaceSelection methods
			 * of a textField to make sure that only a the characters 0-9 can be entered
			 * in this field. This will avoid exceptions when we create the animal
			 * and the age isn't an integer*/
			@Override public void replaceText(int start, int end, String text) {
				if (text.matches("[0-9]*")) {
					super.replaceText(start, end, text);
				}
			}

			@Override public void replaceSelection(String text) {
				if (text.matches("[0-9]*")) {
					super.replaceSelection(text);
				}
			}
		};
		GridPane.setConstraints(ageInput, 1, 7);

		//Gender label
		Label genderLabel = new Label("Gender:");
		GridPane.setConstraints(genderLabel, 0, 8);

		//Gender Radio Buttons
		RadioButton genderMale = new RadioButton("Male");
		RadioButton genderFemale = new RadioButton("Female");
		ToggleGroup genderGroup = new ToggleGroup();
		genderMale.setToggleGroup(genderGroup);
		genderFemale.setToggleGroup(genderGroup);
		HBox genderBox = new HBox(genderMale,genderFemale);
		GridPane.setConstraints(genderBox, 1, 8);

		//Picture Label
		Label picLabel = new Label("Picture:");
		GridPane.setConstraints(picLabel, 0, 9);

		//Picture Button chooser
		ImageView displayPic = new ImageView();
		FileChooser.ExtensionFilter imageFilter = new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.png");
		FileChooser fc = new FileChooser();
		fc.getExtensionFilters().add(imageFilter);
		Button btn = new Button();
		btn.setText("Choose Picture");
		btn.setOnAction((ActionEvent event) -> {
			File pic = fc.showOpenDialog(window);
			try {
				String imageFile = pic.toURI().toURL().toString();
				Image image = new Image(imageFile, 100, 100, true, true);
				displayPic.setImage(image);
			} catch (Exception e) {
				root.setCenter(gridAdAdd);
				//e.printStackTrace();
			} 
		});
		GridPane.setConstraints(btn, 1, 9);

		//Display Picture if selected
		GridPane.setConstraints(displayPic, 2, 8, 2, 6);

		//Add adoption Details Label
		Label ownerLabel = new Label("Enter Adoption details below:");
		ownerLabel.setFont(Font.font("Verdana", FontPosture.ITALIC, 20));
		GridPane.setConstraints(ownerLabel, 2, 1, 2, 1);

		//Date Label
		Label dateLabel = new Label("Date Added");
		GridPane.setConstraints(dateLabel, 2, 2);

		//Date Picker
		DatePicker datePick = new DatePicker();
		HBox dateBox = new HBox (datePick);
		GridPane.setConstraints(dateBox, 3, 2);

		//neutered label
		Label neuteredLabel = new Label("Neutered?:");
		GridPane.setConstraints(neuteredLabel, 2, 3);

		//neutered Radio Buttons
		RadioButton neutYes = new RadioButton("Yes");
		RadioButton neutNo = new RadioButton("No");
		ToggleGroup neutGroup = new ToggleGroup();
		neutYes.setToggleGroup(neutGroup);
		neutNo.setToggleGroup(neutGroup);
		HBox neutBox = new HBox(neutYes,neutNo);
		GridPane.setConstraints(neutBox, 3, 3);

		//chipped label
		Label chippedLabel = new Label("Chipped?:");
		GridPane.setConstraints(chippedLabel, 2, 4);

		//chipped Radio Buttons
		RadioButton chipYes = new RadioButton("Yes");
		RadioButton chipNo = new RadioButton("No");
		ToggleGroup chipGroup = new ToggleGroup();
		chipYes.setToggleGroup(chipGroup);
		chipNo.setToggleGroup(chipGroup);
		HBox chipBox = new HBox(chipYes,chipNo);
		GridPane.setConstraints(chipBox, 3, 4);

		//vaccinated label
		Label vacLabel = new Label("Vaccinated?:");
		GridPane.setConstraints(vacLabel, 2, 5);

		//vaccinated Radio Buttons
		RadioButton vacYes = new RadioButton("Yes");
		RadioButton vacNo = new RadioButton("No");
		ToggleGroup vacGroup = new ToggleGroup();
		vacYes.setToggleGroup(vacGroup);
		vacNo.setToggleGroup(vacGroup);
		HBox vacBox = new HBox(vacYes,vacNo);
		GridPane.setConstraints(vacBox, 3, 5);

		//reserved label
		Label resLabel = new Label("Reserved?:");
		GridPane.setConstraints(resLabel, 2, 6);

		//reserved Radio Buttons
		RadioButton resYes = new RadioButton("Yes");
		RadioButton resNo = new RadioButton("No");
		ToggleGroup resGroup = new ToggleGroup();
		resYes.setToggleGroup(resGroup);
		resNo.setToggleGroup(resGroup);
		HBox resBox = new HBox(resYes,resNo);
		GridPane.setConstraints(resBox, 3, 6);

		//Status label
		Label statLabel = new Label("Status:");
		GridPane.setConstraints(statLabel, 2, 7);

		//Status Radio Buttons
		RadioButton statTrain = new RadioButton("In Training");
		RadioButton statReady = new RadioButton("Ready");
		ToggleGroup statGroup = new ToggleGroup();
		statTrain.setToggleGroup(statGroup);
		statReady.setToggleGroup(statGroup);
		HBox statBox = new HBox(statTrain,statReady);
		GridPane.setConstraints(statBox, 3, 7);

		//Add Button
		Button addAnimal = new Button();
		addAnimal.setText("Add Animal");

		addAnimal.setOnAction(e -> 

		{
			Image img = displayPic.getImage();
			if (img == null)
				img = new Image("file:paw.png",100,100, true, true);

			int age = Integer.parseInt(ageInput.getText());
			String colour = colourInput.getText();
			boolean gender;
			if (genderMale.isSelected()){
				gender = true;
			}
			else{
				gender = false;
			}
			RadioButton stat = (RadioButton)statTrain.getToggleGroup().getSelectedToggle();
			String status = stat.getText();
			boolean neutered;
			if (neutYes.isSelected()){
				neutered = true;
			}
			else{
				neutered = false;
			}
			boolean chipped;
			if (chipYes.isSelected()){
				chipped = true;
			}
			else{
				chipped = false;
			}
			boolean vaccinated;
			if (vacYes.isSelected()){
				vaccinated = true;
			}
			else{
				vaccinated = false;
			}
			boolean reserved;
			if (resYes.isSelected()){
				reserved = true;
			}
			else{
				reserved = false;
			}
			String description = descriptionInput.getText();
			String name = nameInput.getText();
			String breed = breedInput.getText();
			String type = typeChoice.getValue();
			Adoption adoption = new Adoption(datePick.getValue(), neutered, chipped, vaccinated, status, reserved);
			Category cat = adoption;
			Animal animal = new Animal();
			animal.setAge(age);
			animal.setColour(colour);
			animal.setGender(gender);
			animal.setDescription(description);
			animal.setName(name);
			animal.setBreed(breed);
			animal.setType(type);
			animal.setCat(cat);
			animal.setPicture(img);

			if(as.addAnimal(animal))
			{
				AlertBox.display("Add Animal", "Animal added successfully");
				addAdoptionAnimal();
			}
			else
			{
				AlertBox.display("Add Animal", "It didn't work :-(");
			}
		});

		//Cancel Button
		Button cancelAdd = new Button();
		cancelAdd.setText("Cancel");
		cancelAdd.setOnAction(e->{root.setCenter(imgView);});

		//HBox for Buttons
		HBox buttons = new HBox(30, addAnimal, cancelAdd);
		GridPane.setConstraints(buttons, 3, 16);




		//Add everything to grid
		gridAdAdd.getChildren().addAll(addNewAnimal,addAnimalLabel, nameLabel, nameInput, typeLabel, typeChoice,  breedLabel, breedInput,
				colourLabel, colourInput, descriptionLabel, descriptionInput, ageLabel, ageInput, genderLabel, genderBox,
				picLabel, btn, dateLabel, dateBox, ownerLabel, displayPic, buttons, neuteredLabel, neutBox, chippedLabel, chipBox,
				vacLabel, vacBox, statLabel, statBox, resLabel, resBox);

		root.setCenter(gridAdAdd);
	}

	private void displayPupsInTraining() {

		ColumnConstraints col1 = new ColumnConstraints();
		col1.setPercentWidth(25);
		ColumnConstraints col2 = new ColumnConstraints();
		col2.setPercentWidth(25);
		ColumnConstraints col3 = new ColumnConstraints();
		col3.setPercentWidth(25);
		ColumnConstraints col4 = new ColumnConstraints();
		col4.setPercentWidth(25);
		GridPane gridDisplay = new GridPane();
		gridDisplay.getColumnConstraints().addAll(col1, col2, col3, col4);
		gridDisplay.setPadding(new Insets(50, 50, 50, 50));
		gridDisplay.setVgap(15);
		gridDisplay.setHgap(15);

		//Title
		Text title = new Text("REPORT: All adoption puppies still in training.");
		title.setFont(Font.font("default", FontWeight.BOLD, 30));
		GridPane.setConstraints(title, 0, 0, 4, 1, HPos.CENTER, VPos.BASELINE);

		TableView <Animal> table = new TableView<Animal>();
		as.displayAllPupsInTraining(table);
		GridPane.setConstraints(table, 0, 4, 4, 1);

		Label printLabel = new Label("Press Print to print a report");
		printLabel.setFont(Font.font("default", FontPosture.ITALIC, 18));
		GridPane.setConstraints(printLabel, 0, 6, 4, 1, HPos.CENTER, VPos.BASELINE);

		Button printButton = new Button("Print Report");
		printButton.setOnAction(e->{
			String report = "REPORT: ALL ADOPTION PUPPIES STILL IN TRAINING\n";
			as.print(report,table);
		});
		GridPane.setConstraints(printButton, 0, 7, 4, 1, HPos.CENTER, VPos.BASELINE);

		gridDisplay.getChildren().addAll(title,table, printLabel, printButton);
		root.setCenter(gridDisplay);
	}

	private void displayDogsReadyByAge() {
		ColumnConstraints col1 = new ColumnConstraints();
		col1.setPercentWidth(25);
		ColumnConstraints col2 = new ColumnConstraints();
		col2.setPercentWidth(25);
		ColumnConstraints col3 = new ColumnConstraints();
		col3.setPercentWidth(25);
		ColumnConstraints col4 = new ColumnConstraints();
		col4.setPercentWidth(25);
		GridPane gridDisplay = new GridPane();
		gridDisplay.getColumnConstraints().addAll(col1, col2, col3, col4);
		gridDisplay.setPadding(new Insets(50, 50, 50, 50));
		gridDisplay.setVgap(15);
		gridDisplay.setHgap(15);

		//Title
		Text title = new Text("REPORT: All dogs ready for adoption, organised by age.");
		title.setFont(Font.font("default", FontWeight.BOLD, 30));
		GridPane.setConstraints(title, 0, 0, 4, 1, HPos.CENTER, VPos.BASELINE);

		TableView <Animal> table = new TableView<Animal>();
		as.displayAllDogsReadyByAge(table);
		GridPane.setConstraints(table, 0, 4, 4, 1);

		Label printLabel = new Label("Press Print to print a report");
		printLabel.setFont(Font.font("default", FontPosture.ITALIC, 18));
		GridPane.setConstraints(printLabel, 0, 6, 4, 1, HPos.CENTER, VPos.BASELINE);

		Button printButton = new Button("Print Report");
		printButton.setOnAction(e->{
			String report = "REPORT: ALL DOGS READY FOR ADOPTION ORGANISED BY AGE\n";
			as.print(report,table);
		});
		GridPane.setConstraints(printButton, 0, 7, 4, 1, HPos.CENTER, VPos.BASELINE);

		gridDisplay.getChildren().addAll(title,table, printLabel, printButton);
		root.setCenter(gridDisplay);
	}

	private void displayCatsReadyByAge() {

		ColumnConstraints col1 = new ColumnConstraints();
		col1.setPercentWidth(25);
		ColumnConstraints col2 = new ColumnConstraints();
		col2.setPercentWidth(25);
		ColumnConstraints col3 = new ColumnConstraints();
		col3.setPercentWidth(25);
		ColumnConstraints col4 = new ColumnConstraints();
		col4.setPercentWidth(25);
		GridPane gridDisplay = new GridPane();
		gridDisplay.getColumnConstraints().addAll(col1, col2, col3, col4);
		gridDisplay.setPadding(new Insets(50, 50, 50, 50));
		gridDisplay.setVgap(15);
		gridDisplay.setHgap(15);

		//Title
		Text title = new Text("REPORT: All cats ready for adoption, organised by age.");
		title.setFont(Font.font("default", FontWeight.BOLD, 30));
		GridPane.setConstraints(title, 0, 0, 4, 1, HPos.CENTER, VPos.BASELINE);

		TableView <Animal> table = new TableView<Animal>();
		as.displayAllCatsReadyByAge(table);
		GridPane.setConstraints(table, 0, 4, 4, 1);

		Label printLabel = new Label("Press Print to print a report");
		printLabel.setFont(Font.font("default", FontPosture.ITALIC, 18));
		GridPane.setConstraints(printLabel, 0, 6, 4, 1, HPos.CENTER, VPos.BASELINE);

		Button printButton = new Button("Print Report");
		printButton.setOnAction(e->{
			String report = "REPORT: ALL CATS READY FOR ADOPTION ORGANISED BY AGE\n";
			as.print(report,table);
		});
		GridPane.setConstraints(printButton, 0, 7, 4, 1, HPos.CENTER, VPos.BASELINE);

		gridDisplay.getChildren().addAll(title,table, printLabel, printButton);
		root.setCenter(gridDisplay);
	}

	private void displayAllReadyByName() {

		ColumnConstraints col1 = new ColumnConstraints();
		col1.setPercentWidth(25);
		ColumnConstraints col2 = new ColumnConstraints();
		col2.setPercentWidth(25);
		ColumnConstraints col3 = new ColumnConstraints();
		col3.setPercentWidth(25);
		ColumnConstraints col4 = new ColumnConstraints();
		col4.setPercentWidth(25);
		GridPane gridDisplay = new GridPane();
		gridDisplay.getColumnConstraints().addAll(col1, col2, col3, col4);
		gridDisplay.setPadding(new Insets(50, 50, 50, 50));
		gridDisplay.setVgap(15);
		gridDisplay.setHgap(15);

		//Title
		Text title = new Text("REPORT: All animals ready for adoption, organised by name.");
		title.setFont(Font.font("default", FontWeight.BOLD, 30));
		GridPane.setConstraints(title, 0, 0, 4, 1, HPos.CENTER, VPos.BASELINE);

		TableView <Animal> table = new TableView<Animal>();
		as.displayAdoptionAllReadyByName(table);
		GridPane.setConstraints(table, 0, 4, 4, 1);

		Label printLabel = new Label("Press Print to print a report");
		printLabel.setFont(Font.font("default", FontPosture.ITALIC, 18));
		GridPane.setConstraints(printLabel, 0, 6, 4, 1, HPos.CENTER, VPos.BASELINE);

		Button printButton = new Button("Print Report");
		printButton.setOnAction(e->{
			String report = "REPORT: ALL ANIMALS READY FOR ADOPTION ORGANISED BY NAME\n";
			as.print(report,table);
		});
		GridPane.setConstraints(printButton, 0, 7, 4, 1, HPos.CENTER, VPos.BASELINE);

		gridDisplay.getChildren().addAll(title,table, printLabel, printButton);
		root.setCenter(gridDisplay);
	}

	private void displayFoundLocDate() {

		ColumnConstraints col1 = new ColumnConstraints();
		col1.setPercentWidth(25);
		ColumnConstraints col2 = new ColumnConstraints();
		col2.setPercentWidth(25);
		ColumnConstraints col3 = new ColumnConstraints();
		col3.setPercentWidth(25);
		ColumnConstraints col4 = new ColumnConstraints();
		col4.setPercentWidth(25);
		GridPane gridDisplay = new GridPane();
		gridDisplay.getColumnConstraints().addAll(col1, col2, col3, col4);
		gridDisplay.setPadding(new Insets(50, 50, 50, 50));
		gridDisplay.setVgap(15);
		gridDisplay.setHgap(15);

		//Title
		Text title = new Text("REPORT: All animals found in a certain location, between certain dates");
		title.setFont(Font.font("default", FontWeight.BOLD, 30));
		GridPane.setConstraints(title, 0, 0, 4, 1, HPos.CENTER, VPos.BASELINE);

		TableView <Animal> table = new TableView<Animal>();
		GridPane.setConstraints(table, 0, 5, 4, 1);

		Label location = new Label("Enter location and two dates and press search:");
		location.setFont(Font.font("default", FontPosture.ITALIC, 18));
		GridPane.setConstraints(location, 0, 1, 4, 1, HPos.CENTER, VPos.BASELINE);

		Label locLabel = new Label("Enter Location:");
		GridPane.setConstraints(locLabel, 0, 2);

		TextField locationInput = new TextField();
		GridPane.setConstraints(locationInput,1,2);

		Label dateLabel = new Label("Enter 1stdate:");
		GridPane.setConstraints(dateLabel,0,3);

		DatePicker datePick = new DatePicker();
		GridPane.setConstraints(datePick,1,3);

		Label dateLabel2 = new Label("Enter 2nd date:");
		GridPane.setConstraints(dateLabel2,2,3);

		DatePicker datePick2 = new DatePicker();
		GridPane.setConstraints(datePick2,3,3);

		Button locButton = new Button("Search");
		locButton.setOnAction(e->{

			String locInputByUser = locationInput.getText().toLowerCase();
			LocalDate date = datePick.getValue();
			LocalDate date2 = datePick2.getValue();
			as.displayFoundLocationDate(table, locInputByUser, date, date2);
		});
		GridPane.setConstraints(locButton,0,4, 4, 1, HPos.CENTER, VPos.BASELINE);

		Label printLabel = new Label("Press Print to print a report");
		printLabel.setFont(Font.font("default", FontPosture.ITALIC, 18));
		GridPane.setConstraints(printLabel, 0, 6, 4, 1, HPos.CENTER, VPos.BASELINE);

		Button printButton = new Button("Print Report");
		printButton.setOnAction(e->{
			String report = "REPORT: ALL ANIMALS FOUND IN " + locationInput.getText() + 
					" BETWEEN "+ datePick.getValue() +" AND " + datePick2.getValue() + "\n";
			as.print(report,table);
		});
		GridPane.setConstraints(printButton, 0, 7, 4, 1, HPos.CENTER, VPos.BASELINE);

		gridDisplay.getChildren().addAll(title,location,locLabel,locationInput, datePick, dateLabel,
				datePick2, dateLabel2,locButton,table, printLabel, printButton);
		root.setCenter(gridDisplay);
	}

	private void displayFoundDatesGender() {
		ColumnConstraints col1 = new ColumnConstraints();
		col1.setPercentWidth(25);
		ColumnConstraints col2 = new ColumnConstraints();
		col2.setPercentWidth(25);
		ColumnConstraints col3 = new ColumnConstraints();
		col3.setPercentWidth(25);
		ColumnConstraints col4 = new ColumnConstraints();
		col4.setPercentWidth(25);
		GridPane gridDisplay = new GridPane();
		gridDisplay.getColumnConstraints().addAll(col1, col2, col3, col4);
		gridDisplay.setPadding(new Insets(50, 50, 50, 50));
		gridDisplay.setVgap(15);
		gridDisplay.setHgap(15);

		//Title
		Text title = new Text("REPORT: All animals found between certain dates, organised by gender");
		title.setFont(Font.font("default", FontWeight.BOLD, 30));
		GridPane.setConstraints(title, 0, 0, 4, 1, HPos.CENTER, VPos.BASELINE);

		TableView <Animal> table = new TableView<Animal>();
		GridPane.setConstraints(table, 0, 4, 4, 1);

		Label location = new Label("Enter 1st and 2nd date and press search:");
		location.setFont(Font.font("default", FontPosture.ITALIC, 18));
		GridPane.setConstraints(location, 0, 1, 4, 1, HPos.CENTER, VPos.BASELINE);

		Label dateLabel1 = new Label("Enter 1st date:");
		GridPane.setConstraints(dateLabel1, 0, 2);

		DatePicker datePick1 = new DatePicker();
		GridPane.setConstraints(datePick1,1,2);

		Label dateLabel2 = new Label("Enter 2nd date:");
		GridPane.setConstraints(dateLabel2,2,2);

		DatePicker datePick2 = new DatePicker();
		GridPane.setConstraints(datePick2,3,2);

		Button locButton = new Button("Search");
		locButton.setOnAction(e->{

			LocalDate date1 = datePick1.getValue();
			LocalDate date2 = datePick2.getValue();
			as.displayFoundDateGender(table, date1, date2);
		});
		GridPane.setConstraints(locButton,0,3, 4, 1, HPos.CENTER, VPos.BASELINE);

		Label printLabel = new Label("Press Print to print a report");
		printLabel.setFont(Font.font("default", FontPosture.ITALIC, 18));
		GridPane.setConstraints(printLabel, 0, 5, 4, 1, HPos.CENTER, VPos.BASELINE);

		Button printButton = new Button("Print Report");
		printButton.setOnAction(e->{
			String report = "REPORT: ALL ANIMALS FOUND BETWEEN "+ datePick1.getValue() +" AND " + datePick2.getValue() + ", ORGANISED BY GENDER\n";
			as.print(report,table);
		});
		GridPane.setConstraints(printButton, 0, 6, 4, 1, HPos.CENTER, VPos.BASELINE);

		gridDisplay.getChildren().addAll(title,location,datePick1, dateLabel1, datePick2, dateLabel2,locButton,table, printLabel, printButton);
		root.setCenter(gridDisplay);
	}

	private void displayAllFoundLocation() {
		ColumnConstraints col1 = new ColumnConstraints();
		col1.setPercentWidth(20);
		ColumnConstraints col2 = new ColumnConstraints();
		col2.setPercentWidth(30);
		ColumnConstraints col3 = new ColumnConstraints();
		col3.setPercentWidth(20);
		ColumnConstraints col4 = new ColumnConstraints();
		col4.setPercentWidth(30);
		GridPane gridDisplayFoundLocation = new GridPane();
		gridDisplayFoundLocation.getColumnConstraints().addAll(col1, col2, col3, col4);
		gridDisplayFoundLocation.setPadding(new Insets(50, 50, 50, 50));
		gridDisplayFoundLocation.setVgap(15);
		gridDisplayFoundLocation.setHgap(15);

		//Title
		Text title = new Text("REPORT: All animals found in a certain location.");
		title.setFont(Font.font("default", FontWeight.BOLD, 30));
		GridPane.setConstraints(title, 0, 0, 4, 1, HPos.CENTER, VPos.BASELINE);

		TableView <Animal> table = new TableView<Animal>();
		GridPane.setConstraints(table, 0, 3, 4, 1);

		Label location = new Label("Enter location in text field and press search:");
		location.setFont(Font.font("default", FontPosture.ITALIC, 18));
		GridPane.setConstraints(location, 0, 1, 4, 1, HPos.CENTER, VPos.BASELINE);

		Label locLabel = new Label("Enter Location:");
		GridPane.setConstraints(locLabel, 0, 2);

		TextField locationInput = new TextField();
		GridPane.setConstraints(locationInput,1,2);

		Button locButton = new Button("Search");
		locButton.setOnAction(e->{

			String locInputByUser = locationInput.getText().toLowerCase();
			as.displayAllFoundLocation(table, locInputByUser);
		});
		GridPane.setConstraints(locButton,2,2);

		Label printLabel = new Label("Press Print to print a report");
		printLabel.setFont(Font.font("default", FontPosture.ITALIC, 18));
		GridPane.setConstraints(printLabel, 0, 4, 4, 1, HPos.CENTER, VPos.BASELINE);

		Button printButton = new Button("Print Report");
		printButton.setOnAction(e->{
			String report = "REPORT: ALL ANIMALS FOUND IN A SPECIFIC LOCATION (" +  locationInput.getText() + ")\n";
			as.print(report,table);
		});
		GridPane.setConstraints(printButton, 0, 5, 4, 1, HPos.CENTER, VPos.BASELINE);

		gridDisplayFoundLocation.getChildren().addAll(title,location,locLabel,locationInput,locButton,table, printLabel, printButton);
		root.setCenter(gridDisplayFoundLocation);
	}

	private void displayCatsLostLocationDate() {
		ColumnConstraints col1 = new ColumnConstraints();
		col1.setPercentWidth(25);
		ColumnConstraints col2 = new ColumnConstraints();
		col2.setPercentWidth(25);
		ColumnConstraints col3 = new ColumnConstraints();
		col3.setPercentWidth(25);
		ColumnConstraints col4 = new ColumnConstraints();
		col4.setPercentWidth(25);
		GridPane gridDisplay = new GridPane();
		gridDisplay.getColumnConstraints().addAll(col1, col2, col3, col4);
		gridDisplay.setPadding(new Insets(50, 50, 50, 50));
		gridDisplay.setVgap(15);
		gridDisplay.setHgap(15);

		//Title
		Text title = new Text("REPORT: All cats lost in a certain location, on a specific date");
		title.setFont(Font.font("default", FontWeight.BOLD, 30));
		GridPane.setConstraints(title, 0, 0, 4, 1, HPos.CENTER, VPos.BASELINE);

		TableView <Animal> table = new TableView<Animal>();
		GridPane.setConstraints(table, 0, 4, 4, 1);

		Label location = new Label("Enter location and date and press search:");
		location.setFont(Font.font("default", FontPosture.ITALIC, 18));
		GridPane.setConstraints(location, 0, 1, 4, 1, HPos.CENTER, VPos.BASELINE);

		Label locLabel = new Label("Enter Location:");
		GridPane.setConstraints(locLabel, 0, 2);

		TextField locationInput = new TextField();
		GridPane.setConstraints(locationInput,1,2);

		Label dateLabel = new Label("Enter date:");
		GridPane.setConstraints(dateLabel,2,2);

		DatePicker datePick = new DatePicker();
		GridPane.setConstraints(datePick,3,2);

		Button locButton = new Button("Search");
		locButton.setOnAction(e->{

			String locInputByUser = locationInput.getText().toLowerCase();
			LocalDate date = datePick.getValue();
			as.displayCatsLostLocationWithDate(table, locInputByUser, date);
		});
		GridPane.setConstraints(locButton,0,3, 4, 1, HPos.CENTER, VPos.BASELINE);

		Label printLabel = new Label("Press Print to print a report");
		printLabel.setFont(Font.font("default", FontPosture.ITALIC, 18));
		GridPane.setConstraints(printLabel, 0, 5, 4, 1, HPos.CENTER, VPos.BASELINE);

		Button printButton = new Button("Print Report");
		printButton.setOnAction(e->{
			String report = "REPORT: ALL CATS LOST IN " +  locationInput.getText() + ", ON " + datePick.getValue() + "\n";
			as.print(report,table);
		});
		GridPane.setConstraints(printButton, 0, 6, 4, 1, HPos.CENTER, VPos.BASELINE);

		gridDisplay.getChildren().addAll(title,location,locLabel,locationInput, datePick, dateLabel,locButton,table, printLabel, printButton);
		root.setCenter(gridDisplay);
	}

	private void displayLostCertainLocation() {

		ColumnConstraints col1 = new ColumnConstraints();
		col1.setPercentWidth(20);
		ColumnConstraints col2 = new ColumnConstraints();
		col2.setPercentWidth(30);
		ColumnConstraints col3 = new ColumnConstraints();
		col3.setPercentWidth(20);
		ColumnConstraints col4 = new ColumnConstraints();
		col4.setPercentWidth(30);
		GridPane gridDisplayLostLocation = new GridPane();
		gridDisplayLostLocation.getColumnConstraints().addAll(col1, col2, col3, col4);
		gridDisplayLostLocation.setPadding(new Insets(50, 50, 50, 50));
		gridDisplayLostLocation.setVgap(15);
		gridDisplayLostLocation.setHgap(15);

		//Title
		Text title = new Text("REPORT: All animals lost in a certain location.");
		title.setFont(Font.font("default", FontWeight.BOLD, 30));
		GridPane.setConstraints(title, 0, 0, 4, 1, HPos.CENTER, VPos.BASELINE);

		TableView <Animal> table = new TableView<Animal>();
		GridPane.setConstraints(table, 0, 3, 4, 1);

		Label location = new Label("Enter location in text field and press search:");
		location.setFont(Font.font("default", FontPosture.ITALIC, 18));
		GridPane.setConstraints(location, 0, 1, 4, 1, HPos.CENTER, VPos.BASELINE);

		Label locLabel = new Label("Enter Location:");
		GridPane.setConstraints(locLabel, 0, 2);

		TextField locationInput = new TextField();
		GridPane.setConstraints(locationInput,1,2);

		Button locButton = new Button("Search");
		locButton.setOnAction(e->{

			String locInputByUser = locationInput.getText().toLowerCase();
			as.displayAllLostLocation(table, locInputByUser);
		});
		GridPane.setConstraints(locButton,2,2);

		Label printLabel = new Label("Press Print to print a report");
		printLabel.setFont(Font.font("default", FontPosture.ITALIC, 18));
		GridPane.setConstraints(printLabel, 0, 4, 4, 1, HPos.CENTER, VPos.BASELINE);

		Button printButton = new Button("Print Report");
		printButton.setOnAction(e->{
			String report = "REPORT: ALL ANIMALS LOST IN A SPECIFIC LOCATION (" +  locationInput.getText() + ")\n";
			as.print(report,table);
		});
		GridPane.setConstraints(printButton, 0, 5, 4, 1, HPos.CENTER, VPos.BASELINE);

		gridDisplayLostLocation.getChildren().addAll(title,location,locLabel,locationInput,locButton,table, printLabel ,printButton);
		root.setCenter(gridDisplayLostLocation);
	}

	private void displayAllAdoption() {

		ColumnConstraints col1 = new ColumnConstraints();
		col1.setPercentWidth(20);
		ColumnConstraints col2 = new ColumnConstraints();
		col2.setPercentWidth(30);
		ColumnConstraints col3 = new ColumnConstraints();
		col3.setPercentWidth(20);
		ColumnConstraints col4 = new ColumnConstraints();
		col4.setPercentWidth(30);
		GridPane gridDisplayAdoption = new GridPane();
		gridDisplayAdoption.getColumnConstraints().addAll(col1, col2, col3, col4);
		gridDisplayAdoption.setPadding(new Insets(50, 50, 50, 50));
		gridDisplayAdoption.setVgap(15);
		gridDisplayAdoption.setHgap(15);

		//Title
		Text title = new Text("All the adoption animals in the system:");
		title.setFont(Font.font("default", FontWeight.BOLD, 30));
		GridPane.setConstraints(title, 0, 0, 4, 1, HPos.CENTER, VPos.BASELINE);

		//Search results text selection area
		TableView <Animal> tableA = new TableView<Animal>();
		as.displayAllAdoption(tableA);
		GridPane.setConstraints(tableA, 0, 1, 4, 1);

		//Remove by ID label
		Label removeID = new Label("If animal gets adopted enter ID and owner details below to Remove:");
		removeID.setFont(Font.font("default", FontPosture.ITALIC, 18));
		GridPane.setConstraints(removeID, 2, 2, 2, 1, HPos.CENTER, VPos.BASELINE);

		//Animal ID Label
		Label IDLabel = new Label("Animal ID");
		GridPane.setConstraints(IDLabel,2,3);

		//Animal ID Input
		TextField IDInput = new TextField(){
			/*override the replaceText and replaceSelection methods
			 * to make sure that only a the characters 0-9 can be entered
			 * in this field. This will avoid exceptions when we try to remove
			 * by ID and the input isn't an int*/
			@Override public void replaceText(int start, int end, String text) {
				if (text.matches("[0-9]*")) {
					super.replaceText(start, end, text);
				}
			}

			@Override public void replaceSelection(String text) {
				if (text.matches("[0-9]*")) {
					super.replaceSelection(text);
				}
			}
		};
		GridPane.setConstraints(IDInput, 3, 3);

		//Owner Name label
		Label ownerNameLabel = new Label("Owner Name:");
		GridPane.setConstraints(ownerNameLabel, 2, 4);

		//Owner Name Input
		TextField ownerNameInput = new TextField();
		GridPane.setConstraints(ownerNameInput, 3, 4);

		//Owner Address Label
		Label oAddLabel = new Label("Address:");
		GridPane.setConstraints(oAddLabel, 2, 5);

		//OwnerAddress Input
		TextField oAddInput = new TextField();
		GridPane.setConstraints(oAddInput, 3, 5);

		//owner phone label
		Label oPhoneLabel = new Label("Phone Number:");
		GridPane.setConstraints(oPhoneLabel, 2, 6);

		//owner phone input
		TextField oPhoneInput = new TextField();
		GridPane.setConstraints(oPhoneInput, 3, 6);

		//owner email label
		Label oEmailLabel = new Label("Email Address:");
		GridPane.setConstraints(oEmailLabel, 2, 7);

		//owner email input
		TextField oEmailInput = new TextField();
		handleEmail( oEmailInput);
		GridPane.setConstraints(oEmailInput, 3, 7);

		//remove / cancel buttons
		Button remove = new Button("Remove Animal");
		remove.setOnAction(e-> {

			//if email is correct, try and remove animal
			if (as.validateEmail(oEmailInput)){
				int ID = Integer.parseInt(IDInput.getText());
				Animal a = as.findAnimal(ID);
				if (a.getCat().getStat().equals("In Training")){
					AlertBox.display("Animal not ready", "Animal can't be removed as it's still in training");
				}
				else{
					if (as.removeAnimal(a))
					{
						AlertBox.display("Remove Animal", "Animal removed successfully");
						displayAllAdoption();
					}
					else
					{
						AlertBox.display("Remove Animal", "It didn't work :-(");
					}
					Person p = new Person(ownerNameInput.getText(), oAddInput.getText(), oPhoneInput.getText(), oEmailInput.getText());
					as.addSponsorFound(p);
				}
			}else{
				AlertBox.display("Incorrect Email", "Please enter a valid email address");
			}
		});
		Button cancel = new Button("Cancel");
		cancel.setOnAction(e->{root.setCenter(imgView);});
		HBox buttons = new HBox(30, remove, cancel);
		GridPane.setConstraints(buttons, 3, 8);

		//Display Selected label
		Label displaySelected = new Label("Display Selected Animal details below:");
		displaySelected.setFont(Font.font("default", FontPosture.ITALIC, 18));
		GridPane.setConstraints(displaySelected, 0, 2, 2, 1, HPos.CENTER, VPos.BASELINE);

		//Display Details button
		Button display = new Button("Display Animal Details");
		display.setOnAction(e->{
			Animal animal = tableA.getSelectionModel().getSelectedItem();
			displayDetails(animal);
		});
		GridPane.setConstraints(display, 0, 3, 2, 1, HPos.CENTER, VPos.BASELINE);

		//Edit Selected label
		Label editSelected = new Label("Edit Selected Animal details below:");
		editSelected.setFont(Font.font("default", FontPosture.ITALIC, 18));
		GridPane.setConstraints(editSelected, 0, 4, 2, 1, HPos.CENTER, VPos.BASELINE);

		//neutered label
		Label neuteredLabel = new Label("Neutered?:");
		GridPane.setConstraints(neuteredLabel, 0, 5);

		//neutered Radio Buttons
		RadioButton neutYes = new RadioButton("Yes");
		RadioButton neutNo = new RadioButton("No");
		ToggleGroup neutGroup = new ToggleGroup();
		neutYes.setToggleGroup(neutGroup);
		neutNo.setToggleGroup(neutGroup);
		HBox neutBox = new HBox(neutYes,neutNo);
		GridPane.setConstraints(neutBox, 1, 5);

		//chipped label
		Label chippedLabel = new Label("Chipped?:");
		GridPane.setConstraints(chippedLabel, 0, 6);

		//chipped Radio Buttons
		RadioButton chipYes = new RadioButton("Yes");
		RadioButton chipNo = new RadioButton("No");
		ToggleGroup chipGroup = new ToggleGroup();
		chipYes.setToggleGroup(chipGroup);
		chipNo.setToggleGroup(chipGroup);
		HBox chipBox = new HBox(chipYes,chipNo);
		GridPane.setConstraints(chipBox, 1, 6);

		//vaccinated label
		Label vacLabel = new Label("Vaccinated?:");
		GridPane.setConstraints(vacLabel, 0, 7);

		//vaccinated Radio Buttons
		RadioButton vacYes = new RadioButton("Yes");
		RadioButton vacNo = new RadioButton("No");
		ToggleGroup vacGroup = new ToggleGroup();
		vacYes.setToggleGroup(vacGroup);
		vacNo.setToggleGroup(vacGroup);
		HBox vacBox = new HBox(vacYes,vacNo);
		GridPane.setConstraints(vacBox, 1, 7);

		//reserved label
		Label resLabel = new Label("Reserved?:");
		GridPane.setConstraints(resLabel, 0, 8);

		//reserverd Radio Buttons
		RadioButton resYes = new RadioButton("Yes");
		RadioButton resNo = new RadioButton("No");
		ToggleGroup resGroup = new ToggleGroup();
		resYes.setToggleGroup(resGroup);
		resNo.setToggleGroup(resGroup);
		HBox resBox = new HBox(resYes,resNo);
		GridPane.setConstraints(resBox, 1, 8);

		//Status label
		Label statLabel = new Label("Status:");
		GridPane.setConstraints(statLabel, 0, 9);

		//Status Radio Buttons
		RadioButton statTrain = new RadioButton("In Training");
		RadioButton statReady = new RadioButton("Ready");
		ToggleGroup statGroup = new ToggleGroup();
		statTrain.setToggleGroup(statGroup);
		statReady.setToggleGroup(statGroup);
		HBox statBox = new HBox(statTrain,statReady);
		GridPane.setConstraints(statBox, 1, 9);

		//edit / cancel buttons
		Button edit = new Button("Edit Adoption Animal");
		edit.setOnAction(e-> {

			Animal animal = tableA.getSelectionModel().getSelectedItem();
			RadioButton stat = (RadioButton)statTrain.getToggleGroup().getSelectedToggle();
			String status = stat.getText();
			boolean neutered;
			if (neutYes.isSelected()){
				neutered = true;
			}
			else{
				neutered = false;
			}
			boolean chipped;
			if (chipYes.isSelected()){
				chipped = true;
			}
			else{
				chipped = false;
			}
			boolean vaccinated;
			if (vacYes.isSelected()){
				vaccinated = true;
			}
			else{
				vaccinated = false;
			}
			boolean reserved;
			if (resYes.isSelected()){
				reserved = true;
			}
			else{
				reserved = false;
			}
			LocalDate date = animal.getCat().getDate();
			Adoption adoption = new Adoption(date,neutered,chipped,vaccinated,status,reserved);
			if (as.editAdoptionDetails(animal, adoption))
			{
				AlertBox.display("Edit Adoption Animal", "Animal status changed successfully");
			}
			else
			{
				AlertBox.display("Edit Adoption Animal", "It didn't work :-(");
			}

		});
		Button cancelEdit = new Button("Cancel Edit");
		cancelEdit.setOnAction(e->{root.setCenter(imgView);});
		HBox editButtons = new HBox(30, edit, cancelEdit);
		GridPane.setConstraints(editButtons, 1, 10, 2, 1, HPos.CENTER, VPos.BASELINE);



		gridDisplayAdoption.getChildren().addAll(title, tableA, removeID, IDLabel, IDInput, buttons, ownerNameLabel, ownerNameInput, oAddLabel,
				oAddInput, oPhoneLabel, oPhoneInput, oEmailLabel, oEmailInput, displaySelected, display, editSelected, neuteredLabel, neutBox, 
				chippedLabel, chipBox, resLabel, resBox, statLabel, statBox, vacLabel, vacBox, editButtons);
		root.setCenter(gridDisplayAdoption);
	}

	private void displayAllAnimalsByAge() {
		ColumnConstraints col1 = new ColumnConstraints();
		col1.setPercentWidth(25);
		ColumnConstraints col2 = new ColumnConstraints();
		col2.setPercentWidth(25);
		ColumnConstraints col3 = new ColumnConstraints();
		col3.setPercentWidth(25);
		ColumnConstraints col4 = new ColumnConstraints();
		col4.setPercentWidth(25);
		GridPane gridAllByAge = new GridPane();
		gridAllByAge.getColumnConstraints().addAll(col1, col2, col3, col4);
		gridAllByAge.setPadding(new Insets(50, 50, 50, 50));
		gridAllByAge.setVgap(15);
		gridAllByAge.setHgap(15);

		//Title
		Text title = new Text("All the animals in the shelter displayed by age:");
		title.setFont(Font.font("default", FontWeight.BOLD, 30));
		GridPane.setConstraints(title, 0, 0, 4, 1, HPos.CENTER, VPos.BASELINE);

		//Search results table
		TableView <Animal>table = new TableView<Animal>();
		as.displayAllByAge(table);
		GridPane.setConstraints(table, 0, 1, 4, 1);	

		//Display Selected label
		Label displaySelected = new Label("Display Selected Animal details below:");
		displaySelected.setFont(Font.font("default", FontPosture.ITALIC, 20));
		GridPane.setConstraints(displaySelected, 2, 2, 2, 1, HPos.CENTER, VPos.BASELINE);

		//Button Label
		Label buttonLabel = new Label("Select animal then press button:");
		GridPane.setConstraints(buttonLabel, 2, 3, 2, 1, HPos.CENTER, VPos.BASELINE);

		//Display Details button
		Button display = new Button("Display Animal Details");
		display.setOnAction(e->{
			Animal animal = table.getSelectionModel().getSelectedItem();
			displayDetails(animal);
		});
		GridPane.setConstraints(display, 2, 4, 2, 1, HPos.CENTER, VPos.BASELINE);

		//Print label
		Label printReport = new Label("Print a report below:");
		printReport.setFont(Font.font("default", FontPosture.ITALIC, 20));
		GridPane.setConstraints(printReport, 0, 2, 2, 1, HPos.CENTER, VPos.BASELINE);

		//Print Button
		Button print = new Button("Print Report");
		print.setOnAction(e->{
			String report = "ALL THE ANIMALS IN THE SHELTER ORDERED BY AGE:\n";
			as.print(report,table);
		});
		GridPane.setConstraints(print, 0, 4, 2, 1, HPos.CENTER, VPos.BASELINE);

		gridAllByAge.getChildren().addAll(title, table, displaySelected, display, buttonLabel, print, printReport);
		root.setCenter(gridAllByAge);
	}

	private void transferAnimal() {
		ColumnConstraints col1 = new ColumnConstraints();
		col1.setPercentWidth(25);
		ColumnConstraints col2 = new ColumnConstraints();
		col2.setPercentWidth(25);
		ColumnConstraints col3 = new ColumnConstraints();
		col3.setPercentWidth(25);
		ColumnConstraints col4 = new ColumnConstraints();
		col4.setPercentWidth(25);
		GridPane gridTransfer = new GridPane();
		gridTransfer.getColumnConstraints().addAll(col1, col2, col3, col4);
		gridTransfer.setPadding(new Insets(50, 50, 50, 50));
		gridTransfer.setVgap(15);
		gridTransfer.setHgap(15);

		//Title
		Text title = new Text("All the found animals in the shelter for more than 30 days:");
		title.setFont(Font.font("default", FontWeight.BOLD, 30));
		GridPane.setConstraints(title, 0, 0, 4, 1, HPos.CENTER, VPos.BASELINE);

		//Search results table
		TableView <Animal>tableT = new TableView<Animal>();
		as.displayForTransfer(tableT);
		GridPane.setConstraints(tableT, 0, 1, 4, 1);	

		//Remove by ID label
		Label selectLabel = new Label("Select Animal from table and enter Adoption details below:");
		selectLabel.setFont(Font.font("default", FontPosture.ITALIC, 20));
		GridPane.setConstraints(selectLabel, 0, 2, 4,  1, HPos.CENTER, VPos.BASELINE);

		//Name Label
		Label nameLabel = new Label("Name (If not already present):");
		GridPane.setConstraints(nameLabel, 0, 3);

		//Name Input
		TextField nameInput = new TextField();
		GridPane.setConstraints(nameInput, 1, 3);

		//Picture Label
		Label picLabel = new Label("Add a picture:");
		GridPane.setConstraints(picLabel, 2, 3);

		//Picture Button chooser
		ImageView displayPic = new ImageView();
		FileChooser.ExtensionFilter imageFilter = new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.png");
		FileChooser fc = new FileChooser();
		fc.getExtensionFilters().add(imageFilter);
		Button btn = new Button();
		btn.setText("Choose Picture");
		btn.setOnAction((ActionEvent event) -> {
			File pic = fc.showOpenDialog(window);
			try {
				String imageFile = pic.toURI().toURL().toString();
				Image image = new Image(imageFile, 100, 100, true, true);
				displayPic.setImage(image);
			} catch (Exception e) {
				root.setCenter(gridTransfer);
			} 
		});
		GridPane.setConstraints(btn, 3, 3);

		//Display Picture if selected
		GridPane.setConstraints(displayPic, 2, 4, 2, 4);

		//neutered label
		Label neuteredLabel = new Label("Neutered?:");
		GridPane.setConstraints(neuteredLabel, 0, 4);

		//neutered Radio Buttons
		RadioButton neutYes = new RadioButton("Yes");
		RadioButton neutNo = new RadioButton("No");
		ToggleGroup neutGroup = new ToggleGroup();
		neutYes.setToggleGroup(neutGroup);
		neutNo.setToggleGroup(neutGroup);
		HBox neutBox = new HBox(neutYes,neutNo);
		GridPane.setConstraints(neutBox, 1, 4);

		//chipped label
		Label chippedLabel = new Label("Chipped?:");
		GridPane.setConstraints(chippedLabel, 0, 5);

		//chipped Radio Buttons
		RadioButton chipYes = new RadioButton("Yes");
		RadioButton chipNo = new RadioButton("No");
		ToggleGroup chipGroup = new ToggleGroup();
		chipYes.setToggleGroup(chipGroup);
		chipNo.setToggleGroup(chipGroup);
		HBox chipBox = new HBox(chipYes,chipNo);
		GridPane.setConstraints(chipBox, 1, 5);

		//vaccinated label
		Label vacLabel = new Label("Vaccinated?:");
		GridPane.setConstraints(vacLabel, 0, 6);

		//vaccinated Radio Buttons
		RadioButton vacYes = new RadioButton("Yes");
		RadioButton vacNo = new RadioButton("No");
		ToggleGroup vacGroup = new ToggleGroup();
		vacYes.setToggleGroup(vacGroup);
		vacNo.setToggleGroup(vacGroup);
		HBox vacBox = new HBox(vacYes,vacNo);
		GridPane.setConstraints(vacBox, 1, 6);

		//reserved label
		Label resLabel = new Label("Reserved?:");
		GridPane.setConstraints(resLabel, 0, 7);

		//reserverd Radio Buttons
		RadioButton resYes = new RadioButton("Yes");
		RadioButton resNo = new RadioButton("No");
		ToggleGroup resGroup = new ToggleGroup();
		resYes.setToggleGroup(resGroup);
		resNo.setToggleGroup(resGroup);
		HBox resBox = new HBox(resYes,resNo);
		GridPane.setConstraints(resBox, 1, 7);

		//Status label
		Label statLabel = new Label("Status:");
		GridPane.setConstraints(statLabel, 0, 8);

		//Status Radio Buttons
		RadioButton statTrain = new RadioButton("In Training");
		RadioButton statReady = new RadioButton("Ready");
		ToggleGroup statGroup = new ToggleGroup();
		statTrain.setToggleGroup(statGroup);
		statReady.setToggleGroup(statGroup);
		HBox statBox = new HBox(statTrain,statReady);
		GridPane.setConstraints(statBox, 1, 8);

		//transfer Button
		Button transferAnimal = new Button("Transfer to Adoption");
		transferAnimal.setOnAction(e->{

			if (nameInput.getText().isEmpty()){
				AlertBox.display("Name field Empty", "Please either write a new name or confirm the old one");
			}
			else{
				Animal animal = tableT.getSelectionModel().getSelectedItem();
				Image img = displayPic.getImage();
				if (img == null)
					img = new Image("file:paw.png",100,100, true, true);
				String name = nameInput.getText();
				RadioButton stat = (RadioButton)statTrain.getToggleGroup().getSelectedToggle();
				String status = stat.getText();
				boolean neutered;
				if (neutYes.isSelected()){
					neutered = true;
				}
				else{
					neutered = false;
				}
				boolean chipped;
				if (chipYes.isSelected()){
					chipped = true;
				}
				else{
					chipped = false;
				}
				boolean vaccinated;
				if (vacYes.isSelected()){
					vaccinated = true;
				}
				else{
					vaccinated = false;
				}
				boolean reserved;
				if (resYes.isSelected()){
					reserved = true;
				}
				else{
					reserved = false;
				}

				LocalDate date = LocalDate.now();
				Adoption adoption = new Adoption(date,neutered,chipped,vaccinated,status,reserved);
				Category newCat = adoption;
				if(as.transferAnimal(animal, name, img, newCat))
				{
					AlertBox.display("Transfer Animal", "Animal transfered successfully");
					transferAnimal();
				}
				else
				{
					AlertBox.display("Transfer Animal", "It didn't work :-(");
				}
			}
		});

		//Cancel Button
		Button cancelTransfer = new Button("Cancel");
		cancelTransfer.setOnAction(e->root.setCenter(imgView));

		//HBox for Buttons
		HBox buttons = new HBox(30, transferAnimal, cancelTransfer);
		GridPane.setConstraints(buttons, 3, 10);

		gridTransfer.getChildren().addAll(title, tableT,selectLabel, nameLabel, nameInput, picLabel, btn, neuteredLabel, neutBox,
				chippedLabel, chipBox, vacLabel, vacBox, resLabel, resBox,statLabel, statBox, displayPic, buttons);
		root.setCenter(gridTransfer);


	}

	private void displayAllFound() 
	{
		ColumnConstraints col1 = new ColumnConstraints();
		col1.setPercentWidth(20);
		ColumnConstraints col2 = new ColumnConstraints();
		col2.setPercentWidth(30);
		ColumnConstraints col3 = new ColumnConstraints();
		col3.setPercentWidth(20);
		ColumnConstraints col4 = new ColumnConstraints();
		col4.setPercentWidth(30);
		GridPane gridDisplayFound = new GridPane();
		gridDisplayFound.getColumnConstraints().addAll(col1, col2, col3, col4);
		gridDisplayFound.setPadding(new Insets(50, 50, 50, 50));
		gridDisplayFound.setVgap(15);
		gridDisplayFound.setHgap(15);

		//Title
		Text title = new Text("All the found animals in the system:");
		title.setFont(Font.font("default", FontWeight.BOLD, 30));
		GridPane.setConstraints(title, 0, 0, 4, 1, HPos.CENTER, VPos.BASELINE);

		//Search results text selection area
		TableView <Animal> tablef = new TableView<Animal>();
		as.displayAllFound(tablef);
		GridPane.setConstraints(tablef, 0, 1, 4, 1);	

		//Remove by ID label
		Label removeID = new Label("Remove by ID below and enter owner details:");
		removeID.setFont(Font.font("default", FontPosture.ITALIC, 20));
		GridPane.setConstraints(removeID, 2, 2, 2, 1, HPos.CENTER, VPos.BASELINE);

		//Animal ID Label
		Label IDLabel = new Label("Animal ID");
		GridPane.setConstraints(IDLabel,2,3);

		//Animal ID Input
		TextField IDInput = new TextField(){
			/*override the replaceText and replaceSelection methods
			 * to make sure that only a the characters 0-9 can be entered
			 * in this field. This will avoid exceptions when we try to remove
			 * by ID and the input isn't an int*/
			@Override public void replaceText(int start, int end, String text) {
				if (text.matches("[0-9]*")) {
					super.replaceText(start, end, text);
				}
			}

			@Override public void replaceSelection(String text) {
				if (text.matches("[0-9]*")) {
					super.replaceSelection(text);
				}
			}
		};
		GridPane.setConstraints(IDInput, 3, 3);

		//Owner Name label
		Label ownerNameLabel = new Label("Owner Name:");
		GridPane.setConstraints(ownerNameLabel, 2, 4);

		//Owner Name Input
		TextField ownerNameInput = new TextField();
		GridPane.setConstraints(ownerNameInput, 3, 4);

		//Owner Address Label
		Label oAddLabel = new Label("Address:");
		GridPane.setConstraints(oAddLabel, 2, 5);

		//OwnerAddress Input
		TextField oAddInput = new TextField();
		GridPane.setConstraints(oAddInput, 3, 5);

		//owner phone label
		Label oPhoneLabel = new Label("Phone Number:");
		GridPane.setConstraints(oPhoneLabel, 2, 6);

		//owner phone input
		TextField oPhoneInput = new TextField();
		GridPane.setConstraints(oPhoneInput, 3, 6);

		//owner email label
		Label oEmailLabel = new Label("Email Address:");
		GridPane.setConstraints(oEmailLabel, 2, 7);

		//owner email input
		TextField oEmailInput = new TextField();
		handleEmail( oEmailInput);
		GridPane.setConstraints(oEmailInput, 3, 7);

		//remove / cancel buttons
		Button remove = new Button("Remove Animal");
		remove.setOnAction(e-> {
			//if email is correct, try and remove animal
			if (as.validateEmail(oEmailInput)){
				int ID = Integer.parseInt(IDInput.getText());
				Animal a = as.findAnimal(ID);
				if (as.removeAnimal(a))
				{
					AlertBox.display("Remove Animal", "Animal removed successfully");
					displayAllFound();
				}
				else
				{
					AlertBox.display("Remove Animal", "It didn't work :-(");
				}
				Person p = new Person(ownerNameInput.getText(), oAddInput.getText(), oPhoneInput.getText(), oEmailInput.getText());
				as.addSponsorFound(p);
			}
			else{
				AlertBox.display("Email incorrect", "Please enter valid email");
			}
		});
		Button cancel = new Button("Cancel");
		cancel.setOnAction(e->{root.setCenter(imgView);});
		HBox buttons = new HBox(30, remove, cancel);
		GridPane.setConstraints(buttons, 3, 8);

		//Display Selected label
		Label displaySelected = new Label("Display Selected Animal details below:");
		displaySelected.setFont(Font.font("default", FontPosture.ITALIC, 20));
		GridPane.setConstraints(displaySelected, 0, 2, 2, 1, HPos.CENTER, VPos.BASELINE);

		//Button Label
		Label buttonLabel = new Label("Select animal in table then press button");
		GridPane.setConstraints(buttonLabel, 0, 4, 2, 1, HPos.CENTER, VPos.BASELINE);

		//Display Details button
		Button display = new Button("Display Animal Details");
		display.setOnAction(e->{
			Animal animal = tablef.getSelectionModel().getSelectedItem();
			displayDetails(animal);
		});
		GridPane.setConstraints(display, 0, 5, 2, 1, HPos.CENTER, VPos.BASELINE);

		gridDisplayFound.getChildren().addAll(title, tablef, removeID, IDLabel, IDInput, buttons, ownerNameLabel, ownerNameInput, oAddLabel,
				oAddInput, oPhoneLabel, oPhoneInput, oEmailLabel, oEmailInput, displaySelected, display, buttonLabel);
		root.setCenter(gridDisplayFound);
	}

	public void displayAllLost()
	{
		ColumnConstraints col1 = new ColumnConstraints();
		col1.setPercentWidth(25);
		ColumnConstraints col2 = new ColumnConstraints();
		col2.setPercentWidth(25);
		ColumnConstraints col3 = new ColumnConstraints();
		col3.setPercentWidth(25);
		ColumnConstraints col4 = new ColumnConstraints();
		col4.setPercentWidth(25);
		GridPane gridDisplayLost = new GridPane();
		gridDisplayLost.getColumnConstraints().addAll(col1, col2, col3, col4);
		gridDisplayLost.setPadding(new Insets(50, 50, 50, 50));
		gridDisplayLost.setVgap(15);
		gridDisplayLost.setHgap(15);

		//Title
		Text title = new Text("All the lost animals in the system:");
		title.setFont(Font.font("default", FontWeight.BOLD, 30));
		GridPane.setConstraints(title, 0, 0, 4, 1, HPos.CENTER, VPos.BASELINE);

		//Search results table
		TableView <Animal>tablel = new TableView<Animal>();
		as.displayAllLost(tablel);
		GridPane.setConstraints(tablel, 0, 1, 4, 1);	

		//Remove by ID label
		Label removeID = new Label("Remove by ID below:");
		removeID.setFont(Font.font("default", FontPosture.ITALIC, 20));
		GridPane.setConstraints(removeID, 2, 2, 2, 1, HPos.CENTER, VPos.BASELINE);

		//Animal ID Label
		Label IDLabel = new Label("Animal ID");
		GridPane.setConstraints(IDLabel,2,3);

		//Animal ID Input
		TextField IDInput = new TextField(){
			/*override the replaceText and replaceSelection methods
			 * to make sure that only a the characters 0-9 can be entered
			 * in this field. This will avoid exceptions when we try to remove
			 * by ID and the input isn't an int*/
			@Override public void replaceText(int start, int end, String text) {
				if (text.matches("[0-9]*")) {
					super.replaceText(start, end, text);
				}
			}

			@Override public void replaceSelection(String text) {
				if (text.matches("[0-9]*")) {
					super.replaceSelection(text);
				}
			}
		};
		GridPane.setConstraints(IDInput, 3, 3);

		//remove / cancel buttons
		Button remove = new Button("Remove Animal");
		remove.setOnAction(e-> {

			int ID = Integer.parseInt(IDInput.getText());
			Animal a = as.findAnimal(ID);
			as.addSponsor(a);
			if(as.removeAnimal(a))
			{
				AlertBox.display("Remove Animal", "Animal removed successfully");
				displayAllLost();
			}
			else
			{
				AlertBox.display("Remove Animal", "It didn't work :-(");
			}
		});
		Button cancel = new Button("Cancel");
		cancel.setOnAction(e->{root.setCenter(imgView);});
		HBox buttons = new HBox(30, remove, cancel);
		GridPane.setConstraints(buttons, 3, 4);

		//Display Selected label
		Label displaySelected = new Label("Display Selected Animal details below:");
		displaySelected.setFont(Font.font("default", FontPosture.ITALIC, 20));
		GridPane.setConstraints(displaySelected, 0, 2, 2, 1, HPos.CENTER, VPos.BASELINE);

		//Button Label
		Label buttonLabel = new Label("Select animal in table then press button");
		GridPane.setConstraints(buttonLabel, 0, 3, 2, 1, HPos.CENTER, VPos.BASELINE);

		//Display Details button
		Button display = new Button("Display Animal Details");
		display.setOnAction(e->{
			Animal animal = tablel.getSelectionModel().getSelectedItem();
			displayDetails(animal);
		});
		GridPane.setConstraints(display, 0, 4, 2, 1, HPos.CENTER, VPos.BASELINE);

		gridDisplayLost.getChildren().addAll(title, tablel, removeID, IDLabel, IDInput, buttons, displaySelected, buttonLabel, display);
		root.setCenter(gridDisplayLost);
	}

	public void foundAdd()
	{

		ColumnConstraints col1 = new ColumnConstraints();
		col1.setPercentWidth(20);
		ColumnConstraints col2 = new ColumnConstraints();
		col2.setPercentWidth(30);
		ColumnConstraints col3 = new ColumnConstraints();
		col3.setPercentWidth(20);
		ColumnConstraints col4 = new ColumnConstraints();
		col4.setPercentWidth(30);
		GridPane gridFoundAdd = new GridPane();
		gridFoundAdd.getColumnConstraints().addAll(col1, col2, col3, col4);
		gridFoundAdd.setPadding(new Insets(50, 50, 50, 50));
		gridFoundAdd.setVgap(15);
		gridFoundAdd.setHgap(15);

		//Title
		Text addNewAnimal = new Text("Add a found Animal to the system");
		addNewAnimal.setFont(Font.font("default", FontWeight.BOLD, 30));
		GridPane.setConstraints(addNewAnimal, 0, 0, 4, 1, HPos.CENTER, VPos.BASELINE);

		//Add Animal Label
		Label addAnimalLabel = new Label("Add an Animal below:");
		addAnimalLabel.setFont(Font.font("default", FontPosture.ITALIC, 20));
		GridPane.setConstraints(addAnimalLabel, 0, 1, 4, 1, HPos.CENTER, VPos.BASELINE);

		//Name Label
		Label nameLabel = new Label("Name (If present):");
		GridPane.setConstraints(nameLabel, 0, 2);

		//Name Input
		TextField nameInput = new TextField();
		GridPane.setConstraints(nameInput, 1, 2);

		//Type label
		Label typeLabel = new Label("Type:");
		GridPane.setConstraints(typeLabel, 0, 3);

		//Type combo box
		ComboBox <String> typeChoice = new ComboBox<String>();
		as.populateTypes(typeChoice);
		typeChoice.setPromptText("Choose a Type");
		GridPane.setConstraints(typeChoice, 1, 3);

		//Breed Label
		Label breedLabel = new Label("Breed:");
		GridPane.setConstraints(breedLabel, 0, 4);

		//Breed Input
		TextField breedInput = new TextField();
		GridPane.setConstraints(breedInput, 1, 4);

		//Colour Label
		Label colourLabel = new Label("Colour:");
		GridPane.setConstraints(colourLabel, 0, 5);

		//ColourInput
		TextField colourInput = new TextField();
		GridPane.setConstraints(colourInput, 1, 5);

		//Description Label
		Label descriptionLabel = new Label("Description:");
		GridPane.setConstraints(descriptionLabel, 2,2);

		//Description Input
		TextField descriptionInput = new TextField();
		GridPane.setConstraints(descriptionInput, 3,2);

		//Gender label
		Label genderLabel = new Label("Gender:");
		GridPane.setConstraints(genderLabel, 2,3);

		//Gender Radio Buttons
		RadioButton genderMale = new RadioButton("Male");
		RadioButton genderFemale = new RadioButton("Female");
		ToggleGroup genderGroup = new ToggleGroup();
		genderMale.setToggleGroup(genderGroup);
		genderFemale.setToggleGroup(genderGroup);
		HBox genderBox = new HBox(genderMale,genderFemale);
		GridPane.setConstraints(genderBox, 3,3);



		//Location Label
		Label locationLabel = new Label("Location found");
		GridPane.setConstraints(locationLabel, 2,4);

		//Location Input
		TextField locationInput = new TextField();
		GridPane.setConstraints(locationInput, 3,4);

		//Date Label
		Label dateLabel = new Label("Date found");
		GridPane.setConstraints(dateLabel, 2,5);

		//Date Picker
		DatePicker datePick = new DatePicker();
		HBox dateBox = new HBox (datePick);
		GridPane.setConstraints(dateBox, 3,5);

		//Add Button
		Button addAnimal = new Button();
		addAnimal.setText("Add Animal");
		addAnimal.setOnAction(e -> 

		{

			String colour = colourInput.getText();
			boolean gender;
			if (genderMale.isSelected()){
				gender = true;
			}
			else{
				gender = false;
			}
			Image img = new Image("file:paw.png",100,100, true, true);
			String description = descriptionInput.getText();
			String name = nameInput.getText();
			String breed = breedInput.getText();
			String type = typeChoice.getValue();
			Found found = new Found();
			found.setDate(datePick.getValue());
			found.setLocation(locationInput.getText());
			Category cat = found;
			Animal animal = new Animal();
			animal.setPicture(img);
			animal.setColour(colour);
			animal.setGender(gender);
			animal.setDescription(description);
			animal.setName(name);
			animal.setBreed(breed);
			animal.setType(type);
			animal.setCat(cat);

			if(as.addAnimal(animal))
			{
				AlertBox.display("Add Animal", "Animal added successfully");
				foundAdd();
			}
			else
			{
				AlertBox.display("Add Animal", "It didn't work :-(");
			}
		});


		//Cancel Button
		Button cancelAdd = new Button();
		cancelAdd.setText("Cancel");
		cancelAdd.setOnAction(e->{root.setCenter(imgView);});


		//HBox for Buttons
		HBox buttons = new HBox(30, addAnimal, cancelAdd);
		GridPane.setConstraints(buttons, 3, 10);


		//Add everything to grid
		gridFoundAdd.getChildren().addAll(addNewAnimal,addAnimalLabel, nameLabel, nameInput, typeLabel, typeChoice,  breedLabel, breedInput,
				colourLabel, colourInput, descriptionLabel, descriptionInput, genderLabel, genderBox, locationLabel, locationInput, dateLabel, 
				buttons, dateBox);

		root.setCenter(gridFoundAdd);


	}

	public void lostAdd()
	{


		ColumnConstraints col1 = new ColumnConstraints();
		col1.setPercentWidth(20);
		ColumnConstraints col2 = new ColumnConstraints();
		col2.setPercentWidth(30);
		ColumnConstraints col3 = new ColumnConstraints();
		col3.setPercentWidth(20);
		ColumnConstraints col4 = new ColumnConstraints();
		col4.setPercentWidth(30);
		GridPane gridLostAdd = new GridPane();
		gridLostAdd.getColumnConstraints().addAll(col1, col2, col3, col4);
		gridLostAdd.setPadding(new Insets(50, 50, 50, 50));
		gridLostAdd.setVgap(15);
		gridLostAdd.setHgap(15);

		//Title
		Text addNewAnimal = new Text("Add a lost Animal to the system");
		addNewAnimal.setFont(Font.font("default", FontWeight.BOLD, 30));
		GridPane.setConstraints(addNewAnimal, 0, 0, 4, 1, HPos.CENTER, VPos.BASELINE);

		//Add Animal Label
		Label addAnimalLabel = new Label("Add an Animal below:");
		addAnimalLabel.setFont(Font.font("default", FontPosture.ITALIC, 20));
		GridPane.setConstraints(addAnimalLabel, 0, 1, 2, 1);

		//Name Label
		Label nameLabel = new Label("Name:");
		GridPane.setConstraints(nameLabel, 0, 2);

		//Name Input
		TextField nameInput = new TextField();
		GridPane.setConstraints(nameInput, 1, 2);

		//Type label
		Label typeLabel = new Label("Type:");
		GridPane.setConstraints(typeLabel, 0, 3);

		//Type combo box
		ComboBox <String> typeChoice = new ComboBox<String>();
		as.populateTypes(typeChoice);
		typeChoice.setPromptText("Choose a Type");
		GridPane.setConstraints(typeChoice, 1, 3);

		//Breed Label
		Label breedLabel = new Label("Breed:");
		GridPane.setConstraints(breedLabel, 0, 4);

		//Breed Input
		TextField breedInput = new TextField();
		GridPane.setConstraints(breedInput, 1, 4);

		//Colour Label
		Label colourLabel = new Label("Colour:");
		GridPane.setConstraints(colourLabel, 0, 5);

		//ColourInput
		TextField colourInput = new TextField();
		GridPane.setConstraints(colourInput, 1, 5);

		//Description Label
		Label descriptionLabel = new Label("Description:");
		GridPane.setConstraints(descriptionLabel, 0, 6);

		//Description Input
		TextField descriptionInput = new TextField();
		GridPane.setConstraints(descriptionInput, 1, 6);

		//Age label
		Label ageLabel = new Label("Age:");
		GridPane.setConstraints(ageLabel, 0, 7);

		//Age Input
		TextField ageInput = new TextField(){
			/*override the replaceText and replaceSelection methods
			 * to make sure that only a the characters 0-9 can be entered
			 * in this field. This will avoid exceptions when we create the animal
			 * and the age isn't an int*/
			@Override public void replaceText(int start, int end, String text) {
				if (text.matches("[0-9]*")) {
					super.replaceText(start, end, text);
				}
			}

			@Override public void replaceSelection(String text) {
				if (text.matches("[0-9]*")) {
					super.replaceSelection(text);
				}
			}
		};

		GridPane.setConstraints(ageInput, 1, 7);

		//Gender label
		Label genderLabel = new Label("Gender:");
		GridPane.setConstraints(genderLabel, 0, 8);

		//Gender Radio Buttons
		RadioButton genderMale = new RadioButton("Male");
		RadioButton genderFemale = new RadioButton("Female");
		ToggleGroup genderGroup = new ToggleGroup();
		genderMale.setToggleGroup(genderGroup);
		genderFemale.setToggleGroup(genderGroup);
		HBox genderBox = new HBox(genderMale,genderFemale);
		GridPane.setConstraints(genderBox, 1, 8);

		//Picture Label
		Label picLabel = new Label("Picture:");
		GridPane.setConstraints(picLabel, 0, 9);

		//Picture Button chooser
		ImageView displayPic = new ImageView();
		FileChooser.ExtensionFilter imageFilter = new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.png");
		FileChooser fc = new FileChooser();
		fc.getExtensionFilters().add(imageFilter);
		Button btn = new Button();
		btn.setText("Choose Picture");
		btn.setOnAction((ActionEvent event) -> {
			File pic = fc.showOpenDialog(window);
			try {
				String imageFile = pic.toURI().toURL().toString();
				Image image = new Image(imageFile, 100, 100, true, true);
				displayPic.setImage(image);
			} catch (Exception e) {
				root.setCenter(gridLostAdd);
			} 
		});
		GridPane.setConstraints(btn, 1, 9);

		//Display Picture if selected
		GridPane.setConstraints(displayPic, 2, 6, 2, 6);

		//Location Label
		Label locationLabel = new Label("Location Lost");
		GridPane.setConstraints(locationLabel, 0, 10);

		//Location Input
		TextField locationInput = new TextField();
		GridPane.setConstraints(locationInput, 1, 10);

		//Date Label
		Label dateLabel = new Label("Date Lost");
		GridPane.setConstraints(dateLabel, 0, 11);

		//Date Picker
		DatePicker datePick = new DatePicker();
		HBox dateBox = new HBox (datePick);
		GridPane.setConstraints(dateBox, 1, 11);

		//Add Owner Details Label
		Label ownerLabel = new Label("Enter owner details below:");
		ownerLabel.setFont(Font.font("Verdana", FontPosture.ITALIC, 20));
		GridPane.setConstraints(ownerLabel, 2, 1, 2, 1);

		//Owner Name label
		Label oNameLabel = new Label("Name:");
		GridPane.setConstraints(oNameLabel, 2, 2);

		//Owner Name Input
		TextField oNameInput = new TextField();
		GridPane.setConstraints(oNameInput, 3, 2);

		//Owner Address Label
		Label oAddLabel = new Label("Address:");
		GridPane.setConstraints(oAddLabel, 2, 3);

		//OwnerAddress Input
		TextField oAddInput = new TextField();
		GridPane.setConstraints(oAddInput, 3, 3);

		//owner phone label
		Label oPhoneLabel = new Label("Phone Number:");
		GridPane.setConstraints(oPhoneLabel, 2, 4);

		//owner phone input
		TextField oPhoneInput = new TextField();
		GridPane.setConstraints(oPhoneInput, 3, 4);

		//owner email label
		Label oEmailLabel = new Label("Email Address:");
		GridPane.setConstraints(oEmailLabel, 2, 5);

		//owner email input
		TextField oEmailInput = new TextField();
		handleEmail( oEmailInput);
		GridPane.setConstraints(oEmailInput, 3, 5);

		//Add Button
		Button addAnimal = new Button();
		addAnimal.setText("Add Animal");

		addAnimal.setOnAction(e -> {

			//if email is valid, try and add animal
			if (as.validateEmail(oEmailInput)){
				Image img = displayPic.getImage();
				if (img == null)
					img = new Image("file:paw.png",100,100, true, true);

				int age = Integer.parseInt(ageInput.getText());
				String colour = colourInput.getText();
				boolean gender;
				if (genderMale.isSelected()){
					gender = true;
				}
				else{
					gender = false;
				}
				String description = descriptionInput.getText();
				String name = nameInput.getText();
				String breed = breedInput.getText();
				String type = typeChoice.getValue();
				Person person = new Person(oNameInput.getText(), oAddInput.getText(), oPhoneInput.getText(), oEmailInput.getText());
				Lost lost = new Lost(datePick.getValue(),person,locationInput.getText());
				Category cat = lost;
				Animal animal = new Animal();
				person.setAnimalID(animal.getID());
				animal.setAge(age);
				animal.setColour(colour);
				animal.setGender(gender);
				animal.setDescription(description);
				animal.setName(name);
				animal.setBreed(breed);
				animal.setType(type);
				animal.setCat(cat);
				animal.setPicture(img);


				if(as.addAnimal(animal))
				{
					AlertBox.display("Add Animal", "Animal added successfully");
					lostAdd();
				}
				else
				{
					AlertBox.display("Add Animal", "It didn't work :-(");
				}
			}
			else{
				AlertBox.display("Email incorrect", "Please enter valid email");
			}

		});

		//Cancel Button
		Button cancelAdd = new Button();
		cancelAdd.setText("Cancel");
		cancelAdd.setOnAction(e->{root.setCenter(imgView);});

		//HBox for Buttons
		HBox buttons = new HBox(30, addAnimal, cancelAdd);
		GridPane.setConstraints(buttons, 3, 16);




		//Add everything to grid
		gridLostAdd.getChildren().addAll(addNewAnimal,addAnimalLabel, nameLabel, nameInput, typeLabel, typeChoice,  breedLabel, breedInput,
				colourLabel, colourInput, descriptionLabel, descriptionInput, ageLabel, ageInput, genderLabel, genderBox,
				picLabel, btn, locationLabel, locationInput, dateLabel, dateBox, ownerLabel, oNameLabel, oNameInput,
				oAddLabel, oAddInput, oPhoneLabel, oPhoneInput, oEmailLabel, oEmailInput, displayPic, buttons);

		root.setCenter(gridLostAdd);

	}

	public void displaySponsors()
	{
		ColumnConstraints col1 = new ColumnConstraints();
		col1.setPercentWidth(25);
		ColumnConstraints col2 = new ColumnConstraints();
		col2.setPercentWidth(25);
		ColumnConstraints col3 = new ColumnConstraints();
		col3.setPercentWidth(25);
		ColumnConstraints col4 = new ColumnConstraints();
		col4.setPercentWidth(25);
		GridPane gridSponsors = new GridPane();
		gridSponsors.getColumnConstraints().addAll(col1, col2, col3, col4);
		gridSponsors.setPadding(new Insets(50, 50, 50, 50));
		gridSponsors.setVgap(15);
		gridSponsors.setHgap(15);

		//Title
		Text title = new Text("All the possible Sponsors in the system:");
		title.setFont(Font.font("default", FontWeight.BOLD, 30));
		GridPane.setConstraints(title, 0, 0, 4, 1, HPos.CENTER, VPos.BASELINE);

		//Search results table
		TableView<Person> tableS = new TableView<Person>();
		as.displayAllSponsors(tableS);
		GridPane.setConstraints(tableS, 0, 1, 4, 1);	

		Button print = new Button("Print this Table");
		print.setOnAction(e->{
			String report = "ALL THE POSSIBLE SPONSORS IN THE SYSTEM:\n";
			as.printSponsors(report);
		});
		GridPane.setConstraints(print, 0, 2, 4, 1, HPos.CENTER, VPos.BASELINE);

		gridSponsors.getChildren().addAll(title, tableS, print);
		root.setCenter(gridSponsors);

	}

	public void displayDetails(Animal animal)
	{
		ColumnConstraints col1 = new ColumnConstraints();
		col1.setPercentWidth(25);
		ColumnConstraints col2 = new ColumnConstraints();
		col2.setPercentWidth(25);
		ColumnConstraints col3 = new ColumnConstraints();
		col3.setPercentWidth(25);
		ColumnConstraints col4 = new ColumnConstraints();
		col4.setPercentWidth(25);
		GridPane gridDisplayDetails = new GridPane();
		gridDisplayDetails.getColumnConstraints().addAll(col1, col2, col3, col4);
		gridDisplayDetails.setPadding(new Insets(50, 50, 50, 50));
		gridDisplayDetails.setVgap(15);
		gridDisplayDetails.setHgap(15);

		//Title
		Text title = new Text("Selected Animal Details:");
		title.setFont(Font.font("default", FontWeight.BOLD, 30));
		GridPane.setConstraints(title, 0, 0, 4, 1, HPos.CENTER, VPos.BASELINE);

		//About Animal Label
		Label aboutAnimal = new Label("About the animal:");
		aboutAnimal.setFont(Font.font("default", FontPosture.ITALIC, 20));
		GridPane.setConstraints(aboutAnimal, 0, 1, 2, 1);

		//About owner Label
		Label aboutOwner = new Label("About the owner (if any):");
		aboutOwner.setFont(Font.font("default", FontPosture.ITALIC, 20));
		GridPane.setConstraints(aboutOwner, 2, 1, 2, 1);

		//ID Label
		Label IDLabel = new Label("ID:");
		GridPane.setConstraints(IDLabel, 0, 2);

		//ID
		Label ID = new Label(Integer.toString(animal.getID()));
		GridPane.setConstraints(ID, 1, 2);

		//Name Label
		Label nameLabel = new Label("Name:");
		GridPane.setConstraints(nameLabel, 0, 3);

		//Name
		Label name = null;
		try{
			name = new Label (animal.getName());
		}catch(Exception e){
			name = new Label ("-");
		}
		GridPane.setConstraints(name, 1 , 3);

		//Type Label
		Label  typeLabel = new Label("Type:");
		GridPane.setConstraints(typeLabel, 0, 4);

		//Type
		Label type = new Label(animal.getType());
		GridPane.setConstraints(type,1,4);

		//Breed Label
		Label breedLabel = new Label("Breed:");
		GridPane.setConstraints(breedLabel, 0, 5);

		//Breed 
		Label breed = new Label (animal.getBreed());
		GridPane.setConstraints(breed,1,5);

		//Colour Label
		Label colourLabel = new Label("Colour:");
		GridPane.setConstraints(colourLabel, 0, 6);

		//Colour
		Label colour = new Label (animal.getColour());
		GridPane.setConstraints(colour,1,6);


		//Description Label
		Label descriptionLabel = new Label("Description:");
		GridPane.setConstraints(descriptionLabel, 0, 7);

		//Description 
		Label description = new Label(animal.getDescription());
		GridPane.setConstraints(description,1,7);


		//Age label
		Label ageLabel = new Label("Age:");
		GridPane.setConstraints(ageLabel, 0, 8);

		//Age 
		Label age = new Label(Integer.toString(animal.getAge()));
		GridPane.setConstraints(age,1, 8);

		//Gender label
		Label genderLabel = new Label("Gender:");
		GridPane.setConstraints(genderLabel, 0, 9);

		//Gender
		Label gender;
		if (animal.getGender())
			gender = new Label("Male");
		else
			gender = new Label("Female");
		GridPane.setConstraints(gender,1,9);

		//Category Label
		Label catLabel = new Label ("Category");
		GridPane.setConstraints(catLabel, 0, 10);

		//Category
		Label category = new Label (animal.getCat().getClass().toString());
		GridPane.setConstraints(category,1,10);

		//Picture Label
		Label picLabel = new Label("Picture:");
		GridPane.setConstraints(picLabel, 0, 11);

		//Picture
		ImageView picture = new ImageView(animal.getPicture());
		GridPane.setConstraints(picture, 1, 11);

		//Owner Name label
		Label oNameLabel = new Label("Owner Name:");
		GridPane.setConstraints(oNameLabel, 2, 2);

		//Owner Name 
		Label oName = null;
		try{
			oName= new Label(animal.getCat().getContact().getName());
		}catch(Exception e){
			oName= new Label("-");
		}
		GridPane.setConstraints(oName, 3, 2);

		//Owner Address Label
		Label oAddLabel = new Label("Address:");
		GridPane.setConstraints(oAddLabel, 2, 3);

		//OwnerAddress 
		Label oAdd =null;
		try{
			oAdd = new Label(animal.getCat().getContact().getAddress());
		}catch(Exception e){
			oAdd= new Label("-");
		}
		GridPane.setConstraints(oAdd, 3, 3);

		//owner phone label
		Label oPhoneLabel = new Label("Phone Number:");
		GridPane.setConstraints(oPhoneLabel, 2, 4);

		//owner phone 
		Label oPhone = null;
		try{
			oPhone = new Label(animal.getCat().getContact().getPhone());
		}catch(Exception e){
			oPhone= new Label("-");
		}
		GridPane.setConstraints(oPhone, 3, 4);

		//owner email label
		Label oEmailLabel = new Label("Email Address:");
		GridPane.setConstraints(oEmailLabel, 2, 5);

		//owner email input
		Label oEmail = null;
		try{
			oEmail = new Label(animal.getCat().getContact().getEmail());
		}catch(Exception e){
			oEmail= new Label("-");
		}
		GridPane.setConstraints(oEmail, 3, 5);

		//Other details Label
		Label otherDetails = new Label("Other Details:");
		otherDetails.setFont(Font.font("default", FontPosture.ITALIC, 20));
		GridPane.setConstraints(otherDetails, 2, 7, 2, 1);

		//Date Lost/Found label
		Label dateLabel = new Label("Date Lost/Found or added to Adoption");
		GridPane.setConstraints( dateLabel,2 ,8 );

		//Date Lost/Found
		LocalDate d = animal.getCat().getDate();
		String s = d.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
		Label date = new Label(s);
		GridPane.setConstraints( date, 3, 8);

		//Location lost/found Label
		Label locationLabel = new Label("Location Lost/Found (If applicable)");
		GridPane.setConstraints( locationLabel, 2, 9);

		//Location Lost/found
		Label location = new Label( animal.getCat().getLoc());
		GridPane.setConstraints( location, 3, 9);

		//Adoption details Label
		Label adoptionDetails = new Label("Adoption Details:");
		adoptionDetails.setFont(Font.font("default", FontPosture.ITALIC, 20));
		GridPane.setConstraints(adoptionDetails, 2, 1, 2, 1);

		//neuteredLabel
		Label neutLabel = new Label("Neutered?:");
		GridPane.setConstraints(neutLabel, 2, 2);

		//chip Label
		Label chipLabel = new Label("Chipped?:");
		GridPane.setConstraints(chipLabel, 2, 3);

		//vacc Label
		Label vacLabel = new Label("Vaccinated?:");
		GridPane.setConstraints(vacLabel, 2, 4);

		//status Label
		Label statLabel = new Label("Status?:");
		GridPane.setConstraints(statLabel, 2, 5);

		//reserved Label
		Label resLabel = new Label("Reserved?:");
		GridPane.setConstraints(resLabel, 2, 6);


		//neutered get Text
		Label neutText = null;
		Label chipText = null;
		Label vacText = null;
		Label statText = null;
		Label resText = null;

		if (animal.getCat().getClass().toString().equals("class application.Adoption"))
			try{
				//Try to pull adoption details from the animal
				Adoption adoption = (Adoption) animal.getCat();
				neutText = new Label(String.valueOf(adoption.getNeutered()));
				GridPane.setConstraints(neutText, 3, 2);
				chipText = new Label(String.valueOf(adoption.getChipped()));
				GridPane.setConstraints(chipText, 3, 3);
				vacText = new Label(String.valueOf(adoption.getVaccinated()));
				GridPane.setConstraints(vacText, 3, 4);
				statText = new Label(adoption.getStatus());
				GridPane.setConstraints(statText, 3, 5);
				resText = new Label(String.valueOf(adoption.getReserved()));
				GridPane.setConstraints(resText, 3, 6);

			}catch(Exception e){
				neutText= new Label();
				chipText = new Label();
				vacText = new Label();
				statText = new Label();
				resText = new Label();
			}
		GridPane.setConstraints(oPhone, 3, 4);

		//Back Button
		Button back = new Button("Go to this Animals Category Screen");
		if (animal.getCat().getClass().toString().equals("class application.Found"))
			back.setOnAction(e->displayAllFound());
		else if(animal.getCat().getClass().toString().equals("class application.Lost"))
			back.setOnAction(e->displayAllLost());
		else
			back.setOnAction(e->displayAllAdoption());
		GridPane.setConstraints(back, 0, 12, 4, 1, HPos.CENTER, VPos.BASELINE);

		if((animal.getCat().getClass().toString().equals("class application.Lost"))||
				(animal.getCat().getClass().toString().equals("class application.Found"))){
			//Add lost or found details to grid
			gridDisplayDetails.getChildren().addAll(title, aboutAnimal, aboutOwner,  IDLabel, ID,  nameLabel,name, typeLabel,type,  breedLabel,
					breed, colourLabel,colour, descriptionLabel,description, ageLabel,age, genderLabel,gender, catLabel, category,
					picLabel, picture, oNameLabel, oName, oAddLabel, oAdd, oPhoneLabel, oPhone, oEmailLabel, oEmail, otherDetails,
					dateLabel, date, locationLabel, location,back);
		}
		else{
			//add adoption details to grid
			gridDisplayDetails.getChildren().addAll(title, aboutAnimal,  IDLabel, ID,  nameLabel,name, typeLabel,type,  breedLabel,
					breed, colourLabel,colour, descriptionLabel,description, ageLabel,age, genderLabel,gender, catLabel, category,
					picLabel, picture, adoptionDetails, neutLabel,neutText, chipLabel,chipText, vacLabel,vacText, statLabel,statText,
					resLabel,resText, otherDetails, dateLabel, date, locationLabel, location,back);
		}

		root.setCenter(gridDisplayDetails);


	}

	public void handleEmail(TextField oEmailInput){
		//code to check email address as it's typed
		oEmailInput.addEventFilter(KeyEvent.KEY_RELEASED, new EventHandler<KeyEvent>(){
			//override the handle event to check email address as it's typed and style with css
			@Override
			public void handle(KeyEvent arg0) {
				TextField f=(TextField)arg0.getSource();

				if (as.validateEmail(f)){
					f.setStyle("-fx-background-color: green,linear-gradient(to bottom, derive(green,60%) 5%,derive(green,90%) 40%)");
					arg0.consume();
				}
				else{
					f.setStyle("-fx-background-color: red,linear-gradient(to bottom, derive(red,60%) 5%,derive(red,90%) 40%)");
					arg0.consume();
				}
			}});
	}

	public static void main(String[] args) {
		launch(args);
	}
}
