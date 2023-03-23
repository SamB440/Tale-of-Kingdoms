package com.convallyria.taleofkingdoms.common.datafixer;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TaleOfKingdomsDataFixes {

    /**
     * Updates the fixer version of an element.
     * @param element the element to set the version on
     * @return the modified element
     */
    public static @Nullable JsonElement updateFixerVersion(@Nullable JsonElement element) {
        if (element == null) return null;
        element.getAsJsonObject().addProperty("version", Schemas.CURRENT_VERSION);
        return element;
    }

    /**
     * Gets the current fixer version of an element.
     * @param element the element to get the version from
     * @return the version of the element, or the lowest version (1) if it is not set
     */
    public static int getFixerVersion(@NotNull JsonElement element) {
        final JsonObject object = element.getAsJsonObject();
        if (object.has("version")) {
            return object.get("version").getAsInt();
        }
        return 1;
    }
}
