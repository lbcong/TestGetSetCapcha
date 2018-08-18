/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ConstantVariable;

import Entity.SshInfo;
import Service.SSHService;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

public class VariableSession {

    public static boolean flag_status_is_first_run_proxy = true;
    public static boolean flag_status_is_first_run_app = true;
    public static boolean flag_status_IP = true; // 1 active ,0 deactive
}
