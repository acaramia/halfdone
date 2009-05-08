/**
 * Tic Tac Toe
 *
 * Copyright ¬ 1998-2007 Research In Motion Ltd.
 */

package com.rim.samples.device.tictactoe;

import com.rim.samples.device.resource.*;
import net.rim.blackberry.api.blackberrymessenger.*;
import net.rim.device.api.ui.*;
import net.rim.device.api.ui.component.*;
import net.rim.device.api.ui.container.*;
import net.rim.device.api.i18n.*;
import net.rim.device.api.system.Bitmap;
import net.rim.device.api.system.*;


/**
 * The TicTacToe sample implements a tic tac toe game where the computer plays "O".
 * You can also play a two-player game via BlackBerry Messenger.
 */
public class TicTacToe extends UiApplication implements TicTacToeResResource, DialogClosedListener
{
    //members ------------------------------------------------------------------
    private int _difficulty;
    private MainScreen _mainScreen;
    private DifficultyField _difficultyField = new DifficultyField(
            _resources.getString(TICTACTOE_DIFFICULTY),
            new String[] {
                _resources.getString(TICTACTOE_EASY),
                _resources.getString(TICTACTOE_MEDIUM),
                _resources.getString(TICTACTOE_HARD)
            },
            _MEDIUM);
    private boolean _firstTurnFlag;
    private boolean _middleStartFlag;
    private boolean _gameOverFlag;
    private boolean _twoPlayerFlag;
    private boolean _yourTurnFlag;
    private SquareField[] _squareFields = {
            new SquareField(_TOP_LEFT),
            new SquareField(_TOP_CENTER),
            new SquareField(_TOP_RIGHT),
            new SquareField(_LEFT),
            new SquareField(_CENTER),
            new SquareField(_RIGHT),
            new SquareField(_BOTTOM_LEFT),
            new SquareField(_BOTTOM_CENTER),
            new SquareField(_BOTTOM_RIGHT)
    };
    private int[][] _winLineOccurrences;
    private int[] _winLineCount;

    //statics ------------------------------------------------------------------

    static final private boolean _isColour = Graphics.isColor();

    private static ResourceBundle _resources = ResourceBundle.getBundle(TicTacToeResResource.BUNDLE_ID, TicTacToeResResource.BUNDLE_NAME);

    private static final Bitmap _xBitmap =
           Bitmap.getBitmapResource("x.png");
    private static final Bitmap _oBitmap =
           Bitmap.getBitmapResource("o.png");
    private static final Bitmap _blankBitmap;
    private static final Bitmap _verticalBarBitmap =
           Bitmap.getBitmapResource("vertical_bar.gif");
    private static final Bitmap _horizontalBarBitmap =
           Bitmap.getBitmapResource("horizontal_bar.gif");

    private static final int _NONE = 0;
    private static final int _X = 1;
    private static final int _O = 2;
    private static final int _QUIT = -1;

    private static final int _TOP_LEFT = 0;
    private static final int _TOP_CENTER = 1;
    private static final int _TOP_RIGHT = 2;
    private static final int _LEFT = 3;
    private static final int _CENTER = 4;
    private static final int _RIGHT = 5;
    private static final int _BOTTOM_LEFT = 6;
    private static final int _BOTTOM_CENTER = 7;
    private static final int _BOTTOM_RIGHT = 8;

    private static final int _ONE_X = 1;
    private static final int _TWO_X = 2;
    private static final int _THREE_X = 3;
    private static final int _TWO_O = 8;
    private static final int _THREE_O = 12;
    
    private static final int _TIE = 8;

    private static final int _EASY = 0;
    private static final int _MEDIUM = 1;
    private static final int _HARD = 2;

    private static final int[][] winLines = {
        {_TOP_LEFT, _TOP_CENTER, _TOP_RIGHT},           // horizontal top line
        {_LEFT, _CENTER, _RIGHT},                       // horizontal center line
        {_BOTTOM_LEFT, _BOTTOM_CENTER, _BOTTOM_RIGHT},  // horizontal bottom line
        {_TOP_LEFT, _LEFT, _BOTTOM_LEFT},               // vertical left line
        {_TOP_CENTER, _CENTER, _BOTTOM_CENTER},         // vertical center line
        {_TOP_RIGHT, _RIGHT, _BOTTOM_RIGHT},            // vertical right line
        {_TOP_LEFT, _CENTER, _BOTTOM_RIGHT},            // diagonal line
        {_TOP_RIGHT, _CENTER, _BOTTOM_LEFT}             // diagonal line
    };
    
