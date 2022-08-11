import React from 'react'
import List from '@mui/material/List';
import ListItemText from '@mui/material/ListItemText';
import { ListItem } from '@mui/material';
import DeleteIcon from '@mui/icons-material/Delete';
import IconButton from '@mui/material/IconButton';
import "./uploadSectionStyles.css";

/*
* renders an uploaded experimental file with delete option
*/
export default function UploadedFileButtons(props){
    const files = props.files

    const fileButtons = files.map( file => 
        <ListItem className="file-button">
            <ListItemText primaryTypographyProps={{fontSize:'13px'}} key={Math.random()} 
            primary={file.name}/>
            <IconButton onClick={()=>props.handleDelete(file.path,file.name.split('\.')[0])}>     {/*input filePath and name without .csv or .xml as that's how it's saved in the taxoMap*/}
            <DeleteIcon/>
            </IconButton>
        </ListItem>
    )
    return (
        <List>
            {fileButtons}
        </List>
    )
}