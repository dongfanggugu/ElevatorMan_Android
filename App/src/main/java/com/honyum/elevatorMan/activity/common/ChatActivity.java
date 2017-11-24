package com.honyum.elevatorMan.activity.common;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.honyum.elevatorMan.R;
import com.honyum.elevatorMan.adapter.ChatAdapter;
import com.honyum.elevatorMan.base.BaseFragmentActivity;
import com.honyum.elevatorMan.base.ImageCallback;
import com.honyum.elevatorMan.base.ListItemCallback;
import com.honyum.elevatorMan.constant.ConstantIntent;
import com.honyum.elevatorMan.data.AlarmNotify;
import com.honyum.elevatorMan.data.ChannelInfo;
import com.honyum.elevatorMan.net.AudioUrlResponse;
import com.honyum.elevatorMan.net.ChannelResponse;
import com.honyum.elevatorMan.net.ChatListRequest;
import com.honyum.elevatorMan.net.ChatListResponse;
import com.honyum.elevatorMan.net.SendChatRequest;
import com.honyum.elevatorMan.net.UploadAudioRequest;
import com.honyum.elevatorMan.net.UploadImageRequest;
import com.honyum.elevatorMan.net.UploadImageResponse;
import com.honyum.elevatorMan.net.UploadVideoRequest;
import com.honyum.elevatorMan.net.VideoUrlResponse;
import com.honyum.elevatorMan.net.WorkNameResponse;
import com.honyum.elevatorMan.net.WorkersRequest;
import com.honyum.elevatorMan.net.base.NetConstant;
import com.honyum.elevatorMan.net.base.NetTask;
import com.honyum.elevatorMan.net.base.NewRequestHead;
import com.honyum.elevatorMan.net.base.RequestBean;
import com.honyum.elevatorMan.net.base.RequestHead;
import com.honyum.elevatorMan.receiver.JPushMsgReceiver;
import com.honyum.elevatorMan.view.AudioRecorder;
import com.honyum.elevatorMan.view.RecordButton;

import org.litepal.crud.DataSupport;

import java.io.File;
import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import static android.provider.Contacts.PresenceColumns.INVISIBLE;
import static android.view.View.GONE;
import static com.honyum.elevatorMan.activity.common.CommonPicturePickActivity.PICK_TAG;
import static com.honyum.elevatorMan.activity.common.RecordVideoActivity.VIDEO_TAG;

public class ChatActivity extends BaseFragmentActivity implements ListItemCallback<String>, ImageCallback<String> {

    private static final int REFRESH_NOES = 0;
    private static final int REFRESH_TOP = 1;
    private static final int REFRESH_BOTTOM = 2;
    private static final int REFRESH_TEMP = 3;


    private static final int CHAT_CONTENT_TEXT = 1;
    private static final int CHAT_CONTENT_VOICE = 2;
    //private static final int VIDEO_TAG = 1;
    private static final int CARMA_TAG = 3;

    private FrameLayout fl_vv;
    private VideoView videoView_show;
    private FrameLayout fl_image;
    private ImageView iv_show;



    private AudioRecorder audioRecorder;

    private PullToRefreshListView ptrListView;

    private ListView listView;

    private ChatAdapter adapter;

    private List<ChatListResponse.ChatListBody> chatList;

    private EditText etChat;


    //语音时长
    private int audioDuration;

    private Long maxCode;

    private static boolean isForeground;

    //报警id
    private String mAlarmId;

    public static final int MODE_WORKER = 0;

    public static final int MODE_PROPERTY = 1;

    private int mode;

    private ListView channelListView;

    //private int curChannel = 0;

    private List<ChannelInfo> arrayChannel;
    private ListView personListView;

    private View line_view;

    public static boolean isForeground() {
        return isForeground;
    }

    private static OnActivityFinishListener onActivityFinishListener;


