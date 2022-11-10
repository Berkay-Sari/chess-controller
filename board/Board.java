import java.util.Arrays;

public class Board {
	private Piece[][] board = new Piece[8][8];
	//construct an empty board
	//set all to null
	public Board(){
		for(int x = 0; x < 8; x++) {
			for(int y = 0; y < 8; y++) {
				board[x][y] = null;
			}
		}
	}
	public boolean putPiece(Piece p, String pos){
		boolean isValid = false;
		//Check if the given position is in the range [a1-h8]. 
		for(int i = 0; i < 8; i++) {
			char harf = (char) (i + 'a');
			for(int j = 1; j <9; j++) {
				char sayi = (char) (j + 48);
				if(pos.equals(""+ harf + sayi)) {
					isValid = true;
				}
			}
		}
		if (isValid) {
			p.setPosition(pos);
			int x = 8 - (pos.charAt(1) - 48);
			int y = pos.charAt(0) -'a';
			board[x][y] = p;
			return true;
		}
		else {
			return false;
		}
	}
	public Piece getPiece(String pos){
		return board[8 - (pos.charAt(1) - 48)][pos.charAt(0) -'a'];
	}
	public boolean check(String color){
		boolean isCheck = false;

		Piece King = king(color);

		for(int i = 0; i < 8; i++) {
			// For each piece in Row
			for(Piece p : board[i]) {
				// if there is a Piece other than King
				if(p != null && ! p.getClass().getName().equals("King")) {
					// if the color of this Piece is opposite of given color, and If Piece can move to the location of the king, there may be check. 
					if( (!p.getColor().equals(King.getColor())) && p.canMove(King.getPosition())) {
						isCheck = true;
						// if the Piece is Knight then it definitely check.
						if(p.getClass().getName().equals("Knight")) {
							return true;
						}
						for(String isEmpty : checkLine(p.getPosition(), King.getPosition())) {
							
							// if there is a piece on the check line, it means there is no check.
							if(!isEmpty(isEmpty)) {
								if(isCloser(King.getPosition(), isEmpty, p.getPosition())) {
									isCheck = false;
									break;
								}
								else 
									isCheck = true;
							}
							else {
								isCheck = true;
							}
						}
						//If any piece has a check, there is no need to continue
						if(isCheck) {
							return true;
						}
					}
				}
			}
		}
		return isCheck;
	}

	/*
	 * There are three different situations for preventing checkmate:
	 * 1- Moving king to a position that cannot be attacked by enemy pieces.
	 * 2- Blocking the check line with an ally piece.
	 * 3- Capturing the checking piece.
	 */

