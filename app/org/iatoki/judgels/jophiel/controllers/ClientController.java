package org.iatoki.judgels.jophiel.controllers;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.iatoki.judgels.commons.InternalLink;
import org.iatoki.judgels.commons.LazyHtml;
import org.iatoki.judgels.commons.Page;
import org.iatoki.judgels.commons.controllers.BaseController;
import org.iatoki.judgels.commons.views.html.layouts.headingLayout;
import org.iatoki.judgels.commons.views.html.layouts.headingWithActionLayout;
import org.iatoki.judgels.jophiel.Client;
import org.iatoki.judgels.jophiel.ClientNotFoundException;
import org.iatoki.judgels.jophiel.controllers.forms.ClientCreateForm;
import org.iatoki.judgels.jophiel.controllers.forms.ClientUpdateForm;
import org.iatoki.judgels.jophiel.controllers.securities.Authenticated;
import org.iatoki.judgels.jophiel.controllers.securities.Authorized;
import org.iatoki.judgels.jophiel.controllers.securities.HasRole;
import org.iatoki.judgels.jophiel.controllers.securities.LoggedIn;
import org.iatoki.judgels.jophiel.services.ClientService;
import org.iatoki.judgels.jophiel.services.UserActivityService;
import org.iatoki.judgels.jophiel.views.html.client.createClientView;
import org.iatoki.judgels.jophiel.views.html.client.listClientsView;
import org.iatoki.judgels.jophiel.views.html.client.updateClientView;
import org.iatoki.judgels.jophiel.views.html.client.viewClientView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import play.data.Form;
import play.db.jpa.Transactional;
import play.filters.csrf.AddCSRFToken;
import play.filters.csrf.RequireCSRFCheck;
import play.i18n.Messages;
import play.mvc.Http;
import play.mvc.Result;

import java.util.Arrays;

@Authenticated(value = {LoggedIn.class, HasRole.class})
@Authorized(value = {"admin"})
@Component
public final class ClientController extends BaseController {

    private static final long PAGE_SIZE = 20;
    @Autowired
    private ClientService clientService;
    @Autowired
    private UserActivityService userActivityService;


    public Result index() {
        return listClients(0, "id", "asc", "");
    }

    @Transactional
    @AddCSRFToken
    public Result createClient() {
        Form<ClientCreateForm> form = Form.form(ClientCreateForm.class);

        ControllerUtils.getInstance().addActivityLog(userActivityService, "Try to create client <a href=\"" + "http://" + Http.Context.current().request().host() + Http.Context.current().request().uri() + "\">link</a>.");

        return showCreateClient(form);
    }

    @Transactional
    @RequireCSRFCheck
    public Result postCreateClient() {
        Form<ClientCreateForm> form = Form.form(ClientCreateForm.class).bindFromRequest();

        if (form.hasErrors() || form.hasGlobalErrors()) {
            return showCreateClient(form);
        } else {
            ClientCreateForm clientCreateForm = form.get();
            clientService.createClient(clientCreateForm.name, clientCreateForm.applicationType, clientCreateForm.scopes, Arrays.asList(clientCreateForm.redirectURIs.split(",")));

            ControllerUtils.getInstance().addActivityLog(userActivityService, "Create client " + clientCreateForm.name + ".");

            return redirect(routes.ClientController.index());
        }
    }

    @Transactional
    public Result viewClient(long clientId) throws ClientNotFoundException {
        Client client = clientService.findClientById(clientId);
        LazyHtml content = new LazyHtml(viewClientView.render(client));
        content.appendLayout(c -> headingWithActionLayout.render(Messages.get("client.client") + " #" + clientId + ": " + client.getName(), new InternalLink(Messages.get("commons.update"), routes.ClientController.updateClient(clientId)), c));
        ControllerUtils.getInstance().appendSidebarLayout(content);
        ControllerUtils.getInstance().appendBreadcrumbsLayout(content, ImmutableList.of(
                new InternalLink(Messages.get("client.clients"), routes.ClientController.index()),
                new InternalLink(Messages.get("client.view"), routes.ClientController.viewClient(clientId))
        ));
        ControllerUtils.getInstance().appendTemplateLayout(content, "Client - View");

        ControllerUtils.getInstance().addActivityLog(userActivityService, "View client " + client.getName() + " <a href=\"" + "http://" + Http.Context.current().request().host() + Http.Context.current().request().uri() + "\">link</a>.");

        return ControllerUtils.getInstance().lazyOk(content);
    }