    @Override
    public void performItemCallback(String data) {
        fl_vv = (FrameLayout) findViewById(R.id.fl_vv);
        fl_vv.setVisibility(View.VISIBLE);
        Uri uri = Uri.parse(data);
        videoView_show.setVideoURI(uri);
        videoView_show.requestFocus();
        //videoView_show.set
        videoView_show.start();
        videoView_show.setClickable(true);
        fl_vv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (videoView_show.isPlaying()) {
                    videoView_show.stopPlayback();
                }
                fl_vv.setVisibility(GONE);
                findViewById(R.id.ll_content).setVisibility(View.VISIBLE);
            }
        });
        findViewById(R.id.ll_content).setVisibility(GONE);


    }


    @Override
    public void performImageCallback(String data) {
        fl_image = (FrameLayout) findViewById(R.id.fl_image);
        fl_image.setVisibility(View.VISIBLE);
        iv_show = (ImageView) findViewById(R.id.iv_show);
        Glide.with(this)
                .load(data)
                .into(iv_show);
        iv_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fl_image.setVisibility(GONE);
                findViewById(R.id.ll_content).setVisibility(View.VISIBLE);
            }
        });
        findViewById(R.id.ll_content).setVisibility(GONE);

    }


    public interface OnActivityFinishListener {
        void onFinishListener();
    }

    public static void setOnActivityFinishListener(OnActivityFinishListener onActivityFinishListener) {
        ChatActivity.onActivityFinishListener = onActivityFinishListener;
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (videoView_show.isPlaying()) {
            videoView_show.stopPlayback();
        }
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Intent intent = getIntent();

        if (intent != null) {
            mode = intent.getIntExtra("enter_mode", MODE_WORKER);

            if (MODE_WORKER == mode) {
                mAlarmId = intent.getStringExtra("alarm_id");
            }
        }



        if (audioRecorder == null) {
            audioRecorder = new AudioRecorder();
        }

        initTitleBar();
        refreshData();
        initView();

    }

    private void getChannel() {
        RequestBean request = new RequestBean();
        RequestHead head = new RequestHead();

        head.setAccessToken(getConfig().getToken());
        head.setUserId(getConfig().getUserId());

        request.setHead(head);

        String server = getConfig().getServer() + NetConstant.URL_CHAT_CHANNEL;

        NetTask netTask = new NetTask(server, request) {
            @Override
            protected void onResponse(NetTask task, String result) {
                ChannelResponse response = ChannelResponse.getChannelResponse(result);

                arrayChannel = response.getBody();

                if (arrayChannel.size() > 0) {
                    mAlarmId = arrayChannel.get(0).getId();

                    setTitle(R.id.title, arrayChannel.get(0).getText());

                    initChatContent(REFRESH_NOES, 10);

                    ChannelAdapter adapter = new ChannelAdapter(ChatActivity.this, arrayChannel);

                    channelListView.setAdapter(adapter);

                    channelListView.setOnItemClickListener(channelClickListener);

                    getGroupPersons(mAlarmId);
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ChatActivity.this);
                    Dialog dialog = null;
                    builder.setTitle("提示");
                    builder.setCancelable(false);

                    builder.setMessage("当前无对话！")
                            .setPositiveButton("确定",
                                    new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            finish();
                                            dialog.cancel();

                                        }
                                    });
                    dialog = builder.create();
                    dialog.show();

                }

            }
        };

        addBackGroundTask(netTask);
    }

    private void initChatContent(final int refreshType, int rows) {

        String server = getConfig().getServer() + NetConstant.GET_CHAT_LIST;

        ChatListRequest request = new ChatListRequest();
        RequestHead head = new RequestHead();
        ChatListRequest.RequestBody body = request.new RequestBody();

        head.setAccessToken(getConfig().getToken());
        head.setUserId(getConfig().getUserId());

        body.setAlarmId(mAlarmId);
        body.setRows(rows);

        if (REFRESH_BOTTOM == refreshType) {
            body.setMaxCode(null);

        } else {
            body.setMaxCode(maxCode);
        }

        request.setHead(head);
        request.setBody(body);

        NetTask netTask = new NetTask(server, request) {
            @Override
            protected void onResponse(NetTask task, String result) {
                ptrListView.onRefreshComplete();

                ChatListResponse response = ChatListResponse.getChatList(result);
                List<ChatListResponse.ChatListBody> chatList = response.getBody();


                fillChatData(chatList, refreshType);
            }
        };

        addBackGroundTask(netTask);
    }

    private void fillChatData(List<ChatListResponse.ChatListBody> chatList, int refreshType) {
        if (refreshType == REFRESH_TOP) {

            if (null == chatList || chatList.size() == 0) {
                return;
            }

            maxCode = chatList.get(chatList.size() - 1).getCode();

            for (ChatListResponse.ChatListBody body1 : chatList) {
                adapter.add(0, body1);
            }
            listView.setSelection(chatList.size() - 1);

        } else if (refreshType == REFRESH_NOES) {
            adapter.clearAll(true);

            if (null == chatList || chatList.size() == 0) {
                return;
            }

            maxCode = chatList.get(chatList.size() - 1).getCode();

            for (ChatListResponse.ChatListBody body1 : chatList) {
                adapter.add(0, body1);
            }
            listView.setSelection(listView.getBottom());

        } else if (refreshType == REFRESH_BOTTOM) {

            if (null == chatList || chatList.size() == 0) {
                return;
            }

            adapter.cleanLoadData();
            for (ChatListResponse.ChatListBody body1 : chatList) {
                adapter.add(body1);
            }
            listView.setSelection(listView.getBottom());
        } else if (refreshType == REFRESH_TEMP) {
            if (null == chatList || chatList.size() == 0) {
                return;
            }

            for (ChatListResponse.ChatListBody body1 : chatList) {
                adapter.add(body1);
            }
            listView.setSelection(listView.getBottom());
        }
    }

    @Override
    protected void onResume() {
        isForeground = true;
        super.onResume();

    }

    private void refreshData() {
        maxCode = null;
        if (MODE_PROPERTY == mode) {
            getChannel();
        } else {
            //adapter.clearAll(true);
            initChatContent(REFRESH_NOES, 10);
        }
    }

    private void initView() {

        channelListView = (ListView) findViewById(R.id.list_channel);
        channelListView.setVisibility(GONE);

        videoView_show = (VideoView) findViewById(R.id.videoView_show);
        line_view = findViewById(R.id.line_view);


        JPushMsgReceiver.setChatMsgListener(new JPushMsgReceiver.onChatMsgListener() {
            @Override
            public void chatMsgListener(AlarmNotify alarmNotify) {

                String alarmId = alarmNotify.getAlarmId();

                if (alarmId.equals(mAlarmId)) {
                    initChatContent(REFRESH_BOTTOM, 1);

                } else {

                    //ChannelAdapter adapter = (ChannelAdapter) channelListView.getAdapter();

                    for (ChannelInfo info : arrayChannel) {
                        if (info.getId().equals(alarmId)) {
                            info.setToRead(true);
                            break;
                        }
                    }

                    ChannelAdapter adapter = (ChannelAdapter) channelListView.getAdapter();
                    adapter.notifyDataSetChanged();

                }
            }
        });

        personListView = (ListView) findViewById(R.id.list_persons);
        personListView.setVisibility(View.INVISIBLE);
        ptrListView = (PullToRefreshListView) findViewById(R.id.ptrListView);
        listView = ptrListView.getRefreshableView();
        listView.setSelector(R.color.transfer);
        listView.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);


        if (!TextUtils.isEmpty(mAlarmId))
            chatList = DataSupport.where("alarmId = ?", mAlarmId).find(ChatListResponse.ChatListBody.class);
        if (chatList == null || chatList.size() == 0) {
            chatList = new ArrayList<ChatListResponse.ChatListBody>();
        }
        adapter = new ChatAdapter(this, chatList, getConfig().getUserId());
        listView.setAdapter(adapter);

        ptrListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                initChatContent(REFRESH_TOP, 10);
            }
        });

        findViewById(R.id.chat_voice).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findViewById(R.id.chat_voice).setVisibility(View.INVISIBLE);
                findViewById(R.id.ll_chat_input).setVisibility(GONE);
                findViewById(R.id.chat_keyboard).setVisibility(View.VISIBLE);
                findViewById(R.id.chat_voice_btn).setVisibility(View.VISIBLE);
            }
        });

        findViewById(R.id.chat_keyboard).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findViewById(R.id.chat_keyboard).setVisibility(View.INVISIBLE);
                findViewById(R.id.chat_voice_btn).setVisibility(GONE);
                findViewById(R.id.chat_voice).setVisibility(View.VISIBLE);
                findViewById(R.id.ll_chat_input).setVisibility(View.VISIBLE);
            }
        });

        etChat = (EditText) findViewById(R.id.chat_et);
        etChat.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    listView.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
                }
            }
        });
        etChat.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId,
                                          KeyEvent event) {
                long oldTime = 0, newTime = 0;   //&& event.getAction() == KeyEvent.ACTION_DOWN
                if (actionId == EditorInfo.IME_ACTION_SEND) {

                    newTime = System.currentTimeMillis();
                    if (oldTime == 0 || newTime - oldTime > 1000) {
                        List<ChatListResponse.ChatListBody> chatList_img = new ArrayList<ChatListResponse.ChatListBody>();
                        ChatListResponse.ChatListBody body_img = new ChatListResponse.ChatListBody();
                        body_img.setSenderId(getConfig().getUserId());
                        body_img.setType(CHAT_CONTENT_TEXT + "");
                        body_img.setSenderName(getConfig().getName());
                        body_img.setContent(etChat.getText().toString());
                        body_img.setLoad(true);
                        chatList_img.add(body_img);
                        fillChatData(chatList_img, REFRESH_TEMP);
                        sendChat(CHAT_CONTENT_TEXT, etChat.getText().toString());
                    }
                    oldTime = newTime;
                    return true;
                }
                return false;
            }
        });

        listView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                etChat.clearFocus();

                listView.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_DISABLED);
                InputMethodManager imm = (InputMethodManager) ChatActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(etChat.getWindowToken(), 0);
                }

                return false;
            }
        });

        RecordButton rb = (RecordButton) findViewById(R.id.chat_voice_btn);
        rb.setAudioRecord(audioRecorder);
        rb.setRecordListener(new RecordButton.RecordListener() {
            @Override
            public void recordEnd(String filePath) {
                List<ChatListResponse.ChatListBody> chatList = new ArrayList<ChatListResponse.ChatListBody>();
                ChatListResponse.ChatListBody body = new ChatListResponse.ChatListBody();
                body.setSenderId(getConfig().getUserId());
                body.setType("2");
                body.setTimeLength(2);
                body.setLoad(true);
                body.setSenderName(getConfig().getName());
                body.setContent(filePath);
                chatList.add(body);
                fillChatData(chatList, REFRESH_TEMP);
                uploadVoice(filePath);
            }
        });