    static Session _twoPlayerSession;   // used for communication with a BlackBerry Messenger Contact
    private static final Dialog _twoPlayerRequestDialog = new Dialog( _resources.getString( TICTACTOE_DIALOG_REQUESTING ), 
                                                                      new Object [] { _resources.getString( TICTACTOE_CANCEL ) },
                                                                      new int [] { Dialog.CANCEL },
                                                                      0,
                                                                      null );
    private TurnField _turnField = new TurnField();
    private static TicTacToe _theGame;
    private int _yourMark;
    private int _theirMark;
    
    static {
         _blankBitmap = Graphics.isColor() ? 
           Bitmap.getBitmapResource("blanksquare.png") :
           Bitmap.getBitmapResource("blanksquareBW.png");
    }

    public static void main(String[] args)
    {
        if( args != null && args.length > 0 && args[0].equals( "init" ) ) {
            ApplicationDescriptor tictactoe = new ApplicationDescriptor( ApplicationDescriptor.currentApplicationDescriptor(), new String [] {} );
            BlackBerryMessenger bbm = BlackBerryMessenger.getInstance();
            if( bbm != null ) {
                // make sure BlackBerry Messenger is installed before attempting to register listeners
                bbm.addSessionRequestListener( TTTRequestListener.getInstance(), tictactoe );
                bbm.registerService( TTTService.getInstance(), _resources.getString( TICTACTOE_SERVICE_NAME ), tictactoe );
            }
        } else {
            _theGame = new TicTacToe();
            //To make the application enter the event thread and start processing messages, we invoke the enterEventDispatcher method
            _theGame.enterEventDispatcher();
        }
    }

    /**
     * The TicTacToe constructor. Creates all the RIM UI components and pushes the application's root screen onto the UI stack
     */
    TicTacToe()
    {        
        // MainScreen is the basic screen or frame class of the RIM UI.
        _mainScreen = new TicTacToeMainScreen(Field.USE_ALL_HEIGHT|Field.FIELD_LEFT);

        // Add a field to the title region of the MainScreen. We use a simple LabelField here. The ELLIPSIS option truncates
        // the label text with "..." if the text was too long for the space available.
        _mainScreen.setTitle(new LabelField(_resources.getString(TICTACTOE_TITLE), LabelField.ELLIPSIS | LabelField.USE_ALL_WIDTH));

        // Initialization
        newGame();

        // Add the difficulty selector (easy, medium, or hard)
        _mainScreen.add(_difficultyField);

        // Add a blank line.
        _mainScreen.add(new RichTextField(Field.NON_FOCUSABLE));

        Bitmap spacerBitmap = new Bitmap((Graphics.getScreenWidth()-92)/2,2);
        Graphics g = new Graphics(spacerBitmap);
        g.clear();
        
        // Layout the board using FlowFieldManager since there is no grid layout.
        // The horizontal bar gif has a width equal to the full width of the screen so
        // that the board will actually lay itself out properly.
        FlowFieldManager ffm = new FlowFieldManager(Field.FIELD_HCENTER);
        
        ffm.add(new BitmapField(spacerBitmap));
        ffm.add(_squareFields[_TOP_LEFT]);
        ffm.add(new BitmapField(_verticalBarBitmap));
        ffm.add(_squareFields[_TOP_CENTER]);
        ffm.add(new BitmapField(_verticalBarBitmap));
        ffm.add(_squareFields[_TOP_RIGHT]);
        ffm.add(new BitmapField(spacerBitmap));
        ffm.add(new BitmapField(spacerBitmap));
        ffm.add(new BitmapField(_horizontalBarBitmap));
        ffm.add(new BitmapField(spacerBitmap));
        ffm.add(new BitmapField(spacerBitmap));
        ffm.add(_squareFields[_LEFT]);
        ffm.add(new BitmapField(_verticalBarBitmap));
        ffm.add(_squareFields[_CENTER]);
        ffm.add(new BitmapField(_verticalBarBitmap));
        ffm.add(_squareFields[_RIGHT]);
        ffm.add(new BitmapField(spacerBitmap));
        ffm.add(new BitmapField(spacerBitmap));
        ffm.add(new BitmapField(_horizontalBarBitmap));
        ffm.add(new BitmapField(spacerBitmap));
        ffm.add(new BitmapField(spacerBitmap));
        ffm.add(_squareFields[_BOTTOM_LEFT]);
        ffm.add(new BitmapField(_verticalBarBitmap));
        ffm.add(_squareFields[_BOTTOM_CENTER]);
        ffm.add(new BitmapField(_verticalBarBitmap));
        ffm.add(_squareFields[_BOTTOM_RIGHT]);
        ffm.add(new BitmapField(spacerBitmap));


        _mainScreen.add(ffm);
        
        // add a field that tells the player whose turn it is
        _mainScreen.add( _turnField );

        // Set the focus to the top left square.
        _squareFields[_TOP_LEFT].setFocus();

        //We've completed construction of our UI objects. Push the MainScreen instance onto the UI stack for rendering.
        pushScreen(_mainScreen);
    }
    
