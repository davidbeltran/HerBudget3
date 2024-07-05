/**
 *
 * @author David Beltran
 */
package aftergrad.herbudget;

import java.io.IOException; 

public class Statement {
    private final String pdfPath;
    
    /*
    Statement class constructor
    */
    public Statement(String pdfPath) {
        this.pdfPath = pdfPath;
    }
    
    /*
    Sends ArrayList of PDF stripped expenses to Database object
    */
    public void sendToDatabase() throws IOException{
        WorkerPdf wp = new WorkerPdf("idStore.txt", this.pdfPath);
        if (!wp.checkDuplicatePdf()) {
            Database db = new Database(wp.createExpenseList());
            db.fillMongoDB();
        }
    }
}
