package com.convallyria.taleofkingdoms.client.listener;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.TaleOfKingdomsAPI;
import com.convallyria.taleofkingdoms.common.event.PlayerJoinWorldCallback;
import com.convallyria.taleofkingdoms.common.listener.Listener;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class ClientGameInstanceListener extends Listener {

    public ClientGameInstanceListener() {
        final TaleOfKingdomsAPI api = TaleOfKingdoms.getAPI();
        PlayerJoinWorldCallback.EVENT.register(player -> {
            api.getConquestInstanceStorage().mostRecentInstance().ifPresent(instance -> {
                if (!instance.hasPlayer(player.getUuid())) {
                    instance.reset(player);
                }
            });
        });
    }
}
