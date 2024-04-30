package aftergrad.herbudget;


import java.io.File; 
import java.io.IOException;
import org.apache.pdfbox.pdmodel.PDDocument; 
import org.apache.pdfbox.text.PDFTextStripper; 
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
        System.out.println(text);
    }
}
