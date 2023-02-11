package PoolGame.config;

import PoolGame.objects.*;
import PoolGame.GameManager;
import java.util.ArrayList;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * Class to read the configuration file and create pockets.
 */
public class PocketReader implements Reader {
    
    /**
     * Parses the JSON file and builds the pockets.
     * 
     * @param path        The path to the JSON file.
     * @param gameManager The game manager.
     */

     public void parse(String path, GameManager gameManager) {
        JSONParser parser = new JSONParser();
        ArrayList<Pocket> pockets = new ArrayList<Pocket>();

        try {
            Object object = parser.parse(new FileReader(path));

            JSONObject jsonObject = (JSONObject) object;
            // convert Object to JSONObject

            JSONObject jsonTable = (JSONObject) jsonObject.get("Table");
            
            JSONArray jsonPockets = (JSONArray) jsonTable.get("pockets");

            for (Object obj : jsonPockets){
                JSONObject jsonPocket = (JSONObject) obj;

                // the pocket radius is a double
                double radius = (Double) jsonPocket.get("radius");

                // the pocket position is a double
				Double positionX = (Double) ((JSONObject) jsonPocket.get("position")).get("x");
				Double positionY = (Double) ((JSONObject) jsonPocket.get("position")).get("y");

                PoolPocketBuilder builder = new PoolPocketBuilder();
                builder.setRadius(radius);
                builder.setxPos(positionX);
                builder.setyPos(positionY);
                pockets.add(builder.build());

            }
            
            Table table = gameManager.getTable();
            table.setPockets(pockets);
            
        } catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
    }
}
