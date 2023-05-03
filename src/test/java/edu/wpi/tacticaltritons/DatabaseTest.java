package edu.wpi.tacticaltritons;

import edu.wpi.tacticaltritons.database.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.List;

public class DatabaseTest {

    @Test
    public void edgeTest() throws SQLException{
        //insert
        Node start = DAOFacade.getNode(2995);
        Node end = DAOFacade.getNode(3000);
        Edge edge = new Edge(start, end,0);
        Node edgeS = edge.getStartNode();
        Node edgeE = edge.getEndNode();

        DAOFacade.addEdge(edge);
        Edge expected = DAOFacade.getEdge(start, end);
        Node expectedS = expected.getStartNode();
        Node expectedE = expected.getEndNode();
        Assertions.assertTrue(expectedS.equals(edgeS) && expectedE.equals(edgeE));
        DAOFacade.deleteEdge(edge);

        //get
        start = DAOFacade.getNode(2995);
        end = DAOFacade.getNode(3000);
        edge = new Edge(start, end,0);
        edgeS = edge.getStartNode();
        edgeE = edge.getEndNode();

        DAOFacade.addEdge(edge);
        expected = DAOFacade.getEdge(start, end);
        expectedS = expected.getStartNode();
        expectedE = expected.getEndNode();
        Assertions.assertTrue(expectedS == (edgeS) && expectedE == (edgeE));
        DAOFacade.deleteEdge(edge);

        //delete
        start = DAOFacade.getNode(2995);
        end = DAOFacade.getNode(3000);
        edge = new Edge(start, end,0);
        DAOFacade.addEdge(edge);
        DAOFacade.deleteEdge(edge);
        Assertions.assertNull(DAOFacade.getEdge(start, end));
    }

//    @Test
//    public void flowerTest() throws SQLException{
//        LocalDateTime dt = LocalDateTime.parse("2023-11-03T12:45");
//        Time time = Time.valueOf(dt.toLocalTime());
//        Flower flower = new Flower("Abby", "Amber", "Bob", "Barry", "Carol", "Cam",
//                new Date(1696996464), time, "Hall 1 Level 2", "rose", 2, RequestStatus.BLANK);
//        Flower updatedFlower = new Flower("Abby", "Amber", "Bob", "Barry", "Daisy", "Donald",
//                new Date(1696996464), time, "Hall 1 Level 2", "rose", 2, RequestStatus.BLANK);
//
//        //insert
//        DAOFacade.addFlower(flower);
//        List<Flower> expectedList = DAOFacade.getAllFlower();
//        Flower expected = expectedList.get(expectedList.size()-1);
//        int expectedON = expected.getOrderNum();
//        Assertions.assertEquals(flower.getRequesterFirst(), expected.getRequesterFirst());
//        Assertions.assertEquals(flower.getRequesterLast(), expected.getRequesterLast());
//        Assertions.assertEquals(flower.getPatientLast(), expected.getPatientLast());
//        Assertions.assertEquals(flower.getPatientFirst(), expected.getPatientFirst());
//        Assertions.assertEquals(flower.getAssignedStaffFirst(), expected.getAssignedStaffFirst());
//        Assertions.assertEquals(flower.getAssignedStaffLast(), expected.getAssignedStaffLast());
//        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
//        Assertions.assertEquals(formatter.format(flower.getDeliveryDate()),formatter.format(expected.getDeliveryDate()));
//        Assertions.assertEquals(flower.getDeliveryTime(), expected.getDeliveryTime());
//        Assertions.assertEquals(flower.getItems(), expected.getItems());
//        Assertions.assertEquals(flower.getLocation(), expected.getLocation());
//        Assertions.assertEquals(flower.getTotal(), expected.getTotal());
//        Assertions.assertEquals(flower.getStatus(), expected.getStatus());
//        DAOFacade.deleteFlower(DAOFacade.getFlower(expectedON));
//
//        //get
//        DAOFacade.addFlower(flower);
//        expectedList = DAOFacade.getAllFlower();
//        expectedON = expectedList.get(expectedList.size()-1).getOrderNum();
//        expected = DAOFacade.getFlower(expectedON);
//        Assertions.assertEquals(flower.getRequesterFirst(), expected.getRequesterFirst());
//        Assertions.assertEquals(flower.getRequesterLast(), expected.getRequesterLast());
//        Assertions.assertEquals(flower.getPatientLast(), expected.getPatientLast());
//        Assertions.assertEquals(flower.getPatientFirst(), expected.getPatientFirst());
//        Assertions.assertEquals(flower.getAssignedStaffFirst(), expected.getAssignedStaffFirst());
//        Assertions.assertEquals(flower.getAssignedStaffLast(), expected.getAssignedStaffLast());
//        formatter = new SimpleDateFormat("dd/MM/yyyy");
//        Assertions.assertEquals(formatter.format(flower.getDeliveryDate()),formatter.format(expected.getDeliveryDate()));
//        Assertions.assertEquals(flower.getDeliveryTime(), expected.getDeliveryTime());
//        Assertions.assertEquals(flower.getItems(), expected.getItems());
//        Assertions.assertEquals(flower.getLocation(), expected.getLocation());
//        Assertions.assertEquals(flower.getTotal(), expected.getTotal());
//        Assertions.assertEquals(flower.getStatus(), expected.getStatus());
//        DAOFacade.deleteFlower(DAOFacade.getFlower(expectedON));
//
//        //update
//        DAOFacade.addFlower(flower);
//        DAOFacade.updateFlower(updatedFlower);
//        expectedList = DAOFacade.getAllFlower();
//        expectedON = expectedList.get(expectedList.size()-1).getOrderNum();
//        expected = DAOFacade.getFlower(expectedON);
//        Assertions.assertEquals(flower.getRequesterFirst(), expected.getRequesterFirst());
//        Assertions.assertEquals(flower.getRequesterLast(), expected.getRequesterLast());
//        Assertions.assertEquals(flower.getPatientLast(), expected.getPatientLast());
//        Assertions.assertEquals(flower.getPatientFirst(), expected.getPatientFirst());
//        Assertions.assertEquals(flower.getAssignedStaffFirst(), expected.getAssignedStaffFirst());
//        Assertions.assertEquals(flower.getAssignedStaffLast(), expected.getAssignedStaffLast());
//        formatter = new SimpleDateFormat("dd/MM/yyyy");
//        Assertions.assertEquals(formatter.format(flower.getDeliveryDate()),formatter.format(expected.getDeliveryDate()));
//        Assertions.assertEquals(flower.getDeliveryTime(), expected.getDeliveryTime());
//        Assertions.assertEquals(flower.getItems(), expected.getItems());
//        Assertions.assertEquals(flower.getLocation(), expected.getLocation());
//        Assertions.assertEquals(flower.getTotal(), expected.getTotal());
//        Assertions.assertEquals(flower.getStatus(), expected.getStatus());
//        DAOFacade.deleteFlower(DAOFacade.getFlower(expectedON));
//
//        //delete
//        DAOFacade.addFlower(updatedFlower);
//        DAOFacade.deleteFlower(DAOFacade.getFlower(expectedON));
//        Assertions.assertNull(DAOFacade.getFlower(expectedON));
//    }

