import { Alert } from "@mui/material";
import React, { useContext, useEffect, useState } from "react";
import {Outlet} from "react-router-dom";
import {apiClient} from "../../api/apiClient";
import { AlertMessageContext } from "../../context/AlertMessageContext/index";
import { CountryContext } from "../../context/CountryContext/index";
import { PhotoContext } from "../../context/PhotoContext/index";
import { ApiCountry, Photo, User } from "../../types/types";
import {FriendsPopup} from "../FriendsPopup/index";
import {Header} from "../Header/index";
import {PhotoCard} from "../PhoroCard/index";
import {Popup} from "../Popup/index";
import {Profile} from "../Profile/index";


export type LayoutContext = {
  handlePhotoClick: (item: Photo) => void;
  initSubmitPopupAndOpen: (text: string, buttonText: string, onSubmit: () => void) => void;
  handleClosePopup: () => {};
};

export const Layout = () => {

  const {error, message, addMessage, addError} = useContext(AlertMessageContext);
  //countries data
  const [countries, setCountries] = useState<Array<ApiCountry>>([]);

  //user photos data
  const [userPhotos, setUserPhotos] = useState<Array<Photo>>([]);
  const handleAddPhoto = (photo: Photo) => {
    setUserPhotos([...userPhotos, photo]);
  };

  const handleEditPhoto = (photo: Photo) => {
    const newArr = [...userPhotos];
    const ph = newArr.find(ph => ph.id === photo.id);
    if (ph) {
      newArr[newArr.indexOf(ph)] = photo;
      setUserPhotos(newArr);
      addMessage("Changes saved");
    }
  };

  const handleDeletePhoto = (photoId: string) => {
    setUserPhotos(userPhotos.filter(ph => ph.id !== photoId));
    addMessage("Photo deleted");
  };


  //popup state
  const [photoCardOpen, setPhotoCardOpen] = useState<boolean>(false);
  const [profileOpen, setProfileOpen] = useState<boolean>(false);
  const [friendsPopupOpen, setFriendsPopupOpen] = useState<boolean>(false);
  const [submitPopupOpen, setSubmitPopupOpen] = useState<boolean>(false);
  const [submitPopupData, setSubmitPopupData] = useState<{ text: string, buttonText: string, onSubmit: () => void }>({
    text: "",
    buttonText: "Submit",
    onSubmit: () => {
    },
  });
  const [selectedItem, setSelectedItem] = useState<Partial<Photo> | null>(null);

  //friends list
  const [friendsData, setFriendsData] = useState<User[]>([]);

  useEffect(() => {
    apiClient().get("/countries")
        .then((res) => {
          if (res.data) {
            setCountries(res.data);
          }
        });

    apiClient().get("/friends")
      .then((res) => {
        setFriendsData(res.data);
      });

    apiClient().get("/photos")
      .then((res) => {
        if (res.data) {
          setUserPhotos(res.data.map((photo: any) => ({
            id: photo.id,
            src: photo.photo,
            description: photo.description,
            countryCode: photo.country.code,
            username: photo.username,
          } as Photo)));
        }
      });
  }, []);

  const initSubmitPopupAndOpen = (text: string, buttonText: string, onSubmit: () => void) => {
    setSubmitPopupData({text, buttonText, onSubmit});
    setSubmitPopupOpen(true);
    setPhotoCardOpen(false);
    setProfileOpen(false);
    setFriendsPopupOpen(false);
  };


  const handleDeleteFriend = (user: User) => {
    initSubmitPopupAndOpen("Delete friend?", "Delete", () => {
      apiClient().post("friends/remove", {
        ...user
      }).then(() => {
        setFriendsData(friendsData.filter(f => f.id !== user.id));
        handleClosePopup();
        addMessage(`You're not friends with user ${user.username} anymore`);
      }).catch((err) => {
        console.error(err);
        addError(`Friends is not deleted. Reason: ${err.message}`);
      });
    });
  };

  const handleAddPhotoClick = () => {
    setSelectedItem(null);
    setPhotoCardOpen(true);
    setProfileOpen(false);
    setSubmitPopupOpen(false);
  }

  const handleAvatarClick = () => {
    setProfileOpen(true);
    setPhotoCardOpen(false);
    setSubmitPopupOpen(false);
  };

  const handleClosePopup = () => {
    setPhotoCardOpen(false);
    setProfileOpen(false);
    setSubmitPopupOpen(false);
    setFriendsPopupOpen(false);
    setSelectedItem(null);
  }

  const handlePhotoClick = (item: Photo) => {
    setSelectedItem(item);
    setPhotoCardOpen(true);
    setProfileOpen(false);
    setSubmitPopupOpen(false);
    setFriendsPopupOpen(false);
  };

  const handleFriendsIconClick = () => {
    setPhotoCardOpen(false);
    setProfileOpen(false);
    setSubmitPopupOpen(false);
    setFriendsPopupOpen(true);
  };

  return (
      <div className="App" style={{
        position: "relative"
      }}>
        <CountryContext.Provider value={{
          countries
        }}
        >
          <PhotoContext.Provider value={{
            photos: userPhotos,
            handleAddPhoto,
            handleEditPhoto,
            handleDeletePhoto,
          }}>
            <Header handleAvatarClick={handleAvatarClick} handleAddPhotoClick={handleAddPhotoClick}
                    handleFriendsIconClick={handleFriendsIconClick} friends={friendsData}/>
            {error && (<Alert sx={{ position: "sticky"}} color="error" severity="warning">{error}</Alert>)}
            {message && (<Alert sx={{position: "sticky"}} color="info" severity="info">{message}</Alert>)}
            <main className="content">
              {photoCardOpen &&
                  <PhotoCard key={selectedItem?.src} photo={selectedItem} onClose={handleClosePopup}
                             initSubmitPopupAndOpen={initSubmitPopupAndOpen}/>}
              {profileOpen && <Profile onClose={handleClosePopup}/>}
              {submitPopupOpen && <Popup onSubmit={submitPopupData.onSubmit}
                                         onClose={handleClosePopup}
                                         text={submitPopupData.text}
                                         buttonText={submitPopupData.buttonText}
              />}
              {friendsPopupOpen && <FriendsPopup friends={friendsData} onClose={handleClosePopup}
                                                 handleRemoveFriend={handleDeleteFriend}/>}
              <Outlet context={{handlePhotoClick, initSubmitPopupAndOpen, handleClosePopup}}/>
            </main>
          </PhotoContext.Provider>
        </CountryContext.Provider>
      </div>
  );
}
