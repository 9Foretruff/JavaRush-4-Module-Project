package com.javarush.domain.mapper;

import com.javarush.domain.entity.City;
import com.javarush.domain.entity.Country;
import com.javarush.domain.entity.CountryLanguage;
import com.javarush.domain.redis.entity.CityCountry;
import com.javarush.domain.redis.entity.Language;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class CityCountryMapper {

    public static List<CityCountry> transformData(List<City> cities) {
        return cities.stream().map(CityCountryMapper::transformCity).collect(Collectors.toList());
    }

    private static CityCountry transformCity(City city) {
        CityCountry res = new CityCountry();
        res.setId(city.getId());
        res.setName(city.getName());
        res.setPopulation(city.getPopulation());
        res.setDistrict(city.getDistrict());

        Country country = city.getCountry();
        res.setAlternativeCountryCode(country.getAlternativeCode());
        res.setContinent(country.getContinent());
        res.setCountryCode(country.getCode());
        res.setCountryName(country.getName());
        res.setCountryPopulation(country.getPopulation());
        res.setCountryRegion(country.getRegion());
        res.setCountrySurfaceArea(country.getSurfaceArea());
        Set<CountryLanguage> countryLanguages = country.getLanguages();
        res.setLanguages(transformLanguages(countryLanguages));

        return res;
    }

    private static Set<Language> transformLanguages(Set<CountryLanguage> countryLanguages) {
        return countryLanguages.stream().map(CityCountryMapper::transformLanguage).collect(Collectors.toSet());
    }

    private static Language transformLanguage(CountryLanguage cl) {
        Language language = new Language();
        language.setLanguage(cl.getLanguage());
        language.setIsOfficial(cl.getIsOfficial());
        language.setPercentage(cl.getPercentage());
        return language;
    }
}
