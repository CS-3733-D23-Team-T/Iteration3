package edu.wpi.tacticaltritons.controllers;

import edu.wpi.tacticaltritons.App;
import edu.wpi.tacticaltritons.database.DAOFacade;
import edu.wpi.tacticaltritons.database.LocationName;
import edu.wpi.tacticaltritons.database.Move;
import edu.wpi.tacticaltritons.database.Node;
import edu.wpi.tacticaltritons.pathfinding.AlgorithmSingleton;
import edu.wpi.tacticaltritons.robot.RobotComm;
import io.github.palexdev.materialfx.controls.*;
import javafx.animation.*;
import javafx.beans.property.*;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

public class ThreeDMapController {
    @FXML private AnchorPane parent;

    @FXML
    private void initialize() throws SQLException {
        Image image = new Image(Objects.requireNonNull(App.class.getResource("images/clean_map/map_firstfloor_clean.png")).toString());

        WritableImage writer = new WritableImage(image.getPixelReader(), (int) image.getWidth(), (int) image.getHeight());
        PixelWriter pixelWriter = writer.getPixelWriter();
        PixelReader pixelReader = writer.getPixelReader();


        int[][] matrix = new int[(int) writer.getHeight()][(int) writer.getWidth()];
        for (int i = 0; i < writer.getHeight(); i++) {
            for (int j = 0; j < writer.getWidth(); j++) {
                Color c = pixelReader.getColor(j, i);
                double red = c.getRed() * 255;
                double green = c.getRed() * 255;
                double blue = c.getBlue() * 255;

                if(red < 100 && green < 100 && blue < 100){
                    matrix[i][j] = 0;
                }
                else if (red < 230 && green < 230 && blue < 230) {
                    matrix[i][j] = 1;
                }
                else {
                    pixelWriter.setColor(j, i, Color.WHITE);
                    matrix[i][j] = 0;
                }
            }
        }

        ImageView iv = new ImageView(writer);
        iv.getTransforms().add(new Rotate(90, Rotate.X_AXIS));

        Rotate yRotate = new Rotate(0, Rotate.Y_AXIS);
        Rotate xRotate = new Rotate(90, Rotate.X_AXIS);

        StringProperty floorNumber = new SimpleStringProperty("1");
        List<Node> nodes = new ArrayList<>(DAOFacade.getAllCurrentMoves(Date.valueOf(LocalDate.now())).parallelStream()
                .map(Move::getNode)
                .filter(node -> node.getFloor().equals(floorNumber.get())).toList());

        // Create and position camera
        DoubleProperty cameraX = new SimpleDoubleProperty(computeMidPoint(nodes.parallelStream().map(Node::getXcoord).toList()));
        double std = computeSTD(nodes.parallelStream().map(node -> new Point2D(node.getXcoord(), node.getYcoord())).toList());
        DoubleProperty cameraY = new SimpleDoubleProperty(3 * std);
        DoubleProperty cameraZ = new SimpleDoubleProperty(computeMidPoint(nodes.parallelStream().map(Node::getYcoord).toList()));
        PerspectiveCamera camera = new PerspectiveCamera(true);
        camera.getTransforms().addAll(
                yRotate,
                xRotate);
        camera.translateXProperty().bindBidirectional(cameraX);
        camera.translateYProperty().bindBidirectional(cameraY);
        camera.translateZProperty().bindBidirectional(cameraZ);
        camera.setNearClip(1);
        camera.setFarClip(12000);

        // Build the Scene Graph
        Group root = new Group();
        VBox sidePane = new VBox();

        root.getChildren().add(camera);

        Map<Node, LocationName> locations = new HashMap<>();
        List<Move> moves = new ArrayList<>(DAOFacade.getAllCurrentMoves(Date.valueOf(LocalDate.now())));
        nodes.forEach(node -> locations.put(node, moves.parallelStream().filter(move -> move.getNode().getNodeID() == node.getNodeID()).map(Move::getLocation).toList().get(0)));
        Map<String, Color> nodeColors = new HashMap<>();
        nodeColors.put("BATH", Color.MEDIUMSLATEBLUE);
        nodeColors.put("CONF", Color.ORANGERED);
        nodeColors.put("DEPT", Color.GREEN);
        nodeColors.put("ELEV", Color.PURPLE);
        nodeColors.put("EXIT", Color.RED);
        nodeColors.put("HALL", Color.BLACK);
        nodeColors.put("INFO", Color.GOLDENROD);
        nodeColors.put("LABS", Color.MAGENTA);
        nodeColors.put("REST", Color.MEDIUMSLATEBLUE);
        nodeColors.put("RETL", Color.TAN);
        nodeColors.put("SERV", Color.SALMON);
        nodeColors.put("STAI", Color.SILVER);
        AtomicReference<List<Node>> walkingPath = new AtomicReference<>(new ArrayList<>());
        List<Node> pathToCompute = new ArrayList<>();
        List<Box> visibleNodes = new ArrayList<>();

        BooleanProperty pathFinding = new SimpleBooleanProperty(false);

        List<AtomicBoolean> permVisibleTexts = new ArrayList<>();

        MFXButton newPathButton = new MFXButton("New Path");
        newPathButton.disableProperty().bind(pathFinding.not());
        newPathButton.setOnAction(event -> {
            root.getChildren().removeIf(i -> i.getId() != null && i.getId().equals("pathBlock"));
            root.getChildren().removeIf(i -> i.getId() != null && i.getId().contains("bNode"));
            root.getChildren().forEach(node -> {
                if(node.getId() != null && node.getId().contains("node")){
                    node.setVisible(false);
                }
            });
            root.getChildren().forEach(node -> {
                if(node.getId() != null && node.getId().contains("node") && node instanceof VBox) {
                    ((VBox) node).getChildren().removeIf(node1 -> node1.getId() != null && node1.getId().contains("dNode"));
                    ((VBox) node).getChildren().forEach(node1 -> {
                        if(node1 instanceof Text && node1.getId() != null && node1.getId().contains("nodeText")){
                            String id = node1.getId().substring(node1.getId().lastIndexOf('t') + 1);
                            ((Text) node1).setFont(new Font(24));
                            node.setTranslateX(nodes.parallelStream().filter(n -> n.getNodeID() == Integer.parseInt(id)).toList().get(0).getXcoord() - ((node1.getLayoutBounds().getWidth() + 10) / 2));
                        }
                    });
                    node.setVisible(false);
                }
            });
            visibleNodes.forEach(box -> {
                int id = Integer.parseInt(box.getId().substring(box.getId().lastIndexOf("e") + 1));
                Node node = nodes.parallelStream().filter(n -> n.getNodeID() == id).toList().get(0);
                box.setMaterial(new PhongMaterial(nodeColors.get(locations.get(node).getNodeType())));
            });
            permVisibleTexts.forEach(bool -> bool.set(false));
            permVisibleTexts.clear();
            walkingPath.set(null);
            root.getChildren().addAll(visibleNodes);
        });
        sidePane.getChildren().add(newPathButton);

        HBox searchBox = new HBox();
        searchBox.setAlignment(Pos.CENTER);
        MFXFilterComboBox<String> nodeSearchBox = new MFXFilterComboBox<>();
        nodeSearchBox.setPromptText("Find a Location");
        nodeSearchBox.getItems().addAll(locations.values().parallelStream().filter(locationName -> !locationName.getNodeType().equals("HALL")).map(LocationName::getLongName).toList());
        nodeSearchBox.setPrefWidth(200);
        nodeSearchBox.getSelectionModel().selectedItemProperty().addListener((obs, o, n) -> {
            locations.forEach((k, v) -> {
                if(v.getLongName().equals(n)){
                    cameraZ.set(k.getYcoord() + 500);
                    cameraX.set(k.getXcoord());
                    cameraY.set(500);
                    yRotate.setAngle(0);
                    xRotate.setAngle(135);

                    root.getChildren().parallelStream().forEach(node -> {
                        if(node.getId() != null && node.getId().contains("node" + k.getNodeID())){
                            node.setVisible(true);
                        }
                    });
                }
            });
            nodeSearchBox.getSelectionModel().clearSelection();
        });
        nodeSearchBox.setDisable(true);
        nodeSearchBox.setOnHidden(event -> nodeSearchBox.setDisable(true));

        MFXButton searchButton = new MFXButton("Search");
        searchButton.setOnAction(event -> {
            if(nodeSearchBox.isDisable()){
                nodeSearchBox.setDisable(false);
                nodeSearchBox.requestFocus();
                nodeSearchBox.show();
            }
            else{
                if(nodeSearchBox.getSelectedItem() != null){
                    locations.forEach((k, v) -> {
                        if(v.getLongName().equals(nodeSearchBox.getSelectedItem())){
                            cameraZ.set(k.getYcoord() + 500);
                            cameraX.set(k.getXcoord());
                            cameraY.set(500);
                            yRotate.setAngle(0);
                            xRotate.setAngle(135);

                            root.getChildren().parallelStream().forEach(node -> {
                                if(node.getId() != null && node.getId().contains("nodeText" + k.getNodeID())){
                                    node.setVisible(true);
                                }
                            });
                        }
                    });
                    nodeSearchBox.getSelectionModel().clearSelection();
                }
            }
        });
        searchBox.getChildren().add(nodeSearchBox);
        searchBox.getChildren().add(searchButton);
        sidePane.getChildren().add(0, searchBox);

        // Use a SubScene
        SubScene subScene = new SubScene(
                root, 1400, 800,
                true,
                SceneAntialiasing.BALANCED);
        subScene.setFill(Color.BLACK);
        subScene.setCamera(camera);
        Group group = new Group();
        group.getChildren().add(subScene);

        BooleanProperty movement = new SimpleBooleanProperty(false);
        //mouse drag event
        List<Point2D> points = new ArrayList<>();
        subScene.setOnMouseDragged(event -> {
            if(!movement.get()) {
                if (points.size() == 0) {
                    //initial point
                    points.add(new Point2D(event.getSceneX(), event.getSceneY()));
                }
                else {
                    //second point
                    Point2D secondPoint = new Point2D(event.getSceneX(), event.getSceneY());

                    //formula
                    double hyp = points.get(0).distance(secondPoint);
                    double opp = Math.abs(secondPoint.getY() - points.get(0).getY());
                    double adj = Math.abs(secondPoint.getX() - points.get(0).getX());

                    double magnitude = (hyp * 2.5) / Math.sqrt(subScene.getWidth() * subScene.getHeight());

                    double xMultiplier = secondPoint.getY() < points.get(0).getY() ? 1 : -1;
                    double xAngle = magnitude * xMultiplier * Math.atan(opp / adj) * (180 / Math.PI);

                    double yMultiplier = secondPoint.getX() < points.get(0).getX() ? -1 : 1;
                    double yAngle = magnitude * yMultiplier * Math.atan(adj / opp) * (180 / Math.PI);

                    //updating the transform
                    camera.getTransforms().stream().filter(node -> (node instanceof Rotate)).forEach(node -> {
                        if (((Rotate) node).getAxis().equals(Rotate.X_AXIS) && !Double.isNaN(xAngle)) {
                            if (((Rotate) node).getAngle() >= 90 && ((Rotate) node).getAngle() <= 170) {
                                if ((((Rotate) node).getAngle() + xAngle >= 90) && (((Rotate) node).getAngle() + xAngle <= 170)) {
                                    if (Math.abs(xAngle) < 30) {
                                        ((Rotate) node).setAngle((((Rotate) node).getAngle() + xAngle) % 360);
                                    }
                                }
                            }
                        }
                        else if (((Rotate) node).getAxis().equals(Rotate.Y_AXIS) && !Double.isNaN(yAngle)) {
                            if (Math.abs(yAngle) < 30) {
                                ((Rotate) node).setAngle((((Rotate) node).getAngle() + yAngle) % 360);
                            }
                        }
                    });

                    points.clear();
                }
            }
            else{
                if (points.size() == 0) {
                    //initial point
                    points.add(new Point2D(event.getSceneX(), event.getSceneY()));
                }
                else {
                    //second point
                    Point2D secondPoint = new Point2D(event.getSceneX(), event.getSceneY());

                    //formula
                    double opp = secondPoint.getY() - points.get(0).getY();
                    double adj = secondPoint.getX() - points.get(0).getX();

                    double magnitude = (subScene.getWidth() + subScene.getHeight()) / Math.sqrt(subScene.getWidth() * subScene.getHeight());

                    if(Math.abs(opp) < 50 && Math.abs(adj) < 50) {
                        cameraZ.set(cameraZ.get() - opp * magnitude);
                        cameraX.set(cameraX.get() - adj * magnitude);
                    }

                    points.clear();
                }
            }
        });

        root.getChildren().add(iv);

        MFXToggleButton pathFindingToggle = new MFXToggleButton("Path Finding");
        pathFindingToggle.setStyle("-fx-text-fill: blue");
        pathFindingToggle.selectedProperty().bindBidirectional(pathFinding);
        pathFindingToggle.selectedProperty().addListener((obs, o, n) -> {
            permVisibleTexts.forEach(bool -> bool.set(false));
            permVisibleTexts.clear();
            root.getChildren().removeIf(i -> i.getId() != null && i.getId().equals("dNode"));
            root.getChildren().forEach(node -> {
                if(node.getId() != null && node.getId().contains("node") && node instanceof VBox) {
                    ((VBox) node).getChildren().removeIf(node1 -> node1.getId() != null && node1.getId().contains("dNode"));
                    ((VBox) node).getChildren().forEach(node1 -> {
                        if(node1 instanceof Text && node1.getId() != null && node1.getId().contains("nodeText")){
                            String id = node1.getId().substring(node1.getId().lastIndexOf('t') + 1);
                            ((Text) node1).setFont(new Font(24));
                            node.setTranslateX(nodes.parallelStream().filter(n1 -> n1.getNodeID() == Integer.parseInt(id)).toList().get(0).getXcoord() - ((node1.getLayoutBounds().getWidth() + 10) / 2));
                        }
                    });
                }
            });
            root.getChildren().removeIf(i -> i.getId() != null && i.getId().equals("pathBlock"));
            root.getChildren().removeIf(i -> i.getId() != null && i.getId().contains("bNode"));
            visibleNodes.forEach(box -> {
                int id = Integer.parseInt(box.getId().substring(box.getId().lastIndexOf("e") + 1));
                Node node = nodes.parallelStream().filter(n1 -> n1.getNodeID() == id).toList().get(0);
                box.setMaterial(new PhongMaterial(nodeColors.get(locations.get(node).getNodeType())));
            });
            walkingPath.set(null);
            root.getChildren().addAll(visibleNodes);
        });
        sidePane.getChildren().add(1, pathFindingToggle);

        MFXToggleButton wallsToggle = new MFXToggleButton("Walls");
        wallsToggle.setStyle("-fx-text-fill: blue");
        wallsToggle.selectedProperty().addListener((obs, o, n) -> {
            if(n){
//                for (int i = 0; i < matrix.length; i++) {
//                    for (int j = 0; j < matrix[i].length; j++) {
//                        if (matrix[i][j] == 1) {
//                            if (i< matrix.length-1 && j<matrix[i].length-1) {
//                                if (matrix[i + 1][j] == 0 || matrix[i + 1][j + 1] == 0 || matrix[i][j + 1] == 0) {
//                                    Box b = new Box(1, 30, 1);
//                                    b.setTranslateZ(i);
//                                    b.setTranslateX(j);
//                                    b.setMaterial(new PhongMaterial(Color.web("#A17A4B")));
//                                    b.setId("wall");
//                                    root.getChildren().add(b);
//                                }
//                            }
//                            if(i>1 && j>1){
//                                if(matrix[i - 1][j] == 0 || matrix[i - 1][j - 1] == 0 || matrix[i][j - 1] == 0){
//                                    Box b = new Box(1, 30, 1);
//                                    b.setTranslateZ(i);
//                                    b.setTranslateX(j);
//                                    b.setMaterial(new PhongMaterial(Color.web("#A17A4B")));
//                                    b.setId("wall");
//                                    root.getChildren().add(b);
//                                }
//                            }
//                        }
//                    }
//                }
                for (int i = 0; i < matrix.length; i++) {
                    for (int j = 0; j < matrix[i].length; j++) {
                        if (matrix[i][j] == 1) {
                            int wallWidth = 0;
                            for (int k = j; k < matrix[i].length; k++) {
                                if (matrix[i][k] == 1) {
                                    wallWidth++;
                                } else {
                                    break;
                                }
                            }
                            if (wallWidth >= 2) {
                                Box b = new Box(wallWidth, 30, 1);
                                b.setTranslateZ(i);
                                b.setTranslateX(j + wallWidth/2.0);
                                b.setMaterial(new PhongMaterial(Color.web("#A17A4B")));
                                b.setId("wall");
                                root.getChildren().add(b);
                                j += wallWidth;
                            }
                        }
                    }
                }

            }
            else{
                root.getChildren().removeIf(node -> node.getId() != null && node.getId().equals("wall"));
            }
        });
        sidePane.getChildren().add(wallsToggle);

        MFXToggleButton movementToggle = new MFXToggleButton("Toggle Movement");
        movementToggle.setStyle("-fx-text-fill: blue");
        movementToggle.selectedProperty().bindBidirectional(movement);
        sidePane.getChildren().add(1, movementToggle);

        AtomicReference<List<String>> transitionFloor = new AtomicReference<>(null);
        AtomicReference<List<List<Node>>> paths = new AtomicReference<>(null);
        AtomicInteger currentPathIndex = new AtomicInteger(0);

        HBox floorToggles = new HBox();
        ToggleGroup floor = new ToggleGroup();
        MFXRadioButton lower2 = new MFXRadioButton("L2");
        floor.getToggles().add(lower2);
        lower2.selectedProperty().addListener((obs, o, n) -> {
            if(n){
                root.getChildren().removeIf(i -> i.getId() != null && i.getId().equals("pathBlock"));
                root.getChildren().removeIf(i -> i.getId() != null && (i.getId().contains("Node") || i.getId().contains("node")));

                Image img = new Image(Objects.requireNonNull(App.class.getResource("images/clean_map/map_lowerlevel_2_clean.png")).toString());
                WritableImage wri = new WritableImage(img.getPixelReader(), (int) img.getWidth(), (int) img.getHeight());
                PixelWriter pixWri = writer.getPixelWriter();
                PixelReader pixRea = writer.getPixelReader();

                for (int i = 0; i < wri.getHeight(); i++) {
                    for (int j = 0; j < wri.getWidth(); j++) {
                        Color c = pixelReader.getColor(j, i);
                        double red = c.getRed() * 255;
                        double green = c.getRed() * 255;
                        double blue = c.getBlue() * 255;

                        if(red < 100 && green < 100 && blue < 100){
                            matrix[i][j] = 0;
                        }
                        else if (red < 230 && green < 230 && blue < 230) {
                            matrix[i][j] = 1;
                        }
                        else {
                            pixelWriter.setColor(j, i, Color.WHITE);
                            matrix[i][j] = 0;
                        }
                    }
                }

                iv.setImage(wri);

                floorNumber.set("L2");
                nodes.clear();
                try {
                    nodes.addAll(DAOFacade.getAllCurrentMoves(Date.valueOf(LocalDate.now())).parallelStream()
                            .map(Move::getNode)
                            .filter(node -> node.getFloor().equals(String.valueOf(floorNumber.get()))).toList());
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                cameraX.set(computeMidPoint(nodes.parallelStream().map(Node::getXcoord).toList()));
                cameraY.set(3 * computeSTD(nodes.parallelStream().map(node -> new Point2D(node.getXcoord(), node.getYcoord())).toList()));
                cameraZ.set(computeMidPoint(nodes.parallelStream().map(Node::getYcoord).toList()));

                locations.clear();
                moves.clear();
                try {
                    moves.addAll(DAOFacade.getAllCurrentMoves(Date.valueOf(LocalDate.now())));
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                nodes.forEach(node -> locations.put(node, moves.parallelStream().filter(move -> move.getNode().getNodeID() == node.getNodeID()).map(Move::getLocation).toList().get(0)));
                walkingPath.set(null);
                pathToCompute.clear();
                visibleNodes.clear();
                pathFinding.set(false);

                permVisibleTexts.clear();
                nodeSearchBox.getItems().clear();
                nodeSearchBox.getItems().addAll(locations.values().parallelStream().filter(i -> i.getNodeType().equals("HALL")).map(LocationName::getLongName).toList());

                nodes.forEach(node -> {
                    if(!locations.get(node).getNodeType().equals("HALL")) {
                        VBox box = new VBox();
                        box.setId("node" + node.getNodeID());
                        box.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-border-color: black; -fx-border-radius: 10; -fx-border-width: 3");
                        box.setSpacing(10);
                        box.setAlignment(Pos.CENTER);
                        box.setPrefSize(VBox.USE_COMPUTED_SIZE, VBox.USE_COMPUTED_SIZE);
                        box.setPickOnBounds(false);
                        box.setMouseTransparent(true);

                        Text t = new Text(locations.get(node).getShortName());
                        t.setId("nodeText" + node.getNodeID());
                        t.setFont(new Font(24));
                        box.getChildren().add(t);

                        Box s = new Box(10, 10, 10);
                        AtomicBoolean permVisible = new AtomicBoolean(false);
                        s.setOnMouseClicked(event -> {
                            //pathfinding
                            if (pathFinding.get()) {
                                permVisibleTexts.add(permVisible);
                                permVisible.set(true);
                                box.setVisible(true);
                                //starting point
                                s.setMaterial(new PhongMaterial(Color.BLACK));
                                if (pathToCompute.size() == 0) {
                                    pathToCompute.add(node);
                                }
                                //ending point
                                else {
                                    Node start = pathToCompute.get(0);

                                    //removing old paths
                                    root.getChildren().removeIf(i -> i.getId() != null && i.getId().equals("pathBlock"));
                                    root.getChildren().removeIf(i -> i.getId() != null && i.getId().contains("bNode")
                                            && !i.getId().contains(String.valueOf(start.getNodeID()))
                                            && !i.getId().contains(String.valueOf(node.getNodeID())));


                                    try {
                                        //Pathfinding
                                        List<Node> path = AlgorithmSingleton.getInstance().algorithm.findShortestPath(start, node);

                                        paths.set(new ArrayList<>());
                                        List<Node> tempPath = new ArrayList<>();
                                        String curFloor = path.get(0).getFloor();
                                        transitionFloor.set(new ArrayList<>(List.of(curFloor)));
                                        for(int i = 0; i < path.size() - 1; i++){
                                            tempPath.add(path.get(i));
                                            if(!curFloor.equals(path.get(i + 1).getFloor())){
                                                curFloor = path.get(i + 1).getFloor();
                                                transitionFloor.get().add(curFloor);
                                                paths.get().add(tempPath);
                                                tempPath.clear();
                                            }
                                        }
                                        tempPath.add(path.get(path.size() - 1));
                                        paths.get().add(tempPath);

                                        currentPathIndex.set(0);
                                        walkingPath.set(paths.get().get(0));
                                        path.clear();
                                        path.addAll(paths.get().get(0));

                                        for (int i = 0; i < path.size(); i++) {

                                            //formula
                                            double distance = Math.sqrt(Math.pow(path.get(i + 1).getXcoord() - path.get(i).getXcoord(), 2) +
                                                    Math.pow(path.get(i + 1).getYcoord() - path.get(i).getYcoord(), 2));

                                            double opp = path.get(i + 1).getXcoord() - path.get(i).getXcoord();
                                            double adj = path.get(i + 1).getYcoord() - path.get(i).getYcoord();
                                            double angle = Math.atan(opp / adj) * (180 / Math.PI);

                                            Box line = new Box(5, 5, distance);
                                            line.setId("pathBlock");
                                            line.setTranslateZ(path.get(i).getYcoord() + adj / 2);
                                            line.setTranslateX(path.get(i).getXcoord() + opp / 2);
                                            line.getTransforms().add(new Rotate(angle, Rotate.Y_AXIS));
                                            line.setMaterial(new PhongMaterial(Color.BLUE));

                                            root.getChildren().add(line);
                                        }

                                    } catch (SQLException e) {
                                        throw new RuntimeException(e);
                                    }
                                    pathToCompute.clear();
                                }
                            }
                            else {
                                if(!permVisible.get()) {
                                    permVisibleTexts.add(permVisible);
                                    permVisible.set(true);
                                    box.setVisible(true);

                                    t.setFont(new Font(20));

                                    Text lt = new Text(locations.get(node).getLongName());
                                    lt.setId("dNode" + node.getNodeID());
                                    lt.setFont(new Font(24));

                                    Text nXY = new Text("x: " + node.getXcoord() + ", y: " + node.getYcoord());
                                    nXY.setFont(new Font(20));
                                    nXY.setId("dNode" + node.getNodeID());

                                    Text lty = new Text("Type: " + locations.get(node).getNodeType());
                                    lty.setId("dNode" + node.getNodeID());
                                    lty.setFont(new Font(20));

                                    double maxWidth = (computeMax(t.getLayoutBounds().getWidth(),
                                            lt.getLayoutBounds().getWidth(),
                                            nXY.getLayoutBounds().getWidth(),
                                            lty.getLayoutBounds().getWidth()) + 10) / 2;

                                    double maxHeight = computeSum(t.getLayoutBounds().getHeight(),
                                            lt.getLayoutBounds().getHeight(),
                                            nXY.getLayoutBounds().getHeight(),
                                            lty.getLayoutBounds().getHeight());

                                    box.setTranslateX(node.getXcoord() - maxWidth);
                                    box.setTranslateY(100 + maxHeight - box.getHeight());
                                    box.getChildren().add(0, lt);
                                    box.getChildren().addAll(nXY, lty);
                                }
                                else {
                                    box.getChildren().removeIf(i -> i.getId() != null && i.getId().equals("dNode" + node.getNodeID()));
                                    permVisible.set(false);
                                    permVisibleTexts.remove(permVisible);
                                    t.setFont(new Font(24));
                                    box.setTranslateX(node.getXcoord() - ((t.getLayoutBounds().getWidth() + 10) / 2));
                                    box.setVisible(false);
                                    box.setTranslateY(100);
                                }
                            }
                        });
                        s.setOnMouseEntered(event -> {
                            if(!permVisible.get()) {
                                box.setVisible(true);
                            }
                        });
                        s.setOnMouseExited(event -> {
                            if(!permVisible.get()) {
                                box.setVisible(false);
                            }
                        });
                        s.setId("bNode" + node.getNodeID());
                        s.setMaterial(new PhongMaterial(nodeColors.get(locations.get(node).getNodeType())));
                        s.setTranslateZ(node.getYcoord());
                        s.setTranslateX(node.getXcoord());
                        visibleNodes.add(s);

                        //drawing the floating text
                        box.setTranslateZ(node.getYcoord());
                        box.setTranslateX(node.getXcoord() - ((t.getLayoutBounds().getWidth() + 10) / 2));
                        box.setTranslateY(100);

                        box.getTransforms().addAll(
                                new Rotate(180, Rotate.Z_AXIS),
                                new Rotate(180, Rotate.Y_AXIS));
                        box.setVisible(false);
                        box.layoutYProperty().bind(xRotate.pivotXProperty());
                        box.setRotationAxis(Rotate.Y_AXIS);
                        box.rotateProperty().bind(yRotate.angleProperty());
                        box.getTransforms().addAll(xRotate, new Rotate(180, Rotate.X_AXIS));

                        root.getChildren().addAll(s, box);
                    }
                });
            }
        });
        floorToggles.getChildren().add(lower2);

        MFXRadioButton lower1 = new MFXRadioButton("L1");
        floor.getToggles().add(lower1);
        lower1.selectedProperty().addListener((obs, o, n) -> {
            if(n){
                root.getChildren().removeIf(i -> i.getId() != null && i.getId().equals("pathBlock"));
                root.getChildren().removeIf(i -> i.getId() != null && (i.getId().contains("Node") || i.getId().contains("node")));

                Image img = new Image(Objects.requireNonNull(App.class.getResource("images/clean_map/map_lowerlevel_1_clean.png")).toString());
                WritableImage wri = new WritableImage(img.getPixelReader(), (int) img.getWidth(), (int) img.getHeight());
                PixelWriter pixWri = writer.getPixelWriter();
                PixelReader pixRea = writer.getPixelReader();

                for (int i = 0; i < wri.getHeight(); i++) {
                    for (int j = 0; j < wri.getWidth(); j++) {
                        Color c = pixelReader.getColor(j, i);
                        double red = c.getRed() * 255;
                        double green = c.getRed() * 255;
                        double blue = c.getBlue() * 255;

                        if(red < 100 && green < 100 && blue < 100){
                            matrix[i][j] = 0;
                        }
                        else if (red < 230 && green < 230 && blue < 230) {
                            matrix[i][j] = 1;
                        }
                        else {
                            pixelWriter.setColor(j, i, Color.WHITE);
                            matrix[i][j] = 0;
                        }
                    }
                }

                iv.setImage(wri);

                floorNumber.set("L1");
                nodes.clear();
                try {
                    nodes.addAll(DAOFacade.getAllCurrentMoves(Date.valueOf(LocalDate.now())).parallelStream()
                            .map(Move::getNode)
                            .filter(node -> node.getFloor().equals(String.valueOf(floorNumber.get()))).toList());
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                cameraX.set(computeMidPoint(nodes.parallelStream().map(Node::getXcoord).toList()));
                cameraY.set(3 * computeSTD(nodes.parallelStream().map(node -> new Point2D(node.getXcoord(), node.getYcoord())).toList()));
                cameraZ.set(computeMidPoint(nodes.parallelStream().map(Node::getYcoord).toList()));

                locations.clear();
                moves.clear();
                try {
                    moves.addAll(DAOFacade.getAllCurrentMoves(Date.valueOf(LocalDate.now())));
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                nodes.forEach(node -> locations.put(node, moves.parallelStream().filter(move -> move.getNode().getNodeID() == node.getNodeID()).map(Move::getLocation).toList().get(0)));
                walkingPath.set(null);
                pathToCompute.clear();
                visibleNodes.clear();
                pathFinding.set(false);

                permVisibleTexts.clear();
                nodeSearchBox.getItems().clear();
                nodeSearchBox.getItems().addAll(locations.values().parallelStream().filter(i -> i.getNodeType().equals("HALL")).map(LocationName::getLongName).toList());

                nodes.forEach(node -> {
                    if(!locations.get(node).getNodeType().equals("HALL")) {
                        VBox box = new VBox();
                        box.setId("node" + node.getNodeID());
                        box.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-border-color: black; -fx-border-radius: 10; -fx-border-width: 3");
                        box.setSpacing(10);
                        box.setAlignment(Pos.CENTER);
                        box.setPrefSize(VBox.USE_COMPUTED_SIZE, VBox.USE_COMPUTED_SIZE);
                        box.setPickOnBounds(false);
                        box.setMouseTransparent(true);

                        Text t = new Text(locations.get(node).getShortName());
                        t.setId("nodeText" + node.getNodeID());
                        t.setFont(new Font(24));
                        box.getChildren().add(t);

                        Box s = new Box(10, 10, 10);
                        AtomicBoolean permVisible = new AtomicBoolean(false);
                        s.setOnMouseClicked(event -> {
                            //pathfinding
                            if (pathFinding.get()) {
                                permVisibleTexts.add(permVisible);
                                permVisible.set(true);
                                box.setVisible(true);
                                //starting point
                                s.setMaterial(new PhongMaterial(Color.BLACK));
                                if (pathToCompute.size() == 0) {
                                    pathToCompute.add(node);
                                }
                                //ending point
                                else {
                                    Node start = pathToCompute.get(0);

                                    //removing old paths
                                    root.getChildren().removeIf(i -> i.getId() != null && i.getId().equals("pathBlock"));
                                    root.getChildren().removeIf(i -> i.getId() != null && i.getId().contains("bNode")
                                            && !i.getId().contains(String.valueOf(start.getNodeID()))
                                            && !i.getId().contains(String.valueOf(node.getNodeID())));


                                    try {
                                        //Pathfinding
                                        List<Node> path = AlgorithmSingleton.getInstance().algorithm.findShortestPath(start, node);

                                        paths.set(new ArrayList<>());
                                        List<Node> tempPath = new ArrayList<>();
                                        String curFloor = path.get(0).getFloor();
                                        transitionFloor.set(new ArrayList<>(List.of(curFloor)));
                                        for(int i = 0; i < path.size() - 1; i++){
                                            tempPath.add(path.get(i));
                                            if(!curFloor.equals(path.get(i + 1).getFloor())){
                                                curFloor = path.get(i + 1).getFloor();
                                                transitionFloor.get().add(curFloor);
                                                paths.get().add(tempPath);
                                                tempPath.clear();
                                            }
                                        }
                                        tempPath.add(path.get(path.size() - 1));
                                        paths.get().add(tempPath);

                                        currentPathIndex.set(0);
                                        walkingPath.set(paths.get().get(0));
                                        path.clear();
                                        path.addAll(paths.get().get(0));

                                        for (int i = 0; i < path.size(); i++) {

                                            //formula
                                            double distance = Math.sqrt(Math.pow(path.get(i + 1).getXcoord() - path.get(i).getXcoord(), 2) +
                                                    Math.pow(path.get(i + 1).getYcoord() - path.get(i).getYcoord(), 2));

                                            double opp = path.get(i + 1).getXcoord() - path.get(i).getXcoord();
                                            double adj = path.get(i + 1).getYcoord() - path.get(i).getYcoord();
                                            double angle = Math.atan(opp / adj) * (180 / Math.PI);

                                            Box line = new Box(5, 5, distance);
                                            line.setId("pathBlock");
                                            line.setTranslateZ(path.get(i).getYcoord() + adj / 2);
                                            line.setTranslateX(path.get(i).getXcoord() + opp / 2);
                                            line.getTransforms().add(new Rotate(angle, Rotate.Y_AXIS));
                                            line.setMaterial(new PhongMaterial(Color.BLUE));

                                            root.getChildren().add(line);
                                        }

                                    } catch (SQLException e) {
                                        throw new RuntimeException(e);
                                    }
                                    pathToCompute.clear();
                                }
                            }
                            else {
                                if(!permVisible.get()) {
                                    permVisibleTexts.add(permVisible);
                                    permVisible.set(true);
                                    box.setVisible(true);

                                    t.setFont(new Font(20));

                                    Text lt = new Text(locations.get(node).getLongName());
                                    lt.setId("dNode" + node.getNodeID());
                                    lt.setFont(new Font(24));

                                    Text nXY = new Text("x: " + node.getXcoord() + ", y: " + node.getYcoord());
                                    nXY.setFont(new Font(20));
                                    nXY.setId("dNode" + node.getNodeID());

                                    Text lty = new Text("Type: " + locations.get(node).getNodeType());
                                    lty.setId("dNode" + node.getNodeID());
                                    lty.setFont(new Font(20));

                                    double maxWidth = (computeMax(t.getLayoutBounds().getWidth(),
                                            lt.getLayoutBounds().getWidth(),
                                            nXY.getLayoutBounds().getWidth(),
                                            lty.getLayoutBounds().getWidth()) + 10) / 2;

                                    double maxHeight = computeSum(t.getLayoutBounds().getHeight(),
                                            lt.getLayoutBounds().getHeight(),
                                            nXY.getLayoutBounds().getHeight(),
                                            lty.getLayoutBounds().getHeight());

                                    box.setTranslateX(node.getXcoord() - maxWidth);
                                    box.setTranslateY(100 + maxHeight - box.getHeight());
                                    box.getChildren().add(0, lt);
                                    box.getChildren().addAll(nXY, lty);
                                }
                                else {
                                    box.getChildren().removeIf(i -> i.getId() != null && i.getId().equals("dNode" + node.getNodeID()));
                                    permVisible.set(false);
                                    permVisibleTexts.remove(permVisible);
                                    t.setFont(new Font(24));
                                    box.setTranslateX(node.getXcoord() - ((t.getLayoutBounds().getWidth() + 10) / 2));
                                    box.setVisible(false);
                                    box.setTranslateY(100);
                                }
                            }
                        });
                        s.setOnMouseEntered(event -> {
                            if(!permVisible.get()) {
                                box.setVisible(true);
                            }
                        });
                        s.setOnMouseExited(event -> {
                            if(!permVisible.get()) {
                                box.setVisible(false);
                            }
                        });
                        s.setId("bNode" + node.getNodeID());
                        s.setMaterial(new PhongMaterial(nodeColors.get(locations.get(node).getNodeType())));
                        s.setTranslateZ(node.getYcoord());
                        s.setTranslateX(node.getXcoord());
                        visibleNodes.add(s);

                        //drawing the floating text
                        box.setTranslateZ(node.getYcoord());
                        box.setTranslateX(node.getXcoord() - ((t.getLayoutBounds().getWidth() + 10) / 2));
                        box.setTranslateY(100);

                        box.getTransforms().addAll(
                                new Rotate(180, Rotate.Z_AXIS),
                                new Rotate(180, Rotate.Y_AXIS));
                        box.setVisible(false);
                        box.layoutYProperty().bind(xRotate.pivotXProperty());
                        box.setRotationAxis(Rotate.Y_AXIS);
                        box.rotateProperty().bind(yRotate.angleProperty());
                        box.getTransforms().addAll(xRotate, new Rotate(180, Rotate.X_AXIS));

                        root.getChildren().addAll(s, box);
                    }
                });
            }
        });
        floorToggles.getChildren().add(lower1);

        MFXRadioButton floor1 = new MFXRadioButton("1");
        floor.getToggles().add(floor1);
        floor1.setSelected(true);
        floor1.selectedProperty().addListener((obs, o, n) -> {
            if(n){
                root.getChildren().removeIf(i -> i.getId() != null && i.getId().equals("pathBlock"));
                root.getChildren().removeIf(i -> i.getId() != null && (i.getId().contains("Node") || i.getId().contains("node")));

                Image img = new Image(Objects.requireNonNull(App.class.getResource("images/clean_map/map_firstfloor_clean.png")).toString());
                WritableImage wri = new WritableImage(img.getPixelReader(), (int) img.getWidth(), (int) img.getHeight());
                PixelWriter pixWri = writer.getPixelWriter();
                PixelReader pixRea = writer.getPixelReader();

                for (int i = 0; i < wri.getHeight(); i++) {
                    for (int j = 0; j < wri.getWidth(); j++) {
                        Color c = pixelReader.getColor(j, i);
                        double red = c.getRed() * 255;
                        double green = c.getRed() * 255;
                        double blue = c.getBlue() * 255;

                        if(red < 100 && green < 100 && blue < 100){
                            matrix[i][j] = 0;
                        }
                        else if (red < 230 && green < 230 && blue < 230) {
                            matrix[i][j] = 1;
                        }
                        else {
                            pixelWriter.setColor(j, i, Color.WHITE);
                            matrix[i][j] = 0;
                        }
                    }
                }

                iv.setImage(wri);

                floorNumber.set("1");
                nodes.clear();
                try {
                    nodes.addAll(DAOFacade.getAllCurrentMoves(Date.valueOf(LocalDate.now())).parallelStream()
                            .map(Move::getNode)
                            .filter(node -> node.getFloor().equals(String.valueOf(floorNumber.get()))).toList());
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                cameraX.set(computeMidPoint(nodes.parallelStream().map(Node::getXcoord).toList()));
                cameraY.set(3 * computeSTD(nodes.parallelStream().map(node -> new Point2D(node.getXcoord(), node.getYcoord())).toList()));
                cameraZ.set(computeMidPoint(nodes.parallelStream().map(Node::getYcoord).toList()));

                locations.clear();
                moves.clear();
                try {
                    moves.addAll(DAOFacade.getAllCurrentMoves(Date.valueOf(LocalDate.now())));
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                nodes.forEach(node -> locations.put(node, moves.parallelStream().filter(move -> move.getNode().getNodeID() == node.getNodeID()).map(Move::getLocation).toList().get(0)));
                walkingPath.set(null);
                pathToCompute.clear();
                visibleNodes.clear();
                pathFinding.set(false);

                permVisibleTexts.clear();
                nodeSearchBox.getItems().clear();
                nodeSearchBox.getItems().addAll(locations.values().parallelStream().filter(i -> i.getNodeType().equals("HALL")).map(LocationName::getLongName).toList());

                nodes.forEach(node -> {
                    if(!locations.get(node).getNodeType().equals("HALL")) {
                        VBox box = new VBox();
                        box.setId("node" + node.getNodeID());
                        box.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-border-color: black; -fx-border-radius: 10; -fx-border-width: 3");
                        box.setSpacing(10);
                        box.setAlignment(Pos.CENTER);
                        box.setPrefSize(VBox.USE_COMPUTED_SIZE, VBox.USE_COMPUTED_SIZE);
                        box.setPickOnBounds(false);
                        box.setMouseTransparent(true);

                        Text t = new Text(locations.get(node).getShortName());
                        t.setId("nodeText" + node.getNodeID());
                        t.setFont(new Font(24));
                        box.getChildren().add(t);

                        Box s = new Box(10, 10, 10);
                        AtomicBoolean permVisible = new AtomicBoolean(false);
                        s.setOnMouseClicked(event -> {
                            //pathfinding
                            if (pathFinding.get()) {
                                permVisibleTexts.add(permVisible);
                                permVisible.set(true);
                                box.setVisible(true);
                                //starting point
                                s.setMaterial(new PhongMaterial(Color.BLACK));
                                if (pathToCompute.size() == 0) {
                                    pathToCompute.add(node);
                                }
                                //ending point
                                else {
                                    Node start = pathToCompute.get(0);

                                    //removing old paths
                                    root.getChildren().removeIf(i -> i.getId() != null && i.getId().equals("pathBlock"));
                                    root.getChildren().removeIf(i -> i.getId() != null && i.getId().contains("bNode")
                                            && !i.getId().contains(String.valueOf(start.getNodeID()))
                                            && !i.getId().contains(String.valueOf(node.getNodeID())));


                                    try {
                                        //Pathfinding
                                        List<Node> path = AlgorithmSingleton.getInstance().algorithm.findShortestPath(start, node);

                                        paths.set(new ArrayList<>());
                                        List<Node> tempPath = new ArrayList<>();
                                        String curFloor = path.get(0).getFloor();
                                        transitionFloor.set(new ArrayList<>(List.of(curFloor)));
                                        for(int i = 0; i < path.size() - 1; i++){
                                            tempPath.add(path.get(i));
                                            if(!curFloor.equals(path.get(i + 1).getFloor())){
                                                curFloor = path.get(i + 1).getFloor();
                                                transitionFloor.get().add(curFloor);
                                                paths.get().add(tempPath);
                                                tempPath.clear();
                                            }
                                        }
                                        tempPath.add(path.get(path.size() - 1));
                                        paths.get().add(tempPath);

                                        currentPathIndex.set(0);
                                        walkingPath.set(paths.get().get(0));
                                        path.clear();
                                        path.addAll(paths.get().get(0));

                                        for (int i = 0; i < path.size(); i++) {

                                            //formula
                                            double distance = Math.sqrt(Math.pow(path.get(i + 1).getXcoord() - path.get(i).getXcoord(), 2) +
                                                    Math.pow(path.get(i + 1).getYcoord() - path.get(i).getYcoord(), 2));

                                            double opp = path.get(i + 1).getXcoord() - path.get(i).getXcoord();
                                            double adj = path.get(i + 1).getYcoord() - path.get(i).getYcoord();
                                            double angle = Math.atan(opp / adj) * (180 / Math.PI);

                                            Box line = new Box(5, 5, distance);
                                            line.setId("pathBlock");
                                            line.setTranslateZ(path.get(i).getYcoord() + adj / 2);
                                            line.setTranslateX(path.get(i).getXcoord() + opp / 2);
                                            line.getTransforms().add(new Rotate(angle, Rotate.Y_AXIS));
                                            line.setMaterial(new PhongMaterial(Color.BLUE));

                                            root.getChildren().add(line);
                                        }

                                    } catch (SQLException e) {
                                        throw new RuntimeException(e);
                                    }
                                    pathToCompute.clear();
                                }
                            }
                            else {
                                if(!permVisible.get()) {
                                    permVisibleTexts.add(permVisible);
                                    permVisible.set(true);
                                    box.setVisible(true);

                                    t.setFont(new Font(20));

                                    Text lt = new Text(locations.get(node).getLongName());
                                    lt.setId("dNode" + node.getNodeID());
                                    lt.setFont(new Font(24));

                                    Text nXY = new Text("x: " + node.getXcoord() + ", y: " + node.getYcoord());
                                    nXY.setFont(new Font(20));
                                    nXY.setId("dNode" + node.getNodeID());

                                    Text lty = new Text("Type: " + locations.get(node).getNodeType());
                                    lty.setId("dNode" + node.getNodeID());
                                    lty.setFont(new Font(20));

                                    double maxWidth = (computeMax(t.getLayoutBounds().getWidth(),
                                            lt.getLayoutBounds().getWidth(),
                                            nXY.getLayoutBounds().getWidth(),
                                            lty.getLayoutBounds().getWidth()) + 10) / 2;

                                    double maxHeight = computeSum(t.getLayoutBounds().getHeight(),
                                            lt.getLayoutBounds().getHeight(),
                                            nXY.getLayoutBounds().getHeight(),
                                            lty.getLayoutBounds().getHeight());

                                    box.setTranslateX(node.getXcoord() - maxWidth);
                                    box.setTranslateY(100 + maxHeight - box.getHeight());
                                    box.getChildren().add(0, lt);
                                    box.getChildren().addAll(nXY, lty);
                                }
                                else {
                                    box.getChildren().removeIf(i -> i.getId() != null && i.getId().equals("dNode" + node.getNodeID()));
                                    permVisible.set(false);
                                    permVisibleTexts.remove(permVisible);
                                    t.setFont(new Font(24));
                                    box.setTranslateX(node.getXcoord() - ((t.getLayoutBounds().getWidth() + 10) / 2));
                                    box.setVisible(false);
                                    box.setTranslateY(100);
                                }
                            }
                        });
                        s.setOnMouseEntered(event -> {
                            if(!permVisible.get()) {
                                box.setVisible(true);
                            }
                        });
                        s.setOnMouseExited(event -> {
                            if(!permVisible.get()) {
                                box.setVisible(false);
                            }
                        });
                        s.setId("bNode" + node.getNodeID());
                        s.setMaterial(new PhongMaterial(nodeColors.get(locations.get(node).getNodeType())));
                        s.setTranslateZ(node.getYcoord());
                        s.setTranslateX(node.getXcoord());
                        visibleNodes.add(s);

                        //drawing the floating text
                        box.setTranslateZ(node.getYcoord());
                        box.setTranslateX(node.getXcoord() - ((t.getLayoutBounds().getWidth() + 10) / 2));
                        box.setTranslateY(100);

                        box.getTransforms().addAll(
                                new Rotate(180, Rotate.Z_AXIS),
                                new Rotate(180, Rotate.Y_AXIS));
                        box.setVisible(false);
                        box.layoutYProperty().bind(xRotate.pivotXProperty());
                        box.setRotationAxis(Rotate.Y_AXIS);
                        box.rotateProperty().bind(yRotate.angleProperty());
                        box.getTransforms().addAll(xRotate, new Rotate(180, Rotate.X_AXIS));

                        root.getChildren().addAll(s, box);
                    }
                });
            }
        });
        floorToggles.getChildren().add(floor1);

        MFXRadioButton floor2 = new MFXRadioButton("2");
        floor.getToggles().add(floor2);
        floor2.selectedProperty().addListener((obs, o, n) -> {
            if(n){
                root.getChildren().removeIf(i -> i.getId() != null && i.getId().equals("pathBlock"));
                root.getChildren().removeIf(i -> i.getId() != null && (i.getId().contains("Node") || i.getId().contains("node")));

                Image img = new Image(Objects.requireNonNull(App.class.getResource("images/clean_map/map_secondfloor_clean.png")).toString());
                WritableImage wri = new WritableImage(img.getPixelReader(), (int) img.getWidth(), (int) img.getHeight());
                PixelWriter pixWri = writer.getPixelWriter();
                PixelReader pixRea = writer.getPixelReader();

                for (int i = 0; i < wri.getHeight(); i++) {
                    for (int j = 0; j < wri.getWidth(); j++) {
                        Color c = pixelReader.getColor(j, i);
                        double red = c.getRed() * 255;
                        double green = c.getRed() * 255;
                        double blue = c.getBlue() * 255;

                        if(red < 100 && green < 100 && blue < 100){
                            matrix[i][j] = 0;
                        }
                        else if (red < 230 && green < 230 && blue < 230) {
                            matrix[i][j] = 1;
                        }
                        else {
                            pixelWriter.setColor(j, i, Color.WHITE);
                            matrix[i][j] = 0;
                        }
                    }
                }

                iv.setImage(wri);

                floorNumber.set("2");
                nodes.clear();
                try {
                    nodes.addAll(DAOFacade.getAllCurrentMoves(Date.valueOf(LocalDate.now())).parallelStream()
                            .map(Move::getNode)
                            .filter(node -> node.getFloor().equals(String.valueOf(floorNumber.get()))).toList());
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                cameraX.set(computeMidPoint(nodes.parallelStream().map(Node::getXcoord).toList()));
                cameraY.set(3 * computeSTD(nodes.parallelStream().map(node -> new Point2D(node.getXcoord(), node.getYcoord())).toList()));
                cameraZ.set(computeMidPoint(nodes.parallelStream().map(Node::getYcoord).toList()));

                locations.clear();
                moves.clear();
                try {
                    moves.addAll(DAOFacade.getAllCurrentMoves(Date.valueOf(LocalDate.now())));
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                nodes.forEach(node -> locations.put(node, moves.parallelStream().filter(move -> move.getNode().getNodeID() == node.getNodeID()).map(Move::getLocation).toList().get(0)));
                walkingPath.set(null);
                pathToCompute.clear();
                visibleNodes.clear();
                pathFinding.set(false);

                permVisibleTexts.clear();
                nodeSearchBox.getItems().clear();
                nodeSearchBox.getItems().addAll(locations.values().parallelStream().filter(i -> i.getNodeType().equals("HALL")).map(LocationName::getLongName).toList());

                nodes.forEach(node -> {
                    if(!locations.get(node).getNodeType().equals("HALL")) {
                        VBox box = new VBox();
                        box.setId("node" + node.getNodeID());
                        box.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-border-color: black; -fx-border-radius: 10; -fx-border-width: 3");
                        box.setSpacing(10);
                        box.setAlignment(Pos.CENTER);
                        box.setPrefSize(VBox.USE_COMPUTED_SIZE, VBox.USE_COMPUTED_SIZE);
                        box.setPickOnBounds(false);
                        box.setMouseTransparent(true);

                        Text t = new Text(locations.get(node).getShortName());
                        t.setId("nodeText" + node.getNodeID());
                        t.setFont(new Font(24));
                        box.getChildren().add(t);

                        Box s = new Box(10, 10, 10);
                        AtomicBoolean permVisible = new AtomicBoolean(false);
                        s.setOnMouseClicked(event -> {
                            //pathfinding
                            if (pathFinding.get()) {
                                permVisibleTexts.add(permVisible);
                                permVisible.set(true);
                                box.setVisible(true);
                                //starting point
                                s.setMaterial(new PhongMaterial(Color.BLACK));
                                if (pathToCompute.size() == 0) {
                                    pathToCompute.add(node);
                                }
                                //ending point
                                else {
                                    Node start = pathToCompute.get(0);

                                    //removing old paths
                                    root.getChildren().removeIf(i -> i.getId() != null && i.getId().equals("pathBlock"));
                                    root.getChildren().removeIf(i -> i.getId() != null && i.getId().contains("bNode")
                                            && !i.getId().contains(String.valueOf(start.getNodeID()))
                                            && !i.getId().contains(String.valueOf(node.getNodeID())));


                                    try {
                                        //Pathfinding
                                        List<Node> path = AlgorithmSingleton.getInstance().algorithm.findShortestPath(start, node);

                                        paths.set(new ArrayList<>());
                                        List<Node> tempPath = new ArrayList<>();
                                        String curFloor = path.get(0).getFloor();
                                        transitionFloor.set(new ArrayList<>(List.of(curFloor)));
                                        for(int i = 0; i < path.size() - 1; i++){
                                            tempPath.add(path.get(i));
                                            if(!curFloor.equals(path.get(i + 1).getFloor())){
                                                curFloor = path.get(i + 1).getFloor();
                                                transitionFloor.get().add(curFloor);
                                                paths.get().add(tempPath);
                                                tempPath.clear();
                                            }
                                        }
                                        tempPath.add(path.get(path.size() - 1));
                                        paths.get().add(tempPath);

                                        currentPathIndex.set(0);
                                        walkingPath.set(paths.get().get(0));
                                        path.clear();
                                        path.addAll(paths.get().get(0));

                                        for (int i = 0; i < path.size(); i++) {

                                            //formula
                                            double distance = Math.sqrt(Math.pow(path.get(i + 1).getXcoord() - path.get(i).getXcoord(), 2) +
                                                    Math.pow(path.get(i + 1).getYcoord() - path.get(i).getYcoord(), 2));

                                            double opp = path.get(i + 1).getXcoord() - path.get(i).getXcoord();
                                            double adj = path.get(i + 1).getYcoord() - path.get(i).getYcoord();
                                            double angle = Math.atan(opp / adj) * (180 / Math.PI);

                                            Box line = new Box(5, 5, distance);
                                            line.setId("pathBlock");
                                            line.setTranslateZ(path.get(i).getYcoord() + adj / 2);
                                            line.setTranslateX(path.get(i).getXcoord() + opp / 2);
                                            line.getTransforms().add(new Rotate(angle, Rotate.Y_AXIS));
                                            line.setMaterial(new PhongMaterial(Color.BLUE));

                                            root.getChildren().add(line);
                                        }

                                    } catch (SQLException e) {
                                        throw new RuntimeException(e);
                                    }
                                    pathToCompute.clear();
                                }
                            }
                            else {
                                if(!permVisible.get()) {
                                    permVisibleTexts.add(permVisible);
                                    permVisible.set(true);
                                    box.setVisible(true);

                                    t.setFont(new Font(20));

                                    Text lt = new Text(locations.get(node).getLongName());
                                    lt.setId("dNode" + node.getNodeID());
                                    lt.setFont(new Font(24));

                                    Text nXY = new Text("x: " + node.getXcoord() + ", y: " + node.getYcoord());
                                    nXY.setFont(new Font(20));
                                    nXY.setId("dNode" + node.getNodeID());

                                    Text lty = new Text("Type: " + locations.get(node).getNodeType());
                                    lty.setId("dNode" + node.getNodeID());
                                    lty.setFont(new Font(20));

                                    double maxWidth = (computeMax(t.getLayoutBounds().getWidth(),
                                            lt.getLayoutBounds().getWidth(),
                                            nXY.getLayoutBounds().getWidth(),
                                            lty.getLayoutBounds().getWidth()) + 10) / 2;

                                    double maxHeight = computeSum(t.getLayoutBounds().getHeight(),
                                            lt.getLayoutBounds().getHeight(),
                                            nXY.getLayoutBounds().getHeight(),
                                            lty.getLayoutBounds().getHeight());

                                    box.setTranslateX(node.getXcoord() - maxWidth);
                                    box.setTranslateY(100 + maxHeight - box.getHeight());
                                    box.getChildren().add(0, lt);
                                    box.getChildren().addAll(nXY, lty);
                                }
                                else {
                                    box.getChildren().removeIf(i -> i.getId() != null && i.getId().equals("dNode" + node.getNodeID()));
                                    permVisible.set(false);
                                    permVisibleTexts.remove(permVisible);
                                    t.setFont(new Font(24));
                                    box.setTranslateX(node.getXcoord() - ((t.getLayoutBounds().getWidth() + 10) / 2));
                                    box.setVisible(false);
                                    box.setTranslateY(100);
                                }
                            }
                        });
                        s.setOnMouseEntered(event -> {
                            if(!permVisible.get()) {
                                box.setVisible(true);
                            }
                        });
                        s.setOnMouseExited(event -> {
                            if(!permVisible.get()) {
                                box.setVisible(false);
                            }
                        });
                        s.setId("bNode" + node.getNodeID());
                        s.setMaterial(new PhongMaterial(nodeColors.get(locations.get(node).getNodeType())));
                        s.setTranslateZ(node.getYcoord());
                        s.setTranslateX(node.getXcoord());
                        visibleNodes.add(s);

                        //drawing the floating text
                        box.setTranslateZ(node.getYcoord());
                        box.setTranslateX(node.getXcoord() - ((t.getLayoutBounds().getWidth() + 10) / 2));
                        box.setTranslateY(100);

                        box.getTransforms().addAll(
                                new Rotate(180, Rotate.Z_AXIS),
                                new Rotate(180, Rotate.Y_AXIS));
                        box.setVisible(false);
                        box.layoutYProperty().bind(xRotate.pivotXProperty());
                        box.setRotationAxis(Rotate.Y_AXIS);
                        box.rotateProperty().bind(yRotate.angleProperty());
                        box.getTransforms().addAll(xRotate, new Rotate(180, Rotate.X_AXIS));

                        root.getChildren().addAll(s, box);
                    }
                });
            }
        });
        floorToggles.getChildren().add(floor2);

        MFXRadioButton floor3 = new MFXRadioButton("3");
        floor.getToggles().add(floor3);
        floor3.selectedProperty().addListener((obs, o, n) -> {
            if(n){
                root.getChildren().removeIf(i -> i.getId() != null && i.getId().equals("pathBlock"));
                root.getChildren().removeIf(i -> i.getId() != null && (i.getId().contains("Node") || i.getId().contains("node")));

                Image img = new Image(Objects.requireNonNull(App.class.getResource("images/clean_map/map_thirdfloor_clean.png")).toString());
                WritableImage wri = new WritableImage(img.getPixelReader(), (int) img.getWidth(), (int) img.getHeight());
                PixelWriter pixWri = writer.getPixelWriter();
                PixelReader pixRea = writer.getPixelReader();

                for (int i = 0; i < wri.getHeight(); i++) {
                    for (int j = 0; j < wri.getWidth(); j++) {
                        Color c = pixelReader.getColor(j, i);
                        double red = c.getRed() * 255;
                        double green = c.getRed() * 255;
                        double blue = c.getBlue() * 255;

                        if(red < 100 && green < 100 && blue < 100){
                            matrix[i][j] = 0;
                        }
                        else if (red < 230 && green < 230 && blue < 230) {
                            matrix[i][j] = 1;
                        }
                        else {
                            pixelWriter.setColor(j, i, Color.WHITE);
                            matrix[i][j] = 0;
                        }
                    }
                }

                iv.setImage(wri);

                floorNumber.set("3");
                nodes.clear();
                try {
                    nodes.addAll(DAOFacade.getAllCurrentMoves(Date.valueOf(LocalDate.now())).parallelStream()
                            .map(Move::getNode)
                            .filter(node -> node.getFloor().equals(String.valueOf(floorNumber.get()))).toList());
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                cameraX.set(computeMidPoint(nodes.parallelStream().map(Node::getXcoord).toList()));
                cameraY.set(3 * computeSTD(nodes.parallelStream().map(node -> new Point2D(node.getXcoord(), node.getYcoord())).toList()));
                cameraZ.set(computeMidPoint(nodes.parallelStream().map(Node::getYcoord).toList()));

                locations.clear();
                moves.clear();
                try {
                    moves.addAll(DAOFacade.getAllCurrentMoves(Date.valueOf(LocalDate.now())));
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                nodes.forEach(node -> locations.put(node, moves.parallelStream().filter(move -> move.getNode().getNodeID() == node.getNodeID()).map(Move::getLocation).toList().get(0)));
                walkingPath.set(null);
                pathToCompute.clear();
                visibleNodes.clear();
                pathFinding.set(false);

                permVisibleTexts.clear();
                nodeSearchBox.getItems().clear();
                nodeSearchBox.getItems().addAll(locations.values().parallelStream().filter(i -> i.getNodeType().equals("HALL")).map(LocationName::getLongName).toList());

                nodes.forEach(node -> {
                    if(!locations.get(node).getNodeType().equals("HALL")) {
                        VBox box = new VBox();
                        box.setId("node" + node.getNodeID());
                        box.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-border-color: black; -fx-border-radius: 10; -fx-border-width: 3");
                        box.setSpacing(10);
                        box.setAlignment(Pos.CENTER);
                        box.setPrefSize(VBox.USE_COMPUTED_SIZE, VBox.USE_COMPUTED_SIZE);
                        box.setPickOnBounds(false);
                        box.setMouseTransparent(true);

                        Text t = new Text(locations.get(node).getShortName());
                        t.setId("nodeText" + node.getNodeID());
                        t.setFont(new Font(24));
                        box.getChildren().add(t);

                        Box s = new Box(10, 10, 10);
                        AtomicBoolean permVisible = new AtomicBoolean(false);
                        s.setOnMouseClicked(event -> {
                            //pathfinding
                            if (pathFinding.get()) {
                                permVisibleTexts.add(permVisible);
                                permVisible.set(true);
                                box.setVisible(true);
                                //starting point
                                s.setMaterial(new PhongMaterial(Color.BLACK));
                                if (pathToCompute.size() == 0) {
                                    pathToCompute.add(node);
                                }
                                //ending point
                                else {
                                    Node start = pathToCompute.get(0);

                                    //removing old paths
                                    root.getChildren().removeIf(i -> i.getId() != null && i.getId().equals("pathBlock"));
                                    root.getChildren().removeIf(i -> i.getId() != null && i.getId().contains("bNode")
                                            && !i.getId().contains(String.valueOf(start.getNodeID()))
                                            && !i.getId().contains(String.valueOf(node.getNodeID())));


                                    try {
                                        //Pathfinding
                                        List<Node> path = AlgorithmSingleton.getInstance().algorithm.findShortestPath(start, node);

                                        paths.set(new ArrayList<>());
                                        List<Node> tempPath = new ArrayList<>();
                                        String curFloor = path.get(0).getFloor();
                                        transitionFloor.set(new ArrayList<>(List.of(curFloor)));
                                        for(int i = 0; i < path.size() - 1; i++){
                                            tempPath.add(path.get(i));
                                            if(!curFloor.equals(path.get(i + 1).getFloor())){
                                                curFloor = path.get(i + 1).getFloor();
                                                transitionFloor.get().add(curFloor);
                                                paths.get().add(tempPath);
                                                tempPath.clear();
                                            }
                                        }
                                        tempPath.add(path.get(path.size() - 1));
                                        paths.get().add(tempPath);

                                        currentPathIndex.set(0);
                                        walkingPath.set(paths.get().get(0));
                                        path.clear();
                                        path.addAll(paths.get().get(0));

                                        for (int i = 0; i < path.size(); i++) {

                                            //formula
                                            double distance = Math.sqrt(Math.pow(path.get(i + 1).getXcoord() - path.get(i).getXcoord(), 2) +
                                                    Math.pow(path.get(i + 1).getYcoord() - path.get(i).getYcoord(), 2));

                                            double opp = path.get(i + 1).getXcoord() - path.get(i).getXcoord();
                                            double adj = path.get(i + 1).getYcoord() - path.get(i).getYcoord();
                                            double angle = Math.atan(opp / adj) * (180 / Math.PI);

                                            Box line = new Box(5, 5, distance);
                                            line.setId("pathBlock");
                                            line.setTranslateZ(path.get(i).getYcoord() + adj / 2);
                                            line.setTranslateX(path.get(i).getXcoord() + opp / 2);
                                            line.getTransforms().add(new Rotate(angle, Rotate.Y_AXIS));
                                            line.setMaterial(new PhongMaterial(Color.BLUE));

                                            root.getChildren().add(line);
                                        }

                                    } catch (SQLException e) {
                                        throw new RuntimeException(e);
                                    }
                                    pathToCompute.clear();
                                }
                            }
                            else {
                                if(!permVisible.get()) {
                                    permVisibleTexts.add(permVisible);
                                    permVisible.set(true);
                                    box.setVisible(true);

                                    t.setFont(new Font(20));

                                    Text lt = new Text(locations.get(node).getLongName());
                                    lt.setId("dNode" + node.getNodeID());
                                    lt.setFont(new Font(24));

                                    Text nXY = new Text("x: " + node.getXcoord() + ", y: " + node.getYcoord());
                                    nXY.setFont(new Font(20));
                                    nXY.setId("dNode" + node.getNodeID());

                                    Text lty = new Text("Type: " + locations.get(node).getNodeType());
                                    lty.setId("dNode" + node.getNodeID());
                                    lty.setFont(new Font(20));

                                    double maxWidth = (computeMax(t.getLayoutBounds().getWidth(),
                                            lt.getLayoutBounds().getWidth(),
                                            nXY.getLayoutBounds().getWidth(),
                                            lty.getLayoutBounds().getWidth()) + 10) / 2;

                                    double maxHeight = computeSum(t.getLayoutBounds().getHeight(),
                                            lt.getLayoutBounds().getHeight(),
                                            nXY.getLayoutBounds().getHeight(),
                                            lty.getLayoutBounds().getHeight());

                                    box.setTranslateX(node.getXcoord() - maxWidth);
                                    box.setTranslateY(100 + maxHeight - box.getHeight());
                                    box.getChildren().add(0, lt);
                                    box.getChildren().addAll(nXY, lty);
                                }
                                else {
                                    box.getChildren().removeIf(i -> i.getId() != null && i.getId().equals("dNode" + node.getNodeID()));
                                    permVisible.set(false);
                                    permVisibleTexts.remove(permVisible);
                                    t.setFont(new Font(24));
                                    box.setTranslateX(node.getXcoord() - ((t.getLayoutBounds().getWidth() + 10) / 2));
                                    box.setVisible(false);
                                    box.setTranslateY(100);
                                }
                            }
                        });
                        s.setOnMouseEntered(event -> {
                            if(!permVisible.get()) {
                                box.setVisible(true);
                            }
                        });
                        s.setOnMouseExited(event -> {
                            if(!permVisible.get()) {
                                box.setVisible(false);
                            }
                        });
                        s.setId("bNode" + node.getNodeID());
                        s.setMaterial(new PhongMaterial(nodeColors.get(locations.get(node).getNodeType())));
                        s.setTranslateZ(node.getYcoord());
                        s.setTranslateX(node.getXcoord());
                        visibleNodes.add(s);

                        //drawing the floating text
                        box.setTranslateZ(node.getYcoord());
                        box.setTranslateX(node.getXcoord() - ((t.getLayoutBounds().getWidth() + 10) / 2));
                        box.setTranslateY(100);

                        box.getTransforms().addAll(
                                new Rotate(180, Rotate.Z_AXIS),
                                new Rotate(180, Rotate.Y_AXIS));
                        box.setVisible(false);
                        box.layoutYProperty().bind(xRotate.pivotXProperty());
                        box.setRotationAxis(Rotate.Y_AXIS);
                        box.rotateProperty().bind(yRotate.angleProperty());
                        box.getTransforms().addAll(xRotate, new Rotate(180, Rotate.X_AXIS));

                        root.getChildren().addAll(s, box);
                    }
                });
            }
        });
        floorToggles.getChildren().add(floor3);

        floorToggles.setAlignment(Pos.CENTER);
        floorToggles.setSpacing(10);
        sidePane.getChildren().add(floorToggles);

        //drawing nodes
        nodes.forEach(node -> {
            if(!locations.get(node).getNodeType().equals("HALL")) {
                VBox box = new VBox();
                box.setId("node" + node.getNodeID());
                box.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-border-color: black; -fx-border-radius: 10; -fx-border-width: 3");
                box.setSpacing(10);
                box.setAlignment(Pos.CENTER);
                box.setPrefSize(VBox.USE_COMPUTED_SIZE, VBox.USE_COMPUTED_SIZE);
                box.setPickOnBounds(false);
                box.setMouseTransparent(true);

                Text t = new Text(locations.get(node).getShortName());
                t.setId("nodeText" + node.getNodeID());
                t.setFont(new Font(24));
                box.getChildren().add(t);

                Box s = new Box(10, 10, 10);
                AtomicBoolean permVisible = new AtomicBoolean(false);
                s.setOnMouseClicked(event -> {
                    //pathfinding
                    if (pathFinding.get()) {
                        permVisibleTexts.add(permVisible);
                        permVisible.set(true);
                        box.setVisible(true);
                        //starting point
                        s.setMaterial(new PhongMaterial(Color.BLACK));
                        if (pathToCompute.size() == 0) {
                            pathToCompute.add(node);
                        }
                        //ending point
                        else {
                            Node start = pathToCompute.get(0);

                            //removing old paths
                            root.getChildren().removeIf(i -> i.getId() != null && i.getId().equals("pathBlock"));
                            root.getChildren().removeIf(i -> i.getId() != null && i.getId().contains("bNode")
                                    && !i.getId().contains(String.valueOf(start.getNodeID()))
                                    && !i.getId().contains(String.valueOf(node.getNodeID())));


                            try {
                                //Pathfinding
                                List<Node> path = AlgorithmSingleton.getInstance().algorithm.findShortestPath(start, node);

                                paths.set(new ArrayList<>());
                                List<Node> tempPath = new ArrayList<>();
                                String curFloor = path.get(0).getFloor();
                                transitionFloor.set(new ArrayList<>(List.of(curFloor)));
                                for(int i = 0; i < path.size() - 1; i++){
                                    tempPath.add(path.get(i));
                                    if(!curFloor.equals(path.get(i + 1).getFloor())){
                                        curFloor = path.get(i + 1).getFloor();
                                        transitionFloor.get().add(curFloor);
                                        paths.get().add(tempPath);
                                        tempPath.clear();
                                    }
                                }
                                tempPath.add(path.get(path.size() - 1));
                                paths.get().add(tempPath);

                                currentPathIndex.set(0);
                                walkingPath.set(paths.get().get(0));
                                path.clear();
                                path.addAll(paths.get().get(0));

                                for (int i = 0; i < path.size(); i++) {

                                    //formula
                                    double distance = Math.sqrt(Math.pow(path.get(i + 1).getXcoord() - path.get(i).getXcoord(), 2) +
                                            Math.pow(path.get(i + 1).getYcoord() - path.get(i).getYcoord(), 2));

                                    double opp = path.get(i + 1).getXcoord() - path.get(i).getXcoord();
                                    double adj = path.get(i + 1).getYcoord() - path.get(i).getYcoord();
                                    double angle = Math.atan(opp / adj) * (180 / Math.PI);

                                    Box line = new Box(5, 5, distance);
                                    line.setId("pathBlock");
                                    line.setTranslateZ(path.get(i).getYcoord() + adj / 2);
                                    line.setTranslateX(path.get(i).getXcoord() + opp / 2);
                                    line.getTransforms().add(new Rotate(angle, Rotate.Y_AXIS));
                                    line.setMaterial(new PhongMaterial(Color.BLUE));

                                    root.getChildren().add(line);
                                }

                            } catch (SQLException e) {
                                throw new RuntimeException(e);
                            }
                            pathToCompute.clear();
                        }
                    }
                    else {
                        if(!permVisible.get()) {
                            permVisibleTexts.add(permVisible);
                            permVisible.set(true);
                            box.setVisible(true);

                            t.setFont(new Font(20));

                            Text lt = new Text(locations.get(node).getLongName());
                            lt.setId("dNode" + node.getNodeID());
                            lt.setFont(new Font(24));

                            Text nXY = new Text("x: " + node.getXcoord() + ", y: " + node.getYcoord());
                            nXY.setFont(new Font(20));
                            nXY.setId("dNode" + node.getNodeID());

                            Text lty = new Text("Type: " + locations.get(node).getNodeType());
                            lty.setId("dNode" + node.getNodeID());
                            lty.setFont(new Font(20));

                            double maxWidth = (computeMax(t.getLayoutBounds().getWidth(),
                                    lt.getLayoutBounds().getWidth(),
                                    nXY.getLayoutBounds().getWidth(),
                                    lty.getLayoutBounds().getWidth()) + 10) / 2;

                            double maxHeight = computeSum(t.getLayoutBounds().getHeight(),
                                    lt.getLayoutBounds().getHeight(),
                                    nXY.getLayoutBounds().getHeight(),
                                    lty.getLayoutBounds().getHeight());

                            box.setTranslateX(node.getXcoord() - maxWidth);
                            box.setTranslateY(100 + maxHeight - box.getHeight());
                            box.getChildren().add(0, lt);
                            box.getChildren().addAll(nXY, lty);
                        }
                        else {
                            box.getChildren().removeIf(i -> i.getId() != null && i.getId().equals("dNode" + node.getNodeID()));
                            permVisible.set(false);
                            permVisibleTexts.remove(permVisible);
                            t.setFont(new Font(24));
                            box.setTranslateX(node.getXcoord() - ((t.getLayoutBounds().getWidth() + 10) / 2));
                            box.setVisible(false);
                            box.setTranslateY(100);
                        }
                    }
                });
                s.setOnMouseEntered(event -> {
                    if(!permVisible.get()) {
                        box.setVisible(true);
                    }
                });
                s.setOnMouseExited(event -> {
                    if(!permVisible.get()) {
                        box.setVisible(false);
                    }
                });
                s.setId("bNode" + node.getNodeID());
                s.setMaterial(new PhongMaterial(nodeColors.get(locations.get(node).getNodeType())));
                s.setTranslateZ(node.getYcoord());
                s.setTranslateX(node.getXcoord());
                visibleNodes.add(s);

                //drawing the floating text
                box.setTranslateZ(node.getYcoord());
                box.setTranslateX(node.getXcoord() - ((t.getLayoutBounds().getWidth() + 10) / 2));
                box.setTranslateY(100);

                box.getTransforms().addAll(
                        new Rotate(180, Rotate.Z_AXIS),
                        new Rotate(180, Rotate.Y_AXIS));
                box.setVisible(false);
                box.layoutYProperty().bind(xRotate.pivotXProperty());
                box.setRotationAxis(Rotate.Y_AXIS);
                box.rotateProperty().bind(yRotate.angleProperty());
                box.getTransforms().addAll(xRotate, new Rotate(180, Rotate.X_AXIS));

                root.getChildren().addAll(s, box);
            }
        });

        Text filterHeader = new Text("Filters");
        filterHeader.setFont(new Font(24));
        VBox.setMargin(filterHeader, new Insets(20,0,0,0));
        sidePane.getChildren().add(filterHeader);

        VBox filterCol1 = new VBox();
        filterCol1.setSpacing(10);
        filterCol1.setAlignment(Pos.CENTER_LEFT);

        VBox filterCol2 = new VBox();
        filterCol2.setSpacing(10);
        filterCol2.setAlignment(Pos.CENTER_LEFT);

        HBox filters = new HBox();
        filters.setAlignment(Pos.CENTER);
        filters.setSpacing(25);
        filters.getChildren().add(filterCol1);
        filters.getChildren().add(filterCol2);
        sidePane.getChildren().add(filters);

        MFXCheckbox restroomFilter = new MFXCheckbox("Restrooms");
        restroomFilter.setTextFill(Color.MEDIUMSLATEBLUE);
        restroomFilter.setFont(new Font(18));
        restroomFilter.setSelected(true);
        restroomFilter.selectedProperty().addListener((obs, o, n) -> {
            root.getChildren().forEach(node -> {
                if(node.getId() != null && node.getId().contains("bNode")){
                    int id = Integer.parseInt(node.getId().substring(node.getId().lastIndexOf('e') + 1));
                    Node no = nodes.parallelStream().filter(i -> i.getNodeID() == id).toList().get(0);
                    if(locations.get(no).getNodeType().equals("BATH") || locations.get(no).getNodeType().equals("REST")){
                        node.setVisible(n);
                    }
                }
            });
        });


        MFXCheckbox conferenceFilter = new MFXCheckbox("Conference");
        conferenceFilter.setTextFill(Color.ORANGERED);
        conferenceFilter.setFont(new Font(18));
        conferenceFilter.setSelected(true);
        conferenceFilter.selectedProperty().addListener((obs, o, n) -> {
            root.getChildren().forEach(node -> {
                if(node.getId() != null && node.getId().contains("bNode")){
                    int id = Integer.parseInt(node.getId().substring(node.getId().lastIndexOf('e') + 1));
                    Node no = nodes.parallelStream().filter(i -> i.getNodeID() == id).toList().get(0);
                    if(locations.get(no).getNodeType().equals("CONF")){
                        node.setVisible(n);
                    }
                }
            });
        });
        filterCol1.getChildren().add(restroomFilter);
        filterCol2.getChildren().add(conferenceFilter);

        MFXCheckbox departmentFilter = new MFXCheckbox("Department");
        departmentFilter.setTextFill(Color.GREEN);
        departmentFilter.setFont(new Font(18));
        departmentFilter.setSelected(true);
        departmentFilter.selectedProperty().addListener((obs, o, n) -> {
            root.getChildren().forEach(node -> {
                if(node.getId() != null && node.getId().contains("bNode")){
                    int id = Integer.parseInt(node.getId().substring(node.getId().lastIndexOf('e') + 1));
                    Node no = nodes.parallelStream().filter(i -> i.getNodeID() == id).toList().get(0);
                    if(locations.get(no).getNodeType().equals("DEPT")){
                        node.setVisible(n);
                    }
                }
            });
        });

        MFXCheckbox elevatorFilter = new MFXCheckbox("Elevator");
        elevatorFilter.setTextFill(Color.PURPLE);
        elevatorFilter.setFont(new Font(18));
        elevatorFilter.setSelected(true);
        elevatorFilter.selectedProperty().addListener((obs, o, n) -> {
            root.getChildren().forEach(node -> {
                if(node.getId() != null && node.getId().contains("bNode")){
                    int id = Integer.parseInt(node.getId().substring(node.getId().lastIndexOf('e') + 1));
                    Node no = nodes.parallelStream().filter(i -> i.getNodeID() == id).toList().get(0);
                    if(locations.get(no).getNodeType().equals("ELEV")){
                        node.setVisible(n);
                    }
                }
            });
        });

        filterCol1.getChildren().add(departmentFilter);
        filterCol2.getChildren().add(elevatorFilter);

        MFXCheckbox exitFilter = new MFXCheckbox("Exit");
        exitFilter.setTextFill(Color.RED);
        exitFilter.setFont(new Font(18));
        exitFilter.setSelected(true);
        exitFilter.selectedProperty().addListener((obs, o, n) -> {
            root.getChildren().forEach(node -> {
                if(node.getId() != null && node.getId().contains("bNode")){
                    int id = Integer.parseInt(node.getId().substring(node.getId().lastIndexOf('e') + 1));
                    Node no = nodes.parallelStream().filter(i -> i.getNodeID() == id).toList().get(0);
                    if(locations.get(no).getNodeType().equals("EXIT")){
                        node.setVisible(n);
                    }
                }
            });
        });

        MFXCheckbox infoFilter = new MFXCheckbox("Info");
        infoFilter.setTextFill(Color.GOLDENROD);
        infoFilter.setFont(new Font(18));
        infoFilter.setSelected(true);
        infoFilter.selectedProperty().addListener((obs, o, n) -> {
            root.getChildren().forEach(node -> {
                if(node.getId() != null && node.getId().contains("bNode")){
                    int id = Integer.parseInt(node.getId().substring(node.getId().lastIndexOf('e') + 1));
                    Node no = nodes.parallelStream().filter(i -> i.getNodeID() == id).toList().get(0);
                    if(locations.get(no).getNodeType().equals("INFO")){
                        node.setVisible(n);
                    }
                }
            });
        });

        filterCol1.getChildren().add(exitFilter);
        filterCol2.getChildren().add(infoFilter);

        MFXCheckbox labFilter = new MFXCheckbox("Labs");
        labFilter.setTextFill(Color.MAGENTA);
        labFilter.setFont(new Font(18));
        labFilter.setSelected(true);
        labFilter.selectedProperty().addListener((obs, o, n) -> {
            root.getChildren().forEach(node -> {
                if(node.getId() != null && node.getId().contains("bNode")){
                    int id = Integer.parseInt(node.getId().substring(node.getId().lastIndexOf('e') + 1));
                    Node no = nodes.parallelStream().filter(i -> i.getNodeID() == id).toList().get(0);
                    if(locations.get(no).getNodeType().equals("LABS")){
                        node.setVisible(n);
                    }
                }
            });
        });

        MFXCheckbox retailFilter = new MFXCheckbox("Retail");
        retailFilter.setTextFill(Color.TAN);
        retailFilter.setFont(new Font(18));
        retailFilter.setSelected(true);
        retailFilter.selectedProperty().addListener((obs, o, n) -> {
            root.getChildren().forEach(node -> {
                if(node.getId() != null && node.getId().contains("bNode")){
                    int id = Integer.parseInt(node.getId().substring(node.getId().lastIndexOf('e') + 1));
                    Node no = nodes.parallelStream().filter(i -> i.getNodeID() == id).toList().get(0);
                    if(locations.get(no).getNodeType().equals("RETL")){
                        node.setVisible(n);
                    }
                }
            });
        });

        filterCol1.getChildren().add(labFilter);
        filterCol2.getChildren().add(retailFilter);

        MFXCheckbox serviceFilter = new MFXCheckbox("Service");
        serviceFilter.setTextFill(Color.SALMON);
        serviceFilter.setFont(new Font(18));
        serviceFilter.setSelected(true);
        serviceFilter.selectedProperty().addListener((obs, o, n) -> {
            root.getChildren().forEach(node -> {
                if(node.getId() != null && node.getId().contains("bNode")){
                    int id = Integer.parseInt(node.getId().substring(node.getId().lastIndexOf('e') + 1));
                    Node no = nodes.parallelStream().filter(i -> i.getNodeID() == id).toList().get(0);
                    if(locations.get(no).getNodeType().equals("SERV")){
                        node.setVisible(n);
                    }
                }
            });
        });

        MFXCheckbox stairsFilter = new MFXCheckbox("Stairs");
        stairsFilter.setTextFill(Color.SILVER);
        stairsFilter.setFont(new Font(18));
        stairsFilter.setSelected(true);
        stairsFilter.selectedProperty().addListener((obs, o, n) -> {
            root.getChildren().forEach(node -> {
                if(node.getId() != null && node.getId().contains("bNode")){
                    int id = Integer.parseInt(node.getId().substring(node.getId().lastIndexOf('e') + 1));
                    Node no = nodes.parallelStream().filter(i -> i.getNodeID() == id).toList().get(0);
                    if(locations.get(no).getNodeType().equals("STAI")){
                        node.setVisible(n);
                    }
                }
            });
        });

        filterCol1.getChildren().add(serviceFilter);
        filterCol2.getChildren().add(stairsFilter);

        sidePane.setStyle("-fx-background-color: white; -fx-border-color: black; -fx-min-width: 300; -fx-min-height: 800");
        sidePane.setAlignment(Pos.TOP_CENTER);
        BooleanProperty menuShowing = new SimpleBooleanProperty(true);
        MFXButton transportMenu = new MFXButton("hide");
        transportMenu.setOnAction(event -> {
            if(menuShowing.get()){
                menuShowing.set(false);
                transportMenu.setText("show");
                TranslateTransition transition = new TranslateTransition();
                transition.setNode(sidePane);
                transition.setByX(-sidePane.getWidth());
                transition.setDuration(Duration.millis(500));
                transition.play();
                transition.setOnFinished(e -> {
                    sidePane.getChildren().remove(transportMenu);
                    transportMenu.setLayoutX(0);
                    parent.getChildren().add(transportMenu);
                });
            }
            else {
                menuShowing.set(true);
                transportMenu.setText("hide");
                TranslateTransition transition = new TranslateTransition();
                transition.setNode(sidePane);
                transition.setByX(sidePane.getWidth());
                transition.setDuration(Duration.millis(500));
                transition.play();
                parent.getChildren().remove(transportMenu);
                sidePane.getChildren().add(0, transportMenu);
            }
        });
        transportMenu.setAlignment(Pos.CENTER_RIGHT);
        sidePane.getChildren().add(0, transportMenu);

        parent.getChildren().add(sidePane);
        parent.getChildren().add(group);
        group.toBack();

        AtomicInteger nodeCounter = new AtomicInteger();
        AtomicReference<SequentialTransition> pathTransition = new AtomicReference<>();
        parent.setOnKeyPressed(event -> {
            double sceneSize = Math.sqrt(App.getPrimaryStage().getWidth() * App.getPrimaryStage().getHeight());
            double screenIncrement = sceneSize / 5;

            double angleY = Math.toRadians(yRotate.getAngle());
            double dxForward = screenIncrement * Math.sin(angleY);
            double dzForward = screenIncrement * Math.cos(angleY);
            double dxNormal = screenIncrement * Math.cos(angleY);
            double dzNormal = screenIncrement * -1 * Math.sin(angleY);

            if (event.isShiftDown() && event.getCode() == KeyCode.EQUALS) {
                camera.setTranslateY(camera.getTranslateY() - screenIncrement);
            }
            else if (event.isShiftDown() && event.getCode() == KeyCode.MINUS) {
                camera.setTranslateY(camera.getTranslateY() + screenIncrement);
            }
            else if (event.getCode() == KeyCode.W) {
                double forwardDir = -1;
                double normalDir = 0;

                cameraX.set(cameraX.get() + forwardDir * dxForward + normalDir * dxNormal);
                cameraZ.set(cameraZ.get() + forwardDir * dzForward + normalDir * dzNormal);
            }
            else if (event.getCode() == KeyCode.D) {
                double forwardDir = 0;
                double normalDir = 1;

                cameraX.set(cameraX.get() + forwardDir * dxForward + normalDir * dxNormal);
                cameraZ.set(cameraZ.get() + forwardDir * dzForward + normalDir * dzNormal);
            }
            else if (event.getCode() == KeyCode.S) {
                double forwardDir = 1;
                double normalDir = 0;

                cameraX.set(cameraX.get() + forwardDir * dxForward + normalDir * dxNormal);
                cameraZ.set(cameraZ.get() + forwardDir * dzForward + normalDir * dzNormal);
            }
            else if (event.getCode() == KeyCode.A) {
                double forwardDir = 0;
                double normalDir = -1;

                cameraX.set(cameraX.get() + forwardDir * dxForward + normalDir * dxNormal);
                cameraZ.set(cameraZ.get() + forwardDir * dzForward + normalDir * dzNormal);
            }
            else if (event.getCode() == KeyCode.L && pathTransition.get() == null) {
                //Move camera to node start
                camera.translateXProperty().unbindBidirectional(cameraX);
                camera.translateYProperty().unbindBidirectional(cameraY);
                camera.translateZProperty().unbindBidirectional(cameraZ);

                List<Integer> pathIds = walkingPath.get().parallelStream().map(Node::getNodeID).toList();

                root.getChildren().parallelStream().filter(n -> n instanceof VBox).forEach(n -> {
                    if(n.getId() != null){
                        int id = Integer.parseInt(n.getId().substring(n.getId().lastIndexOf('e') + 1));
                        if(pathIds.contains(id)){
                            n.setTranslateY(30);
                            n.setVisible(true);
                            ((VBox) n).getChildren().forEach(node1 -> {
                                if(node1 instanceof Text && node1.getId() != null && node1.getId().contains("nodeText")){
                                    String tid = node1.getId().substring(node1.getId().lastIndexOf('t') + 1);
                                    ((Text) node1).setFont(new Font(10));
                                    n.setTranslateX(nodes.parallelStream().filter(n1 -> n1.getNodeID() == Integer.parseInt(tid)).toList().get(0).getXcoord() - ((node1.getLayoutBounds().getWidth() + 10) / 2));
                                }
                            });
                        }
                    }
                });

                camera.setTranslateX(walkingPath.get().get(0).getXcoord());
                camera.setTranslateZ(walkingPath.get().get(0).getYcoord());

                camera.getTransforms().clear();
                double opp = walkingPath.get().get(1).getXcoord() - walkingPath.get().get(0).getXcoord();
                double adj = walkingPath.get().get(1).getYcoord() - walkingPath.get().get(0).getYcoord();
                double angle = 180 + Math.toDegrees(Math.atan2(opp, adj));
                xRotate.setAngle(180);
                yRotate.setAngle(angle);
                camera.getTransforms().addAll(xRotate, yRotate);
                camera.setTranslateY(13);
                double oldAngle = angle;
                //Transition from node to node
                pathTransition.set(new SequentialTransition());
                for (int i = nodeCounter.get(); i < walkingPath.get().size() - 1; i++) {
                    //animation from node to node
                    Point3D currentPosition = new Point3D(walkingPath.get().get(i).getXcoord(), 10, walkingPath.get().get(i).getYcoord());
                    Point3D endPosition = new Point3D(walkingPath.get().get(i + 1).getXcoord(), 10, walkingPath.get().get(i + 1).getYcoord());

                    Point3D translation = endPosition.subtract(currentPosition);

                    double distance = translation.magnitude();
                    double speed = 75;
                    double duration = distance / speed;

                    TranslateTransition transition = new TranslateTransition(Duration.seconds(duration), camera);
                    transition.setByX(translation.getX());
                    transition.setByY(translation.getY());
                    transition.setByZ(translation.getZ());

                    //Animation for rotating
                    if(i < walkingPath.get().size() - 2){
                        camera.getTransforms().clear();
                        camera.getTransforms().add(xRotate);
                        opp = walkingPath.get().get(i + 2).getXcoord() - walkingPath.get().get(i+1).getXcoord();
                        adj = walkingPath.get().get(i + 2).getYcoord() - walkingPath.get().get(i+1).getYcoord();
                        angle = 180 + Math.toDegrees(Math.atan2(opp, adj));
                        double diff = angle - oldAngle;
                        if (diff > 180) {
                            angle -= 360;
                        } else if (diff < -180) {
                            angle += 360;
                        }

                        double rotationSpeed = 100;
                        double rotateDuration = Math.abs(oldAngle-angle) / rotationSpeed;

                        RotateTransition rotateTransition = new RotateTransition(Duration.seconds(rotateDuration), camera);
                        rotateTransition.setFromAngle(oldAngle);
                        rotateTransition.setToAngle(angle);
                        rotateTransition.setAxis(Rotate.Y_AXIS);
                        oldAngle = angle;

                        double finalAngle = angle;
                        rotateTransition.setOnFinished(event1 -> {
                            yRotate.setAngle(finalAngle);
                        });
                        pathTransition.get().getChildren().addAll(transition, rotateTransition);
                    }
                    else {
                        pathTransition.get().getChildren().add(transition);
                    }
                }
                pathTransition.get().play();
            }
            else if (event.getCode() == KeyCode.K && pathTransition.get() == null) {
                RobotComm.runRobot(walkingPath.get());
                while(!RobotComm.running){
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                try {
                    Thread.sleep(2500);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                System.out.println("Done waiting");
                //Move camera to node start
                camera.translateXProperty().unbindBidirectional(cameraX);
                camera.translateYProperty().unbindBidirectional(cameraY);
                camera.translateZProperty().unbindBidirectional(cameraZ);

                List<Integer> pathIds = walkingPath.get().parallelStream().map(Node::getNodeID).toList();

                root.getChildren().parallelStream().filter(n -> n instanceof VBox).forEach(n -> {
                    if(n.getId() != null){
                        int id = Integer.parseInt(n.getId().substring(n.getId().lastIndexOf('e') + 1));
                        if(pathIds.contains(id)){
                            n.setTranslateY(30);
                            n.setVisible(true);
                            ((VBox) n).getChildren().forEach(node1 -> {
                                if(node1 instanceof Text && node1.getId() != null && node1.getId().contains("nodeText")){
                                    String tid = node1.getId().substring(node1.getId().lastIndexOf('t') + 1);
                                    ((Text) node1).setFont(new Font(10));
                                    n.setTranslateX(nodes.parallelStream().filter(n1 -> n1.getNodeID() == Integer.parseInt(tid)).toList().get(0).getXcoord() - ((node1.getLayoutBounds().getWidth() + 10) / 2));
                                }
                            });
                        }
                    }
                });

                camera.setTranslateX(walkingPath.get().get(0).getXcoord());
                camera.setTranslateZ(walkingPath.get().get(0).getYcoord());
                System.out.println("Test2");
                camera.getTransforms().clear();
                double opp = walkingPath.get().get(1).getXcoord() - walkingPath.get().get(0).getXcoord();
                double adj = walkingPath.get().get(1).getYcoord() - walkingPath.get().get(0).getYcoord();
//                double angle = 180 + Math.toDegrees(Math.atan2(opp, adj));
                double angle = 0;
                xRotate.setAngle(180);
                yRotate.setAngle(angle);
                camera.getTransforms().addAll(xRotate, yRotate);
                camera.setTranslateY(13);
                double oldAngle = angle;
                //Transition from node to node
                pathTransition.set(new SequentialTransition());
                System.out.println("test3");
                for (int i = nodeCounter.get(); i < walkingPath.get().size() - 1; i++) {
                    //animation from node to node
                    Point3D currentPosition = new Point3D(walkingPath.get().get(i).getXcoord(), 10, walkingPath.get().get(i).getYcoord());
                    Point3D endPosition = new Point3D(walkingPath.get().get(i + 1).getXcoord(), 10, walkingPath.get().get(i + 1).getYcoord());

                    Point3D translation = endPosition.subtract(currentPosition);

                    double distance = translation.magnitude();
                    double speed = 20;
                    double duration = distance / speed;

                    TranslateTransition transition = new TranslateTransition(Duration.seconds(duration), camera);
                    transition.setByX(translation.getX());
                    transition.setByY(translation.getY());
                    transition.setByZ(translation.getZ());

                    //Animation for rotating
                    if(i < walkingPath.get().size() - 2){
                        camera.getTransforms().clear();
                        camera.getTransforms().add(xRotate);
                        opp = walkingPath.get().get(i + 2).getXcoord() - walkingPath.get().get(i+1).getXcoord();
                        adj = walkingPath.get().get(i + 2).getYcoord() - walkingPath.get().get(i+1).getYcoord();
                        angle = 180 + Math.toDegrees(Math.atan2(opp, adj));
                        double diff = angle - oldAngle;
                        if (diff > 180) {
                            angle -= 360;
                        } else if (diff < -180) {
                            angle += 360;
                        }

                        double rotationSpeed = speed * 2;
                        double rotateDuration = Math.abs(oldAngle-angle) / rotationSpeed;

                        RotateTransition rotateTransition = new RotateTransition(Duration.seconds(rotateDuration), camera);
                        rotateTransition.setFromAngle(oldAngle);
                        rotateTransition.setToAngle(angle);
                        rotateTransition.setAxis(Rotate.Y_AXIS);
                        oldAngle = angle;

                        double finalAngle = angle;
                        rotateTransition.setOnFinished(event1 -> {
                            yRotate.setAngle(finalAngle);
                        });
//                        PauseTransition pause = new PauseTransition(Duration.millis(20));
                        pathTransition.get().getChildren().addAll(transition,new PauseTransition(Duration.millis(100)), rotateTransition,new PauseTransition(Duration.millis(200)));
                    }
                    else {
                        pathTransition.get().getChildren().add(transition);
                    }
                }
                pathTransition.get().play();
                System.out.println("test4");
            }
            else if (event.getCode() == KeyCode.E) { // Turn Camera Right
                
                camera.getTransforms().stream().filter(node -> node instanceof Rotate).forEach(transform -> {
                    if(((Rotate) transform).getAxis() == Rotate.Y_AXIS){
                        ((Rotate) transform).setAngle(((Rotate) transform).getAngle() - 7.5);
                    }
                });
            }
            else if (event.getCode() == KeyCode.Q) { // Turn Camera Left
                camera.getTransforms().stream().filter(node -> node instanceof Rotate).forEach(transform -> {
                    if(((Rotate) transform).getAxis() == Rotate.Y_AXIS){
                        ((Rotate) transform).setAngle(((Rotate) transform).getAngle() + 7.5);
                    }
                });
            }
            else if(event.getCode() == KeyCode.ESCAPE){
                if(pathFinding.get()) {
                    if (pathTransition.get() != null) {
                        camera.setRotationAxis(Rotate.Y_AXIS);
                        camera.setRotate(0);
                        xRotate.setAngle(90);
                        if (pathTransition.get().getStatus() == Animation.Status.RUNNING) {
                            pathTransition.get().stop();
                        }
                        pathTransition.set(null);
                        camera.getTransforms().clear();
                        camera.getTransforms().addAll(
                                yRotate,
                                xRotate
                        );
                        camera.translateXProperty().bindBidirectional(cameraX);
                        camera.translateYProperty().bindBidirectional(cameraY);
                        camera.translateZProperty().bindBidirectional(cameraZ);

                        root.getChildren().forEach(node -> {
                            if(node.getId() != null && node.getId().contains("node") && node instanceof VBox) {
                                ((VBox) node).getChildren().removeIf(node1 -> node1.getId() != null && node1.getId().contains("dNode"));
                                ((VBox) node).getChildren().forEach(node1 -> {
                                    if(node1 instanceof Text && node1.getId() != null && node1.getId().contains("nodeText")){
                                        String id = node1.getId().substring(node1.getId().lastIndexOf('t') + 1);
                                        ((Text) node1).setFont(new Font(24));
                                        node.setTranslateX(nodes.parallelStream().filter(n -> n.getNodeID() == Integer.parseInt(id)).toList().get(0).getXcoord() - ((node1.getLayoutBounds().getWidth() + 10) / 2));
                                    }
                                });
                                if(!node.getId().contains(String.valueOf(walkingPath.get().get(0).getNodeID())) && !node.getId().contains(String.valueOf(walkingPath.get().get(walkingPath.get().size() - 1).getNodeID()))){
                                    node.setVisible(false);
                                }
                            }
                        });
                    }
                    else if(walkingPath.get() != null){
                        newPathButton.fire();
                    }

                }
                else{
                    permVisibleTexts.forEach(bool -> bool.set(false));
                    permVisibleTexts.clear();
                    root.getChildren().removeIf(i -> i.getId() != null && i.getId().equals("dNode"));
                    root.getChildren().forEach(node -> {
                        if(node.getId() != null && node.getId().contains("node") && node instanceof VBox) {
                            ((VBox) node).getChildren().removeIf(node1 -> node1.getId() != null && node1.getId().contains("dNode"));
                            ((VBox) node).getChildren().forEach(node1 -> {
                                if(node1 instanceof Text && node1.getId() != null && node1.getId().contains("nodeText")){
                                    String id = node1.getId().substring(node1.getId().lastIndexOf('t') + 1);
                                    ((Text) node1).setFont(new Font(24));
                                    node.setTranslateX(nodes.parallelStream().filter(n -> n.getNodeID() == Integer.parseInt(id)).toList().get(0).getXcoord() - ((node1.getLayoutBounds().getWidth() + 10) / 2));
                                }
                            });
                            node.setVisible(false);
                        }
                    });
                }
            }
            else if(event.getCode() == KeyCode.SPACE){
                if(pathFinding.get()){
                    if(pathTransition.get() != null){
                        if(pathTransition.get().getStatus() == Animation.Status.RUNNING){
                            pathTransition.get().pause();
                        }
                        else if(pathTransition.get().getStatus() == Animation.Status.STOPPED){
                            pathTransition.get().play();
                        }
                    }
                }
            }
            else if(event.getCode() == KeyCode.ENTER){
                if(pathFinding.get() && pathTransition.get().getStatus() == Animation.Status.STOPPED && currentPathIndex.get() < paths.get().size()){
                    currentPathIndex.set(currentPathIndex.get() + 1);
                    int floorN = parseFloorNumb(transitionFloor.get().get(currentPathIndex.get()));
                    switch (floorN){
                        case 0 -> lower2.setSelected(true);
                        case 1 -> lower1.setSelected(true);
                        case 2 -> floor1.setSelected(true);
                        case 3 -> floor2.setSelected(true);
                        case 4 -> floor3.setSelected(true);
                    }
                    List<Node> path = paths.get().get(currentPathIndex.get());
                    walkingPath.set(path);
                    for (int i = 0; i < path.size() - 1; i++) {
                        //formula
                        double distance = Math.sqrt(Math.pow(path.get(i + 1).getXcoord() - path.get(i).getXcoord(), 2) +
                                Math.pow(path.get(i + 1).getYcoord() - path.get(i).getYcoord(), 2));

                        double opp = path.get(i + 1).getXcoord() - path.get(i).getXcoord();
                        double adj = path.get(i + 1).getYcoord() - path.get(i).getYcoord();
                        double angle = Math.atan(opp / adj) * (180 / Math.PI);

                        Box line = new Box(5, 5, distance);
                        line.setId("pathBlock");
                        line.setTranslateZ(path.get(i).getYcoord() + adj / 2);
                        line.setTranslateX(path.get(i).getXcoord() + opp / 2);
                        line.getTransforms().add(new Rotate(angle, Rotate.Y_AXIS));
                        line.setMaterial(new PhongMaterial(Color.BLUE));

                        root.getChildren().add(line);
                    }
                }
            }
        });

        parent.setOnScroll(event -> {
            System.out.println(event.getDeltaY());
        });
    }

    private double computeMax(double... items){
        double max = Double.MIN_VALUE;
        if(items == null) return max;
        for(double item : items){
            if(item > max) max = item;
        }
        return max;
    }
    private double computeSum(double... items){
        double sum = 0;
        if(items == null) return sum;
        for(double item : items){
            sum += item;
        }
        return sum;
    }
    private double computeSum(List<Integer> items){
        double sum = 0;
        if(items == null) return sum;
        for(int item : items){
            sum += item;
        }
        return sum;
    }
    private double computeMidPoint(List<Integer> items){
        if(items == null) return 0;
        return computeSum(items) / items.size();
    }
    private double computeSTD(List<Point2D> items){
        if(items == null) return 0;

        double sumX = computeSum(items.parallelStream().map(x -> (int) x.getX()).toList());
        double sumY = computeSum(items.parallelStream().map(y -> (int) y.getY()).toList());

        double meanX = sumX / items.size();
        double meanY = sumY / items.size();

        double stdX = 0, stdY = 0;
        for(Point2D point : items){
            stdX += Math.pow(point.getX() - meanX, 2);
            stdY += Math.pow(point.getY() - meanY, 2);
        }

        return Math.sqrt(Math.sqrt(stdX / items.size()) * Math.sqrt(stdY / items.size()));
    }
    private int parseFloorNumb(String floor){
        if(floor == null) return -1;
        switch (floor){
            case "L2" -> {
                return 0;
            }
            case "L1" -> {
                return 1;
            }
            case "1" -> {
                return 2;
            }
            case "2" -> {
                return 3;
            }
            case "3" -> {
                return 4;
            }
        }
        return -1;
    }
}
