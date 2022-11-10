public class Game2 {
    public static void main(String[] args){
        Board B = new Board();
        B.putPiece(new King("black"), "d8");
        B.putPiece(new King("white"), "h1");
        B.putPiece(new Rook("black"), "e1");
        B.putPiece(new Bishop("white"), "h2");
        B.putPiece(new Pawn("white"), "f2");
        B.putPiece(new Pawn("white"), "g2");
        if (B.check("white"))
            System.out.println("Beyazin sahi tehdit altinda");
        if (B.check("black"))
            System.out.println("Siyahin sahi tehdit altinda");

        if (B.checkMate("white"))
            System.out.println("Siyah kazandi");
        if (B.checkMate("black"))
            System.out.println("Beyaz kazandi");

        B.putPiece(new Queen("black"), "h8");

        if (B.checkMate("white"))
            System.out.println("Kazanan: Siyah");
        if (B.checkMate("black"))
            System.out.println("Kazanan: Beyaz");
    }
}