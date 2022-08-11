/**
 * creates a tab-delimited csv file with two columns. one with a name of a file, a second with the relevant taxonomic data
 */

const createSampleTaxoCSVList = (data) => {
    const header = "fileName\tclassification\tsuperkingdom\tkingdom\tphylum\tclass\torder\tfamily\tgenus\tspecies\n"
    var body = header

    for(var i in data){
        let taxoArray = data[i].taxoArray
        let fileName = data[i].fileName
        let classification = fileName
        let superKingdom = taxoArray[0]
        let kingdom = taxoArray[1]
        let phylum = taxoArray[2]
        let taxoClass = taxoArray[3]
        let order = taxoArray[4]
        let family = taxoArray[5]
        let genus = taxoArray[6]
        let species = taxoArray[7]

        let row = fileName+"\t"+classification+"\t"+superKingdom+"\t"+kingdom+"\t"+phylum+"\t"+taxoClass+"\t"+order+"\t"+family+"\t"+genus+"\t"+species
        let endOfLine = "\n"

        //if(i<data.length-1){
            row+=endOfLine
        //}
        body+=row
    }

    return body
}

export default createSampleTaxoCSVList