	public boolean checkMate(String color){
		//no check means no checkMate.
		if(!check(color)) 
			return false;

		boolean canMove = true; //situation 1
		boolean canBlock = true; //situation 2
		boolean canCapture = false; //situation 3
		
		Piece King = king(color);
		
		String [] king_moves = King.getAllMoves();
		for(String str : king_moves) {
			if(getPiece(str) != null) {
				if(getPiece(str).getColor().equals(King.getColor())) {
					king_moves = remove(king_moves, str);
				}
			}
		}
		
		//	---	1 ---
		
		for(int i = 0; i < 8; i++) {
			// For each piece in Row
			for(Piece p : board[i]) {
				if(p != null) {
					// if the color of this Piece is opposite of given color
					if(!p.getColor().equals(King.getColor())) {
						for(String move : p.getAllMoves()) {
							//If this piece is attacking where the king can go
							if(Arrays.asList(king_moves).contains(move)) {
								//Knight and King cannot be blocked. 
								if(p.getClass().getName().equals("King") || p.getClass().getName().equals("Knight")) {
									king_moves = remove(king_moves, move);
								}
								else {
									String[] path = path(p.getPosition(), move);
									for(String str : path) {
										if(getPiece(str) != null) {
											continue;
										}
										king_moves = remove(king_moves, move);
									}
								}
							}
						}
					}
				}
			}
		}
		if(king_moves.length == 0)
			canMove = false;
		
		
		//	---	2 ---
		
		Piece [] checkers = checkers(color);
		for(Piece p : checkers) {
			if (p == null) {
				break;
			}
			String[] path = checkLine(p.getPosition(), King.getPosition());
			
			for(int i = 0; i < 8; i++) {
				// For each piece in Row
				for(Piece ally : board[i]) {
					boolean bool = true; // to check is there any piece on path.
					if(ally != null && !ally.getClass().getName().equals("King")) {
						// if the color of this Piece is same with King, then it is an ally.
						// and this piece shouldn't be pinned
	
						if(ally.getColor().equals(King.getColor()) && !isPin(ally.getPosition(), King)) {
							for(String move : ally.getAllMoves()) {
								//if ally piece can move to path then it blocked the checkLine
								if(Arrays.asList(path).contains(move)) {
									// if the Piece is Knight then it definitely can block.
									if(ally.getClass().getName().equals("Knight")) {
										if(!ally.isPlay()) {
											ally.setPlay(true);
											checkers = remove(checkers, p);
										}
									}
									else {
										String [] path2 = path(ally.getPosition(), move);
										for(String s : path2) {
											if(s.equals(ally.getPosition())) continue;
											if(!isEmpty(s)) {
												bool = false;
											}
										}
										//mark this piece as played, cause it may be block another checkLine for another enemy piece.
										if(bool && !ally.isPlay()) {
											ally.setPlay(true);
											checkers = remove(checkers, p);
										}
									}
								}
							}
						}
					}
				}
			}
		}
		for(Piece p : checkers) {
			if(p != null) {
				canBlock = false;
			}
		}

		//	---	3 ---

		checkers = checkers(color);
		String[] uncontrolledCapturingPieces = {"Knight", "King", "Pawn" }; //if the catching pieces is one of these, there is no need to check for any blocking.
		for(Piece p : checkers) {
			if (p == null) {
				break;
			}
			for(int i = 0; i < 8; i++) {
				for(Piece enemy : board[i]) {
					if(enemy != null) {
						if(enemy.getColor().equals(color)) {
				
							if(enemy.canMove(p.getPosition())) {
								int isThereAnyPiece = 0;
								if(Arrays.asList(uncontrolledCapturingPieces).contains(enemy.getClass().getName())) {
									canCapture = true;
								}
								else {
									String [] capturingPath = checkLine(enemy.getPosition(), p.getPosition());
									for(String str : capturingPath) {
										if(getPiece(str) != null) {
											isThereAnyPiece++;
											break;
										}
									}
									if(isThereAnyPiece == 0) {
										canCapture = true;
									}
								}
							}
						}
					}
				}
			}
		}
					
			
		/*
		 * If you want to see why there is no checkmate, follow the comment line below. 
		 * It will show you which situations are provided 	
		 */
		
		System.out.println("Move: " + canMove + "\tBlock: " + canBlock + "\tCapture: " + canCapture);
		
		return (!canMove) && (!canBlock) && (!canCapture);
	}

	public boolean isEmpty(String pos) {
		return board[8 - (pos.charAt(1) - 48)][pos.charAt(0) -'a'] == null;
	}

	public Piece[] checkers(String color) {
		boolean isCheck = false;
		Piece checkers[] = new Piece[10];
		int index = 0;
		Piece King = king(color);
		for(int i = 0; i < 8; i++) {
			// For each piece in Row
			for(Piece p : board[i]) {
				// if there is a Piece other than King
				if(p != null && ! p.getClass().getName().equals("King")) {
					// if the color of this Piece is opposite of given color, and If Piece can move to the location of the king, there may be check. 
					if( (!p.getColor().equals(King.getColor())) && p.canMove(King.getPosition())) {
						isCheck = true;
						if(p.getClass().getName().equals("Knight")) {
							isCheck = true;
						}
						else {
							for(String isEmpty : checkLine(p.getPosition(), King.getPosition())) {

								// if there is a piece on the check line, it means there is no check.
								if(!isEmpty(isEmpty)) {
									if(isCloser(King.getPosition(), isEmpty, p.getPosition())) {
										isCheck = false;
										break;
									}
									else 
										isCheck = true;
								}
							}
						}
						if(isCheck) {
							checkers[index++] = p;
						}
					}
					
				}
			}
		}
		return checkers;
	}
	
	//To find where is the King
	public Piece king(String color) {
		for(int i = 0; i < 8; i++) {
			for(Piece p : board[i]) {
				if(p != null) {
					if(p.getColor().equals(color) && p.getClass().getName().equals("King")) 
						return p;
				}
			}
		}
		return null;
	}
	
