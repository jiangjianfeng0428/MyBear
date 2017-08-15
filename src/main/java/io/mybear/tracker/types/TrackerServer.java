package io.mybear.tracker.types;

import io.mybear.common.constants.CommonConstant;

/**
 * Created by 258662572@qq.com on 2017/7/27.
 */
public class TrackerServer {
    public int port;

    public byte[] ipAddr = new byte[CommonConstant.INET6_ADDRSTRLEN];

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public byte[] getIpAddr() {
        return ipAddr;
    }
}
