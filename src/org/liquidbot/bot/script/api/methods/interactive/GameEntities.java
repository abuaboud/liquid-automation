package org.liquidbot.bot.script.api.methods.interactive;

import org.liquidbot.bot.client.parser.HookReader;
import org.liquidbot.bot.client.reflection.Reflection;
import org.liquidbot.bot.script.api.interfaces.Filter;
import org.liquidbot.bot.script.api.methods.data.Game;
import org.liquidbot.bot.script.api.query.GameObjectQuery;
import org.liquidbot.bot.script.api.wrappers.GameObject;
import org.liquidbot.bot.script.api.wrappers.Tile;
import org.liquidbot.bot.utils.Utilities;

import java.lang.reflect.Field;
import java.util.LinkedHashSet;
import java.util.Set;

/*
 * Created by Hiasat on 8/1/14
 */
public class GameEntities {

    private static final GameObjectQuery query = new GameObjectQuery();

    /**
     * Gets the query system
     * @return query instance
     */
    public static GameObjectQuery query() {
        return query;
    }

    /**
     * Get All Object in the Region
     *
     * @return GameObject[]: return all objects in your region
     */
    public static GameObject[] getAll() {
        return getAll(new Filter<GameObject>() {
            @Override
            public boolean accept(GameObject gameObject) {
                return true;
            }
        });
    }

    /**
     * Return all gameObjects that have one of these names
     *
     * @param names
     * @return GameObject[] : Get all gameObjects with any of these names
     */
    public static GameObject[] getAll(final String... names) {
        return getAll(new Filter<GameObject>() {
            @Override
            public boolean accept(GameObject gameObject) {
                return gameObject.isValid() && gameObject.getName() != null && Utilities.inArray(gameObject.getName(), names);
            }
        });
    }

    /**
     * Return all gameObjects that have one of these ids
     *
     * @param ids
     * @return GameObject[] : Get all gameObjects with any of these ids
     */
    public static GameObject[] getAll(final int... ids) {
        return getAll(new Filter<GameObject>() {
            @Override
            public boolean accept(GameObject gameObject) {
                return gameObject.isValid() && Utilities.inArray(gameObject.getId(), ids);
            }
        });
    }


    /**
     * Get All Object in the Region that apply to that filter
     *
     * @param filter
     * @return GameObject[] : get All Objects in the region that apply to that filter
     */
    public static GameObject[] getAll(Filter<GameObject> filter) {
        Set<GameObject> objects = new LinkedHashSet<GameObject>();
        Object region = Reflection.value("Client#getRegion()", null);
        Object[][][] tiles = (Object[][][]) Reflection.value("GroundRegion#getGroundTiles()", region);
        if (tiles == null) {
            return objects.toArray(new GameObject[objects.size()]);
        }
        Field gameObjectsField = Reflection.field("GroundTile#getGameObjects()");
        Field floorDecorationField = Reflection.field("GroundTile#getFloorDecoration()");
        Field boundaryField = Reflection.field("GroundTile#getBoundary()");
        Field wallObjectField = Reflection.field("GroundTile#getWallObject()");

        int z = Game.getPlane();
        int baseX = Game.getBaseX(), baseY = Game.getBaseY();

        for (int x = 0; x < 104; x++) {
            for (int y = 0; y < 104; y++) {
                Object groundTile = tiles[z][x][y];
                if (groundTile != null) {

                    Object[] gameObjects = (Object[]) Reflection.value(gameObjectsField, groundTile);
                    if (gameObjects != null) {
                        for (Object j : gameObjects) {
                            if (j != null) {
                                GameObject obj = new GameObject(j, GameObject.Type.INTERACTIVE, x + baseX, y + baseY,z);
                                if (obj != null && (filter == null || filter.accept(obj)))
                                    objects.add(obj);

                            }
                        }
                    }


                    Object floorDeco = Reflection.value(floorDecorationField, groundTile);
                    if (floorDeco != null) {
                        GameObject obj = new GameObject(floorDeco, GameObject.Type.FLOOR_DECORATION, x + baseX, y + baseY,z);
                        if (obj != null && (filter == null || filter.accept(obj)))
                            objects.add(obj);
                    }


                    Object boundary = Reflection.value(boundaryField, groundTile);
                    if (boundary != null) {
                        GameObject obj = new GameObject(boundary, GameObject.Type.BOUNDARY, x + baseX, y + baseY,z);
                        if (obj != null && (filter == null || filter.accept(obj)))
                            objects.add(obj);
                    }


                    Object wallObject = Reflection.value(wallObjectField, groundTile);
                    if (wallObject != null) {
                        GameObject obj = new GameObject(wallObject, GameObject.Type.WALL_OBJECT, x + baseX, y + baseY,z);
                        if (obj != null && (filter == null || filter.accept(obj)))
                            objects.add(obj);
                    }

                }

            }
        }
        return objects.toArray(new GameObject[objects.size()]);
    }

    /**
     * @param filter
     * @return GameObject : nearest gameObject to Local Player
     *         that apply to that filter
     */
    public static GameObject getNearest(Filter<GameObject> filter) {
        return getNearest(Players.getLocal().getLocation(), filter);
    }

    /**
     * @param filter
     * @return GameObject : nearest gameObject to start tile
     *         that apply to that filter
     */
    public static GameObject getNearest(Tile start, Filter<GameObject> filter) {
        GameObject closet = null;
        int distance = 255;
        for (GameObject gameObject : getAll(filter)) {
            if (gameObject.isValid() && distance > gameObject.distanceTo(start)) {
                closet = gameObject;
                distance = gameObject.distanceTo(start);
            }
        }
        return closet;
    }

    /**
     * Get closet GroundItem that has that Id or one of ids
     *
     * @param ids target GameObject Id or Ids
     * @return GameObject
     */
    public static GameObject getNearest(final int... ids) {
        return getNearest(Players.getLocal().getLocation(), new Filter<GameObject>() {
            @Override
            public boolean accept(GameObject gameObject) {
                return gameObject.isValid() && Utilities.inArray(gameObject.getId(), ids);
            }
        });
    }

    /**
     * Get closet GameObject that has that name or one of names
     *
     * @param names target GameObject name or names
     * @return GameObject
     */
    public static GameObject getNearest(final String... names) {
        return getNearest(Players.getLocal().getLocation(), new Filter<GameObject>() {
            @Override
            public boolean accept(GameObject groundItem) {
                return groundItem.isValid() && Utilities.inArray(groundItem.getName(), names);
            }
        });
    }

    /**
     * @param tile
     * @return GameObject : gameObject that is in specific tile
     *         if there isn't it will return null
     */
    public static GameObject getAt(final Tile tile) {
        return getNearest(Players.getLocal().getLocation(), new Filter<GameObject>() {

            @Override
            public boolean accept(GameObject obj) {
                return obj != null && tile.equals(obj.getLocation());
            }
        });
    }
}
