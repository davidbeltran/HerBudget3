package aftergrad.herbudget;

import java.util.ArrayList;
import java.io.File;
import org.apache.pdfbox.pdmodel.PDDocument; 
import org.apache.pdfbox.text.PDFTextStripper; 
import java.io.IOException; 
import java.util.regex.Pattern; 
import java.util.regex.Matcher;

/**
 *
 * @author David Beltran
 */
public class Statement {
    private String pdfPath;
    private String pdfText;
    private String regexPattern;
    private final ArrayList<ArrayList> expenses;
    
    public Statement(String pdfPath) {
        this.pdfPath = pdfPath;
        this.regexPattern = "(?:\\n((?:0[1-9]|1[1,2])/(?:0[1-9]|[12][0-9]|3[01]))\\s*(.+)" +
            " ((?:-\\d+\\.\\d{2})|(?:\\d+\\.\\d{2})))";
        this.pdfText = "";
        this.expenses = new ArrayList<>();
    }
    
    private String preparePdf(String pdfPath) throws IOException{
        File file = new File(pdfPath);
        try (PDDocument doc = PDDocument.load(file)) {
            PDFTextStripper stripper = new PDFTextStripper();
            this.pdfText = stripper.getText(doc);
            doc.close();
        }
        return pdfText;
    }

    private ArrayList<ArrayList> createExpenseList() throws IOException {
        Pattern pat = Pattern.compile(this.regexPattern);
        Matcher mat = pat.matcher(preparePdf(this.pdfPath));
        while (mat.find()) {
            ArrayList temp = new ArrayList<>();
            temp.add(mat.group(1));
            temp.add(mat.group(2));
            temp.add(Double.valueOf(mat.group(3)));
            this.expenses.add(temp);
        }
        return this.expenses;
    }
    
    public void getExpenses() throws IOException{
        createExpenseList();
        for (ArrayList exp : this.expenses){
            System.out.printf("Date: %s, Detail: %s, Amount: %.2f\n", 
                    exp.get(0), exp.get(1), exp.get(2));
        }
    }
    
    public void sendToDatabase() throws IOException{
        createExpenseList();
        Database db = new Database(this.expenses);
        db.fillMongoDB();
    }
    
    public String getPdfPath() {
        return pdfPath;
    }

    public void setPdfPath(String pdfPath) {
        this.pdfPath = pdfPath;
    }

    public String getRegexPattern() {
        return regexPattern;
    }

    public void setRegexPattern(String regexPattern) {
        this.regexPattern = regexPattern;
    }
}
