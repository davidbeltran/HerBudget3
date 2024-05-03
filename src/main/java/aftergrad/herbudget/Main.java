package aftergrad.herbudget;


import java.io.File; 
import java.io.IOException;
import org.apache.pdfbox.pdmodel.PDDocument; 
import org.apache.pdfbox.text.PDFTextStripper; 
import java.util.regex.*;
import java.util.ArrayList;
/**
 *
 * @author David Beltran
 */
public class Main {
    public static void main(String[] args) throws IOException {
        File file = new File("NovDec.pdf");
        PDDocument document = PDDocument.load(file);
        PDFTextStripper stripper = new PDFTextStripper();
        String text = stripper.getText(document);
        document.close();
        String regPat = "(?:\\n((?:0[1-9]|1[1,2])/(?:0[1-9]|[12][0-9]|3[01]))\\s*(.+)" +
            " ((?:-\\d+\\.\\d{2})|(?:\\d+\\.\\d{2})))";
        Pattern pat = Pattern.compile(regPat);
        Matcher mat = pat.matcher(text);
        
        ArrayList<ArrayList> expenses = new ArrayList<>();
        while (mat.find()) {
            ArrayList temp = new ArrayList<>();
            temp.add(mat.group(1));
            temp.add(mat.group(2));
            temp.add(Double.valueOf(mat.group(3)));
            expenses.add(temp);
        }
        
        for (ArrayList exp : expenses){
            System.out.printf("Date: %s, Detail: %s, Amount: %.2f\n", exp.get(0), exp.get(1), exp.get(2));
        }
    }
}
