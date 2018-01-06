public class RomanNumber implements NumberMethods {
    @Override
    public boolean isCorrectDigit(char character) {
        if (character == 'I')
            return true;
        if (character == 'V')
            return true;
        if (character == 'X')
            return true;
        if (character == 'L')
            return true;
        if (character == 'C')
            return true;
        if (character == 'D')
            return true;
        if (character == 'M')
            return true;
        return false;
    }

    @Override
    public int getValueOfDigit(char character) {
        if (character == 'I')
            return 1;
        if (character == 'V')
            return 5;
        if (character == 'X')
            return 10;
        if (character == 'L')
            return 50;
        if (character == 'C')
            return 100;
        if (character == 'D')
            return 500;
        if (character == 'M')
            return 1000;
        return 0;
    }

    @Override
    public int countValue(String line) {
        int decimalNumber = 0;
        for (int ind = 0; ind < line.length() && isCorrectDigit(line.charAt(ind)); ind++) {
            decimalNumber += getValueOfDigit(line.charAt(ind));
            int prevInd = ind - 1;
            if (prevInd >= 0 && getValueOfDigit(line.charAt(prevInd)) < getValueOfDigit(line.charAt(ind)))
                decimalNumber -= 2 * getValueOfDigit(line.charAt(prevInd));
        }
        return decimalNumber;
    }
}
