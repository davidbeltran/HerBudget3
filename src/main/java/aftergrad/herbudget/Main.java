package aftergrad.herbudget;

import java.io.IOException;
import java.util.Random;
import java.util.ArrayList;
/**
 *
 * @author David Beltran
 */
public class Main {
    public static void main(String[] args) throws IOException {
        Statement statement = new Statement("NovDec.pdf");
        //statement.getExpenses();
        statement.sendToDatabase();
//        ArrayList<Double> nums = new ArrayList<>();
//        nums.add(-307.38);
//        nums.add(-307.38);
//        nums.add(-307.38);
//        nums.add(-307.38);
//        nums.add(-307.38);
//        nums.add(-307.38);
//        nums.add(-307.38);
//        nums.add(-307.38);
//        nums.add(-307.38);
//        nums.add(-307.38);
//        nums.add(-307.38);
//        nums.add(-307.38);
//        nums.add(-307.38);
//        nums.add(-307.38);
//        Random rand = new Random();
//        for (double num : nums) {
//            int randInt = rand.nextInt(1000);
//            int otro = (Math.abs((int) num)) + randInt;
//            System.out.println(otro);
//        }
    }
}