    static TicTacToe getGame() {
        return _theGame;
    }

    /**
     * Initialize the variables and clear the board to start a new game (re-uses the main screen).
     */
    private void newGame() {
        for (int i=_TOP_LEFT; i<=_BOTTOM_RIGHT; ++i) {
            _squareFields[i].setSquare(_NONE);
        };
        _firstTurnFlag = true;
        _middleStartFlag = false;
        _gameOverFlag = false;
        _twoPlayerFlag = false;
        _yourMark = _X;
        _theirMark = _O;

        // win lines going through each square
        _winLineOccurrences = new int[][] {
          {0, 3, 6},      // square 0 - top left
          {0, 4},         // square 1 - top center
          {0, 5, 7},      // square 2 - top right
          {1, 3},         // square 3 - left
          {1, 4, 6, 7},   // square 4 - center
          {1, 5},         // square 5 - right
          {2, 3, 7},      // square 6 - bottom left
          {2, 4},         // square 7 - bottom center
          {2, 5, 6}       // square 8 - bottom right
       };

       _winLineCount = new int[] {0, 0, 0, 0, 0, 0, 0, 0};
    }
    
    void newTwoPlayerGame( boolean firstTurn, Session session ) {
        newGame();
        _yourTurnFlag = firstTurn;
        _twoPlayerFlag = true;
        if( firstTurn ) {
            _turnField.yourTurn();
            _yourMark = _X;
            _theirMark = _O;
        } else {
            _turnField.theirTurn();
            _yourMark = _O;
            _theirMark = _X;
        }
        
        _twoPlayerSession = session;
        ApplicationDescriptor application = ApplicationDescriptor.currentApplicationDescriptor();
        _twoPlayerSession.addListener( new TTTSessionListener(), application );
    }
    
    /**
     * The Tic Tac Toe game's main screen.  Adds a "new game" item to its menu, and
     * prevents the save screen from being displayed when the user selects the default
     * close item (there is nothing to save).
     */
    private class TicTacToeMainScreen extends MainScreen
    {
        private MenuItem _newGameItem;       // menu item for starting a new game
        private MenuItem _newTwoPlayerGame;  // menu item for starting a two player game via BlackBerry Messenger
        
        /**
         * Constructs a new main screen instance with provided style and creates
         * the "new game" menu item.
         * @param style Style(s) for this new screen.
         */
        public TicTacToeMainScreen( long style ) {
            super( style );
            
            _newGameItem = new MenuItem( TicTacToe.this._resources, TicTacToe.TICTACTOE_MENUITEM_NEWGAME, 100000, 10 ) {
                public void run() {
                    TicTacToe.this.newGame();
                }
            };
            
            _newTwoPlayerGame = new MenuItem( TicTacToe.this._resources, TicTacToe.TICTACTOE_MENUITEM_NEWTWOGAME, 100000, 10 ) {
                public void run() {
                    BlackBerryMessenger bbm = BlackBerryMessenger.getInstance();
                    if( bbm != null ) {
                        MessengerContact player = BlackBerryMessenger.getInstance().chooseContact();
                        TicTacToe.this.requestTwoPlayerGame( player );
                    } else {
                        Dialog.alert( TicTacToe.this._resources.getString( TICTACTOE_WARNING_NO_BBM ) );
                    }
                }
            };
        }
        
