package compiler;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.management.ObjectName;

public class Main {
	
	
	public static void main(String[]args)throws IOException{
		
		//Array List Objects 
		ArrayList<Class>ClassArray = new ArrayList<>(2); //rename
		ArrayList<Import>Import1 = new ArrayList<>(2);
		ArrayList<Methods>Method1 = new ArrayList<>(2);
		ArrayList<Variables>Variable1 = new ArrayList<>(2);
		
		boolean user = true; //Variable that stores user's choice whether they want to enter more files
		int fileCounter = 0; //Counts the number of files entered
		
		//Different object class
		ArrayList<String>objectsClass =  new ArrayList<>(2);
		ArrayList<String>dParam = new ArrayList<>(2);
		ArrayList<Integer>dLine = new ArrayList<>(2);
		ArrayList<String>objectName = new ArrayList<>(2);
		
		ArrayList<String>fileTracker  = new ArrayList<>(2);
		
		while(user == true){
			
			fileCounter++; //number of files read in
			
			//Classes Instantiated
			Class newClass = new Class();
			Methods method = new Methods();
			Import importClass = new Import();
			Variables v = new Variables(); // 
			
			//Add objects
			ClassArray.add(newClass);
			Method1.add(method);
			Import1.add(importClass);
			Variable1.add(v);
		
			
			System.out.println("Enter file name: ");
			Scanner get = new Scanner(System.in);
			String getter = "";
			getter = get.next();
			
			fileTracker.add(getter);//add file name
			
			//Below gets class information, such as name
			Scanner file = new Scanner(Paths.get(getter), "UTF-8");
			String in = file.nextLine(); 
			Pattern Class = Pattern.compile("\\s*(public|private|protected|abstract)?\\s+(class)\\s+(\\w+)(\\s)*([{]?)");
			Matcher matcher = Class.matcher(in);
			boolean found = false;  //if class found
			String className = "";  //holds class name
			int counter = 1;        //line number counter
			ArrayList<Integer>classCounter = new ArrayList<>(1); //Hold class line number
			boolean classfound = false;
			
			//KeyWord Regex
			Pattern keyword = Pattern.compile("(import|for|do|while|for|public|class|abstract|final|String|int|double|char|boolean|protected|private|float|package)");
			Matcher keyMatcher = keyword.matcher(in);
			
			//Find class declaration, get name and then break from loop
			while(file.hasNextLine()){
				if(matcher.find()){
					if(matcher.group(3) == null){
						System.out.println("Error: No class name on line " + counter);
						found = false;
						break;
					}
					keyMatcher = keyword.matcher(matcher.group(3));
					if(keyMatcher.find()){
						System.out.println("Error: Class Name is a keyword on line " + counter);
						found = false; //Set the boolean to found, if class has been found
						break; 
					}
					className = matcher.group(3); //Store name of class into class Name for later use
					found = true; //Set the boolean to found, if class has been found
					classfound = true;
					classCounter.add(counter);
					newClass.addName(className);
					newClass.addLine(counter);
					if(matcher.group(1) == null){
						newClass.specifier("package-private");
					}else{
						newClass.specifier(matcher.group(1));
					}
					break; 
				}
				counter++;
				in = file.nextLine();
				matcher = Class.matcher(in);	
			}
			file.close();
			
			
			
			//Finds Constructor and its parameters
			
			file = new Scanner(Paths.get(getter), "UTF-8");
			int constructorLine = 1; //Holds line number for where constructor is foundd 
			String hold = "";
			in = file.nextLine();
			Pattern construct = Pattern.compile("^\\s*(public|protected|private)?\\s*"+className+"\\s*\\(\\s*((.*))\\s*\\)\\s*([{]?)"); //pattern consist of class name
			matcher = construct.matcher(in);
			boolean constructorFound = false;
			ArrayList<Integer>constructorCounter = new ArrayList<>(2);
			if(found == true){ 
				 while(file.hasNextLine()){
					 if(matcher.find()){
						 hold = matcher.group(3);
						 hold = hold.trim();
						 if(hold.length() < 1){
							 hold = "-"; //stands for 0 parameters
						 }
						if(constructorLine < counter){
							 System.out.println("Error: Constructor created before class declaration, check line " + constructorLine );
						 }else{
							 newClass.addConstructor(className, constructorLine, hold); //hold parameter
							 constructorFound = true;
							 constructorCounter.add(constructorLine);
						 }
					 }
					 constructorLine++;
					 in = file.nextLine();
					 matcher = construct.matcher(in);
				 }
			}
			file.close();
			
			newClass.parameterEdit(); //eidt parameters to contain types
			if(newClass.duplicateParameterConstructor() == false){
			} //find duplicate parameter constructors
			
			
			//New instantiation get parameters
			file = new Scanner(Paths.get(getter), "UTF-8");
			
			Pattern object = Pattern.compile("\\s*(\\w+)\\s+(\\w+)\\s*=\\s*(new)\\s+(\\w+)\\s*\\(\\s*(.+)?\\s*\\)\\s*"); ///edit later
			in = file.nextLine();
			Matcher objectFinder = object.matcher(in);	
			
			Matcher nameMatcher = keyword.matcher(in);
					
			ArrayList<String>objectParameter = new ArrayList<>(20);
			String paramHolder = "";
			int line =1;
			  
			while(file.hasNextLine()){
				if(objectFinder.find()){
					keyMatcher = keyword.matcher(objectFinder.group(1));
					nameMatcher = keyword.matcher(objectFinder.group(2));
					if(keyMatcher.find() || nameMatcher.find()){
						System.out.println("Error: Object is instantiated using keyword on line: " + line);
					}else{
						if(objectFinder.group(5) == null){
							objectParameter.add("-");
							paramHolder = "-";
						}else{
							objectParameter.add(objectFinder.group(5));
							paramHolder = objectFinder.group(5) ;
						}
						dLine.add(line);
						if(paramHolder.length()>1){
							char[] newCharArray = new char[10];               //Change this so not hard coded d
							newCharArray = paramHolder.toCharArray();          //This gets whole string of Parameter and puts it into char
							int count = 0;                                       //Comma counter 
							for(int j = 0; j < newCharArray.length; j++) {     //Goes through a paramter and each part of it, goes through each character
								if(newCharArray[j] == ','){ //Count the number of commas 
									count++; //add 1
								}
							}
							count = count+1; //if 1 comma, there needs to be 2 values, therefore add 1 to count
						
							String[] arrayComma = new String[count]; //create a String array to hold each part of string, using number of commas there are	as count			
							arrayComma = paramHolder.split(","); //Split the string and store it into different parts of array 
							String objectStringFinal = "";             //Holds Final String, basically builder
						
							//Go through each part of parameter and give value
							for (int x = 0; x < count; x++) {
								arrayComma[x] = arrayComma[x].trim();
								if(arrayComma[x].matches("\\d+")){
									//System.out.println("INT " + arrayComma[x]);
									objectStringFinal+= "int";
								}else{
									if(arrayComma[x].contains(".")){
										objectStringFinal += "double";
										//System.out.println("Double " + objectStringFinal);
									}else{
										if(arrayComma[x].substring(0,1).contains("'")){ //for characters only 
											//System.out.println("CHAR " + arrayComma[x]);
											objectStringFinal+= "char";
										}else{
											if(arrayComma[x].contains("")){
												objectStringFinal+="String";
												//System.out.println("String");
											}							
										}
									}
								}
							}
							objectsClass.add(objectFinder.group(1).trim());
							objectName.add(objectFinder.group(2).trim());
							dParam.add(objectStringFinal);
							//dLine.add(line);
						}else{
							objectsClass.add(objectFinder.group(1).trim());
							objectName.add(objectFinder.group(2).trim());
							dParam.add(paramHolder);
							//dLine.add(line);
						}
					}
				}
				line++; //Line counter 
				in= file.nextLine();
				objectFinder = object.matcher(in);
			}
			file.close();
			
			if(newClass.instantiationPossible() == true){
				
			}
			
			
			//check objects of the same name
			for(int i = 0; i < objectName.size()-1; i++){
				for(int j = i+1; j < objectName.size(); j++){
					if(objectName.get(i).equals(objectName.get(j))){
						System.out.println("Error: Same object names, check lines " + dLine.get(i) + " and line " + dLine.get(j));
					}
				}
			}
			
				
			//								Function Name and Parameter GEt
			
			//Fix below for abstract also and final, and see for constructors also if these replace public or come after public
			//Searches for functions and gets their parameters, these functions are not constructors
			counter = 1;
			file = new Scanner(Paths.get(getter), "UTF-8");
			in = file.nextLine();
			Pattern func = Pattern.compile("^\\s*(public\\s+|protected\\s+|private\\s+)?\\s*(int|String|boolean|double|char|float)\\s+(\\w+)\\s*\\(\\s*(.*)?\\s*\\)\\s*" );
			matcher = func.matcher(in);	
			ArrayList<Integer>functionCounter = new ArrayList<>(2);
			boolean functionFound = false;
			String functionParameterHold = "";
			String functionName = "";
			while(file.hasNextLine()){
				if(matcher.find()){ //If function is found and it doesn't contain the class name
					functionParameterHold = matcher.group(4);
					functionParameterHold = functionParameterHold.trim();
					functionName = matcher.group(3);
					if(functionParameterHold.length()<1){//if no parameters, change it to -
						functionParameterHold = "-";
					}
					keyMatcher = keyword.matcher(functionName);
					if(keyMatcher.find()){
						System.out.println("Error: Function name is keyword on line " + counter);
					}else{
						method.add(functionName, counter, functionParameterHold);
						functionCounter.add(counter);
						functionFound = true;
					}
					
				}
				in = file.nextLine();
				counter++;
				matcher = func.matcher(in);
			}
			file.close();
			
			method.parametersEdit(); //Edit the parameters of the function
			if(method.duplicates() == false){ //find any duplicate functions with the same name and same parameters, if false, then no errors
			
			}
			
			
			
			
			
			
			
			// 						PACKAGE AND IMPORT SECTION 
			 
			//Finds package and imports
			file = new Scanner(Paths.get(getter), "UTF-8");
			in = file.nextLine();
			boolean packageFound = false;	
			counter = 1;
			//Add before class after class stuff and beginning of line, change import to accpeet * and . 
			//fix Regex below both so that what to do if there is no name for pacakge or import (\\w+)*
			//check after class declaration
			
			Pattern pack = Pattern.compile("\\s*(package)\\s+(\\w+)?\\s*");  //package LATER
			matcher = pack.matcher(in);
			Pattern impo = Pattern.compile("\\s*(import)\\s+([\\w.]+)?\\s*([*])?");    //import
			Matcher match = impo.matcher(in);
			ArrayList<Integer>packageCounter = new ArrayList<>(20);
			ArrayList<String>packageArray = new ArrayList<>(1); //change this to hold the name of package without semicolon when adding
			while(file.hasNextLine()){
				if(matcher.find()){  //Package
					 if(matcher.group(2) == null){
						 System.out.println("Error: No name found for package on line " + counter);
					 }else{
						 keyMatcher = keyword.matcher(matcher.group(2));
						 if(keyMatcher.find()){
							 System.out.println("Error: Package Name is a Keyword on line  "+ counter);
						 }else{
							 packageFound = true;
							 packageArray.add(matcher.group(2));
							 packageCounter.add(counter);
							 newClass.addPackageName(matcher.group(2));
						 }
					 }
				 }
				 if(match.find()){ //import
					 if(match.group(2) == null){
					     System.out.println("Error: No name found for import on line " + counter);
					 }else{
						 keyMatcher = keyword.matcher(match.group(2));
						 if(keyMatcher.find()){
							 System.out.println("Error: Import has a keyword, check line " + counter);
						 }else{
							 if(match.group(3)== null){
								 importClass.add(match.group(2), counter);
							 }else{
								 importClass.add(match.group(2)+match.group(3), counter);
							 }
						 }
					 }
				 }
				 counter++;
				 in = file.nextLine();
				 matcher = pack.matcher(in);
				 match = impo.matcher(in);
			 }
			 file.close();
			 		 
			 // call function from import class to check for duplicates
			 if(importClass.repeats()){
				 
			 }
			 
			 
	
			 //Checks if package is first when package is found in the input file
			 file = new Scanner(Paths.get(getter), "UTF-8");
			 in = file.nextLine();
			 int checkCounter = 1;
			 String packageHold = in;	//Get first line in input and store it in a hold string
			 boolean packageFirst = true; //Default, packageFirst is true
			 if(packageFound == true){
				 while(checkCounter < packageCounter.get(0)){ //If the counter going through the file is less than the first package counter
					 packageHold = packageHold.trim();    //Trim the string for white spaces
					 if(packageHold.length() >= 1 ){ //If the length is >=1, that means the package is not first, because the counter is less than the first instance of package
						 packageFirst = false;  //Set the packageFirst to false
						 break; //break from loop
					 } 
					 checkCounter++; //Add 1 to the counter
					 packageHold = file.nextLine(); //Store next line into hold string
				 }
			 }
			 file.close();
			 
			 boolean packageOk = true;
			 //Check if package was not first when package was found in the file
			 if(packageFirst == false && packageFound == true){ //If package was found and was not first
				 System.out.println("Package is not first in input file: Error on line " + packageCounter.get(0)); //Display error
				 packageOk = false;
			 }
			 //Checks for more than 1 package
			 if(packageArray.size() >1){ //If the size of pacakgeArray is more than 1, then display the errors
				 for(int i = 1; i < packageArray.size(); i++){
					 System.out.println("More than 1 package found on line: " + packageCounter.get(i));
					 packageOk = false;
				 }
			 }
			 //add package boolean to class
			 newClass.addPackageBoolean(packageOk);
			
			 
			 //So go through a for loop, if an object belongs to a different class
			 //check the imports, if no import exists before class declaration that matches a certain format
			 
			 if(packageFound == true && packageFirst == true && packageOk == true){
				 packageOk = true;
			 }
			 
			
			
		
			
	 
		 
		 	//   Public Static Main , Find main
			file = new Scanner(Paths.get(getter), "UTF-8");
			in = file.nextLine();
			int mainCounter = 1;
			Pattern mainCheck = Pattern.compile("\\s*(public)\\s+(static)\\s+(void)\\s+(main)\\s*[(]\\s*(.*)\\s*");// for pubic staic void main
			Matcher mainFinder = mainCheck.matcher(in);
			boolean mainFound = false;
			ArrayList<Integer>mainLineCounter = new ArrayList<>(2);
			while(file.hasNextLine()){
				if(mainFinder.find()){
					if(classCounter.size()<1){
						System.out.println("Error: No Class found but main found line: +" +  mainCounter);
					}else{
						if(classCounter.get(0) > mainCounter){
							System.out.println("Error: Main declared before class, check line "+ mainCounter);
						}else{
							mainFound = true;
							newClass.addMain(mainCounter);
							mainLineCounter.add(mainCounter);
						}
					}
					
				}
				mainCounter++;
				in = file.nextLine();
				mainFinder = mainCheck.matcher(in);
			}  
			file.close(); 
			
			//if main is found and size greater than 2, then errors on those lines, fixthis beloooow
			if(mainFound == true){
				if(newClass.duplicateMain()==false){
					if(newClass.getLine() > newClass.getMainLine()){
						System.out.println("Error: Main comes before class declaration line " + newClass.getMainLine() );
					}
				}else{
					
				}
			}
			
			//If main is true but duplicates false
				//Check main for coming after class declaration
			
			
			//If main false, error for any constructors instantiatedd check for this below
			//If main is true, duplicates are false, check for error for constructors before it
			
			 
					
			//INSTANCE VARIABLE SECTION
			 		 
			//Instance variables need to do still******************
			//Also fix word characters also being numbers when initialized for final 
			//Regex final
			//Also \w+ allows for numbers and words, check if you need to make sure that String 7 = 7 is not a problem
			
			//check for instance variable in function
				
			file = new Scanner(Paths.get(getter), "UTF-8");
			in = file.nextLine();
			Pattern instance = Pattern.compile("^\\s*(private\\s+|public\\s+|protected\\s+)?(static\\s+)?(int|float|double|char|boolean|String)\\s+(\\w+)\\s*(=\\s*\\w+)?\\s*([;])?" );
			objectFinder = object.matcher(in);
			Matcher mat = instance.matcher(in);
			matcher = func.matcher(in);	//checks for functions to make sure that function is not recognized as a regex
			boolean verror = false;
			int instanceCounter = 1;
			
			
			//If it is before constructor and it exists and after class declaration
			//If after main or function then false
			
			while(file.hasNextLine()){
				if(mat.find() && !matcher.find() && !objectFinder.find()){
					if(classfound == false){
						System.out.println("Error: No class found for instance variable, check line "+ instanceCounter);
					}else{
						if(in.contains(" "+className+" ") || in.contains(" " + className + "(")){
							
						}else{
							//If class is found there can be an instance variable then 
							if(instanceCounter < classCounter.get(0)){
								System.out.println("Error: Instance found before class declaration, check line " + instanceCounter);
								verror = true;
							}else{
								if(constructorFound == true){
									if(constructorCounter.get(0) < instanceCounter){
										if(mat.group(1)!=null){
										System.out.println("Error: Constructor comes before instance variable, check line " + instanceCounter);
										verror = true;
										}
									}
								}else{
									if(mainFound == true){
										if( mainLineCounter.get(0) < instanceCounter){
											System.out.println("Error: Main comes before instance variable, check line " +instanceCounter );
											verror = true;
										}
									}else{
										if(functionFound == true){
											if(functionCounter.get(0) < instanceCounter){
												if(mat.group(1) == null){
												}else{
													System.out.println("Error: Function comes before instance variable, check line " + instanceCounter);
													verror = true;
												}
											}
										}
									}
								}
							}
							if(verror == false){ //If no errors in where instance declared, then added
								if(mat.group(1)==null){ //if no access specifier automatically public
									if(mat.group(2) == null){ // if no static
										v.add("public", null, mat.group(3), mat.group(4), instanceCounter);
									}else{ //if static
										v.add("public", mat.group(2), mat.group(3), mat.group(4), instanceCounter);
									}
									
								}else{  //If access specifier
									if(mat.group(2) == null){ //if no static
										v.add("public", null, mat.group(3), mat.group(4), instanceCounter);
									}else{ //if static
										v.add("public", mat.group(2), mat.group(3), mat.group(4), instanceCounter);
									}
								}
							}
						}
					  }
					}
				
			instanceCounter++;
			in = file.nextLine();
			mat = instance.matcher(in);
			matcher = func.matcher(in);	
			objectFinder = object.matcher(in);
			
		}
		file.close(); 
		v.duplicates(); //check for duplicate instance variable names
		
		
		
		//INTERFACE
		file = new Scanner(Paths.get(getter), "UTF-8");
		in = file.nextLine();
		Pattern interfaceP = Pattern.compile("\\s*(public)?\\s*(interface)\\s*(\\w+)\\s*[{]?\\s*");
		Matcher instanceMatcher = interfaceP.matcher(in);
		int interfaceCounter = 1;
		ArrayList<String>interfaceArrayString= new ArrayList<>(2);
		ArrayList<Integer>interfaceArrayInt = new ArrayList<>(2);
		while(file.hasNextLine()){
			if(instanceMatcher.find()){
			    keyMatcher = keyword.matcher(in);
				keyMatcher = keyword.matcher(instanceMatcher.group(3));
				if(keyMatcher.find()){
					System.out.println("Error: Keyword for interface name on line " + interfaceCounter + " in file: " + getter);
				}else{
					interfaceArrayString.add(instanceMatcher.group(3));
					interfaceArrayInt.add(interfaceCounter);
			   }
			}
			in = file.nextLine();
			instanceMatcher = interfaceP.matcher(in);
			interfaceCounter++;			
		}
		file.close();

		
		
		/*
		//Extends
		file = new Scanner(Paths.get(getter), "UTF-8");
		in = file.nextLine();
		Pattern extendsP = Pattern.compile("\\s*(public|private|protected|abstract)?\\s+(class)\\s+(\\w+)\\s+(extends)\\s+(\\w+)\\s*[{]?");
		Matcher extendsM = extendsP.matcher(in);
		int extendsCounter = 1;
		ArrayList<String>extendArray = new ArrayList<>(2);
		ArrayList<Integer>extendCounter = new ArrayList<>(2);
		
		while(file.hasNextLine()){
			if(extendsM.find()){
				keyMatcher = keyword.matcher(extendsM.group(5));
				if(keyMatcher.find()){
					System.out.println("Error: Name of class being extended is a keyword, check line " + extendsCounter);
				}else{
					if(extendsM.group(3).equals(extendsM.group(5))){
						System.out.println("Error: Extending same class, check line " + extendsCounter);
					}else{
						extendArray.add(extendsM.group(5));
						extendCounter.add(extendsCounter);
					}
				}
				extendsCounter++;
				in = file.nextLine();
				extendsM = extendsP.matcher(in);
			}
		}
		
		//check if extended class exists, go through for loop just like imports
		System.out.println("HAHAHA");
		*/
		
		
		
		
	
		//Checks for semicolon
		file = new Scanner(Paths.get(getter), "UTF-8");
		in = file.nextLine();
		Pattern semi = Pattern.compile("(for\\s*\\(|do\\s*|while\\s*\\(|if\\s*\\(|else\\s*|[{]|[}]|\\s*(public|private|protected|abstract)?\\s+(class)\\s+(\\w+)(\\s)*([{]?))");
		Matcher semiFinder = semi.matcher(in);
		int counters = 1;
		while(file.hasNextLine()){
			if(!semiFinder.find()){
				in = in.trim();
				if(in.length()>0){
					if(!";".equals(in.substring(in.length()-1, in.length()))){
						System.out.println("Semi colon needed line " + counters);
					}
				}
			}
			counters++;
			in = file.nextLine();
			semiFinder = semi.matcher(in);
		}
			
			/*
			 //					IF WHILE FOR FINDER/COUNTER
			
			 //Fix class counter because if whiles only work in functions or constructors
			 //Add these to an ArrayList? alongn with line numbers? then find
			//Finds if, while, for, do keywords 
			int iCounter = 1;
			file = new Scanner(Paths.get("myFile"), "UTF-8");
			in = file.nextLine();
			Pattern whiles = Pattern.compile("\\s*(if|while|for)\\s*([(])\\s*((.)*)\\s*[)]\\s*[{]?");
			matcher = whiles.matcher(in);	
			Pattern dos = Pattern.compile("\\s*(do)\\s*[{]?");
			Matcher dosMatcher = dos.matcher(in);
			while(file.hasNextLine()){
				if(matcher.find()){ 
					if(classCounter.size()>=1){
						if(iCounter > classCounter.get(0)){
							System.out.println(matcher.group(1) + " has been found on line " + iCounter); // specifier
						}else{
							System.out.println("Error: " + matcher.group(1) + " has been declared before class declaration");
						}
					}else{
						System.out.println("Error: " + matcher.group(1) + " has been declared with no class declaration");
					}
				}
				if(dosMatcher.find()){ 
					if(classCounter.size()>=1){
						if(iCounter > classCounter.get(0)){
							System.out.println("do found on line: " + iCounter);
						}else{
							System.out.println("Error: " + dosMatcher.group(1) + " has been declared before class declaration");
						}
					}else{
						System.out.println("Error: " + dosMatcher.group(1) + " has been declared with no class declaration");
					}
				}
				in = file.nextLine();
				iCounter++;
				matcher = whiles.matcher(in);
				dosMatcher = dos.matcher(in);
			}
			file.close(); 
				
		
			
			/*
			 //					BRACES COUNTING
			
			//Code below counting messed up fix
			//Below code for braces counting, need to make sure that missing braces can affect the true total
			int openC = 0; //open brace
			int closeC = 0; //close brace
			file = new Scanner(Paths.get("myFile"), "UTF-8");
			in = file.nextLine();
			Pattern brace = Pattern.compile("[{]");
			Pattern closebrace = Pattern.compile("[}]");
			Matcher Cmatcher = brace.matcher(in);	
			Matcher cMatcher = closebrace.matcher(in);
			
			Pattern braceRegex = Pattern.compile("(while|for|if)");
			Matcher braceMatcher = braceRegex.matcher(in);
			int braceCounter = 0;
			
			matcher = whiles.matcher(in);	
			dosMatcher = dos.matcher(in);
			while(file.hasNextLine()){
				
				if(dosMatcher.find()){
					braceCounter++;
				}
				if(braceMatcher.find()){
					braceCounter++;
				}
				if(matcher.find()){ 
					openC++;
				}
				if(cMatcher.find()){
					closeC++;
				}
				in = file.nextLine();
				braceMatcher = brace.matcher(in);
				cMatcher = closebrace.matcher(in);
				matcher = whiles.matcher(in);
				dosMatcher = dos.matcher(in);
			}
			braceCounter+= constructorArray.size();
			braceCounter+=functionCounter.size();
			braceCounter+=classCounter.size();
			braceCounter*=2;
			System.out.println("Number of braces expected : " + braceCounter);
			System.out.println("Number of opening braces found: " + openC);
			System.out.println("Number of closing braces found: " + closeC);
			int totalBrace = openC+closeC;
			if(totalBrace >braceCounter ){
				System.out.println("Number of extra braces: " + (totalBrace - braceCounter));
			}
			if(totalBrace< braceCounter){
				System.out.println("Number of braces needed: " + (braceCounter - totalBrace));
			}
			if(totalBrace == braceCounter){
				System.out.println("Number of braces ok: ");
			}
		
			file.close();
			*/
			
		
			//while(user presses yes, create a new class object to hold all this in an array list and ask user at the end if more files to enter
			System.out.println("Would you like to enter another file? Press Y for Yes or N for No");
			Scanner input = new Scanner(System.in);
			char choice = input.next().charAt(0);
			if(choice == ('Y') || choice == ('y')){
				user = true;
				
			}else{ //If user is done inputting files
				
				boolean notallowed = false;
				//Checks if instantiated objects can actually be instantiated
				for(int i = 0; i < objectsClass.size(); i++){
					for(int j = 0; j < ClassArray.size(); j++){ // num of classes found, or files basically
						if(objectsClass.get(i).equals(ClassArray.get(j).getName())){	
							if(ClassArray.get(j).getSpecifier().equals("abstract")){
								System.out.println("Error: Cannot instantiate an object that belongs to an abstract class");
								System.out.println("Error is in file: " + fileTracker.get(i) + " on line " + dLine.get(i));
								notallowed = true;
							}else{
								if(dParam.get(i).equals("-")){ //- stands for empty parameters
									//no error because empty parameters by default will create a default constructor
									notallowed= true;
								}else{
									if(ClassArray.get(j).compare(dParam.get(i)) == false){
										System.out.println("Error: trying to instantiate object: " + objectsClass.get(i) + "(" + dParam.get(i) + ") is not possible" );
										System.out.println("Error is in file: " + fileTracker.get(i) + " on line "+ dLine.get(i));
										notallowed = true;
									}
								}
							}
						}
						if(j == ClassArray.size() && notallowed == false){
							System.out.println(objectsClass.get(i) + " class does not exist for object " + objectsClass.get(i) + " to be instantiated.");
							System.out.println("Error is in file: " + fileTracker.get(j) + " on line " + dLine.get(i));
							
						}
					}
					
				}
						
				
				 //CHECK INSTANTIATED OBJECT FOR CORRECT IMPORTS
				 //package must be ok, meaning there is 1 and that no errros
				
				
				 ArrayList<Boolean>objectDifferent = new ArrayList<>(1); 
				 if(fileTracker.size()>1){
					 for(int i = 0; i < objectsClass.size(); i++){     //Number of instantaited objects
						    ArrayList<Boolean>error = new ArrayList<>(2);
						 	for(int j = 0; j < fileTracker.size();j++){
							 //if instantiated object belongs to different class 
							
							 if(!objectsClass.get(i).contains(ClassArray.get(j).getName())){
								 String x = ClassArray.get(j).getPackageName() + "."+ClassArray.get(j).getClassName();
								 String y = ClassArray.get(j).getPackageName()+".*";
								 if(Import1.get(j).sizeArray() > 0){
									error.add(Import1.get(j).objectIsImported(x, y));	//get the name of the class, and compare it to the imports that are already avialble for the class
								 }else{
									 System.out.println("You need to import the correct file for the instantiated object: " + objectsClass.get(i));
									 System.out.println("Check line " + dLine.get(i) + " in file " + fileTracker.get(j));
								 }
							 } 
							 //packagename.classname or packagename.* for import 
						 	}
						 	boolean errorNotFound = false;
						 	for(int a = 0; a < error.size(); a++){
						 		if(error.get(a).equals(true)){
						 			errorNotFound = true;
						 		}
						 	}
							if(errorNotFound == false){
							    System.out.println("Error: Need import for object " +  objectsClass.get(i));
							}
					 }	
				 }
				 
		
				user = false;
			}
			
			
		}
		
	}
}
	
			