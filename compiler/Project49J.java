package compiler;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.Checksum;
 

public class Project49J {
	
	//Package keyword Finder
	public static void packageFinder() throws IOException{
			
		Scanner file = new Scanner(Paths.get("myFile"),"UTF-8"); 
		
		String s = file.nextLine();  				  //Stores line from input file
		Pattern pattern = Pattern.compile("package"); //Search for package keyword
		Matcher matcher = pattern.matcher(s); 		  //Input line compared to package word
		
		int counter = 1;        //Line counter
		boolean first = false;  //If false, package has not showed up first
		boolean found = false;  //If false, package has not been found
		
		while(file.hasNextLine()){		//This while loop looks at the first string containing content to check if it has package
			s = s.replaceAll(" ", "");  //remove empty spaces
			if(matcher.find()){         //if package found
				found = true;          
				first = true;         
				break;                 // break
			}else{						//else
				if(s.length() < 1){     //check for empty string and if true, read in next line
				}else{                  //if length is 1 or more 
					break;              //break, because then package is definitely not first in the input file
				}
			}
			s = file.nextLine();
			matcher = pattern.matcher(s);
			counter++;
		} 
		file.close();
		
		//Check where package appears and how often  
		ArrayList<String>more = new ArrayList<>(1);     //ArrayList that holds package lines
		ArrayList<Integer>count = new ArrayList<>(5);   //ArrayList holds line number where package appears
		file = new Scanner(Paths.get("myFile"),"UTF-8");  //Open file again
		counter = 0;    //reset line counter to 0
		while(file.hasNextLine()){       //while file has content
			s = s.replaceAll(" ", "");   // replace empty spaces
			if(matcher.find()){          //if package found
				more.add(s);             //add string to arraylist of package
				count.add(counter);      //also add the line number where found into counter array list
				found = true;            //set found to true
			} 					
			counter++;					//increase line number
			s = file.nextLine();        // read in next string
			matcher = pattern.matcher(s);  //reset matcher to next line in input file
		}
		
		//Check errors for moe than 1 package and if package was declared first
		if(first == false && found == true){       //If package is not first but it was found in file
			if(more.size() > 1){                    //If more than 1 package found
				for(int i = 0; i < more.size(); i++){
					System.out.println("Error: More than 1 package and should be declared first" + more.get(i)+ " " + count.get(i));
				}
			}else{  //If 1 package found
				System.out.println("Error: Package should be declared first " + more.get(0) + count.get(0));
			}
		}
		if(first == true && found == true){      //If package is first and found
			if(more.size() > 1){ //If more than one package 
				for(int i = 1; i < more.size(); i++){ //Go through for loop and error message
					System.out.println("Error: More than 1 package found: " + more.get(i)+ ", line: " + count.get(i));
				}
			}
		}
		
		//Check if package has name
		String check = "";
		for(int i = 0; i < more.size(); i++){
			check = more.get(i);
			if(more.get(i).length() == 7){
				System.out.println("Error: Package needs name " + " line: " +count.get(i));
			}
			if(more.get(i).length() == 8){
				if(";".equals(check.substring(check.length()-1, check.length()))){
					System.out.println("Error: Package needs name " + " line: " + count.get(i));
				}
			}
		}
		
	}

	
	//FIX REPETION 
	
