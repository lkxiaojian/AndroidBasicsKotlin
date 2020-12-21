package debug

import com.zky.basics.api.RetrofitManager.Companion.init
import com.zky.basics.common.BaseApplication

/**
 * Created by lk
 * Date 2019-10-28
 * Time 16:39
 * Detail:
 */
class LiveApplication : BaseApplication() {
    override fun onCreate() {
        super.onCreate()
        init(this)
    }
}