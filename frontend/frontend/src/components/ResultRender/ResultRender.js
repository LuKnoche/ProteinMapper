import React from 'react';
import MuiTable from '../Table/MuiTable';
import './resultRenderStyles.css'
/**
 * shows a message, when there is no data to show in a resultTable
 * renders a resultTable when there is data to show
 */
export function ResultRender(props){

    const resultData = props.resultData

    if(resultData.length===0){
        return(
            <p className="table-empty-message"></p>
        )
    }else{
        return(
            <MuiTable resultData={resultData}  getUploadedCustomModels={props.getUploadedCustomModels}/>
        )
    }
}
