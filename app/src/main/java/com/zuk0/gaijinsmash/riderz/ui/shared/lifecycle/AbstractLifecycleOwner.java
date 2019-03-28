package com.zuk0.gaijinsmash.riderz.ui.shared.lifecycle;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;

public abstract class AbstractLifecycleOwner<T> implements LifecycleOwner {

    private LifecycleRegistry lifecycleRegistry;

    public AbstractLifecycleOwner(AbstractLifecycleObserver observer) {
        lifecycleRegistry = new LifecycleRegistry(this);
        getLifecycle().addObserver(observer);
    }

    public void startOwner() {
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_START);
    }

    public void stopOwner() {
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_STOP);
    }

    public void destroyOwner() {
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY);
    }

    @NonNull
    @Override
    public Lifecycle getLifecycle() {
        return lifecycleRegistry;
    }
}
