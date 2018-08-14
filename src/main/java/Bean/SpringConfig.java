/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Bean;

import Service.CheckCapcha;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import Service.Codenvy;
import Service.CreateWebdriver;
import Service.DowloadService;
import Service.GetTextFromGit;
import Service.PathDriver;
import Utils.Utils;
import java.awt.AWTException;
import java.awt.Robot;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;

@Configuration
@ComponentScans({
    @ComponentScan(basePackages = "Bean")
    ,@ComponentScan(basePackages = "Service")
    ,@ComponentScan(basePackages = "Utils")
})
public class SpringConfig {
    
    @Bean
    public Robot robot() throws AWTException{
        return new Robot();
    }
    
    @Bean
    public GetTextFromGit getTextFromGit() {
        return new GetTextFromGit();
    }


    @Bean
    public CheckCapcha checkCapcha() {
        return new CheckCapcha();
    }
    
    @Bean
    public DowloadService dowloadService() {
        return new DowloadService();
    }
    
    @Bean
    public Codenvy codenvy() {
        return new Codenvy();
    }

    @Bean
    public Utils utils() {
        return new Utils();
    }

    @Bean
    public CreateWebdriver createWebdriver() {
        return new CreateWebdriver();
    }

    @Bean
    public PathDriver pathDriver() {
        return new PathDriver();
    }

    @Bean
    public SystemConfig systemConfig() {
        return new SystemConfig();
    }
}
