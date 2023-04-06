import deer from "@img/deer-logo.svg";
import AddCircleOutlineIcon from "@mui/icons-material/AddCircleOutline";
import PhotoCameraIcon from "@mui/icons-material/PhotoCamera";
import GroupIcon from '@mui/icons-material/Group';
import PublicIcon from "@mui/icons-material/Public";
import LogoutIcon from '@mui/icons-material/Logout';
import {
  AppBar,
  Avatar,
  Button,
  ListItemIcon,
  Stack,
  Toolbar,
  Tooltip,
  Typography
} from "@mui/material";
import React, {FC, useContext} from "react";
import {Link} from "react-router-dom";
import {apiClient} from "../../api/apiClient";
import {AUTH_URL} from "../../api/config";
import {PhotoContext} from "../../context/PhotoContext/index";
import {UserContext} from "../../context/UserContext/index";
import {User} from "../../types/types";

interface HeaderInterface {
  handleAvatarClick: () => void;
  handleAddPhotoClick: () => void;
  handleFriendsIconClick: () => void;
  friends: User[];
}

export const Header: FC<HeaderInterface> = ({
                                              handleAvatarClick,
                                              handleAddPhotoClick,
                                              handleFriendsIconClick,
                                              friends
                                            }) => {
  const {user, handleChangeUser} = useContext(UserContext);
  const {photos} = useContext(PhotoContext);

  const getVisitedCountriesCount = () => {
    const countries = photos.map(p => p.countryCode);
    // @ts-ignore
    const unique = [...new Set(countries)];
    return unique.length;
  };
  const handleLogout = () => {
    apiClient().get(`${AUTH_URL}/logout`)
    .then(() => {
      sessionStorage.clear();
      handleChangeUser({
        username: "",
        firstName: "",
        lastName: "",
      } as User);
      location.pathname = "/landing";
    }).catch((err) => {
      console.error(err);
    })
  };

  return (
      <AppBar position="static" color="transparent">
        <Toolbar>
          <Link to={"/"}>
            <img src={deer} height={55} width={55} alt="Rangiffler logo"/>
          </Link>
          <Typography variant="h4" component="h1" sx={{flexGrow: 1, margin: "12px"}}>
            Rangiffler
          </Typography>
          <Stack direction='row' spacing={3}>
            <Button onClick={handleAddPhotoClick}
                    variant="contained"
                    startIcon={<AddCircleOutlineIcon/>}>Add photo</Button>
            <Stack direction="row" spacing={2}>
              <Avatar sx={{
                width: 48,
                height: 48,
                cursor: "pointer",
              }}
                      src={user?.avatar}
                      alt={user?.username}
                      onClick={handleAvatarClick}
              />
              <Stack direction='row'
                     spacing={0.5}
                     sx={{
                       display: "flex",
                       alignItems: "center"
                     }}
              >
                <ListItemIcon>
                  <Tooltip title="Your visited countries">
                    <Stack direction="row" gap={1}>
                      <PublicIcon/> {getVisitedCountriesCount()}
                    </Stack>
                  </Tooltip>
                </ListItemIcon>
                <ListItemIcon>
                  <Tooltip title="Your photos">
                    <Stack direction="row" gap={1}>
                      <PhotoCameraIcon/> {photos?.length}
                    </Stack>
                  </Tooltip>
                </ListItemIcon>
                <ListItemIcon>
                  <Tooltip title="Your friends">
                    <Stack direction="row" gap={1} onClick={handleFriendsIconClick}
                           sx={{cursor: "pointer"}}>
                      <GroupIcon/> {friends?.length}
                    </Stack>
                  </Tooltip>
                </ListItemIcon>
                <ListItemIcon>
                  <Tooltip title="Logout">
                    <Stack direction="row" gap={1}
                           sx={{cursor: "pointer"}}>
                      <LogoutIcon onClick={handleLogout}/>
                    </Stack>
                  </Tooltip>
                </ListItemIcon>
              </Stack>
            </Stack>
          </Stack>
        </Toolbar>
      </AppBar>
  );
};
