/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Service;

import Entity.SshInfo;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SSHService {

    @Autowired
    GetTextFromGit getTextFromGit;

    public List<SshInfo> getSSHs(String rawUrlFile) {
        List<String> listsGithub = null;
        List<SshInfo> listsSshInfo = new ArrayList<>();
        try {
            listsGithub = getTextFromGit.getStringFromGithubRaw(rawUrlFile);
            if (listsGithub != null) {
                for (int i = 0; i < listsGithub.size(); i++) {
                    if (!listsGithub.get(i).equals("") && !listsGithub.get(i).equals(" ")) {
                        SshInfo info = new SshInfo();
                        String[] chuoi_tach = listsGithub.get(i).split("\\|");
                        if (chuoi_tach.length >= 3) {
                            info.setUser(chuoi_tach[1]);
                            info.setPass(chuoi_tach[2]);
                            info.setHost(chuoi_tach[0]);
//                            if (chuoi_tach.length >= 4) {
//                                info.setPort(Integer.parseInt(chuoi_tach[3]));
//                            }
                            listsSshInfo.add(info);
                        }
                    }
                }
            }
            return listsSshInfo;
        } catch (Exception ex) {
            System.out.println("gettext:" + ex.getMessage());
        }
        return null;
    }
}
