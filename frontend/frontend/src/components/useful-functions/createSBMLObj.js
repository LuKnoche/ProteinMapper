import objectToXML from 'object-to-xml';
/*
takes the lists of compartments,species and reactions and a filename and creates an SBML-like object
out of that data which then gets converted into an actual xml-object
returns xml-object
*/
const createSBMLObj = (listOfCompartments, listOfSpecies, listOfReactions, filename) => {
    var compartment = []
    var species = []
    var reaction = []
    if(listOfCompartments.length>0){compartment = makeListOfCompartments(listOfCompartments)} 
    if(listOfSpecies.length>0){species = makeListOfSpecies(listOfSpecies)}
    if(listOfReactions.length>0){reaction = makeListOfReactions(listOfReactions)}

    const sbmlObj = {
        "?xml version=\"1.0\" encoding=\"UTF-8\"?":null,
        sbml: {
            '@':{
                xmlns: "http://www.sbml.org/sbml/level3/version1/core",
                level: "3",
                version:"1",
                sboTerm:"SBO:0000624",
            },
            model:{
                '@':{id:filename,
                },
                '#':{
                    // listOfUnitDefinitions: {
                    //     '@': {
                    //         id: "mmol_per_gDW_per_hr"
                    //     },
                    //     '#': {
                    //         listOfUnits:{unit: [
                    //             {'@':{
                    //                 kind: "mole",
                    //                 scale: "-3",
                    //                 multiplier: "1",
                    //                 exponent: "1"
                    //             }},
                    //             {'@':{
                    //                 kind: "gram",
                    //                 scale: "0",
                    //                 multiplier: "1",
                    //                 exponent: "-1"
                    //             }},
                    //             {'@':{
                    //                 kind: "second",
                    //                 scale: "0",
                    //                 multiplier: "1",
                    //                 exponent: "-1"
                    //             }}
                    //         ]}
                    //     }
                    // },
                    listOfCompartments:{compartment},
                    listOfSpecies: {species},
                    listOfReactions:{reaction}
                }
            }
        }
    }
    const sbml = new Array(objectToXML(sbmlObj).trim())
return sbml
}

const makeListOfCompartments = (compartmentsRaw) => {
    return compartmentsRaw.map(compartment =>{
        return {
            '@': {
                id: compartment.id,
                name: compartment.name,
                constant: compartment.constant,
            }
        }
    })
}

const makeListOfParameters = (parametersRaw) => {
    return parametersRaw.map(parameter => {
        return {
            '@': {
                id: parameter.id,
                value: parameter.value,
                constant: parameter.constant.toString(),
                sboTerm: parameter.sboTerm,
                units: parameter.units
            },
        }
    })
}