    @Transactional
    public Result listClients(long page, String orderBy, String orderDir, String filterString) {
        Page<Client> currentPage = clientService.pageClients(page, PAGE_SIZE, orderBy, orderDir, filterString);

        LazyHtml content = new LazyHtml(listClientsView.render(currentPage, orderBy, orderDir, filterString));
        content.appendLayout(c -> headingWithActionLayout.render(Messages.get("client.list"), new InternalLink(Messages.get("commons.create"), routes.ClientController.createClient()), c));
        ControllerUtils.getInstance().appendSidebarLayout(content);
        ControllerUtils.getInstance().appendBreadcrumbsLayout(content, ImmutableList.of(
                new InternalLink(Messages.get("client.clients"), routes.ClientController.index())
        ));
        ControllerUtils.getInstance().appendTemplateLayout(content, "Clients");

        ControllerUtils.getInstance().addActivityLog(userActivityService, "Open all clients <a href=\"" + "http://" + Http.Context.current().request().host() + Http.Context.current().request().uri() + "\">link</a>.");

        return ControllerUtils.getInstance().lazyOk(content);
    }

    @Transactional
    @AddCSRFToken
    public Result updateClient(long clientId) throws ClientNotFoundException {
        Client client = clientService.findClientById(clientId);
        ClientUpdateForm clientUpdateForm = new ClientUpdateForm();

        clientUpdateForm.name = client.getName();
        clientUpdateForm.redirectURIs = StringUtils.join(client.getRedirectURIs(), ",");
        clientUpdateForm.scopes = Lists.newArrayList(client.getScopes());

        Form<ClientUpdateForm> form = Form.form(ClientUpdateForm.class).fill(clientUpdateForm);

        ControllerUtils.getInstance().addActivityLog(userActivityService, "Try to update client " + client.getName() + " <a href=\"" + "http://" + Http.Context.current().request().host() + Http.Context.current().request().uri() + "\">link</a>.");

        return showUpdateClient(form, clientId, client.getName());
    }

    @Transactional
    public Result postUpdateClient(long clientId) throws ClientNotFoundException {
        Client client = clientService.findClientById(clientId);
        Form<ClientUpdateForm> form = Form.form(ClientUpdateForm.class).bindFromRequest();

        if (form.hasErrors()) {
            return showUpdateClient(form, client.getId(), client.getName());
        } else {
            ClientUpdateForm clientUpdateForm = form.get();
            clientService.updateClient(client.getId(), clientUpdateForm.name, clientUpdateForm.scopes, Arrays.asList(clientUpdateForm.redirectURIs.split(",")));

            ControllerUtils.getInstance().addActivityLog(userActivityService, "Update client " + client.getName() + ".");

            return redirect(routes.ClientController.index());
        }
    }

    @Transactional
    public Result deleteClient(long clientId) throws ClientNotFoundException {
        Client client = clientService.findClientById(clientId);
        clientService.deleteClient(client.getId());

        ControllerUtils.getInstance().addActivityLog(userActivityService, "Delete client " + client.getName() + " <a href=\"" + "http://" + Http.Context.current().request().host() + Http.Context.current().request().uri() + "\">link</a>.");

        return redirect(routes.ClientController.index());
    }

    private Result showCreateClient(Form<ClientCreateForm> form) {
        LazyHtml content = new LazyHtml(createClientView.render(form));
        content.appendLayout(c -> headingLayout.render(Messages.get("client.create"), c));
        ControllerUtils.getInstance().appendSidebarLayout(content);
        ControllerUtils.getInstance().appendBreadcrumbsLayout(content, ImmutableList.of(
                new InternalLink(Messages.get("client.clients"), routes.ClientController.index()),
                new InternalLink(Messages.get("client.create"), routes.ClientController.createClient())
        ));
        ControllerUtils.getInstance().appendTemplateLayout(content, "Client - Create");
        return ControllerUtils.getInstance().lazyOk(content);
    }

    private Result showUpdateClient(Form<ClientUpdateForm> form, long clientId, String clientName) {
        LazyHtml content = new LazyHtml(updateClientView.render(form, clientId));
        content.appendLayout(c -> headingLayout.render(Messages.get("client.client") + " #" + clientId + ": " + clientName, c));
        ControllerUtils.getInstance().appendSidebarLayout(content);
        ControllerUtils.getInstance().appendBreadcrumbsLayout(content, ImmutableList.of(
                new InternalLink(Messages.get("client.clients"), routes.ClientController.index()),
                new InternalLink(Messages.get("client.update"), routes.ClientController.updateClient(clientId))
        ));
        ControllerUtils.getInstance().appendTemplateLayout(content, "Client - Update");
        return ControllerUtils.getInstance().lazyOk(content);
    }
}
