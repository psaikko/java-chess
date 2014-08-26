package chess;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.ArrayList;
/**
 * A subclass of Piece
 * @author Paul
 */
public class Knight extends Piece{
       
    private final int imageNumber = 1;
    
    /**
     * Creates a new knight
     * @param location location of the piece
     * @param color color of the piece
     */
    public Knight(Point location, Color color) {
        numMoves = 0;
        this.color = color;
        this.location = location;
    }

    /**
     * Private constructor used for making copies of the piece
     * @param location location of the piece
     * @param color color of the piece
     * @param moves the number of moves made by the piece
     */
    private Knight(Point location, Color color, int moves) {
        this.numMoves = moves;
        this.color = color;
        this.location = location;
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
     * Returns a copy of the knight
     * @return a copy of the knight
     */
    public Piece clone() {
        return new Knight(new Point(this.location.x, this.location.y),
                this.color, this.numMoves);
    }
    
    /**
     * A method to get all the valid moves for a piece
     * @param board the board to get valid moves on for the piece.
     * @param checkKing whether or not to check if the move puts own king
     *  in check. Necessary to prevent infinite recursion.
     * @return List containing valid move points
     */
    public List<Move> getValidMoves(Board board, boolean checkKing) {       
        int x = location.x;
        int y = location.y;

        List<Move> moves = new ArrayList<Move>();

        // if no board given, return empty list
        if (board == null)
            return moves;
        
        // check L-shapes
        addIfValid(board, moves, new Point(x + 1, y + 2));
        addIfValid(board, moves, new Point(x - 1, y + 2));
        addIfValid(board, moves, new Point(x + 1, y - 2));
        addIfValid(board, moves, new Point(x - 1, y - 2));
        addIfValid(board, moves, new Point(x + 2, y - 1));
        addIfValid(board, moves, new Point(x + 2, y + 1));
        addIfValid(board, moves, new Point(x - 2, y - 1));
        addIfValid(board, moves, new Point(x - 2, y + 1));    

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
     * Checks if a given move is valid
     * @param list list to add the move to
     * @param pt move to check validity of
     */
    private void addIfValid(Board board, List<Move> list, Point pt) {
        // if the location is valid
        if(board.validLocation(pt)) {
            // and the location does not contain same color piece
            Piece pc = board.getPieceAt(pt);
            if(pc == null || pc.getColor() != this.color) {
                // all the move to the list
                list.add(new Move(this, pt, pc));
            }
        }
    }
}