const makeListOfSpecies = (speciesRaw) => {
    return speciesRaw.map(species => {
        return {
            '@': {
                id: species.id,
                constant: species.constant.toString(),
                boundaryCondition: species.boundaryCondition.toString(),
                hasOnlySubstanceUnits: species.hasOnlySubstanceUnits.toString(),
                name: species.name,
                metaid: species.metaId,
                sboTerm: species.sboTerm,
                compartment: species.compartment,

            },
            '#': {
                'sbml:annotation': {
                    '@': {
                        'xmlns:sbml': "http://www.sbml.org/sbml/level3/version1/core"
                    },
                    '#': {
                        'rdf:RDF': {
                            '@': {
                                'xmlns:rdf': "http://www.w3.org/1999/02/22-rdf-syntax-ns#"
                            },
                            '#': {
                                'rdf:Description': {
                                    '@': {
                                        'rdf:about': "#" + species.id
                                    },
                                    '#': {
                                        'bqbiol:is': {
                                            '@': {
                                                'xmlns:bqbiol': "http://biomodels.net/biology-qualifiers/"
                                            },
                                            '#': {
                                                'rdf:Bag': {
                                                    'rdf:li': species.identifiers.map(identifier => {
                                                        return {
                                                            '@': {
                                                                'rdf:resource': identifier
                                                            }
                                                        }
                                                    })
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    })
}

const makeListOfReactions = (reactionsRaw) => {
    return reactionsRaw.map(reaction => {
        // const gpaList = makeGeneProductAssociation(reaction.geneProductAssociation)
        return {
            '@': {
                id: reaction.id,
                fast: reaction.fast.toString(),
                reversible: reaction.reversible.toString(),
                name: reaction.name,
                metaid: reaction.metaId,
                sboTerm: reaction.sboTerm,
                //'fbc:upperFluxBound': reaction.fbcUpperFluxBound,
                //'fbc:lowerFluxBound': reaction.fbcLowerFluxBound
            },
            '#': {
                'sbml:annotation': {
                    '@': {
                        'xmlns:sbml': "http://www.sbml.org/sbml/level3/version1/core"
                    },
                    '#': {
                        'rdf:RDF': {
                            '@': {
                                'xmlns:rdf': "http://www.w3.org/1999/02/22-rdf-syntax-ns#"
                            },
                            '#': {
                                'rdf:Description': {
                                    '@': {
                                        'rdf:about': "#" + reaction.id
                                    },
                                    '#': {
                                        'bqbiol:is': {
                                            '@': {
                                                'xmlns:bqbiol': "http://biomodels.net/biology-qualifiers/"
                                            },
                                            '#': {
                                                'rdf:Bag': {
                                                    'rdf:li': reaction.identifiers.map(identifier => {
                                                        return {
                                                            '@': {
                                                                'rdf:resource': identifier
                                                            }
                                                        }
                                                    })
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                },
                listOfReactants: {
                    '#': {
                        speciesReference: reaction.listOfReactants.map(reactant => {
                            return {
                                '@': {
                                    species: reactant.species,
                                    stoichiometry: reactant.stoichiometry,
                                    constant: reactant.constant.toString()
                                }
                            }
                        })
                    }
                },
                listOfProducts: {
                    '#': {
                        speciesReference: reaction.listOfProducts.map(product => {
                            return {
                                '@': {
                                    species: product.species,
                                    stoichiometry: product.stoichiometry,
                                    constant: product.constant.toString()
                                }
                            }
                        })
                    }
                },
                /*'fbc:geneProductAssociation': reaction.geneProductAssociation.map(association => {
                    let label = association.type
                    if (label === 'fbc:geneProductRef') {
                        return {
                            'fbc:geneProductRef': {
                                '@': {
                                    'fbc:geneProduct': association.geneProductRef
                                }
                            }
                        }
                    }
                    else if (label === 'fbc:or' || label === 'fbc:and') {
                        let content2 = {
                            'fbc:geneProductRef': [{
                                '@': {
                                    'fbc:geneProduct': "ssdfgsdfgdfg"
                                }
                            },
                            {
                                '@': {
                                    'fbc:geneProduct': "sdfsdfgsdg"
                                }
                            },
                            {
                                '@': {
                                    'fbc:geneProduct': "sdfergg"
                                }
                            }],
                            'fbc:and':[{
                                '#': {'fbc:geneProductRef': [{
                                    '@': {
                                        'fbc:geneProduct': "sdfg"
                                    }
                                },
                                {
                                    '@': {
                                        'fbc:geneProduct': "sdfg"
                                    }
                                },
                                {
                                    '@': {
                                        'fbc:geneProduct': "sdfg"
                                    }
                                }]
                            }
                            },{
                                '#':{'asdf':"hehehehe"}
                            }]
                        }
                            
                           let content = {}
                        return {
                            [label]: {
                                '#': makeGPAContent(association,content)
                            }
                        }
                    }
                })*/
            }
        }
    })
}

function makeGPAContent(association, contentObj){
    let list = association.listOfAssociations
    //contentObj = {...contentObj,...{"key1":'value1'}}
    let gprArray = []
    let andArray = []
    let orArray = []
    for(let i = 0; i < list.length; i++){
        let gpa = list[i]
        if(gpa.type === 'fbc:geneProductRef'){
            let geneProductRef = {
                '@': {
                    'fbc:geneProduct': gpa.geneProductRef
                }
            }
            gprArray.push(geneProductRef)
        }
        if(gpa.type === 'fbc:and'){
            let andContent = {}
            let and = {
                '#': makeGPAContent(gpa,andContent)
            }
            andArray.push(and)
        }
        if(gpa.type === 'fbc:or'){
            let orContent = {}
            let or = {
                '#': makeGPAContent(gpa,orContent)
            }
        }
    }
    if(gprArray.length > 0){
        let gpr = {'fbc:geneProductRef':gprArray}
        contentObj = {...contentObj,...gpr}
    }
    if(andArray.length > 0){
        let andElement = {'fbc:and':andArray}
        contentObj = {...contentObj,...andElement}
    }
    if(orArray.length > 0){
        let orElement = {'fbc:or':orArray}
        contentObj = {...contentObj,...orElement}
    }
    return contentObj
}


const makeListOfGeneProducts = (geneProductsRaw) => {
    return geneProductsRaw.map(geneProduct =>{
        return {
            '@': {
                'fbc:id':geneProduct.id,
                'fbc:label':geneProduct.label,
                'fbc:name':geneProduct.name,
                metaid:geneProduct.metaId,
                sboTerm:geneProduct.sboTerm
            },
            '#': {
                'sbml:annotation': {
                    '@': {
                        'xmlns:sbml':"http://www.sbml.org/sbml/level3/version1/core"
                    },
                    '#':{
                        'rdf:RDF': {
                            '@': {
                                'xmlns:rdf':"http://www.w3.org/1999/02/22-rdf-syntax-ns#"
                            },
                            '#': {
                                'rdf:Description': {
                                    '@': {
                                        'rdf:about': "#" + geneProduct.id
                                    },
                                    '#': {
                                        'bqbiol:is': {
                                            '@': {
                                                'xmlns:bqbiol': "http://biomodels.net/biology-qualifiers/"
                                            },
                                            '#': {
                                                'rdf:Bag': {
                                                    'rdf:li': geneProduct.identifiers.map(identifier => {
                                                        return {
                                                            '@': {
                                                                'rdf:resource': identifier
                                                            }
                                                        }
                                                    })
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    })
}

export default createSBMLObj