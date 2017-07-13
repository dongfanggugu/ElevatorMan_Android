package com.honyum.elevatorMan.adapter;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.honyum.elevatorMan.R;
import com.honyum.elevatorMan.activity.common.ChatActivity;
import com.honyum.elevatorMan.base.ImageCallback;
import com.honyum.elevatorMan.base.ListItemCallback;
import com.honyum.elevatorMan.net.ChatListResponse;
import com.honyum.elevatorMan.utils.Utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;


public class ChatAdapter extends MyBaseAdapter<ChatListResponse.ChatListBody> {

    private static final int TEXT_LEFE = 0;
    private static final int TEXT_RIGHT = 1;
    private static final int VOICE_LEFE = 2;
    private static final int VOICE_RIGHT = 3;
    private static final int IMAGE_LEFE = 4;
    private static final int IMAGE_RIGHT = 5;
    private static final int VIDEO_LEFE = 6;
    private static final int VIDEO_RIGHT = 7;


    private static final long INTERVAL_TIME = 2 * 60 * 1000;

    private String mId;

    private MediaPlayer mp;

    private boolean isPlaying;

    private List<ChatListResponse.ChatListBody> list;

    public ChatAdapter(Context context, List<ChatListResponse.ChatListBody> dataSource, String mId) {
        super(context, dataSource);
        this.mId = mId;
        list = dataSource;
    }

