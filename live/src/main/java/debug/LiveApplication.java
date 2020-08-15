package debug;


import com.zky.basics.api.RetrofitManager;
import com.zky.basics.common.BaseApplication;

/**
 * Created by lk
 * Date 2019-10-28
 * Time 16:39
 * Detail:
 */
public class LiveApplication extends BaseApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        RetrofitManager.init(this);
    }
}
