package org.iatoki.judgels.jophiel.models.entities;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(RedirectURIModel.class)
public abstract class RedirectURIModel_ extends org.iatoki.judgels.play.models.entities.AbstractModel_ {

        public static volatile SingularAttribute<RedirectURIModel, Long> id;
        public static volatile SingularAttribute<RedirectURIModel, String> clientJid;
        public static volatile SingularAttribute<RedirectURIModel, String> redirectURI;
}
