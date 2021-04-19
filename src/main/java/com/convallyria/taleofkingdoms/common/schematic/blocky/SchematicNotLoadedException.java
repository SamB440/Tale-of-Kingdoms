package com.convallyria.taleofkingdoms.common.schematic.blocky;

/**
 * Called when a schematic's data has not been loaded.
 * @author SamB440
 */
public class SchematicNotLoadedException extends Exception {

    public SchematicNotLoadedException(String message) {
        super(message);
    }
}
