package com.zuk0.gaijinsmash.riderz.di.component;


import android.app.Application;

import com.zuk0.gaijinsmash.riderz.RiderzApplication;
import com.zuk0.gaijinsmash.riderz.di.module.ActivityBuilder;
import com.zuk0.gaijinsmash.riderz.di.module.AppModule;
import com.zuk0.gaijinsmash.riderz.di.module.NetModule;
import com.zuk0.gaijinsmash.riderz.ui.activity.main.MainActivity;
import com.zuk0.gaijinsmash.riderz.ui.fragment.home.HomeFragment;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjectionModule;
import dagger.android.support.AndroidSupportInjectionModule;

/*
    This is a bridge between @Module and @Inject
    https://blog.mindorks.com/the-new-dagger-2-android-injector-cbe7d55afa6a
 */
@Singleton
@Component(modules = {
        AndroidSupportInjectionModule.class,
        AndroidInjectionModule.class,
        ActivityBuilder.class,
        AppModule.class,
        NetModule.class
})
public interface AppComponent {

    void inject(RiderzApplication application);

    @Component.Builder
    interface Builder{

        @BindsInstance
        Builder application(Application application);

        AppComponent build();
    }
}
