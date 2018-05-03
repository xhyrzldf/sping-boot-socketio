package com.matrix.simplesocketio.socket;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter.Listener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.net.URISyntaxException;

/**
 * socket io 客户端实现
 *
 * @author : M@tr!x [xhyrzldf@gmail.com]
 */
@SuppressWarnings("ALL")
@Slf4j
@Component
public class SocketClient {
    // 初始化连接
    private static Socket socket;
    // 连接标识
    private boolean isConnected;
    // 事件返回信息
    private String onMessageContent = null;


    /*
     * 初始化 server端连接地址
     */
    static {
        //初始化连接
        initSocket();
    }

    private static void initSocket() {
        try {
            socket = IO.socket("http://localhost:9090");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    /**
     * 连接处理
     */
    public void connectSocket() {
        // 连接
        socket.connect();
        // 添加监听事件
        addConnectListenerEvent();
    }

    /**
     * 断开连接
     */
    public void disConnectSocket() {
        // 断开连接
        socket.disconnect();
        // 取消监听事件
        addDisConnectListenerEvent();
    }

    /**
     * 启动客户端并指定发送消息任务开始时间
     *
     * @param id        指定的任务id
     * @param startTime 任务开始时间
     */
    public void startClient(String message) {
        //判断是否开启连接，如果没有开启就先初始化然后开启
        this.judgeConnection();

        //发送加入事件 'join' 内容为id , 同时监听返回事件'joinSuccess' ，执行回调函数
        socket.emit("join", message)
                .on("joinSuccess", data -> {
                    log.info("收到返回监听事件：" + data[0]);
                    onMessageContent = (String) data[0];
                });
    }

    /**
     * 判断连接
     */
    private void judgeConnection() {
        if (!isConnected) {
            initSocket();
            this.connectSocket();
        }
    }

    /**
     * 添加连接监听事件
     */
    private void addConnectListenerEvent() {
        socket.on(Socket.EVENT_CONNECT, onConnect);  // 连接成功
        socket.on(Socket.EVENT_DISCONNECT, onDisconnect);  // 断开连接
        socket.on(Socket.EVENT_CONNECT_ERROR, onConnetError);// 连接错误
        socket.on(Socket.EVENT_CONNECT_TIMEOUT, onConnetTimeout);  // 连接超时
    }

    /**
     * 关闭之前开启的监听事件
     */
    private void addDisConnectListenerEvent() {
        socket.off(Socket.EVENT_CONNECT, onConnect);  // 连接成功
        socket.off(Socket.EVENT_DISCONNECT, onDisconnect);  // 断开连接
        socket.off(Socket.EVENT_CONNECT_ERROR, onConnetError);// 连接错误
        socket.off(Socket.EVENT_CONNECT_TIMEOUT, onConnetTimeout);  // 连接超时
    }

    /**
     * 连接监听事件
     * 实现消息回调接口
     */
    private Listener onConnect = new Listener() {
        @Override
        public void call(Object... objects) {
            log.info("client 连接服务端成功：");
            if (!isConnected) {
                socket.emit("connect message", "hello");
            }
            isConnected = true;
        }
    };

    /**
     * 断开连接端口监听
     */
    private Listener onDisconnect = objects -> {
        log.info("client 断开服务端连接：" + objects[0]);
        isConnected = false;
    };

    /**
     * 连接错误监听
     */
    private Listener onConnetError = new Listener() {
        @Override
        public void call(Object... objects) {
            log.info("client 连接服务端错误：" + objects[0]);
        }
    };
    /**
     * 连接超时监听
     */
    private Listener onConnetTimeout = new Listener() {
        @Override
        public void call(Object... objects) {
            log.info("client 连接服务端超时：" + objects[0]);
        }
    };

}