	//IMPORT KEYWORD
	public static void importFinder() throws IOException{
		
		Scanner file = new Scanner(Paths.get( "myFile"),"UTF-8"); 
		String s = file.nextLine();   
		Pattern pattern = Pattern.compile("import");		
		Matcher matcher = pattern.matcher(s); 	
	
		int counter = 1;
	
		ArrayList<String>name = new ArrayList<>(20);   //array list for storing import keywords
		ArrayList<Integer>position = new ArrayList<>(100); //array list for counters for each import
		
		while(file.hasNextLine()){  //Find import keywords
			if(matcher.find()){      //If import word found 
				position.add(counter); //Add line number 
				name.add(s);       //add string to array list
			}
			counter++; 			 //count each line
			s = file.nextLine(); //next line
			matcher = pattern.matcher(s);
		}
		file.close();
		
		//Removes white space and adds to new array list, along with finds if import doesn't have a name
		ArrayList<String>updateName = new ArrayList<>(20);  //New list to add modified strings for only import name
		String hold; //holds import names
		for(int i = 0; i < name.size(); i++){            //Add all imports into another array list
			hold = name.get(i);                          //While not a word character, take out blankspace
			hold= hold.replaceAll(" ", ""); 			 //White space removal
			if(hold.length() == 6){
				System.out.println("Error: import name needed on line: " + position.get(i));
			}
			if(hold.length() == 7 && ";".equals(hold.substring(hold.length()-1, hold.length()))){
				System.out.println("Error: import name needed on line: " + position.get(i));
			}
			updateName.add(i, hold);                     //Add import names at same position modified	
		}
	
		//Find repeated values and display error
		String check = "";
		for(int i =0; i < updateName.size(); i++){       //2 for loops here to compare each string with all strings of import present in array list
			for(int j = i+1; j < updateName.size(); j++){
				check = updateName.get(j);
				if(updateName.get(i).equals(updateName.get(j))){
					if(updateName.get(j).length() >6 && !(";".equals(check.substring(check.length()-1, check.length())))){
						System.out.println("Error: Repeat of same import name at line: " + position.get(i)); 
						//If same import name found, print repeat message and where it is found
					}
				}
			}
		}
	}
	
	
	public static void classFinder() throws IOException{
		
		//use param logic from the function
		//for this function find class name done, then find constructors and if they have same name 
		//check to see if they come before other things
		
		Scanner file = new Scanner(Paths.get( "myFile"),"UTF-8"); 
		String s = file.nextLine();   
		Pattern pattern = Pattern.compile("\\s(class)\\s");		
		Matcher matcher = pattern.matcher(s); 		
		String classHold = "";		//holds class name
		int counter = 1;
		
		//Finds class and what line number it is on
		while(file.hasNextLine()){
			counter++; 			//count each line
			s = file.nextLine(); //next line
			matcher = pattern.matcher(s); 
			if(matcher.find()){			
				classHold = s.substring(matcher.end(), s.length());  //start from after class keyword to length
				classHold = classHold.trim();  //remove space front and end
				//classHold = classHold.replaceAll(" ", "");
				if("{".equals(classHold.substring(classHold.length()-1, classHold.length()))){//if brace at end
					classHold = classHold.substring(0, classHold.length()-1); //remove  brace start
					System.out.println(classHold);
					break;
				}else{ //check for starting brace if no brace error bottom still holds
					classHold = classHold.substring(0,classHold.length());
					break;
				}
			}
			counter++;
			s = file.nextLine();
			matcher = pattern.matcher(s); 	
		}
		file.close();
		System.out.println("Class name: " + classHold);
		
		int count = 1;
		file = new Scanner(Paths.get( "myFile"),"UTF-8"); 
		//have class name now, find constructor
		//get to the line with class, then read afterwards
		while(file.hasNextLine()){
			s = file.nextLine();
			if(count > counter){
				break; //if line number is bigger than one declared class stop going throug loop
			}
			count++;
		}
		pattern = Pattern.compile(classHold);		 //set pattern to class name
		matcher = pattern.matcher(s);   //set it to input line
		ArrayList<Integer>countlist = new ArrayList<>(2); //line counter
		ArrayList<String>list  = new ArrayList<>(2);  //
		while(file.hasNextLine()){ //while there is input
			if(matcher.find()){  //if name of constructor found
				s = s.trim(); //trim it 
				s = s.replaceAll(" ", ""); //replace the spaces
				System.out.println("Constructor found " + s + " line: " + count);
				//what you need to fix is if there is { and check lines until there is one refer to function param 
				list.add(s); //add it to list along with line number
				countlist.add(count);
			}
			s= file.nextLine();
			count++;
			matcher = pattern.matcher(s);
		}
		file.close();

		
		//Find parameters of constructor again refer to  param function
		ArrayList<String>param = new ArrayList<>(5);
		StringBuilder builder = new StringBuilder();
		//got the constructors here and getting signatures
		for(int i = 0; i < list.size(); i++){
			System.out.println("list " + list.get(i) + " count " + countlist.get(i));
			int j = 0;
			while(!"(".equals(list.get(i).substring(j, j+1))){
				j++;
				System.out.println(list.get(i).substring(j, j+1) + " this is subtr " + j);
			}
			
			j++;
			while(!")".equals(list.get(i).substring(j, j+1))){
				builder.append(list.get(i).substring(j, j+1));
				j++;
			}
		
			String complete = builder.toString();
			System.out.println("Parameter: "+ complete);
			System.out.println(list.get(i) + " this is list " + i);
			param.add(i, complete);
		}
		//find repeat params in constructor use same method for functions
		for(int i = 0; i < list.size()-1; i++){
			for(int j = i+1; j < list.size(); j++){
				if(list.get(i).equals(list.get(j)) && (param.get(i).equals(param.get(j)))){
					System.out.println("parameter repteat :");
				}
			}
		}
	}
	
