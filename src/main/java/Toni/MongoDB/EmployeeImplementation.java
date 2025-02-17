package Toni.MongoDB;

import DAO.Interfaces.EmployeeDAO;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class EmployeeImplementation implements EmployeeDAO {
    private AccessDB dbAccess;
    private final Scanner scanner;

    public EmployeeImplementation() {
        dbAccess = new AccessDB();
        scanner = new Scanner(System.in);
    }

    @Override
    public void addEmployee(Models.Employee employee) {
        MongoCollection<Document> equiposCollection = dbAccess.getCollection("departamento");
        Employee dep = new Employee(employee);
        equiposCollection.insertOne(dep.toDocument());
    }


    @Override
    public Optional<Models.Employee> updateEmployee(Object id) {
        return Optional.empty();
    }

    @Override
    public boolean deleteEmployee(Object id) {
        return false;
    }

    @Override
    public Optional<Models.Employee> findEmployeeByID(Object id) {
        return Optional.empty();
    }

    @Override
    public List<Models.Employee> findAllEmployees() {
        return List.of();
    }
}