        /**
        * Invoked when a key is pressed.
        */
        public boolean keyChar(char key, int status, int time) {
            switch (key) {
                // ENTER, SPACE, x, and X can be used to place the X (in addition to track wheel click)
                // On some devices, the CONTROL_SELECT key can be used as well.
                case Characters.ENTER: case Characters.SPACE: case 'x': case 'X': case Characters.CONTROL_SELECT:
                    if( _twoPlayerFlag ) {
                        if( _yourTurnFlag ) {
                            youMove();
                        }
                    } else {
                        youMove();
                    }
                    break;
                // q, Q, m, and M are used to bring up the menu (in addition to track wheel click - except
                // in empty squares)
                case 'q': case 'Q': case 'm': case 'M':
                    _mainScreen.onMenu( 0 );
                    break;
                // some devices use the CONTROL_<direction> keys to navigate.
                // we want to make sure that these special keys aren't absorbed by the screen.
                case Characters.CONTROL_DOWN: case Characters.CONTROL_RIGHT: case Characters.CONTROL_UP: case Characters.CONTROL_LEFT:
                    return false;
            }
    
            return super.keyChar(key, status, time);
        }        
        
        /**
         * Creates this screen's menu, which consists of the default main
         * screen menu as well as a "new game" menu item.
         * @param menu Menu to which items are added.
         * @param instance The instance of the desired menu.
         */
        protected void makeMenu( Menu menu, int instance ) {
            super.makeMenu( menu, instance );
            menu.add( _newGameItem );
            menu.add( _newTwoPlayerGame );
        }
        
        /**
         * Overrides the default implementation.  Completes the player's move.
         * 
         * @return True if the game is not over; otherwise, false.
         */
        public boolean navigationClick( int status, int time ) {
            if( _twoPlayerFlag ) {
                if( _yourTurnFlag ) {
                    youMove();
                }
            } else {
                youMove();
            }
            //once the game is done we want to return true to show the menu
            //otherwise keep returning false to allow clicks to play
            return !_gameOverFlag;
        }        
        
        /**
         * Unconditionally closes this screen without prompting to save.
         * @return True because the screen is always closed.
         */
        public boolean onClose() {
            if( _twoPlayerSession != null && _twoPlayerSession.isOpen() ) {
                _twoPlayerSession.close();
            }
            close();
            return true;
        }
    }
    
    private final class TurnField extends RichTextField {
        public TurnField() {
            super( Field.NON_FOCUSABLE | Field.FIELD_HCENTER | Field.USE_ALL_WIDTH );
            yourTurn();
        }
        public void yourTurn() {
            setText( TicTacToe.this._resources.getString( TICTACTOE_TURN_YOURS ) );
        }
        public void theirTurn() {
            setText( TicTacToe.this._resources.getString( TICTACTOE_TURN_THEIRS ) );
        }
    }

    /**
     * DifficultyField - start a new game when the difficutly is changed.
     */
    private class DifficultyField extends ObjectChoiceField implements FieldChangeListener {
        public DifficultyField(String label, Object[] choices, int initialIndex) {
            super(label, choices, initialIndex);
            setChangeListener(this);
        }

        public void fieldChanged(Field field,
                         int context) {
             if (context == 0) {                
                 newGame();
             }
        }


    }

    /**
     * Squares on the board are represented using this SquareField class.  Each
     * square maintains a reference to its position (index) on the board, and exposes
     * a method for setting the square to an "X", "O", or a blank square (for newGame).
     */
    private class SquareField extends BitmapField {
        private int _squareIndex;
        private int _state = _NONE;
        private Bitmap _overlay;

        public SquareField(int squareIndex) {
            super(_blankBitmap);
            _squareIndex = squareIndex;
        }

        public int getGameState() {
            return _state;
        }

        public int getSquareIndex() {
            return _squareIndex;
        }

