import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.lang.String;
import java.lang.Character;
import java.util.regex.Pattern;

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
            LinesOfFile[i] = input.nextLine();
        input.close();
    }

    @Override
    public void removeHyphenation() {
        String currentLine, nextLine;
        int indexOfLineDivision;
        boolean spaceNotFound;
        for (int i = 0; i < (NumberOfLines - 1); i++)
            if(LinesOfFile[i].endsWith("-")){
                currentLine = LinesOfFile[i];
                nextLine = LinesOfFile[i+1];
                indexOfLineDivision = currentLine.length() - 1;
                currentLine = currentLine.substring(0, currentLine.length()-1) + nextLine;
                spaceNotFound = true;
                while (spaceNotFound && indexOfLineDivision < currentLine.length()) {
                    char character = currentLine.charAt(indexOfLineDivision);
                    spaceNotFound = !(Character.isWhitespace(character));
                    indexOfLineDivision++;
                }
                LinesOfFile[i] = currentLine.substring(0,indexOfLineDivision);
                LinesOfFile[i+1] = currentLine.substring(indexOfLineDivision,currentLine.length());
        }
    }

    @Override
    public boolean isBadLine (String line) {
        if(line.length() < 2)
            return true;
        if(line.contains("©Kancelaria Sejmu"))
            return true;
        if ((line.toLowerCase()).equals(line.toUpperCase()) && line.length() == 10 && line.charAt(4) == '-' && line.charAt(7) == '-')
            return true;
        return false;
    }

    public void removeUnnecessaryLinesAtTheBeginning() {
        int numberOfLinesToDelete = 0;
        String branchRegex = NodeType.findRegex(NodeType.dział);
        String chapterRegex = NodeType.findRegex(NodeType.rozdział);
        for (String line : LinesOfFile) {
            if ( ! Pattern.matches(branchRegex, line) && ! Pattern.matches(chapterRegex, line))
                numberOfLinesToDelete++;
            else break;
        }
        String[] NewLinesOfFile = new String[NumberOfLines-numberOfLinesToDelete];
        for (int i = 0; i < (NumberOfLines - numberOfLinesToDelete); i++)
                NewLinesOfFile[i] = LinesOfFile[i+numberOfLinesToDelete];
        LinesOfFile = NewLinesOfFile;
        NumberOfLines = NumberOfLines - numberOfLinesToDelete;
    }

    @Override
    public void removeUnnecessaryLines() {
        removeUnnecessaryLinesAtTheBeginning();
        int newNumberOfLines = 0;
        for (String line : LinesOfFile)
            if ( ! isBadLine(line))
                newNumberOfLines++;
        String[] NewLinesOfFile = new String[newNumberOfLines];
        int newIndex = 0;
        for (String line : LinesOfFile)
            if ( ! isBadLine(line))
                NewLinesOfFile[newIndex++] = line;
        LinesOfFile = NewLinesOfFile;
        NumberOfLines = newNumberOfLines;
    }

    @Override
    public boolean isArticleToSeparate (String line) {
        if( ! Pattern.matches(NodeType.findRegex(NodeType.artykuł),line))
            return false;
        return (line.indexOf('.',5) != line.length() - 1);
    }

    @Override
    public void separateArticlesAndContents() {
        int newNumberOfLines = NumberOfLines;
        for (String line : LinesOfFile)
            if ( isArticleToSeparate(line))
                newNumberOfLines++;
        String[] NewLinesOfFile = new String[newNumberOfLines];
        int newIndex = 0;
        for (String line : LinesOfFile) {
            if ( ! isArticleToSeparate(line))
                NewLinesOfFile[newIndex++] = line;
            else {
                int indexOfEndOfArticlePart = line.indexOf('.',5);
                NewLinesOfFile[newIndex++] = line.substring(0,indexOfEndOfArticlePart+1);
                NewLinesOfFile[newIndex++] = line.substring(indexOfEndOfArticlePart+2);
            }
        }
        ThereWasEnterBetweenArticleAndContent = (newNumberOfLines != NumberOfLines);
        LinesOfFile = NewLinesOfFile;
        NumberOfLines = newNumberOfLines;
    }

}
