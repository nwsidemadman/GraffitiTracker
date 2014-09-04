package net.ccaper.graffitiTracker.mvc.validators;

import net.ccaper.graffitiTracker.objects.AppUser;
import net.ccaper.graffitiTracker.objects.BannedInet;
import net.ccaper.graffitiTracker.utils.InetAddressUtils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * 
 * @author ccaper
 * 
 *         Validator for {@link net.ccaper.graffitiTracker.objects.BannedInet}
 * 
 */
// TODO(ccaper): confirm needed
@Component
public class BannedInetValidator implements Validator {

  /*
   * (non-Javadoc)
   * 
   * @see org.springframework.validation.Validator#supports(java.lang.Class)
   */
  @Override
  public boolean supports(Class<?> clazz) {
    return AppUser.class.isAssignableFrom(clazz);
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.springframework.validation.Validator#validate(java.lang.Object,
   * org.springframework.validation.Errors)
   */
  @Override
  public void validate(Object target, Errors errors) {
    BannedInet bannedInet = (BannedInet) target;
    if (StringUtils.isEmpty(bannedInet.getInetMinIncl())) {
      errors.rejectValue("inetMinIncl", "invalidInetMinIncl",
          "InetMinIncl can not be empty.");
    } else if (StringUtils.isEmpty(bannedInet.getInetMaxIncl())) {
      errors.rejectValue("inetMaxIncl", "invalidInetMaxIncl",
          "InetMaxIncl can not be empty.");
    } else if (InetAddressUtils.stringToNumber(bannedInet.getInetMinIncl()) > InetAddressUtils
        .stringToNumber(bannedInet.getInetMaxIncl())) {
      errors.rejectValue("inetMinIncl", "invalidInetMinIncl",
          "InetMinIncl can not be larger than InetMaxIncl.");
      errors.rejectValue("inetMaxIncl", "invalidInetMaxIncl",
          "InetMaxIncl can not smaller than InetMinIncl.");
    }
  }
}
