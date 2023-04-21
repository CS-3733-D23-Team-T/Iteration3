package edu.wpi.tacticaltritons.database;

import edu.wpi.tacticaltritons.database.dao.ConferenceDao;
import edu.wpi.tacticaltritons.database.dao.FurnitureRequestOptionsDao;
import edu.wpi.tacticaltritons.database.daoImplementations.*;
import org.controlsfx.control.tableview2.filter.filtereditor.SouthFilter;

import java.sql.SQLException;
import java.util.ArrayList;
import java.sql.Date;
import java.util.List;

public class DAOFacade {
  static NodeDaoImpl nodeDao = new NodeDaoImpl();
  static EdgeDaoImpl edgeDao = new EdgeDaoImpl();
  static LocationNameDaoImpl locationNameDao = new LocationNameDaoImpl();
  static MoveDaoImpl moveDao = new MoveDaoImpl();
  static LoginDaoImpl loginDao = new LoginDaoImpl();
  static RequestOptionsDaoImpl requestOptionsDao = new RequestOptionsDaoImpl();
  static FlowerDaoImpl flowerDao = new FlowerDaoImpl();
  static MealDaoImpl mealDao = new MealDaoImpl();
  static SessionDaoImpl sessionDao = new SessionDaoImpl();
  static ConferenceDaoImpl conferenceDao = new ConferenceDaoImpl();
  static FlowerRequestOptionsDaoImpl flowerRequestOptionsDao = new FlowerRequestOptionsDaoImpl();
  static FurnitureDaoImpl furnitureDao = new FurnitureDaoImpl();
  static FurnitureRequestOptionsDaoImpl furnitureRequestOptionsDao = new FurnitureRequestOptionsDaoImpl();
  static HomeServiceRequestsDaoImpl homeServiceRequestsDao = new HomeServiceRequestsDaoImpl();
  static InvitationsDaoImpl invitationsDao = new InvitationsDaoImpl();

  public static void addNode(Node node) throws SQLException {
    nodeDao.insert(node);
  }

  public static void addEdge(Edge edge) throws SQLException {
    edgeDao.insert(edge);
  }

  public static void addLocationName(LocationName locationName) throws SQLException {
    locationNameDao.insert(locationName);
  }

  public static void addMove(Move move) throws SQLException {
    moveDao.insert(move);
  }

  public static void addLogin(Login login) throws SQLException {
    loginDao.insert(login);
  }

  public static void addFlower(Flower flower) throws SQLException {
    flowerDao.insert(flower);
  }

  public static void addMeal(Meal meal) throws SQLException {
    mealDao.insert(meal);
  }

  public static void deleteNode(Node node) throws SQLException {
    nodeDao.delete(node);
  }

  public static void deleteEdge(Edge edge) throws SQLException {
    edgeDao.delete(edge);
  }

  public static void deleteLocationName(LocationName locationName) throws SQLException {
    locationNameDao.delete(locationName);
  }

  public static void deleteMove(Move move) throws SQLException {
    moveDao.delete(move);
  }

  public static void deleteLogin(Login login) throws SQLException {
    loginDao.delete(login);
  }

  public static void deleteFlower(Flower flower) throws SQLException {
    flowerDao.delete(flower);
  }

  public static void deleteMeal(Meal meal) throws SQLException {
    mealDao.delete(meal);
  }

  public static void updateNode(Node node) throws SQLException {
    nodeDao.update(node);
  }

  public static void updateNode(Node oldNode, Node newNode) throws SQLException {
    nodeDao.update(oldNode, newNode);
  }

  public static void updateLocationName(LocationName locationName) throws SQLException {
    locationNameDao.update(locationName);
  }

  public static void updateLocationName(LocationName oldLocationName, LocationName newLocationName) throws SQLException {
    locationNameDao.update(oldLocationName, newLocationName);
  }

  public static void updateLogin(Login login) throws SQLException {
    loginDao.update(login);
  }

  public static void updateFlower(Flower flower) throws SQLException {
    flowerDao.update(flower);
  }

  public static void updateMeal(Meal meal) throws SQLException {
    mealDao.update(meal);
  }

  public static List<Node> getAllNodes() throws SQLException {
    return nodeDao.getAll();
  }

  public static List<Edge> getAllEdges() throws SQLException {
    return edgeDao.getAll();
  }

  public static List<LocationName> getAllLocationNames() throws SQLException {
    return locationNameDao.getAll();
  }

  public static List<Move> getAllMoves() throws SQLException {
    return moveDao.getAll();
  }

  public static List<Login> getAllLogins() throws SQLException {
    return loginDao.getAll();
  }

  public static List<Flower> getAllFlower() throws SQLException {
    return flowerDao.getAll();
  }

  public static List<Meal> getAllMeal() throws SQLException {
    return mealDao.getAll();
  }

  public static Node getNode(int nodeID) throws SQLException {
    return nodeDao.get(nodeID);
  }

  public static Node getNode(String locationName, Date date) throws SQLException {
    return nodeDao.get(locationName, date);
  }

  public static LocationName getItemsLocationName(String locationName) throws SQLException {
    return locationNameDao.get(locationName);
  }

  public static Login getLogin(String username) throws SQLException {
    return loginDao.get(username);
  }

  public static List<RequestOptions> getAllOptions(String restaurant) throws SQLException {
    return requestOptionsDao.getFromRestaurant(restaurant);
  }

  public static List<RequestOptions> getAllOptions() throws SQLException {
    return requestOptionsDao.getAll();
  }

