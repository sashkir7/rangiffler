import CloseIcon from "@mui/icons-material/Close";
import {LoadingButton} from "@mui/lab";
import {
  Avatar,
  Box,
  Card,
  CardActions,
  CardContent,
  Grid,
  IconButton,
  TextField,
  Typography
} from "@mui/material";
import React, {FC, FormEvent, useContext, useState} from "react";
import {apiClient} from "../../api/apiClient";
import {MAX_TEXT_FIELD_LENGTH, MIN_REQUIRED_TEXT_FIELD_LENGTH} from "../../constants/const";
import {UserContext} from "../../context/UserContext/index";
import {User} from "../../types/types";
import {ImageUpload} from "../ImageUpload/index";

interface ProfileInterface {
  onClose: () => void;
}

export const Profile: FC<ProfileInterface> = ({onClose}) => {

  const {user, handleChangeUser} = useContext(UserContext);

  const [profileData, setProfileData] = useState<Partial<User>>(user);
  const initialFieldErrorsState = {
    firstname: null,
    lastname: null,
  };
  const [fieldErrors, setFieldErrors] = useState<{ [key: string]: string | null }>(initialFieldErrorsState);

  const hasFormAnyError: boolean = Object.values(fieldErrors).filter(v => v !== null).length > 0;
  const resetFieldErrors = (fieldName: string): void => {
    setFieldErrors({...fieldErrors, [fieldName]: null});
  };
  const checkTextLengthValid = (field: string, isFieldRequired: boolean, text?: string): void => {
    if (isFieldRequired) {
      if (!text) {
        setFieldErrors({...fieldErrors, [field]: "This field is required!"})
      } else if (text.length < MIN_REQUIRED_TEXT_FIELD_LENGTH) {
        setFieldErrors({
          ...fieldErrors,
          [field]: "Length of this field must be more than 2 characters!"
        })
      } else {
        resetFieldErrors(field);
      }
    } else if (text && (text.length > MAX_TEXT_FIELD_LENGTH)) {
      setFieldErrors({
        ...fieldErrors,
        [field]: "Length of this field must be no longer than 50 characters"
      })
    } else {
      resetFieldErrors(field);
    }
  };
  const handleProfileChange = (evt: FormEvent<HTMLFormElement>) => {
    evt.preventDefault();

    apiClient().patch("/currentUser", profileData)
    .then(res => handleChangeUser(res.data));
    onClose();
    setFieldErrors(initialFieldErrorsState);
  }

  const handleFieldChange = (event: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
    const {name, value} = event.target;
    setProfileData({...profileData, [name]: value});
  }

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
              <IconButton size='small' onClick={() => {
                onClose();
                setFieldErrors(initialFieldErrorsState);
              }}>
                <CloseIcon/>
              </IconButton>
            </CardActions>
            <CardContent>
              {
                <>
                  <form onSubmit={handleProfileChange}>
                    <Grid container width="100%" direction="column" gap={1} sx={{
                      justifyContent: "center",

                    }}>
                      <Grid item sx={{
                        display: "flex",
                        justifyContent: "center",
                        marginBottom: "8px",
                      }}>
                        <Box sx={{
                          position: "relative",
                        }}>
                          <Avatar sx={{width: 450, height: 450}}
                                  src={profileData?.avatar}
                                  alt={profileData?.username}
                                  variant="rounded"
                          />
                          <Box sx={{
                            position: "absolute",
                            right: "5%",
                            top: "80%",
                          }}>
                            <ImageUpload
                                handlePhotoChange={(avatar) => setProfileData({
                                  ...profileData,
                                  avatar,
                                })}
                            />
                          </Box>
                        </Box>
                      </Grid>
                      <Grid item sx={{
                        textAlign: "center",
                        maxHeight: "70px",
                      }}>
                        <Typography variant="body2">
                          Username: {profileData?.username}
                        </Typography>
                      </Grid>
                      <Grid item sx={{
                        textAlign: "center",
                        maxHeight: "70px",
                      }}>
                        <TextField
                            sx={{
                              width: "80%",
                              margin: "12px",
                            }}
                            label="First name"
                            size="small"
                            name="firstname"
                            value={profileData?.firstname}
                            helperText={fieldErrors["firstname"]}
                            error={fieldErrors["firstname"] !== null}
                            onChange={event => {
                              checkTextLengthValid(event.target.name, true, event.target.value);
                              handleFieldChange(event)
                            }}
                        />
                      </Grid>
                      <Grid item sx={{
                        textAlign: "center",
                        maxHeight: "70px",
                      }}>
                        <TextField
                            sx={{
                              width: "80%",
                              margin: "12px",
                            }}
                            label="Last name"
                            size="small"
                            name="lastname"
                            value={profileData?.lastname}
                            helperText={fieldErrors["lastname"]}
                            error={fieldErrors["lastname"] !== null}
                            onChange={event => {
                              checkTextLengthValid(event.target.name, true, event.target.value);
                              handleFieldChange(event)
                            }}
                        />
                      </Grid>
                      <Grid item sx={{textAlign: "center"}}>
                        <LoadingButton variant="contained" type="submit"
                                       disabled={hasFormAnyError}>Save</LoadingButton>
                      </Grid>
                    </Grid>
                  </form>
                </>
              }
            </CardContent>
          </Card>
        </Box>
      </Box>
  );
}
