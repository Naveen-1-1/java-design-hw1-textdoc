import static org.junit.Assert.assertEquals;

import document.TextDocument;
import document.TextDocumentImpl;
import org.junit.Before;
import org.junit.Test;

/**
 * Represents the tests for @code TextDocumentImpl.
 */
public class TextDocumentImplTest {

  private TextDocument textDocumentD1;
  private TextDocument textDocumentD2;

  /**
   * Sets up two text documents for testing.
   */
  @Before
  public void setup() {
    textDocumentD1 = new TextDocumentImpl("Hello World");
    textDocumentD2 = new TextDocumentImpl("Helo Word!");
  }

  /**
   * Checks for exception when null text is passed for initialization.
   *
   * @throws NullPointerException If null text is used in initialization.
   */
  @Test(expected = NullPointerException.class)
  public void testTextDocumentImplForNullText() {
    new TextDocumentImpl(null);
  }

  /**
   * Sanity check of get text.
   */
  @Test
  public void testGetText() {
    assertEquals("Hello World", textDocumentD1.getText());
  }

  /**
   * Sanity check of word count in text.
   */
  @Test
  public void testGetWordCount() {
    assertEquals(2, textDocumentD1.getWordCount());
  }

  /**
   * Check of word count in whitespace string.
   */
  @Test
  public void testGetWordCountForWhitespaceString() {
    textDocumentD1 = new TextDocumentImpl(" \n \t  ");
    assertEquals(0, textDocumentD1.getWordCount());
  }

  /**
   * Check of word count in empty string.
   */
  @Test
  public void testGetWordCountForEmptyString() {
    textDocumentD1 = new TextDocumentImpl("");
    assertEquals(0, textDocumentD1.getWordCount());
  }

  /**
   * Check of word count in string filled with whitespaces.
   */
  @Test
  public void testGetWordCountWithExtraWhitespace() {
    textDocumentD1 = new TextDocumentImpl("   Hello\tmy    name\nis      Naveen  ");
    assertEquals(5, textDocumentD1.getWordCount());
  }

  /**
   * Checks for exception when null is passed as param while calling minusDiff.
   *
   * @throws NullPointerException If null is used as param for calling minusDiff.
   */
  @Test(expected = NullPointerException.class)
  public void testMinusDiffForNullText() {
    textDocumentD1.minusDiff(null);
  }

  /**
   * Sanity check of minusDiff functionality.
   */
  @Test
  public void testMinusDiffBasic() {
    assertEquals("ll", textDocumentD1.minusDiff(textDocumentD2));
  }

  /**
   * Checks for exception when null is passed as param while calling plusDiff.
   *
   * @throws NullPointerException If null is used as param for calling plusDiff.
   */
  @Test(expected = NullPointerException.class)
  public void testPlusDiffForNullText() {
    textDocumentD1.plusDiff(null);
  }

  /**
   * Sanity check of plusDiff functionality.
   */
  @Test
  public void testPlusDiffBasic() {
    assertEquals("!", textDocumentD1.plusDiff(textDocumentD2));
  }

  private void customSetup(String d1, String d2) {
    textDocumentD1 = new TextDocumentImpl(d1);
    textDocumentD2 = new TextDocumentImpl(d2);
  }

  /**
   * When one string text is empty.
   */
  @Test
  public void testDiffEmptyString() {
    String d1 = "wxyz";
    String d2 = "";

    customSetup(d1, d2);
    assertEquals("wxyz", textDocumentD1.minusDiff(textDocumentD2));
    assertEquals("", textDocumentD1.plusDiff(textDocumentD2));

    customSetup(d2, d1);
    assertEquals("", textDocumentD1.minusDiff(textDocumentD2));
    assertEquals("wxyz", textDocumentD1.plusDiff(textDocumentD2));
  }

  /**
   * When one string text is filled with whitespaces.
   */
  @Test
  public void testDiffWhitespaceString() {
    String d1 = "wxyz";
    String d2 = " \n \t  ";

    customSetup(d1, d2);
    assertEquals("wxyz", textDocumentD1.minusDiff(textDocumentD2));
    assertEquals(" \n \t  ", textDocumentD1.plusDiff(textDocumentD2));

    customSetup(d2, d1);
    assertEquals(" \n \t  ", textDocumentD1.minusDiff(textDocumentD2));
    assertEquals("wxyz", textDocumentD1.plusDiff(textDocumentD2));
  }