    @Test
    public void flowerTest() throws SQLException{
        LocalDateTime dt = LocalDateTime.parse("2023-11-03T12:45");
        Time time = Time.valueOf(dt.toLocalTime());
        Flower flower = new Flower("Abby", "Amber", "Bob", "Barry", "Carol", "Cam",
                new Date(1696996464), time, "Hall 1 Level 2", "rose", 2, RequestStatus.BLANK);

        DAOFacade.addFlower(flower);
        List<Flower> allFlowers = DAOFacade.getAllFlower();
        int mostRecentNum = 0;
        for(Flower x : allFlowers){
            if(x.getOrderNum()>mostRecentNum){
                mostRecentNum = x.getOrderNum();
            }
        }
        Flower testFlower = DAOFacade.getFlower(mostRecentNum);
        Assertions.assertEquals("Abby", testFlower.getRequesterFirst());
        Assertions.assertEquals("Amber", testFlower.getRequesterLast());
        Assertions.assertEquals("Bob", testFlower.getPatientFirst());
        Assertions.assertEquals("Barry", testFlower.getPatientLast());
        Assertions.assertEquals("Carol", testFlower.getAssignedStaffFirst());
        Assertions.assertEquals("Cam", testFlower.getAssignedStaffLast());
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Assertions.assertEquals(formatter.format(new Date(1696996464)), formatter.format(testFlower.getDeliveryDate()));
        Assertions.assertEquals(time, testFlower.getDeliveryTime());
        Assertions.assertEquals("Hall 1 Level 2", testFlower.getLocation());
        Assertions.assertEquals("rose", testFlower.getItems());
        Assertions.assertEquals(2, testFlower.getTotal());
        Assertions.assertEquals("", testFlower.getStatus().toString());

        LocalDateTime dt2 = LocalDateTime.parse("2023-12-03T12:45");
        Time time2 = Time.valueOf(dt.toLocalTime());
        Flower updatedFlower = new Flower(mostRecentNum, "Sarah", "Lee", "Kelly", "Clark", "Daisy", "Donald",
                new Date(1696996485), time2, "Hall 1 Level 1", "sunflower", 4, RequestStatus.PROCESSING);
        DAOFacade.updateFlower(updatedFlower);
        Flower testFlowerUpdated = DAOFacade.getFlower(mostRecentNum);
        Assertions.assertEquals("Sarah", testFlowerUpdated.getRequesterFirst());
        Assertions.assertEquals("Lee", testFlowerUpdated.getRequesterLast());
        Assertions.assertEquals("Kelly", testFlowerUpdated.getPatientFirst());
        Assertions.assertEquals("Clark", testFlowerUpdated.getPatientLast());
        Assertions.assertEquals("Daisy", testFlowerUpdated.getAssignedStaffFirst());
        Assertions.assertEquals("Donald", testFlowerUpdated.getAssignedStaffLast());
        Assertions.assertEquals(formatter.format(new Date(1696996485)), formatter.format(testFlowerUpdated.getDeliveryDate()));
        Assertions.assertEquals(time2, testFlowerUpdated.getDeliveryTime());
        Assertions.assertEquals("Hall 1 Level 1", testFlowerUpdated.getLocation());
        Assertions.assertEquals("sunflower", testFlowerUpdated.getItems());
        Assertions.assertEquals(4, testFlowerUpdated.getTotal());
        Assertions.assertEquals("Processing", testFlowerUpdated.getStatus().toString());

        DAOFacade.deleteFlower(testFlowerUpdated);
        Assertions.assertNull(DAOFacade.getFlower(testFlower.getOrderNum()));
    }

