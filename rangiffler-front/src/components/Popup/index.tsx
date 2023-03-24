import CloseIcon from "@mui/icons-material/Close";
import {LoadingButton} from "@mui/lab";
import {Box, Card, CardActions, CardContent, Grid, IconButton, Typography} from "@mui/material";
import React, {FC, FormEvent} from "react";


interface PopupInterface {
  text: string,
  onSubmit: () => void;
  onClose: () => void;
  buttonText?: string;
}

export const Popup: FC<PopupInterface> = ({text, onSubmit, onClose, buttonText}) => {

  const handleSubmit = (evt: FormEvent<HTMLFormElement>) => {
    evt.preventDefault();
    onSubmit();
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
          top: "50%",
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
              <IconButton size='small' onClick={onClose}>
                <CloseIcon/>
              </IconButton>
            </CardActions>
            <CardContent sx={{
              width: "500px",
              margin: "0 auto",
            }}>
              <form onSubmit={(evt) => handleSubmit(evt)}>
                <Grid>
                  <Typography variant="body2" sx={{marginBottom: "30px", textAlign: "center"}}>
                    {text}
                  </Typography>
                </Grid>
                <Grid item sx={{textAlign: "center"}}>
                  <LoadingButton variant="contained"
                                 type="submit">{buttonText ?? "Submit"}</LoadingButton>
                </Grid>
              </form>
            </CardContent>
          </Card>
        </Box>
      </Box>
  );
}