    @Override
    public View getItemView(int position, View convertView, ViewGroup parent) {

        final ViewHolder vh;
        final ChatListResponse.ChatListBody body = getItem(position);
        int type = Utils.getInt(body.getType());
        final int itemViewType = getItemViewType(position);

        if (null == convertView) {
            switch (type) {
                case 1:
                    if (itemViewType == TEXT_RIGHT) {
                        convertView = inflater.inflate(R.layout.chat_text_right_item, parent, false);
                    } else {
                        convertView = inflater.inflate(R.layout.chat_text_left_item, parent, false);
                    }

                    vh = new ViewHolder();

                    vh.chatTime = (TextView) convertView.findViewById(R.id.chat_time);
                    vh.tvName = (TextView) convertView.findViewById(R.id.tv_name);
                    vh.chatContent = (TextView) convertView.findViewById(R.id.chat_content);

                    convertView.setTag(vh);
                    break;
                case 2:
                    if (itemViewType == VOICE_RIGHT) {
                        convertView = inflater.inflate(R.layout.chat_voice_right_item, parent, false);
                    } else {
                        convertView = inflater.inflate(R.layout.chat_voice_left_item, parent, false);
                    }

                    vh = new ViewHolder();

                    vh.chatTime = (TextView) convertView.findViewById(R.id.chat_time);
                    vh.tvName = (TextView) convertView.findViewById(R.id.tv_name);
                    vh.voiceContent = (ImageView) convertView.findViewById(R.id.chat_content);
                    vh.voiceDuration = (TextView) convertView.findViewById(R.id.chat_voice_duration);
                    vh.pdSending = (ProgressBar) convertView.findViewById(R.id.chat_progressBar);

                    convertView.setTag(vh);
                    break;
                case 3:
                    if (itemViewType == IMAGE_RIGHT) {
                        convertView = inflater.inflate(R.layout.chat_video_right_item, parent, false);
                    } else {
                        convertView = inflater.inflate(R.layout.chat_video_left_item, parent, false);
                    }

                    vh = new ViewHolder();

                    vh.chatTime = (TextView) convertView.findViewById(R.id.chat_time);
                    vh.tvName = (TextView) convertView.findViewById(R.id.tv_name);
                    vh.voiceContent = (ImageView) convertView.findViewById(R.id.voiceContent);
                    vh.voiceDuration = (TextView) convertView.findViewById(R.id.chat_voice_duration);
                    vh.pdSending = (ProgressBar) convertView.findViewById(R.id.chat_progressBar);

                    convertView.setTag(vh);
                    break;
                case 4:
                    if (itemViewType == VIDEO_RIGHT) {
                        convertView = inflater.inflate(R.layout.chat_video_right_item, parent, false);
                    } else {
                        convertView = inflater.inflate(R.layout.chat_video_left_item, parent, false);
                    }

                    vh = new ViewHolder();

                    vh.chatTime = (TextView) convertView.findViewById(R.id.chat_time);
                    vh.tvName = (TextView) convertView.findViewById(R.id.tv_name);
                    vh.voiceContent = (ImageView) convertView.findViewById(R.id.voiceContent);
                    vh.voiceDuration = (TextView) convertView.findViewById(R.id.chat_voice_duration);
                    vh.pdSending = (ProgressBar) convertView.findViewById(R.id.chat_progressBar);


                    convertView.setTag(vh);
                    break;
                default:
                    throw new RuntimeException("不正确的消息类型");
            }
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        long timestamp = Utils.getTimestamp(body.getSendTime());

        if (position == 0) {
            vh.chatTime.setVisibility(View.VISIBLE);
            vh.chatTime.setText(Utils.getTime(timestamp));
        } else {
            long preTimestamp = Utils.getTimestamp(list.get(position - 1).getSendTime());
            if (timestamp - preTimestamp > INTERVAL_TIME) {
                vh.chatTime.setVisibility(View.VISIBLE);
                vh.chatTime.setText(Utils.getTime(timestamp));
            } else {
                vh.chatTime.setVisibility(View.GONE);
            }
        }

        switch (type) {
            case 1:
                vh.tvName.setText(body.getSenderName());

                final String content = body.getContent();
                try {
                    vh.chatContent.setText(URLDecoder.decode(content, "utf-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                break;
            case 2:
                vh.tvName.setText(body.getSenderName());
                vh.voiceDuration.setHint(body.getTimeLength() + "＂");

                ViewGroup.LayoutParams params
                        = vh.voiceContent.getLayoutParams();

                if (10 >= body.getTimeLength()) {
                    params.width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, body.getTimeLength() + (body.getTimeLength() * 2 - 1) + 80,
                            context.getResources().getDisplayMetrics());
                } else if (10 < body.getTimeLength() && 20 > body.getTimeLength()) {
                    params.width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100,
                            context.getResources().getDisplayMetrics());
                } else if (20 < body.getTimeLength() && 30 > body.getTimeLength()) {
                    params.width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 120,
                            context.getResources().getDisplayMetrics());
                } else if (30 < body.getTimeLength() && 40 > body.getTimeLength()) {
                    params.width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 140,
                            context.getResources().getDisplayMetrics());
                } else if (40 < body.getTimeLength() && 50 > body.getTimeLength()) {
                    params.width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 160,
                            context.getResources().getDisplayMetrics());
                } else if (50 < body.getTimeLength() && 60 > body.getTimeLength()) {
                    params.width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 180,
                            context.getResources().getDisplayMetrics());
                } else if (60 < body.getTimeLength()) {
                    params.width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 200,
                            context.getResources().getDisplayMetrics());
                }


                vh.voiceContent.setLayoutParams(params);

                vh.voiceContent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        playVoice(body.getContent(), vh.voiceContent, itemViewType);
                    }
                });

                break;
            case 3:
                vh.tvName.setText(body.getSenderName());
                // vh.voiceDuration.setHint(body.getTimeLength() + "＂");
                vh.voiceDuration.setVisibility(View.GONE);
                Glide.with(context)
                        .load(body.getContent()).override(60, 80)
                        .into(vh.voiceContent);
                vh.voiceContent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((ImageCallback<String>)context).performImageCallback(body.getContent());
                    }
                });

                break;
            case 4:
                vh.tvName.setText(body.getSenderName());
                vh.voiceDuration.setVisibility(View.GONE);
                vh.voiceContent.setBackground(new BitmapDrawable(createVideoThumbnail(body.getContent(), 60, 80)));
                vh.voiceContent.setImageResource(R.drawable.video_play);
                vh.voiceContent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((ListItemCallback<String>)context).performItemCallback(body.getContent());
                    }
                });





                break;
        }

        return convertView;
    }

    private Bitmap createVideoThumbnail(String url, int width, int height) {
        Bitmap bitmap = null;
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        int kind = MediaStore.Video.Thumbnails.MINI_KIND;
        try {
            if (Build.VERSION.SDK_INT >= 14) {
                retriever.setDataSource(url, new HashMap<String, String>());
            } else {
                retriever.setDataSource(url);
            }
            bitmap = retriever.getFrameAtTime();
        } catch (IllegalArgumentException ex) {
            // Assume this is a corrupt video file
        } catch (RuntimeException ex) {
            // Assume this is a corrupt video file.
        } finally {
            try {
                retriever.release();
            } catch (RuntimeException ex) {
                // Ignore failures while cleaning up.
            }
        }
        if (kind == MediaStore.Images.Thumbnails.MICRO_KIND && bitmap != null) {
            bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
                    ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        }
        return bitmap;
    }


    private void playVoice(String audioUri, final ImageView voiceContent, final int pos) {
        try {
            if (mp == null || !isPlaying) {
                mp = new MediaPlayer();
            }

            if (isPlaying) {
                mp.stop();
                isPlaying = false;

                AnimationDrawable ad = (AnimationDrawable) voiceContent.getDrawable();
                ad.stop();

                if (pos == VOICE_LEFE) {
                    voiceContent.setImageResource(R.drawable.voice_right3);
                } else {
                    voiceContent.setImageResource(R.drawable.voice_left3);
                }

                mp.release();
                return;
            }


            ChatActivity.setOnActivityFinishListener(new ChatActivity.OnActivityFinishListener() {
                @Override
                public void onFinishListener() {
                    if (isPlaying) {
                        mp.stop();
                        isPlaying = false;

                        AnimationDrawable ad = (AnimationDrawable) voiceContent.getDrawable();
                        ad.stop();

                        if (pos == VOICE_LEFE) {
                            voiceContent.setImageResource(R.drawable.voice_right3);
                        } else {
                            voiceContent.setImageResource(R.drawable.voice_left3);
                        }

                        mp.release();
                    }
                }
            });


            mp.setDataSource(audioUri);
            mp.prepareAsync();

            mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.start();
                    isPlaying = true;

                    if (pos == VOICE_LEFE) {
                        voiceContent.setImageResource(R.drawable.chat_voice_left_anim);
                    } else {
                        voiceContent.setImageResource(R.drawable.chat_voice_right_anim);
                    }

                    AnimationDrawable ad = (AnimationDrawable) voiceContent.getDrawable();
                    ad.start();
                }
            });

            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    if (isPlaying) {
                        mp.stop();
                        isPlaying = false;

                        AnimationDrawable ad = (AnimationDrawable) voiceContent.getDrawable();
                        ad.stop();

                        if (pos == VOICE_LEFE) {
                            voiceContent.setImageResource(R.drawable.voice_right3);
                        } else {
                            voiceContent.setImageResource(R.drawable.voice_left3);
                        }

                        mp.release();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemViewType(int position) {
        int type = Utils.getInt(getItem(position).getType());
        String id = getItem(position).getSenderId();

        switch (type) {
            case 1:
                if (mId.equals(id)) {
                    return TEXT_RIGHT;
                } else {
                    return TEXT_LEFE;
                }
            case 2:
                if (mId.equals(id)) {
                    return VOICE_RIGHT;
                } else {
                    return VOICE_LEFE;
                }
            case 3:
                if (mId.equals(id)) {
                    return IMAGE_RIGHT;
                } else {
                    return IMAGE_LEFE;
                }
            case 4:
                if (mId.equals(id)) {
                    return VIDEO_RIGHT;
                } else {
                    return VIDEO_LEFE;
                }
            default:
                throw new RuntimeException("不正确的消息类型");
        }
    }

    @Override
    public int getViewTypeCount() {
        return 8;
    }

    static class ViewHolder {
        private TextView chatTime, tvName, chatContent, voiceDuration;
        private ImageView voiceContent;
        private ProgressBar pdSending;
    }
}
