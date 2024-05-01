package aftergrad.herbudget;


import java.io.File; 
import java.io.IOException;
import org.apache.pdfbox.pdmodel.PDDocument; 
import org.apache.pdfbox.text.PDFTextStripper; 
import java.util.regex.*;
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
        String regPat = "(?:\\n((?:0[1-9]|1[1,2])/(?:0[1-9]|[12][0-9]|3[01]))\\s*(.+)'\n ((?:-\\d+\\.\\d{2})|(?:\\d+\\.\\d{2})))";
        Pattern pat = Pattern.compile(regPat);
        Matcher mat = pat.matcher(text);
        System.out.println(mat.toString());
    }
}
