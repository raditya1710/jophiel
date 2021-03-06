package org.iatoki.judgels.jophiel.user.profile.phone;

import com.google.inject.ImplementedBy;
import org.iatoki.judgels.jophiel.user.profile.info.UserInfo;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@ImplementedBy(UserProfileServiceImpl.class)
public interface UserProfileService {

    boolean infoExists(String userJid);

    void updateProfile(String userJid, String name, boolean showName, String userIpAddress);

    void updateProfile(String userJid, String name, boolean showName, String password, String userIpAddress);

    void upsertInfo(String userJid, String gender, Date birthDate, String streetAddress, int postalCode, String institution, String city, String provinceOrState, String country, String shirtSize, String biodata, String userIpAddress);

    List<UserInfo> getUsersInfoByUserJids(Collection<String> userJids);

    UserInfo findInfo(String userJid);

    String updateAvatarWithGeneratedFilename(String userJid, File imageFile, String imageType, String ipAddress) throws IOException;

    String getAvatarImageUrlString(String imageName);
}
