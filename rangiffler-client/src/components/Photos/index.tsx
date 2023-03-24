import {Box, Button, ImageList, ImageListItem, Typography} from "@mui/material";
import React, {FC, useContext} from "react";
import {CountryContext} from "../../context/CountryContext/index";
import {Photo} from "../../types/types";
import PlaceIcon from '@mui/icons-material/Place';
import "./styles.scss";

interface PhotosInterface {
  photos: Array<Photo>;
  handlePhotoClick: (item: Photo) => void;
}

export const Photos: FC<PhotosInterface> = ({photos, handlePhotoClick}) => {
  const {countries} = useContext(CountryContext);

  return (
      <Box sx={{margin: "0 auto", width: 1200, height: 650, overflowY: 'scroll'}}>
        <ImageList
            cols={4}
            gap={14}
        >
          {photos.map((item) => (
              <ImageListItem key={item.id} sx={{
                display: "block"
              }}>
                <Box>
                  <Button
                      onClick={() => handlePhotoClick(item)}>
                    <img
                        className={"photo__list-item"}
                        src={item.src}
                        width={270}
                        height={270}
                        alt={item.description}
                        loading='lazy'
                    />
                  </Button>
                  <span>
                    <Typography sx={{padding: "12px", textAlign: "center"}}>
                        <PlaceIcon
                            sx={{verticalAlign: "bottom"}}/>{countries.find(country => country.code === item.countryCode)?.name}
                    </Typography>
                  </span>
                </Box>
              </ImageListItem>
          ))}
        </ImageList>
      </Box>
  );
};
