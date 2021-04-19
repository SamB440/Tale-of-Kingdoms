package com.convallyria.taleofkingdoms.common.schematic.blocky;

/**
 * Called when an NBT block has the wrong id
 * @author SamB440
 */
public class WrongIdException extends Exception {

    public WrongIdException(String message) {
        super(message);
    }
}
