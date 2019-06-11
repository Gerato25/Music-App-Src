package nitish.build.com.saavntest1;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.tonyodev.fetch2.Download;
import com.tonyodev.fetch2.Fetch;
import com.tonyodev.fetch2.FetchConfiguration;
import com.tonyodev.fetch2core.Func;
import com.tonyodev.fetch2core.Func2;

import org.jetbrains.annotations.Nullable;

public class NotificationRecive extends BroadcastReceiver {
    Fetch fetch;
    int notifId = 100;
    @Override
    public void onReceive(Context context, Intent intent) {
        //Toast.makeText(context,"recieved",Toast.LENGTH_SHORT).show();

        FetchConfiguration fetchConfiguration = new FetchConfiguration.Builder(context)
                .setDownloadConcurrentLimit(1)
                .build();

        fetch = Fetch.Impl.getInstance(fetchConfiguration);

        String action=intent.getStringExtra("action");
        notifId = intent.getIntExtra("notifID",100);

        Log.i("resu1",action);

        if(action.equals("pause")){
            pause_d(notifId);
        }
        else if(action.equals("cancel")){
            cancel_d(notifId);
        }
        else if(action.equals("resume")){
            resume_d(notifId);
        }

        //This is used to close the notification tray
//        Intent it = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
//        context.sendBroadcast(it);
    }

    public void pause_d(int downId){
        fetch.pause(downId);
        Log.i("resu1","pa");
    }
    public void cancel_d(int downId){
//        fetch.cancel(downId);
        Log.i("resu1","ca");
    }
    public void resume_d(int downId){
//        fetch.resume(downId);
        Log.i("resu1","res");
    }


}
