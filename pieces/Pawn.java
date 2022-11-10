import java.util.Arrays;

public class Pawn extends Piece{

	public Pawn(String color) {
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
		
		int up_down[] = new int[3]; 
        int left_right[] = new int[3];
        
		if(getColor() == "black") {
			
			if(X == 1) {
			
				up_down = new int[4];
				
				up_down[0] = 1;
				up_down[1] = 2;
				up_down[2] = 1;
				up_down[3] = 1;
				
				left_right = new int[4];
				
				left_right[0] = 1;
				left_right[1] = 0;
				left_right[2] = 0;
				left_right[3] = -1;
				
				for (int i = 0; i < 4; i++) { 

		            int x = X + up_down[i]; 
		            int y = Y + left_right[i]; 
		  
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
			else {
				up_down[0] = 1;
				up_down[1] = 1;
				up_down[2] = 1;
				
				left_right[0] = 1;
				left_right[1] = 0;
				left_right[2] = -1;
				
			}
		}
		if(getColor() == "white") {
			
			if(X == 6) {
				
				up_down = new int[4];

				up_down[0] = -1;
				up_down[1] = -2;
				up_down[2] = -1;
				up_down[3] = -1;

				left_right = new int[4];

				left_right[0] = 1;
				left_right[1] = 0;
				left_right[2] = 0;
				left_right[3] = -1;

				for (int i = 0; i < 4; i++) { 

					int x = X + up_down[i]; 
					int y = Y + left_right[i]; 

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
			else {
				up_down[0] = -1;
				up_down[1] = -1;
				up_down[2] = -1;
				
				left_right[0] = 1;
				left_right[1] = 0;
				left_right[2] = -1;
			}
		}

        for (int i = 0; i < 3; i++) { 

            int x = X + up_down[i]; 
            int y = Y + left_right[i]; 
  
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
