package org.iatoki.judgels.jophiel.oauth2;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(AccessTokenModel.class)
public abstract class AccessTokenModel_ extends org.iatoki.judgels.play.models.entities.AbstractModel_ {

        public static volatile SingularAttribute<AccessTokenModel, Long> id;
        public static volatile SingularAttribute<AccessTokenModel, String> code;
        public static volatile SingularAttribute<AccessTokenModel, String> userJid;
        public static volatile SingularAttribute<AccessTokenModel, String> clientJid;
        public static volatile SingularAttribute<AccessTokenModel, String> token;
        public static volatile SingularAttribute<AccessTokenModel, Long> expireTime;
        public static volatile SingularAttribute<AccessTokenModel, Boolean> redeemed;
        public static volatile SingularAttribute<AccessTokenModel, String> scopes;
}