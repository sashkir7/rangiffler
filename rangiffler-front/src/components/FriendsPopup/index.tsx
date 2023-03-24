import CloseIcon from "@mui/icons-material/Close";
import PersonRemoveAlt1Icon from "@mui/icons-material/PersonRemoveAlt1";
import {
  Avatar,
  Box,
  Card,
  CardActions,
  CardContent,
  IconButton,
  Paper,
  Stack,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableRow,
  Tooltip,
} from "@mui/material";
import React, {FC} from "react";
import {User} from "../../types/types";

interface FriendsPopupInterface {
  friends: User[];
  onClose: () => void;
  handleRemoveFriend: (user: User) => void;
}

export const FriendsPopup: FC<FriendsPopupInterface> = ({friends, onClose, handleRemoveFriend}) => {
  return (
      <Box sx={{
        width: "100%",
        display: "block",
        backgroundColor: "rgba(0, 0, 0, 0.4)",
        height: "calc(100vh - 45px)",
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
              <TableContainer component={Paper} sx={{maxHeight: "50vh", overflow: "scroll"}}>
                {friends?.length > 0 ? (
                        <Table stickyHeader aria-label="friends table">
                          <TableBody>
                            {friends.map(user => (
                                <TableRow
                                    key={user.username}
                                    sx={{'&:last-child td, &:last-child th': {border: 0}}}
                                >
                                  <TableCell>
                                    <Avatar sx={{width: 48, height: 48}}
                                            src={user.avatar}
                                            alt={user.username}
                                    />
                                  </TableCell>
                                  <TableCell>{user.username}</TableCell>
                                  <TableCell>
                                    <Tooltip title="Remove friend">
                                      <IconButton size="small" onClick={() => handleRemoveFriend(user)}>
                                        <PersonRemoveAlt1Icon/>
                                      </IconButton>
                                    </Tooltip>
                                  </TableCell>
                                </TableRow>
                            ))}
                          </TableBody>
                        </Table>) :
                    (
                        <Stack sx={{margin: "16px auto", width: "200px", textAlign: "center"}}>
                          No friends yet
                        </Stack>
                    )}
              </TableContainer>
            </CardContent>
          </Card>
        </Box>
      </Box>
  );
}
