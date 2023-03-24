import {TabContext, TabList, TabPanel} from "@mui/lab";
import {Box, Tab} from "@mui/material";
import React, {FC, useState} from "react";
import {FriendsTab} from "../FriendsTab";
import {MainTab} from "../MainTab/index";
import {PeopleTab} from "../PeopleTab/index";

export const Content: FC = () => {
  const [tab, setTab] = useState("main");
  const handleChangeTab = (event: React.SyntheticEvent, newValue: string) => {
    setTab(newValue);
  };

  return (
      <>
        <TabContext value={tab}>
          <Box sx={{
            padding: "10px",
          }}>
            <TabList
                onChange={handleChangeTab}
                aria-label="Tabs"
            >
              <Tab label="Your travels"
                   value="main"
              />
              <Tab
                  label="Friends travels"
                  value="friends"
              />
              <Tab
                  label="People Around"
                  value="all"
              />
            </TabList>
          </Box>
          <TabPanel value="main">
            <MainTab/>
          </TabPanel>
          <TabPanel value="friends">
            <FriendsTab/>
          </TabPanel>
          <TabPanel value="all">
            <PeopleTab/>
          </TabPanel>
        </TabContext>
      </>

  );
}
