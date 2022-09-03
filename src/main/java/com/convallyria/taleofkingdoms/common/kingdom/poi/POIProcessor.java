package com.convallyria.taleofkingdoms.common.kingdom.poi;

import com.convallyria.taleofkingdoms.common.kingdom.PlayerKingdom;
import net.minecraft.structure.StructureTemplate;

public interface POIProcessor {

    void compute(PlayerKingdom kingdom, StructureTemplate.StructureBlockInfo info);
}
