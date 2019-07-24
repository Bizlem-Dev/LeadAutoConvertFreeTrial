package leadconverter.create.rule;

import java.util.HashMap;
import java.util.Map;

public class Test {
	
	public static void main(String[] args)  
    { 
		String original_category="NoExplore";
		String move_category="Entice";
		
		int original_category_position=getCategoryPosition(original_category);
		int move_category_position=getCategoryPosition(move_category);
		
		if(move_category_position>original_category_position){
			System.out.println("Move");
		}else{
			System.out.println("No Need to Move");
		}
    } 
      
    public static int getCategoryPosition(String category)  
    { 
    	int position=0;
    	HashMap<String, Integer> category_map = new HashMap<String, Integer>(); 
        category_map.put("Explore", 1); 
        category_map.put("Entice", 2); 
        category_map.put("Inform", 3);
        category_map.put("Warm", 4);
        category_map.put("Connect", 5);
        
        if (category_map.containsKey(category))  
        { 
        	position = category_map.get(category); 
            System.out.println("value for key "+category+" is : " + position); 
        } 
        return position;
         
    } 

}
