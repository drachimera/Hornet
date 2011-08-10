package biopipes.databases

import com.tinkerpop.pipes.AbstractPipe

/**
 * User: qvh
 * Date: 7/22/11
 * Time: 12:37 PM
 * To change this template use File | Settings | File Templates.
 */
//class MINiML <S> extends AbstractPipe<S, S> {

    def xmlFile = "/Users/qvh/jgigeo/GSE13543_family.xml"
    def xml = new XmlParser().parse(xmlFile)
    xml.breadthFirst().each{println it.name().toString() } //.collect{ println it }

/*
RNAseq_experiment

The data file will contain one row for each RNAseq experiment. Each row consists of the following tab-delimited fields:

Name	        Data type	Can be null?	Key?	Description         Availible?
exp_oid	        int	            No	    PK	    Unique experiment ID            no
project_name	Varchar(1000)			        Project name                    no      e.g. B. anthracis transcriptome analysis using Illumina and SOLiD sequencing
exp_name	    Varchar(1000)			        Experiment name
description	    Varchar(4000)			        description
ext_accession	Varchar(255)			        External accession
exp_date	    Varchar(255)			        Experiment date
exp_contact	    Varchar(1000)			        Contact information
method_type	    Varchar(1000)			        method
add_date	    date			                Add date
notes	        Varchar(4000)			        notes
*/

/*
RNAseq_experiment_sop

The data file contains additional (tag, value) pair information for RNAseq experiment. Each RNAseq experiment can have zero to many rows of data.
Name	Data type	Can be null?	Key?	Description
exp_oid	    int	            No	    FK	    experiment ID
tag	        Varchar(255)			        Project name
value	    Varchar(4000)			        Experiment name

 */

//}

/*
def xmlFile = "/Users/qvh/jgigeo/GSE13543_family.xml"
def xmlns="http://www.ncbi.nlm.nih.gov/projects/geo/info/MINiML"


def xml = new XmlParser().parse(xmlFile)
xml.attributes().each{k,v->
  println "-" * 15
  println k
  println v
}

xml.depthFirst().collect{ println it.value }

//xml.each{
//  println it//.text()
//}

//if(a1.length() > 0){
//  def zip = a1
//  def url = baseUrl + "?p=" + zip
//  def xml = url.toURL().text
//
//  def rss = new XmlSlurper().parseText(xml)
//  println rss.channel.title
//  println "Sunrise: ${rss.channel.astronomy.@sunrise}"
//  println "Sunset: ${rss.channel.astronomy.@sunset}"
//  println "Currently:"
//  println "\t" + rss.channel.item.condition.@date
//  println "\t" + rss.channel.item.condition.@temp
//  println "\t" + rss.channel.item.condition.@text
//}else{
//  println "USAGE: weather zipcode"
//}
*/