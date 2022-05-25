//package com.convallyria.taleofkingdoms.common.generator;
//
//import com.convallyria.taleofkingdoms.TaleOfKingdoms;
//import com.convallyria.taleofkingdoms.common.entity.EntityTypes;
//import com.convallyria.taleofkingdoms.common.utils.EntityUtils;
//import com.mojang.serialization.Codec;
//import net.minecraft.nbt.NbtCompound;
//import net.minecraft.structure.PoolStructurePiece;
//import net.minecraft.structure.PostPlacementProcessor;
//import net.minecraft.structure.SimpleStructurePiece;
//import net.minecraft.structure.StructureGeneratorFactory;
//import net.minecraft.structure.StructureManager;
//import net.minecraft.structure.StructurePiece;
//import net.minecraft.structure.StructurePiecesGenerator;
//import net.minecraft.structure.StructurePlacementData;
//import net.minecraft.structure.pool.StructurePoolBasedGenerator;
//import net.minecraft.structure.processor.BlockIgnoreStructureProcessor;
//import net.minecraft.util.BlockMirror;
//import net.minecraft.util.BlockRotation;
//import net.minecraft.util.Identifier;
//import net.minecraft.util.math.BlockBox;
//import net.minecraft.util.math.BlockPos;
//import net.minecraft.util.math.Direction;
//import net.minecraft.util.math.Vec3i;
//import net.minecraft.util.registry.Registry;
//import net.minecraft.world.ServerWorldAccess;
//import net.minecraft.world.gen.feature.StructureFeature;
//import net.minecraft.world.gen.feature.StructurePoolFeatureConfig;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//import java.util.Random;
//import java.util.concurrent.ThreadLocalRandom;
//
//public class ReficuleVillageGenerator extends StructureFeature<StructurePoolFeatureConfig> {
//
//    private static final Identifier ONE = new Identifier(TaleOfKingdoms.MODID, "reficule_village/reficule_village_one");
//    private static final Identifier THREE = new Identifier(TaleOfKingdoms.MODID, "reficule_village/reficule_village_three");
//    private static final Identifier FOUR = new Identifier(TaleOfKingdoms.MODID, "reficule_village/reficule_village_four");
//    private static final Identifier MIDDLE = new Identifier(TaleOfKingdoms.MODID, "reficule_village/reficule_village_middle");
//    private static final Identifier MIDDLE_TWO = new Identifier(TaleOfKingdoms.MODID, "reficule_village/reficule_village_middle_two");
//    private static final Identifier TOWER = new Identifier(TaleOfKingdoms.MODID, "reficule_village/reficule_village_tower");
//
//    public ReficuleVillageGenerator(Codec<StructurePoolFeatureConfig> codec) {
//        // Create the pieces layout of the structure and give it to the game
//        super(codec, ReficuleVillageGenerator::createPiecesGenerator, PostPlacementProcessor.EMPTY);
//    }
//
//    /*
//     * This is where extra checks can be done to determine if the structure can spawn here.
//     * This only needs to be overridden if you're adding additional spawn conditions.
//     *
//     * Fun fact, if you set your structure separation/spacing to be 0/1, you can use
//     * canGenerate to return true only if certain chunk coordinates are passed in
//     * which allows you to spawn structures only at certain coordinates in the world.
//     *
//     * Basically, this method is used for determining if the land is at a suitable height,
//     * if certain other structures are too close or not, or some other restrictive condition.
//     *
//     * For example, Pillager Outposts added a check to make sure it cannot spawn within 10 chunk of a Village.
//     * (Bedrock Edition seems to not have the same check)
//     *
//     * If you are doing Nether structures, you'll probably want to spawn your structure on top of ledges.
//     * Best way to do that is to use getColumnSample to grab a column of blocks at the structure's x/z position.
//     * Then loop through it and look for land with air above it and set blockpos's Y value to it.
//     * Make sure to set the final boolean in StructurePoolBasedGenerator.generate to false so
//     * that the structure spawns at blockpos's y value instead of placing the structure on the Bedrock roof!
//     *
//     * Also, please for the love of god, do not do dimension checking here.
//     * If you do and another mod's dimension is trying to spawn your structure,
//     * the locate command will make minecraft hang forever and break the game.
//     *
//     * Instead, use the removeStructureSpawningFromSelectedDimension method in
//     * StructureTutorialMain class. If you check for the dimension there and do not add your
//     * structure's spacing into the chunk generator, the structure will not spawn in that dimension!
//     */
//    public static Optional<StructurePiecesGenerator<StructurePoolFeatureConfig>> createPiecesGenerator(StructureGeneratorFactory.Context<StructurePoolFeatureConfig> context) {
//
//        // Check if the spot is valid for our structure. This is just as another method for cleanness.
//        // Returning an empty optional tells the game to skip this spot as it will not generate the structure.
////        if (!RunDownHouseStructure.isFeatureChunk(context)) {
////            return Optional.empty();
////        }
//
//        /*
//         * The only reason we are using StructurePoolFeatureConfig here is that further down, we are using
//         * StructurePoolBasedGenerator.generate which requires StructurePoolFeatureConfig. However, if you create your own
//         * StructurePoolBasedGenerator.generate, you could reduce the amount of workarounds like above that you need
//         * and give yourself more opportunities and control over your structures.
//         *
//         * An example of a custom StructurePoolBasedGenerator.generate in action can be found here (warning, it is using Mojmap mappings):
//         * https://github.com/TelepathicGrunt/RepurposedStructures-Fabric/blob/1.18/src/main/java/com/telepathicgrunt/repurposedstructures/world/structures/pieces/PieceLimitedJigsawManager.java
//         */
//        StructurePoolFeatureConfig newConfig = new StructurePoolFeatureConfig(
//                // The path to the starting Template Pool JSON file to read.
//                //
//                // Note, this is "structure_tutorial:run_down_house/start_pool" which means
//                // the game will automatically look into the following path for the template pool:
//                // "resources/data/structure_tutorial/worldgen/template_pool/run_down_house/start_pool.json"
//                // This is why your pool files must be in "data/<modid>/worldgen/template_pool/<the path to the pool here>"
//                // because the game automatically will check in worldgen/template_pool for the pools.
//                () -> context.registryManager().get(Registry.STRUCTURE_POOL_KEY)
//                        .get(new Identifier(TaleOfKingdoms.MODID, "run_down_house/start_pool")),
//
//                // How many pieces outward from center can a recursive jigsaw structure spawn.
//                // Our structure is only 1 piece outward and isn't recursive so any value of 1 or more doesn't change anything.
//                // However, I recommend you keep this a decent value like 7 so people can use datapacks to add additional pieces to your structure easily.
//                // But don't make it too large for recursive structures like villages or you'll crash server due to hundreds of pieces attempting to generate!
//                10
//        );
//
//        // Create a new context with the new config that has our json pool. We will pass this into JigsawPlacement.addPieces
//        StructureGeneratorFactory.Context<StructurePoolFeatureConfig> newContext = new StructureGeneratorFactory.Context<>(
//                context.chunkGenerator(),
//                context.biomeSource(),
//                context.seed(),
//                context.chunkPos(),
//                newConfig,
//                context.world(),
//                context.validBiome(),
//                context.structureManager(),
//                context.registryManager()
//        );
//
//        // Turns the chunk coordinates into actual coordinates we can use. (Gets center of that chunk)
//        BlockPos blockpos = context.chunkPos().getCenterAtY(0);
//
//        Optional<StructurePiecesGenerator<StructurePoolFeatureConfig>> structurePiecesGenerator =
//                StructurePoolBasedGenerator.generate(
//                        newContext, // Used for StructurePoolBasedGenerator to get all the proper behaviors done.
//                        PoolStructurePiece::new, // Needed in order to create a list of jigsaw pieces when making the structure's layout.
//                        blockpos, // Position of the structure. Y value is ignored if last parameter is set to true.
//                        false,  // Special boundary adjustments for villages. It's... hard to explain. Keep this false and make your pieces not be partially intersecting.
//                        // Either not intersecting or fully contained will make children pieces spawn just fine. It's easier that way.
//                        true // Place at heightmap (top land). Set this to false for structure to be place at the passed in blockpos's Y value instead.
//                        // Definitely keep this false when placing structures in the nether as otherwise, heightmap placing will put the structure on the Bedrock roof.
//                );
//
//        /*
//         * Note, you are always free to make your own StructurePoolBasedGenerator class and implementation of how the structure
//         * should generate. It is tricky but extremely powerful if you are doing something that vanilla's jigsaw system cannot do.
//         * Such as for example, forcing 3 pieces to always spawn every time, limiting how often a piece spawns, or remove the intersection limitation of pieces.
//         *
//         * An example of a custom StructurePoolBasedGenerator.generate in action can be found here (warning, it is using Mojmap mappings):
//         * https://github.com/TelepathicGrunt/RepurposedStructures-Fabric/blob/1.18/src/main/java/com/telepathicgrunt/repurposedstructures/world/structures/pieces/PieceLimitedJigsawManager.java
//         */
//
//        if(structurePiecesGenerator.isPresent()) {
//            // I use to debug and quickly find out if the structure is spawning or not and where it is.
//            // This is returning the coordinates of the center starting piece.
//            StructureTutorialMain.LOGGER.log(Level.DEBUG, "Rundown House at " + blockpos);
//        }
//
//        // Return the pieces generator that is now set up so that the game runs it when it needs to create the layout of structure pieces.
//        return structurePiecesGenerator;
//    }
//
//    public static List<StructurePiece> getPieces(StructureManager manager, BlockPos pos) {
//        List<StructurePiece> pieces = new ArrayList<>();
//        final Direction direction = Direction.random(ThreadLocalRandom.current());
//        ReficuleVillagePiece onePiece = new ReficuleVillagePiece(manager, ONE, pos.subtract(new Vec3i(0, 6, 0)), BlockRotation.NONE, 0);
//        onePiece.setOrientation(direction);
//        pieces.add(onePiece);
//
//        BlockPos middlePos = pos.add(new Vec3i(48, 0, 0));
//        ReficuleVillagePiece middlePiece = new ReficuleVillagePiece(manager, MIDDLE, middlePos, BlockRotation.NONE, 0);
//        middlePiece.setOrientation(direction);
//        pieces.add(middlePiece);
//
//        BlockPos threePos = middlePos.add(new Vec3i(32, 0, 0));
//        ReficuleVillagePiece threePiece = new ReficuleVillagePiece(manager, THREE, threePos, BlockRotation.NONE, 0);
//        threePiece.setOrientation(direction);
//        pieces.add(threePiece);
//
//        BlockPos middleTwoPos = middlePos.subtract(new Vec3i(0, 0, 36));
//        ReficuleVillagePiece middleTwoPiece = new ReficuleVillagePiece(manager, MIDDLE_TWO, middleTwoPos, BlockRotation.NONE, 0);
//        middleTwoPiece.setOrientation(direction);
//        pieces.add(middleTwoPiece);
//
//        BlockPos fourPos = middleTwoPos.add(new Vec3i(0, 0, 13)).add(new Vec3i(32, 0, 0));
//        ReficuleVillagePiece fourPiece = new ReficuleVillagePiece(manager, FOUR, fourPos, BlockRotation.NONE, 0);
//        fourPiece.setOrientation(direction);
//        pieces.add(fourPiece);
//
//        BlockPos towerPos = pos.subtract(new Vec3i(0, 0, 32));
//        ReficuleVillagePiece towerPiece = new ReficuleVillagePiece(manager, TOWER, towerPos, BlockRotation.NONE, 0);
//        towerPiece.setOrientation(direction);
//        pieces.add(towerPiece);
//        return pieces;
//    }
//
//    public static class ReficuleVillagePiece extends SimpleStructurePiece {
//        private final BlockRotation rotation;
//        private final Identifier template;
//
//        public ReficuleVillagePiece(StructureManager structureManager, Identifier identifier, BlockPos blockPos, BlockRotation blockRotation, int i) {
//            super(TaleOfKingdoms.REFICULE_VILLAGE, 0, structureManager, identifier, identifier.toString(), createPlacementData(blockRotation, identifier), blockPos);
//            this.pos = blockPos;
//            this.rotation = blockRotation;
//            this.template = identifier;
//            createPlacementData(blockRotation, identifier);
//        }
//
//        public ReficuleVillagePiece(StructureManager structureManager, NbtCompound nbtCompound) {
//            super(TaleOfKingdoms.REFICULE_VILLAGE, nbtCompound, structureManager, (identifier) -> {
//                return createPlacementData(BlockRotation.valueOf(nbtCompound.getString("Rot")), identifier);
//            });
//            this.template = new Identifier(nbtCompound.getString("Template"));
//            this.rotation = BlockRotation.valueOf(nbtCompound.getString("Rot"));
//            createPlacementData(rotation, template);
//        }
//
//        private static StructurePlacementData createPlacementData(BlockRotation blockRotation, Identifier identifier) {
//            return (new StructurePlacementData())
//                    .setRotation(blockRotation)
//                    .setMirror(BlockMirror.NONE)
//                    .addProcessor(BlockIgnoreStructureProcessor.IGNORE_STRUCTURE_BLOCKS);
//        }
//
//        @Override
//        protected void handleMetadata(String metadata, BlockPos pos, ServerWorldAccess serverWorldAccess, Random random,
//                                      BlockBox boundingBox) {
//            double percent = Math.random() * 100;
//            if (metadata.equals("Survivor")) {
//                if (percent > 20) {
//                    EntityUtils.spawnEntity(EntityTypes.LONEVILLAGER, serverWorldAccess, pos);
//                }
//                return;
//            }
//
//            if (percent > 60) {
//                switch (metadata) {
//                    case "ReficuleSoldier" -> EntityUtils.spawnEntity(EntityTypes.REFICULE_SOLDIER, serverWorldAccess, pos);
//                    case "ReficuleArcher" -> EntityUtils.spawnEntity(EntityTypes.REFICULE_GUARDIAN, serverWorldAccess, pos);
//                    case "ReficuleMage" -> EntityUtils.spawnEntity(EntityTypes.REFICULE_MAGE, serverWorldAccess, pos);
//                }
//            }
//        }
//    }
//}
