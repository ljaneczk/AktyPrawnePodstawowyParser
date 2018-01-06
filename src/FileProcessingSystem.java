import java.io.IOException;

public class FileProcessingSystem {
    public static void main(String[] args) throws Exception {
        try {
            if (args.length == 0)
                throw new Exception("Nie podano ścieżki dostępu do pliku.");
            for (String argument : args) {
                String lowerCaseArgument = argument.toLowerCase();
                if (lowerCaseArgument.equals("-h") || lowerCaseArgument.equals("help") || lowerCaseArgument.equals("man")) {
                    printInformationWithHelp();
                    return;
                }
            }
            InputText Text = new InputText(args[0]);
            Text.removeHyphenation();
            Text.removeUnnecessaryLines();
            Text.separateArticlesAndContents();
            StructureTree Tree = new StructureTree(Text);
            if (args.length == 1)
                throw new Exception("Nie podano opcji programu.");
            if ( ! args[1].equals("-s") && ! args[1].equals("-t"))
                throw new Exception("Zły format opcji programu.");
            if (args[1].equals("-s"))
                showTableOfContents(args,Tree);
            else
                showContent(args,Tree);

        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        }
    }

    public static void showTableOfContents(String[] args, StructureTree Tree) throws Exception {
        if (args.length == 2)
            TreeOperations.showTableOfContentsOfFile(Tree.getRoot(),"");
        else if ( ! args[2].toLowerCase().equals("DZI.".toLowerCase()) || args.length < 4)
            throw new Exception("Podane argumenty nie są poprawne.");
        else {
            Number indexOfBranch = new Number(args[3],new Node(NodeType.dział));
            TreeOperations.showTableOfContestsOfBranch(Tree.getRoot(),indexOfBranch);
        }
    }

    public static void showContent(String[] args, StructureTree Tree) throws Exception {
        if (args.length < 4)
            throw new Exception("Zbyt mała liczba argumentów.");
        else if (args.length == 6 && args[4].equals("-"))
            TreeOperations.showContentOfRangeOfArticles(Tree.getRoot(),args[2]+" "+args[3]+" "+args[4]+" "+args[5]);
        else {
            String path = args[2];
            for (int ind = 3; ind < args.length; ind++)
                path = path + " " + args[ind];
            TreeOperations.showContentOfSelectedPath(Tree.getRoot(),path);
        }
    }

    public static void printInformationWithHelp() {
        System.out.println("Należy podać trzy części argumentów oddzielone spacjami.\n");
        System.out.println("Pierwszy argument to ścieżka dostępu do pliku.\n");
        System.out.println("Drugi argument to tryb działania: \"-s\" w przypadku spisu treści, \"-t\" w przypadku wyświetlania treści.\n");
        System.out.println("Trzeci argument zależy od opcji podanej w drugim argumencie.\n");
        System.out.println("Jeżeli wybrana opcja to \"-s\", to po niej można nie podawać argumentów i wtedy wypisywany jest spis treści");
        System.out.println("całej ustawy, jeśli chce się zobaczyć spis treści wybranego działu (o ile istnieje), to należy go podać");
        System.out.println("Jeżeli wybrana opcja to \"-t\", to po niej należy podać argument. Argument składa się z informacji");
        System.out.println("o wypisywanym fragmencie w postaci ścieżki kolejnych zagłębień w strukturze");
        System.out.println("np. Roz, Art, Art ust, Art ust pun, Art ust pun lit. Zamiast tego można podać zakres artykułów do wypisania.\n");
        System.out.println("Format trzeciego argumentu to albo zakres artykułów postaci \"Art. X. - Y.\", gdzie X, Y są odpowiednimi");
        System.out.println("numerami artykułu, albo ciąg postaci \"typ. X\", gdzie typ to jeden spośród DZI, Roz, Art, ust, pun, lit,");
        System.out.println("natomiast X to odpowiednia liczba lub litera(y), zakończona znakiem '.' lub ')'. Takie fragmenty");
        System.out.println("rozdzielone przecinkiem ',' i jedną spacją\n");
        System.out.println("Przykłady:");
        System.out.println("C:\\Users\\Programmer\\Desktop\\obiektowe\\uokik.txt -t Art. 10., ust. 2., pun. 2), lit. a)");
        System.out.println("C:\\Users\\Programmer\\Desktop\\obiektowe\\konstytucja.txt -s ");
    }


}