  public static void addRequestOption(RequestOptions requestOptions) throws SQLException {
    requestOptionsDao.insert(requestOptions);
  }

  public static void updatePrice(RequestOptions requestOptions) throws SQLException {
    requestOptionsDao.updatePrice(requestOptions);
  }

  public static void deleteRequestOption(RequestOptions requestOptions) throws SQLException {
    requestOptionsDao.delete(requestOptions);
  }

  public static Flower getFlower(int orderNum) throws SQLException {
    return flowerDao.get(orderNum);
  }

  public static LocationName getLocationName(String locationName) throws SQLException {
    return locationNameDao.get(locationName);
  }

  public static Edge getEdge(Node startNode, Node endNode) throws SQLException {
    return edgeDao.get(startNode, endNode);
  }

  public static RequestOptions getRequestOptions(String itemName, String restaurant)
      throws SQLException {
    return requestOptionsDao.get(itemName, restaurant);
  }

  public static Move getMove(Node node, LocationName location, Date moveDate) throws SQLException {
    return moveDao.get(node, location, moveDate);
  }

  public static Meal getMeal(int orderNum) throws SQLException {
    return mealDao.get(orderNum);
  }

  public static void addConference(Conference conference) throws SQLException {
    conferenceDao.insert(conference);
  }

  public static List<Conference> getAllConference() throws SQLException {
    return conferenceDao.getAll();
  }

  public static void updateConference(Conference conference) throws SQLException {
    conferenceDao.update(conference);
  }

  public static void deleteConference(Conference conference) throws SQLException {
    conferenceDao.delete(conference);
  }

  public static Conference getConference(int orderNum) throws SQLException {
    return conferenceDao.get(orderNum);
  }

  public static Session getSession(String username) throws SQLException {
    return sessionDao.get(username);
  }

  public static List<Session> getAllSessions() throws SQLException {
    return sessionDao.getAll();
  }

  public static void addSession(Session session) throws SQLException {
    sessionDao.insert(session);
  }

  public static void updateSession(Session session) throws SQLException {
    sessionDao.update(session);
  }

  public static void deleteSession(Session session) throws SQLException {
    sessionDao.delete(session);
  }
  public static FlowerRequestOptions getFlowerRequestOptions(String itemName, String shop)
          throws SQLException {
    return flowerRequestOptionsDao.get(itemName, shop);
  }
  public static List<FlowerRequestOptions> getAllShopFlowerRequestOptions(String shop) throws SQLException {
    return flowerRequestOptionsDao.getFromShop(shop);
  }

  public static List<FlowerRequestOptions> getAllFlowerRequestOptions() throws SQLException {
    return flowerRequestOptionsDao.getAll();
  }

  public static void addFlowerRequestOption(FlowerRequestOptions flowerRequestOptions) throws SQLException {
    flowerRequestOptionsDao.insert(flowerRequestOptions);
  }

  public static void updatePrice(FlowerRequestOptions flowerRequestOptions) throws SQLException {
    flowerRequestOptionsDao.updatePrice(flowerRequestOptions);
  }

  public static void deleteRequestOption(FlowerRequestOptions flowerRequestOptions) throws SQLException {
    flowerRequestOptionsDao.delete(flowerRequestOptions);
  }
  public static FurnitureRequestOptions getFurnitureRequestOptions(String itemName)
          throws SQLException {
    return furnitureRequestOptionsDao.get(itemName);
  }

  public static List<FurnitureRequestOptions> getAllFurnitureRequestOptions() throws SQLException {
    return furnitureRequestOptionsDao.getAll();
  }

  public static void addFurnitureRequestOption(FurnitureRequestOptions furnitureRequestOptions) throws SQLException {
    furnitureRequestOptionsDao.insert(furnitureRequestOptions);
  }

  public static void deleteFurnitureRequestOption(FurnitureRequestOptions furnitureRequestOptions) throws SQLException {
    furnitureRequestOptionsDao.delete(furnitureRequestOptions);
  }

  public static void addFurniture(Furniture furniture) throws SQLException {
    furnitureDao.insert(furniture);
  }

  public static List<Furniture> getAllFurniture() throws SQLException {
    return furnitureDao.getAll();
  }

  public static void updateFurniture(Furniture furniture) throws SQLException {
    furnitureDao.update(furniture);
  }

  public static void deleteFurniture(Furniture furniture) throws SQLException {
    furnitureDao.delete(furniture);
  }

  public static Furniture getFurniture(int orderNum) throws SQLException {
    return furnitureDao.get(orderNum);
  }

  public static List<HomeServiceRequests> getSessionServiceRequests(String firstName, String lastName) throws SQLException{
    return homeServiceRequestsDao.getAll(firstName,lastName);
  }

  public static List<Move> getAllCurrentMoves(Date currentDate) throws SQLException{
    return moveDao.getAllCurrent(currentDate);
  }

  public static List<Move> getAllFutureMoves(Date currentDate) throws SQLException{
    return moveDao.getAllFuture(currentDate);
  }

  public static List<Invitations> getAllSessionInvitations(String firstName, String lastName, Date currentDate) throws SQLException{
    return invitationsDao.getAll(firstName,lastName,currentDate);
  }

  public static void addInvitation(Invitations invitation) throws SQLException{
    invitationsDao.insert(invitation);
  }

  public static void updateInvitation(Invitations invitation) throws SQLException {
    invitationsDao.update(invitation);
  }
}
