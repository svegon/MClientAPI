package com.github.svegon.utils.fast.util.objects;

import java.util.function.Supplier;

public class LateBindingSupplier<T> implements com.google.common.base.Supplier<T> {
    private Supplier<? extends T> backingSupplier;

    public synchronized void bind(Supplier<? extends T> backingSupplier) {
        if (this.backingSupplier != null) {
            throw new IllegalStateException();
        }

        this.backingSupplier = backingSupplier;
    }

    @Override
    public T get() {
        return backingSupplier.get();
    }
}
