import deer from "@img/deer-logo.svg";
import {Box, ButtonGroup, Card, CardContent, CardMedia, Grid, Typography} from "@mui/material";
import React, {useContext} from "react";
import {Link, Navigate} from "react-router-dom";
import "./styles.scss"
import {generateCodeChallenge, generateCodeVerifier} from "../../api/apiUtils";
import {AUTH_URL} from "../../api/config";
import {UserContext} from "../../context/UserContext/index";

export const Landing = () => {

  const {user} = useContext(UserContext);

  const verifier = generateCodeVerifier();
  sessionStorage.setItem('codeVerifier', verifier);
  const codeChallenge = generateCodeChallenge();
  sessionStorage.setItem('codeChallenge', codeChallenge);

  return (
      <>
        {user?.username ? <Navigate to={"/"}/> : (
            <Box sx={{
              width: "100%",
              display: "block",
              backgroundColor: "rgba(0, 0, 0, 0.4)",
              height: "100vh",
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
                  <CardContent sx={{
                    width: "500px",
                    margin: "0 auto",
                    padding: "20px"

                  }}>
                    <Grid container direction="column" sx={{
                      justifyContent: "center",
                    }}>
                      <Grid item sx={{textAlign: "center"}}>
                        <Typography variant="h3" component="h1" sx={{flexGrow: 1, margin: "12px"}}>
                          Be like Rangiffler
                        </Typography>
                      </Grid>
                      <Grid item sx={{
                        display: "flex",
                        textAlign: "center",
                        position: "relative",
                        justifyContent: "center",

                      }}>
                        <CardMedia
                            sx={{
                              width: "300px",
                              height: "300px",
                              objectFit: "fit-content"
                            }}
                            component="img"
                            image={deer}
                            alt="New image"
                        />
                      </Grid>
                      <Grid item sx={{textAlign: "center"}}>
                        <ButtonGroup sx={{margin: "20px"}}>
                          <Link className={"main__link"} to={'/redirect'}>Login</Link>
                          <a className={"main__link"} href={`${AUTH_URL}/register`}>Register</a>
                        </ButtonGroup>
                      </Grid>
                    </Grid>
                  </CardContent>
                </Card>
              </Box>
            </Box>)}
      </>);
}
