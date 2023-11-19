import org.yaml.snakeyaml.Yaml 
import java.nio.file.*

def yamlFile = new File('myyaml-2.yaml')

def yamlContent = yamlFile.text 

def yamlData = new Yaml().load(yamlContent) 

// Define the keys to insert
def insertKeys = ['jkcd-in', 'njkcd-in']

// Modify the data structure
yamlData.app.commital = insertKeyAfter(yamlData.app.commital, 'jkcd-in', 'jkcd-in-1')
yamlData.app.'non-commital' = insertKeyAfter(yamlData.app.'non-commital', 'njkcd-in', 'njkcd-in-1')

//Function to insert a key after another in a map.
def insertKeyAfter(map, afterKey, newKey) {
    map.collect { Key, value ->
        if (key == afterKey) {
            [(Key): value, (newKey): value]
        }else{
            [(Key): value]
        }
    }.flatten().inject([:]) { result, entry -> result << entry }
}

//Convert the transformed data back to Yaml
de updatatedYamlContent = new Yaml().dump(yamlData)

//Write the updated YAML content back to file
Files.write(yamlFile.toPAth(), updatedYamlContent.getBytes())

println "Yaml updated successfully."