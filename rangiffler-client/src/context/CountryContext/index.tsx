import {createContext} from "react";
import {ApiCountry} from "../../types/types";

interface CountryContextInterface {
  countries: Array<ApiCountry>;
}

const defaultState = {
  countries: [],
};

export const CountryContext = createContext<CountryContextInterface>(defaultState);
