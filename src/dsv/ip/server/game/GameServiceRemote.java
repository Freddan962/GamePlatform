package dsv.ip.server.game;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Represents the interface used for Game related RMI communication.
 *
 * @author Fredrik Sander
 */
public interface GameServiceRemote extends Remote {
  // Should handle if the player already has a challenge
  boolean challengeToGame(String forUsername, String toChallengeUsername) throws RemoteException;
  GameSession getGame(String forUsername) throws RemoteException;
  void respondToGameChallenge(String forUsername, boolean accepted) throws RemoteException;
}
