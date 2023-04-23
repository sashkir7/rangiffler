package data.repository.hibernate;

import data.DataBase;
import data.entity.CountryEntity;
import data.jpa.EmfContext;
import data.jpa.JpaService;
import data.repository.CountryRepository;

import java.util.List;

public class HibernateCountryRepository extends JpaService implements CountryRepository {

    public HibernateCountryRepository() {
        super(EmfContext.INSTANCE.getEmf(DataBase.GEO).createEntityManager());
    }

    public List<CountryEntity> getAllCountries() {
        return em.createQuery("SELECT c FROM CountryEntity c", CountryEntity.class)
                .getResultList();
    }

}
