package net.ccaper.GraffitiTracker.objects;

public class UserSecurityQuestion {
  private int userid;
  private String securityQuestion;

  public int getUserid() {
    return userid;
  }

  public void setUserid(int userid) {
    this.userid = userid;
  }

  public String getSecurityQuestion() {
    return securityQuestion;
  }

  public void setSecurityQuestion(String securityQuestion) {
    this.securityQuestion = securityQuestion;
  }
}
