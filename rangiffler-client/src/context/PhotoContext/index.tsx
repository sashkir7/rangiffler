import {createContext} from "react";
import {Photo} from "../../types/types";

interface PhotoContextInterface {
  photos: Array<Photo>;
  handleAddPhoto: (photo: Photo) => void;
  handleEditPhoto: (photo: Photo) => void;
  handleDeletePhoto: (photoId: string) => void;
}

const defaultState = {
  photos: [],
  handleAddPhoto: () => {
  },
  handleEditPhoto: () => {
  },
  handleDeletePhoto: () => {
  },
};

export const PhotoContext = createContext<PhotoContextInterface>(defaultState);
