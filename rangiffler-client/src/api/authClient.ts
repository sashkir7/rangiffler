import axios from "axios";
import {AUTH_URL, CLIENT, SECRET} from "./config";
import {Buffer} from "buffer";

export const authClient = axios.create({
  baseURL: AUTH_URL,
  headers: {
    "Content-type": "application/json",
    "Authorization": `Basic ${Buffer.from(`${CLIENT}:${SECRET}`).toString("base64")}`,
  }
});
