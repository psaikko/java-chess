package chess;

import java.io.Serializable;
import java.awt.Point;
import java.util.List;
import java.util.ArrayList;

/**
 * Contains all the game pieces and general game rule logic
 * @author Paul
 */
public class Board implements Serializable, Cloneable {
    private Board previousState = null;
    private Piece.Color turn;
    private List<Piece> pieces = new ArrayList<Piece>();
    
    private Piece inCheck = null;
    private Piece lastMoved = null;
    private Ai ai = null;
    
    /**
     * Sets an Ai for the board
     * @param computerPlayer
     */
    public void setAi(Ai computerPlayer) {
        this.ai = computerPlayer;
    }
    
    /**
     * Returns the Ai object for the board. 
     * @return the Ai object of the board. Null if 2-player game.
     */
    public Ai getAi() {
        return ai;
    }
    
    /**
     * If a king is in check, returns it. Null otherwise.
     * @return king in check
     */
    public Piece getPieceInCheck() {
        return inCheck;
    }
    
    /**
     * Returns the piece that was moved last;
     * @return last moved piece
     */
    public Piece getLastMovedPiece() {
        return lastMoved;
    }
    
    /**
     * Creates a new board object
     * @param initPieces true to initializa all pieces, false to leave board empty
     */
    public Board(boolean initPieces) {
        turn = Piece.Color.White;
        
        if (initPieces) {        
            // black pieces
            pieces.add(new Pawn(new Point(0, 1), Piece.Color.Black));
            pieces.add(new Pawn(new Point(1, 1), Piece.Color.Black));
            pieces.add(new Pawn(new Point(2, 1), Piece.Color.Black));
            pieces.add(new Pawn(new Point(3, 1), Piece.Color.Black));
            pieces.add(new Pawn(new Point(4, 1), Piece.Color.Black));
            pieces.add(new Pawn(new Point(5, 1), Piece.Color.Black));
            pieces.add(new Pawn(new Point(6, 1), Piece.Color.Black));
            pieces.add(new Pawn(new Point(7, 1), Piece.Color.Black));

            pieces.add(new Rook(new Point(0, 0), Piece.Color.Black));
            pieces.add(new Knight(new Point(1, 0), Piece.Color.Black));
            pieces.add(new Bishop(new Point(2, 0), Piece.Color.Black));
            pieces.add(new Queen(new Point(3, 0), Piece.Color.Black));
            pieces.add(new King(new Point(4, 0), Piece.Color.Black));
            pieces.add(new Bishop(new Point(5, 0), Piece.Color.Black));
            pieces.add(new Knight(new Point(6, 0), Piece.Color.Black));
            pieces.add(new Rook(new Point(7, 0), Piece.Color.Black));

            // white pieces
            pieces.add(new Pawn(new Point(0, 6), Piece.Color.White));
            pieces.add(new Pawn(new Point(1, 6), Piece.Color.White));
            pieces.add(new Pawn(new Point(2, 6), Piece.Color.White));
            pieces.add(new Pawn(new Point(3, 6), Piece.Color.White));
            pieces.add(new Pawn(new Point(4, 6), Piece.Color.White));
            pieces.add(new Pawn(new Point(5, 6), Piece.Color.White));
            pieces.add(new Pawn(new Point(6, 6), Piece.Color.White));
            pieces.add(new Pawn(new Point(7, 6), Piece.Color.White));

            pieces.add(new Rook(new Point(0, 7), Piece.Color.White));
            pieces.add(new Knight(new Point(1, 7), Piece.Color.White));
            pieces.add(new Bishop(new Point(2, 7), Piece.Color.White));
            pieces.add(new Queen(new Point(3, 7), Piece.Color.White));
            pieces.add(new King(new Point(4, 7), Piece.Color.White));
            pieces.add(new Bishop(new Point(5, 7), Piece.Color.White));
            pieces.add(new Knight(new Point(6, 7), Piece.Color.White));
            pieces.add(new Rook(new Point(7, 7), Piece.Color.White));
        }
    }
    
    /**
     * Private constructor used to create a deep copy of the board
     * @param turn the color of the pieces to move next
     * @param previousState previous state of the board
     * @param pieces all the pieces on the board
     * @param lastMoved piece to move last
     * @param inCheck king in check
     * @param ai ai present on the board
     */
    private Board(Piece.Color turn, Board previousState, List<Piece> pieces,
            Piece lastMoved, Piece inCheck, Ai ai) {
        this.turn = turn;
        if (inCheck != null)
            this.inCheck = inCheck.clone();
        if (lastMoved != null)
            this.lastMoved = lastMoved.clone();
        this.ai = ai;
        this.previousState = previousState;
        for(Piece p : pieces) {
            this.pieces.add(p.clone());
        }
    }
    