        private boolean setSquare(int value) {

            if (value == _NONE) {
                setBitmap(_blankBitmap);
                _overlay = null;
                _state = value;
                return false;
            }
            if (_state == _NONE) {
                int[] wlo = _winLineOccurrences[_squareIndex];

                // Add 4 to the count in each win line going through this square
                // if O was played.  Add 1 if X was played.  This way we can determine
                // how many X's and O's are in each win line (i.e. a count of
                // 6 means there's two X's and one O in a line).
                if (value == _O) {
                   if ( TicTacToe.this._isColour ) _overlay = _oBitmap;
                   else setBitmap(_oBitmap);
                   for (int i=wlo.length-1; i>=0; --i) {
                       _winLineCount[wlo[i]] += 4;
                   }
               } else if (value == _X) {
                   if ( TicTacToe.this._isColour ) _overlay = _xBitmap;
                   else setBitmap(_xBitmap);
                   for (int i=wlo.length-1; i>=0; --i) {
                       ++_winLineCount[wlo[i]];
                   }
               }
               _winLineOccurrences[_squareIndex] = new int[] {};
               //setFocus();
               invalidate();
               _state = value;
               return true;
           }
           return false;
        }
        
        public void paint(Graphics g)
        {
            super.paint(g);
            
            if ( TicTacToe.this._isColour && _overlay != null )
            {
                Bitmap b = _overlay;
                //just paint the overlay directly, don't clear the underlying bitmap
                g.drawBitmap(0, 0, b.getWidth(), b.getHeight(), b, 0, 0);
            }
        }

        /* squares that have been played in cannot receive focus */
        public boolean isFocusable() {
            if (_gameOverFlag) {
                return false;
            } else if (_state == _NONE) {
                return true;
            } else {
                return false;
            }
        }
    }

    /**
     * Returns the index of the first empty square in a given line
     * (where line is one of the 8 possible winning lines).  This method is
     * called only in cases where the specified line contains one and only one
     * empty square (this is not explicitly checked).
     */
    private int getLastSquareInLine(int line) {
       for (int i=0; i<3; ++i) {
           if (_squareFields[winLines[line][i]].getGameState() == _NONE) {
               return winLines[line][i];
            }
       }
       throw new java.lang.IllegalStateException("No empty squares in row " + line);
    }

    /**
     * Calculates and plays the computer's (O's) move.
     * First checks if it can win on this turn.
     * <p>If difficulty is "easy" it then just picks the next available square.
     * <p>If difficulty is "medium" it checks if it has to block the user from winning.
     * <p>If difficulty is "hard" it also checks to stop the user from forming a "fork"
     * situation, opening up two possibilities of winning at once.  These situations are
     * limited by starting in either the center or the corner, and by further checks on the
     * second turn if the user started in the center.
     */
    private void computerMoves() {
        _difficulty = _difficultyField.getSelectedIndex();

        if (_difficulty == _HARD) {
            // first turn
            if (_firstTurnFlag) {
                if (_squareFields[_CENTER].getGameState() == _NONE) {
                    // Play in the center square.
                    _squareFields[_CENTER].setSquare(_O);
                } else {
                    // X played in the center square.  Play in the top left corner.
                    _squareFields[_TOP_LEFT].setSquare(_O);
                    _middleStartFlag = true;
                }
                _firstTurnFlag = false;
                return;
            }

            if (_middleStartFlag) {
                // set the flag to false since this check only needs to be done on the second turn.
                _middleStartFlag = false;
                if (_squareFields[_BOTTOM_RIGHT].getGameState() == _X) {
                    //       O| |
                    //       -+-+-
                    //        |X|
                    //       -+-+-
                    //        | |X
                    // O has to now play in one of the corners to prevent a fork
                    // (this check is done here to simplify the logic in the fork checking
                    // done below)
                    _squareFields[_TOP_RIGHT].setSquare(_O);
                    return;
                }
            }
        }

        // check to see if computer can win this turn
        for (int i=0; i<8; ++i) {
            if (_winLineCount[i] == _TWO_O) {
                _squareFields[getLastSquareInLine(i)].setSquare(_O);
                endGame(_O);
                return;
            }
        }

        if (_difficulty >= _MEDIUM) {
            // check to block opponent's possible win
            for (int i=0; i<8; ++i) {
                if (_winLineCount[i] == _TWO_X) {
                    _squareFields[getLastSquareInLine(i)].setSquare(_O);
                    return;
                }
            }

            if (_difficulty == _HARD) {
                // check if opponent can form a fork on next turn (two lines of 2)
                // (leave this out for medium level difficulty)
                int _squareMakes2 = -1;
                for (int i=_TOP_LEFT; i<=_BOTTOM_RIGHT; ++i) {
                    int count = 0;
                    int wlo[] = _winLineOccurrences[i];

                    for (int j=wlo.length-1; j>=0; --j) {
                        // This square (i) is blank, since otherwise wlo will have length zero.
                        // Count the win lines going through this square containing just one X
                        // (and two blanks).
                        if (_winLineCount[wlo[j]] == _ONE_X) {
                            ++count;
                        }
                    }
                    if (count > 1) {
                        // A forking situation exists.  (If X moves in square i, there will be
                        // two possiblities for X to win on its next turn).
                        if (_squareMakes2 == -1) {
                            // First forking square found.
                            _squareMakes2 = i;
                        } else {
                            // This is the second forking square found.  Because of the logic
                            // included above with the middleStartFlag we can just choose to
                            // go in any of the other available squares on the board (besides the
                            // two forking square).
                            for (int k=_TOP_LEFT; k<=_BOTTOM_RIGHT; ++k) {
                                if ((k != _squareMakes2) && (k != i)) {
                                    if (_squareFields[k].getGameState() == _NONE) {
                                        _squareFields[k].setSquare(_O);
                                        return;
                                    }
                                }
                            }
                        }
                    }
                }
                // There was only one forking square.  Move in that square to block it.
                if (_squareMakes2 != -1) {
                    _squareFields[_squareMakes2].setSquare(_O);
                    return;
                }
            }
        }

        // just choose the next available square
        // (for easy level difficulty jump right to here)
        for (int i=_TOP_LEFT; i<=_BOTTOM_RIGHT; ++i) {
            if (_squareFields[i].getGameState() == _NONE) {
                _squareFields[i].setSquare(_O);
                return;
            }
        }

        endGame(_NONE);
        return;

    }