    @Test
    public void mealTest() throws SQLException{
        LocalDateTime dt = LocalDateTime.parse("2023-11-03T12:45");
        Time time = Time.valueOf(dt.toLocalTime());
        Meal meal = new Meal("Abby", "Amber", "Bob", "Barry", "Carol", "Cam",
                new Date(1696996464), time, "Hall 1 Level 2", "burger", 2, RequestStatus.BLANK);

        DAOFacade.addMeal(meal);
        List<Meal> allMeals = DAOFacade.getAllMeal();
        int mostRecentNum = 0;
        for(Meal x : allMeals){
            if(x.getOrderNum()>mostRecentNum){
                mostRecentNum = x.getOrderNum();
            }
        }
        Meal testMeal = DAOFacade.getMeal(mostRecentNum);
        Assertions.assertEquals("Abby", testMeal.getRequesterFirst());
        Assertions.assertEquals("Amber", testMeal.getRequesterLast());
        Assertions.assertEquals("Bob", testMeal.getPatientFirst());
        Assertions.assertEquals("Barry", testMeal.getPatientLast());
        Assertions.assertEquals("Carol", testMeal.getAssignedStaffFirst());
        Assertions.assertEquals("Cam", testMeal.getAssignedStaffLast());
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Assertions.assertEquals(formatter.format(new Date(1696996464)), formatter.format(testMeal.getDeliveryDate()));
        Assertions.assertEquals(time, testMeal.getDeliveryTime());
        Assertions.assertEquals("Hall 1 Level 2", testMeal.getLocation());
        Assertions.assertEquals("burger", testMeal.getItems());
        Assertions.assertEquals(2, testMeal.getTotal());
        Assertions.assertEquals("", testMeal.getStatus().toString());

        LocalDateTime dt2 = LocalDateTime.parse("2023-12-03T12:45");
        Time time2 = Time.valueOf(dt.toLocalTime());
        Meal updatedMeal = new Meal(mostRecentNum, "Sarah", "Lee", "Kelly", "Clark", "Daisy", "Donald",
                new Date(1696996485), time2, "Hall 1 Level 1", "fries", 4, RequestStatus.PROCESSING);
        DAOFacade.updateMeal(updatedMeal);
        Meal testMealUpdated = DAOFacade.getMeal(mostRecentNum);
        Assertions.assertEquals("Sarah", testMealUpdated.getRequesterFirst());
        Assertions.assertEquals("Lee", testMealUpdated.getRequesterLast());
        Assertions.assertEquals("Kelly", testMealUpdated.getPatientFirst());
        Assertions.assertEquals("Clark", testMealUpdated.getPatientLast());
        Assertions.assertEquals("Daisy", testMealUpdated.getAssignedStaffFirst());
        Assertions.assertEquals("Donald", testMealUpdated.getAssignedStaffLast());
        Assertions.assertEquals(formatter.format(new Date(1696996485)), formatter.format(testMealUpdated.getDeliveryDate()));
        Assertions.assertEquals(time2, testMealUpdated.getDeliveryTime());
        Assertions.assertEquals("Hall 1 Level 1", testMealUpdated.getLocation());
        Assertions.assertEquals("fries", testMealUpdated.getItems());
        Assertions.assertEquals(4, testMealUpdated.getTotal());
        Assertions.assertEquals("Processing", testMealUpdated.getStatus().toString());

        DAOFacade.deleteMeal(testMealUpdated);
        Assertions.assertNull(DAOFacade.getMeal(testMeal.getOrderNum()));
    }

