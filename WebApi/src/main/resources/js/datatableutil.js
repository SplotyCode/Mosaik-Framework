function reloadDataTables() {
    $(".data-table-auto-load").each(function() {
        $(this).DataTable();
    }
}

$(document).ready(function() {
    reloadDataTables();
});