package aftergrad.herbudget;

import com.mongodb.MongoBulkWriteException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.result.InsertManyResult;
import org.bson.Document;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author David Beltran
 */
public class Database {
    private final ArrayList<ArrayList> expenseList;
    private final String uri;
    
    public Database(ArrayList expenseList) {
        this.expenseList = expenseList;
        this.uri = "mongodb+srv://beltrannowd5:Diska1725!@herbudgetclusterjava" +
                ".f2hiz7o.mongodb.net/?retryWrites=true&w=majority&appName=HerBudgetClusterJava";
    }
    
    private List<Document> prepareMongoDoc() {
        List<Document> docs = new ArrayList<>();
        int id = 1;
        for (ArrayList exp : this.expenseList) {
            docs.add(new Document("_id", id).append("Date", exp.get(0))
                    .append("Details", exp.get(1)).append("Amount", exp.get(2)));
            id++;
        }
        return docs;
    }
    
    public void fillMongoDB() {
        List<Integer> insertedIds = new ArrayList<>();
        try (MongoClient client = MongoClients.create(this.uri)) {
            MongoDatabase db = client.getDatabase("HerBudget");
            MongoCollection<Document> coll = db.getCollection("Expenses");
            try {
                InsertManyResult result = coll.insertMany(prepareMongoDoc());
                result.getInsertedIds().values()
                        .forEach(doc -> insertedIds.add(doc.asInt32().getValue()));
                System.out.println("Inserted documents with the following ids: " + insertedIds);
            }
            catch(MongoBulkWriteException ex) {
                ex.getWriteResult().getInserts()
                        .forEach(doc -> insertedIds.add(doc.getId().asInt32().getValue()));
                System.out.println("Duplicate entries found. " + 
                    "successfully processed documents with the following ids: " + insertedIds);
                System.out.println("THIS!! " + ex.getWriteResult().getInserts());
            }
        }
    }
}
// 


/*

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoException;
import com.mongodb.ServerApi;
import com.mongodb.ServerApiVersion;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
public class MongoClientConnectionExample {
    public static void main(String[] args) {
        String connectionString = "mongodb+srv://beltrannowd5:Diska1725!@herbudgetclusterjava.f2hiz7o.mongodb.net/?retryWrites=true&w=majority&appName=HerBudgetClusterJava";
        ServerApi serverApi = ServerApi.builder()
                .version(ServerApiVersion.V1)
                .build();
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString(connectionString))
                .serverApi(serverApi)
                .build();
        // Create a new client and connect to the server
        try (MongoClient mongoClient = MongoClients.create(settings)) {
            try {
                // Send a ping to confirm a successful connection
                MongoDatabase database = mongoClient.getDatabase("admin");
                database.runCommand(new Document("ping", 1));
                System.out.println("Pinged your deployment. You successfully connected to MongoDB!");
            } catch (MongoException e) {
                e.printStackTrace();
            }
        }
    }
}



mongodb+srv://beltrannowd5:Diska1725!@herbudgetclusterjava.f2hiz7o.mongodb.net/?retryWrites=true&w=majority&appName=HerBudgetClusterJava
*/