package org.iatoki.judgels.jophiel.controllers;

import com.google.common.collect.ImmutableList;
import org.iatoki.judgels.play.IdentityUtils;
import org.iatoki.judgels.play.InternalLink;
import org.iatoki.judgels.play.LazyHtml;
import org.iatoki.judgels.play.controllers.AbstractJudgelsControllerUtils;
import org.iatoki.judgels.play.views.html.layouts.menusLayout;
import org.iatoki.judgels.play.views.html.layouts.profileView;
import org.iatoki.judgels.play.views.html.layouts.sidebarLayout;
import org.iatoki.judgels.jophiel.JophielUtils;
import org.iatoki.judgels.jophiel.services.UserActivityService;
import org.iatoki.judgels.jophiel.views.html.client.linkedClientsLayout;
import play.i18n.Messages;
import play.mvc.Http;

public final class ControllerUtils extends AbstractJudgelsControllerUtils {

    private static final ControllerUtils INSTANCE = new ControllerUtils();

    @Override
    public void appendSidebarLayout(LazyHtml content) {
        ImmutableList.Builder<InternalLink> internalLinkBuilder = ImmutableList.builder();
        internalLinkBuilder.add(new InternalLink(Messages.get("welcome.welcome"), routes.WelcomeController.index()));
        internalLinkBuilder.add(new InternalLink(Messages.get("profile.profile"), routes.UserProfileController.profile()));
        if (JophielUtils.hasRole("admin")) {
            internalLinkBuilder.add(new InternalLink(Messages.get("user.users"), routes.UserController.index()));
            internalLinkBuilder.add(new InternalLink(Messages.get("user.activities"), routes.UserActivityController.index()));
            internalLinkBuilder.add(new InternalLink(Messages.get("client.clients"), routes.ClientController.index()));
        }

        LazyHtml sidebarContent = new LazyHtml(profileView.render(
              IdentityUtils.getUsername(),
              IdentityUtils.getUserRealName(),
              org.iatoki.judgels.jophiel.controllers.routes.UserProfileController.profile().absoluteURL(Http.Context.current().request()),
              org.iatoki.judgels.jophiel.controllers.routes.UserAccountController.logout().absoluteURL(Http.Context.current().request())
        ));
        sidebarContent.appendLayout(c -> menusLayout.render(internalLinkBuilder.build(), c));
        sidebarContent.appendLayout(c -> linkedClientsLayout.render(org.iatoki.judgels.jophiel.controllers.apis.routes.ClientAPIController.linkedClientList().path(), "lib/jophielcommons/javascripts/linkedClients.js", c));
        content.appendLayout(c -> sidebarLayout.render(sidebarContent.render(), c));
    }

    public void addActivityLog(UserActivityService userActivityService, String log) {
        userActivityService.createUserActivity("localhost", IdentityUtils.getUserJid(), System.currentTimeMillis(), log, IdentityUtils.getIpAddress());
    }

    static ControllerUtils getInstance() {
        return INSTANCE;
    }
}
