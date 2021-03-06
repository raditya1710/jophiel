package org.iatoki.judgels.jophiel.controllers.api.internal;

import org.iatoki.judgels.AutoComplete;
import org.iatoki.judgels.jophiel.user.profile.info.City;
import org.iatoki.judgels.jophiel.controllers.api.AbstractJophielAPIController;
import org.iatoki.judgels.jophiel.controllers.securities.Authenticated;
import org.iatoki.judgels.jophiel.controllers.securities.LoggedIn;
import org.iatoki.judgels.jophiel.user.profile.info.CityService;
import play.db.jpa.Transactional;
import play.mvc.Result;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.util.List;
import java.util.stream.Collectors;

@Singleton
@Named
public final class InternalCityAPIController extends AbstractJophielAPIController {

    private final CityService cityService;

    @Inject
    public InternalCityAPIController(CityService cityService) {
        this.cityService = cityService;
    }

    @Authenticated(LoggedIn.class)
    @Transactional(readOnly = true)
    public Result autocompleteCity(String term) {
        List<City> cities = cityService.getCitiesByTerm(term);
        List<AutoComplete> autocompletedCities = cities.stream()
                .map(c -> new AutoComplete("" + c.getId(), c.getName(), c.getName()))
                .collect(Collectors.toList());
        return okAsJson(autocompletedCities);
    }
}
