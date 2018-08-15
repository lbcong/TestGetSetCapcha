/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Service;

import Utils.JSchSession;
import Utils.Worker;
import com.jcraft.jsch.Session;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProxyWithSSH extends Thread {

    @Autowired
    GetTextFromGit getTextFromGit;

    String bindAddress = "0.0.0.0";
    int socksPort = 1080;
    String user = "admin";
    String host = "206.214.13.39";
    int sshPort = 22;
    String passwd = "admin";
    JSchSession session = new JSchSession();
    ServerSocket ss = null;
    boolean running = true;

    @Override
    public void run() {
//        List<String> lists = null;
//        try {
//            lists = getTextFromGit.getStringFromGithubRaw("https://raw.githubusercontent.com/lbcong/SaveFileTemp/master/ConfigFileConnect");
//        } catch (Exception ex) {
//            System.out.println("gettext:" + ex.getMessage());
//        }
        InetAddress baddress = null;
        int clientNumber = 0;
        session = session.Connect(user, host, sshPort, passwd);
        try {
            baddress = InetAddress.getByName(bindAddress);
            ss = new ServerSocket(socksPort, 0, baddress);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            System.exit(1);
        }
        while (running) {
            Socket socketOfServer = null;
            try {
                socketOfServer = ss.accept();
                System.out.println("open new socket : " + " port: " + socketOfServer.getPort());
                new Worker(socketOfServer, clientNumber++, session).run();
            } catch (Exception e) {
                System.out.println("loi" + e.getMessage());
            }
        }
    }
}
