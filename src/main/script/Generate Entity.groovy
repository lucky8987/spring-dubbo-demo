import com.intellij.database.model.DasTable
import com.intellij.database.model.ObjectKind
import com.intellij.database.util.Case
import com.intellij.database.util.DasUtil

/*
 * Available context bindings:
 *   SELECTION   Iterable<DasObject>
 *   PROJECT     project
 *   FILES       files helper
 */

packageName = "com.example.demo.dao.entity;"
typeMapping = [
        (~/(?i)bigint|int unsigned/)           : "Long",
        (~/(?i)byte/)                          : "Boolean",
        (~/(?i)int/)                           : "Integer",
        (~/(?i)enum/)                          : "Enum",
        (~/(?i)float|double|decimal|real/)     : "Double",
        (~/(?i)datetime|time|timestamp|date/)  : "java.util.Date",
        (~/(?i)/)                              : "String"
]

FILES.chooseDirectoryAndSave("Choose directory", "Choose where to store generated files") { dir ->
    SELECTION.filter { it instanceof DasTable && it.getKind() == ObjectKind.TABLE }.each { generate(it, dir) }
}

def generate(table, dir) {
    def className = javaName(table.getName(), true)
    def fields = calcFields(table)
    new File(dir, className + ".java").withPrintWriter { out -> generate(out, dir, className, table, fields) }
}

def generate(out, dir, className, table, fields) {
    out.println "package $packageName"
    out.println ""
    out.println "/**"
    out.println " * Table: ${table.name}"
    out.println " * Desc: ${table.comment}实体类"
    out.println " */"
    out.println "public class $className {"
    out.println ""
    fields.each() {
        if (it.comment != null) out.println "    /** ${it.comment} */"
        if (it.annos != "") out.println "    ${it.annos}"
        out.println "    private ${it.type} ${it.name};"
        out.println ""
    }
    out.println ""
    fields.each() {
        out.println ""
        out.println "    public ${it.type} get${it.name.capitalize()}() {"
        out.println "      return ${it.name};"
        out.println "    }"
        out.println ""
        out.println "    public void set${it.name.capitalize()}(${it.type} ${it.name}) {"
        out.println "      this.${it.name} = ${it.name};"
        out.println "    }"
        out.println ""
    }
    out.println "}"
}

def calcFields(table) {
    DasUtil.getColumns(table).reduce([]) { fields, col ->
        def spec = Case.LOWER.apply(col.getDataType().getSpecification())
        def typeStr = typeMapping.find { p, t -> p.matcher(spec).find() }.value
        fields += [[
                           name : javaName(col.getName(), false),
                           type : typeStr,
                           annos: "",
                           comment: col.getComment(),
                           dataType  : spec
                   ]]
    }
}

def javaName(str, capitalize) {
    def s = com.intellij.psi.codeStyle.NameUtil.splitNameIntoWords(str)
            .collect { Case.LOWER.apply(it).capitalize() }
            .join("")
            .replaceAll(/[^\p{javaJavaIdentifierPart}[_]]/, "_")
    capitalize || s.length() == 1? s : Case.LOWER.apply(s[0]) + s[1..-1]
}
