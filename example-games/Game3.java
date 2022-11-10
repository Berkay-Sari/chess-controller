public class Game3 {
	public static void main(String[] args){
		Board B = new Board();
        B.putPiece(new King("black"), "a8");
        B.putPiece(new King("white"), "c7");
        B.putPiece(new Rook("white"), "a1");

        if (B.check("white"))
            System.out.println("Beyazin sahi tehdit altinda");
        if (B.check("black"))
            System.out.println("Siyahin sahi tehdit altinda");

        if (B.checkMate("white"))
            System.out.println("Siyah kazandi");
        if (B.checkMate("black"))
            System.out.println("Beyaz kazandi");

        B.putPiece(new Pawn("black"), "b2");
        
        if (B.check("white"))
            if (B.checkMate("white"))
                System.out.println("Kazanan: Siyah.");
            else
                System.out.println("Siyah mat edemedi.");

        if (B.check("black"))
            if (B.checkMate("black"))
                System.out.println("Kazanan: Beyaz.");
            else
                System.out.println("Beyaz mat edemedi.");
	}
}
