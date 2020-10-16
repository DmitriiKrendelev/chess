package org.dmkr.chess.common.lang;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.function.Consumer;

@RequiredArgsConstructor(staticName = "switchByType")
public class SwitchByType<T> {
    private @NonNull final T object;
    private boolean executed;

    @SuppressWarnings("unchecked")
    public <S> SwitchByType<T> ifInstanceThenDo(Class<S> cl, Consumer<S> action) {
        if (cl.isInstance(object)) {
            action.accept((S) object);
            executed = true;
        }

        return this;
    }

    public void elseFail() {
        if (!executed) {
            throw new IllegalArgumentException("Unexpected type: " + object.getClass());
        }
    }

}
