/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Service;

import Utils.JSchSession;
import Utils.Worker;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import Entity.SshInfo;
import ConstantVariable.Constant;
import com.jcraft.jsch.Session;
import java.util.ArrayList;

public class ProxyWithSSH {

    GetTextFromGit getTextFromGit = new GetTextFromGit();
    SshInfo sshInfo = new SshInfo();
    SSHService sSHService = new SSHService();
    private String bindAddress = "127.0.0.1";
    private int socksPort = 1080;
    public static String user = "";
    public static String host = "";
    private int sshPort = 22;
    public static String passwd = "";
    private Session session;
    public ServerSocket ss = null;
    public static boolean is_proxy_running = true;
    public static String status_proxy = "";
    private String keyOfProcessUsing = "";
    public static List<SshInfo> listInfo = new ArrayList<>(); // list ssh se duoc dung de fake ip
    public static int index_ssh = 0; // index cua ssh dang su dung 

    // cau hinh listen proxy
    public void setting(String url) {
        InetAddress baddress = null;
        try {
            // get list proxy
            listInfo = sSHService.getSSHs2(url);
            changeInfo();
            baddress = InetAddress.getByName(bindAddress);
            ss = new ServerSocket(socksPort, 0, baddress);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

    }

    //tao ket noi den ssh
    synchronized public void start() {
        status_proxy = Constant.Creating;
        while (true) {
            session = JSchSession.Connect(user, host, sshPort, passwd);
            if (session == null) {
                changeInfo();
            } else {
                status_proxy = Constant.Actived;
                keyOfProcessUsing = "";
                is_proxy_running = true;
                Thread startThread = new Thread() {
                    @Override
                    public void run() {
                        try {
                            createAndListenSocket();
                        } catch (Exception e) {
                            e.getMessage();
                        }
                    }
                };
                startThread.start();
                break;
            }
        }
    }

    // kiem tra xem ssh do con live khong
    synchronized public boolean checkSshlive() {
        return session.isConnected();
    }

    // tao cac socket de foward data sang ssh
    public void createAndListenSocket() {
        while (is_proxy_running) {
            if (checkSshlive()) {
                Socket socketOfServer = null;
                try {
                    socketOfServer = ss.accept();
//                    System.out.println("open new socket : " + " port: " + socketOfServer.getPort());
                    new Worker(socketOfServer, session).run();
                } catch (Exception e) {
                    if (ss == null) {
                        is_proxy_running = false;
                    }
                    System.out.println("loi :" + e.getMessage());
                }
            } else {
                status_proxy = Constant.Creating;
                changeIp();
            }

        }
    }

    public void stop() {
        try {
            if (ss != null) {
                ss.close();
                status_proxy = Constant.Close;
                is_proxy_running = false;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    // thay doi ip cua ssh
    synchronized public void changeIp() {
        changeInfo();
        start();
    }

    // thay doi ip cua ssh
    synchronized public void disconnect() {
        status_proxy = Constant.Creating;
        session.disconnect();
    }

    // thay doi thong tin cua ssh
    synchronized public void changeInfo() {
        try {
            SshInfo info = new SshInfo();
            if (listInfo.size() == 1) {
                info = listInfo.get(0);
            } else {
                if ((index_ssh + 1) >= (listInfo.size())) {
                    index_ssh = 0;
                } else {
                    index_ssh++;
                }
                info = listInfo.get(index_ssh);
            }
            this.user = info.getUser();
            this.passwd = info.getPass();
            this.host = info.getHost();
        } catch (Exception ex) {
            System.out.println("loi changeip" + ex.getMessage());
        }
    }

    public void checkConnect() throws InterruptedException {
        while (session == null) {
            Thread.sleep(500);
        }
        if (!checkSshlive()) {
            changeIp();
        }
    }

    public String getUser() {
        return user;
    }

    public String getHost() {
        return host;
    }

    public String getPasswd() {
        return passwd;
    }

    public String getKeyOfProcessUsing() {
        return keyOfProcessUsing;
    }

    public void setKeyOfProcessUsing(String keyOfProcessUsing) {
        this.keyOfProcessUsing = keyOfProcessUsing;
    }

    public Session getSession() {
        return session;
    }

}
