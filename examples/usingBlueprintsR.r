Hi there,

The other day I was wondering how easy it would be to use my Blueprints graphdb in R. And here's how easy it is. Hopefully someone will find this useful. :-) Later on, I might do some R bindings for Blueprints, if I have the time. Meanwhile, check rJava documentation to understand how to map your Java code to rJava syntax.

library(rJava)

.jinit()

lib.dir <- "/Replace/With/JAR/Directory"
graphdb.path <- "Your/GraphDB/Here"
vertex.property.name <- "justAPropertyToTest"

.jaddClassPath( paste(lib.dir, "blueprints-core-0.6.jar", sep="/") )
.jaddClassPath( paste(lib.dir, "blueprints-neo4j-graph-0.6.jar", sep="/") )
.jaddClassPath( paste(lib.dir, "neo4j-kernel-1.3.M05.jar", sep="/") )
.jaddClassPath( paste(lib.dir, "neo4j-management-1.3.M05.jar", sep="/") )
.jaddClassPath( paste(lib.dir, "geronimo-jta_1.1_spec-1.1.1.jar", sep="/") )
.jaddClassPath( paste(lib.dir, "neo4j-lucene-index-1.3.M05.jar", sep="/") )
.jaddClassPath( paste(lib.dir, "org.apache.servicemix.bundles.lucene-3.0.1_2.jar", sep="/") )

attach(javaImport("com.tinkerpop.blueprints.pgm.impls.neo4j"))

g <- new(Neo4jGraph, graphdb.path)
it <- g$getVertices()

c <- 0
while (.jrcall(it, "hasNext"))
{
    v <- .jrcall(it, "next")
    print(v$getProperty(vertex.property.name))
    
    c <- c + 1
    if (c > 10)
    {
        break
    }
}

g$shutdown()
