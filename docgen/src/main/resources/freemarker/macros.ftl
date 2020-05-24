<#macro propertyDefinitionsList propertyDefs spaces=0>
  <#list propertyDefs as propertyDef>
<#if spaces gt 0>${''?left_pad(spaces, ' ')}</#if>* **${propertyDef.name}** (*${propertyDef.propertyType}${propertyDef.isMultiValued()?string('[]', '')}*):
${propertyDef.description}<#if propertyDef.exampleValues != ""> *(Example: ${propertyDef.exampleValues})</#if>
  <#if propertyDef.propertyType.toString() == "nested">
  <@propertyDefinitionsList propertyDefs=propertyDef.properties spaces=spaces + 4/>
  </#if>
  </#list>
</#macro>

<#macro associationDefinitionsList associationDefs>
  <#list associationDefs as associationDef>
* **${associationDef.name}** (*${associationDef.resourceDefRef}*): ${associationDef.description}
  </#list>
</#macro>