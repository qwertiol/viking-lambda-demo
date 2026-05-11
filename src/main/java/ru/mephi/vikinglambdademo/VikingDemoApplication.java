package ru.mephi.vikinglambdademo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import ru.mephi.vikinglambdademo.gui.VikingDesktopFrame;
import ru.mephi.vikinglambdademo.controller.VikingListener;
import ru.mephi.vikinglambdademo.service.VikingService;
import ru.mephi.vikinglambdademo.service.VikingLambdaService;

import javax.swing.SwingUtilities;

@SpringBootApplication
public class VikingDemoApplication {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(VikingDemoApplication.class);
        app.setHeadless(false);

        ConfigurableApplicationContext context = app.run(args);

        VikingService vikingService = context.getBean(VikingService.class);
        VikingListener vikingListener = context.getBean(VikingListener.class);
        VikingLambdaService lambdaService = context.getBean(VikingLambdaService.class);  // добавлено

        SwingUtilities.invokeLater(() -> {
            VikingDesktopFrame frame = new VikingDesktopFrame(vikingService, lambdaService);  // теперь 2 сервиса передаем
            vikingListener.setGui(frame);
            frame.setVisible(true);
        });
    }
}