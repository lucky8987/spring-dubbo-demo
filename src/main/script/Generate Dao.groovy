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

packageName = "com.example.demo.dao;"
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
    def entityName = javaName(table.getName(), true)
    def className = entityName + "Dao"
    className = className.startsWith("Tn") ? className.replaceFirst("Tn","") : className
    def fields = calcFields(table)
    new File(dir, className + ".java").withPrintWriter { out -> generate(out, className, entityName, table, fields) }
}

def generate(out, className, entityName, table, fields) {
    out.println "package $packageName"
    out.println ""
    out.println "import org.apache.ibatis.annotations.*;"
    out.println "import java.util.Collection;"
    out.println "import java.util.List;"
    out.println ""
    out.println "/**"
    out.println " * mybatis注解实现：http://www.mybatis.org/mybatis-3/zh/java-api.html"
    out.println " */"
    out.println "@Mapper"
    out.println "public interface $className {"
    out.println ""
    out.println "    String columns = \"${fields['name'].join(',')}\";"
    out.println ""
    out.println "    /**"
    out.println "     * 新增 (该方法自动生成)"
    out.println "     * @param entity 插入对象"
    out.println "     * @return 受影响行数"
    out.println "     */"
    out.print "    @Insert(\"Insert Into ${table.name}(\"+columns+\")"
    out.println "  VALUES (#{${fields['javaName'].join("},#{")}})\")"
    out.println "    int insert($entityName entity);"
    out.println ""
    out.println "    /**"
    out.println "     * 新增并返回id到当前entity (该方法自动生成)"
    out.println "     * @param entity 插入对象"
    out.println "     * @return 受影响行数"
    out.println "     */"
    out.print "    @Insert(\"Insert Into ${table.name}(\"+columns+\")"
    out.println "  Values (#{${fields['javaName'].join("},#{")}})\")"
    out.println "    @SelectKey(statement = \"Select LAST_INSERT_ID()\", keyProperty = \"id\", before = false, resultType = int.class)"
    out.println "    int insertSetId($entityName entity);"
    out.println ""
    out.println "    /**"
    out.println "     * 批量插入 (该方法自动生成)"
    out.println "     * @param list 插入对象"
    out.println "     * @return 受影响行数"
    out.println "     */"
    out.println "    @Insert(\"<script> Insert Into ${table.name}(\"+columns+\") VALUES \" +"
    out.println "       \"<foreach collection='list' item='item' index='index' separator=','> \" +"
    out.println "           \" (#{item.${fields['javaName'].join("},#{item.")}}) \" +"
    out.println "        \"</foreach> </script>\")"
    out.println "    int batchInsert(Collection<$entityName> list);"
    out.println ""
    out.println "    /**"
    out.println "     * 主键查询记录 (该方法自动生成)"
    out.println "     * @param id 主键"
    out.println "     * @return $entityName 实体对象"
    out.println "     */"
    out.println "    @Select(\"Select * from ${table.name} where id = #{id}\")"
    out.println "    $entityName selectById(int id);"
    out.println ""
    out.println "    /**"
    out.println "     * 批量查询记录 (该方法自动生成)"
    out.println "     * @param ids 主键集合"
    out.println "     * @return List<$entityName> 实体对象集合"
    out.println "     */"
    out.println "    @Select(\"<script> select * from ${table.name} where id in  \"+"
    out.println "                   \" <foreach collection='ids' item='id' index='index' open='(' separator=',' close=')'> \" +"
    out.println        "                   \" #{id} \" +"
    out.println "                   \" </foreach> </script>\")"
    out.println "    List<$entityName> selectByIds(@Param(\"ids\") Collection<Integer> ids);"
    out.println ""
    out.println "    /**"
    out.println "     * 删除 (该方法自动生成)"
    out.println "     * @param id 主键"
    out.println "     * @return 受影响行数"
    out.println "     */"
    out.println "    @Delete(\"Delete from ${table.name} where id = #{id}\")"
    out.println "    int delete(int id);"
    out.println "}"
}

def calcFields(table) {
    DasUtil.getColumns(table).reduce([]) { fields, col ->
        def spec = Case.LOWER.apply(col.getDataType().getSpecification())
        def typeStr = typeMapping.find { p, t -> p.matcher(spec).find() }.value
        fields += [[
                           name : col.getName(),
                           javaName : javaName(col.getName(), false)
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
