package com.convallyria.taleofkingdoms.common.listener;

import com.convallyria.taleofkingdoms.common.event.tok.KingdomStartCallback;

public class KingdomListener extends Listener {

    public KingdomListener() {
        KingdomStartCallback.EVENT.register((player, instance) -> {
            //SoundManager soundManager = (SoundManager) TaleOfKingdoms.getAPI().get().getManager("Sound Manager");
            //player.playSound(soundManager.getSound(SoundManager.TOKSound.TOKTHEME), SoundCategory.MASTER,1, 1);
        });
    }
}
