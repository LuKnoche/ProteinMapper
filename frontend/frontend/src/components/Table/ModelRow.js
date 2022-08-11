import React from 'react'
import ReactionRow from './ReactionRow';
import './tableStyles.css'
import createCSVProteinFile from '../useful-functions/createCSVProteinFile';
import createSBMLObj from '../useful-functions/createSBMLObj';

import Collapse from '@mui/material/Collapse';
import Tooltip from '@mui/material/Tooltip';
import TableRow from'@mui/material/TableRow';
import TableCell from '@mui/material/TableCell';
import KeyboardArrowDownIcon from '@mui/icons-material/KeyboardArrowDown';
import KeyboardArrowUpIcon from '@mui/icons-material/KeyboardArrowUp';
import Box from '@mui/material/Box';


import {saveAs} from "file-saver";
import SearchBar from 'material-ui-search-bar'

/*
renders all the data for the results corresponding to the displayed model
has an expandable list of modelReactions that where found, a search bar which can look for EC and name, multiple download buttons
and a button to port the data over to the pathwayCreator to visualize it
*/

export default function ModelRow(props){
    const modelResultData = props.modelResultData
    const bestResults = props.bestResults
    const commonTaxPath = props.modelResultData.commonTaxPath

    const listOfReactions = props.modelResultData.listOfReactions

    const sampleProteinsHits = props.modelResultData.sampleProteinsHits
    const sampleProteinsMisses = props.modelResultData.sampleProteinsMisses

    const modelName = modelResultData.modelName
    const sampleName = props.sampleName
    const sbmlFileName = modelResultData.modelName + "_" + sampleName
    const csvHitsFileName = sampleName + "_" + modelResultData.modelName + "_hits" + ".csv"
    const csvMissesFileName = sampleName + "_" + modelResultData.modelName + "_misses" + ".csv"

    const intersectionPercModelTotal = props.modelResultData.intersectionPercModelTotal
    const intersectionPercModelEC = props.modelResultData.intersectionPercModelEC
    const intersectionPercSample = props.modelResultData.intersectionPercSample
    const modelClassification = props.modelResultData.modelClassification
    const modelLink = props.modelResultData.modelLink
    const numIdentifiedReactions = props.modelResultData.numIdentifiedReactions
    const numHitsSample = props.modelResultData.numHitsSample
    const numReacModelWithECs = props.modelResultData.numReacModelWithECs
    const numReacModelTotal = props.modelResultData.numReacModelTotal

    const customModel = modelResultData.customModel

    const [rowOpen, setRowOpen] = React.useState(false)
    const [filteredModelHits, setFilteredModelHits] = React.useState(listOfReactions)
    const [searched, setSearched] = React.useState("");

    // sorting and filtering -> enables search for reaction names or ECnumber
    const requestSearch = (searchedValue) => {
        const filtered = [...listOfReactions].filter((reactionRow) => {
            if (reactionRow.name.toLowerCase().includes(searchedValue.toLowerCase())) {
                return true
            }
            if(reactionRow.sboTerm.toLowerCase().includes(searchedValue.toLowerCase())){
                return true
            }
            for (let i in reactionRow.ecNumbers) {
                if (reactionRow.ecNumbers[i].includes(searchedValue.toLowerCase())) {
                    return true
                }
            }
            return false
        })
        setFilteredModelHits(filtered)
    }

    const cancelSearch = () => {
        setSearched("")
        requestSearch(searched)
    }

    const sortedModelHits = React.useMemo(() => {
        let sortedResults = [...filteredModelHits]
        sortedResults.sort((a, b) => {
            if (a.name < b.name) {
                return -1;
            }
            if (a.name > b.name) {
                return 1;
            }
            return 0
        });
        return sortedResults
    }, filteredModelHits)

    function checkBestResults() {
        var className = ""
        for (var bestResult in bestResults) {
            if (bestResults[bestResult].modelName === modelName) {
                className = "table-model-info-mark-bestResult"
                break
            }
            else {
                className =  ""
            }
        }
        return className
    }

    async function handleDownloadSBML(modelId, listOfReactions) {
        var reactionIds = ''
        for (let i in listOfReactions) {
            reactionIds += listOfReactions[i].id + ";"
        }
        const formData = new FormData();
        formData.append("reactionIds", reactionIds)
        console.log(formData)

        if(customModel === true){
            let customModels = props.getUploadedCustomModels()
            for(let i in customModels){
               if(customModels[i].name === modelName+".xml"){
                   formData.append('customModelData',customModels[i])
               }
            }
        }

        let res = await fetch('requestResultSBML', {
            method: 'POST',
            headers: {
                'modelId': modelId,
                'newModelId': sbmlFileName,
                'customModel': customModel
            },
            body: formData
        }).then(res => res.json())
        console.log(res)

        let sbml = createSBMLObj(res.listOfCompartments, res.listOfSpecies, res.listOfReactions, res.id)
        let blob = new Blob([sbml], { type: 'text/xml;charset=utf-8;' })
        saveAs(blob, res.id + ".xml")
        
    }

    const handleDownloadCSV = (data, filename) => {
        var csv = createCSVProteinFile(data)
        var blob = new Blob([csv], { type: 'text/csv;charset=utf-8;' })
        saveAs(blob, filename)
    }

    return(
        <React.Fragment>
            <TableRow key="tableModelInfo" className={checkBestResults()}>
                <TableCell
                      onClick={()=>setRowOpen(!rowOpen)}>
                        {rowOpen ? <KeyboardArrowUpIcon size="small"/> : <KeyboardArrowDownIcon size="small"/>}
                </TableCell>
                <Tooltip title="Click to view model on http://bigg.ucsd.edu">
                <TableCell align='left' component="a" href={modelLink} target="_blank">{modelName}</TableCell>
                </Tooltip>
                <TableCell>{modelClassification}</TableCell>
                <TableCell>{numReacModelWithECs} of {numReacModelTotal} total</TableCell>
                <TableCell>{numIdentifiedReactions}</TableCell>
                <TableCell>{Number(Math.round(intersectionPercModelEC+'e2')+'e-2')}%</TableCell>
                <TableCell>{Number(Math.round(intersectionPercModelTotal+'e2')+'e-2')}%</TableCell>
                <TableCell>{numHitsSample}</TableCell>
                <Tooltip arrow placement="right" enterDelay={400} enterNextDelay={500} title={<p>{intersectionPercSample}</p>}>
                <TableCell>{Number(Math.round(intersectionPercSample+'e2')+'e-2')}%</TableCell>
                </Tooltip>
                <TableCell>{Number(Math.round(commonTaxPath.taxoScore+'e2')+'e-2')}/8</TableCell>
                <TableCell>{commonTaxPath.classification}</TableCell>
            </TableRow>
            <TableRow key="tableModelReactionsRow">
                <TableCell style={{ paddingBottom : 0, paddingTop : 0 }} colSpan={11}>
                    <Collapse in={rowOpen} unmountOnExit timeout='auto' sx={{maxHeight:350,overflow:'auto',maxWidth:1700}}>
                        <div className="model-results-interaction-bar">
                        <SearchBar
                            className='searchBarModelHits'
                            value={searched}
                            onChange={(searchedValue) => requestSearch(searchedValue)}
                            onCancelSearch={() => cancelSearch()}
                            placeholder='search EC or reaction name'
                        />
                        {/* <button className="standardButton">Display in PathwayCreator</button> */}
                        <button className="standardButton" onClick={() => handleDownloadSBML(modelName,listOfReactions)}>Download identified reactions as SBML</button>
                        <button className="standardButton" onClick={() => handleDownloadCSV(sampleProteinsHits,csvHitsFileName)}>Download list of hit proteins</button>
                        <button className="standardButton" onClick={() => handleDownloadCSV(sampleProteinsMisses,csvMissesFileName)}>Download list of missed proteins</button>
                        </div>
                        <Box margin={{margin : 1}}>
                            <ReactionRow modelHits={sortedModelHits}/>
                        </Box>
                    </Collapse>
                </TableCell>
            </TableRow>
        </React.Fragment>
    )
}