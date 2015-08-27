package org.iatoki.judgels.jophiel.models.entities;

import org.iatoki.judgels.play.models.entities.AbstractModel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "jophiel_city")
public final class CityModel extends AbstractModel {

    @Id
    @GeneratedValue
    public long id;

    @Column(columnDefinition = "TEXT", unique = true)
    public String city;

    public int referenceCount;
}