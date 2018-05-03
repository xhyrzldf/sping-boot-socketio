package com.matrix.simplesocketio.controller;

import com.matrix.simplesocketio.socket.SocketClient;
import com.matrix.simplesocketio.socket.SocketServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * simple-socket Io.
 *
 * @author : M@tr!x [xhyrzldf@gmail.com]
 */
@RestController
public class SocketController {

    private SocketServer socketServer = SocketServer.getInstance();

    private final SocketClient socketClient;

    @Autowired
    public SocketController(SocketClient socketClient) {
        this.socketClient = socketClient;
    }


    /**
     * 开启客户端，发送 'join' 事件 ，监听 'join success'事件
     */
    @GetMapping("/client/start")
    public void startClient() {
        socketClient.startClient("Hello Server!");
    }

    /**
     * 开启服务器
     * 监听 'join' 事件，返回 'join success' 事件
     */
    @GetMapping("server/start")
    public void startServer() {
        socketServer.startServer("Hello Client!");
    }

    /**
     * 关闭socket服务器
     */
    @GetMapping("server/stop")
    public void stopServer() {
        socketServer.stopServer();
    }
}
