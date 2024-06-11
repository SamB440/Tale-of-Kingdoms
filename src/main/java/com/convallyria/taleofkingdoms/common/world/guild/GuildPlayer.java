package com.convallyria.taleofkingdoms.common.world.guild;

import com.convallyria.taleofkingdoms.common.kingdom.PlayerKingdom;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.Uuids;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class GuildPlayer {

    public static final Codec<GuildPlayer> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    Codec.BOOL.fieldOf("signed_contract").forGetter(GuildPlayer::hasSignedContract),
                    Codec.INT.fieldOf("coins").forGetter(GuildPlayer::getCoins),
                    Codec.INT.fieldOf("banker_coins").forGetter(GuildPlayer::getBankerCoins),
                    Codec.INT.fieldOf("worthiness").forGetter(GuildPlayer::getWorthiness),
                    Codec.LONG.fieldOf("farmer_last_bread").forGetter(GuildPlayer::getFarmerLastBread),
                    PlayerKingdom.CODEC.optionalFieldOf("kingdom").forGetter(guildPlayer -> Optional.ofNullable(guildPlayer.getKingdom())),
                    Uuids.CODEC.listOf().fieldOf("hunters").forGetter(GuildPlayer::getHunters),
                    Codec.BOOL.fieldOf("has_rebuilt_guild").forGetter(GuildPlayer::hasRebuiltGuild)
            ).apply(instance, GuildPlayer::new)
    );

    private boolean signedContract;
    private int coins;
    private int bankerCoins;
    private int worthiness;
    private long farmerLastBread;
    private @Nullable PlayerKingdom kingdom;
    private final List<UUID> hunters;
    private boolean hasRebuiltGuild;

    public GuildPlayer() {
        this.hunters = new ArrayList<>();
        this.farmerLastBread = -1;
    }

    private GuildPlayer(boolean signedContract, int coins, int bankerCoins, int worthiness, long farmerLastBread, Optional<PlayerKingdom> kingdom, List<UUID> hunters, boolean hasRebuiltGuild) {
        this.signedContract = signedContract;
        this.coins = coins;
        this.bankerCoins = bankerCoins;
        this.worthiness = worthiness;
        this.farmerLastBread = farmerLastBread;
        this.kingdom = kingdom.orElse(null);
        this.hunters = new ArrayList<>(hunters);
        this.hasRebuiltGuild = hasRebuiltGuild;
    }

    public boolean hasSignedContract() {
        return signedContract;
    }

    public void setSignedContract(boolean signedContract) {
        this.signedContract = signedContract;
    }

    public int getCoins() {
        return coins;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }

    public int getBankerCoins() {
        return bankerCoins;
    }

    public void setBankerCoins(int bankerCoins) {
        this.bankerCoins = bankerCoins;
    }

    public int getWorthiness() {
        return worthiness;
    }

    public void setWorthiness(int worthiness) {
        if (!hasSignedContract()) return;
        this.worthiness = worthiness;
    }

    public long getFarmerLastBread() {
        return farmerLastBread;
    }

    public void setFarmerLastBread(long farmerLastBread) {
        this.farmerLastBread = farmerLastBread;
    }

    public @Nullable PlayerKingdom getKingdom() {
        return kingdom;
    }

    public void setKingdom(@Nullable PlayerKingdom kingdom) {
        this.kingdom = kingdom;
    }

    public List<UUID> getHunters() {
        return hunters;
    }

    public boolean hasRebuiltGuild() {
        return hasRebuiltGuild;
    }

    public void setHasRebuiltGuild(boolean hasRebuiltGuild) {
        this.hasRebuiltGuild = hasRebuiltGuild;
    }
}
