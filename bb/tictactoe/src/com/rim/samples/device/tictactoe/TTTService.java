/*
 * TTTService.java
 *
 * © Research In Motion Limited, 2003-2003
 * Confidential and proprietary.
 */

package com.rim.samples.device.tictactoe;

import net.rim.blackberry.api.blackberrymessenger.*;
import net.rim.device.api.system.Application;


/**
 * A class to be registered with BlackBerry Messenger. The Service will appear in BlackBerry Messenger's menu under the
 * "Start Service..." menu item. This will allow you to start a game of Tic Tac Toe with the contact you're in a conversation
 * with.
 */
class TTTService implements Service{
    private static TTTService _instance;
    
    private TTTService() {
    }
    
    static TTTService getInstance() {
        if( _instance == null ) {
            _instance = new TTTService();
        }
        return _instance;
    }
    
    public void start( MessengerContact contact ) {
        Application.getApplication().requestForeground();
        TicTacToe.getGame().requestTwoPlayerGame( contact );
    }
}
