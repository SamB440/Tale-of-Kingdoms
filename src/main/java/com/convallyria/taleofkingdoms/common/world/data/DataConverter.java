package com.convallyria.taleofkingdoms.common.world.data;

import com.convallyria.taleofkingdoms.common.world.ConquestInstance;

import java.util.List;

public abstract class DataConverter {

    public abstract void convert(ConquestInstance instance);

    public abstract List<String> getVersions();
}
