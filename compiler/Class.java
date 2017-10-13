package compiler;

import java.util.ArrayList;

public class Class {
	
	private String className;
	private int classLine;
	private String type; //whats this? public?
	public ArrayList<String>constructorName = new ArrayList<>(20);
	private ArrayList<Integer>constructorCounter = new ArrayList<>(20);
	private ArrayList<String>constructorParameter = new ArrayList<>(20);
	private ArrayList<Integer>mainCounter = new ArrayList<>(20);
	
	private ArrayList<String>objectInstantiatedParameter = new ArrayList<>(20);
	private ArrayList<Integer>objectInstantiatedLine = new ArrayList<>(20);
	
	public ArrayList<String>changedConstructorParameters = new ArrayList<>(20); //Holds edited parameters
	
	public boolean packageOk;
	public String packageName;
	
	public void addPackageBoolean(boolean exist){
		packageOk = exist;
	}
	public String getClassName(){
		return className;
	}
	public boolean getPackageBoolean(){
		return packageOk;
	}
	public String getPackageName(){
		return packageName;
	}
	public void addPackageName(String name){
		packageName = name;
	}
	
	public void addName(String name){
		className = name;
	}
	
	public void addLine(int x){
		classLine = x;
	}
	public int sizeArray(){
		return constructorParameter.size();
	}
	public void specifier(String classType){
		type = classType;
	}

	public void addConstructor(String name, int counter, String parameter){
		constructorName.add(name);
		constructorCounter.add(counter);
		constructorParameter.add(parameter);
	}
	
	public void addObjectInstantiationParameter(String x, int y){
		objectInstantiatedParameter.add(x);
		objectInstantiatedLine.add(y);
	}
	
	public String getName(){
		return className;
	}
	public int getLine(){
		return classLine;
	}
	public int getMainLine(){
		return mainCounter.get(0);
	}
	public String getSpecifier(){
		return type;
	}


	public void parameterEdit(){
		
		String holdParameter = ""; //Holds parameter as a string from constructorParameter array list
		String []splitParameter;   //String array to hold each part of a parameter per constructor
		
		for(int i = 0; i < constructorParameter.size(); i++){  //For loop goes through all constructors
			
			holdParameter = constructorParameter.get(i); //holdParameter holds constructor parameter at position i
			if(holdParameter.length()>2){
				
				splitParameter = holdParameter.trim().split(",");  //trims white spaces and splits at comma
				StringBuilder builder = new StringBuilder(); 		//a new string builder created
				
				for(int j = 0; j < splitParameter.length; j++){   //for loop to go through each part of parameter
					splitParameter[j] = splitParameter[j].trim();  //trims each part of parameter
					
					if(splitParameter[j].length() >= 3){
						if("int".equals(splitParameter[j].substring(0, 3))){  //if int 
							builder.append(splitParameter[j].substring(0, 3)); //this part of parameter = int
						}
					}
					if(splitParameter[j].length() >= 4){
						if("char".equals(splitParameter[j].substring(0, 4))){
							builder.append(splitParameter[j].substring(0, 4));
						}
					}
					if(splitParameter[j].length() >= 6){
						if("double".equals(splitParameter[j].substring(0, 6))){
							builder.append(splitParameter[j].substring(0, 6));
						}
					}
					if(splitParameter[j].length() >= 7){
						if("boolean".equals(splitParameter[j].substring(0, 7))){
							builder.append(splitParameter[j].substring(0, 7));
						}
					}
					if(splitParameter[j].length() >= 6){
						if("String".equals(splitParameter[j].substring(0, 6))){
							builder.append(splitParameter[j].substring(0, 6));
						}
					}
					if(splitParameter[j].length() >= 5){
						if("float".equals(splitParameter[j].substring(0, 5))){
								builder.append(splitParameter[j].substring(0, 5));
						}
					}
					if(splitParameter[j].length() >= className.length()){
						if(className.equals(splitParameter[j].substring(0, className.length()))){
							if(" ".equals(splitParameter[j].substring(className.length(), className.length()+1))){
								builder.append(splitParameter[j].substring(0, className.length()));
								System.out.println("Paramete rOBJECT " + splitParameter[j].substring(className.length(), className.length()+1) );
							}
						}
					}
				}
				String parameterString = builder.toString();		//builder.toString assigned to string
				changedConstructorParameters.add(parameterString);	//Add edited parameter to array list	
			}else{
				changedConstructorParameters.add(holdParameter);    //Add empty parameter to array list
			}
		}

	}
	
	
	//Find object instantiation and checks constructors to see if it is possible
	public boolean instantiationPossible(){
		
		int counter = 0;
		ArrayList<Boolean>objectFound = new ArrayList<>(20);
		if(objectInstantiatedParameter.size()>0){
			for(int a = 0; a < objectInstantiatedParameter.size();a++){
				objectFound.add(false);
			}
			for(int i = 0; i < objectInstantiatedParameter.size(); i++ ){
				for(int j = 0; j <  changedConstructorParameters.size(); j++){
					
					if(objectInstantiatedParameter.get(i).equals(changedConstructorParameters.get(j))){
						objectFound.add(i, true);
					}
				}
				
			}
		}
		for(int i = 0; i< objectInstantiatedParameter.size(); i++){
			if(objectFound.get(i) == false){
				return false;
			}
		}
		return true;
	}
	

	public boolean duplicateParameterConstructor(){
				
		int count = 0;
		
		for(int i = 0; i < constructorName.size()-1; i++){
			for(int j = i+1; j < constructorName.size(); j++){
				if(constructorName.get(i).equals(constructorName.get(j))){
					if(changedConstructorParameters.get(j).equals(changedConstructorParameters.get(i))){
						System.out.println("Error: Same constructors with same parameters on lines: " + constructorCounter.get(i) + " and " + constructorCounter.get(j));
						count++;
					}
				}
			}
		}
		
		if(count > 0){
			return true;
		}
		return false;	
	}
	
	public void addMain(int x){ //Adds line number to array list
		mainCounter.add(x);
	}
	
	public boolean duplicateMain(){
		
		if(mainCounter.size()>1){
			for(int i = 0; i < mainCounter.size()-1; i++){
				System.out.println("Error: More than 1 main created on lines " + mainCounter.get(i) + " and line: " + mainCounter.get(i+1));
			}
		}
		
		if(mainCounter.size() > 0){
			return true;
		}
		return false;
	}
	
	public boolean compare(String x){
		boolean found = false;
		for(int i = 0; i < changedConstructorParameters.size(); i++){
			if(x.equals(changedConstructorParameters.get(i))){
				found = true;
			}
		}
		
		if(found == true){
			return true;
		}
		return false;
	}
	
	
}
