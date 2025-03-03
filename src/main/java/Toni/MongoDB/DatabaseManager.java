package Toni.MongoDB;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.set;

import Exceptions.DatabaseOpeningException;
import Utils.Constants;

/**
 * Manages the connection and operations with the MongoDB database.
 */
public class DatabaseManager {
    private MongoDatabase db;
    private MongoClient mongoClient;

    /**
     * Constructs a DatabaseManager and opens the database connection.
     */
    public DatabaseManager() {
        openDb();
    }

    /**
     * Opens the connection to the MongoDB database.
     * @throws DatabaseOpeningException if there is an error opening the database.
     */
    public void openDb(){
        try {
            // Acceder a una base de datos
            CodecRegistry pojoCodecRegistry = CodecRegistries.fromRegistries(
                    MongoClientSettings.getDefaultCodecRegistry(),
                    CodecRegistries.fromProviders(PojoCodecProvider.builder().automatic(true).build())
            );

            MongoClientSettings settings = MongoClientSettings.builder()
                    .codecRegistry(pojoCodecRegistry)
                    .applyConnectionString(new ConnectionString(Constants.MONGO_URI))
                    .build();
            mongoClient = MongoClients.create(settings);
            db = mongoClient.getDatabase(Constants.MONGO_DB_NAME);
            System.out.println("Conexión exitosa a MongoDB!");
        } catch (Exception e) {
            throw new DatabaseOpeningException("Error: " + e.getMessage());
        }
    }

    /**
     * Closes the connection to the MongoDB database.
     */
    public void closeDB(){
        mongoClient.close();
    }

    /**
     * Retrieves a collection from the database.
     * @param collectionName the name of the collection to retrieve.
     * @return the MongoCollection object.
     */
    public MongoCollection<Document> getCollection(String collectionName){
        return db.getCollection(collectionName);
    }

    /**
     * Retrieves a department document by its ID.
     * @param id the ID of the department.
     * @return the department document.
     */
    public Document getDepartamentDoc(int id){
        MongoCollection<Document> equiposCollection = getCollection(Constants.DEPARTMENT_COLLECTION);
        Document query = new Document("id",id);
        return equiposCollection.find(query).first();
    }

    /**
     * Saves a department to the database.
     * @param department the department to save.
     * @return true if the department was saved successfully, false otherwise.
     */
    public boolean saveDepartament(Department department){
        boolean result = false;
        MongoCollection<Document> depatmentCollection = getCollection(Constants.DEPARTMENT_COLLECTION);
        depatmentCollection.insertOne(department.toDocument());
        if (getDepartamentDoc(department.getDepartmentID())!=null){
            result = true;
        }
        return result;
    }

    /**
     * Updates a department's name and/or address.
     * @param id the ID of the department.
     * @param name the new name of the department.
     * @param address the new address of the department.
     */
    public void updateDepartment(int id, String name, String address ){
        Document query = new Document("id", id); //eq()
        Document update = new Document("name", name).append("address", address);
        Document setData = new Document("$set", update);
        getCollection(Constants.DEPARTMENT_COLLECTION).updateOne(query, setData);

    }

    /**
     * Deletes a department by its ID.
     * @param id the ID of the department to delete.
     */
    public void deleteDepartamento(int id) {
        Document filtro = new Document("id", id);
        getCollection(Constants.DEPARTMENT_COLLECTION).deleteOne(filtro);
    }

    /**
     * Retrieves all department documents from the database.
     * @return a list of all department documents.
     */
    public List<Document> getAllDepartments(){
        MongoCollection<Document> departmentCollection = getCollection(Constants.DEPARTMENT_COLLECTION);
        List<Document> departmentsDoc = new ArrayList<>();
        for (Document departmentDoc: departmentCollection.find()){
            departmentsDoc.add(departmentDoc);
        }
        return departmentsDoc;
    }

    /**
     * Deletes an employee by its ID.
     * @param id the ID of the employee to delete.
     * @return true if the employee was deleted successfully, false otherwise.
     */
    public boolean deleteEmployee(int id) {
        Document filtro = new Document("id", id);
        return getCollection(Constants.EMPLOYEE_COLLECTION).deleteOne(filtro).getDeletedCount() > 0;
    }

    /**
     * Retrieves an employee document by its ID.
     * @param id the ID of the employee.
     * @return the employee document.
     */
    public Document getEmployeeDocById(int id){
        MongoCollection<Document> equiposCollection = getCollection(Constants.EMPLOYEE_COLLECTION);
        Document query = new Document("id",id);
        return equiposCollection.find(query).first();
    }

    /**
     * Retrieves an employee document by its ID.
     * @param id the ID of the employee.
     * @return the employee document.
     */
    public Document getEmployeeDocByDepId(int id){
        MongoCollection<Document> equiposCollection = getCollection(Constants.EMPLOYEE_COLLECTION);
        Document query = new Document("departmentId",id);
        return equiposCollection.find(query).first();
    }

    /**
     * Updates an employee's name, position, and/or department.
     * @param id the ID of the employee.
     * @param name the new name of the employee.
     * @param position the new position of the employee.
     * @param department the new department of the employee.
     */
    public void updateEmployee(int id, String name, String position, Department department ){
        Document query = new Document("id", id);
        Document setData = new Document();

        if (!name.isEmpty()) {
            setData.append("name", name);
        }
        if (!position.isEmpty()) {
            setData.append("position", position);
        }
        if (department != null) {
            setData.append("department_id", department.getDepartmentID());
        }
        if (!setData.isEmpty()) {
            Document update = new Document("$set", setData);
            System.out.println(getCollection(Constants.EMPLOYEE_COLLECTION).updateOne(query,  update));
        }

    }

    /**
     * Retrieves all employee documents from the database.
     * @return a list of all employee documents.
     */
    public List<Document> getAllEmlpoyees(){
        MongoCollection<Document> departmentCollection = getCollection(Constants.EMPLOYEE_COLLECTION);
        List<Document> employeesDoc = new ArrayList<>();
        for (Document employeeDoc: departmentCollection.find()){
            employeesDoc.add(employeeDoc);
        }
        return employeesDoc;
    }
}
