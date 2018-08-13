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
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@RestController
public class TestDowloadController {

    public static boolean isFirst = true;
    public static WebDriver webDriver = null;
    @Autowired
    DowloadService dowloadService;
    @Autowired
    CreateWebdriver createWebdriver;
    @Autowired
    Codenvy codenvy;

    @RequestMapping(value = "/getCap",
            method = RequestMethod.GET,
            produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody
    byte[] selenium(HttpServletResponse response) throws IOException {
        String output = "";
        try {
            if (isFirst) {
                webDriver = createWebdriver.getGoogle(Constant.binaryGoogleWindows);
                isFirst = false;
            }
            codenvy.Start(webDriver);
//            webDriver.navigate().to("http://tokuda.herokuapp.com/login/");
//            return dowloadService.dowloadFile(webDriver);

        } catch (Exception e) {
            e.getMessage();
        }
        return null;
    }

    

}
