package net.ccaper.graffitiTracker.objects;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 
 * @author ccaper
 * 
 *         POJO for TextCaptcha values
 * 
 */
public class TextCaptcha {
  private String question;
  private Set<String> answers;

  /**
   * Instantiates a new text captcha.
   * 
   * @param question
   *          the question
   * @param answers
   *          the answers
   */
  public TextCaptcha(String question, String... answers) {
    this.question = question;
    this.answers = new HashSet<String>(Arrays.asList(answers));
  }

  /**
   * Gets the question.
   * 
   * @return the question
   */
  public String getQuestion() {
    return question;
  }

  /**
   * Sets the question.
   * 
   * @param question
   *          the new question
   */
  public void setQuestion(String question) {
    this.question = question;
  }

  /**
   * Gets the answers.
   * 
   * @return the answers
   */
  public Set<String> getAnswers() {
    return answers;
  }

  /**
   * Sets the answers.
   * 
   * @param answers
   *          the new answers
   */
  public void setAnswers(String... answers) {
    this.answers = new HashSet<String>(Arrays.asList(answers));
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((answers == null) ? 0 : answers.hashCode());
    result = prime * result + ((question == null) ? 0 : question.hashCode());
    return result;
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    TextCaptcha other = (TextCaptcha) obj;
    if (answers == null) {
      if (other.answers != null)
        return false;
    } else if (!answers.equals(other.answers))
      return false;
    if (question == null) {
      if (other.question != null)
        return false;
    } else if (!question.equals(other.question))
      return false;
    return true;
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "TextCaptcha [question=" + question + ", answers=" + answers + "]";
  }
}
