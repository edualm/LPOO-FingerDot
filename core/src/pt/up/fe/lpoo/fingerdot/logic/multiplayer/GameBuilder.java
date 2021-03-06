//
//  FingerDot
//
//  Created by Eduardo Almeida and Joao Almeida
//  LPOO 13/14
//

package pt.up.fe.lpoo.fingerdot.logic.multiplayer;

import pt.up.fe.lpoo.fingerdot.logic.common.Dot;

import java.util.ArrayList;

public class GameBuilder {
    final MultiPlayerMessenger _messenger;

    ArrayList<GameGeneratorPart> _partArray;

    int _parts;

    /**
     * Intializes a game builder.
     *
     * @param msg A MultiplayerMessenger instance.
     */

    public GameBuilder(MultiPlayerMessenger msg) {
        _messenger = msg;

        _partArray = new ArrayList<GameGeneratorPart>();

        _parts = 0;
    }

    /**
     * Add a part to the builder.
     *
     * @param part The part to add.
     */

    public void addPart(GameGeneratorPart part) {
        _partArray.add(part);

        if (_parts == 0)
            _parts = part.getLength();

        _checkForCompletion();
    }

    private void _checkForCompletion() {
        if (_partArray.size() == _parts) {
            ArrayList<Dot> returnArray = new ArrayList<Dot>();

            for (int i = 0; i < _parts; i++) {
                for (int j = 0; j < _partArray.size(); j++) {
                    if (_partArray.get(j).getPartNumber() == i) {
                        returnArray.addAll(_partArray.get(j).getDots());

                        break;
                    }
                }
            }

            if (_messenger != null) {
                _messenger.onDotsReceived(returnArray);
            }
        }
    }
}