    /**
     * Sets the game over flag and pops up a dialog indicating the results.
     * Opens up the menu at the end so that the user can choose to play a new game
     * or quit.
     */
    private void endGame(int winner) {
        _gameOverFlag = true;
        if( winner == _yourMark ) {
            Dialog.alert( _resources.getString(TICTACTOE_YOU_WIN) );
        } else if( winner == _theirMark ) {
            if( _twoPlayerFlag ) {
                Dialog.alert( _resources.getString( TICTACTOE_OPPONENT_WINS ) );
            } else {
                Dialog.alert(_resources.getString(TICTACTOE_COMPUTER_WINS));
            }
        } else if( winner == _QUIT ) {
            Dialog.alert( _resources.getString( TICTACTOE_GAME_ENDED ) );
        } else {
            Dialog.alert(_resources.getString(TICTACTOE_TIE_GAME));
        }
        if( _twoPlayerFlag && _yourTurnFlag && _twoPlayerSession != null && _twoPlayerSession.isOpen() ) {
            _twoPlayerSession.close();
        }
        _turnField.setText( _resources.getString( TICTACTOE_GAME_OVER ) );
        _mainScreen.onMenu( 0 );
    }

    /**
     * Plays the move selected, and passes control over to the computer (computerMoves)
     * unless the game is over or something is in focus which is not an empty square - in which
     * case the menu is opened up.
     */
    private void youMove() {
        if (!_gameOverFlag) {
            boolean youWin = false;
            Field leafField = getActiveScreen().getLeafFieldWithFocus();
            if ( leafField instanceof SquareField ) {
                if (((SquareField)leafField).setSquare(_yourMark)) {
                    int numMoves = 0;
                    for (int i=0; i<8; i++) {
                        if ( ( _yourMark == _X && _winLineCount[i] == _THREE_X ) ||
                             ( _yourMark == _O && _winLineCount[i] == _THREE_O ) ) {
                            youWin = true;
                        }
                        if( _squareFields[i].getGameState() != _NONE ) {
                            numMoves++;
                        }
                    }
                    if( _twoPlayerFlag ) {
                        // send a message representing the move
                        Message move = new Message( "com.rim.tictactoe/move", 
                                                    new byte[] {}, 
                                                    null, 
                                                    ((SquareField)leafField).getSquareIndex(), 
                                                    null );
                        _twoPlayerSession.send( move );
                        _yourTurnFlag = false;
                        _turnField.theirTurn();
                        if( youWin ) {
                            endGame( _yourMark );
                        } else if( numMoves == _TIE ) {
                            endGame( _NONE );
                        }
                    } else {
                        if( youWin ) {
                            endGame( _yourMark );
                        } else {
                            computerMoves();
                        }
                    }
                    return;
                }
            }
        }
    }
    
