
package com.example.DBTest;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class main {
    private JFrame myFrame;
    private JPanel mainPanel;
    private JComboBox<String> comboBox;
    private List<Media> mediaList;
    private JTable table;
    private DefaultTableModel tableModel;
    private JButton addToWeeklyButton;
    private JFrame frame2;
    private JButton showWeeklyButton;

    private String[] typeList;

   /* public static void main(String[] args) {
  new main().init();
    } */
    private  void init() {

        typeList = new String[] {"ALL","music", "movie", "game", "tvshow", "bake", "cook" , "book"};

         mediaList  = new ArrayList<>();
        String fileName = "resources/firstSheet.csv";
       // String fileName = "firstSheet.csv"; for jar build
        Path pathToFile = Paths.get(fileName);

        try (BufferedReader br = Files.newBufferedReader(pathToFile,
                StandardCharsets.US_ASCII)) {

            String line = br.readLine();

            int i = 0;
            while (line != null ) {

                 String[] attributes = line.split(",");

                if (!attributes[0].equals("type")) {
                  //  Media media = createMedia(attributes);
                  //  mediaList.add(media);
                    i++;

                }


                line = br.readLine();
            }

        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
       // printMediaList();
        initGUI();
    }
    private void initGUI() {
        myFrame = new JFrame();
        mainPanel = new JPanel();
        comboBox = new JComboBox(typeList);
        tableModel = new DefaultTableModel(new String[]{"type", "name", "link", "date"}, 0);
        addToWeeklyButton = new JButton("add2 to weekly");
        showWeeklyButton = new JButton("show weekly");
        frame2 = new JFrame();
        JTextArea textArea = new JTextArea(readWeeklyFile());
        frame2.add(textArea);
        frame2.setSize(800, 800);

        addToWeeklyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            addToWeeklyFile();


            }
        });
        showWeeklyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame2.setVisible(true);
            }
        });



        comboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String selectedItem = (String)comboBox.getSelectedItem();
                filterByType(selectedItem);

            }
        });



        table = new JTable();
        table.setModel(tableModel);

        for(int i = 0; i < mediaList.size(); i++) {
                tableModel.addRow(mediaList.get(i).getStringArray());
        }


        JScrollPane scrollPane = new JScrollPane(table);
        table.setFillsViewportHeight(true);


        mainPanel.add(showWeeklyButton);
        mainPanel.add(addToWeeklyButton);
        mainPanel.add(scrollPane);
        mainPanel.add(comboBox);
        myFrame.setSize(800, 800);
        myFrame.add(mainPanel);
        myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        myFrame.setVisible(true);



    }

   /* private  Media createMedia(String[] metadata) {
        String type = metadata[0];
        String name = metadata[1];
        String link = metadata[2];
        Date date = null;
        try {
           date =new SimpleDateFormat("yyyy/MM/dd").parse(metadata[3]);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String creator = metadata[4];
        String subType = metadata[5];

        Media media = null;

        switch (type) {
            case "music":
                media = new Music(name, type, link, date, subType, creator);
                break;
            case "movie":
                media = new Movie(name, type, link, date);
                break;
            case "game":
                media = new Game(name, type, link, date);
                break;
            case "tvshow":
                media = new TVShow(name, type, link, date);
                break;
            case "bake":
                media = new Bake(name, type, link, date);
                break;
            case "cook":
                media = new Cook(name, type, link, date);
                break;
            default:
                media = new Media(name, type, link, "a", true, 3);
                break;
        }
        return media;
    } */


    private void printMediaList() {

        for (Media media: mediaList) {
            System.out.println(media);


        }
    }
    private String[] stringMediaList() {
        String[] stringList = new String[mediaList.size()];

        for (int i = 0; i < mediaList.size(); i++) {
            stringList[i] = mediaList.get(i).toString();
        }
        return stringList;
    }
    private String[][] get2dArray() {
        String[][] strings = new String[mediaList.size()][];

        for (int row = 0; row < mediaList.size(); row++) {
            strings[row] = mediaList.get(row).getStringArray();

        }
        return strings;


    }
    private void filterByType(String type) {

        System.out.println(table.getColumnCount());

        tableModel.setRowCount(0);


        if(type.toLowerCase().equals("all")) {
            for(int i = 0; i < mediaList.size(); i++) {
                tableModel.addRow(mediaList.get(i).getStringArray());
            }
        } else {
        for(int i = 0; i < mediaList.size(); i++) {
            if(mediaList.get(i).getType().equals(type)) {
                tableModel.addRow(mediaList.get(i).getStringArray());
            }
        }
        }
    }
    private void addToWeeklyFile() {
        int selectedRow = table.getSelectedRow();


            if(selectedRow != -1) {


            try {
                FileWriter myWriter = new FileWriter("src\\weeklyFile.txt", true);


                for(int i = 0; i < table.getColumnCount(); i++) {
                    myWriter.write(table.getValueAt(selectedRow, i).toString() + "  ");

                }
                myWriter.write(System.lineSeparator());

                myWriter.close();

            } catch (IOException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }
            }

    }
    private String readWeeklyFile() {

        String outputString = "";

        String fileName = "resources/weeklyFile.txt";
       // String fileName = "weeklyFile.txt"; //for jar build
        Path pathToFile = Paths.get(fileName);
        try (BufferedReader br = Files.newBufferedReader(pathToFile,
                StandardCharsets.US_ASCII)) {

            String line = br.readLine();


            int i = 0;
            while (line != null ) {
                outputString += line;
                outputString += System.lineSeparator();
                line = br.readLine();

            }

        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        return outputString;
    }



}
//TODO test testhello
