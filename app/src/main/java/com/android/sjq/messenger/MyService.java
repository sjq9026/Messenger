package com.android.sjq.messenger;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

public class MyService extends Service {
    public MyService() {        }
    @Override
    public IBinder onBind(Intent intent) {
        return mMessenger.getBinder();
    }
    private Messenger mMessenger = new Messenger(new MessengerHandler());
    private static class MessengerHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Constants.MSG_FROM_CLIENT:
                    Log.i("TAG", "This is Service----->收到了来自客户端的消息："
                            + msg.getData().get("data"));
                    try {Thread.sleep(3000);} catch (InterruptedException e) {e.printStackTrace();}
                    Message replyMsg = Message.obtain(null, Constants.MSG_FROM_SERVICE);
                    Bundle bundle = new Bundle();
                    bundle.putString("reply", "网易养的猪");
                    replyMsg.setData(bundle);
                    Messenger messenger = msg.replyTo;
                    try {
                        messenger.send(replyMsg);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                default:
                    super.handleMessage(msg);
            }
        }
    }


}
