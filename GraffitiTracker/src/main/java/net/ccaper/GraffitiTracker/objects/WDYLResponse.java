package net.ccaper.GraffitiTracker.objects;

public class WDYLResponse {
  private Boolean response;
  
  public Boolean getResponse() {
    return response;
  }
  
  public void setResponse(String response) {
    this.response = Boolean.parseBoolean(response);
  }
}
