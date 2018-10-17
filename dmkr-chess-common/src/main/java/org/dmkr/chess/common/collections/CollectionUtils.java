package org.dmkr.chess.common.collections;

import lombok.experimental.UtilityClass;

import java.util.Collection;

@UtilityClass
public class CollectionUtils {

    public static <T> boolean isEmpty(Collection<T> c) {
        return c == null || c.isEmpty();
    }

}
