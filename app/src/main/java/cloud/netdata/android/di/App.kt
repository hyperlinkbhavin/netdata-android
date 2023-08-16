package cloud.netdata.android.di

import android.app.Application

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        Injector.INSTANCE.initAppComponent(this, "MYTRAVELBOOK")
    }


}
