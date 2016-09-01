Bonjour,

Ci-dessous la liste des participants pour la session de ${date} :
<ul>
    <#list players as player>
    <li>${player}</li>
    </#list>
</ul>

<#if subs?has_content>
Remplaçants :
<ul>
    <#list subs as sub>
        <li>${sub}</li>
    </#list>
<ul>
</#if>

<a href="https://pes5.herokuapp.com/">https://pes5.herokuapp.com/</a>