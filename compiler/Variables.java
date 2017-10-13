package compiler;

import java.util.ArrayList;

public class Variables {

	private ArrayList<String>instanceName = new ArrayList<>(5); //name of variable
	private ArrayList<Integer>instanceCounter = new ArrayList<>(5); //line number 
	private ArrayList<String>instanceSpecifier = new ArrayList<>(5); //specifier (public?)
	private ArrayList<String>instanceType = new ArrayList<>(5); //int?
	private ArrayList<Boolean>instanceStatic = new ArrayList<>(5);
	
	public void add(String specifier, String isStatic, String type, String name, int counter){
		
		instanceSpecifier.add(specifier);
		if(isStatic != null){
			instanceStatic.add(true);
		}else{
			instanceStatic.add(false);
		}
		instanceType.add(type);
		instanceName.add(name);
		instanceCounter.add(counter);
	}
	
	public boolean duplicates(){
		int counter = 0;
		if(instanceName.size() > 0){
			for(int i = 0; i < instanceName.size()-1; i++){
				for(int j = i+1; j < instanceName.size(); j++){
					if(instanceName.get(i).equals(instanceName.get(j))){
						System.out.println("Error: Duplicate names for instance variables found on lines " + instanceCounter.get(i) +  " and " + instanceCounter.get(j));
						counter++;
					}
				}
			}
		}
		if(counter > 0){
			return true;
		}
		return false;		
	}
	
	public void staticLoop(){
		for(int i = 0; i < instanceName.size();i++){
			System.out.println(instanceStatic.get(i));
		}
	}
}
