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
import java.util.Random;
/**
 *
 * @author David Beltran
 */
public class Database {
    private final ArrayList<ArrayList> expenseList;
    private final String uri;
    private final Random rand;
    
    public Database(ArrayList expenseList) {
        this.expenseList = expenseList;
        this.uri = "mongodb+srv://beltrannowd5:Diska1725!@herbudgetclusterjava" +
                ".f2hiz7o.mongodb.net/?retryWrites=true&w=majority&appName=HerBudgetClusterJava";
        this.rand = new Random();
    }
    
    private String generateMongoID(String date, double amount)
    {
        int randInt = this.rand.nextInt(1000);
        int numID = (Math.abs((int) amount)) + randInt;
        return date + numID;
    }
    
    private List<Document> prepareMongoDoc() {
        List<Document> docs = new ArrayList<>();
        //int id = 1;
        for (ArrayList exp : this.expenseList) {
            String id = generateMongoID(exp.get(0), exp.get(2));
            docs.add(new Document("_id", id).append("Date", exp.get(0))
                    .append("Details", exp.get(1)).append("Amount", exp.get(2)));
            //id++;
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
//                ex.getWriteResult().getInserts()
//                        .forEach(doc -> insertedIds.add(doc.getId().asInt32().getValue()));
//                System.out.println("Duplicate entries found. " + 
//                    "successfully processed documents with the following ids: " + insertedIds);
//                System.out.println("message: " + ex.getMessage());
                
                for (Integer ids : insertedIds) {
                    System.out.println(ids);
                }
            }
        }
    }
}


/*
mongodb+srv://beltrannowd5:Diska1725!@herbudgetclusterjava.f2hiz7o.mongodb.net/?retryWrites=true&w=majority&appName=HerBudgetClusterJava
*/