	//whitespace or { after )
	
	
	
	
	
	
	public static void param()throws IOException{
		
		Scanner file = new Scanner(Paths.get("myFile"),"UTF-8"); 
		String s = file.nextLine();   
		Pattern pattern = Pattern.compile("[()]");		
		Matcher matcher = pattern.matcher(s); 
		String hold = "";
		String funcname = "";
		
		//now get function name
		
		ArrayList<String>name = new ArrayList<>(2); // for function 
		ArrayList<String>list  = new ArrayList<>(2); //parameter holder
		ArrayList<Integer>counterl = new ArrayList<>(2); // holds line number for cuntion
		int counter = 1;
		String funchold = "";
		while(file.hasNextLine()){
			if(matcher.find()){
				//s = s.replaceAll(" ", ""); //remove the white spaces from string
				s = s.trim();
				s = s.replaceAll(" ", "");
				if(!s.contains("if(") && !s.contains("while(") && !s.contains("for(")){
					if("{".equals(s.substring(s.length()-1, s.length()))){  //if ending has a brace, then you know it is function
						name.add(s.substring(0, matcher.start()-1));
						System.out.println("name of func: " + name.get(0));
						System.out.println("param: " + matcher.group() + " starts: " + matcher.start());
						s = s.substring(matcher.start(), s.length()); //remove ending brace 
						System.out.println(s + "with brace ending " + counter);
						int i = 0; //position 1 is after (
						while(!")".equals(s.substring(i, i+1))){ //keep going until ) found
							i++; //move counter up
						}
						list.add(s.substring(0, i)); //inside  = 1 -i	
						counterl.add(counter);
						System.out.println("Param Added: " + s.substring(0, i));
					}
					if(!"{".equals(s.substring(s.length()-1, s.length()))){  //if ending has a brace, then you know it is function
						funchold = s.substring(0, matcher.start()-1); //holds function name 
						System.out.println("param: " + matcher.group() + " starts: " + matcher.start());
						s = s.substring(matcher.start(), s.length()); //remove ending brace 
						System.out.println(s + "with brace ending " + counter);
						int i = 0; //position 1 is after (
						while(!")".equals(s.substring(i, i+1))){ //keep going until ) found
							i++; //move counter up
						}
						hold = s.substring(0, i);
						
						System.out.println("Param hold: " + s.substring(0, i));
						counter++;
						s = file.nextLine();
						s = s.trim();
						s = s.replaceAll(" ", "");
						//s = s.trim();
						while(s.length() <1){
							counter++;
							s = file.nextLine();
							s = s.replaceAll(" ", "");
						}
						if(s.length() >= 1){
							s = s.trim();
							System.out.println(s);
							if("{".equals(s.substring(0,1))){
								System.out.println("Next Line");
								list.add(hold); //inside  = 1 -i
								counterl.add(counter);
								System.out.println("Added " + hold);
								name.add(funchold);
								System.out.println("func name " + name.get(0));
								
							}else{
								System.out.println("No starting brace found {");
							}
						}
					}
				}
			}
			counter++;
			s= file.nextLine();
			matcher = pattern.matcher(s);
		}
		file.close();
		
		for(int i = 0; i < name.size()-1; i++){
			for(int j = i+1; j < name.size(); j++){
				if(name.get(i).equals(name.get(j))){
					if(list.get(i).equals(list.get(j))){
						System.out.println("1: (" + list.get(i) + ") param 2: (" + list.get(j));
						System.out.println("1: (" + name.get(i) + ") name 2: (" + name.get(j));
						System.out.println("line: " + counterl.get(j) );
					}
				}
			}
		}
		
	}
	
	
	public static void semiColon() throws IOException{
		
		//semicolon key word 
		Scanner file = new Scanner(Paths.get( "myFile"),"UTF-8"); 
		int counter = 1;
		String s = file.nextLine();   
		Pattern pattern = Pattern.compile(";");	 //If no semicolon	need to allow for if, while, for, constructor, function
		Matcher matcher = pattern.matcher(s); 		

		ArrayList<Integer>commaC = new ArrayList<>(100); //Store line numbers for finding where errors occured
		while(file.hasNextLine()){
			s = s.replaceAll(" ","");
			if(s.length() < 1){
				System.out.println("empty space");
			}else{
				if(s.contains("{") || s.contains("}") || s.contains("class") || s.contains("if") ||  s.contains("while") || s.contains("do")){
					System.out.println("Comma ok " + counter + s );
					commaC.add(counter);
				}else{
					if(s.contains(";")){
						System.out.println("Comma ok " );
					}else{
						if(s.length() > 1){
						System.out.println("need comma " + counter );
						}
					}
				}
			}
		counter++; 			//count each line
		s = file.nextLine(); //next line
		matcher = pattern.matcher(s);
		}
		file.close();	
	}
	
	
	//nothing before class that isn't import or package can be declared
	public static void random() throws IOException{
		
		Scanner file = new Scanner(Paths.get("myfile"),"UTF-8" );
		String s = file.nextLine();
		
		Pattern pattern = Pattern.compile("class");
		Matcher matcher = pattern.matcher(s);
		int counter = 1;
		while(file.hasNextLine()){
			if(matcher.find()){
				System.out.println("Ok random exit");
				break;
			}
			s = s.replaceAll(" ", "");
			if(!s.contains("import") && !s.contains("package") && s.length() >= 1 ){
				System.out.println("error" + " " + counter);
			}
			counter++;
			s = file.nextLine();
			matcher = pattern.matcher(s);
		}
		file.close();
	}
	