    @Test
    public void furnitureTest() throws SQLException{
        Furniture furniture = new Furniture("Abby", "Amber", "Carol", "Cam",
                new Date(1696996464), "Hall 1 Level 2", "chair",  RequestStatus.BLANK);

        DAOFacade.addFurniture(furniture);
        List<Furniture> allFurniture = DAOFacade.getAllFurniture();
        int mostRecentNum = 0;
        for(Furniture x : allFurniture){
            if(x.getOrderNum()>mostRecentNum){
                mostRecentNum = x.getOrderNum();
            }
        }
        Furniture testFurniture = DAOFacade.getFurniture(mostRecentNum);
        Assertions.assertEquals("Abby", testFurniture.getFirstName());
        Assertions.assertEquals("Amber", testFurniture.getLastName());
        Assertions.assertEquals("Carol", testFurniture.getAssignedStaffFirst());
        Assertions.assertEquals("Cam", testFurniture.getAssignedStaffLast());
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Assertions.assertEquals(formatter.format(new Date(1696996464)), formatter.format(testFurniture.getDeliveryDate()));
        Assertions.assertEquals("Hall 1 Level 2", testFurniture.getLocation());
        Assertions.assertEquals("chair", testFurniture.getItems());
        Assertions.assertEquals("", testFurniture.getStatus().toString());

        Furniture updatedFurniture = new Furniture(mostRecentNum, "Sarah", "Lee", "Daisy", "Donald",
                new Date(1696996485), "Hall 1 Level 1", "table", RequestStatus.PROCESSING);
        DAOFacade.updateFurniture(updatedFurniture);
        Furniture testFurnitureUpdated = DAOFacade.getFurniture(mostRecentNum);
        Assertions.assertEquals("Sarah", testFurnitureUpdated.getFirstName());
        Assertions.assertEquals("Lee", testFurnitureUpdated.getLastName());
        Assertions.assertEquals("Daisy", testFurnitureUpdated.getAssignedStaffFirst());
        Assertions.assertEquals("Donald", testFurnitureUpdated.getAssignedStaffLast());
        Assertions.assertEquals(formatter.format(new Date(1696996485)), formatter.format(testFurnitureUpdated.getDeliveryDate()));
        Assertions.assertEquals("Hall 1 Level 1", testFurnitureUpdated.getLocation());
        Assertions.assertEquals("table", testFurnitureUpdated.getItems());
        Assertions.assertEquals("Processing", testFurnitureUpdated.getStatus().toString());

        DAOFacade.deleteFurniture(testFurnitureUpdated);
        Assertions.assertNull(DAOFacade.getFurniture(testFurniture.getOrderNum()));
    }

