import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.lang.String;
import java.lang.Character;
import java.util.regex.Pattern;

/*Czy tutaj potrzebny jest interfejs? */
public class InputText implements InputParser {
    private String[] LinesOfFile;
    private int NumberOfLines;
    private boolean ThereWasEnterBetweenArticleAndContent = true;

    public boolean getThereWasEnterBetweenArticleAndContent() {
        return ThereWasEnterBetweenArticleAndContent;
    }

    public String[] getLinesOfFile() {
        return LinesOfFile;
    }

    public void print() {
        for (int i = 0; i < NumberOfLines; i++) {
            System.out.println(LinesOfFile[i]);
        }
        System.out.println(NumberOfLines);
    }

    public void print_pattern(String pattern) {
        System.out.println("\n\n\n");
        for(String line : LinesOfFile)
            if(Pattern.matches('^'+pattern+".*",line))
                System.out.println(line);
    }

    public InputText(String path) throws IOException {
        File file = new File(path);
        Scanner input = new Scanner(file);
        NumberOfLines = 0;
        while(input.hasNextLine()) {
            input.nextLine();
            NumberOfLines++;
        }
        input.close();
        LinesOfFile = new String[NumberOfLines];
        input = new Scanner(file);
        for (int i = 0; i < NumberOfLines; i++)
            LinesOfFile[i] = new String(input.nextLine());
        //print();
        input.close();
    }

    @Override
    public void removeHyphenation() {
        String CurrentLine, NextLine;
        int indexOfLineDivision;
        boolean spaceNotFound;
        for (int i = 0; i < (NumberOfLines - 1); i++)
            if(LinesOfFile[i].endsWith("-")){
                CurrentLine = new String(LinesOfFile[i]);
                NextLine = new String(LinesOfFile[i+1]);
                indexOfLineDivision = CurrentLine.length() - 1;
                //System.out.print(CurrentLine+" "+NextLine+"\n");
                CurrentLine = CurrentLine.substring(0,CurrentLine.length()-1) + NextLine;
                //System.out.println(CurrentLine);
                spaceNotFound = true;
                while (spaceNotFound && indexOfLineDivision<CurrentLine.length()) {
                    char c = CurrentLine.charAt(indexOfLineDivision);
                    spaceNotFound = !(Character.isWhitespace(c));
                    indexOfLineDivision++;
                }
                LinesOfFile[i] = CurrentLine.substring(0,indexOfLineDivision);
                LinesOfFile[i+1] = CurrentLine.substring(indexOfLineDivision,CurrentLine.length());
        }
        //print();
    }

    @Override
    public boolean isBadLine (String Line) {
        if(Line.length()<2)
            return true;
        if(Line.contains("©Kancelaria Sejmu"))
            return true;
        if ((Line.toLowerCase()).equals(Line.toUpperCase()) && Line.length()==10 && Line.charAt(4)=='-' && Line.charAt(7)=='-')
            return true;
        return false;
    }

    public void removeUnnecessaryLinesAtTheBeginning() {
        int NumberOfLinesToDelete = 0;
        String DziałRegex = NodeType.findRegex(NodeType.dział);
        String RozdziałRegex = NodeType.findRegex(NodeType.rozdział);
        for (String line : LinesOfFile) {
            if (!Pattern.matches(DziałRegex, line) && !Pattern.matches(RozdziałRegex, line))
                NumberOfLinesToDelete++;
            else break;
        }
        //else System.out.println(LinesOfFile[i]);
        String[] NewLinesOfFile = new String[NumberOfLines-NumberOfLinesToDelete];
        for (int i = 0; i < (NumberOfLines - NumberOfLinesToDelete); i++)
                NewLinesOfFile[i] = LinesOfFile[i+NumberOfLinesToDelete];
        LinesOfFile = NewLinesOfFile;
        NumberOfLines -= NumberOfLinesToDelete;
    }

    @Override
    public void removeUnnecessaryLines() {
        removeUnnecessaryLinesAtTheBeginning();
        int newNumberOfLines = 0;
        for (String Line : LinesOfFile)
            if ( ! isBadLine(Line))
                newNumberOfLines++;
            //else System.out.println(LinesOfFile[i]);
        String[] NewLinesOfFile = new String[newNumberOfLines];
        int newIndex = 0;
        for (String Line : LinesOfFile)
            if ( ! isBadLine(Line))
                NewLinesOfFile[newIndex++] = Line;
        LinesOfFile = NewLinesOfFile;
        NumberOfLines = newNumberOfLines;
        //System.out.println("\n\n\n\n");
        //print();
    }

    @Override
    public boolean isArticleToSeparate (String line) {
        if( ! Pattern.matches(NodeType.findRegex(NodeType.artykuł),line))
            return false;
        return (line.indexOf('.',5) != line.length() - 1);      //5 = "Art. ".length()
    }

    @Override
    public void separateArticlesAndContents() {
        int NewNumberOfLines = NumberOfLines;
        for (String Line : LinesOfFile)
            if ( isArticleToSeparate(Line))
                NewNumberOfLines++;
        String[] NewLinesOfFile = new String[NewNumberOfLines];
        int newIndex = 0;
        for (String Line : LinesOfFile) {
            if ( ! isArticleToSeparate(Line))
                NewLinesOfFile[newIndex++] = Line;
            else {
                int indexOfEndOfArticlePart = Line.indexOf('.',5);
                NewLinesOfFile[newIndex++] = Line.substring(0,indexOfEndOfArticlePart+1);
                NewLinesOfFile[newIndex++] = Line.substring(indexOfEndOfArticlePart+2);
            }
        }
        ThereWasEnterBetweenArticleAndContent = (NewNumberOfLines != NumberOfLines);
        LinesOfFile = NewLinesOfFile;
        NumberOfLines = NewNumberOfLines;
        //print();
    }

}
