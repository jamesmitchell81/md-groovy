import groovy.io.FileType

def files = []

new File(".").eachDirRecurse { dir ->
    dir.eachFileMatch(~/.*.md/) { file ->
        files << file
    }
}

class MdMatcher {
    def patterns = [
        "h1" : ~/#\s([a-zA-Z0-9])/
    ]

    def headers = [
        "h1": /^#\s([a-zA-Z0-9\s]+)/,
        "h2": /^#{2}\s([a-zA-Z0-9\s]+)/,
        "h3": /^#{3}\s([a-zA-Z0-9\s]+)/,
        "h4": /^#{4}\s([a-zA-Z0-9\s]+)/,
        "h5": /^#{5}\s([a-zA-Z0-9\s]+)/,
        "h6": /^#{6}\s([a-zA-Z0-9\s]+)/
    ]

    Boolean isHeader(line) {
        (line =~ /#\s([a-zA-Z0-9\s]+)/).find()
    }

    Boolean isList(line) {
        (line =~ /^([0-9a-zA-Z]+\.\s)|^([*]+\s)+|^([-]+\s)+/)
    }

    Map parseHeader(line) {

    }

    def parse() {

    }
}

markdown = new MdMatcher()

def headers = [
    "h1": /^#\s([a-zA-Z0-9\s]+)/,
    "h2": /^#{2}\s([a-zA-Z0-9\s]+)/,
    "h3": /^#{3}\s([a-zA-Z0-9\s]+)/,
    "h4": /^#{4}\s([a-zA-Z0-9\s]+)/,
    "h5": /^#{5}\s([a-zA-Z0-9\s]+)/,
    "h6": /^#{6}\s([a-zA-Z0-9\s]+)/
]

def lists = [
    "ul": /^([*]+\s)+|^([-]+\s)+/,
    "ol": /^([0-9a-zA-Z]+\.\s)+/
]

def paragraph = /(.)+/
def list /* all */ = /^[0-9.]+\.\s|^[*]+\s|^[-]+\s/

def m
def elements = []

files.each {
    new File(it.path).withInputStream { stream ->
        stream.eachLine { line ->

            // headers
            // is a header.
            if ( markdown.isHeader(line) ) {
                // which header
                headers.each { k, v ->
                    m = line =~ v
                    if (m.find()) {
                        def pair = [:]
                        pair[k] = m[0][1]
                        elements << pair
                    }
                }
            }

            if ( markdown.isList(line) ) {
                lists.each { k, v ->
                    m = line =~ v
                    if (m.find()) {
                        def pair = [:]
                        pair[k] = (line - ~v)
                        elements << pair
                    }
                }
            }


        }
    }
}

elements.each { item ->
    item.each {
        println "$it.key $it.value"
    }
}

// def writer = new StringWriter()  // html is written here by markup builder
// def markup = new groovy.xml.MarkupBuilder(writer)  // the builder

// markup.html{
//     "table" {
//         tr {
//             td(class:"row", "hello world!")
//         }
//     }
// }

// println writer.toString()

/* produces output :
<html>
  <table>
    <tr>
      <td>hello world!</td>
    </tr>
  </table>
</html>
*/

