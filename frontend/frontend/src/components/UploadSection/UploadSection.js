import { React,useState } from 'react'
import ReactDropzone from 'react-dropzone'
import Paper from '@mui/material/Paper';
import Radio from '@mui/material/Radio';
import RadioGroup from '@mui/material/RadioGroup';
import FormGroup from '@mui/material/FormGroup';
import FormControlLabel from '@mui/material/FormControlLabel';
import FormLabel from '@mui/material/FormLabel';

import UploadedFileButtons from './UploadedFileButtons'
import TaxonomyConfigurationPopup from './TaxonomyConfigurationPopup';
import createSampleTaxoCSVList from '../useful-functions/createSampleTaxoCSVList';
import './uploadSectionStyles.css'
import '../../style.css'
/*
* handles all the upload functionality and database selection
* uploaded files get displayed in a list, they can also be deleted again before evaluation
*/
function UploadSection(props) {

    const [uploadedFiles, setUploadedFiles] = useState([])
    const [filesTaxonomy, setFilesTaxonomy] = useState()
    const [popupState, setPopupState] = useState(false)

    const handleFileDelete = (filePath,fileName) => {
        var newFilesList = uploadedFiles.filter((file) => file.path !== filePath)
        setUploadedFiles(newFilesList)

       var newFilesTaxoList = filesTaxonomy.filter((fileTaxo) => fileTaxo.fileName !== fileName)
       setFilesTaxonomy(newFilesTaxoList)
    }


    const handleFileDrop = (files) => {
        //let newFileList = [...uploadedFiles,...files]
        setUploadedFiles(files)

        // set taxonomy for every file, so if it doesn't get defined the server won't throw an error
        var filesTaxonomyArray = []
        var customModelArray = []
        for(var i in files){
            let fileTaxo = {fileName: files[i].name.split('\.')[0], taxoArray:["UNKNOWN","UNKNOWN","UNKNOWN","UNKNOWN","UNKNOWN","UNKNOWN","UNKNOWN","UNKNOWN"]}
            filesTaxonomyArray.push(fileTaxo)  // cant assign taxoArray from e.g. let undefinedTaxo = [...] because then when an entry gets configured it changes that entry on all files

            let nameElements = files[i].name.split('\.')
            let fileType = nameElements[nameElements.length - 1]
            if(fileType === "xml"){
                customModelArray.push(files[i])
            }
        }    
        // basically don't assign the taxoArray via a variable
        //let newTaxList = [...filesTaxonomy,...filesTaxonomyArray]
        setFilesTaxonomy(filesTaxonomyArray)
        props.setUploadedCustomModels(customModelArray)
    }
    const acceptedFiles = (db) => {
        if(db === "BiGG"){
            return ".csv"
        }
        if(db === "userSBML" || db === "both"){
            return [".csv",".xml"]
        }
    }

    async function uploadFiles() {
        if (uploadedFiles.length !== 0) {
            //implement filesTaxonomyMap to Array -> []
            console.time("fileUpload")
            let taxoCSVFormat = createSampleTaxoCSVList(filesTaxonomy)
            const formData = new FormData();
            for (var item in uploadedFiles) {
                formData.append(uploadedFiles[item].name, uploadedFiles[item])
            }
            formData.append("taxonomy.csv",taxoCSVFormat)

            const options = {
                method: 'POST',
                body: formData
            };
            await fetch('upload', options)
            props.evaluateFiles()
            console.timeEnd("fileUpload")
        }
    }

    const togglePopup = () => {
        setPopupState(!popupState)
    }
    
    return (
        <div className="upload-elements">
            <ReactDropzone onDrop={files => handleFileDrop(files)} onDropRejected={()=>alert('Your files were rejected. Please check if they are in the right format. No mora than 5 files can be accepted at once.')}>
                {({ getRootProps, getInputProps }) => (
                    <div {...getRootProps()} className="dropzone">
                        <input {...getInputProps()} accept={acceptedFiles(props.dataBase)} />
                        <p>Drag an experimental data file here or click to upload files</p>
                    </div>
                )}
            </ReactDropzone>
            <Paper sx={{width:330, height: 160, overflow:'auto'}}>
                <UploadedFileButtons files={uploadedFiles} handleDelete={handleFileDelete}/>
            </Paper>
            <div className="main-radios">
                <FormGroup className="main-row-radio">
                    <FormLabel id='db-select-label'>Select DB</FormLabel>
                    <RadioGroup name="db-radio-selector" defaultValue="BiGG" className='main-row-radio-buttons'>
                        <FormControlLabel value="BiGG" control={<Radio />} label="BiGGModels" onClick={() => props.setDatabase("BiGG")} />
                        <FormControlLabel value="user" control={<Radio />} label="User-SBML only" onClick={() => props.setDatabase("userSBML")} />
                        <FormControlLabel value="both" control={<Radio />} label="User-SBML and BiGGModels" onClick={() => props.setDatabase("both")} />
                    </RadioGroup>
                </FormGroup>
            </div>
            <div class="column-buttons">
                <button className='standardButton' onClick={() => togglePopup()}>Configure Taxonomy</button>
                <button className='standardButton' onClick={() => uploadFiles()}>Evaluate Data</button>
                <button className='standardButton' onClick={() => props.clearTable()}>Clear Table</button>
            </div>  
            <div>
            </div>
            {popupState ? <TaxonomyConfigurationPopup toggle={togglePopup} files={uploadedFiles} filesTaxo={filesTaxonomy}/> : null}
        </div>
    )
}

export default UploadSection;