	//braces check if u need these cause you found it for param function just error message , only for function this solved
	
	public static void braces() throws IOException{  //braces for functions do while if constructor class each one has correspond closing
		
		
		Scanner file = new Scanner(Paths.get("myfile"),"UTF-8" );
		String s = file.nextLine();
		
		Pattern pattern = Pattern.compile("class");
		Matcher matcher = pattern.matcher(s);
		int counter = 1;
		while(file.hasNextLine()){
			
			s = s.replaceAll(" ", "");
			if(matcher.find()){
				System.out.println("class found " + matcher.group());
				break;
			}
			
			counter++;
			s = file.nextLine();
			matcher = pattern.matcher(s);
		}
		file.close();
		
		
	}
	
	public static void intance()throws IOException{
		
		//find class keyword then search everything after that until you find () on a string line then that line doesn't count
		
		Scanner file = new Scanner(Paths.get( "myFile"),"UTF-8"); 
		String s = file.nextLine();   
		Pattern pattern = Pattern.compile("\\s(class)\\s");		
		Matcher matcher = pattern.matcher(s); 		
		String classHold = "";		//holds class name
		int counter = 1;
		
		//Finds class and what line number it is on
		while(file.hasNextLine()){
			counter++; 			//count each line
			s = file.nextLine(); //next line
			matcher = pattern.matcher(s); 
			if(matcher.find()){			
				classHold = s.substring(matcher.end(), s.length());  //start from after class keyword to length
				classHold = classHold.trim();  //remove space front and end
				//classHold = classHold.replaceAll(" ", "");
				if("{".equals(classHold.substring(classHold.length()-1, classHold.length()))){//if brace at end
					classHold = classHold.substring(0, classHold.length()-1); //remove  brace start
					System.out.println(classHold);
					break;
				}else{ //check for starting brace if no brace error bottom still holds
					classHold = classHold.substring(0,classHold.length());
					break;
				}
			}
			counter++;
			s = file.nextLine();
			matcher = pattern.matcher(s); 	
		}
		ArrayList<String>insta = new ArrayList<>(5);
		while(file.hasNextLine()){
			counter++;
			s = file.nextLine();
			s = s.trim();
			s = s.replaceAll(" ", "");
			if(s.contains("(") && s.contains(")")){
				break;
			}else{
				if(s.length() < 1){
					
				}//if it contains the following values check canvas then instance variable maybe more but store it
				 //check if u need int a = m; kind of style, check semicolon and then if private
				 //check if it is used elsewhere there are xceptions, check the school hw assignment and why private didn't work
				if(s.contains("int")){
					insta.add(s);
				}
			}
		}
		
		file.close();	
	}
	
	public static void main(String[]args) throws IOException{
	//Find a way to return values of each function to determine success or not
		
		//packageFinder(); 
	
	//Fix finding repeated names
		
		//importFinder();   
	
	//Fix
		//classFinder();
	
	//Fix paramteres printing double like import finder
		//param();
		
	//braces();
    //semiColon();  //Fix for certain cases 
		//random();        //find lines that do not contain import or package	
		
	//instance variables, once class found, search begins after {, if no { then error but search
		//then while the beginning is int, bool, whatever store it as instance variable , can make check for semi colons hear also
		//
    
	}
}