    @Test
    public void locationNameTest() throws SQLException{
        LocationName locationName = new LocationName("MRI ROOM 1", "MRI ONE", "LABS");
        String locationNameLN = locationName.getLongName();

        LocationName updatedLocationName = new LocationName("MRI ROOM 1", "MRI 1", "LABS");
        String updatedLocationNameLN = locationName.getLongName();


        //insert
        DAOFacade.addLocationName(locationName);
        LocationName expected = DAOFacade.getLocationName("MRI ROOM 1");
        String expectedLN = expected.getLongName();
        Assertions.assertEquals(locationNameLN, expectedLN);
        DAOFacade.deleteLocationName(locationName);

        //get
        DAOFacade.addLocationName(locationName);
        expected = DAOFacade.getLocationName("MRI ROOM 1");
        expectedLN = expected.getLongName();
        Assertions.assertEquals(locationNameLN, expectedLN);
        DAOFacade.deleteLocationName(locationName);

        //update
        DAOFacade.addLocationName(locationName);
        DAOFacade.updateLocationName(updatedLocationName);
        expected = DAOFacade.getLocationName("MRI ROOM 1");
        expectedLN = expected.getLongName();
        Assertions.assertEquals(updatedLocationNameLN, expectedLN);
        DAOFacade.deleteLocationName(updatedLocationName);

        //delete
        DAOFacade.addLocationName(updatedLocationName);
        DAOFacade.deleteLocationName(updatedLocationName);
        Assertions.assertNull(DAOFacade.getLocationName("MRI ROOM 1"));

        LocationName locationTest = new LocationName("MRI ROOM 1", "MRI ONE", "LABS");
        DAOFacade.addLocationName(locationTest);
        LocationName loc = DAOFacade.getLocationName(locationTest.getLongName());
        Assertions.assertEquals("MRI ONE", loc.getShortName());
        Assertions.assertEquals("LABS", loc.getNodeType());
        loc.setShortName("MRI?");
        loc.setNodeType("STAI");
        Assertions.assertEquals("MRI?", loc.getShortName());
        Assertions.assertEquals("STAI", loc.getNodeType());
        DAOFacade.deleteLocationName(locationTest);
        Assertions.assertNull(DAOFacade.getLocationName("MRI ROOM 1"));
    }

//    @Test
//    public void mealTest() throws SQLException{
//        LocalDateTime dt = LocalDateTime.parse("2023-11-03T11:00");
//        Time time = Time.valueOf(dt.toLocalTime());
//        Meal meal = new Meal("Arnold", "Adams", "Betty", "Bo", "Catherine", "Chen",
//                new Date(1681444464), time, "MS Waiting", "tulip", 2, RequestStatus.BLANK);
//
//        Meal updatedMeal = new Meal("Arnold", "Adams", "Betty", "Bo", "Danny", "Dean",
//                new Date(1681444464), time, "MS Waiting", "tulip", 3, RequestStatus.PROCESSING);
//
//        //insert
//        DAOFacade.addMeal(meal);
//        List<Meal> expectedList = DAOFacade.getAllMeal();
//        Meal expected = expectedList.get(expectedList.size()-1);
//        int expectedON = expected.getOrderNum();
//        Assertions.assertEquals(meal.getRequesterFirst(), expected.getRequesterFirst());
//        Assertions.assertEquals(meal.getRequesterLast(), expected.getRequesterLast());
//        Assertions.assertEquals(meal.getPatientLast(), expected.getPatientLast());
//        Assertions.assertEquals(meal.getPatientFirst(), expected.getPatientFirst());
//        Assertions.assertEquals(meal.getAssignedStaffFirst(), expected.getAssignedStaffFirst());
//        Assertions.assertEquals(meal.getAssignedStaffLast(), expected.getAssignedStaffLast());
//        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
//        Assertions.assertEquals(formatter.format(meal.getDeliveryDate()),formatter.format(expected.getDeliveryDate()));
//        Assertions.assertEquals(meal.getDeliveryTime(), expected.getDeliveryTime());
//        Assertions.assertEquals(meal.getItems(), expected.getItems());
//        Assertions.assertEquals(meal.getLocation(), expected.getLocation());
//        Assertions.assertEquals(meal.getTotal(), expected.getTotal());
//        Assertions.assertEquals(meal.getStatus(), expected.getStatus());
//        DAOFacade.deleteMeal(DAOFacade.getMeal(expectedON));
//
//        //get
//        DAOFacade.addMeal(meal);
//        expectedList = DAOFacade.getAllMeal();
//        expectedON = expectedList.get(expectedList.size()-1).getOrderNum();
//        System.out.println(expectedON);
//        expected = DAOFacade.getMeal(expectedON);
//        Assertions.assertEquals(meal.getRequesterFirst(), expected.getRequesterFirst());
//        Assertions.assertEquals(meal.getRequesterLast(), expected.getRequesterLast());
//        Assertions.assertEquals(meal.getPatientLast(), expected.getPatientLast());
//        Assertions.assertEquals(meal.getPatientFirst(), expected.getPatientFirst());
//        Assertions.assertEquals(meal.getAssignedStaffFirst(), expected.getAssignedStaffFirst());
//        Assertions.assertEquals(meal.getAssignedStaffLast(), expected.getAssignedStaffLast());
//        formatter = new SimpleDateFormat("dd/MM/yyyy");
//        Assertions.assertEquals(formatter.format(meal.getDeliveryDate()),formatter.format(expected.getDeliveryDate()));
//        Assertions.assertEquals(meal.getDeliveryTime(), expected.getDeliveryTime());
//        Assertions.assertEquals(meal.getItems(), expected.getItems());
//        Assertions.assertEquals(meal.getLocation(), expected.getLocation());
//        Assertions.assertEquals(meal.getTotal(), expected.getTotal());
//        Assertions.assertEquals(meal.getStatus(), expected.getStatus());
//        DAOFacade.deleteMeal(DAOFacade.getMeal(expectedON));
//
//        //update
//        DAOFacade.addMeal(meal);
//        DAOFacade.updateMeal(updatedMeal);
//        expectedList = DAOFacade.getAllMeal();
//        expectedON = expectedList.get(expectedList.size()-1).getOrderNum();
//        expected = DAOFacade.getMeal(expectedON);
//        Assertions.assertEquals(meal.getRequesterFirst(), expected.getRequesterFirst());
//        Assertions.assertEquals(meal.getRequesterLast(), expected.getRequesterLast());
//        Assertions.assertEquals(meal.getPatientLast(), expected.getPatientLast());
//        Assertions.assertEquals(meal.getPatientFirst(), expected.getPatientFirst());
//        Assertions.assertEquals(meal.getAssignedStaffFirst(), expected.getAssignedStaffFirst());
//        Assertions.assertEquals(meal.getAssignedStaffLast(), expected.getAssignedStaffLast());
//        formatter = new SimpleDateFormat("dd/MM/yyyy");
//        Assertions.assertEquals(formatter.format(meal.getDeliveryDate()),formatter.format(expected.getDeliveryDate()));
//        Assertions.assertEquals(meal.getDeliveryTime(), expected.getDeliveryTime());
//        Assertions.assertEquals(meal.getItems(), expected.getItems());
//        Assertions.assertEquals(meal.getLocation(), expected.getLocation());
//        Assertions.assertEquals(meal.getTotal(), expected.getTotal());
//        Assertions.assertEquals(meal.getStatus(), expected.getStatus());
//        DAOFacade.deleteMeal(DAOFacade.getMeal(expectedON));
//
//        //delete
//        DAOFacade.addMeal(updatedMeal);
//        DAOFacade.deleteMeal(DAOFacade.getMeal(expectedON));
//        Assertions.assertNull(DAOFacade.getMeal(expectedON));
//    }

