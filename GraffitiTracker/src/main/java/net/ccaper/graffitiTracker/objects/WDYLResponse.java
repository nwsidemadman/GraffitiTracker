package net.ccaper.graffitiTracker.objects;

/**
 * 
 * @author ccaper
 * 
 * POJO for Google's WDYL Banned Word API
 *
 */
public class WDYLResponse {
  private Boolean response;
  
  /**
   * Gets the response.
   *
   * @return the response
   */
  public Boolean getResponse() {
    return response;
  }
  
  /**
   * Sets the response.
   *
   * @param response the new response
   */
  public void setResponse(String response) {
    this.response = Boolean.parseBoolean(response);
  }
}
