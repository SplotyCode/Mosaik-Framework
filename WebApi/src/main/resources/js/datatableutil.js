function reloadDataTables() {
    $(".data-table-auto-load").each(function() {
        $(this).DataTable();
    });
}

function removeRow() {
    $(this).closest('tr').remove();
}

$(document).ready(function() {
    reloadDataTables();
});