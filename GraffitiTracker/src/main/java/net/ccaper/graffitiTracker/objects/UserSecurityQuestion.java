package net.ccaper.graffitiTracker.objects;

/**
 * 
 * @author ccaper
 * 
 *         POJO for UserSecurityQuestion
 * 
 */
public class UserSecurityQuestion {
  private int userid;
  private String securityQuestion;

  /**
   * Gets the userid.
   * 
   * @return the userid
   */
  public int getUserid() {
    return userid;
  }

  /**
   * Sets the userid.
   * 
   * @param userid
   *          the new userid
   */
  public void setUserid(int userid) {
    this.userid = userid;
  }

  /**
   * Gets the security question.
   * 
   * @return the security question
   */
  public String getSecurityQuestion() {
    return securityQuestion;
  }

  /**
   * Sets the security question.
   * 
   * @param securityQuestion
   *          the new security question
   */
  public void setSecurityQuestion(String securityQuestion) {
    this.securityQuestion = securityQuestion;
  }
}
