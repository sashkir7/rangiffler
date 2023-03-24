import PublicIcon from "@mui/icons-material/Public";
import ZoomInIcon from "@mui/icons-material/ZoomIn";
import ZoomOutIcon from "@mui/icons-material/ZoomOut";
import {IconButton, Tooltip} from "@mui/material";
import React, {FC, useState} from "react";
import WorldMap from "react-svg-worldmap";
import {CountryContext} from "react-svg-worldmap/dist/types";
import {MapCountry} from "../../types/types";

interface MapInterface {
  data: MapCountry[];
  handleCountryClick: (context: CountryContext & {
    event: React.MouseEvent<SVGElement, Event>;
  }) => void;
  photoFilter: string | null;
  handleWholeWorldClick: () => void;
}

export const Map: FC<MapInterface> = ({
                                        data,
                                        handleCountryClick,
                                        photoFilter,
                                        handleWholeWorldClick
                                      }) => {

  const [zoomed, setZoomed] = useState<boolean>(false);

  return (
      <div style={{margin: "0 auto", position: "relative"}}>
        <Tooltip title="Back to whole world">
          <IconButton
              disabled={!Boolean(photoFilter)}
              onClick={handleWholeWorldClick}
              sx={{
                position: "absolute",
                top: 25,
                right: 60,
              }}
          >
            <PublicIcon sx={{
              verticalAlign: "sub",
              width: "32px",
              height: "32px",
            }}/>
          </IconButton>
        </Tooltip>
        <Tooltip title={zoomed ? "Zoom out" : "Zoom in"}>
          <IconButton onClick={() => setZoomed((prevState) => !prevState)}
                      sx={{
                        position: "absolute",
                        top: 20,
                        right: 0,
                      }}
          >
            {zoomed ? (
                <ZoomOutIcon sx={{
                  width: "50px",
                  height: "50px",
                }}/>
            ) : (
                <ZoomInIcon sx={{
                  width: "50px",
                  height: "50px",
                }}/>
            )}
          </IconButton>
        </Tooltip>
        <WorldMap
            color="#3c5548"
            value-suffix="photos"
            size={zoomed ? "xxl" : "xl"}
            data={data}
            onClickFunction={handleCountryClick}
        />
      </div>
  );
}
