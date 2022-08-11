import React from 'react';
import SampleRow from './SampleRow';
import '../../style.css';
import './tableStyles.css'

import Table from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
import TableRow from'@mui/material/TableRow';
import TableCell from '@mui/material/TableCell';
import TableContainer from '@mui/material/TableContainer';
import TableHead from '@mui/material/TableHead';
import Paper from '@mui/material/Paper';

/**
 * renders a table with all the results
 */
export default function MuiTable(props){

    //const resultData = props.resultData

    return(
        <Paper sx={{width:1800, margin:'auto'}}>
            <TableContainer>
                <Table>
                    <TableHead>
                        <TableRow key={"tableHead"}>
                            <TableCell/>
                            <TableCell>Sample Name</TableCell>
                            <TableCell>Taxonomy</TableCell>
                            <TableCell># of Proteins with ECs</TableCell>
                            <TableCell>Best Fitting Model(s)</TableCell>
                            <TableCell></TableCell>
                        </TableRow>
                    </TableHead>
                    <TableBody>
                        {props.resultData.map(sample =><SampleRow sample={sample}  getUploadedCustomModels={props.getUploadedCustomModels}/>)}
                    </TableBody>
                </Table>
            </TableContainer>
        </Paper>
    )
}