	public boolean isCloser(String king, String pos1, String pos2) {
		//For ex: king -> c3, pos1 -> f6, pos2 -> g7 return: true
		int harf_king = king.charAt(0) - 'a';
		int sayi_king = Integer.parseInt(king.substring(1));
		int harf_pos1 = pos1.charAt(0) - 'a';
		int sayi_pos1 = Integer.parseInt(pos1.substring(1));
		int harf_pos2 = pos2.charAt(0) - 'a';
		int sayi_pos2 = Integer.parseInt(pos2.substring(1));
		int harf_distance_1 = harf_king - harf_pos1; // 2 - 5
		int sayi_distance_1 = sayi_king - sayi_pos1; // 3 - 6
		int harf_distance_2 = harf_king - harf_pos2; // 2 - 6
		int sayi_distance_2 = sayi_king - sayi_pos2; // 3 - 7
		if(harf_distance_1 < 0) {
			harf_distance_1 = 0 - harf_distance_1;
		}
		if(harf_distance_2 < 0) {
			harf_distance_2 = 0 - harf_distance_2;
		}
		if(sayi_distance_1 < 0) {
			sayi_distance_1 = 0 - sayi_distance_1;
		}
		if(sayi_distance_2 < 0) {
			sayi_distance_2 = 0 - sayi_distance_2;
		}

		return (sayi_distance_1 + harf_distance_1) < (harf_distance_2 + sayi_distance_2);
	}

	public  String[] remove(String[] moves, String move) {
		if(!Arrays.asList(moves).contains(move))
			return moves;
		if(moves == null) 
			return null;
		else if(moves.length <= 0) 
			return moves;
		else {
			String[] newMoves = new String[moves.length - 1];
			int count = 0;
			for (String i : moves) {
				if (!i.equals(move)) {
					newMoves[count++] = i;
				}
			}
			return newMoves;
		}
	}
	
	public  Piece[] remove(Piece[] moves, Piece move) {
		if(!Arrays.asList(moves).contains(move))
			return moves;
		if(moves == null) 
			return null;
		else if(moves.length <= 0) 
			return moves;
		else {
			Piece[] newMoves = new Piece[moves.length - 1];
			int count = 0;
			for (Piece i : moves) {
				if( i == null ) continue;
				if (!i.equals(move)) {
					newMoves[count++] = i;
				}
			}
			return newMoves;
		}
	}
	
	/*
	 * pin is a situation brought on by an attacking piece
	 * in which a defending piece cannot move without exposing a more valuable defending piece on its other side to capture by the attacking piece.
	 */
	
	public boolean isPin(String ally, Piece King) {
		boolean isPin = false;
		for(int i = 0; i < 8; i++) {
			// For each piece in Row
			for(Piece p : board[i]) {
				if(p != null) {
					// if the color of this Piece is opposite of given color
					if(!p.getColor().equals(King.getColor())) {

						if(p.canMove(King.getPosition())) {
							String[] path = checkLine(p.getPosition(), King.getPosition());
							int isThereAnyPieceExceptPin = -1;
							for(String str : path) {
								if(getPiece(str) != null) {
									isThereAnyPieceExceptPin++;
								}
							}
							if(Arrays.asList(path).contains(ally) && isThereAnyPieceExceptPin == 0) {
								isPin = true;
							}
						}
					}
				}
			}
		}
		return isPin;
	}
	

	public String[] checkLine(String src, String dst) {

		String[] line = null;
		if(src.charAt(1) == dst.charAt(1)) {
			int src_pos = src.charAt(0) - 'a';
			int dst_pos = dst.charAt(0) - 'a';

			if(src.compareTo(dst) < 0) {

				line = new String[dst_pos - src_pos - 1];

				for(int i = 0; i < line.length; i++) {
					char column = (char)(src.charAt(0) + i + 1);
					String str = "" + column + src.charAt(1);
					line[i] = str;
				}
			}

			else {
				line = new String[src_pos - dst_pos - 1];

				for(int i = 0; i < line.length; i++) {
					char column = (char)(dst.charAt(0) + i + 1);
					String str = "" + column + dst.charAt(1);
					line[i] = str;
				}
			}
		}
		else if(src.charAt(0) == dst.charAt(0)) {
			int src_pos = Integer.parseInt(src.substring(1));
			int dst_pos = Integer.parseInt(dst.substring(1));

			if(src.compareTo(dst) < 0) {

				line = new String[dst_pos - src_pos - 1];

				for(int i = 0; i < line.length; i++) {
					int row = ++src_pos;
					String str = "" + src.charAt(0) + row;
					line[i] = str;
				}
			}

			else {
				line = new String[src_pos - dst_pos - 1];

				for(int i = 0; i < line.length; i++) {
					int row = ++dst_pos;
					String str = "" + dst.charAt(0) + row;
					line[i] = str;
				}
			}
		}
		else {
			int src_pos2 = Integer.parseInt(src.substring(1));
			int dst_pos2 = Integer.parseInt(dst.substring(1));
			//rigth
			if(src.charAt(0) < dst.charAt(0)) {
				//up
				if(src.charAt(1) < dst.charAt(1)) {
					
					line = new String[dst_pos2 - src_pos2 - 1];

					for(int i = 0; i < line.length; i++) {
						char column = (char)(src.charAt(0) + i + 1);
						int row = ++src_pos2;
						String str = "" + column + row;
						line[i] = str;
					}
				}
				//down
				else {

					line = new String[src_pos2 - dst_pos2 - 1];

					for(int i = 0; i < line.length; i++) {
						char column = (char)(dst.charAt(0) - i - 1);
						int row = ++dst_pos2;
						String str = "" + column + row;
						line[i] = str;
					}

				}
			}
			//left
			else {
				//up
				if(src.charAt(1) < dst.charAt(1)) {
					
					line = new String[dst_pos2 - src_pos2 - 1];

					for(int i = 0; i < line.length; i++) {
						char column = (char)(dst.charAt(0) + i + 1);
						int row = --dst_pos2;
						String str = "" + column + row;
						line[i] = str;
					}
				}
				//down
				else {
					
					line = new String[src_pos2 - dst_pos2 - 1];

					for(int i = 0; i < line.length; i++) {
						char column = (char)(dst.charAt(0) + i + 1);
						int row = ++dst_pos2;
						String str = "" + column + row;
						line[i] = str;
					}

				}

			}
		}
		return line;
	}
	
