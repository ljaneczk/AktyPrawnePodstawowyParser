import java.io.IOException;
import java.util.LinkedList;
import java.util.regex.Pattern;

public class FileProcessingSystem {
    public static void main(String[] args) throws Exception {
        //System.out.println("Proszę podać ścieżkę do pliku");
        try {
            if (args.length == 0)
                throw new Exception("Nie podano ścieżki dostępu do pliku\n");
            InputText Text = new InputText(args[1]);
            Text.removeHyphenation();
            Text.removeUnnecessaryLines();
            Text.separateArticlesAndContents();
            StructureTree Tree = new StructureTree(Text);
            System.out.println("Uff\n\n\n\n\n\n\n\n\n");
            TreeOperations.deepFirstSearch(Tree.getRoot());
            /*Number n1 = new Number("Rozdział 4a",NodeType.rozdział);
            Number n2 = new Number("Rozdział 4cd",NodeType.rozdział);
            Number n3 = new Number("Rozdział 4cda",NodeType.rozdział);
            Number n4 = new Number("Rozdział 4bcdfga",NodeType.rozdział);
            System.out.println(n1);
            System.out.println(n2);
            System.out.println(n3);
            System.out.println(n4);
            System.out.println(n1.isNotGreaterThan(n2));
            System.out.println(n2.isNotGreaterThan(n3));
            System.out.println(n3.isNotGreaterThan(n4));
            System.out.println(n2.isNotGreaterThan(n4));
            System.out.println(n1.isNotGreaterThan(n4));*/
            System.out.println("\n\n");
            TreeOperations.showWholeContentOfFile(Tree.getRoot(),"");
            System.out.println("\n\n");
            String path = "Art. 103.";
            TreeOperations.showContentOfSelectedPath(Tree.getRoot(),path);
            System.out.println("\n\n");
            String path1 = "Art. 101., ust. 1.";
            TreeOperations.showContentOfSelectedPath(Tree.getRoot(),path1);
            System.out.println("\n\n");
            String path2 = "Art. 101., ust. 2.";
            TreeOperations.showContentOfSelectedPath(Tree.getRoot(),path2);
            //Art. 2, ust. 2., pkt 2), lit. a)
            System.out.println("\n\n");
            String path3 = "Art. 2.";
            TreeOperations.showContentOfSelectedPath(Tree.getRoot(),path3);
            System.out.println("\n\n");
            String path4 = "Art. 2., ust. 2.";
            TreeOperations.showContentOfSelectedPath(Tree.getRoot(),path4);
            System.out.println("\n\n");
            String path5 = "Art. 101., ust. 2., pun. 2)";
            TreeOperations.showContentOfSelectedPath(Tree.getRoot(),path5);
            System.out.println("\n\n");
            String path6 = "Art. 101., ust. 2., pun. 2), lit. a)";
            TreeOperations.showContentOfSelectedPath(Tree.getRoot(),path6);


        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }


}
