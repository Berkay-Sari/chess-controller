
public class Game4 {
	public static void main(String[] args){
		Board B = new Board();
        B.putPiece(new King("black"), "a6");
        B.putPiece(new Rook("white"), "b2");
        B.putPiece(new Queen("white"), "c6");
        B.putPiece(new Queen("black"), "a5");
        B.putPiece(new King("white"), "g7");

        if (B.check("white"))
            System.out.println("Beyazin sahi tehdit altinda");
        if (B.check("black"))
            System.out.println("Siyahin sahi tehdit altinda");

        if (B.checkMate("white"))
            System.out.println("Siyah kazandi");
        if (B.checkMate("black"))
            System.out.println("Beyaz kazandi");

	}
}
