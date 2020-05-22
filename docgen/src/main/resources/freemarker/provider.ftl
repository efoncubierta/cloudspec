<#include "/macros.ftl">

# CloudSpec Provider: ${provider.name}

## Resource definitions

<#list provider.resourceDefs as resourceDef>
* [${resourceDef.ref}](${resourceDef.ref}): ${resourceDef.description}
</#list>

<#list provider.resourceDefs as resourceDef>
### ${resourceDef.description}

<#if resourceDef.properties?has_content>
**Properties**

<@propertyDefinitionsList propertyDefs=resourceDef.properties/>
</#if>

<#if resourceDef.associations?has_content>
**Associations**

<@associationDefinitionsList associationDefs=resourceDef.associations/>
</#if>

</#list>