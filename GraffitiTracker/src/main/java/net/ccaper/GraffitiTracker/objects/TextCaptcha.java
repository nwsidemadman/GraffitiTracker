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

  public Set<String> getAnswers() {
    return answers;
  }

  @Override
  public String toString() {
    return "TextCaptcha [question=" + question + ", answers=" + answers + "]";
  }
}
