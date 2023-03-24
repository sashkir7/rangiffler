import deer from "@img/deer-logo.svg";
import {Box, Typography} from "@mui/material";
import {Link} from "react-router-dom";

export const NotFoundPage = () => {
  return (
      <Box sx={{
        textAlign: "center",
        marginTop: "20px",
      }}>
        <Box
            component="img"
            sx={{
              height: 300,
              width: 300,
            }}
            alt="Rangiffler Logo"
            src={deer}
        />
        <Typography variant="h4" component="h1">
          404.Page not found.
        </Typography>
        <Typography>
          Go to <Link to={"/"}>Main Page</Link>
        </Typography>
      </Box>
  );
}
