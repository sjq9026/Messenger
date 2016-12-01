package com.android.sjq.messenger;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    //服务端的messenger
    private Messenger mMessenger;
    //客户端的Messenger
    private Messenger mClientMessenger;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = new Intent(this, MyService.class);
        bindService(intent, mServiceConnection, BIND_AUTO_CREATE);
        mClientMessenger = new Messenger(new ClientHandler());
    }

    ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Log.i("TAG", "******************onServiceConnected*******************");
            mMessenger = new Messenger(iBinder);
        }
        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            Log.i("TAG", "******************onServiceDisconnected*******************");
        }
    };

    public void sendMsg(View view) {
        Message msg = Message.obtain(null, Constants.MSG_FROM_CLIENT);
        Bundle bundle = new Bundle();
        bundle.putString("data", "未央黑猪肉");
        msg.setData(bundle);
        msg.replyTo = mClientMessenger;
        try {
            mMessenger.send(msg);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }


    private class ClientHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Constants.MSG_FROM_SERVICE:
                    Log.i("TAG", "This is Client  收到了来自服务端的回复-------->" + msg.getData().get("reply"));
                    Message msg2 = Message.obtain(null, Constants.MSG_FROM_CLIENT);
                    Bundle bundle = new Bundle();
                    bundle.putString("data", "对 就是那个丁磊养的黑猪肉");
                    msg2.setData(bundle);
                    msg2.replyTo = mClientMessenger;
                    try {
                        mMessenger.send(msg2);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    super.handleMessage(msg);
            }

        }
    }
}