    @Test
    public void moveTest() throws SQLException{
        Node node = new Node(100, 1000, 500, "L1", "Tower");
        LocationName locationName = new LocationName("Neuroscience Waiting Room", "MRI ONE", "LABS");
        Date date = new Date(1676091264);
        Move move = new Move(node,locationName,date);
        DAOFacade.addMove(move);
        Move testMove = DAOFacade.getMove(node,locationName,date);
        Assertions.assertEquals(100,testMove.getNode().getNodeID());
        Assertions.assertEquals("Neuroscience Waiting Room",testMove.getLocation().getLongName());
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Assertions.assertEquals(formatter.format(date),formatter.format(testMove.getMoveDate()));
        DAOFacade.deleteMove(testMove);
        Assertions.assertNull(DAOFacade.getMove(node,locationName,date));
    }

    @Test
    public void nodeTest() throws SQLException{
        Node node = new Node(10, 1000, 500, "L1", "Tower");
        int nodeID = node.getNodeID();

        Node updatedNode = new Node(10, 1200, 600, "L1", "Tower");
        int updatedNodeID = node.getNodeID();

        Date date = new Date(1676091264);
        LocationName locationName = new LocationName("FOR TESTING", "TEST", "LAB");
        Move move = new Move(node, locationName, date);

        //insert
        DAOFacade.addNode(node);
        Node expected = DAOFacade.getNode(nodeID);
        int expectedID = expected.getNodeID();
        Assertions.assertEquals(nodeID, expectedID);
        DAOFacade.deleteNode(node);

        //get
        DAOFacade.addNode(node);
        expected = DAOFacade.getNode(nodeID);
        expectedID = expected.getNodeID();
        System.out.println(expectedID);
        Assertions.assertEquals(nodeID, expectedID);
        DAOFacade.deleteNode(node);

        //get
        DAOFacade.addNode(node);
        DAOFacade.addLocationName(locationName);
        DAOFacade.addMove(move);
        expected = DAOFacade.getNode("FOR TESTING", date);
        expectedID = expected.getNodeID();
        System.out.println(expectedID);
        Assertions.assertEquals(nodeID, expectedID);
        DAOFacade.deleteLocationName(locationName);
        DAOFacade.deleteMove(move);
        DAOFacade.deleteNode(node);

        //update
        DAOFacade.addNode(node);
        DAOFacade.updateNode(updatedNode);
        expected = DAOFacade.getNode(updatedNodeID);
        expectedID = expected.getNodeID();
        Assertions.assertEquals(updatedNodeID, expectedID);
        DAOFacade.deleteNode(node);

        //delete
        DAOFacade.addNode(node);
        DAOFacade.deleteNode(updatedNode);
        Assertions.assertNull(DAOFacade.getNode(updatedNodeID));

        Node nodeTest = new Node(10, 1000, 500, "L1", "Tower");
        DAOFacade.addNode(nodeTest);
        Node nod = DAOFacade.getNode(nodeTest.getNodeID());
        Assertions.assertEquals(1000,nod.getXcoord());
        Assertions.assertEquals(500,nod.getYcoord());
        Assertions.assertEquals("L1",nod.getFloor());
        Assertions.assertEquals("Tower",nod.getBuilding());
        nod.setXcoord(10);
        nod.setYcoord(10);
        nod.setFloor("L2");
        nod.setBuilding("Shapiro");
        Assertions.assertEquals(10,nod.getXcoord());
        Assertions.assertEquals(10,nod.getYcoord());
        Assertions.assertEquals("L2",nod.getFloor());
        Assertions.assertEquals("Shapiro",nod.getBuilding());
        DAOFacade.deleteNode(nodeTest);
        Assertions.assertNull(DAOFacade.getNode(10));
    }

