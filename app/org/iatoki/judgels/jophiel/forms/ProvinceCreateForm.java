package org.iatoki.judgels.jophiel.forms;

import play.data.validation.Constraints;

public final class ProvinceCreateForm {

    @Constraints.Required
    public String name;
}