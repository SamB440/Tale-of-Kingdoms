/*
 * WorldEdit, a Minecraft world manipulation toolkit
 * Copyright (C) sk89q <http://www.sk89q.com>
 * Copyright (C) WorldEdit team and contributors
 *
 * This program is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package net.islandearth.taleofkingdoms.common.schematic;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.RunContext;

/**
 * Operation helper methods.
 */
public final class Operations {

	private static final Map<UUID, Integer> progress = new HashMap<>();
	
	private Operations() {
    }

    /**
     * Complete a given operation synchronously until it completes.
     *
     * @param op operation to execute
     * @throws WorldEditException WorldEdit exception
     */
    public static void complete(UUID operationId, Operation op) throws WorldEditException {
        while (op != null) {
            op = op.resume(new RunContext());
        	if (progress.containsKey(operationId)) progress.replace(operationId, progress.get(operationId) + 25);
        	else progress.put(operationId, 25);
        }
    }
    
    public static int getProgress(UUID operationId) {
    	if (!progress.containsKey(operationId)) return 0;
    	return progress.get(operationId);
    }

    /**
     * Complete a given operation synchronously until it completes. Catch all
     * errors that is not {@link MaxChangedBlocksException} for legacy reasons.
     *
     * @param op operation to execute
     * @throws MaxChangedBlocksException thrown when too many blocks have been changed
     */
    public static void completeLegacy(Operation op) throws MaxChangedBlocksException {
        while (op != null) {
            try {
                op = op.resume(new RunContext());
            } catch (MaxChangedBlocksException e) {
                throw e;
            } catch (WorldEditException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Complete a given operation synchronously until it completes. Re-throw all
     * {@link com.sk89q.worldedit.WorldEditException} exceptions as
     * {@link java.lang.RuntimeException}s.
     *
     * @param op operation to execute
     */
    public static void completeBlindly(UUID operationId, Operation op) {
        while (op != null) {
            try {
                op = op.resume(new RunContext());
            	if (progress.containsKey(operationId)) progress.replace(operationId, progress.get(operationId) + 25);
            	else progress.put(operationId, 25);
            } catch (WorldEditException e) {
                throw new RuntimeException(e);
            }
        }
    }

}