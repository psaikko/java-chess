package chess;

import java.util.List;
import java.util.ArrayList;
import java.io.Serializable;
/**
 * Contains AI logic for the chess game
 * @author Paul
 */
public class Ai implements Serializable{
    
    private Piece.Color aiColor;
    private int depth;

    /**
     * Creates a new AI object
     * @param color
     * @param depth
     */
    public Ai(Piece.Color color, int depth) {
        this.aiColor = color;
        this.depth = depth;
    }
    
    /**
     * Returns the color of the pieces the AI controls
     * @return color of the AI pieces
     */
    public Piece.Color getColor() {
        return aiColor;
    }
    
    /**
     * Returns a move for the ai to make based on a min/max algorithm
     *  with alpha-beta pruning. Based on generic pseudocode explanation of
     *  algorithm from http://ai-depot.com/articles/minimax-explained/
     * @param game current state of the board
     * @return best move
     */
    public Move getMove(Board game) {     
        // if given a null board, return null
        if (game == null)
            return null;
        // if it isn't the ai's turn, return null
        if (game.getTurn() != aiColor)
            return null;
        // initialize best value and best move variables
        int bestValue = Integer.MIN_VALUE;
        Move bestMove = null;
        
        // get the best move for the ai (max) from the available moves
        for (Move m : getMoves(game)) {
            // get the value of the move (min)
            int moveValue = min(game.tryMove(m), depth - 1, bestValue, Integer.MAX_VALUE);
            
            // if the value is > than bestValue, current move is best
            if (moveValue > bestValue || bestValue == Integer.MIN_VALUE) {
                bestValue = moveValue;
                bestMove = m;
            }
        }
        
        return bestMove;
    }
    
    /**
     * Returns the value of the best move for the ai for the given board
     * @param game game state after the the last player move
     * @param depth current depth of the analysis
     * @param alpha best max move
     * @param beta best min move
     * @return value of the move
     */
    private int max(Board game, int depth, int alpha, int beta) {
        // end search if game over or depth limit reached
        if (depth == 0)
            return valueOfBoard(game);

        List<Move> possibleMoves = getMoves(game);

        // if no moves can be made, game has ended
        if (possibleMoves.size() == 0)
            return valueOfBoard(game);

        // get the best move for the ai (max) from the available moves
        for(Move m : possibleMoves) {
            // get the value of the move
            int moveValue = min(game.tryMove(m), depth - 1, alpha, beta);
            
            // see if it is better than previous best move
            if (moveValue > alpha) {
                alpha = moveValue;
            }            
            // if the alpha value (value of the best move found for the ai by
            // this method) is greater than the beta value (the best move for 
            // the opponent so far found by the min method that called this
            // method) then we know that the min method will not choose this
            // path and we can stop the search
            if (alpha >= beta)
                return alpha;
        }

        return alpha;
    }
    
    /**
     * Returns the value of the best move for the player for the given board
     * @param game game state after the last ai move
     * @param depth current depth of the analysis
     * @param alpha best max move
     * @param beta best min move
     * @return value of the move
     */
    private int min(Board game, int depth, int alpha, int beta) {
        // end search if game over or depth limit reached
        if (depth == 0)
            return valueOfBoard(game);

        List<Move> possibleMoves = getMoves(game);

        // if no moves can be made, game has ended
        if (possibleMoves.size() == 0)
            return valueOfBoard(game);

        // get the best move for the player (min) from the available moves
        for(Move m : possibleMoves) {
            int moveValue = max(game.tryMove(m), depth - 1, alpha, beta);
            if (moveValue < beta) {
                beta = moveValue;
            }             
            // if the alpha value (best move found for the ai so far by the
            // max method that called this method) is greater than the beta 
            // value (best move found for the opponent by this method) then
            // we know that the max method will not choose this path and we
            // can stop the search.
            if (alpha >= beta)
                return beta;
        }       
        return beta;
    }
    
    /**
     * Returns all the possible moves for the board.
     * @param game Board to get moves for
     * @return list of possible moves.
     */
    private List<Move> getMoves(Board game) {
        // initialize an arraylist
        List<Move> moves = new ArrayList<Move>();
        
        // for each piece
        for (Piece p : game.getPieces())
            // of the color that moves next
            if (p.getColor() == game.getTurn())
                // add all valid moves to the list
                moves.addAll(p.getValidMoves(game, true));
        return moves;
    }
    
    /**
     * Calculates the value of a board for the AI.
     * @param gameBoard Board to evaluate
     * @return value of the board
     */
    private int valueOfBoard(Board gameBoard) {
        int value = 0;
        int aiPieces = 0;
        int aiMoves = 0;
        int playerPieces = 0;
        int playerMoves = 0;
        int aiCaptures = 0;
        int playerCaptures = 0;
        
        // give the board state a value based on the number of pieces on the
        // board and the number of available moves 

        for(Piece pc : gameBoard.getPieces())
            if(pc.getColor() == aiColor) {
                // account for number of pieces on board
                aiPieces += valueOfPiece(pc);

                if (aiColor == gameBoard.getTurn())
                {
                    List<Move> validMoves = pc.getValidMoves(gameBoard, true);
                    for(Move m : validMoves) {
                        // account for how many moves can be made
                        aiMoves++;
                        if (m.getCaptured() != null) {
                            // account for possible captures
                            aiCaptures += valueOfPiece(m.getCaptured());
                        }
                    }
                }
            } else {
                playerPieces += valueOfPiece(pc);

                if (aiColor != gameBoard.getTurn())
                {
                    List<Move> validMoves = pc.getValidMoves(gameBoard, true);
                    for(Move m : validMoves) {
                        // account for how many moves can be made
                        playerMoves++;
                        if (m.getCaptured() != null) {
                            // account for possible captures
                            playerCaptures += valueOfPiece(m.getCaptured());
                        }
                    }
                }
            }

        value = (aiPieces - playerPieces) + (aiMoves - playerMoves)
                + (aiCaptures - playerCaptures);

        // if a side can make no valid moves, the game is over
        if (gameBoard.getTurn() == aiColor && aiMoves == 0)
            // if the ai can make no moves, it has lost. this is bad.
            value = Integer.MIN_VALUE;
        else if (gameBoard.getTurn() != aiColor && playerMoves == 0)
            // if the player can make no more moves, we win. this is good.
            value = Integer.MAX_VALUE;
        

        return value;
    }
       
    /**
     * A method for evaluating the value of a piece
     * @param pc piece to evaluate
     * @return value of the piece 
     */
    private int valueOfPiece(Piece pc) {
        return (int)Math.pow(pc.getImageNumber() + 1, 3) * 100;
    }
}
