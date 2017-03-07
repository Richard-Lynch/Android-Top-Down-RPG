package tcd.game;


import android.content.Context;

import java.util.Map;

/**
 * Created by stefano on 04/02/17.
 */

public class NPC extends AnimateObject {


    int num_turns;
    int goal = 0;
    int[] goalAx;
    int[] goalAy;

    NPC(Context context, String s,int canvasWidth, int canvasHeight){
        super(context,s, GameObjectTypes.NPC, canvasWidth, canvasHeight);

        goalAx = new int[]{1, 1, 1, 0, 0, 0, -1, -1, -1, 0, 0, 0};
        goalAy = new int[]{0, 0, 0, 1, 1, 1, 0, 0, 0, -1, -1, -1};
        num_turns = goalAx.length;
//
//        goalAx[0] = 1;
//        goalAy[0] = 0;
//
//        goalAx[1] = 0;
//        goalAy[1] = 1;
//
//        goalAx[2] = -1;
//        goalAy[2] = 0;
//
//        goalAx[3] = 0;
//        goalAy[3] = -1;

        setVelX(goalAx[goal]);
        setVelX(goalAy[goal]);

    }

    @Override
    public void update(Player[] players, NPC[] npcs, InanObject[] inanObjects, int id, GameObjectTypes type, Map<Integer, Integer> colMap, Map<Integer, GameObject> objMap) {
        super.update(players, npcs, inanObjects, id, type, colMap, objMap);

        if(!this.moving && !this.collided){
            goal++;
            if(goal >= num_turns){
                goal = 0;
            }
        }
        setVelX(goalAx[goal]);
        setVelY(goalAy[goal]);

    }
}