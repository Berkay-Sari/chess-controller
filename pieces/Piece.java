
public class Piece {
	private String position;
	private String color;
	private boolean isPlay = false;
	public boolean canMove(String newPosition){
		return true;
	}
	//Hamleleri alfabetik sirada doner
	public String[] getAllMoves() {
		String [] moves = new String[64];
		for(int i = 0; i < 8; i++) {
			char harf = (char) (i + 'a');
			for(int j = 1; j <9; j++) {
				char sayi = (char) (j + 48);
				moves[i*8 + j - 1] = ""+ harf + sayi;
			}
		}
		return moves;
		
		/*
		a1 a2 a3 ... a8
		b1 b2 b3 ... b8
		c1 c2 c3 ... c8
		.
		.
		.
		*/
		
	}
	public Piece(String color) {
		this.color = color;
	}
	public void setPosition(String newPosition){
		position = newPosition;
	}
	public void setColor(String newColor){
		color = newColor;
	}
	public String getPosition(){
		return position;
	}
	public String getColor(){
		return color;
	}
	
	public boolean isPlay() {
		return isPlay;
	}
	public void setPlay(boolean isPlay) {
		this.isPlay = isPlay;
	}
}

