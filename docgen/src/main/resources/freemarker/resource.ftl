---
title: "${resourceDef.description}"
linkTitle: "${resourceDef.description}"
description: >
  All about resource ${resourceDef.ref}.
---

<#macro propertyDefinitionsList propertyDefs spaces=0>
  <#list propertyDefs as propertyDef>
<#if spaces gt 0>${''?left_pad(spaces, ' ')}</#if>* **${propertyDef.name}**
(`${propertyDef.propertyType}${propertyDef.isMultiValued()?string('[]', '')}`):
${propertyDef.description}.
    <#if propertyDef.exampleValues != "">
Example values: `${propertyDef.exampleValues}`
    </#if>
    <#if propertyDef.propertyType.toString() == "nested">
<@propertyDefinitionsList propertyDefs=propertyDef.properties spaces=spaces + 4/>
    </#if>
  </#list>
</#macro>

<#macro associationDefinitionsList associationDefs>
  <#list associationDefs as associationDef>
* **${associationDef.name}**
(*${associationDef.defRef}*${associationDef.isMany()?string('[]', '')}):
${associationDef.description}
  </#list>
</#macro>

<#if resourceDef.properties?has_content>
## Properties

<@propertyDefinitionsList propertyDefs=resourceDef.properties/>
</#if>

<#if resourceDef.associations?has_content>
## Associations

<@associationDefinitionsList associationDefs=resourceDef.associations/>
</#if>