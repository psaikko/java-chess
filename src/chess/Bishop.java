package chess;

import java.awt.Point;
import java.util.List;
import java.util.ArrayList;
import java.awt.image.BufferedImage;
/**
 * A subclass of Piece
 * @author Paul
 */
public class Bishop extends Piece{
       
    private final int imageNumber = 2;
    
    /**
     * Creates a new bishop
     * @param location location of the piece
     * @param color color of the piece
     */
    public Bishop(Point location, Color color) {
        this.numMoves = 0;
        this.color = color;
        this.location = location;
    }

    /**
     * Private constructor used for making copies of the piece
     * @param location location of the piece
     * @param color color of the piece
     * @param moves the number of moves made by the piece
     */
    private Bishop(Point location, Color color, int moves) {
        this.numMoves = moves;
        this.color = color;
        this.location = location;
    }
    
    /**
    * @return index of the piece's image in an array
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
     * Returns a copy of the bishop
     * @return a copy of the bishop
     */
    public Piece clone() {
        return new Bishop(new Point(this.location.x, this.location.y),
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
        List<Move> moves = new ArrayList<Move>();

        // if no board given, return empty list
        if (board == null)
            return moves;
        
        // add moves in diagonal lines to the list
        addMovesInLine(board, moves, 1, 1);
        addMovesInLine(board, moves, -1, 1);
        addMovesInLine(board, moves, 1, -1);
        addMovesInLine(board, moves, -1, -1);

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
     * Adds valid moves in a straight line to the list
     * @param moves list to add to
     * @param xi x direction of line (-1/0/1)
     * @param yi y direction of line (-1/0/1)
     */
    private void addMovesInLine(Board board, List<Move> moves, int xi, int yi) {
        int x = location.x;
        int y = location.y;
        
        Point pt = new Point(x + xi, y + yi);
        Piece pc;
        
        while(board.validLocation(pt)) {
            pc = board.getPieceAt(pt);
            if(pc == null) {
                moves.add(new Move(this, pt, pc));
            } else if(pc.getColor() != this.color) {
                moves.add(new Move(this, pt, pc));
                break;
            } else {
                break;
            }
            pt = new Point(pt.x + xi, pt.y + yi);
        }
    }
}
