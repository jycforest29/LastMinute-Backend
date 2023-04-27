package com.lastminute.payment.common;

// TODO: 이 부분만 다른 멀티모듈(user) import. Enum을 검증하는 부분에서 멀티모듈 간 코드 중복이 많아 해결 방법 논의 후 변경 필요.
import com.lastminute.user.exception.EnumNotFoundException;

public interface Convertable {
    Object getKey();

    static <E extends Convertable> E findByKey(Object key, Class<E> enumClass) throws EnumNotFoundException {
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
