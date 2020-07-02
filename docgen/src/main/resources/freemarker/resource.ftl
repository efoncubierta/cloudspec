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
    <#if propertyDef.allowedValues?has_content>
Allowed values: `${propertyDef.allowedValues?join("`, `")}`
    </#if>
    <#if propertyDef.propertyType.toString() == "nested">
<@propertyDefinitionsList propertyDefs=propertyDef.properties spaces=spaces + 4/>
<@associationDefinitionsList associationDefs=propertyDef.associations spaces=spaces + 4/>
    </#if>
  </#list>
</#macro>

<#macro associationDefinitionsList associationDefs spaces=0>
  <#list associationDefs as associationDef>
<#if spaces gt 0>${''?left_pad(spaces, ' ')}</#if>* **&gt;${associationDef.name}**
([${associationDef.defRef}](../../${associationDef.defRef.providerName}/${associationDef.defRef.groupName}_${associationDef.defRef.resourceName})${associationDef.isMany()?string('[]', '')}):
${associationDef.description}
  </#list>
</#macro>

## Members
<@propertyDefinitionsList propertyDefs=resourceDef.properties/>
<@associationDefinitionsList associationDefs=resourceDef.associations/>