    @Test
    public void requestOptionsTest() throws SQLException{
        RequestOptions requestOptions = new RequestOptions("Milk", 0.99, "Cafe", "Cafe is a fine eating establishment","Fresh Lowfat 1% milk", "Drink");
        String requestOptionsI = requestOptions.getItemName();
        String requestOptionsR = requestOptions.getRestaurant();

        RequestOptions updatedRequestOptions = new RequestOptions("Milk", 2.99, "Cafe", "Cafe is a fine eating establishment","Fresh Lowfat 1% milk", "Drink");

        //insert
        DAOFacade.addRequestOption(requestOptions);
        RequestOptions expected = DAOFacade.getRequestOptions("Milk", "Cafe");
        String expectedI = expected.getItemName();
        String expectedR = expected.getRestaurant();
        Assertions.assertTrue(expectedI.equals(requestOptionsI) && expectedR.equals(requestOptionsR));
        DAOFacade.deleteRequestOption(requestOptions);

        //get
        DAOFacade.addRequestOption(requestOptions);
        expected = DAOFacade.getRequestOptions("Milk", "Cafe");
        expectedI = expected.getItemName();
        expectedR = expected.getRestaurant();
        Assertions.assertTrue(expectedI.equals(requestOptionsI) && expectedR.equals(requestOptionsR));
        DAOFacade.deleteRequestOption(requestOptions);

        //update
        DAOFacade.addRequestOption(requestOptions);
        DAOFacade.updatePrice(updatedRequestOptions);
        expected = DAOFacade.getRequestOptions("Milk", "Cafe");
        expectedI = expected.getItemName();
        expectedR = expected.getRestaurant();
        Assertions.assertTrue(expectedI.equals(requestOptionsI) && expectedR.equals(requestOptionsR));
        DAOFacade.deleteRequestOption(updatedRequestOptions);

        //delete
        DAOFacade.addRequestOption(updatedRequestOptions);
        DAOFacade.deleteRequestOption(updatedRequestOptions);
        Assertions.assertNull(DAOFacade.getRequestOptions("Milk", "Cafe"));
    }

    @Test
    public void flowerRequestOptionsTest() throws SQLException{
        FlowerRequestOptions requestOptions = new FlowerRequestOptions("Flower", 0.99, "Shop", "Description", "Tropical", "itemDescription");

        DAOFacade.addFlowerRequestOption(requestOptions);
        FlowerRequestOptions testOption = DAOFacade.getFlowerRequestOptions("Flower", "Shop");
        Assertions.assertEquals("Flower", testOption.getItemName());
        Assertions.assertEquals(0.99, testOption.getPrice());
        Assertions.assertEquals("Shop", testOption.getShop());
        Assertions.assertEquals("Description", testOption.getShopDescription());
        Assertions.assertEquals("Tropical", testOption.getItemType());
        Assertions.assertEquals("itemDescription", testOption.getItemDescription());

        FlowerRequestOptions updatedOptions = new FlowerRequestOptions("Flower", 3.99, "Shop", "Description", "Tropical", "itemDescription");
        DAOFacade.updatePrice(updatedOptions);
        FlowerRequestOptions testUpdated = DAOFacade.getFlowerRequestOptions("Flower", "Shop");
        Assertions.assertEquals("Flower", testUpdated.getItemName());
        Assertions.assertEquals(3.99, testUpdated.getPrice());
        Assertions.assertEquals("Shop", testUpdated.getShop());

        DAOFacade.deleteRequestOption(updatedOptions);
        Assertions.assertNull(DAOFacade.getFlowerRequestOptions("Flower", "Shop"));
    }

