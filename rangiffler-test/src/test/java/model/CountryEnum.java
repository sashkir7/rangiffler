package model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum CountryEnum {
    // ToDo Заполнить первую координату для каждой страны 
    //  используется для поиска страны на карте
    FIJI("Fiji", "FJ", ""),
    TANZANIA("Tanzania", "TZ", ""),
    WESTERN_SAHARA("Western Sahara", "EH", ""),
    CANADA("Canada", "CA", ""),
    UNITED_STATES("United States", "US", ""),
    KAZAKHSTAN("Kazakhstan", "KZ", ""),
    UZBEKISTAN("Uzbekistan", "UZ", ""),
    PAPUA_NEW_GUINEA("Papua New Guinea", "PG", ""),
    INDONESIA("Indonesia", "ID", ""),
    ARGENTINA("Argentina", "AR", ""),
    Chile("Chile", "CL", ""),
    DEMOCRATIC_REPUBLIC_OF_THE_CONGO("Democratic Republic of the Congo", "CD", ""),
    SOMALIA("Somalia", "SO", ""),
    KENYA("Kenya", "KE", ""),
    SUDAN("Sudan", "SD", ""),
    CHAD("Chad", "TD", ""),
    HAITI("Haiti", "HT", ""),
    DOMINICAN_REPUBLIC("Dominican Republic", "DO", ""),
    RUSSIA("Russia", "RU", "M957.096"),
    BAHAMAS("Bahamas", "BS", ""),
    FALKLAND_ISLANDS("Falkland Islands", "FK", ""),
    NORWAY("Norway", "NO", ""),
    GREENLAND("Greenland", "GL", ""),
    TIMOR_LESTE("Timor-Leste", "TL", ""),
    SOUTH_AFRICA("South Africa", "ZA", ""),
    LESOTHO("Lesotho", "LS", ""),
    MEXICO("Mexico", "MX", ""),
    URUGUAY("Uruguay", "UY", ""),
    BRAZIL("Brazil", "BR", ""),
    BOLIVIA("Bolivia", "BO", ""),
    PERU("Peru", "PE", ""),
    COLOMBIA("Colombia", "CO", ""),
    PANAMA("Panama", "PA", ""),
    COSTA_RICA("Costa Rica", "CR", ""),
    NICARAGUA("Nicaragua", "NI", ""),
    HONDURAS("Honduras", "HN", ""),
    EL_SALVADOR("El Salvador", "SV", ""),
    GUATEMALA("Guatemala", "GT", ""),
    BELIZE("Belize", "BZ", ""),
    VENEZUELA("Venezuela", "VE", ""),
    GUYANA("Guyana", "GY", ""),
    SURINAME("Suriname", "SR", ""),
    FRANCE("France", "FR", ""),
    ECUADOR("Ecuador", "EC", ""),
    PUERTO_RICO("Puerto Rico", "PR", ""),
    JAMAICA("Jamaica", "JM", ""),
    CUBA("Cuba", "CU", ""),
    ZIMBABWE("Zimbabwe", "ZW", ""),
    BOTSWANA("Botswana", "BW", ""),
    NAMIBIA("Namibia", "NA", ""),
    SENEGAL("Senegal", "SN", ""),
    MALI("Mali", "ML", ""),
    MAURITANIA("Mauritania", "MR", ""),
    BENIN("Benin", "BJ", ""),
    NIGER("Niger", "NE", ""),
    NIGERIA("Nigeria", "NG", ""),
    CAMEROON("Cameroon", "CM", ""),
    TOGO("Togo", "TG", ""),
    GHANA("Ghana", "GH", ""),
    COTED_IVOIRE("Côted\"Ivoire", "CI", ""),
    GUINEA("Guinea", "GN", ""),
    GUINEA_BISSAU("Guinea-Bissau", "GW", ""),
    LIBERIA("Liberia", "LR", ""),
    SIERRA_LEONE("Sierra Leone", "SL", ""),
    BURKINA_FASO("Burkina Faso", "BF", ""),
    CENTRAL_AFRICAN_REPUBLIC("Central African Republic", "CF", ""),
    REPUBLIC_OF_THE_CONGO("Republic of the Congo", "CG", ""),
    GABON("Gabon", "GA", ""),
    EQUATORIAL_GUINEA("Equatorial Guinea", "GQ", ""),
    ZAMBIA("Zambia", "ZM", ""),
    MALAWI("Malawi", "MW", ""),
    MOZAMBIQUE("Mozambique", "MZ", ""),
    ESWATINI("Eswatini", "SZ", ""),
    ANGOLA("Angola", "AO", ""),
    BURUNDI("Burundi", "BI", ""),
    ISRAEL("Israel", "IL", ""),
    LEBANON("Lebanon", "LB", ""),
    MADAGASCAR("Madagascar", "MG", ""),
    PALESTINE("Palestine", "PS", ""),
    THE_GAMBIA("The Gambia", "GM", ""),
    TUNISIA("Tunisia", "TN", ""),
    ALGERIA("Algeria", "DZ", ""),
    JORDAN("Jordan", "JO", ""),
    UNITED_ARAB_EMIRATES("United Arab Emirates", "AE", ""),
    QATAR("Qatar", "QA", ""),
    KUWAIT("Kuwait", "KW", ""),
    IRAQ("Iraq", "IQ", ""),
    OMAN("Oman", "OM", ""),
    VANUATU("Vanuatu", "VU", ""),
    CAMBODIA("Cambodia", "KH", ""),
    THAILAND("Thailand", "TH", ""),
    LAO_PDR("Lao PDR", "LA", ""),
    MYANMAR("Myanmar", "MM", ""),
    VIETNAM("Vietnam", "VN", ""),
    DEM_REP_KOREA("Dem. Rep. Korea", "KP", ""),
    REPUBLIC_OF_KOREA("Republic of Korea", "KR", ""),
    MONGOLIA("Mongolia", "MN", ""),
    INDIA("India", "IN", "M739.809"),
    BANGLADESH("Bangladesh", "BD", ""),
    BHUTAN("Bhutan", "BT", ""),
    NEPAL("Nepal", "NP", ""),
    PAKISTAN("Pakistan", "PK", ""),
    AFGHANISTAN("Afghanistan", "AF", ""),
    TAJIKISTAN("Tajikistan", "TJ", ""),
    KYRGYZSTAN("Kyrgyzstan", "KG", ""),
    TURKMENISTAN("Turkmenistan", "TM", ""),
    IRAN("Iran", "IR", ""),
    SYRIA("Syria", "SY", ""),
    ARMENIA("Armenia", "AM", ""),
    SWEDEN("Sweden", "SE", ""),
    BELARUS("Belarus", "BY", ""),
    UKRAINE("Ukraine", "UA", "M564.851"),
    POLAND("Poland", "PL", ""),
    AUSTRIA("Austria", "AT", "M525.327"),
    HUNGARY("Hungary", "HU", ""),
    MOLDOVA("Moldova", "MD", ""),
    ROMANIA("Romania", "RO", ""),
    LITHUANIA("Lithuania", "LT", ""),
    LATVIA("Latvia", "LV", ""),
    ESTONIA("Estonia", "EE", ""),
    GERMANY("Germany", "DE", ""),
    BULGARIA("Bulgaria", "BG", ""),
    GREECE("Greece", "GR", ""),
    TURKEY("Turkey", "TR", ""),
    ALBANIA("Albania", "AL", ""),
    CROATIA("Croatia", "HR", ""),
    SWITZERLAND("Switzerland", "CH", ""),
    LUXEMBOURG("Luxembourg", "LU", ""),
    BELGIUM("Belgium", "BE", ""),
    NETHERLANDS("Netherlands", "NL", ""),
    PORTUGAL("Portugal", "PT", ""),
    SPAIN("Spain", "ES", ""),
    IRELAND("Ireland", "IE", ""),
    NEW_CALEDONIA("New Caledonia", "NC", ""),
    SOLOMON_ISLANDS("Solomon Islands", "SB", ""),
    NEW_ZEALAND("New Zealand", "NZ", "M952.187"),
    AUSTRALIA("Australia", "AU", "M874.248"),
    SRI_LANKA("Sri Lanka", "LK", ""),
    CHINA("China", "CN", ""),
    TAIWAN("Taiwan", "TW", ""),
    ITALY("Italy", "IT", ""),
    DENMARK("Denmark", "DK", ""),
    UNITED_KINGDOM("United Kingdom", "GB", ""),
    ICELAND("Iceland", "IS", ""),
    AZERBAIJAN("Azerbaijan", "AZ", ""),
    GEORGIA("Georgia", "GE", ""),
    PHILIPPINES("Philippines", "PH", ""),
    MALAYSIA("Malaysia", "MY", ""),
    BRUNEI_DARUSSALAM("Brunei Darussalam", "BN", ""),
    SLOVENIA("Slovenia", "SI", ""),
    FINLAND("Finland", "FI", ""),
    SLOVAKIA("Slovakia", "SK", ""),
    CZECH_REPUBLIC("Czech Republic", "CZ", ""),
    ERITREA("Eritrea", "ER", ""),
    JAPAN("Japan", "JP", ""),
    PARAGUAY("Paraguay", "PY", ""),
    YEMEN("Yemen", "YE", ""),
    SAUDI_ARABIA("Saudi Arabia", "SA", "M573.313"),
    NORTHERN_CYPRUS("Northern Cyprus", "CYP", ""),
    CYPRUS("Cyprus", "CY", ""),
    MOROCCO("Morocco", "MA", ""),
    EGYPT("Egypt", "EG", ""),
    LIBYA("Libya", "LY", ""),
    ETHIOPIA("Ethiopia", "ET", ""),
    DJIBOUTI("Djibouti", "DJ", ""),
    SOMALILAND("Somaliland", "SOM", ""),
    UGANDA("Uganda", "UG", ""),
    RWANDA("Rwanda", "RW", ""),
    BOSNIA_AND_HERZEGOVINA("Bosnia and Herzegovina", "BA", ""),
    MACEDONIA("Macedonia", "MK", ""),
    SERBIA("Serbia", "RS", ""),
    MONTENEGRO("Montenegro", "ME", ""),
    KOSOVO("Kosovo", "XK", ""),
    TRINIDAD_AND_TOBAGO("Trinidad and Tobago", "TT", ""),
    SOUTH_SUDAN("South Sudan", "SS", "");

    private final String name;
    @Getter
    private final String code, firstCoordinate;

    public static CountryEnum fromCode(String code) {
        for (CountryEnum country : CountryEnum.values()) {
            if (country.getCode().equals(code))
                return country;
        }
        throw new IllegalArgumentException("Country by code " + code + " not found");
    }

    @Override
    public String toString() {
        return name;
    }

}