    public void requestTwoPlayerGame( MessengerContact player ) {
        _twoPlayerRequestDialog.setDialogClosedListener( this );
        _twoPlayerRequestDialog.show();
        new SetupTwoPlayerGameThread( player ).start();
    }
    
    public void dialogClosed( Dialog dialog, int choice ) {
        if( dialog == _twoPlayerRequestDialog && choice == Dialog.CANCEL ) {
            // tell the setup thread that the user on this end has cancelled the request
            synchronized( _twoPlayerSession ) {
                _twoPlayerSession.notify();
            }
        }
    }
    
    private final class SetupTwoPlayerGameThread extends Thread {
        private MessengerContact _player;
        SetupTwoPlayerGameThread( MessengerContact player ) {
            _player = player;
        }
        
        public void run() {
            _twoPlayerSession = _player.getSession();
            synchronized( _twoPlayerSession ) {
                ApplicationDescriptor application = ApplicationDescriptor.currentApplicationDescriptor();
                _twoPlayerSession.sendRequest( new TTTSetupListener(), application, TicTacToe.this._resources.getString( TICTACTOE_JDE_URL ) );
                try {
                    _twoPlayerSession.wait( 180000 );
                } catch ( InterruptedException ie ) {
                }
                Runnable r;
                if( _twoPlayerSession.isOpen() ) {
                    r = new Runnable() {
                        public void run() {
                            _twoPlayerRequestDialog.close();
                            Dialog.alert( TicTacToe.this._resources.getString( TICTACTOE_DIALOG_SESSION_ACCEPTED ) );
                        }
                    };
                } else {
                    r = new Runnable() {
                        public void run() {
                            _twoPlayerRequestDialog.close();
                            Dialog.alert( TicTacToe.this._resources.getString( TICTACTOE_DIALOG_SESSION_REFUSED ) );
                        }
                    };
                }
                Application.getApplication().invokeLater( r );
            }
        }
    }
    
    private final class TTTSetupListener implements SessionSetupListener {
        public TTTSetupListener() {}
        public void sessionRequestAccepted( Session session ) {
            wakeRequestThread( session );
            newTwoPlayerGame( false, session );
        }
        public void sessionRequestDelivered( Session session ) {}
        public void sessionRequestFailed( Session session, int reason ) {
            wakeRequestThread( session );
        }
        public void sessionRequestRefused( Session session ) {
            wakeRequestThread( session );
        }
        private void wakeRequestThread( Session session ) {
            if( session == _twoPlayerSession && _twoPlayerSession != null ) {
                synchronized( _twoPlayerSession ) {
                    _twoPlayerSession.notify();
                }
            }
        }
    }
    
    private final class TTTSessionListener implements SessionListener {
        public TTTSessionListener() {}
        public void messageDelivered( Session session, Message message ) {}
        public void messageQueuedForSend( Session session, Message message ) {}
        public void messageReceived( Session session, Message message ) {
            if( session == _twoPlayerSession ) {
                int move = message.getInteger();
                SquareField marked = _squareFields[move];
                marked.setSquare( _theirMark );
                _yourTurnFlag = true;
                _turnField.yourTurn();
                int numMoves = 0;
                for(int i=0; i<8; i++) {
                    if( ( _theirMark == _X && _winLineCount[i] == _THREE_X ) ||
                        ( _theirMark == _O && _winLineCount[i] == _THREE_O ) ) {
                        endGame( _theirMark );
                    }
                    if( _squareFields[i].getState() != _NONE ) {
                        numMoves++;
                    }
                }
                if( numMoves == _TIE ) {
                    endGame( _NONE );
                }
            }
        }
        public void messageSent( Session session, Message message ) {}
        public void sessionClosed( Session session ) {
            if( session == _twoPlayerSession && !_gameOverFlag ) {
                endGame( _QUIT );
            }
        }
    }
        
}
