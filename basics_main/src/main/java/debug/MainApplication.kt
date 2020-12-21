package debug

import androidx.multidex.MultiDex
import com.zky.basics.api.RetrofitManager.Companion.init
import com.zky.basics.common.BaseApplication

class MainApplication : BaseApplication() {
    override fun onCreate() {
        super.onCreate()
        init(this)
        MultiDex.install(this)
    }
}