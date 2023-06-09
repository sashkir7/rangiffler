import {
  Avatar,
  ButtonGroup,
  IconButton,
  Paper,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  Tooltip,
} from "@mui/material";
import {AxiosResponse} from "axios";
import React, { FC, useContext, useEffect, useState } from "react";
import {useOutletContext} from "react-router-dom";
import {apiClient} from "../../api/apiClient";
import { AlertMessageContext } from "../../context/AlertMessageContext/index";
import {User} from "../../types/types";
import PersonAddAlt1Icon from '@mui/icons-material/PersonAddAlt1';
import HowToRegIcon from '@mui/icons-material/HowToReg';
import PersonOffIcon from '@mui/icons-material/PersonOff';
import PersonRemoveAlt1Icon from '@mui/icons-material/PersonRemoveAlt1';
import {LayoutContext} from "../Layout/index";

export const PeopleTab: FC = () => {
  const [allUsers, setAllUsers] = useState<User[]>([]);
  const {initSubmitPopupAndOpen, handleClosePopup} = useOutletContext<LayoutContext>();
  const {addMessage, addError} = useContext(AlertMessageContext);

  useEffect(() => {
    apiClient().get("/users")
    .then((res) => {
      var sortedUsers: User[] = [];
      let statuses = ["INVITATION_SENT", "INVITATION_RECEIVED", "FRIEND", "NOT_FRIEND"];
      statuses.forEach(function(status) {
        let usersByStatus = res.data[status];
        usersByStatus.forEach(function(user) {
            user.friendStatus = status;
        });
        sortedUsers = sortedUsers.concat(usersByStatus);
      });
      setAllUsers(sortedUsers);
    });
  }, []);

  const updateFriendsStatus = (user: User, friendStatus: string) => {
    const newArr = [...allUsers];
    const u = newArr.find(u => u.id === user.id);
    if (u) {
      u.friendStatus = friendStatus;
      newArr[newArr.indexOf(u)] = u;
      setAllUsers(newArr);
    }
  };

  const handleSendInvitation = (user: User) => {
    apiClient().post("friends/invite", {
      ...user
    }).then((res) => {
      updateFriendsStatus(user, "INVITATION_SENT");
//       addMessage(`Invitation to user ${user.username} is sent`);
    }).catch((err) => {
      console.error(err);
      addError(`Invitation is not sent. Reason: ${err.message}`);
    });
  };

  const handleAcceptInvitation = (user: User) => {
    apiClient().post("friends/submit", {
      ...user
    }).then((res) => {
      updateFriendsStatus(user, "FRIEND");
//       addMessage(`User ${user.username} added to your friends`);
    }).catch((err) => {
      console.error(err);
      addError(`Invitation is not accepted. Reason: ${err.message}`);
    });
  };

  const handleDeclineInvitation = (user: User) => {
    initSubmitPopupAndOpen("Decline friend?", "Decline", () => {
      apiClient().post("friends/decline", {
        ...user
      }).then(res => {
        updateFriendsStatus(user, "NOT_FRIEND");
        handleClosePopup();
//         addMessage(`You declined invitation from user ${user.username}`);
      }).catch((err) => {
        console.error(err);
        addError(`Invitation is not declined. Reason: ${err.message}`);
      });
    });
  };

  const handleDeleteFriend = (user: User) => {
    initSubmitPopupAndOpen("Delete friend?", "Delete", () => {
      apiClient().post("friends/remove", {
        ...user
      }).then(res => {
        updateFriendsStatus(user, "NOT_FRIEND");
        handleClosePopup();
//         addMessage(`You're not friends with user ${user.username} anymore`);
      }).catch((err) => {
        console.error(err);
        addError(`Friend is not deleted. Reason: ${err.message}`);
      });
    });
  };

  const getUserControls = (user: User) => {
    const friendStatus = user.friendStatus;
    switch (friendStatus) {
      case "NOT_FRIEND":
        return (
            <Tooltip title="Add friend">
              <IconButton size={"small"}
                          onClick={() => handleSendInvitation(user)}>
                <PersonAddAlt1Icon/>
              </IconButton>
            </Tooltip>);
      case "INVITATION_SENT":
        return <>Invitation sent</>;
      case "INVITATION_RECEIVED":
        return (
            <ButtonGroup>
              <Tooltip title="Accept invitation">
                <IconButton size={"small"}
                            onClick={() => handleAcceptInvitation(user)}>
                  <HowToRegIcon/>
                </IconButton>
              </Tooltip>
              <Tooltip title="Decline invitation">
                <IconButton size={"small"}
                            onClick={() => handleDeclineInvitation(user)}>
                  <PersonOffIcon/>
                </IconButton>
              </Tooltip>
            </ButtonGroup>
        );
      case "FRIEND":
        return (
            <Tooltip title="Remove friend">
              <IconButton size={"small"} onClick={() => handleDeleteFriend(user)}>
                <PersonRemoveAlt1Icon/>
              </IconButton>
            </Tooltip>);
    }
  }

  return (
      <TableContainer component={Paper} sx={{maxHeight: "80vh"}}>
        <Table stickyHeader aria-label="all people table">
          <TableHead>
            <TableRow>
              <TableCell></TableCell>
              <TableCell>Username</TableCell>
              <TableCell>Name</TableCell>
              <TableCell></TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {allUsers.map(user => (
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
                  <TableCell>{user.firstname} {user.lastname}</TableCell>
                  <TableCell>{getUserControls(user)}</TableCell>
                </TableRow>
            ))}
          </TableBody>
        </Table>
      </TableContainer>
  );
};
