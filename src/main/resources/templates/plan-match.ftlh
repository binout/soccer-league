Bonjour,

<p>
Ci-dessous la liste des participants pour la session du ${date} :
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
</p>

<a href="https://pes5.cleverapps.io/">https://pes5.cleverapps.io/</a>
