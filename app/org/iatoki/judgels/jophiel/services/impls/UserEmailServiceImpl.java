package org.iatoki.judgels.jophiel.services.impls;

import org.iatoki.judgels.play.IdentityUtils;
import org.iatoki.judgels.play.JudgelsPlayProperties;
import org.iatoki.judgels.jophiel.JophielProperties;
import org.iatoki.judgels.jophiel.models.daos.UserDao;
import org.iatoki.judgels.jophiel.models.daos.UserEmailDao;
import org.iatoki.judgels.jophiel.models.entities.UserEmailModel;
import org.iatoki.judgels.jophiel.models.entities.UserModel;
import org.iatoki.judgels.jophiel.services.UserEmailService;
import play.i18n.Messages;
import play.libs.mailer.Email;
import play.libs.mailer.MailerClient;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

@Singleton
@Named("userEmailService")
public final class UserEmailServiceImpl implements UserEmailService {

    private final UserDao userDao;
    private final UserEmailDao userEmailDao;
    private final MailerClient mailerClient;

    @Inject
    public UserEmailServiceImpl(UserDao userDao, UserEmailDao userEmailDao, MailerClient mailerClient) {
        this.userDao = userDao;
        this.userEmailDao = userEmailDao;
        this.mailerClient = mailerClient;
    }

    @Override
    public boolean isEmailOwnedByUser(String email, String username) {
        UserModel userModel = userDao.findByUsername(username);
        UserEmailModel emailModel = userEmailDao.findByEmail(email);

        return (emailModel.userJid.equals(userModel.jid));
    }

    @Override
    public boolean existByEmail(String email) {
        return userEmailDao.isExistByEmail(email);
    }

    @Override
    public boolean activateEmail(String emailCode) {
        if (userEmailDao.isExistByCode(emailCode)) {
            UserEmailModel emailModel = userEmailDao.findByCode(emailCode);
            if (!emailModel.emailVerified) {
                emailModel.emailVerified = true;

                userEmailDao.edit(emailModel, emailModel.userJid, IdentityUtils.getIpAddress());
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    @Override
    public boolean isEmailNotVerified(String userJid) {
        return userEmailDao.isExistNotVerifiedByUserJid(userJid);
    }

    @Override
    public String getEmailCodeOfUnverifiedEmail(String userJid) {
        UserEmailModel userEmailModel = userEmailDao.findByUserJid(userJid);
        return userEmailModel.emailCode;
    }

    @Override
    public void sendActivationEmail(String name, String email, String link) {
        Email mail = new Email();
        mail.setSubject(JudgelsPlayProperties.getInstance().getAppCopyright() + " " + Messages.get("registrationEmail.userRegistration"));
        mail.setFrom(JophielProperties.getInstance().getNoreplyName() + " <" + JophielProperties.getInstance().getNoreplyEmail() + ">");
        mail.addTo(name + " <" + email + ">");
        mail.setBodyHtml("<p>" + Messages.get("registrationEmail.thankYou") + " " + JudgelsPlayProperties.getInstance().getAppCopyright() + ".</p><p>" + Messages.get("registrationEmail.pleaseActivate") + " <a href='" + link + "'>here</a>.</p>");
        mailerClient.send(mail);
    }

    @Override
    public void sendChangePasswordEmail(String email, String link) {
        Email mail = new Email();
        mail.setSubject(JudgelsPlayProperties.getInstance().getAppCopyright() + " " + Messages.get("forgotPasswordEmail.forgotPassword"));
        mail.setFrom(JophielProperties.getInstance().getNoreplyName() + " <" + JophielProperties.getInstance().getNoreplyEmail() + ">");
        mail.addTo(email);
        mail.setBodyHtml("<p>" + Messages.get("forgotPasswordEmail.request") + " " + JudgelsPlayProperties.getInstance().getAppCopyright() + ".</p><p>" + Messages.get("forgotPasswordEmail.changePassword") + " <a href='" + link + "'>here</a>.</p>");
        mailerClient.send(mail);
    }
}
