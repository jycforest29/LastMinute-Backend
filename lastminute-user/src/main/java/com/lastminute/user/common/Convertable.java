package com.lastminute.user.common;

import com.lastminute.user.exception.EnumNotFoundException;

public interface Convertable {

    String getKey();

    static <E extends Convertable> E findByKey(String key, Class<E> enumClass) throws EnumNotFoundException {
        if (key == null) {
            return null;
        }
        for (E enumValue : enumClass.getEnumConstants()) {
            if (key.equals(enumValue.getKey())) {
                return enumValue;
            }
        }
        throw new EnumNotFoundException("key에 해당하는 enum을 찾을 수 없습니다. : " + key);
    }
}
