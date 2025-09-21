package document;

/**
 * This class implements the TextDocument interface and uses the least common subsequence for diff
 * and least substring for commonSubText.
 */
public class TextDocumentImpl implements TextDocument {

  private final String text;

  /**
   * Initializes the Text Document Implementation with a valid non-null text.
   *
   * @param text The non-null string that needs to be stored in the class.
   * @throws NullPointerException If the text is null.
   */
  public TextDocumentImpl(String text) {
    if (text == null) {
      throw new NullPointerException("Text is null");
    }
    this.text = text;
  }

  /**
   * Returns the stored text.
   *
   * @return The stored text.
   */
  @Override
  public String getText() {
    return this.text;
  }

  /**
   * Retrieves the total number of words in the stored text.
   *
   * @return The total word count in text.
   */
  @Override
  public int getWordCount() {
    if (this.text.trim().isEmpty()) {
      return 0;
    }
    return this.text.trim().split("\\s+").length;
  }

  private int[][] computeLcsDpTable(String otherText) {
    int rows = this.text.length() + 1;
    int cols = otherText.length() + 1;
    int[][] lcsDpTable = new int[rows][cols];

    for (int i = 1; i < rows; i++) {
      for (int j = 1; j < cols; j++) {
        if (this.text.charAt(i - 1) == otherText.charAt(j - 1)) {
          lcsDpTable[i][j] = lcsDpTable[i - 1][j - 1] + 1;
        } else {
          lcsDpTable[i][j] = Math.max(lcsDpTable[i - 1][j], lcsDpTable[i][j - 1]);
        }
      }
    }

    return lcsDpTable;
  }

  /**
   * Returns a string that when removed from the text becomes a subsequence of other's text.
   *
   * @param other Another text document object that contains the text.
   * @return The string that when removed from the text becomes a subsequence of other's text.
   * @throws NullPointerException If the other text document is null.
   */
  @Override
  public String minusDiff(TextDocument other) {
    if (other == null) {
      throw new NullPointerException("Other text document is null");
    }
    String otherText = other.getText();
    int[][] lcsDpTable = this.computeLcsDpTable(otherText);
    return this.computeMinusDiff(otherText, lcsDpTable);
  }

  private String computeMinusDiff(String otherText, int[][] lcsDpTable) {
    int i = this.text.length();
    int j = otherText.length();
    StringBuilder result = new StringBuilder();
    while (i > 0) {
      if (j > 0 && this.text.charAt(i - 1) == otherText.charAt(j - 1)) {
        i--;
        j--;
        continue;
      }
      if (j == 0 || lcsDpTable[i - 1][j] > lcsDpTable[i][j - 1]) {
        result.append(this.text.charAt(i - 1));
        i--;
      } else {
        j--;
      }
    }
    return result.reverse().toString();
  }

  /**
   * Returns a string that when added to the text makes it a supersequence of other's text.
   *
   * @param other Another text document object that contains the text.
   * @return The string that when added to the text makes it a supersequence of other's text.
   * @throws NullPointerException If the other text document is null.
   */
  @Override
  public String plusDiff(TextDocument other) {
    if (other == null) {
      throw new NullPointerException("Other text document is null");
    }
    String otherText = other.getText();
    int[][] lcsDpTable = this.computeLcsDpTable(otherText);
    return this.computePlusDiff(otherText, lcsDpTable);
  }

  private String computePlusDiff(String otherText, int[][] lcsDpTable) {
    int i = this.text.length();
    int j = otherText.length();
    StringBuilder result = new StringBuilder();
    while (j > 0) {
      if (i > 0 && this.text.charAt(i - 1) == otherText.charAt(j - 1)) {
        i--;
        j--;
        continue;
      }
      if (i == 0 || lcsDpTable[i - 1][j] <= lcsDpTable[i][j - 1]) {
        result.append(otherText.charAt(j - 1));
        j--;
      } else {
        i--;
      }
    }
    return result.reverse().toString();
  }
}
