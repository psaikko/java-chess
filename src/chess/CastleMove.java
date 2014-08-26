package chess;

import java.awt.Point;

/**
 * A subclass of move to allow castling
 * @author Paul
 */
public class CastleMove extends Move {  
    private Piece rook;
    private Point moveRookTo;
    
    /**
     * Creates a new castle object
     * @param king king to move
     * @param moveKing point to move king to
     * @param rook rook to move
     * @param moveRook point to move rook to
     */
    public CastleMove(Piece king, Point moveKing, Piece rook, Point moveRook) {
        super(king, moveKing, null);
        this.moveRookTo = moveRook;
        this.rook = rook;
    }
    
    /**
     * Returns the point the rook involved in the castling will move to
     * @return where the rook will move
     */
    public Point getRookMoveTo() {
        return moveRookTo;
    }
    
    /**
     * Returns the rook involved in the castling
     * @return the rook
     */
    public Piece getRook() {
        return rook;
    }
}