    /**
     * Returns the list of all the pieces on the board
     * @return List<Piece> containing all pieces on the board
     */
    public List<Piece> getPieces() {
        return pieces;
    }
    
    /**
     * Returns the piece at the specified location
     * @param p the specified location
     * @return the piece at the location. null if no piece found
     */
    public Piece getPieceAt(Point p) {
        for(Piece pc : pieces) {
            if(pc.getLocation().x == p.x &&
               pc.getLocation().y == p.y)
                return pc;
        }
        return null;
    }
    
    /**
     * Removes the piece from the board
     * @param p the piece to remove
     */
    public void removePiece(Piece p) {
        if (pieces.contains(p)) {
            pieces.remove(p);
            return;
        }
    }
    
    /**
     * Adds a piece on to the board
     * @param p Piece to add
     */
    public void addPiece(Piece p) {
        pieces.add(p);
    }
    
    /**
     * Removes the piece at the given point
     * @param p Point to remove the piece from
     */
    public void removePieceAt(Point p) {
        Piece temp = null;
        for(Piece pc : pieces) {
            if (pc.getLocation().equals(p)) {
                temp = pc;
                break;
            }
        }
        if (temp != null)
            pieces.remove(temp);
    }
    
    /**
     * Returns the color that has the current turn
     * @return the color to move next
     */
    public Piece.Color getTurn() {
        return turn;
    }
    
    /**
     * Performs the given move. Does not check validity. Use only moves from
     *  the Pieces' getValidMoves() methods.
     * @param m move to perform
     * @param playerMove whether or not this move is made directly by a human player. 
     * Determines whether a dialog will be shown on pawn promotion.
     */
    public void doMove(Move m, boolean playerMove) {
        this.previousState = this.clone();
        
        // implementing en passant rule
        for(Piece pc : pieces)
            if (pc.getColor() == turn && pc instanceof Pawn)
                ((Pawn)pc).enPassantOk = false;
        
        // if move is castling
        if (m instanceof CastleMove) {
            CastleMove c = (CastleMove)m;
            c.getPiece().moveTo(c.getMoveTo());
            c.getRook().moveTo(c.getRookMoveTo());
        } else {
            if(m.getCaptured() != null);
                this.removePiece(m.getCaptured());
            
            // implementing en passant rule
            if (m.getPiece() instanceof Pawn)
                if (Math.abs(m.getPiece().getLocation().y - m.getMoveTo().y) == 2)
                    ((Pawn)m.getPiece()).enPassantOk = true;                
            
            m.getPiece().moveTo(m.getMoveTo());    
            
            // promote pawn if reached final rank
            checkPawnPromotion(m.getPiece(), playerMove);
        }
        
        this.lastMoved = m.getPiece();
        this.inCheck = kingInCheck();
        
        // change the color of pieces moving next
        turn = Piece.Color.values()[(turn.ordinal() + 1) % 2];
    }
    
    /**
     * Checks if the given piece is a pawn that needs to be promoted. 
     * If it is an ai piece, automatically promotes it to 
     * @param pawn Piece to check
     * @param showDialog Whether or not to ask the user what to promote pawn to. 
     * If false, automatically promotes to Queen.
     */
    private void checkPawnPromotion(Piece pawn, boolean showDialog) {
        if(pawn instanceof Pawn && (pawn.getLocation().y == 0 || pawn.getLocation().y == 7)) {
            Piece promoted;
            
            // if ai, promote automatically to queen
            if (!showDialog || (ai != null && ai.getColor() == pawn.getColor())) {
                promoted = new Queen(pawn.getLocation(), pawn.getColor());
            } else { 
                // else, give the player a choice
                Object type = javax.swing.JOptionPane.showInputDialog(
                        null, "", 
                        "Choose promotion:",
                        javax.swing.JOptionPane.QUESTION_MESSAGE,
                        null,
                        new Object[] { "Queen", "Rook", "Bishop", "Knight" },
                        "Queen");
                
                // will be null if JOptionPane is cancelled or closed
                // default to queen in that case
                if (type == null)
                    type = "Queen";
                
                // interpret the JOptionPane result
                if (type.toString().equals("Queen"))
                    promoted = new Queen(pawn.getLocation(), pawn.getColor());
                else if (type.toString().equals("Rook"))
                    promoted = new Rook(pawn.getLocation(), pawn.getColor());
                else if (type.toString().equals("Bishop"))
                    promoted = new Bishop(pawn.getLocation(), pawn.getColor());
                else
                    promoted = new Knight(pawn.getLocation(), pawn.getColor());
            }

            // remove pawn and add promoted piece to board
            pieces.remove(pawn);
            pieces.add(promoted);
        }
    }
    
