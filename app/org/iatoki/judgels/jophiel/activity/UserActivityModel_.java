package org.iatoki.judgels.jophiel.activity;

import org.iatoki.judgels.play.model.AbstractModel_;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(UserActivityModel.class)
public abstract class UserActivityModel_ extends AbstractModel_ {

        public static volatile SingularAttribute<UserActivityModel, Long> id;
        public static volatile SingularAttribute<UserActivityModel, String> clientJid;
        public static volatile SingularAttribute<UserActivityModel, String> time;
        public static volatile SingularAttribute<UserActivityModel, String> log;
}
