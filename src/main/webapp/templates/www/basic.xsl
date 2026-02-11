{include file="header.tpl" title=$title navStyle="nav2" admin=$admin}

{if $admin}
<div id="admin_panel">
<button onclick="openPageEditDialog({$id});">Edit Page</button>
</div>
{/if}

<!-- Main Page Content : BEGIN -->
<div id="content">
{$content}
</div>
	
<!-- Page Footer -->
<!-- Page Footer -->
{include file="footer.tpl"}