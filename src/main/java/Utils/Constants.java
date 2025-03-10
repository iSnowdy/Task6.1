package Utils;

public class Constants {
    //PLAiN TEXT
    public static final String FILE_PATH = "src/main/resources";
    public static final String FILE_NAME = "%s/estructuraFichero.txt".formatted(FILE_PATH);
    public static final String ENTITY_DEPARTMENT = "departments";
    public static final String ENTITY_EMPLOYEE = "employees";

    //RELATIONAL
    //HIBERNATE
    //DB4O CONSTANTS
    public static final String DB40_DB_NAME = "%s/Company_DB4O.db4o".formatted(FILE_PATH);
    //MONGO CONSTANTS
    public static final String MONGO_URI = "mongodb+srv://Furbol:Furbol@futbolcluster.javn8.mongodb.net/?retryWrites=true&w=majority&appName=FutbolCluster&connectTimeoutMS=60000&socketTimeoutMS=60000"; // TODO strongly recommend to use tour own MongoDB
    public static final String MONGO_DB_NAME = "dep_emp";
    public static final String DEPARTMENT_COLLECTION = "DEPARTMENT";
    public static final String EMPLOYEE_COLLECTION = "EMPLOYEE";

}

