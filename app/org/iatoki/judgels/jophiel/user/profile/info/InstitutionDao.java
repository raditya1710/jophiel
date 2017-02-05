package org.iatoki.judgels.jophiel.user.profile.info;

import com.google.inject.ImplementedBy;
import org.iatoki.judgels.play.model.Dao;

import java.util.Optional;

@ImplementedBy(InstitutionHibernateDao.class)
public interface InstitutionDao extends Dao<Long, InstitutionModel> {

    boolean existsByName(String name);

    Optional<InstitutionModel> findByName(String name);
}
