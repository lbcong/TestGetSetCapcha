/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utils;

import com.jcraft.jsch.Session;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

/**
 *
 * @author Alex
 */
public class Worker implements Runnable {

    private int clientNumber;
    private Socket socketOfServer;
    Session session;
    OutputStream out;
    InputStream in;
    byte[] buff = new byte[1024];

    public Worker(Socket socketOfServer,Session session) {
        this.clientNumber = clientNumber;
        this.socketOfServer = socketOfServer;
        this.session = session;
    }

    @Override
    public void run() {
        try {
            socketOfServer.setTcpNoDelay(true);
            out = socketOfServer.getOutputStream();
            in = socketOfServer.getInputStream();
            int bytematch = 0;
            bytematch = in.read();
            if (bytematch == 4) {
                byte cd = read();
                int port = readShort();
                String host = readIPV4Address();
                byte cd1 = read();
                new Foward(host, port, out, in, session).run();
                buff[0] = 0;
                buff[1] = 90;
                out.write(buff, 0, 8);
                out.flush();
            }
        } catch (Exception ex) {
            System.out.println("loi 1 :" + ex.getMessage());
        }

    }

    public void read(byte[] buf, int s, int l) throws IOException {
        int _s = s;
        int _l = l;
        while (_l > 0) {
            int i = in.read(buf, _s, _l);
            switch (i) {
                default:
                    _s += i;
                    _l -= i;
                    break;
                case -1:
                    throw new IOException();
            }
        }
    }

    public byte read() throws IOException {
        int i = in.read();
        switch (i) {
            case -1:
                throw new IOException();
            default:
                return (byte) (i & 0xFF);
        }

    }

    public String readIPV4Address() throws IOException {
        read(buff, 0, 4);
        byte[] tmp = new byte[4];
        System.arraycopy(buff, 0, tmp, 0, 4);
        return InetAddress.getByAddress(tmp).getHostAddress();
    }

    public int readShort() throws IOException {
        read(buff, 0, 2);
        return buff[0] << 8 & 0xFF00 | buff[1] & 0xFF;
    }

}