  /**
   * When both strings have no LCS.
   */
  @Test
  public void testDiffNoLcs() {
    String d1 = "wxyz";
    String d2 = "abc";

    customSetup(d1, d2);
    assertEquals("wxyz", textDocumentD1.minusDiff(textDocumentD2));
    assertEquals("abc", textDocumentD1.plusDiff(textDocumentD2));

    customSetup(d2, d1);
    assertEquals("abc", textDocumentD1.minusDiff(textDocumentD2));
    assertEquals("wxyz", textDocumentD1.plusDiff(textDocumentD2));
  }

  /**
   * When both the strings are same.
   */
  @Test
  public void testDiffNoLcsDiff() {
    customSetup("wxyz", "wxyz");
    assertEquals("", textDocumentD1.minusDiff(textDocumentD2));
    assertEquals("", textDocumentD1.plusDiff(textDocumentD2));
  }

  /**
   * When consecutive LCS at the start of both the equal length strings.
   */
  @Test
  public void testDiffEqualStringsLcsAtStartConsecutive() {
    customSetup("hiixyz", "hiibcd");
    assertEquals("xyz", textDocumentD1.minusDiff(textDocumentD2));
    assertEquals("bcd", textDocumentD1.plusDiff(textDocumentD2));
  }

  /**
   * When consecutive LCS at the end of both the equal length strings.
   */
  @Test
  public void testDiffEqualStringsLcsAtEndConsecutive() {
    customSetup("xyzhii", "bcdhii");
    assertEquals("xyz", textDocumentD1.minusDiff(textDocumentD2));
    assertEquals("bcd", textDocumentD1.plusDiff(textDocumentD2));
  }

  /**
   * When consecutive LCS between both the equal length strings.
   */
  @Test
  public void testDiffEqualStringsLcsAtBetweenConsecutive() {
    customSetup("xyhiiz", "bchiid");
    assertEquals("xyz", textDocumentD1.minusDiff(textDocumentD2));
    assertEquals("bcd", textDocumentD1.plusDiff(textDocumentD2));
  }

  /**
   * When LCS separated (longer) and LCS consecutive of the unequal length strings.
   */
  @Test
  public void testDiffUnEqualStringsLcsSeparatedLongerAndLcsConsecutive() {
    String d1 = "hxiyiza";
    String d2 = "hiibcd";
    customSetup(d1, d2);
    assertEquals("xyza", textDocumentD1.minusDiff(textDocumentD2));
    assertEquals("bcd", textDocumentD1.plusDiff(textDocumentD2));

    customSetup(d2, d1);
    assertEquals("bcd", textDocumentD1.minusDiff(textDocumentD2));
    assertEquals("xyza", textDocumentD1.plusDiff(textDocumentD2));
  }

  /**
   * When LCS separated and LCS consecutive(longer) of the unequal length strings.
   */
  @Test
  public void testDiffUnEqualStringsLcsConsecutiveLongerAndLcsSeparated() {
    String d1 = "hiibcda";
    String d2 = "hxiyiz";
    customSetup(d1, d2);
    assertEquals("bcda", textDocumentD1.minusDiff(textDocumentD2));
    assertEquals("xyz", textDocumentD1.plusDiff(textDocumentD2));

    customSetup(d2, d1);
    assertEquals("xyz", textDocumentD1.minusDiff(textDocumentD2));
    assertEquals("bcda", textDocumentD1.plusDiff(textDocumentD2));
  }

  /**
   * When one string equal to LCS of the unequal length strings.
   */
  @Test
  public void testDiffUnEqualStringsWhereOneStringEqualToLcs() {
    String d1 = "bcdahii";
    String d2 = "hii";
    customSetup(d1, d2);
    assertEquals("bcda", textDocumentD1.minusDiff(textDocumentD2));
    assertEquals("", textDocumentD1.plusDiff(textDocumentD2));

    customSetup(d2, d1);
    assertEquals("", textDocumentD1.minusDiff(textDocumentD2));
    assertEquals("bcda", textDocumentD1.plusDiff(textDocumentD2));
  }

  /**
   * When the diff gives out different LCS strings based on the DP table construction.
   */
  @Test
  public void testDiffDifferentLcs() {
    String d1 = "abcde";
    String d2 = "acbed";
    customSetup(d1, d2);
    assertEquals("bd", textDocumentD1.minusDiff(textDocumentD2));
    assertEquals("bd", textDocumentD1.plusDiff(textDocumentD2));

    customSetup(d2, d1);
    assertEquals("ce", textDocumentD1.minusDiff(textDocumentD2));
    assertEquals("ce", textDocumentD1.plusDiff(textDocumentD2));
  }
}
