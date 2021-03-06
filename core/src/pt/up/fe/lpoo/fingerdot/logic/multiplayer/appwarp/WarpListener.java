//
//  FingerDot
//
//  Created by Eduardo Almeida and Joao Almeida
//  LPOO 13/14
//

package pt.up.fe.lpoo.fingerdot.logic.multiplayer.appwarp;

public interface WarpListener {
	
	public void onWaitingStarted(String message);
	
	public void onError(String message);
	
	public void onGameStarted(String message);
	
	public void onGameFinished(int code, boolean isRemote);
	
	public void onGameUpdateReceived(String message);

    public void onConnectDone(boolean status);
}
