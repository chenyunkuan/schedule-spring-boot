<html>
<head>
    <title>国家(地区)信息</title>
    <link href="${request.contextPath}/static/css/style.css" rel="stylesheet" type="text/css"/>
</head>
<style>
    input[type="text"]{width:350px;}
    input[readonly="readonly"] {background : #dedede;
        border: 0px;}
</style>
<body style="margin-top:50px;overflow: hidden;">
<form action="${request.contextPath}/job/save" method="post">
    <input type="hidden" name="id" value="<#if job.id??>${job.id}</#if>"/>
    <table class="gridtable" style="width:1366px;">
        <tr>
            <th colspan="5">job信息 - [<a href="${request.contextPath}/job">返回</a>]</th>
        </tr>
        <tr>
            <th>jobID：</th>
            <td><input type="text" name="idsss" readonly="readonly" value="<#if job.id??>${job.id}</#if>"/>
            </td>
            <th>job名称：</th>
            <td><input type="text" name="jobName" value="<#if job.jobName??>${job.jobName}</#if>"/>
            </td>
        <tr>
            <th>group名称：</th>
            <td><input type="text" name="groupName" value="<#if job.groupName??>${job.groupName}</#if>"/>
            </td>
            <th>触发器名字：</th>
            <td><input type="text" name="triggerName" value="<#if job.triggerName??>${job.triggerName}</#if>"/>
            </td>
        </tr>
        <tr>
            <th>时间表达式：</th>
            <td><input type="text" name="cronExpression" value="<#if job.cronExpression??>${job.cronExpression}</#if>"/>
            </td>
            <th>访问的url：</th>
            <td><input type="text" name="url" value="<#if job.url??>${job.url}</#if>"/>
            </td>
        </tr>
        <tr>
            <th>运行状态：</th>
            <td><input type="text" name="status" readonly="readonly" value="<#if job.status??>${job.status}</#if>"/>
            </td>
            <th></th>
            <td><input type="submit" value="保存"/></td>
        </tr>

    <#if msg??>
        <tr style="color:#00ba00;">
            <th colspan="5">${msg}</th>
        </tr>
    </#if>
    </table>
</form>
</body>
</html>
