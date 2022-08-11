/* 
creates a tab-delimited csv file in the same style as the ideal format of an experimental sample file
and makes it available to the caller 
*/

const createCSVProteinFile = (data) => {
    const header = "id\tko\tec\tsuperKingdom\tphylum\tclass\torder\tfamily\tgenus\tspecies\tdescription\tsample1\tsample2\n"
    var body = header

    for(var i in data){
        let koString = ""
        if(data[i].konumbers.length>0){
            for(let x in data[i].konumbers){
                if(x>0){
                    koString+="\\|"+data[i].konumbers[x]
                }else{
                    koString=data[i].konumbers[x]
                }
            }
        }
        let ecString = ""
        if(data[i].ecNumbers.length>0){
            for(let x in data[i].ecNumbers){
                if(x>0){
                    ecString+="|"+data[i].ecNumbers[x]
                }else{
                    ecString=data[i].ecNumbers[x]
                }
            }
        }

        let line=data[i].id+"\t"+koString+"\t"+ecString+"\t"+data[i].taxonomy.SuperKingdom+"\t"+data[i].taxonomy.Phylum+"\t"+data[i].taxonomy.Class+"\t"+data[i].taxonomy.Order+"\t"+data[i].taxonomy.Family+"\t"+data[i].taxonomy.Genus+"\t"+data[i].taxonomy.Species+"\t"+data[i].description+"\t"+" "+"\t"+" "
        let endOfLine = "\n"
        if(i<data.length){
            line+=endOfLine
        }
        body+=line
    }
    return body
}

export default createCSVProteinFile