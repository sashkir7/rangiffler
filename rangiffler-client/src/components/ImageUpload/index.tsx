import {Fab} from "@mui/material";
import React, {ChangeEvent, FC} from "react";
import AddAPhotoIcon from '@mui/icons-material/AddAPhoto';
import "./styles.scss";

interface ImageUploadInterface {
  handlePhotoChange: (photo?: string) => void;
}

export const ImageUpload: FC<ImageUploadInterface> = ({handlePhotoChange}) => {
  const handleUploadClick = (event: ChangeEvent<HTMLInputElement>) => {
    const file = event.target.files?.[0];
    if (!file) {
      return;
    }
    const reader = new FileReader();
    reader.readAsDataURL(file);

    reader.onloadend = function () {
      handlePhotoChange(reader.result?.toString());
    };
  };

  return (
      <>
        <input
            className="visually-hidden"
            accept="image/*"
            id="file"
            multiple
            type="file"
            onChange={handleUploadClick}
        />
        <label htmlFor="file">
          <Fab component="span">
            <AddAPhotoIcon/>
          </Fab>
        </label>
      </>
  );
}
