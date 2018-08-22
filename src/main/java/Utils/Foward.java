/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utils;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelDirectTCPIP;
import com.jcraft.jsch.Session;
import java.io.InputStream;
import java.io.OutputStream;

public class Foward implements Runnable {

    OutputStream out;
    InputStream in;
    String host;
    int port;
    Session session;

    public Foward(String host, int port, OutputStream out, InputStream in, Session session) {
        this.port = port;
        this.host = host;
        this.in = in;
        this.out = out;
        this.session = session;
    }

    @Override
    public void run() {
        try {
            // tao chanel den remote host nhan va tra du lieu thong qua chanel nay
            session.openChannel("direct-tcpip");
            Channel channel = session.openChannel("direct-tcpip");
            if (channel instanceof ChannelDirectTCPIP) {
                ChannelDirectTCPIP localChannelDirectTCPIP = (ChannelDirectTCPIP) channel;
                //day input len ssh
                localChannelDirectTCPIP.setInputStream(in);
                // get out tra ve tu ssh
                localChannelDirectTCPIP.setOutputStream(out);
                localChannelDirectTCPIP.setHost(host);
                localChannelDirectTCPIP.setPort(port);
//                System.out.println("open chanel to foward " + "host: " + host + " port:" + port);
                localChannelDirectTCPIP.connect();
            }
        } catch (Exception e) {
            e.getMessage();
        }
    }

}
