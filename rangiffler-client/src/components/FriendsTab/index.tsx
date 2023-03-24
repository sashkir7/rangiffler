import {Grid} from "@mui/material";
import React, {FC, useContext, useEffect, useMemo, useState} from "react";
import {useOutletContext} from "react-router-dom";
import {CountryContext as MapCountryContext} from "react-svg-worldmap/dist/types";
import {apiClient} from "../../api/apiClient";
import {CountryContext} from "../../context/CountryContext/index";
import {ApiCountry, MapCountry, Photo} from "../../types/types";
import {LayoutContext} from "../Layout/index";
import {Map} from "../Map/index";
import {Photos} from "../Photos/index";


export const FriendsTab: FC = () => {

  const {countries} = useContext(CountryContext);
  const [friendsPhotos, setFriendsPhotos] = useState<Photo[]>([]);
  const [photoFilter, setPhotoFilter] = useState<string | null>(null);
  const {handlePhotoClick} = useOutletContext<LayoutContext>();
  const filteredPhotos = useMemo<Photo[]>(
      () => photoFilter ? friendsPhotos.filter(photo => photo.countryCode.toLowerCase() === photoFilter.toLowerCase()) : friendsPhotos
      , [photoFilter, friendsPhotos]);

  const [data, setData] = useState<MapCountry[]>([]);


  useEffect(() => {
    apiClient()
    .get("/friends/photos")
    .then(res => {
      if (res.data) {
        setFriendsPhotos(res.data.map((photo: any) => ({
          id: photo.id,
          src: photo.photo,
          description: photo.description,
          countryCode: photo.country.code,
          username: photo.username,
        } as Photo)));
      }
    });
  }, []);

  useEffect(() => {
    const countryData: MapCountry[] = [];
    countries.map((dataItem: ApiCountry) => {
      countryData.push({
        country: dataItem.code,
        value: friendsPhotos.filter(photo => photo.countryCode === dataItem.code).length || 0
      });
    });
    setData(countryData);
  }, [friendsPhotos]);

  const handleCountryClick = (context: MapCountryContext & {
    event: React.MouseEvent<SVGElement, Event>;
  }) => {
    setPhotoFilter(context.countryCode);
  };

  return (
      <>
        <Grid container direction='row' columns={2} spacing={2}>
          <Grid item style={{margin: "0 auto"}}>
            <Map data={data} handleCountryClick={handleCountryClick} photoFilter={photoFilter}
                 handleWholeWorldClick={() => setPhotoFilter(null)}/>
          </Grid>
        </Grid>
        <Photos handlePhotoClick={handlePhotoClick} photos={filteredPhotos}/>
      </>
  );
};
