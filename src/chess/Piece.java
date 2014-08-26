package chess;

import java.awt.Point;
import java.util.List;
import java.io.Serializable;
import java.awt.image.BufferedImage;
/**
 * A generic Piece class. Other pieces inherit this class.
 * @author Paul
 */
public abstract class Piece implements Serializable, Cloneable{
    public static enum Color {White, Black};

    // [0]:pawn [1]:knight [2]:bishop [3]:rook [4]:queen [5]:king
    protected static BufferedImage[] whiteImages;
    protected static BufferedImage[] blackImages;
    
    protected int numMoves;
    protected Color color;
    protected Point location;
    
    /**
     * Returns the number of moves made by the piece
     * @return number of moves
     */
    public int getNumberOfMoves() {
        return numMoves;
    }
    
    /**
     * Gets the color of the piece
     * @return a java.awt.Color object representing the piece's color
     */
    public Color getColor() {
        return this.color;
    }
    
    /**
     * Moves the piece to a new location. Use getValidMoves() to check if a 
     * point is valid.
     * @param p the Point to move to
     */
    public void moveTo(Point p) {
        this.location = p;
        numMoves++;
    }
    
    /**
     * Returns the location (Point) of the piece
     * @return the location of the piece
     */
    public Point getLocation() {
        return this.location;
    }
    
    /**
     * Returns the index of the Piece's image in an array.
     *  Can be used for determining the relative value of the piece.
     *  Pieces have the following indices:
     *  [0]:pawn [1]:knight [2]:bishop [3]:rook [4]:queen [5]:king
     * @return array index
     */
    public abstract int getImageNumber() ;
    
    /**
     * Gets the white image of the piece
     * @return white image
     */
    public abstract BufferedImage getWhiteImage() ; 
    
    /**
     * Gets the black image of the piece
     * @return black image
     */
    public abstract BufferedImage getBlackImage() ;
    
    /**
     * Gets a list of the valid moves the piece can make
     * @return valid moves
     */
    public abstract List<Move> getValidMoves(Board board, boolean checkKing);
    
    /**
     * Returns a copy of the piece
     * @return a copy of the piece
     */
    @Override
    public abstract Piece clone();
    
    /**
     * Sets the array of BufferedImages to be used for drawing black pieces.
     *  This must method must be called before attempting to draw pieces.
     * @param images Array of buffered images. Images should be in the following
     *  order: [0]:pawn [1]:knight [2]:bishop [3]:rook [4]:queen [5]:king
     */
    public static void setWhiteImages(BufferedImage[] images) {
        whiteImages = images;
    }
  
    /**
     * Sets the array of BufferedImages to be used for drawing white pieces.
     *  This must method must be called before attempting to draw pieces.
     * @param images Array of buffered images. Images should be in the following
     *  order: [0]:pawn [1]:knight [2]:bishop [3]:rook [4]:queen [5]:king
     */
    public static void setBlackImages(BufferedImage[] images) {
        blackImages = images;
    }

}
