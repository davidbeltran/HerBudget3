/**
 *
 * @author David Beltran
 */
package aftergrad.herbudget;

import java.util.ArrayList;
import java.io.File;
import java.io.FileWriter;
import org.apache.pdfbox.pdmodel.PDDocument; 
import org.apache.pdfbox.text.PDFTextStripper; 
import java.io.IOException; 
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern; 
import java.util.regex.Matcher;

public class Statement {
    private String pdfPath;
    private String pdfText;
    private String regexPattern;
    private final ArrayList<ArrayList> expenses;
    private String year;
    
    /*
    Statement class constructor
    */
    public Statement(String pdfPath) {
        this.pdfPath = pdfPath;
        this.regexPattern = "(?:\\n((?:0[1-9]|1[1,2])/(?:0[1-9]|[12][0-9]|3[01]))\\s*(.+)" +
            " ((?:-\\d+\\.\\d{2})|(?:\\d+\\.\\d{2})))";
        this.pdfText = "";
        this.expenses = new ArrayList<>();
    }
    
    /*
    Strips PDF file and returns a string with PDF text
    */
    private String processPdf(String pdfPath) throws IOException{
        File file = new File(pdfPath);
        try (PDDocument doc = PDDocument.load(file)) {
            PDFTextStripper stripper = new PDFTextStripper();
            this.pdfText = stripper.getText(doc);
            doc.close();
        }
        return pdfText;
    }
    
    /*
    Checks if PDF file has been processed. If not processed the PDF file name is 
    stored in a text file to ensure only new data is stored in MongoDB
    */
    private boolean checkDuplicatePdf() throws IOException{
        String file = "idStore.txt";
        try {
            File idStore = new File(file);
            if (!idStore.exists()){
                idStore.createNewFile();
            }
            if (!searchPdf(file)) {
                try (FileWriter fw = new FileWriter(file, true)) {
                    fw.write(this.pdfPath + "\n");
                    fw.close();
                }
                return false;
            } else {
                System.out.printf("%s has already been processed.", this.pdfPath);
            }
        } catch (IOException e){
            System.out.println(e.getMessage());
        }
        return true;
    }
    
    /*
    Searches text file with PDF file names for duplicates.
    Registers the PDF file name's year to be used for expense date storage
    */
    private boolean searchPdf(String file) throws IOException {
        Path fileName = Path.of(file);
        String fileContent = Files.readString(fileName);
        Pattern rgxPat = Pattern.compile(this.pdfPath);
        Matcher mat = rgxPat.matcher(fileContent);
        if (mat.find()) {
            return true;
        }
        rgxPat = Pattern.compile("\\d{2}");
        mat = rgxPat.matcher(this.pdfPath);
        while (mat.find()) {
            this.year = mat.group();
        }
        return false;
    }

    /*
    Regex used to find pattern of expense information and stripped.
    Information is stored in ArrayList and returned to send to Database object
    */
    private ArrayList<ArrayList> createExpenseList() throws IOException {
        Pattern pat = Pattern.compile(this.regexPattern);
        Matcher mat = pat.matcher(processPdf(this.pdfPath));
        while (mat.find()) {
            ArrayList temp = new ArrayList<>();
            String strDate = mat.group(1) + "/" + this.year;
            LocalDate locDate = LocalDate.parse(strDate, DateTimeFormatter.ofPattern("MM/dd/yy"));
            temp.add(locDate);
            temp.add(mat.group(2));
            temp.add(Double.valueOf(mat.group(3)));
            this.expenses.add(temp);
        }
        return this.expenses;
    }
    
    /*
    Displays expense information to console
    */
    public void getExpenses() throws IOException{
        createExpenseList();
        for (ArrayList exp : this.expenses){
            System.out.printf("Date: %s, Detail: %s, Amount: %.2f\n", 
                    exp.get(0), exp.get(1), exp.get(2));
        }
    }
    
    /*
    Sends ArrayList of PDF stripped expenses to Database object
    */
    public void sendToDatabase() throws IOException{
        if (!checkDuplicatePdf()) {
            createExpenseList();
            Database db = new Database(this.expenses);
            db.fillMongoDB();
        }
    }
    
    /*
    Getters and setters
    */
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
    
    /*
    tester method
    */
    public void practice() throws IOException {
        
    }
}
