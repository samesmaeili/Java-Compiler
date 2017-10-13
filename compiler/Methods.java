package compiler;

import java.util.ArrayList;

public class Methods {

	
	private ArrayList<String>methodName = new ArrayList<>(20);
	private ArrayList<Integer>methodCounter = new ArrayList<>(20);
	private ArrayList<String>methodParameter = new ArrayList<>(20);
	
	private ArrayList<String>changedFunctionParameters = new ArrayList<>(20); //Holds edited parameters without variable names
	
	public void add(String name, int counter, String param){
		methodName.add(name);
		methodCounter.add(counter);
		methodParameter.add(param);	
	}
	public void parametersEdit(){
		
		String functionHold = "";  //Holds function parameters from arraylist
		String []functionSplits;  //String split array
		
		for(int i = 0; i < methodName.size(); i++){
			functionHold = methodParameter.get(i);  //hold function parameter
			if(functionHold.length()>2){  //If length is greater than 2
				functionSplits = functionHold.trim().split(",");
				StringBuilder functionBuilder = new StringBuilder();
				for(int j = 0; j < functionSplits.length; j++){
					functionSplits[j] = functionSplits[j].trim();
					if(functionSplits[j].length() >= 3){
						if("int".equals(functionSplits[j].substring(0, 3))){
							functionBuilder.append(functionSplits[j].substring(0, 3));
						}
					}
					if(functionSplits[j].length() >= 4){
						if("char".equals(functionSplits[j].substring(0, 4))){
							functionBuilder.append(functionSplits[j].substring(0, 4));
						}
					}
					if(functionSplits[j].length() >= 6){
						if("double".equals(functionSplits[j].substring(0, 6))){
							functionBuilder.append(functionSplits[j].substring(0, 6));
						}
					}
					if(functionSplits[j].length() >= 7){
						if("boolean".equals(functionSplits[j].substring(0, 7))){
							functionBuilder.append(functionSplits[j].substring(0, 7));
						}
					}
					if(functionSplits[j].length() >= 6){
						if("String".equals(functionSplits[j].substring(0, 6))){
							functionBuilder.append(functionSplits[j].substring(0, 6));
						}
					}
					if(functionSplits[j].length() >= 5){
						if("float".equals(functionSplits[j].substring(0, 5))){
								functionBuilder.append(functionSplits[j].substring(0, 5));
						}
					}
				}
				String functionParameterString = functionBuilder.toString();
				changedFunctionParameters.add(functionParameterString);		
			}else{
				changedFunctionParameters.add(functionHold); //if empty parameters
			}
		}
			
	}
	public boolean duplicates(){
		//for loop to find duplicate parameters, fix duplicates
		int count = 0;
		for(int i = 0; i < changedFunctionParameters.size()-1; i++){
			for(int j = i+1; j < changedFunctionParameters.size(); j++){
				if(methodName.get(i).equals(methodName.get(j))){
					if(changedFunctionParameters.get(j).equals(changedFunctionParameters.get(i))){
						System.out.println("Error: Same function names with same parameters on lines: " + methodCounter.get(i) + " and " + methodCounter.get(j));
						count++;
					}
				}
			}
		}	
		if(count != 0){
			return true;
		}
		return false; //no duplicates
	}
	
}
