package document;

/**
 * This interface represents all the operations offered by a text document.
 * The text document can count its words and can compare two documents for
 * similarity.
 */

public interface TextDocument {
  /**
   * Return the contents of this document.
   *
   * @return the contents as a String
   */
  String getText();

  /**
   * Return the number of words in this document. A word is defined as a
   * sequence of symbols separated by whitespace.
   *
   * @return the number of words in this document
   */
  int getWordCount();

  /**
   * Computes the subsequence of characters that need to be removed from
   * {@code this} document such that the remainder is a subsequence
   * in the {@code other} document.
   *
   * @param other the text document for comparison with this
   * @return a subsequence of characters that need to be removed from the string in this document
   */
  String minusDiff(TextDocument other);

  /**
   * Computes the subsequence of characters in
   * {@code other } that needs to be added to this document
   * after removing the characters obtained from {@link TextDocument#minusDiff(TextDocument)}.
   *
   * @param other the text document for comparison with this
   * @return string fill me later
   */
  String plusDiff(TextDocument other);
}
