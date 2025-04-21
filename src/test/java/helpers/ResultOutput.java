package helpers;

public class ResultOutput {
    private static String nameMethod = "nameDefault";
    private static int passedCount = 0;

    public static void printTestStart(String nameMethod){
        if(!ResultOutput.nameMethod.equals("nameDefault") && !ResultOutput.nameMethod.equals(nameMethod)){
            passedCount = 0;
        }
        ResultOutput.nameMethod = nameMethod;
        System.out.println("-------------------");
        System.out.println("Отчет тестирования метода " + nameMethod + ":");
    }

    public static void log(String message) {
        System.out.println(message);
    }

    public static void printTestEnd(String nameMethod){
        passedCount++;
        System.out.println("Количество успешных пройденных тестов в методе " + nameMethod + ": " + passedCount);
        System.out.println("-------------------");
    }
}
