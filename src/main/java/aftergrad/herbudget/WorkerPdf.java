/**
 *
 * @author David Beltran
 */
package aftergrad.herbudget;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

public class WorkerPdf {
    private final String FileStorage;
    private final String ReDetail;
    private final String ReYear;
    private final String PdfDoc;
    private String CurrentYear;
    
    public WorkerPdf (String FileStorage, String PdfDoc) {
        this.FileStorage = FileStorage;
        this.PdfDoc = PdfDoc;
        this.ReDetail = "(?:\\n((?:0[1-9]|1[1,2])/(?:0[1-9]|[12][0-9]|3[01]))\\s*(.+)" +
            " ((?:-\\d+\\.\\d{2})|(?:\\d+\\.\\d{2})))";
        this.ReYear = "\\d{2}";
        this.CurrentYear = "";
    }
    
    /*
    Searches text file with PDF file names for duplicates.
    Registers the PDF file name's year to be used for expense date storage
    */
    private boolean searchPdf(String file) throws IOException {
        Path fileName = Path.of(file);
        String fileContent = Files.readString(fileName);
        Pattern rgxPat = Pattern.compile(this.PdfDoc);
        Matcher mat = rgxPat.matcher(fileContent);
        if (mat.find()) {
            return true;
        }
        rgxPat = Pattern.compile(this.ReYear);
        mat = rgxPat.matcher(this.PdfDoc);
        while (mat.find()) {
            this.CurrentYear = mat.group();
        }
        return false;
    }
    
    /*
    Checks if PDF file has been processed. If not processed the PDF file name is 
    stored in a text file to ensure only new data is stored in MongoDB
    */
    public boolean checkDuplicatePdf() throws IOException{
        try {
            File idStore = new File(this.FileStorage);
            if (!idStore.exists()){
                idStore.createNewFile();
            }
            if (!searchPdf(this.FileStorage)) {
                try (FileWriter fw = new FileWriter(this.FileStorage, true)) {
                    fw.write(this.PdfDoc + "\n");
                    fw.close();
                }
                return false;
            } else {
                System.out.printf("%s has already been processed.", this.PdfDoc);
            }
        } catch (IOException e){
            System.out.println(e.getMessage());
        }
        return true;
    }
    
    /*
    Strips PDF file and returns a string with PDF text
    */
    private String processPdf() throws IOException{
        File file = new File(this.PdfDoc);
        String PdfText = "";
        try (PDDocument doc = PDDocument.load(file)) {
            PDFTextStripper stripper = new PDFTextStripper();
            PdfText = stripper.getText(doc);
            doc.close();
        }
        return PdfText;
    }
    
    /*
    Regex used to find pattern of expense information and stripped.
    Information is stored in ArrayList and returned to send to Database object
    */
    public ArrayList<ArrayList> createExpenseList() throws IOException {
        Pattern pat = Pattern.compile(this.ReDetail);
        Matcher mat = pat.matcher(processPdf());
        ArrayList expenses = new ArrayList<>();
        while (mat.find()) {
            ArrayList temp = new ArrayList<>();
            String strDate = mat.group(1) + "/" + this.CurrentYear;
            LocalDate locDate = LocalDate.parse(strDate, DateTimeFormatter.ofPattern("MM/dd/yy"));
            temp.add(locDate);
            temp.add(mat.group(2));
            temp.add(Double.valueOf(mat.group(3)));
            expenses.add(temp);
        }
        return expenses;
    }
}
