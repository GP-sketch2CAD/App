package gachon.termproject.finalproject;

import android.os.Handler;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;

import gachon.termproject.finalproject.ArctObj.Door;
import gachon.termproject.finalproject.ArctObj.NemoColumn;
import gachon.termproject.finalproject.ArctObj.NemoRoom;
import gachon.termproject.finalproject.ArctObj.NemoWindow;
import gachon.termproject.finalproject.stack.StackManager;

public class Exporter {
    JSONObject json;
    StackManager stackManager;
    String ipAddress = "172.30.1.40";
    Socket socket;
    int port = 8080;

    DataOutputStream dos;
    DataInputStream dis;

    public Exporter(StackManager stackManager) {
        this.stackManager = stackManager;

    }

    public void obj2json() {
        json = new JSONObject();
        JSONObject temp, attr;

        try {
            json.put("name", "from_android");

            JSONArray jsonArray = new JSONArray();
            JSONArray tempArray;
            for (Object obj : stackManager.objStack) {
                if (obj instanceof NemoRoom) {
                    temp = new JSONObject();
                    temp.put("type", "nemoRoom");

                    tempArray = new JSONArray();
                    tempArray.put(((NemoRoom) obj).innerCords[1].getX());
                    tempArray.put(-((NemoRoom) obj).innerCords[1].getY());
                    temp.put("leftBot", tempArray);

                    tempArray = new JSONArray();
                    tempArray.put(((NemoRoom) obj).innerCords[3].getX());
                    tempArray.put(-((NemoRoom) obj).innerCords[3].getY());
                    temp.put("rightTop", tempArray);
                    temp.put("thickness", ((NemoRoom) obj).thickness);
                    jsonArray.put(temp);
                } else if (obj instanceof Door) {
                    temp = new JSONObject();
                    temp.put("type", "door");

                    tempArray = new JSONArray();
                    tempArray.put(((Door) obj).coords[1].getX());
                    tempArray.put(-((Door) obj).coords[1].getY());
                    temp.put("cord", tempArray);

                    temp.put("degree", ((Door) obj).degree);
                    temp.put("doorType", ((Door) obj).doorType);

                    attr = new JSONObject();
                    attr.put("garo", ((Door) obj).garo);
                    attr.put("sero", ((Door) obj).sero);
                    attr.put("doke", ((Door) obj).doke);
                    attr.put("frame", ((Door) obj).frame);

                    temp.put("attr", attr);
                    jsonArray.put(temp);
                } else if (obj instanceof NemoWindow) {
                    temp = new JSONObject();
                    temp.put("type", "window");

                    tempArray = new JSONArray();
                    tempArray.put(((NemoWindow) obj).coords[1].getX());
                    tempArray.put(-((NemoWindow) obj).coords[1].getY());
                    temp.put("cord", tempArray);

                    temp.put("degree", ((NemoWindow) obj).degree);
                    temp.put("windowType", ((NemoWindow) obj).windowType);

                    attr = new JSONObject();
                    attr.put("garo", ((NemoWindow) obj).garo);
                    attr.put("sero", ((NemoWindow) obj).sero);
                    attr.put("frame_garo", ((NemoWindow) obj).frame_garo);
                    attr.put("frame_sero", ((NemoWindow) obj).frame_sero);

                    temp.put("attr", attr);
                    jsonArray.put(temp);
                } else if (obj instanceof NemoColumn) {
                    temp = new JSONObject();
                    temp.put("type", "column");
                    tempArray = new JSONArray();
                    tempArray.put(((NemoColumn) obj).coords[1].getX());
                    tempArray.put(-((NemoColumn) obj).coords[1].getY());
                    temp.put("leftBot", tempArray);

                    tempArray = new JSONArray();
                    tempArray.put(((NemoColumn) obj).coords[3].getX());
                    tempArray.put(-((NemoColumn) obj).coords[3].getY());
                    temp.put("rightTop", tempArray);

                    jsonArray.put(temp);
                }
            }

            json.put("arctObj", jsonArray);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void connect2Server(File fileDIr) {
        Handler mHandler = new Handler();

        Log.w("connect", "연결 하는중");

        Thread checkUpdate = new Thread() {
            public void run() {
                int data_len = 0;
                try {
                    socket = new Socket(ipAddress, port);

                    Log.e("서버", "서버 ");
                    // json data 서버로 보내기
                    dos = new DataOutputStream(socket.getOutputStream());
                    dos.writeUTF(json.toString());

                    // 서버에서 생성된 dxf파일 받아오기
                    dis = new DataInputStream(socket.getInputStream());
                    File f = new File(fileDIr, "Dxffile.dxf");
                    FileOutputStream output = new FileOutputStream(f);
                    BufferedOutputStream bos = new BufferedOutputStream(output);

                    int readdata = 0;
                    byte[] buf = new byte[1024];
                    while ((readdata = dis.read(buf)) != -1) {
                        bos.write(buf, 0, readdata);
                        bos.flush();
                    }
                    bos.close();
                    dos.close();
                    dis.close();
                    System.out.println("Dxffile.dxf 수신완료");
                } catch (IOException e) {
                    Log.e("서버 접속 못함", "서버 접속 못함");
                    e.printStackTrace();

                } catch (Exception e) {
                    System.out.println("서버 에러!!");
                    e.printStackTrace();
                }
            }
        };
        checkUpdate.start();
    }


}
