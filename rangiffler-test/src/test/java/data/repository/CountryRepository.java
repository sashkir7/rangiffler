package data.repository;

import data.entity.CountryEntity;

import java.util.List;

public interface CountryRepository extends Repository {

    List<CountryEntity> getAllCountries();

}
