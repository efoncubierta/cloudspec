# CloudSpec Provider: ${provider.description}

## Resource definitions

<#list provider.resourceDefs as resourceDef>
* [${resourceDef.ref}](${resourceDef.ref.groupName}/${resourceDef.ref.resourceName}.md): ${resourceDef.description}
</#list>