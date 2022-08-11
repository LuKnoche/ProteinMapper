import React, { Component } from 'react'
import './uploadSectionStyles.css'
import '../../style.css'
import { List,ListItem,ListItemText } from '@mui/material';
import { TextField } from '@mui/material';

export default function TaxonomyConfigurationPopup(props) {

    const filesTaxoMap = new Map()
    const files = props.files
    const filesTaxo = props.filesTaxo

    if (typeof filesTaxo !== 'undefined') {
        filesTaxo.map((fileTaxo) => {
            filesTaxoMap.set(fileTaxo.fileName, fileTaxo.taxoArray)
        })
    }

    const handleClick = () => {
        props.toggle();
    }

    const getTaxEntry = (fileName, index) => {
        let taxoArray = filesTaxoMap.get(fileName)
        return taxoArray[index]
    }

    const handleTextFieldChange = (e) => {
        let fileName = e.target.id.split(',')[0]
        let entryIndex = e.target.id.split(',')[1].split('taxElement')[1]
    
        console.log(fileName)
        let taxEntries = filesTaxoMap.get(fileName)
        let newTaxEntry = e.target.value
        taxEntries[entryIndex] = newTaxEntry
        console.log(filesTaxoMap)
       filesTaxoMap.set(fileName,taxEntries)
    }
        return(
            <div className="modal">
                <div className="modal-content">
                    <h3>Input Taxonomy for each file</h3>
                    <div className="modal-content-taxoList">
                    <List>
                        {files.map(file => {
                            const fileName = file.name.split('\.')[0]
                            return (
                                <div className="modal-content-taxoList-item">
                                <ListItem>
                                    <div className="modal-content-taxoList-item-inner">
                                    <ListItemText primary={file.name}/>
                                    <div className="modal-content-taxoList-item-inner-taxoRanks">
                                    <TextField sx={{width:200}} id={fileName+",taxElement0"} placeholder={getTaxEntry(fileName,0)} label={"SuperKingdom"} onChange={handleTextFieldChange}/>
                                    <TextField sx={{width:200}} id={fileName+",taxElement1"} placeholder={getTaxEntry(fileName,1)} label={"Kingdom"} onChange={handleTextFieldChange}/>
                                    <TextField sx={{width:200}} id={fileName+",taxElement2"} placeholder={getTaxEntry(fileName,2)} label={"Phylum"} onChange={handleTextFieldChange}/>
                                    <TextField sx={{width:200}} id={fileName+",taxElement3"} placeholder={getTaxEntry(fileName,3)} label={"Class"} onChange={handleTextFieldChange}/>
                                    <TextField sx={{width:200}} id={fileName+",taxElement4"} placeholder={getTaxEntry(fileName,4)} label={"Order"} onChange={handleTextFieldChange}/>
                                    <TextField sx={{width:200}} id={fileName+",taxElement5"} placeholder={getTaxEntry(fileName,5)} label={"Family"} onChange={handleTextFieldChange}/>
                                    <TextField sx={{width:200}} id={fileName+",taxElement6"} placeholder={getTaxEntry(fileName,6)} label={"Genus"} onChange={handleTextFieldChange}/>
                                    <TextField sx={{width:200}} id={fileName+",taxElement7"} placeholder={getTaxEntry(fileName,7)} label={"Species"} onChange={handleTextFieldChange}/>
                                    </div>
                                    </div>
                                </ListItem>
                                </div>
                            )
                        })}
                    </List>
                    </div>
                    <button type="button" className="close standardButton" onClick={handleClick}>close</button>
                </div>
            </div>
        )
}