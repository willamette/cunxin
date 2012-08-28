package org.cunxin.app.module;

import com.google.inject.AbstractModule;
import org.cunxin.app.adapter.DataAdapter;

public class CunxinServiceModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(DataAdapter.class).asEagerSingleton();
    }
}
