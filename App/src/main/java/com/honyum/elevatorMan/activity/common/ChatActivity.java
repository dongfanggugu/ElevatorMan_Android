package com.honyum.elevatorMan.activity.common;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.honyum.elevatorMan.R;
import com.honyum.elevatorMan.adapter.ChatAdapter;
import com.honyum.elevatorMan.base.BaseFragmentActivity;
import com.honyum.elevatorMan.data.AlarmNotify;
import com.honyum.elevatorMan.data.ChannelInfo;
import com.honyum.elevatorMan.net.AudioUrlResponse;
import com.honyum.elevatorMan.net.ChannelResponse;
import com.honyum.elevatorMan.net.ChatListRequest;
import com.honyum.elevatorMan.net.ChatListResponse;
import com.honyum.elevatorMan.net.SendChatRequest;
import com.honyum.elevatorMan.net.UploadAudioRequest;
import com.honyum.elevatorMan.net.base.NetConstant;
import com.honyum.elevatorMan.net.base.NetTask;
import com.honyum.elevatorMan.net.base.RequestBean;
import com.honyum.elevatorMan.net.base.RequestHead;
import com.honyum.elevatorMan.receiver.JPushMsgReceiver;
import com.honyum.elevatorMan.view.AudioRecorder;
import com.honyum.elevatorMan.view.RecordButton;

import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;
import java.net.FileNameMap;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends BaseFragmentActivity {

    private static final int REFRESH_NOES = 0;
    private static final int REFRESH_TOP = 1;
    private static final int REFRESH_BOTTOM = 2;

    private static final int CHAT_CONTENT_TEXT = 1;
    private static final int CHAT_CONTENT_VOICE = 2;

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

    public static boolean isForeground() {
        return isForeground;
    }

    private static OnActivityFinishListener onActivityFinishListener;

    public interface OnActivityFinishListener {
        void onFinishListener();
    }

    public static void setOnActivityFinishListener(OnActivityFinishListener onActivityFinishListener) {
        ChatActivity.onActivityFinishListener = onActivityFinishListener;
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

        isForeground = true;

        if (audioRecorder == null) {
            audioRecorder = new AudioRecorder();
        }

        initTitleBar();

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
                }

            }
        };

        addTask(netTask);
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

                    for (ChatListResponse.ChatListBody body1 : chatList) {
                        adapter.add(body1);
                    }
                    listView.setSelection(listView.getBottom());
                }
            }
        };

        addBackGroundTask(netTask);
    }

    private void initView() {

        channelListView = (ListView)findViewById(R.id.list_channel);
        channelListView.setVisibility(View.GONE);

        if (MODE_PROPERTY == mode) {
            getChannel();
        } else {
            initChatContent(REFRESH_NOES, 10);
        }


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

        ptrListView = (PullToRefreshListView) findViewById(R.id.ptrListView);
        listView = ptrListView.getRefreshableView();
        listView.setSelector(R.color.transfer);
        listView.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);

        chatList = new ArrayList<ChatListResponse.ChatListBody>();
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
                findViewById(R.id.ll_chat_input).setVisibility(View.GONE);
                findViewById(R.id.chat_keyboard).setVisibility(View.VISIBLE);
                findViewById(R.id.chat_voice_btn).setVisibility(View.VISIBLE);
            }
        });

        findViewById(R.id.chat_keyboard).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findViewById(R.id.chat_keyboard).setVisibility(View.INVISIBLE);
                findViewById(R.id.chat_voice_btn).setVisibility(View.GONE);
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
                uploadVoice(filePath);
            }
        });

        findViewById(R.id.chat_send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textChat = etChat.getText().toString();
                sendChat(CHAT_CONTENT_TEXT, textChat);
            }
        });
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
                etChat.setText("");
            }
        };

        addTask(netTask);
    }


    private void initTitleBar() {

        if (MODE_WORKER == mode) {
            initTitleBar("聊天", R.id.title, R.drawable.back_normal, backClickListener);
        } else {

            initTitleBar(R.id.title, "物业",
                    R.drawable.back_normal, backClickListener,
                    R.drawable.icon_menu, showChannelListener);
        }
    }

    View.OnClickListener showChannelListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            if (View.GONE == channelListView.getVisibility()) {
                channelListView.setVisibility(View.VISIBLE);
            } else {
                channelListView.setVisibility(View.GONE);
            }
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        isForeground = false;
        if (onActivityFinishListener != null) {
            onActivityFinishListener.onFinishListener();
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

            int curChannel = adapter.getSelectedItem();

            if (curChannel == position) {
                return;
            }

            String alarmId = (String)view.getTag(R.id.channel_id);

            mAlarmId = alarmId;

            maxCode = null;
            initChatContent(REFRESH_NOES, 10);

            String text = (String)view.getTag(R.id.channel_text);
            setTitle(R.id.title, text);

            adapter.setSelectedItem(position);

            channelListView.setVisibility(View.GONE);
        }
    };
}
