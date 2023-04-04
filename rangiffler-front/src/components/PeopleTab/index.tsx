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
import React, {FC, useEffect, useState} from "react";
import {useOutletContext} from "react-router-dom";
import {apiClient} from "../../api/apiClient";
import {User} from "../../types/types";
import PersonAddAlt1Icon from '@mui/icons-material/PersonAddAlt1';
import HowToRegIcon from '@mui/icons-material/HowToReg';
import PersonOffIcon from '@mui/icons-material/PersonOff';
import PersonRemoveAlt1Icon from '@mui/icons-material/PersonRemoveAlt1';
import {LayoutContext} from "../Layout/index";

export const PeopleTab: FC = () => {
  const [allUsers, setAllUsers] = useState<User[]>([]);
  const {initSubmitPopupAndOpen} = useOutletContext<LayoutContext>();

  useEffect(() => {
    apiClient().get("/users")
    .then((res) => {
        let users: object = res.data;
        var sortedUsers: User[] = []
            .concat(users["INVITATION_SENT"])
            .concat(users["INVITATION_RECEIVED"])
            .concat(users["FRIEND"])
            .concat(users["NOT_FRIEND"]);
      setAllUsers(sortedUsers);
    });
  }, []);

  const handleApiResponse = (res: AxiosResponse, user: User) => {
    const newArr = [...allUsers];
    const u = newArr.find(u => u.id === user.id);
    if (u) {
      newArr[newArr.indexOf(u)] = res.data;
      setAllUsers(newArr);
    }
  };

  const handleSendInvitation = (user: User) => {
    apiClient().post("users/invite/", {
      ...user
    }).then((res) => {
      handleApiResponse(res, user);
    });
  };

  const handleAcceptInvitation = (user: User) => {
    apiClient().post("friends/submit", {
      ...user
    }).then((res) => {
      handleApiResponse(res, user);
    });
  };

  const handleDeclineInvitation = (user: User) => {
    initSubmitPopupAndOpen("Decline friend?", "Decline", () => {
      apiClient().post("friends/decline", {
        ...user
      }).then(res => {
        handleApiResponse(res, user);
      });
    });
  };

  const handleDeleteFriend = (user: User) => {
    initSubmitPopupAndOpen("Delete friend?", "Delete", () => {
      apiClient().post("friends/remove", {
        ...user
      }).then(res => {
        handleApiResponse(res, user);
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
                  <TableCell>{user.firstName} {user.lastName}</TableCell>
                  <TableCell>{getUserControls(user)}</TableCell>
                </TableRow>
            ))}
          </TableBody>
        </Table>
      </TableContainer>
  );
};
