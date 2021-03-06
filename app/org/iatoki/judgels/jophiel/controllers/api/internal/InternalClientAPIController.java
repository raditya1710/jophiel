package org.iatoki.judgels.jophiel.controllers.api.internal;

import com.google.gson.Gson;
import org.iatoki.judgels.AutoComplete;
import org.iatoki.judgels.jophiel.client.Client;
import org.iatoki.judgels.jophiel.controllers.api.AbstractJophielAPIController;
import org.iatoki.judgels.jophiel.controllers.securities.Authenticated;
import org.iatoki.judgels.jophiel.controllers.securities.LoggedIn;
import org.iatoki.judgels.jophiel.client.ClientService;
import play.db.jpa.Transactional;
import play.mvc.Result;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.util.List;
import java.util.stream.Collectors;

@Singleton
@Named
public final class InternalClientAPIController extends AbstractJophielAPIController {

    private final ClientService clientService;

    @Inject
    public InternalClientAPIController(ClientService clientService) {
        this.clientService = clientService;
    }

    @Authenticated(LoggedIn.class)
    @Transactional(readOnly = true)
    public Result autocompleteClient(String term) {
        List<Client> clients = clientService.getClientsByTerm(term);
        List<AutoComplete> autocompletedClients = clients.stream()
                .map(c -> new AutoComplete("" + c.getId(), c.getName(), c.getName()))
                .collect(Collectors.toList());
        return ok(new Gson().toJson(autocompletedClients));
    }
}
