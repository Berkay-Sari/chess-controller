import java.util.Arrays;

public class King extends Piece {

	public King(String color) {
		super(color);
	}
	
	public boolean canMove(String newPos){
		return Arrays.asList(getAllMoves()).contains(newPos);
	}
	public String[] getAllMoves() {
		String moves [] = new String[8];
		int index = 0;
		
		int X = 8 - (getPosition().charAt(1) - 48); 
		int Y = getPosition().charAt(0) -'a';
		
		int left_right[] = { -1, -1, -1, 0, 1, 1, 1, 0 }; 
        int up_down[] = { 1, 0, -1, -1, -1, 0, 1, 1 }; 
        
        for (int i = 0; i < 8; i++) { 

            int x = X + left_right[i]; 
            int y = Y + up_down[i]; 
  
            if (x >= 0 && y >= 0 && x < 8 && y < 8) 
                moves[index++] = ""+(char)(y+'a') + (char)(56 - x); 
           
        } 
        
        String str[]= new String[index];
        
        for(int i = 0; i < index; i++) {
        	str[i] = moves[i];
        }
        
        Arrays.sort(str);
        
     
        return str;
	}	


}
