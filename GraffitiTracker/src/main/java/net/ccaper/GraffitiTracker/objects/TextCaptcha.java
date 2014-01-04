package net.ccaper.GraffitiTracker.objects;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class TextCaptcha {
  private String question;
  private Set<String> answers;

  public TextCaptcha(String question, String... answers) {
    this.question = question;
    this.answers = new HashSet<String>(Arrays.asList(answers));
  }

  public String getQuestion() {
    return question;
  }

  public void setQuestion(String question) {
    this.question = question;
  }

  public Set<String> getAnswers() {
    return answers;
  }

  public void setAnswers(String... answers) {
    this.answers = new HashSet<String>(Arrays.asList(answers));
  }
  
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((answers == null) ? 0 : answers.hashCode());
    result = prime * result + ((question == null) ? 0 : question.hashCode());
    return result;
  }

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

  @Override
  public String toString() {
    return "TextCaptcha [question=" + question + ", answers=" + answers + "]";
  }
}
