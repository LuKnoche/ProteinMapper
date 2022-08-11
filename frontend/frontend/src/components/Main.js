import {React, useState} from "react";
import "../style.css";
import UploadSection from './UploadSection/UploadSection';
import {ResultRender} from './ResultRender/ResultRender';

export default function Main() {
    const [resultData, setResultData]=useState([]);
    const [dataBase, setDataBase]=useState("BiGG");
    const [uploadedCustomModels, setUploadedCustomModels] =useState([])

    function handleDataBaseChange(newValue){
        setDataBase(newValue)
    }

    async function evaluateFiles(){
        console.time("fileEval")
        var res = await fetch("mapping",{
            headers : {'database' : dataBase}
        })
        setResultData([])                   //set to empty, so the table gets reset. otherwise opened drawers may remain open, even though the data has changed
        const json = await res.json();
        setResultData(json)
        console.log(json)
        //console.log(res)
        console.timeEnd("fileEval")
     }

    function clearResultData(){
        setResultData([]);
    }

    function getUploadedCustomModels(){
        return uploadedCustomModels
    }

    return (
        <div className="main">
            <UploadSection
            setDatabase={handleDataBaseChange} //functions from main into child, so main handles the functions and incoming data
            clearTable={clearResultData}
            evaluateFiles={evaluateFiles}
            setUploadedCustomModels={setUploadedCustomModels}
            dataBase={dataBase}
            />
            <ResultRender resultData={resultData} getUploadedCustomModels={getUploadedCustomModels}/>
        </div>
    )
}