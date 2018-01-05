public class ArabicNumber implements NumberMethods {
    @Override
    public boolean isCorrectDigit(char character) {
        return (character >= '0' && character <= '9');
    }

    @Override
    public int getValueOfDigit(char character) {
        return (character - '0');
    }

    @Override
    public int countValue(String line) {
        int decimalNumber = 0;
        for (int ind = 0; ind < line.length() && isCorrectDigit(line.charAt(ind)); ind++) {
            decimalNumber = 10 * decimalNumber + getValueOfDigit(line.charAt(ind));
        }
        return decimalNumber;
    }

    /*@Override
    public boolean isGreaterOrEqualThan(Object other) {
        return true;
    }*/
}
