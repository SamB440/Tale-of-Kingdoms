package net.islandearth.taleofkingdoms.common.listener;

public class SleepListener extends Listener {
//TODO
    /*@SubscribeEvent
    public void onSleep(SleepFinishedTimeEvent event) {
        if (Minecraft.getInstance().getIntegratedServer() == null) return;
        Optional<ConquestInstance> instance = TaleOfKingdoms.getAPI().get().getConquestInstanceStorage().getConquestInstance(Minecraft.getInstance().getIntegratedServer().getFolderName());
        if (Minecraft.getInstance().isSingleplayer()) {
            PlayerEntity player = Minecraft.getInstance().player;
            if (player != null && instance.isPresent() && instance.get().isInGuild(player)) {
                IWorld world = event.getWorld();
                if (world.getWorld().getDimension().isDaytime()) {
                    Timer timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            world.getWorld().getDimension().setWorldTime(13000);
                        }
                    }, 20);
                }
            }
        }
    }

    @SubscribeEvent
    public void onSleep(SleepingLocationCheckEvent event) {
        if (Minecraft.getInstance().getIntegratedServer() == null) return;
        Optional<ConquestInstance> instance = TaleOfKingdoms.getAPI().get().getConquestInstanceStorage().getConquestInstance(Minecraft.getInstance().getIntegratedServer().getFolderName());
        if (event.getEntityLiving() instanceof PlayerEntity) {
            PlayerEntity playerEntity = (PlayerEntity) event.getEntityLiving();
            if (instance.isPresent() && instance.get().isInGuild(playerEntity)) {
                event.setResult(Event.Result.ALLOW);
            }
        }
    }

    @SubscribeEvent
    public void onSleep(SleepingTimeCheckEvent event) {
        if (Minecraft.getInstance().getIntegratedServer() == null) return;
        Optional<ConquestInstance> instance = TaleOfKingdoms.getAPI().get().getConquestInstanceStorage().getConquestInstance(Minecraft.getInstance().getIntegratedServer().getFolderName());
        if (event.getEntityLiving() instanceof PlayerEntity) {
            PlayerEntity playerEntity = (PlayerEntity) event.getEntityLiving();
            if (instance.isPresent() && instance.get().isInGuild(playerEntity)) {
                event.setResult(Event.Result.ALLOW);
            }
        }
    }*/
}