    @Test
    public void furnitureRequestOptionsTest() throws SQLException{
        FurnitureRequestOptions requestOptions = new FurnitureRequestOptions("Sofa", "Seating", "For multi - sitting");

        DAOFacade.addFurnitureRequestOption(requestOptions);
        FurnitureRequestOptions testOption = DAOFacade.getFurnitureRequestOptions("Sofa");
        Assertions.assertEquals("Sofa", testOption.getItemName());
        Assertions.assertEquals("Seating", testOption.getItemType());
        Assertions.assertEquals("For multi - sitting", testOption.getItemDescription());

        DAOFacade.deleteFurnitureRequestOption(requestOptions);
        Assertions.assertNull(DAOFacade.getFurnitureRequestOptions("Sofa"));
    }

    @Test
    public void conferenceTest() throws SQLException{
        Conference conference = new Conference("Aaron", "Arch", new Date(1681444464),
                "user test/usertest@wpi.edu", 1, "BTM Conference Center", RequestStatus.BLANK);
        Conference updatedConference = new Conference("Aaron", "Arch", new Date(1681444464),
                "user test/usertest@wpi.edu,Vanessa Sam/vasam@wpi.edu", 2, "BTM Conference Center", RequestStatus.BLANK);
        int updatedConferenceON = updatedConference.getOrderNum();

        //insert
        DAOFacade.addConference(conference);
        List<Conference> expectedList = DAOFacade.getAllConference();
        int on = 0;
        for (Conference con : expectedList){
            if(con.getOrderNum() > on){
                on = con.getOrderNum();
            }
        }
        Conference expected = DAOFacade.getConference(on);
        Assertions.assertEquals(conference.getFirstName(), expected.getFirstName());
        Assertions.assertEquals(conference.getLastName(), expected.getLastName());
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Assertions.assertEquals(formatter.format(conference.getDate()),formatter.format(expected.getDate()));
        Assertions.assertEquals(conference.getAttendance(), expected.getAttendance());
        Assertions.assertEquals(conference.getExpectedSize(), expected.getExpectedSize());
        Assertions.assertEquals(conference.getLocation(), expected.getLocation());
        Assertions.assertEquals(conference.getStatus(), expected.getStatus());
        DAOFacade.deleteConference(conference);

        //get
        DAOFacade.addConference(conference);
        expectedList = DAOFacade.getAllConference();
        on = 0;
        for (Conference con : expectedList){
            if(con.getOrderNum() > on){
                on = con.getOrderNum();
            }
        }
        expected = DAOFacade.getConference(on);
        Assertions.assertEquals(conference.getFirstName(), expected.getFirstName());
        Assertions.assertEquals(conference.getLastName(), expected.getLastName());
        formatter = new SimpleDateFormat("dd/MM/yyyy");
        Assertions.assertEquals(formatter.format(conference.getDate()),formatter.format(expected.getDate()));
        Assertions.assertEquals(conference.getAttendance(), expected.getAttendance());
        Assertions.assertEquals(conference.getExpectedSize(), expected.getExpectedSize());
        Assertions.assertEquals(conference.getLocation(), expected.getLocation());
        Assertions.assertEquals(conference.getStatus(), expected.getStatus());
        DAOFacade.deleteConference(conference);

        //update
        DAOFacade.addConference(conference);
        DAOFacade.updateConference(updatedConference);
        expectedList = DAOFacade.getAllConference();
        on = 0;
        for (Conference con : expectedList){
            if(con.getOrderNum() > on){
                on = con.getOrderNum();
            }
        }
        expected = DAOFacade.getConference(on);
        Assertions.assertEquals(conference.getFirstName(), expected.getFirstName());
        Assertions.assertEquals(conference.getLastName(), expected.getLastName());
        formatter = new SimpleDateFormat("dd/MM/yyyy");
        Assertions.assertEquals(formatter.format(conference.getDate()),formatter.format(expected.getDate()));
        Assertions.assertEquals(conference.getAttendance(), expected.getAttendance());
        Assertions.assertEquals(conference.getExpectedSize(), expected.getExpectedSize());
        Assertions.assertEquals(conference.getLocation(), expected.getLocation());
        Assertions.assertEquals(conference.getStatus(), expected.getStatus());
        DAOFacade.deleteConference(conference);

        //delete
        DAOFacade.addConference(updatedConference);
        DAOFacade.deleteConference(DAOFacade.getConference(on));
        Assertions.assertNull(DAOFacade.getConference(on));
    }
}
