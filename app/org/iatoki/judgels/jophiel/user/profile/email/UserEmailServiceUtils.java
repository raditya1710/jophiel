package org.iatoki.judgels.jophiel.user.profile.email;

import org.iatoki.judgels.play.JudgelsPlayUtils;

import java.util.UUID;

final class UserEmailServiceUtils {

    private UserEmailServiceUtils() {
        // prevent instantiation
    }

    static UserEmail createUserEmailFromModel(UserEmailModel userEmailModel) {
        return new UserEmail(userEmailModel.id, userEmailModel.jid, userEmailModel.userJid, userEmailModel.email, userEmailModel.emailVerified);
    }

    static UserEmailModel persistEmail(UserEmailDao userEmailDao, String userJid, String email, String userIpAddress) {
        UserEmailModel userEmailModel = new UserEmailModel();
        userEmailModel.email = email;
        userEmailModel.userJid = userJid;
        userEmailModel.emailVerified = false;
        userEmailModel.emailCode = JudgelsPlayUtils.hashMD5(UUID.randomUUID().toString());

        userEmailDao.persist(userEmailModel, userJid, userIpAddress);

        return userEmailModel;
    }
}
