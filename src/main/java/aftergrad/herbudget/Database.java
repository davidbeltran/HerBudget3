/**
 *
 * @author David Beltran
 */
package aftergrad.herbudget;

import com.mongodb.MongoBulkWriteException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import java.util.ArrayList;
import java.util.List;

public class Database {
    private final ArrayList<ArrayList> expenseList;
    private final String uri;
    
    /*
    Database class constructor
    */
    public Database(ArrayList expenseList) {
        this.expenseList = expenseList;
        this.uri = "mongodb+srv://beltrannowd5:Diska1725!@herbudgetclusterjava" +
                ".f2hiz7o.mongodb.net/?retryWrites=true&w=majority&appName=HerBudgetClusterJava";
    }
    
    /*
    Fills ArrayList with Document type expense objects needed for MongoDB storage 
    */
    private List<Document> prepareMongoDoc() {
        List<Document> docs = new ArrayList<>();
        for (ArrayList exp : this.expenseList) {
            docs.add(new Document("Date", exp.get(0)).append("Details", exp.get(1))
                    .append("Amount", exp.get(2)));
        }
        return docs;
    }
    
    /*
    Populates MongoDB storage with ArrayList of expense Document type objects
    */
    public void fillMongoDB() {
        try (MongoClient client = MongoClients.create(this.uri)) {
            MongoDatabase db = client.getDatabase("HerBudget");
            MongoCollection<Document> coll = db.getCollection("Expenses");
            try {
                coll.insertMany(prepareMongoDoc());
            }
            catch(MongoBulkWriteException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }
}