	//Almost same with checkLine method, but additionally contains dst position.
	public String[] path(String src, String dst) {

		String[] line = null;
		if(src.charAt(1) == dst.charAt(1)) {
			int src_pos = src.charAt(0) - 'a';
			int dst_pos = dst.charAt(0) - 'a';

			if(src.compareTo(dst) < 0) {

				line = new String[dst_pos - src_pos];

				for(int i = 0; i < line.length; i++) {
					char column = (char)(src.charAt(0) + i + 1);
					String str = "" + column + src.charAt(1);
					line[i] = str;
				}
			}

			else {
				line = new String[src_pos - dst_pos];

				for(int i = 0; i < line.length; i++) {
					char column = (char)(dst.charAt(0) + i + 1);
					String str = "" + column + dst.charAt(1);
					line[i] = str;
				}
			}
		}
		else if(src.charAt(0) == dst.charAt(0)) {
			int src_pos = Integer.parseInt(src.substring(1));
			int dst_pos = Integer.parseInt(dst.substring(1));

			if(src.compareTo(dst) < 0) {

				line = new String[dst_pos - src_pos];

				for(int i = 0; i < line.length; i++) {
					int row = ++src_pos;
					String str = "" + src.charAt(0) + row;
					line[i] = str;
				}
			}

			else {
				line = new String[src_pos - dst_pos];

				for(int i = 0; i < line.length; i++) {
					int row = ++dst_pos;
					String str = "" + dst.charAt(0) + row;
					line[i] = str;
				}
			}
		}
		else {
			int src_pos2 = Integer.parseInt(src.substring(1));
			int dst_pos2 = Integer.parseInt(dst.substring(1));
			//rigth
			if(src.charAt(0) < dst.charAt(0)) {
				//up
				if(src.charAt(1) < dst.charAt(1)) {
					
					line = new String[dst_pos2 - src_pos2];

					for(int i = 0; i < line.length; i++) {
						char column = (char)(src.charAt(0) + i + 1);
						int row = ++src_pos2;
						String str = "" + column + row;
						line[i] = str;
					}
				}
				//down
				else {

					line = new String[src_pos2 - dst_pos2];

					for(int i = 0; i < line.length; i++) {
						char column = (char)(dst.charAt(0) - i - 1);
						int row = ++dst_pos2;
						String str = "" + column + row;
						line[i] = str;
					}

				}
			}
			//left
			else {
				//up
				if(src.charAt(1) < dst.charAt(1)) {
					
					line = new String[dst_pos2 - src_pos2];

					for(int i = 0; i < line.length; i++) {
						char column = (char)(dst.charAt(0) + i + 1);
						int row = --dst_pos2;
						String str = "" + column + row;
						line[i] = str;
					}
				}
				//down
				else {
					
					line = new String[src_pos2 - dst_pos2 - 1];

					for(int i = 0; i < line.length; i++) {
						char column = (char)(dst.charAt(0) + i + 1);
						int row = ++dst_pos2;
						String str = "" + column + row;
						line[i] = str;
					}

				}

			}
		}

		return line;
	}
	
}