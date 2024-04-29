package aftergrad.herbudget;


import java.io.File; 
import org.apache.pdfbox.pdmodel.PDDocument; 
import org.apache.pdfbox.text.PDFTextStripper; 
/**
 *
 * @author David Beltran
 */
public class Main {
    public static void main(String[] args) {
        File file = new File("sample.pdf");
        PDDocument document = PDDocument.load(file);
        PDFTextStripper stripper = new PDFTextStripper();
        String text = stripper.getText(document);
        document.close();
        System.out.println("hello world");
    }
}
