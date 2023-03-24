export type User = {
  id: number,
  username: string,
  firstName: string,
  lastName: string,
  avatar: string,
  friendStatus: string,
}

export type ApiCountry = {
  id: number,
  code: string,
  name: string,
};

export type MapCountry = {
  country: string,
  value: number,
};

export type Photo = {
  id: string,
  username: string,
  src: string,
  countryCode: string,
  description?: string,
}

export type FriendStatus = "FRIEND" | "NOT_FRIEND" | "INVITATION_SENT" | "INVITATION_RECEIVED"
