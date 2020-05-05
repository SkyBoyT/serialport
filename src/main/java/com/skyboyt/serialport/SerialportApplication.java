package com.skyboyt.serialport;

import com.skyboyt.serialport.ui.MainFrame;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.awt.*;

@SpringBootApplication
public class SerialportApplication {

    public static void main(String[] args) {
        //启动JFrame窗口
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new MainFrame().setVisible(true);
            }
        });
        SpringApplication.run(SerialportApplication.class, args);
    }

}
