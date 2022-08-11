import React from 'react'
import ModelRow from './ModelRow'
import './tableStyles.css'

import Collapse from '@mui/material/Collapse';
import Table from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
import TableRow from '@mui/material/TableRow';
import TableCell from '@mui/material/TableCell';
import TableHead from '@mui/material/TableHead';
import KeyboardArrowDownIcon from '@mui/icons-material/KeyboardArrowDown';
import KeyboardArrowUpIcon from '@mui/icons-material/KeyboardArrowUp';
import Box from '@mui/material/Box'
import ArrowUpwardIcon from '@mui/icons-material/ArrowUpward';
import ArrowDownwardIcon from '@mui/icons-material/ArrowDownward';
import CompareArrowsIcon from '@mui/icons-material/CompareArrows';

import SearchBar from 'material-ui-search-bar'

/*
* renders all the data relating to an uploaded experimental data file
* this includes the results of all tested models, the found reactions etc.
*/
export default function SampleRow(props) {
    const sampleData = props.sample
    const lastTaxEntry = findLastTaxEntry(props.sample.sampleTaxo)
    const [sampleOpen, setSampleOpen] = React.useState(false)
    const [sortConfig, setSortConfig] = React.useState({ key: 'taxoScore', direction: 'descending' })
    const [filteredResults, setFilteredResults] = React.useState(sampleData.testResults)
    const [searched, setSearched] = React.useState("");

    // filtering and sorting -> feed the filtered list into the sorting method, so the filtered results get sorted
    const requestSearch = (searchedValue) => {
        const filtered = [...sampleData.testResults].filter((row) => {
            if (row.modelName.toLowerCase().includes(searchedValue.toLowerCase()) || row.modelClassification.toLowerCase().includes(searchedValue.toLowerCase())) {
                return true
            }
            return false
        })
        setFilteredResults(filtered)
    }

    const cancelSearch = () => {
        setSearched("")
        requestSearch(searched)
    }

    const sortedResults = React.useMemo(() => {
        let sortedTestResults = [...filteredResults]
        if (sortConfig !== null) {
            sortedTestResults.sort((a, b) => {
                if (a[sortConfig.key] < b[sortConfig.key]) {
                    return sortConfig.direction === 'ascending' ? -1 : 1;
                }
                if (a[sortConfig.key] > b[sortConfig.key]) {
                    return sortConfig.direction === 'ascending' ? 1 : -1;
                }
                return 0;
            });
        }
        return sortedTestResults;
    }, [sortConfig, filteredResults]);

    const requestSort = key => {
        let direction = 'descending';
        if (sortConfig.key === key && sortConfig.direction === 'descending') {
            direction = 'ascending';
        }
        setSortConfig({ key, direction })
    }

    const checkSortStatus = ((key) => {
        if (sortConfig.key === key) {
            if (sortConfig.direction === 'ascending') {
                return (<ArrowUpwardIcon fontSize='small' />)
            }
            if (sortConfig.direction === 'descending') {
                return (<ArrowDownwardIcon fontSize='small' />)
            }
        }
        return (<CompareArrowsIcon fontSize='small' className='hiddenIcon' />)
    })

    // find the last taxonomic entry for the samples
    function findLastTaxEntry() {
        const taxData = arguments[0]
        var taxRanks = [taxData.SuperKingdom, taxData.Kingdom, taxData.Phylum, taxData.Class, taxData.Order, taxData.Family, taxData.Genus, taxData.Species]

        const ranks = ["SuperKingdom", "Kingdom", "Phylum", "Class", "Order", "Family", "Genus", "Species"]
        var lastRank = ""
        if (taxRanks[7] === "" || taxRanks[7] === "UNKNOWN") {
            for (var i in taxRanks) {
                if (taxRanks[i] === "") {
                    lastRank = ranks[i - 1] + " " + taxRanks[i - 1]
                    break
                }
            }
        }
        else {
            lastRank = ranks[7] + " " + taxRanks[7]
        }
        return lastRank
    }

    // find the names of the best fitting model(s) to visually mark them in the list of models
    function bestFitNames() {
        const bestFits = props.sample.bestResults
        var names = bestFits.map(result => <p>{result.modelName}</p>)
        if (bestFits.length > 3) {
            return (
                <div className="tableHead-multiple-bestFits">
                    Too many models, best models marked inside
                </div>
            )
        } else {
            return (
                <div>
                    {names}
                </div>
            )
        }
    }

    function checkModelStatus(status){
        if(status===true){
            return props.getUploadedCustomModels
        }
        else{return null}
    }

    return (
        <React.Fragment>
            <TableRow key="tableSampleInfos" className="table-sample-infos">
                <TableCell
                    onClick={() => setSampleOpen(!sampleOpen)}>
                    {sampleOpen ? <KeyboardArrowUpIcon size="small" /> : <KeyboardArrowDownIcon size="small" />}
                </TableCell>
                <TableCell>{sampleData.sampleName}</TableCell>
                <TableCell>{lastTaxEntry}</TableCell>
                <TableCell>{sampleData.numProteinsWithEC} of {sampleData.numSampleProteins} total</TableCell>
                <TableCell>{bestFitNames()}</TableCell>
                <TableCell>
                    <SearchBar
                        className='searchBar'
                        value={searched}
                        onChange={(searchedValue) => requestSearch(searchedValue)}
                        onCancelSearch={() => cancelSearch()}
                        placeholder='search modelID or classification'
                    />
                </TableCell>
            </TableRow>
            <TableRow key="tableCollapseRowSampleData">
                <TableCell style={{ paddingBottom: 0, paddingTop: 0 }} colSpan={6}>
                    <Collapse in={sampleOpen} timeout="auto" unmountOnExit sx={{ maxHeight: 800, overflow: 'auto' }}>
                        <Box>
                            <Table stickyHeader size="small">
                                <TableHead>
                                    <TableRow key="tableModelHead">
                                        <TableCell />
                                        <TableCell onClick={() => requestSort('modelName')} align='left'>
                                            <div className='tableHeadCellWithIcon'>
                                                {checkSortStatus('modelName')}
                                                <p>ModelID</p>
                                            </div>
                                        </TableCell>
                                        <TableCell onClick={() => requestSort('modelClassification')}>
                                            <div className='tableHeadCellWithIcon'>
                                                {checkSortStatus('modelClassification')}
                                                <p>Classification</p>
                                            </div>
                                        </TableCell>
                                        <TableCell onClick={() => requestSort('numReacModelTotal')}>
                                            <div className='tableHeadCellWithIcon'>
                                                {checkSortStatus('numReacModelTotal')}
                                                <p>Reactions in Model(with ECs)</p>
                                            </div>
                                        </TableCell>
                                        <TableCell onClick={() => requestSort('numIdentifiedReactions')}>
                                            <div className='tableHeadCellWithIcon'>
                                                {checkSortStatus('numIdentifiedReactions')}
                                                <p>Reactions identified</p>
                                            </div>
                                        </TableCell>
                                        <TableCell onClick={() => requestSort('intersectionPercModelEC')}>
                                            <div className='tableHeadCellWithIcon'>
                                                {checkSortStatus('intersectionPercModelEC')}
                                                <p>% of Reactions with EC found</p>
                                            </div>
                                        </TableCell>
                                        <TableCell onClick={() => requestSort('intersectionPercModelTotal')}>
                                            <div className='tableHeadCellWithIcon'>
                                                {checkSortStatus('intersectionPercModelTotal')}
                                                <p>% of total Reactions found</p>
                                            </div>
                                        </TableCell>
                                        <TableCell onClick={() => requestSort('numHitsSample')}>
                                            <div className='tableHeadCellWithIcon'>
                                                {checkSortStatus('numHitsSample')}
                                                <p>Proteins assigned </p>
                                            </div>
                                        </TableCell>
                                        <TableCell onClick={() => requestSort('intersectionPercSample')}>
                                            <div className='tableHeadCellWithIcon'>
                                                {checkSortStatus('intersectionPercSample')}
                                                <p>% of Proteins with EC</p>
                                            </div>
                                        </TableCell>
                                        <TableCell onClick={() => requestSort('taxoScore')}>
                                            <div className='tableHeadCellWithIcon'>
                                                {checkSortStatus('taxoScore')}
                                                <p>TaxoScore</p>
                                            </div>
                                        </TableCell>
                                        <TableCell>TaxLastCommon</TableCell>
                                    </TableRow>
                                </TableHead>
                                <TableBody>
                                    {sortedResults.map(modelResultData =>
                                        <ModelRow
                                            key={Math.random().toString(36).substr(2, 9)}
                                            modelResultData={modelResultData}
                                            bestResults={sampleData.bestResults}
                                            sampleName={sampleData.sampleName}
                                            getUploadedCustomModels={checkModelStatus(modelResultData.customModel)}
                                        />)}
                                </TableBody>
                            </Table>
                        </Box>
                    </Collapse>
                </TableCell>
            </TableRow>
        </React.Fragment>
    )
}