//        findViewById(R.id.chat_send).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String textChat = etChat.getText().toString();
//                sendChat(CHAT_CONTENT_TEXT, textChat);
//            }
//        });
        findViewById(R.id.chat_video).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChatActivity.this, RecordVideoActivity.class);
                intent.putExtra("AlarmId", mAlarmId);
                intent.putExtra("tag", "video");
                startActivityForResult(intent, VIDEO_TAG);

            }
        });
        findViewById(R.id.chat_image).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChatActivity.this, CommonPicturePickActivity.class);
                intent.putExtra("AlarmId", mAlarmId);
                intent.putExtra("tag", "image");
                startActivityForResult(intent, PICK_TAG);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);

        if (data != null) {
            String url = data.getStringExtra(ConstantIntent.INTENT_DATA);
            switch (requestCode) {
                case VIDEO_TAG:
                    List<ChatListResponse.ChatListBody> chatList = new ArrayList<ChatListResponse.ChatListBody>();
                    ChatListResponse.ChatListBody body = new ChatListResponse.ChatListBody();
                    body.setSenderId(getConfig().getUserId());
                    body.setType("4");
                    body.setSenderName(getConfig().getName());
                    body.setContent("");
                    body.setLoad(true);
                    chatList.add(body);
                    fillChatData(chatList, REFRESH_TEMP);
                    uploadVideo(url);
                    break;
                case PICK_TAG:
                    List<ChatListResponse.ChatListBody> chatList_img = new ArrayList<ChatListResponse.ChatListBody>();
                    ChatListResponse.ChatListBody body_img = new ChatListResponse.ChatListBody();
                    body_img.setSenderId(getConfig().getUserId());
                    body_img.setType("3");
                    body_img.setSenderName(getConfig().getName());
                    body_img.setContent("");
                    body_img.setLoad(true);
                    chatList_img.add(body_img);
                    fillChatData(chatList_img, REFRESH_TEMP);
                    requestUploadImage(url);
                    break;
                default:
                    showToast("选取内容出错");
                    break;
            }
        }


    }

    /**
     * 上传语音文件
     *
     * @param filePath 语音文件路径
     */
    private void uploadVoice(String filePath) {

        if (TextUtils.isEmpty(filePath)) {
            return;
        }
        try {
            audioDuration = audioRecorder.getAudioDuration(filePath);

            FileInputStream inputStream = new FileInputStream(filePath);
            byte[] buffer = new byte[1024];
            int len;
            StringBuilder sb = new StringBuilder();
            while ((len = inputStream.read(buffer)) != -1) {
                String decode = Base64.encodeToString(buffer, 0, len, Base64.DEFAULT);
                sb.append(decode);
            }
            inputStream.close();

            String server = getConfig().getServer() + NetConstant.UPLOAD_AUDIO;

            UploadAudioRequest request = new UploadAudioRequest();
            RequestHead head = new RequestHead();
            UploadAudioRequest.RequestBody body = request.new RequestBody();

            head.setAccessToken(getConfig().getToken());
            head.setUserId(getConfig().getUserId());

            body.setAudio(sb.toString());

            request.setHead(head);
            request.setBody(body);

            NetTask netTask = new NetTask(server, request) {
                @Override
                protected void onResponse(NetTask task, String result) {
                    AudioUrlResponse response = AudioUrlResponse.getAudioUrl(result);
                    String audioUrl = response.getBody().getUrl();
                    sendChat(CHAT_CONTENT_VOICE, audioUrl);
                }
            };

            addBackGroundTask(netTask);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送聊天信息
     *
     * @param chatType 聊天类型
     * @param content  聊天内容
     */
    private void sendChat(int chatType, String content) {

        if (chatType == CHAT_CONTENT_TEXT && TextUtils.isEmpty(content)) {
            showToast("消息内容不能为空");
            return;
        }
        etChat.setText("");
        String server = getConfig().getServer() + NetConstant.SEND_CHAT;

        SendChatRequest request = new SendChatRequest();
        RequestHead head = new RequestHead();
        SendChatRequest.RequestBody body = request.new RequestBody();

        head.setAccessToken(getConfig().getToken());
        head.setUserId(getConfig().getUserId());

        body.setAlarmId(mAlarmId);
        body.setType(chatType + "");
        body.setUserName(getConfig().getName());

        if (chatType == CHAT_CONTENT_TEXT) {
//            body.setContent(content);
            try {
                body.setContent(URLEncoder.encode(content, "utf-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } else {
            body.setContent(content);
            body.setTimeLength(audioDuration);
        }

        request.setHead(head);
        request.setBody(body);

        NetTask netTask = new NetTask(server, request) {
            @Override
            protected void onResponse(NetTask task, String result) {
                listView.setSelection(listView.getBottom());

                //refreshData();
            }
        };

        addBackGroundTask(netTask);
    }


    private void initTitleBar() {

        if (MODE_WORKER == mode) {
            initTitleBar("聊天", R.id.title, R.drawable.back_normal, backClickListener);
        } else {

            initTitleBar(R.id.title, "聊天",
                    R.drawable.back_normal, backClickListener,
                    R.drawable.more_chat, showChannelListener);
        }
    }

    View.OnClickListener showChannelListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            if (GONE == channelListView.getVisibility()) {
                channelListView.setVisibility(View.VISIBLE);
                personListView.setVisibility(View.VISIBLE);
                line_view.setVisibility(View.VISIBLE);
            } else {
                channelListView.setVisibility(GONE);
                personListView.setVisibility(View.GONE);
                line_view.setVisibility(View.GONE);
            }
        }
    };

    private RequestBean getImageRequestBean(String userId, String token, String path) {
        UploadImageRequest request = new UploadImageRequest();
        request.setHead(new NewRequestHead().setuserId(userId).setaccessToken(token));
        request.setBody(request.new UploadImageBody().setImg(path));
        return request;
    }


    private void requestUploadImage(final String path) {
        NetTask task = new NetTask(getConfig().getServer() + NetConstant.UP_LOAD_IMG,
                getImageRequestBean(getConfig().getUserId(), getConfig().getToken(), path)) {
            @Override
            protected void onResponse(NetTask task, String result) {
                UploadImageResponse response = UploadImageResponse.getUploadImageResponse(result);
                if (response.getHead() != null && response.getHead().getRspCode().equals("0")) {
                    String url = response.getBody().getUrl();
                    // showAppToast(getString(R.string.sucess));
                    sendChat(3, url);


                    File file = new File(path);
                    if (file != null && file.exists()) {
                        //   Log.e(LOG_TAG, "file.exists():" + file.exists());
                        file.delete();
                    }
                    //3是图片

                }
                //Log.e("!!!!!!!!!!!!!!", "onResponse: "+ msInfoList.get(0).getMainttypeId());

            }
        };

        addBackGroundTask(task);
    }

    /**
     * 上传视频文件
     *
     * @param filePath 文件路径
     */
    private void uploadVideo(String filePath) {

        if (TextUtils.isEmpty(filePath)) {
            return;
        }
        try {
            //audioDuration = audioRecorder.getAudioDuration(filePath);

            FileInputStream inputStream = new FileInputStream(filePath);
            byte[] buffer = new byte[1024];
            int len;
            StringBuilder sb = new StringBuilder();
            while ((len = inputStream.read(buffer)) != -1) {
                String decode = Base64.encodeToString(buffer, 0, len, Base64.DEFAULT);
                sb.append(decode);
            }
            inputStream.close();

            String server = getConfig().getServer() + NetConstant.UPLOAD_VIDEO;

            UploadVideoRequest request = new UploadVideoRequest();
            RequestHead head = new RequestHead();
            UploadVideoRequest.RequestBody body = request.new RequestBody();

            head.setAccessToken(getConfig().getToken());
            head.setUserId(getConfig().getUserId());

            body.setVideo(sb.toString());

            request.setHead(head);
            request.setBody(body);

            NetTask netTask = new NetTask(server, request) {
                @Override
                protected void onResponse(NetTask task, String result) {
                    VideoUrlResponse response = VideoUrlResponse.getVideoUrl(result);
                    String url = response.getBody().getUrl();
                    if (!TextUtils.isEmpty(url)) {
                        sendChat(4, url);
                        File file = new File(filePath);
                        if (file != null && file.exists()) {
                            //   Log.e(LOG_TAG, "file.exists():" + file.exists());
                            file.delete();
                        }

                    }
                    //sendChat(CHAT_CONTENT_VOICE, audioUrl);
                }

            };

            addBackGroundTask(netTask);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        isForeground = false;
        if (onActivityFinishListener != null) {
            onActivityFinishListener.onFinishListener();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (chatList != null)
            for (ChatListResponse.ChatListBody body : chatList) {
                body.save();
            }
    }

    private class ChannelAdapter extends BaseAdapter {

        private Context context;

        private List<ChannelInfo> channelList;

        private int selectedItem;


        public ChannelAdapter(Context context, List<ChannelInfo> channelList) {
            this.context = context;

            this.channelList = channelList;

            selectedItem = 0;
        }


        @Override
        public int getCount() {
            return this.channelList.size();
        }

        @Override
        public Object getItem(int position) {
            return this.channelList.get(position);
        }



        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (null == convertView) {
                convertView = View.inflate(this.context, R.layout.layout_chat_channel_item, null);
            }

            TextView textView = (TextView) convertView.findViewById(R.id.tv_channel);

            textView.setText(this.channelList.get(position).getText());

            convertView.setTag(R.id.channel_id, this.channelList.get(position).getId());
            convertView.setTag(R.id.channel_text, this.channelList.get(position).getText());

            if (position == selectedItem) {
                convertView.setBackgroundColor(getResources().getColor(R.color.grey));
            } else {
                convertView.setBackgroundColor(getResources().getColor(R.color.transparent));
            }

            if (this.channelList.get(position).isToRead()) {
                convertView.findViewById(R.id.view_flag).setVisibility(View.VISIBLE);

            } else {
                convertView.findViewById(R.id.view_flag).setVisibility(View.INVISIBLE);
            }

            return convertView;
        }

        public int getSelectedItem() {
            return selectedItem;
        }

        public void setSelectedItem(int selectedItem) {
            this.selectedItem = selectedItem;
            notifyDataSetChanged();
        }

    }

    AdapterView.OnItemClickListener channelClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            arrayChannel.get(position).setToRead(false);

            ChannelAdapter adapter = (ChannelAdapter) parent.getAdapter();
            String alarmId = (String) view.getTag(R.id.channel_id);
            int curChannel = adapter.getSelectedItem();

            if (curChannel == position) {
                getGroupPersons(alarmId);
                return;
            }


            mAlarmId = alarmId;

            maxCode = null;
            initChatContent(REFRESH_NOES, 10);

            String text = (String)view.getTag(R.id.channel_text);
            setTitle(R.id.title, text);

            adapter.setSelectedItem(position);

            getGroupPersons(alarmId);

            //channelListView.setVisibility(View.GONE);
        }
    };

    private void getGroupPersons(String alarmId) {
        NetTask task = new NetTask(getConfig().getServer() + NetConstant.GET_CHATUSERBYALARMID,
                getRequest(alarmId)) {
            @Override
            protected void onResponse(NetTask task, String result) {
                WorkNameResponse response = WorkNameResponse.getResult(result);
                if (response != null)
                    response.getBody();
                List<ChannelInfo> ci = new ArrayList<ChannelInfo>();
                for (WorkNameResponse.WorkNameResponseBody body : response.getBody()
                        ) {
                    ChannelInfo cinfo = new ChannelInfo();
                    cinfo.setId(alarmId);
                    cinfo.setToRead(false);
                    cinfo.setText(body.getName());
                    ci.add(cinfo);
                }
                ChannelAdapter cl = new ChannelAdapter(ChatActivity.this, ci);
                personListView.setAdapter(cl);
                personListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        channelListView.setVisibility(View.INVISIBLE);
                        personListView.setVisibility(View.INVISIBLE);
                        line_view.setVisibility(View.INVISIBLE);

                    }
                });
            }
        };
        addBackGroundTask(task);

    }

    /**
     * 获取请求bean
     *
     * @param alarmId
     * @return
     */
    private RequestBean getRequest(String alarmId) {
        WorkersRequest request = new WorkersRequest();
        WorkersRequest.WorkersReqBody body = request.new WorkersReqBody();
        RequestHead head = new RequestHead();

        head.setUserId(getConfig().getUserId());
        head.setAccessToken(getConfig().getToken());

        body.setAlarmId(alarmId);

        request.setBody(body);
        request.setHead(head);

        return request;
    }
}
