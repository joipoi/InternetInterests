package com.example.DBTest;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.swing.*;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.List;
import java.util.Map;

@SpringBootApplication
public class mainRun implements CommandLineRunner {

    @Autowired
	private JdbcTemplate jdbcTemplate;
    public static boolean hasDB;

	public static void main(String[] args) {
        SpringApplicationBuilder builder = new SpringApplicationBuilder(mainRun.class);
        builder.headless(false);
        ConfigurableApplicationContext context = builder.run(args);
	}

    @Override
    public void run(String... args)  {
      /*  String sql = "INSERT INTO `recipe_app`.`ingredients_at_home` (`name`, `type`, `amount`, `dateBought`, `expirationDate`, `price`) VALUES (?, ?, ?, ?, ?, ?);";
        int result = jdbcTemplate.update(sql, "a", "b", "a", "2022-06-05", "2022-06-05", "3");

        if(result > 0) {
            System.out.println("new row inserted");
        } */

      String sql = "SELECT * FROM media";
        List<Map<String, Object>> list =  jdbcTemplate.queryForList(sql);

        Runtime rt = Runtime.getRuntime();

        JFrame jFrame = new JFrame("a");
        jFrame.setLayout(null);
        JLabel label = new JLabel("Do you have a Database? if you dont know what that means press no");
        label.setBounds(0,0, 500, 100);
        jFrame.getContentPane().add(label);
        JButton yesBtn = new JButton("yes");
        yesBtn.setBounds(10,100, 200, 100);
        JLabel label2 = new JLabel("exit this window to exit program");
        label2.setBounds(0,0, 500, 800);
        jFrame.getContentPane().add(yesBtn);
        jFrame.getContentPane().add(label2);

        yesBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                hasDB = true;
                try {
                    rt.exec("rundll32 url.dll,FileProtocolHandler " + "http://localhost:8080/db");
                } catch (IOException error) {
                    error.printStackTrace();
                }
            }
        });

        JButton noBtn = new JButton("no");
        noBtn.setBounds(300,100, 200, 100);
        jFrame.getContentPane().add(noBtn);
        noBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                hasDB = false;
                try {
                    rt.exec("rundll32 url.dll,FileProtocolHandler " + "http://localhost:8080/frontpage");
                } catch (IOException error) {
                    error.printStackTrace();
                }
            }
        });


        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jFrame.setSize(600,500);
        jFrame.setVisible(true);




        // String sql = "INSERT INTO `recipe_app`.`ingredients_at_home` (`name`, `type`, `amount`, `dateBought`, `expirationDate`, `price`) VALUES ('a', 'b', 'a', '2022-06-05', '2022-06-05', '3');";
    }
}
