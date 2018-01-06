public class Number {
    private int IntegerPartOfNumber;
    private String StringPartOfNumber;

    Number(String line, NodeType type) {
        IntegerPartOfNumber = 0;
        StringPartOfNumber = "";
        int beginIndex = findBeginAfterErasingBeginning(type);
        line = line.substring(beginIndex);
        findPartsOfNumber(line,type);
    }

    Number(String line, Node vertex) {
        IntegerPartOfNumber = 0;
        StringPartOfNumber = "";
        findPartsOfNumber(line,vertex.getNodeType());
    }

    private int findBeginAfterErasingBeginning (NodeType type) {
        switch (type) {
            case artykuł:
                return "Art. ".length();
            case rozdział:
                return "Rozdział ".length();
            case dział:
                return "DZIAŁ ".length();
            default:
                return 0;
        }
    }

    private void findPartsOfNumber(String line, NodeType type) {
        int index = 0;
        if(type != NodeType.tytuł && type != NodeType.plik && type != NodeType.litera) {
            if ( isArabic(line)) {
                ArabicNumber number;
                number = new ArabicNumber();
                IntegerPartOfNumber = number.countValue(line);
                while (index < line.length() && number.isCorrectDigit(line.charAt(index)))
                    index++;
            }
            else {
                RomanNumber number;
                number = new RomanNumber();
                IntegerPartOfNumber = number.countValue(line);
                while (index < line.length() && number.isCorrectDigit(line.charAt(index)))
                    index++;
            }
        }

        if (type != NodeType.tytuł) {
            while (index < line.length() && isLetter(line.charAt(index))) {
                StringPartOfNumber = StringPartOfNumber + line.charAt(index);
                index++;
            }
        }
    }

    public boolean isNotGreaterThan (Object other) {
        if ( ! (other instanceof Number))
            return true;
        Number number = (Number) other;
        if (this.IntegerPartOfNumber != number.IntegerPartOfNumber)
            return (this.IntegerPartOfNumber < number.IntegerPartOfNumber);
        return (this.StringPartOfNumber.compareTo(number.StringPartOfNumber) <= 0);
    }

    public boolean isNotLessThan (Object other) {
        if ( ! (other instanceof Number))
            return true;
        Number number = (Number) other;
        if (this.IntegerPartOfNumber != number.IntegerPartOfNumber)
            return (this.IntegerPartOfNumber > number.IntegerPartOfNumber);
        return (this.StringPartOfNumber.compareTo(number.StringPartOfNumber) >= 0);
    }

    public boolean isEqualTo (Object other) {
        if ( ! (other instanceof Number))
            return true;
        Number number = (Number) other;
        if (this.IntegerPartOfNumber != number.IntegerPartOfNumber)
            return false;
        return (this.StringPartOfNumber.compareTo(number.StringPartOfNumber) == 0);
    }

    private boolean isArabic (String line) {
        if (line.length() == 0)
            return false;
        return (line.charAt(0) >= '0' && line.charAt(0) <= '9');
    }

    private boolean isLetter (char character) {
        if (character >= 'a' && character <= 'z')
            return true;
        if (character >= 'A' && character <= 'Z')
            return true;
        return false;
    }

    public String toString() {
        return Integer.toString(IntegerPartOfNumber)+StringPartOfNumber;
    }
}
