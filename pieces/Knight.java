import java.util.Arrays;

public class Knight extends Piece {
	//Bos tahtada yapabilecegi hareketler
	public boolean canMove(String newPos){
		return Arrays.asList(getAllMoves()).contains(newPos);
	}
	public String[] getAllMoves() {

		String moves [] = new String[8];
		int index = 0;
		
		int X = 8 - (getPosition().charAt(1) - 48); 
		int Y = getPosition().charAt(0) -'a';
		
		int left_right[] = { 2, 1, -1, -2, -2, -1, 1, 2 }; 
        int up_down[] = { 1, 2, 2, 1, -1, -2, -2, -1 }; 
        
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
	
	public Knight(String color) {
		super(color);
	}
}
