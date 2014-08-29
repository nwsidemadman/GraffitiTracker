package net.ccaper.graffitiTracker.service;

/**
 * 
 * @author ccaper
 * 
 *         Service for checking banned words
 * 
 */
public interface BannedWordService {

  /**
   * Does string contain banned word.
   * 
   * @param string
   *          the string
   * @return true, if string contains a banned word
   */
  boolean doesStringContainBannedWord(String string);
}
