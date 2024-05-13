package aftergrad.herbudget;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import static com.mongodb.client.model.Filters.eq;
import java.io.IOException;
import org.bson.Document;
/**
 *
 * @author David Beltran
 */
public class Main {
    public static void main(String[] args) throws IOException {
        //Statement statement = new Statement("NovDec.pdf");
        //statement.getExpenses();
        
        // Replace the placeholder with your MongoDB deployment's connection string
        String uri = "mongodb+srv://beltrannowd5:Diska1725!@herbudgetclusterjava.f2hiz7o.mongodb.net/?retryWrites=true&w=majority&appName=HerBudgetClusterJava";
        try (MongoClient mongoClient = MongoClients.create(uri)) {
            MongoDatabase database = mongoClient.getDatabase("sample_mflix");
            MongoCollection<Document> collection = database.getCollection("movies");
            Document doc = collection.find(eq("title", "Back to the Future")).first();
            if (doc != null) {
                System.out.println(doc.toJson());
            } else {
                System.out.println("No matching documents found.");
            }
        }
        
        
    }
}
