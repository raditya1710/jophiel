package org.iatoki.judgels.jophiel.models.daos;

import org.iatoki.judgels.play.models.daos.Dao;
import org.iatoki.judgels.jophiel.models.entities.AccessTokenModel;

public interface AccessTokenDao extends Dao<Long, AccessTokenModel> {

    boolean existsValidByTokenAndTime(String token, long time);

    AccessTokenModel findByCode(String code);

    AccessTokenModel findByToken(String token);
}
