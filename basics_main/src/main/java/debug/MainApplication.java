package debug;


import androidx.multidex.MultiDex;
import com.zky.basics.api.RetrofitManager;
import com.zky.basics.common.BaseApplication;


public class MainApplication extends BaseApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        RetrofitManager.init(this);
        MultiDex.install(this);
    }


}
