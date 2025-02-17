package Toni.MongoDB;

import Exceptions.DatabaseOpeningException;
import Utils.Constants;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

public class DatabaseManager {
    private MongoDatabase db;
    private MongoClient mongoClient;
    public DatabaseManager() {
        openDb();
    }
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

    public void closeDb(){
        mongoClient.close();
    }

    public MongoCollection<Document> getCollection(String collectionName){
        return db.getCollection(collectionName);
    }

    public Document getDepartamentDoc(int id){
        MongoCollection<Document> equiposCollection = getCollection(Constants.DEPARTMENT_COLLECTION);
        Document query = new Document("id",id);
        return equiposCollection.find(query).first();
    }

    public boolean saveDepartament(Department department){
        boolean result = false;
        MongoCollection<Document> depatmentCollection = getCollection(Constants.DEPARTMENT_COLLECTION);
        depatmentCollection.insertOne(department.toDocument());
        if (getDepartamentDoc(department.getDepartmentID())!=null){
            result = true;
        }
        return result;
    }

    public void updateDepartment(int id, String name, String address ){
        Document query = new Document("id", id); //eq()
        Document update; // set()
        if (name.isEmpty()){
            update = new Document("address", address);
        } else if (address.isEmpty()) {
            update = new Document("name", name);
        } else  {
            update = new Document("name", name).append("address", address);
        }
        getCollection(Constants.DEPARTMENT_COLLECTION).updateOne(eq(query), update);

    }

    public void deleteDepartamento(int id) {
        Document filtro = new Document("id", id);
        getCollection(Constants.DEPARTMENT_COLLECTION).deleteOne(eq(filtro));
    }

    public List<Document> getAllDepartments(){
        MongoCollection<Document> departmentCollection = getCollection(Constants.DEPARTMENT_COLLECTION);
        List<Document> departmentsDoc = new ArrayList<>();
        for (Document departmentDoc: departmentCollection.find()){
            departmentsDoc.add(departmentDoc);
        }
        return departmentsDoc;
    }
}
