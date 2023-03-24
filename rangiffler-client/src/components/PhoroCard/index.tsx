import {LoadingButton} from "@mui/lab";
import {
  Box,
  Card,
  CardActions,
  CardContent,
  CardMedia,
  Grid,
  IconButton,
  MenuItem,
  Select,
  TextField,
  Typography
} from "@mui/material";
import React, {FC, FormEvent, useContext, useState} from "react";
import CloseIcon from '@mui/icons-material/Close';
import EditIcon from '@mui/icons-material/Edit';
import DeleteOutlineIcon from '@mui/icons-material/DeleteOutline';
import PlaceIcon from '@mui/icons-material/Place';
import {apiClient} from "../../api/apiClient";
import {CountryContext} from "../../context/CountryContext/index";
import {PhotoContext} from "../../context/PhotoContext/index";
import {UserContext} from "../../context/UserContext/index";
import {Photo} from "../../types/types";
import {ImageUpload} from "../ImageUpload/index";
import imageMock from "@img/uploadImageMock.png";

interface PhotoCardInterface {
  photo: Partial<Photo> | null;
  onClose: () => void;
  initSubmitPopupAndOpen: (text: string, buttonText: string, onSubmit: () => void) => void;
}

export const PhotoCard: FC<PhotoCardInterface> = ({photo, onClose, initSubmitPopupAndOpen}) => {
  const {countries} = useContext(CountryContext);
  const {user} = useContext(UserContext);
  const {handleAddPhoto, handleEditPhoto, handleDeletePhoto} = useContext(PhotoContext);
  const initialPhotoCard: Photo = {
    id: "",
    username: "",
    src: "",
    description: "",
    countryCode: countries[0].code,
  }
  const [photoData, setPhotoData] = useState<Partial<Photo>>(photo ?? initialPhotoCard);
  const [edit, setEdit] = useState<boolean>(photo === null);
  const onEditClick = () => {
    setEdit((prevState) => !prevState);
  };

  const deletePhotoHandler = (photoId: string) => {
    apiClient().delete("/photos", {
      params: {
        photoId
      }
    }).then(() => {
      handleDeletePhoto(photoId);
      onClose();
    });
  }

  const onDeleteClick = (photo: Partial<Photo>) => {
    initSubmitPopupAndOpen("You are going to delete photo. Are you sure?", "Delete", () => deletePhotoHandler(photo.id!));
  };

  const selectOptions = countries?.map(country => (
      <MenuItem value={country.code} key={country.code}>{country.name}</MenuItem>));
  const handleAddPhotoClick = (evt: FormEvent<HTMLFormElement>) => {
    evt.preventDefault();
    apiClient().post("/photos", {
      photo: photoData?.src,
      description: photoData?.description,
      country: countries.find(c => c.code === photoData?.countryCode),
    }).then((res) => {
      const ph = res.data;
      handleAddPhoto({
        id: ph.id,
        src: ph.photo,
        description: ph.description,
        countryCode: ph.country.code,
        username: ph.username,
      });
      onClose();
    });
  };

  const handleEditPhotoClick = (evt: FormEvent<HTMLFormElement>) => {
    evt.preventDefault();
    apiClient().patch(`/photos/${photo?.id}`, {
      id: photoData.id,
      photo: photoData.src,
      description: photoData?.description,
      country: countries.find(c => c.code === photoData?.countryCode),
      username: photoData.username
    }).then((res) => {
      const ph = res.data;
      handleEditPhoto({
        id: ph.id,
        src: ph.photo,
        description: ph.description,
        countryCode: ph.country.code,
        username: ph.username,
      });
      onClose();
    })
  };

  return (
      <Box sx={{
        width: "100%",
        display: "block",
        backgroundColor: "rgba(0, 0, 0, 0.4)",
        height: "160vh",
        position: "absolute",
        zIndex: "4",
      }}>
        <Box sx={{
          position: "fixed",
          zIndex: "5",
          top: "55%",
          left: "50%",
          transform: "translate(-50%, -50%)",
        }}
             width="600px">
          <Card>
            <CardActions sx={{
              display: "flex",
              flexDirection: "row",
              justifyContent: "flex-end",
            }}>
              {
                  photo && photo?.username === user?.username &&
                  (<IconButton size='small' onClick={onEditClick}>
                    <EditIcon/>
                  </IconButton>)
              }
              {
                  photo && photo?.username === user?.username &&
                  (<IconButton size='small' onClick={() => onDeleteClick(photo)}>
                    <DeleteOutlineIcon/>
                  </IconButton>)
              }
              <IconButton size='small' onClick={onClose}>
                <CloseIcon/>
              </IconButton>
            </CardActions>
            <CardContent sx={{
              width: "500px",
              margin: "0 auto",
            }}>
              {
                edit ? (
                        <>
                          <form onSubmit={!photo ? handleAddPhotoClick : handleEditPhotoClick}>
                            <Grid container direction="column" sx={{
                              justifyContent: "center",
                            }}>
                              <Grid item sx={{
                                display: "flex",
                                textAlign: "center",
                                position: "relative",
                                justifyContent: "center",

                              }}>
                                <CardMedia
                                    sx={{
                                      width: "500px",
                                      height: "500px",
                                      objectFit: "fit-content"
                                    }}
                                    component="img"
                                    image={photoData?.src || imageMock}
                                    alt="New image"
                                />
                                <Grid sx={{
                                  position: "absolute",
                                  right: "10%",
                                  top: "85%",
                                }}>
                                  {!photo?.src &&
                                      <>
                                        <ImageUpload
                                            handlePhotoChange={(photo) => setPhotoData({
                                              ...photoData,
                                              src: photo,
                                            })}
                                        />
                                      </>
                                  }
                                </Grid>
                              </Grid>
                              <Grid item sx={{textAlign: "center"}}>
                                <Select
                                    sx={{
                                      width: "500px",
                                      margin: "12px",
                                      textAlign: "left",
                                    }}
                                    required
                                    size="small"
                                    value={photoData?.countryCode}
                                    MenuProps={{PaperProps: {sx: {maxHeight: 170}}}}
                                    onChange={event => setPhotoData({
                                      ...photoData,
                                      countryCode: event.target.value
                                    })}>
                                  {selectOptions}
                                </Select>
                              </Grid>
                              <Grid item sx={{textAlign: "center"}}>
                                <TextField
                                    multiline
                                    minRows={2}
                                    maxRows={5}
                                    sx={{
                                      width: "500px",
                                      margin: "12px",
                                    }}
                                    label='Description'
                                    size='small'
                                    value={photoData?.description}
                                    onChange={event => setPhotoData({
                                      ...photoData,
                                      description: event.target.value
                                    })}
                                />
                              </Grid>
                              <Grid item sx={{textAlign: "center"}}>
                                <LoadingButton variant="contained" type="submit"
                                               disabled={!photoData?.src}>Save</LoadingButton>
                              </Grid>
                            </Grid>
                          </form>
                        </>)
                    : (
                        <>
                          <CardMedia
                              sx={{
                                width: "500px",
                                height: "500px",
                                objectFit: "fit-content"
                              }}
                              component="img"
                              image={photo?.src}
                              alt={photo?.description}
                          />
                          <Typography gutterBottom variant="h6" component="p">
                            <PlaceIcon
                                sx={{verticalAlign: "text-top"}}/>{countries.find(c => c.code === photo?.countryCode)?.name}
                          </Typography>
                          <Typography variant="body2">
                            {photo?.description}
                          </Typography>
                        </>)
              }
            </CardContent>
          </Card>
        </Box>
      </Box>
  )
};
