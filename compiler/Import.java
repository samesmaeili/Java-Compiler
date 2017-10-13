package compiler;

import java.util.ArrayList;

	
    public class Import{
	
	private ArrayList<String>importList = new ArrayList<>(20);
	private ArrayList<Integer>importCounter = new ArrayList<>(20);
	private boolean duplicates = false;
	
	
	
	Import(){
		
	}
	public int sizeArray(){
		return importList.size();
	}
	public void add(String name, int counter){
		importList.add(name);
		importCounter.add(counter);
	}
	
	public boolean repeats(){
		
		if(importList.size() >0 ){
			 for(int i = 0; i < importList.size()-1; i++){
				 for(int j = i+1; j < importList.size(); j++){
					 if(importList.get(i).equals(importList.get(j))){
						 if(i!=j){
							 System.out.println("Duplicate import names on line " + importCounter.get(i) + " and line " + importCounter.get(j));
							 duplicates = true;
						 }
					 }
				}
			}
		 }
		if(duplicates == false){
			return false;
		}
		return true;
	}
	
	public boolean objectIsImported(String x, String y){
		for(int i = 0; i < importList.size(); i++){
			if(importList.get(i).equals(x) || importList.get(i).equals(y)){
				return true;
			}
		}
		return false;
		
	}
	
}
