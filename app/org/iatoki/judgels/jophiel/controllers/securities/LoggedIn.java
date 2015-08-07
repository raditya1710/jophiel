package org.iatoki.judgels.jophiel.controllers.securities;

import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Security;

public class LoggedIn extends Security.Authenticator {

    @Override
    public String getUsername(Http.Context context) {
        return context.session().get("username");
    }

    @Override
    public Result onUnauthorized(Http.Context context) {
        return redirect(org.iatoki.judgels.jophiel.controllers.routes.UserAccountController.login());
    }
}