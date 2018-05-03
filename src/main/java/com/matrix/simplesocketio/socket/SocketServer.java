package com.matrix.simplesocketio.socket;

import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * socketio 服务端
 * netty-socketio 实现的 socket.io 服务端
 *
 * @author : M@tr!x [xhyrzldf@gmail.com]
 */
@Slf4j
@Component
public class SocketServer {
    private static SocketIOServer server;

    private static SocketServer socketServer = new SocketServer();

    private SocketServer() {
    }

    public static SocketServer getInstance() {
        return socketServer;
    }


    static {
        //初始化服务端
        initServer();
    }

    /**
     * 初始化服务端
     *
     * @return
     */
    private static void initServer() {
        Configuration config = new Configuration();
        config.setHostname("localhost");
        config.setPort(9090);
        server = new SocketIOServer(config);
    }

    /**
     * 启动服务端
     */
    public void startServer(String returnMsg) {

        // 添加连接监听
        server.addConnectListener(socketIOClient -> log.info("server 服务端启动成功"));
        // 添加断开连接监听
        server.addDisconnectListener(socketIOClient -> log.info("server 服务端断开连接"));

        // 添加事件监听 监听join事件，返回joinSuccess事件
        server.addEventListener("join", String.class,
                (socketIOClient, str, ackRequest) -> {
                    log.info("收到客户端加入消息：" + str);
                    server.getBroadcastOperations().sendEvent("joinSuccess", returnMsg);
                });

        // 启动服务端
        server.start();
    }

    /**
     * 停止服务端
     */
    public void stopServer() {
        server.stop();
    }
}
