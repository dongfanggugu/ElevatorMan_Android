package com.honyum.elevatorMan.view;


import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AudioRecorder implements RecordStrategy {

    private MediaRecorder recorder;
    private String fileName;
    private String fileFolder = Environment.getExternalStorageDirectory()
            + File.separator + "concretecloud/audio/";

    private boolean isRecording = false;

    @Override
    public void ready() {
        // TODO Auto-generated method stub
        File file = new File(fileFolder);
        if (!file.exists()) {
            file.mkdirs();
        }
        fileName = getCurrentDate();
        recorder = new MediaRecorder();
        recorder.setOutputFile(fileFolder + "/" + fileName + ".amr");
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);// 设置MediaRecorder的音频源为麦克风
        recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);// 设置MediaRecorder录制的音频格式
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);// 设置MediaRecorder录制音频的编码为amr
    }

    // 以当前时间作为文件名
    private String getCurrentDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_HHmmss");
        Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
        return formatter.format(curDate);
    }

    @Override
    public void start() {
        // TODO Auto-generated method stub
        if (!isRecording) {
            try {
                recorder.prepare();
                recorder.start();
            } catch (IllegalStateException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            isRecording = true;
        }

    }

    @Override
    public void stop() {
        // TODO Auto-generated method stub
        if (isRecording) {
            try {
                recorder.stop();
                recorder.release();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
            isRecording = false;
        }

    }

    @Override
    public void deleteOldFile() {
        // TODO Auto-generated method stub
        File file = new File(fileFolder + "/" + fileName + ".amr");
        file.deleteOnExit();
    }

    @Override
    public double getAmplitude() {
        // TODO Auto-generated method stub
        if (!isRecording) {
            return 0;
        }
        return recorder.getMaxAmplitude();
    }

    @Override
    public String getFilePath() {
        // TODO Auto-generated method stub
        return fileFolder + "/" + fileName + ".amr";
    }

    /**
     * 获取音频时长
     *
     * @param filePath 音频路径
     * @return 音频时长(秒)
     */
    public int getAudioDuration(String filePath) {
        try {
            MediaPlayer player = new MediaPlayer();
            player.setDataSource(filePath);
            player.prepare();
            return (int) Math.ceil((player.getDuration() / 1000));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
