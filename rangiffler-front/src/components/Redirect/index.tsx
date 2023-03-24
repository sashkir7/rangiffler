import {CircularProgress} from "@mui/material";
import React, {useContext, useEffect} from "react";
import {useNavigate, useSearchParams} from "react-router-dom";
import {apiClient} from "../../api/apiClient";
import {authClient} from "../../api/authClient";
import {AUTH_URL, CLIENT, FRONT_URL} from "../../api/config";
import {UserContext} from "../../context/UserContext/index";

export const Redirect = () => {
  const {handleChangeUser} = useContext(UserContext);
  const [searchParams] = useSearchParams();
  const navigate = useNavigate();

  useEffect(() => {
    if (searchParams?.get("code")) {
      const code = searchParams?.get("code");
      const client = CLIENT;

      const verifier = sessionStorage.getItem("codeVerifier");
      const initialUrl = `/oauth2/token?client_id=${client}&redirect_uri=${FRONT_URL}/authorized&grant_type=authorization_code`;
      const url = `${initialUrl}&code=${code}&code_verifier=${verifier}`;

      authClient.post(url).then((res) => {
        if (res?.data?.id_token) {
          sessionStorage.setItem("id_token", res.data.id_token);
          apiClient(res.data.id_token)
          .get("/currentUser")
          .then((res) => {
            if (res?.data) {
              handleChangeUser(res.data);
              navigate("/");
            } else {
              navigate("/landing");
            }
          })
        }
      })
      .catch((err) => {
        navigate("/landing");
        console.error(err);
      })
    }
  }, []);

  useEffect(() => {
    if (!searchParams?.get("code")) {
      const codeChallenge = sessionStorage.getItem("codeChallenge");
      const link = `${AUTH_URL}/oauth2/authorize?response_type=code&client_id=client&scope=openid&redirect_uri=${FRONT_URL}/authorized&code_challenge=${codeChallenge}&code_challenge_method=S256`;
      window.location.href = link;
    }
  }, []);
  return <CircularProgress color="primary" sx={{position: "absolute", top: "50%", right: "50%"}}/>;
}
