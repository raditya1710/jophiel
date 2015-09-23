package org.iatoki.judgels.jophiel.models.daos.hibernate;

import org.iatoki.judgels.play.models.daos.impls.AbstractHibernateDao;
import org.iatoki.judgels.jophiel.models.daos.AuthorizationCodeDao;
import org.iatoki.judgels.jophiel.models.entities.AuthorizationCodeModel;
import org.iatoki.judgels.jophiel.models.entities.AuthorizationCodeModel_;
import play.db.jpa.JPA;

import javax.inject.Named;
import javax.inject.Singleton;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

@Singleton
@Named("authorizationCodeDao")
public final class AuthorizationCodeHibernateDao extends AbstractHibernateDao<Long, AuthorizationCodeModel> implements AuthorizationCodeDao {

    public AuthorizationCodeHibernateDao() {
        super(AuthorizationCodeModel.class);
    }

    @Override
    public boolean isAuthorized(String clientJid, String userJid, String scopes) {
        CriteriaBuilder cb = JPA.em().getCriteriaBuilder();
        CriteriaQuery<Long> query = cb.createQuery(Long.class);

        Root<AuthorizationCodeModel> root = query.from(AuthorizationCodeModel.class);

        query.select(cb.count(root)).where(cb.and(cb.equal(root.get(AuthorizationCodeModel_.clientJid), clientJid), cb.equal(root.get(AuthorizationCodeModel_.userJid), userJid), cb.equal(root.get(AuthorizationCodeModel_.scopes), scopes)));

        return (JPA.em().createQuery(query).getSingleResult() != 0);
    }

    @Override
    public AuthorizationCodeModel findByCode(String code) {
        CriteriaBuilder cb = JPA.em().getCriteriaBuilder();
        CriteriaQuery<AuthorizationCodeModel> query = cb.createQuery(AuthorizationCodeModel.class);

        Root<AuthorizationCodeModel> root =  query.from(AuthorizationCodeModel.class);

        query.where(cb.equal(root.get(AuthorizationCodeModel_.code), code));

        return JPA.em().createQuery(query).getSingleResult();
    }
}