package org.rangiffler.service;

import java.util.List;
import java.util.UUID;
import org.rangiffler.model.CountryJson;
import org.springframework.stereotype.Service;

@Service
public class CountryService {

  private final List<CountryJson> countries = List.of(
      CountryJson.builder()
          .id(UUID.randomUUID())
          .code("ru")
          .name("Russia")
          .build(),
      CountryJson.builder()
          .id(UUID.randomUUID())
          .code("it")
          .name("Italy")
          .build(),
      CountryJson.builder()
          .id(UUID.randomUUID())
          .code("de")
          .name("Germany")
          .build(),
      CountryJson.builder()
          .id(UUID.randomUUID())
          .code("fr")
          .name("France")
          .build(),
      CountryJson.builder()
          .id(UUID.randomUUID())
          .code("FJ")
          .name("Fiji")
          .build(),
      CountryJson.builder()
          .id(UUID.randomUUID())
          .code("TZ")
          .name("Tanzania")
          .build(),
      CountryJson.builder()
          .id(UUID.randomUUID())
          .code("EH")
          .name("Western Sahara")
          .build(),
      CountryJson.builder()
          .id(UUID.randomUUID())
          .code("CA")
          .name("Canada")
          .build(),
      CountryJson.builder()
          .id(UUID.randomUUID())
          .code("US")
          .name("United States")
          .build(),
      CountryJson.builder()
          .id(UUID.randomUUID())
          .code("KZ")
          .name("Kazakhstan")
          .build());

  public List<CountryJson> getAllCountries() {
    return countries;
  }

  public CountryJson getCountryByCode(String code) {
    return countries.stream().filter(c -> c.getCode().equals(code)).findFirst().orElseThrow();
  }
}
