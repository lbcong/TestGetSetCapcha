package restcontroller;

import ConstantVariable.Constant;
import Service.Codenvy;
import Service.CreateWebdriver;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import org.springframework.web.bind.annotation.RestController;
import Service.DowloadService;
import Service.ProxyWithSSH;
import org.springframework.http.MediaType;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@EnableScheduling
@Controller
public class TaskController {

    @Autowired
    ProxyWithSSH proxyWithSSH;
    @Autowired
    Codenvy codenvy;

    @RequestMapping(value = "/sendTextCapcha", method = RequestMethod.GET)
    public String sendTextCapcha() {
        return "formSendCapcha";
    }

    @Autowired
    private SimpMessagingTemplate template;

    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public String greeting(String string) throws Exception {
        Thread.sleep(3000); // simulated delay
        return string;
    }

    @Scheduled(fixedRate = 2000)
    public void greeting() throws InterruptedException {
        this.template.convertAndSend("/topic/greetings", "trang thai proxy: " + proxyWithSSH.status_proxy + " ip: " + proxyWithSSH.getHost() + " user " + proxyWithSSH.getUser());
    }

    @Scheduled(fixedRate = 3000)
    public void looperAuto() throws InterruptedException {
        String str = "";
        for (String item : codenvy.getListAccountCreated()) {
            str = str + item;
        }
        this.template.convertAndSend("/auto/greetings", "tong acc can tao" + codenvy.getNumber_acc_must_create() + " tong acc da tao: " + codenvy.getNumber_acc_created() + " tong acc tao fail " + codenvy.getNumber_acc_fail()+str);
    }

    // lien tuc kiem tra se ket noi den ssh co con ket noi ko
//    @Scheduled(fixedRate = 10000)
    public void tracerProxy() throws InterruptedException {
        if ((proxyWithSSH.getKeyOfProcessUsing().equals("") || proxyWithSSH.getKeyOfProcessUsing().equals(Constant.Key_Tracer))
                && (proxyWithSSH.ss != null)) {
            proxyWithSSH.setKeyOfProcessUsing(Constant.Key_Tracer);
            int time_limit = 0;
            int change_limit = 0;
            while (!Constant.Actived.equals(proxyWithSSH.status_proxy)) {
                if (time_limit == Constant.Time_Limit_Waiting_Change_Ssh) {
                    if (change_limit < proxyWithSSH.listInfo.size()) {
                        proxyWithSSH.changeIp();
                        change_limit++;
                        time_limit = 0;
                    } else {
                        proxyWithSSH.setKeyOfProcessUsing("");
                        System.out.println("Ket noi mang loi hay kiem tra lai ssh hoac proxy");
                        System.exit(0);
                        break;
                    }
                } else {
                    time_limit++;
                }
            }
            proxyWithSSH.setKeyOfProcessUsing("");
        }

    }
}