    /**
     * Returns a new board object, with the move executed
     * @param m move to execute
     * @return new board
     */
    public Board tryMove(Move m) {
        // creates a copy of the board
        Board helper = this.clone();
        
        if (m instanceof CastleMove) {
            // creates a copy of the move for the copied board
            CastleMove c = (CastleMove)m;
            Piece king = helper.getPieceAt(c.getPiece().getLocation());
            Piece rook = helper.getPieceAt(c.getRook().getLocation());
            
            // performs the move on the copied board
            helper.doMove(new CastleMove(king, c.getMoveTo(),
                    rook, c.getRookMoveTo()), false);
        } else {       
            // creates a copy of the move for the copied board
            Piece capture = null;
            if(m.getCaptured() != null)
                capture = helper.getPieceAt(m.getCaptured().getLocation());

            Piece moving = helper.getPieceAt(m.getPiece().getLocation());

            // performs the move on the copied board
            helper.doMove(new Move(moving,
                    m.getMoveTo(), capture), false);
        }
        
        // returns the copied board with the move executed
        return helper;
    }  
    
    /**
     * Used to find out if a king is in check
     * @return a Piece (King) if one is in check, else null
     */
    private Piece kingInCheck() {
        // go through all the pieces on the board
        for(Piece pc : pieces)
            // go through all the moves that can be made by the piece
            for(Move mv : pc.getValidMoves(this, false))
                // if a move would result in a king being captured
                if (mv.getCaptured() instanceof King) {
                    // that king is in check
                    this.inCheck = mv.getCaptured();
                    // return it.
                    return mv.getCaptured();
                }
        return null;
    }
    
    /**
     * Checks if a move puts a king in check
     * @param m move to check
     * @param kingColor color of the king to check
     * @return true if move puts king in check
     */
    public boolean movePutsKingInCheck(Move m, Piece.Color kingColor) {
        // create a copy of the board
        Board helper = tryMove(m);
        
        // go through all the pieces on the board
        for(Piece pc : helper.getPieces())
            // if the color is different than the color 
            // of the king we are checking for
            if (pc.color != kingColor)
                // go through all of it's available moves
                for(Move mv : pc.getValidMoves(helper, false))
                    // if a move would result in the capture of a king
                    if (mv.getCaptured() instanceof King) 
                        return true;
        return false;
    }
    
    /**
     * Checks if either color can make no more moves
     * @return true can signify either a checkmate or a stalemate.
     *  Use kingInCheck() to determine which.
     */
    public boolean gameOver() {
        // create an array for all the moves that can be made by 
        // black pieces, white pieces
        List<Move> whiteMoves = new ArrayList<Move>();
        List<Move> blackMoves = new ArrayList<Move>();
        
        // all moves to the arrays for all pieces
        for(Piece p : pieces) {
            if(p.getColor() == Piece.Color.White)
                whiteMoves.addAll(p.getValidMoves(this, true));
            else
                blackMoves.addAll(p.getValidMoves(this, true));
        }
        
        // if either side can make no valid moves, the game is over
        return (whiteMoves.size() == 0 || blackMoves.size() == 0);
    }
    
    /**
     * Returns a copy of the Board
     * @return a copy of this board
     */
    @Override
    public Board clone() {
        return new Board(turn, previousState, pieces, lastMoved, inCheck, ai);
    }
    
    /**
     * Gets the board's last state.
     * @return state of the board before the last move
     */
    public Board getPreviousState() {
        if(previousState != null)
            return previousState;
        return this;
    }
    
    /**
     * Checks if a position is within the confines of the board.
     *  Does not check if position is occupied.
     * @param p point to check validity of
     * @return true if valid, false if not
     */
    public boolean validLocation(Point p) {
        return (p.x >= 0 && p.x <= 7) && (p.y >= 0 && p.y <= 7);
    }
}
