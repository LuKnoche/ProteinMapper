import React from 'react';
import List from '@mui/material/List';
import ListItemText from '@mui/material/ListItemText';
import { ListItem } from '@mui/material';
import './tableStyles.css'
/*
* renders the name, ECs and SBOTerms of a given reaction
*/
export default function ReactionRow(props){
    const modelHits = props.modelHits

    function displayECs(ecs){
        if(ecs.length>1){
        var x="";
        for(let i=0;i<ecs.length-1;i++){
            x+=ecs[i] +", "
        }
        x+=ecs[ecs.length-1]
        return x
    }
        return ecs
    }

    const dataMap = modelHits.map((hit)=>
                <ListItem sx={{pl: 4,paddingBottom:0,paddingTop:0}} key={Math.random().toString(36).substr(2,9)} >
                    <ListItemText primaryTypographyProps={{fontSize:'13px'}} primary={hit.name+"\t"+hit.sboTerm} secondaryTypographyProps={{fontSize: '13px'}} secondary={displayECs(hit.ecNumbers)}/>
                </ListItem>
               )  
    return(
        <List component="div" disablePadding>
                {dataMap}
        </List>
    ); 
}