package com.convallyria.taleofkingdoms.common.world.data;

public enum DataConverters {
    DATA_CONVERTER_1(new ConquestDataConverter1());

    private final DataConverter converter;

    DataConverters(DataConverter converter) {
        this.converter = converter;
    }

    public DataConverter getConverter() {
        return converter;
    }
}
