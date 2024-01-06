package finalproject.tiles;

import finalproject.system.Tile;
import finalproject.system.TileType;

public class DesertTile extends Tile {
    //TODO level 0: finish constructor
    public DesertTile() {
        super(2,6,3); // Call to the super class (Tile) constructor

        // Initialization
        this.type = TileType.Desert;
    }
}
