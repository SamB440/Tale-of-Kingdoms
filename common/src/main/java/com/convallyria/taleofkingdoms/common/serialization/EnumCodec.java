package com.convallyria.taleofkingdoms.common.serialization;

import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.Lifecycle;
import com.mojang.serialization.codecs.PrimitiveCodec;

import java.util.Optional;

public record EnumCodec<E extends Enum<E>>(Class<E> clazz) implements PrimitiveCodec<E> {

    @Override
    public <T> DataResult<E> read(DynamicOps<T> dynamicOps, T t) {
        DataResult<String> res = dynamicOps.getStringValue(t);
		final Optional<String> result = res.result();
		if (res.error().isPresent() || result.isEmpty())
			return DataResult.error(() -> "Unable to parse EnumCodec for \"" + clazz.getSimpleName() + "\": "
					+ (res.error().isPresent() ? res.error().get().message() : "(no error)"), Lifecycle.stable());
        Optional<E> value = getValue(clazz, result.get());
        return value.map(DataResult::success).orElseGet(() -> DataResult.error(() -> "Unable to parse EnumCodec for \"" + clazz.getSimpleName() + "\": Unknown enum value \"" + result.get() + "\"", Lifecycle.stable()));
    }

    @Override
    public <T> T write(DynamicOps<T> dynamicOps, E e) {
        return dynamicOps.createString(e.name());
    }

    static <E extends Enum<E>> Optional<E> getValue(Class<E> clazz, String serializedName) {
        for (E e : clazz.getEnumConstants()) {
            if (e.name().equalsIgnoreCase(serializedName)) {
                return Optional.of(e);
            }
        }
        return Optional.empty();
    }
}