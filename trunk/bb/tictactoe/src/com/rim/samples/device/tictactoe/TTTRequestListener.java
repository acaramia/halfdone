/*
 * TTTRequestListener.java
 *
 * © Research In Motion Limited, 2003-2003
 * Confidential and proprietary.
 */

package com.rim.samples.device.tictactoe;

import net.rim.blackberry.api.blackberrymessenger.*;
import net.rim.device.api.system.Application;

/**
 * 
 */
class TTTRequestListener implements SessionRequestListener {
    static TTTRequestListener _instance;
    
    private TTTRequestListener() {}
    
    static TTTRequestListener getInstance() {
        if( _instance == null ) {
            _instance = new TTTRequestListener();
        }
        return _instance;
    }
    
    public void sessionRequestAccepted( Session session ) {
        Application.getApplication().requestForeground();
        TicTacToe.getGame().newTwoPlayerGame( true, session );
    }
}

