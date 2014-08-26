package chess;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.ArrayList;
/**
 * A subclass of piece
 * @author Paul
 */
public class Pawn extends Piece {
    
    private final int imageNumber = 0;
    
    public boolean enPassantOk = false;
    
    /**
     * Creates a new pawn
     * @param location location of the piece
     * @param color color of the piece
     */
    public Pawn(Point location, Color color) {
        this.numMoves = 0;
        this.color = color;
        this.location = location;
    }    
    
    /**
     * Private constructor used to make copies of the piece
     * @param location location of the piece
     * @param color color of the piece
     * @param moves number of moves the piece has made
     * @param captureableEnPassant whether the pawn can currently be captured 
     *  "en passant" or not
     */
    private Pawn(Point location, Color color, int moves, boolean captureableEnPassant) {
        enPassantOk = captureableEnPassant;
        this.numMoves = moves;
        this.color = color;
        this.location = location;
    }

    /**
     * Returns a copy of the pawn
     * @return a copy of the pawn
     */
    public Piece clone() {
        return new Pawn(new Point(this.location.x, this.location.y),
                this.color, this.numMoves, this.enPassantOk);
    }
    
    /**
     * Returns the index of the Piece's image in an array.
     *  Can be used for determining the relative value of the piece.
     *  Pieces have the following indices:
     *  [0]:pawn [1]:knight [2]:bishop [3]:rook [4]:queen [5]:king
     * @return array index
     */
    public int getImageNumber() {
        return imageNumber;
    }
    
    /**
     * Returns the white image for this piece
     * @return white image
     */
    public BufferedImage getWhiteImage() {
        return whiteImages[imageNumber];
    }
    
    /**
     * Returns the black image for this piece
     * @return black image
     */
    public BufferedImage getBlackImage() {
        return blackImages[imageNumber];
    }
    
    /**
     * A method to get all the valid moves for a piece
     * @param board the board to get valid moves on for the piece.
     * @param checkKing whether or not to check if the move puts own king
     *  in check. Necessary to prevent infinite recursion.
     * @return List containing valid move points
     */
    public List<Move> getValidMoves(Board board, boolean checkKing) {
        List<Move> moves = new ArrayList<Move>();

        // if no board given, return empty list
        if (board == null)
            return moves;

        // checks moves where the pawn advances a rank
        advance(board, moves);
        // checks moves where the pawn captures another piece
        capture(board, moves);
        // checks en passant moves
        enPassant(board, moves);

        // check that move doesn't put own king in check
        if (checkKing)
            for(int i = 0; i < moves.size(); i++)
                if (board.movePutsKingInCheck(moves.get(i), this.color)) {
                    // if move would put king it check, it is invalid and
                    // is removed from the list
                    moves.remove(moves.get(i));
                    // iterator is decremented due to the size of the list
                    // decreasing.
                    i--;
                }
        return moves;
    }
    
    /**
     * Adds moves in which the pawn advances to the list of moves
     * @param moves the list of moves
     */
    private void advance(Board board, List<Move> moves) {
        int x = location.x;
        int y = location.y;
        
        Piece pc;
        Point pt;
        int move;
        
        if (color == Color.White)
            move = -1;
        else
            move = 1;
                
        pt = new Point(x, y + move);
        if (board.validLocation(pt)) {
            pc = board.getPieceAt(pt);            
            if(pc == null) {
                moves.add(new Move(this, pt, pc));     
                
                pt = new Point(x, y + move * 2);
                if (board.validLocation(pt)) {
                    pc = board.getPieceAt(pt);
                    if(pc == null && numMoves == 0)
                        moves.add(new Move(this, pt, pc));
                }
            } 
        }
    }

    /**
     * Adds moves in which the pawn captures to the list of moves
     * @param moves the list of moves
     */
    private void capture(Board board, List<Move> moves) {
        int x = location.x;
        int y = location.y;
        
        Piece pc;
        Point pt;
        int move;
        
        if (color == Color.White)
            move = -1;
        else
            move = 1;
            
        pt = new Point(x - 1, y + move);
        if (board.validLocation(pt)) {
            pc = board.getPieceAt(pt);            
            if (pc != null)
                if(this.color != pc.getColor())
                    moves.add(new Move(this, pt, pc));    
        }
        pt = new Point(x + 1, y + move);
        if (board.validLocation(pt)) {
            pc = board.getPieceAt(pt);           
            if (pc != null)
                if(this.color != pc.getColor())
                    moves.add(new Move(this, pt, pc));       
        }
    }
    
    /**
     * Adds moves in which the pawn captures "en passant" to the list of moves
     * @param moves the list of moves
     */
    private void enPassant(Board board, List<Move> moves) {
        int x = location.x;
        int y = location.y; 
        
        if (this.color == Color.White && this.location.y == 3) {
            if(canCaptureEnPassant(board, new Point(x - 1, y)))
                moves.add(new Move(this, new Point(x - 1, y - 1),
                        board.getPieceAt(new Point(x - 1, y))));
            if(canCaptureEnPassant(board, new Point(x + 1, y)))
                moves.add(new Move(this, new Point(x + 1, y - 1),
                        board.getPieceAt(new Point(x + 1, y)))); 
        }
        if (this.color == Color.Black && this.location.y == 4) {
            if(canCaptureEnPassant(board, new Point(x - 1, y)))
                moves.add(new Move(this, new Point(x - 1, y + 1),
                        board.getPieceAt(new Point(x - 1, y))));
            if(canCaptureEnPassant(board, new Point(x + 1, y)))
                moves.add(new Move(this, new Point(x + 1, y + 1),
                        board.getPieceAt(new Point(x + 1, y))));            
        }
    }
    
    /**
     * Checks if the pawn can capture another pawn by en passant
     * @param pt location of the other pawn
     * @return true if can be captured
     */
    private boolean canCaptureEnPassant(Board board, Point pt) {
        Piece temp = board.getPieceAt(pt);
        if(temp != null)
            if (temp instanceof Pawn && temp.getColor() !=  this.color)
                if (((Pawn)temp).enPassantOk)
                    return true;
        return false;
    }
}
