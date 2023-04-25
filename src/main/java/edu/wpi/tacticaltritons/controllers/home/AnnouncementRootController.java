package edu.wpi.tacticaltritons.controllers.home;

import edu.wpi.tacticaltritons.App;
import edu.wpi.tacticaltritons.database.Announcements;
import edu.wpi.tacticaltritons.database.DAOFacade;
import edu.wpi.tacticaltritons.navigation.Screen;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Text;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AnnouncementRootController {
    @FXML private BorderPane announcementCol0;
    @FXML private BorderPane announcementCol1;
    @FXML private BorderPane announcementCol2;
    @FXML private BorderPane announcementCol3;
    @FXML private BorderPane announcementCol4;

    @FXML
    private void initialize() throws IOException, SQLException {
        List<Announcements> announcementsList = DAOFacade.getAllAnnouncements(Timestamp.valueOf(LocalDateTime.now()));
        int i = 0;
        if(announcementsList != null) {
            Map<Announcements, Map<String, String>> announcementMap = new HashMap<>();
            announcementsList.forEach(announcement -> {
                Map<String, String> fields = new HashMap<>();
                fields.put("title", announcement.getTitle());
                fields.put("creator", announcement.getCreator());
                fields.put("effectiveDate", DateTimeFormatter.ofPattern("MM/dd/yyyy").format(announcement.getEffectiveDate().toLocalDateTime()));
                fields.put("type", announcement.getType());
                fields.put("separator", "-");
                announcementMap.put(announcement, fields);
            });


            for(i = 0; i < 5 && i < announcementMap.size(); i++){
                FXMLLoader loader = new FXMLLoader(App.class.getResource(Screen.ANNOUNCEMENT.getFilename()));
                FlowPane content = loader.load();
                recursiveAnnouncementSetter(content, announcementMap.get(announcementMap.keySet().stream().toList().get(i)));
                if(i == 0) announcementCol0.setCenter(content);
                else if(i == 1) announcementCol1.setCenter(content);
                else if(i == 2) announcementCol2.setCenter(content);
                else if(i == 3) announcementCol3.setCenter(content);
                else announcementCol4.setCenter(content);
            }
        }
        for(int j = i; j < 5; j++){
            FXMLLoader loader = new FXMLLoader(App.class.getResource(Screen.ANNOUNCEMENT.getFilename()));
            FlowPane content = loader.load();
            emptyAnnouncement(content);
            if(j == 0) announcementCol0.setCenter(content);
            else if(j == 1) announcementCol1.setCenter(content);
            else if(j == 2) announcementCol2.setCenter(content);
            else if(j == 3) announcementCol3.setCenter(content);
            else announcementCol4.setCenter(content);
        }
        if(announcementsList == null || announcementsList.size() == 0){
            Map<String, String> info = new HashMap<>();
            info.put("title", "No Announcements");
            info.put("creator", "");
            info.put("effectiveDate", "");
            info.put("type", "");

            System.out.println("hi");
            FXMLLoader loader = new FXMLLoader(App.class.getResource(Screen.ANNOUNCEMENT.getFilename()));
            FlowPane content = loader.load();
            recursiveAnnouncementSetter(content, info);
            announcementCol2.setCenter(content);
        }
    }

    private void recursiveAnnouncementSetter(FlowPane announcement, Map<String, String> info){
        for(Node node : announcement.getChildren()){
            if(node.getClass().equals(FlowPane.class)){
                recursiveAnnouncementSetter((FlowPane) node, info);
            }
            else if(node.getClass().equals(Text.class) && node.getId() != null){
                switch (node.getId()){
                    case "title" -> ((Text) node).setText(info.get("title"));
                    case "creator" -> ((Text) node).setText(info.get("creator"));
                    case "effectiveDate" -> ((Text) node).setText(info.get("effectiveDate"));
                    case "type" -> ((Text) node).setText(info.get("type"));
                    case "separator" -> ((Text) node).setText(info.get("separator"));
                }
            }
        }
    }
    private void emptyAnnouncement(FlowPane announcement){
        Map<String, String> emptyMap = new HashMap<>();
        emptyMap.put("title", "");
        emptyMap.put("creator", "");
        emptyMap.put("effectiveDate", "");
        emptyMap.put("type", "");
        emptyMap.put("separator", "");
        recursiveAnnouncementSetter(announcement, emptyMap);
    }
}
