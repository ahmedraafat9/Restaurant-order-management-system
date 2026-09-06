import java.util.List;

public class ReportGenerator {

    private ReportGenerator() {

    }


    public static <T extends Reportable> void generateReport(String title, List<T> items) {
        System.out.println("======= " + title + " =======");
        if (items.isEmpty()){
            System.out.println("No items found");
            return;
        }
        items.forEach(System.out::println);

    }
}
