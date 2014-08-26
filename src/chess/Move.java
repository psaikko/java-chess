package chess;

import java.awt.Point;

/**
 * Provides a convenient way of handling moves on the board
 * @author Paul
 */
public class Move {
    private Piece toMove;
    private Point moveTo;
    private Piece toCapture;
    
    /**
     * Creates a new move object
     * @param toMove the piece to move
     * @param moveTo the location to move to
     * @param toCapture the piece captured, null if none
     */
    public Move(Piece toMove, Point moveTo, Piece toCapture) {
        this.toMove = toMove;
        this.moveTo = moveTo;
        this.toCapture = toCapture;
    }
    
    /**
     * Returns the destination of the move
     * @return the destination of the move
     */
    public Point getMoveTo() {
        return moveTo;
    }
    
    /**
     * Returns the piece being moved
     * @return the piece being moved
     */
    public Piece getPiece() {
        return toMove;
    }
    
    /**
     * Returns the piece being captured
     * @return null if move doesn't capture a piece
     */
    public Piece getCaptured() {
        return toCapture;
    }
}
