package com.convallyria.taleofkingdoms.common.serialization;

import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.Lifecycle;
import com.mojang.serialization.codecs.PrimitiveCodec;

import java.util.Optional;

public record EnumCodec<E extends Enum<E> & EnumCodec.Values>(Class<E> clazz) implements PrimitiveCodec<E> {

    @Override
    public <T> DataResult<E> read(DynamicOps<T> dynamicOps, T t) {
        DataResult<String> res = dynamicOps.getStringValue(t);
		final Optional<String> result = res.result();
		if (res.error().isPresent() || result.isEmpty())
			return DataResult.error(() -> "Unable to parse EnumCodec for \"" + clazz.getSimpleName() + "\": "
					+ (res.error().isPresent() ? res.error().get().message() : "(no error)"), Lifecycle.stable());
        Optional<E> value = Values.getValue(clazz, result.get());
        return value.map(DataResult::success).orElseGet(() -> DataResult.error(() -> "Unable to parse EnumCodec for \"" + clazz.getSimpleName() + "\": Unknown enum value \"" + result.get() + "\"", Lifecycle.stable()));
    }

    @Override
    public <T> T write(DynamicOps<T> dynamicOps, E e) {
        return dynamicOps.createString(e.getSerializedName());
    }

	static <E extends Enum<E>> DataResult<E> validate(String name, E[] enumValues) {
		for (E e : enumValues) {
			if (e.name().equals(name)) {
				return DataResult.success(e);
			}
		}
		return DataResult.error(() -> String.format("\"%s\" has no enum value \"%s\"!", "", name), Lifecycle.stable());
	}

    public interface Values {

        String getSerializedName();

        static <E extends Enum<E> & Values> Optional<E> getValue(Class<E> clazz, String serializedName) {
            for (E e : clazz.getEnumConstants()) {
				if (e.getSerializedName().equalsIgnoreCase(serializedName)) {
					return Optional.of(e);
				}
			}
            return Optional.empty();
        }
    }
}